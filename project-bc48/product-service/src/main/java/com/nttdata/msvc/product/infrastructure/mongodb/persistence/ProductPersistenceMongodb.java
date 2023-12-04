package com.nttdata.msvc.product.infrastructure.mongodb.persistence;

import com.nttdata.msvc.product.domain.exceptions.ConflictException;
import com.nttdata.msvc.product.domain.exceptions.InsufficientFundsException;
import com.nttdata.msvc.product.domain.exceptions.ProductAlreadyExistsException;

import com.nttdata.msvc.product.domain.model.*;
import com.nttdata.msvc.product.domain.persistence.ProductPersistence;
import com.nttdata.msvc.product.infrastructure.api.dtos.*;
import com.nttdata.msvc.product.infrastructure.mongodb.daos.ClientRepository;
import com.nttdata.msvc.product.infrastructure.mongodb.daos.ComissionRepository;
import com.nttdata.msvc.product.infrastructure.mongodb.daos.ProductRepository;
import com.nttdata.msvc.product.infrastructure.mongodb.entities.ClientEntity;
import com.nttdata.msvc.product.infrastructure.mongodb.entities.ProductEntity;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.*;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.webjars.NotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Flow;

import static com.nttdata.msvc.product.utils.Constants.*;


@Repository
@Slf4j
public class ProductPersistenceMongodb implements ProductPersistence {

  private final ProductRepository productRepository;
  private final WebClient.Builder webClientBuilder;
  private final ClientRepository clientRepository;
  private final ComissionRepository comissionRepository;

  public ProductPersistenceMongodb(ProductRepository productRepository, ClientRepository clientRepository, ComissionRepository comissionRepository, WebClient.Builder webClientBuilder) {
    this.productRepository = productRepository;
    this.webClientBuilder = webClientBuilder;
    this.clientRepository = clientRepository;
    this.comissionRepository = comissionRepository;
  }

  @Override
  public Flowable<Product> findAll() {
    return productRepository
      .findAll()
      .map(productEntity -> ProductEntity.toProduct(productEntity));
  }

  @Override
  public Maybe<Product> findById(String productId) {
    return productRepository
      .findById(productId)
      .switchIfEmpty(Maybe.error(new NotFoundException("Product does not exist: " + productId)))
      .map(productEntity -> ProductEntity.toProduct(productEntity));
  }

  @Override
  @Transactional
  public Single<Product> create(Product product) {
    ProductEntity productEntity = ProductEntity.toProductEntity(product);
    return productRepository.save(productEntity)
      .map(productSaved -> ProductEntity.toProduct(productSaved));
  }
//    Flowable<ProductEntity> clientProducts = productRepository.findProductEntitiesByIdClient(product.getIdClient());
//    Single<ClientEntity> clientFounded = Single.fromPublisher(webClientBuilder.build().get().uri("http://CLIENT-SERVICE/clients/" + product.getIdClient()).retrieve().bodyToMono(ClientEntity.class));
//    ProductEntity productEntity = ProductEntity.toProductEntity(product);
//    return clientProducts
//      .isEmpty()
//      .flatMap(isEmpty -> {
//        if (isEmpty) {
//          return clientFounded
//            .flatMap(clientMapped -> {
//              String productType = productEntity.getProductType();
//              if (getProductTypeDescriptions().containsKey(productType))
//                productEntity.setProductTypeDescription(getProductTypeDescriptions().get(productType));
//              if (clientMapped.getClientType().equals(ENTERPRISE) && (productType.equals(CUENTA_AHORRO) || productType.equals(PLAZO_FIJO))) {
//                return Single.error(new ConflictException("This client type cannot have this kind of product"));
//              }
//              return productRepository.save(productEntity).map(productSaved -> ProductEntity.toProduct(productSaved));
//            });
//        } else {
//          return Single.error(new RuntimeException("Not implemented method if clientProducts is not empty"));
//        }
//      });

  @Override
  public Maybe<Product> update(Product product) {
    return productRepository
      .findById(product.getId())
      .switchIfEmpty(Maybe.error(new NotFoundException("Non existent product: " + product.getId())))
      .flatMapSingle(productEntity -> {
        BeanUtils.copyProperties(product, productEntity);
        String productType = productEntity.getProductType();
        if (getProductTypeDescriptions().containsKey(productType))
          productEntity.setProductTypeDescription(getProductTypeDescriptions().get(productType));
        return productRepository.save(productEntity);
      })
      .map(productEntity -> ProductEntity.toProduct(productEntity));
  }

  private Map<String, String> getProductTypeDescriptions() {
    return Map.of(
      CUENTA_AHORRO, CUENTA_AHORRO_DESC,
      CUENTA_CORRIENTE, CUENTA_CORRIENTE_DESC,
      PLAZO_FIJO, PLAZO_FIJO_DESC,
      CREDITO_PERSONAL, CREDITO_PERSONAL_DESC,
      CREDITO_EMPRESARIAL, CREDITO_EMPRESARIAL_DESC,
      TARJETA_CREDITO, TARJETA_CREDITO_DESC
    );
  }

  @Override
  public Maybe<Void> delete(String productId) {
    return productRepository
      .findById(productId)
      .switchIfEmpty(Maybe.error(new NotFoundException("Non existent product: " + productId)))
      .flatMap(productEntity -> productRepository.deleteById(productId).andThen(Maybe.empty()));
  }

  @Override
  public Maybe<ProductResponseDTO> deposit(DepositRequestDTO depositRequestDTO) {
    return Maybe.create(new MaybeOnSubscribe<ProductResponseDTO>() {
      @Override
      public void subscribe(@NonNull MaybeEmitter<ProductResponseDTO> emitter) throws Throwable {
        Maybe<Product> originAccount = productRepository.findById(depositRequestDTO.getIdOriginProduct()).map(productEntity -> ProductEntity.toProduct(productEntity));

        Maybe<Product> destinationAccount = productRepository.findById(depositRequestDTO.getIdOriginProduct()).map(productEntity -> ProductEntity.toProduct(productEntity));

        Maybe.zip(originAccount, destinationAccount, (origin, destination) -> {

          double currentAvailableTransactions = Double.parseDouble(origin.getAvailableTransactionsWithoutCost()) - 1;
          origin.setAvailableTransactionsWithoutCost(String.valueOf(currentAvailableTransactions));

          double originNewAvailableBalance = Double.parseDouble(origin.getAvailableBalance()) - Double.parseDouble(depositRequestDTO.getAmount());
          if (currentAvailableTransactions <= 0) {
            originNewAvailableBalance = originNewAvailableBalance - 14.90;
            origin.setAvailableBalance(String.valueOf(originNewAvailableBalance));
            Comission comission = Comission
              .builder()
              .idProduct(origin.getId())
              .amount(String.valueOf(14.90))
              .idClient(origin.getIdClient())
              .build();
            comissionRepository.save(comission);
          } else {
            origin.setAvailableBalance(String.valueOf(originNewAvailableBalance));
          }


          double destinationNewAvailableBalance = Double.parseDouble(destination.getAvailableBalance()) + Double.parseDouble(depositRequestDTO.getAmount());
          destination.setAvailableBalance(String.valueOf(destinationNewAvailableBalance));

          ProductEntity productOrigin = ProductEntity.toProductEntity(origin);
          ProductEntity productDestination = ProductEntity.toProductEntity(destination);
          productRepository.save(productOrigin);
          productRepository.save(productDestination);

          return new ProductResponseDTO(DEPOSIT_SUCCESSFULLY, STATUS_OK);
        }).subscribe(
          // On success
          responseDTO -> {
            emitter.onSuccess(responseDTO);
            emitter.onComplete();
          },
          // On error
          emitter::onError
        );
      }
    });
  }

  @Override
  public Maybe<ProductResponseDTO> withdrawal(WithdrawalRequestDTO withdrawalRequestDTO) {
    return Maybe.create(new MaybeOnSubscribe<ProductResponseDTO>() {
      @Override
      public void subscribe(@NonNull MaybeEmitter<ProductResponseDTO> emitter) throws Throwable {
        Maybe<Product> originAccount = productRepository.findById(withdrawalRequestDTO.getIdOriginProduct()).map(productEntity -> ProductEntity.toProduct(productEntity));
        originAccount.subscribe(
          origin -> {
            double newAvailableBalance = Double.parseDouble(origin.getAvailableBalance()) - Double.parseDouble(withdrawalRequestDTO.getAmount());
            if (Double.parseDouble(origin.getAvailableTransactionsWithoutCost()) <= 0) {
              newAvailableBalance = newAvailableBalance - 14.90;
              origin.setAvailableBalance(String.valueOf(newAvailableBalance));
              Comission comission = Comission
                .builder()
                .idProduct(origin.getId())
                .amount(String.valueOf(14.90))
                .idClient(origin.getIdClient())
                .build();
              comissionRepository.save(comission);
            } else {
              origin.setAvailableBalance(String.valueOf(newAvailableBalance));
            }
            ProductEntity productEntity = ProductEntity.toProductEntity(origin);
            productRepository.save(productEntity);
            ProductResponseDTO responseDTO = new ProductResponseDTO(WITHDRAWAL_SUCCESSFULLY, STATUS_OK);

            emitter.onSuccess(responseDTO);
            emitter.onComplete();
          },
          emitter::onError
        );
      }
    });
  }

  @Override
  public Maybe<ProductResponseDTO> payCreditProduct(PayCreditProductRequestDTO payCreditProductRequestDTO) {
    return Maybe.create(new MaybeOnSubscribe<ProductResponseDTO>() {
      @Override
      public void subscribe(@NonNull MaybeEmitter<ProductResponseDTO> emitter) throws Throwable {
        Maybe<Product> originAccount = productRepository.findById(payCreditProductRequestDTO.getIdOriginProduct()).map(productEntity -> ProductEntity.toProduct(productEntity));
        originAccount.subscribe(
          // On success
          origin -> {
            double currentBalance = Double.parseDouble(origin.getAvailableBalance());
            double paymentAmount = Double.parseDouble(payCreditProductRequestDTO.getAmount());

            if (currentBalance >= paymentAmount) {
              double newBalance = currentBalance - paymentAmount;
              origin.setAvailableBalance(String.valueOf(newBalance));

              ProductResponseDTO responseDTO = new ProductResponseDTO(PAYMENT_SUCCESSFULLY, STATUS_OK);

              emitter.onSuccess(responseDTO);
              emitter.onComplete();
            } else {
              emitter.onError(new InsufficientFundsException("Insufficient funds to pay credit product."));
            }
          },
          emitter::onError
        );
      }
    });
  }

  @Override
  public Maybe<ProductResponseDTO> chargeConsumptionAccordCreditLine(ChargeConsumptionDTO chargeConsumptionDTO) {
    return Maybe.create(new MaybeOnSubscribe<ProductResponseDTO>() {
      @Override
      public void subscribe(@NonNull MaybeEmitter<ProductResponseDTO> emitter) throws Throwable {
        Maybe<Product> originAccount = productRepository.findById(chargeConsumptionDTO.getIdOriginProduct())
          .map(productEntity -> ProductEntity.toProduct(productEntity));

        originAccount.subscribe(
          origin -> {
            double currentBalance = Double.parseDouble(origin.getAvailableBalance());
            double chargeAmount = Double.parseDouble(chargeConsumptionDTO.getAmount());

            if (currentBalance >= chargeAmount) {
              double newBalance = currentBalance - chargeAmount;
              origin.setAvailableBalance(String.valueOf(newBalance));

              ProductResponseDTO responseDTO = new ProductResponseDTO(CHARGE_CONSUMPTION_SUCCESSFUL, STATUS_OK);

              emitter.onSuccess(responseDTO);
              emitter.onComplete();
            } else {
              emitter.onError(new InsufficientFundsException("Insufficient funds to purchase"));
            }
          },
          emitter::onError
        );
      }
    });
  }

  @Override
  public Maybe<ProductBalanceResponseDTO> getAvailableBalancePerProduct(Product product) {
    return Maybe.create(new MaybeOnSubscribe<ProductBalanceResponseDTO>() {
      @Override
      public void subscribe(@NonNull MaybeEmitter<ProductBalanceResponseDTO> emitter) throws Throwable {
        Maybe<Product> productFounded = productRepository.findById(product.getId()).map(productEntity -> ProductEntity.toProduct(productEntity));
        productFounded.subscribe(
          p -> {
            double availableBalance = Double.parseDouble(p.getAvailableBalance());
            ProductBalanceResponseDTO responseDTO = new ProductBalanceResponseDTO(String.valueOf(availableBalance), STATUS_OK);

            emitter.onSuccess(responseDTO);
            emitter.onComplete();
          }
        );
      }
    });
  }

  @Override
  public Flowable<Movement> getMovementsFromProduct(Product product) {
    Flux<Movement> movements = webClientBuilder.build()
      .get()
      .uri("http://MOVEMENT-SERVICE/movements?productId={productId}", product.getId())
      .retrieve()
      .bodyToFlux(Movement.class);
    return Flowable.fromPublisher(movements);
  }

  @Override
  @Transactional
  public Single<Product> createPersonalProduct(Product product) {
    return productRepository
      .findProductEntitiesByIdClient(product.getIdClient())
      .filter(productFiltered ->
        productFiltered.getProductType().equals(CUENTA_AHORRO) ||
          productFiltered.getProductType().equals(CUENTA_CORRIENTE) ||
          productFiltered.getProductType().equals(PLAZO_FIJO))
      .doOnNext(productEntity -> validatePersonalClientAndProducts(productEntity, product))
      .switchIfEmpty(saveProductAndMapToProductEntity(product).toFlowable())
      .singleOrError()
      .onErrorResumeNext(throwable -> {
        if (throwable instanceof ConflictException) return Single.error(throwable);
        else return Single.error(new ConflictException("The client type already has current type product"));
      })
      .map(productEntity -> ProductEntity.toProduct(productEntity));
  }

  private void validatePersonalClientAndProducts(ProductEntity productEntity, Product product) {
    getClient(productEntity.getIdClient())
      .map(client -> {
        if (product.getProductType().equals(CUENTA_AHORRO) &&
          client.getClientType().equals(PERSONAL_VIP) &&
          !productEntity.getProductType().equals(TARJETA_CREDITO)) {
          return Single.error(new ConflictException("Personal Vip client has to have a Credit Card with the bank"));
        }
        if (product.getProductType().equals(CUENTA_AHORRO) &&
          client.getClientType().equals(PERSONAL_VIP) &&
          Double.valueOf(productEntity.getAvailableBalance()) >= 500.00
        ) {
          return Single.error(new ConflictException("The save account hasn't enough founds"));
        }
        if (!client.getClientType().equals(PERSONAL))
          return Single.error(new ConflictException("The client type is not Personal"));
        else return Single.just(productEntity);
      })
      .onErrorResumeNext(throwable -> Single.error(new ConflictException("The client type is not Personal")));
  }

  @Override
  public Single<Product> createEnterpriseProduct(Product product) {
    return productRepository
      .findProductEntitiesByIdClient(product.getIdClient())
      .filter(productFiltered ->
        productFiltered.getProductType().equals(CUENTA_AHORRO) ||
          productFiltered.getProductType().equals(PLAZO_FIJO))
      .doOnNext(productEntity -> validateEnterpriseClientAndProducts(productEntity, product))
      .switchIfEmpty(saveProductAndMapToProductEntity(product).toFlowable())
      .singleOrError()
      .onErrorResumeNext(throwable -> {
        if (throwable instanceof ConflictException) return Single.error(throwable);
        else return Single.error(new ConflictException("The client type already has current type product"));
      })
      .map(productEntity -> ProductEntity.toProduct(productEntity));
  }

  @Override
  public Flowable<AvailableBalanceDTO> getAllAvailableBalances(String idClient) {
    return productRepository
      .findProductEntitiesByIdClient(idClient)
      .switchIfEmpty(Flowable.error(new NotFoundException("There are not products for this client")))
      .flatMap(this::mapToAvailableBalanceDTO);
  }

  @Override
  public Flowable<Comission> getAllComissionsOfAClientProduct(Comission comission) {
    return comissionRepository
      .getAllByIdClientAndIdProduct(comission.getIdClient(), comission.getIdProduct());
  }

  private void validateEnterpriseClientAndProducts(ProductEntity productEntity, Product product) {
    getClient(productEntity.getIdClient())
      .map(client -> {
        if (product.getProductType().equals(CUENTA_CORRIENTE) &&
          client.getClientType().equals(ENTERPRISE_PYME) &&
          !productEntity.getProductType().equals(TARJETA_CREDITO)) {
          return Single.error(new ConflictException("Enterprise PYME client has to have a Credit Card with the bank"));
        }
        if (!client.getClientType().equals(PERSONAL))
          return Single.error(new ConflictException("The client type is not Personal"));
        else return Single.just(productEntity);
      })
      .onErrorResumeNext(throwable -> Single.error(new ConflictException("The client type is not Personal")));
  }

  private Single<ProductEntity> saveProductAndMapToProductEntity(Product product) {
    ProductEntity productEntity = ProductEntity.toProductEntity(product);
    if (product.getProductType().equals(CUENTA_AHORRO)) productEntity.setProductTypeDescription(CUENTA_AHORRO_DESC);
    if (product.getProductType().equals(CUENTA_CORRIENTE))
      productEntity.setProductTypeDescription(CUENTA_CORRIENTE_DESC);
    if (product.getProductType().equals(PLAZO_FIJO)) productEntity.setProductTypeDescription(PLAZO_FIJO_DESC);
    return productRepository.save(productEntity);
  }

  public Single<Client> getClient(String clientId) {
    Mono<Client> clientFounded = webClientBuilder.build()
      .get()
      .uri("http://CLIENT-SERVICE/clients/{clientId}", clientId)
      .retrieve()
      .bodyToMono(Client.class);
    return Single.fromPublisher(clientFounded);
  }

  public Flowable<AvailableBalanceDTO> mapToAvailableBalanceDTO(ProductEntity productEntity) {
    AvailableBalanceDTO availableBalanceDTO = AvailableBalanceDTO
      .builder()
      .availableBalance(productEntity.getAvailableBalance())
      .productName(productEntity.getProductTypeDescription())
      .idClient(productEntity.getIdClient())
      .build();
    return Flowable.just(availableBalanceDTO);
  }
}
