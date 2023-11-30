package com.nttdata.msvc.product.infrastructure.mongodb.persistence;

import com.nttdata.msvc.product.domain.exceptions.ConflictException;
import com.nttdata.msvc.product.domain.exceptions.InsufficientFundsException;
import com.nttdata.msvc.product.domain.exceptions.ProductAlreadyExistsException;

import com.nttdata.msvc.product.domain.model.Movement;
import com.nttdata.msvc.product.domain.model.Product;
import com.nttdata.msvc.product.domain.model.ProductType;
import com.nttdata.msvc.product.domain.persistence.ProductPersistence;
import com.nttdata.msvc.product.infrastructure.api.dtos.*;
import com.nttdata.msvc.product.infrastructure.mongodb.daos.ClientRepository;
import com.nttdata.msvc.product.infrastructure.mongodb.daos.ProductRepository;
import com.nttdata.msvc.product.infrastructure.mongodb.entities.ClientEntity;
import com.nttdata.msvc.product.infrastructure.mongodb.entities.ProductEntity;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.*;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.webjars.NotFoundException;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nttdata.msvc.product.utils.Constants.*;


@Repository
@Slf4j
public class ProductPersistenceMongodb implements ProductPersistence {

  private final ProductRepository productRepository;
  private final WebClient.Builder webClientBuilder;
  private final ClientRepository clientRepository;

  public ProductPersistenceMongodb(ProductRepository productRepository, ClientRepository clientRepository, WebClient.Builder webClientBuilder) {
    this.productRepository = productRepository;
    this.webClientBuilder = webClientBuilder;
    this.clientRepository = clientRepository;
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
    Flowable<ProductEntity> clientProducts = productRepository.findProductEntitiesByIdClient(product.getIdClient());
    Single<ClientEntity> clientFounded = Single.fromPublisher(webClientBuilder.build().get().uri("http://CLIENT-SERVICE/clients/" + product.getIdClient()).retrieve().bodyToMono(ClientEntity.class));
    ProductEntity productEntity = ProductEntity.toProductEntity(product);
    return clientProducts
      .isEmpty()
      .flatMap(isEmpty -> {
        if (isEmpty) {
          return clientFounded
            .flatMap(clientMapped -> {
              String productType = productEntity.getProductType();
              if (getProductTypeDescriptions().containsKey(productType))
                productEntity.setProductTypeDescription(getProductTypeDescriptions().get(productType));
              if (clientMapped.getClientType().equals(ENTERPRISE) && (productType.equals(CUENTA_AHORRO) || productType.equals(PLAZO_FIJO))) {
                return Single.error(new ConflictException("This client type cannot have this kind of product"));
              }
              return productRepository.save(productEntity).map(productSaved -> ProductEntity.toProduct(productSaved));
            });
        } else {
          return Single.error(new RuntimeException("Not implemented method if clientProducts is not empty"));
        }
      });

  }

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

          double originNewAvailableBalance = Double.parseDouble(origin.getAvailableBalance()) - Double.parseDouble(depositRequestDTO.getAmount());
          origin.setAvailableBalance(String.valueOf(originNewAvailableBalance));

          double destinationNewAvailableBalance = Double.parseDouble(destination.getAvailableBalance()) + Double.parseDouble(depositRequestDTO.getAmount());
          destination.setAvailableBalance(String.valueOf(destinationNewAvailableBalance));

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
            origin.setAvailableBalance(String.valueOf(newAvailableBalance));

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
    Flux<Movement> movements = Flux.create(emitter -> webClientBuilder.build()
      .get()
      .uri("http://MOVEMENT-SERVICE/movements?productId={productId}", product.getId())
      .retrieve()
      .bodyToFlux(Movement.class)
      .subscribe(emitter::next, emitter::error, emitter::complete));
    return Flowable.fromPublisher(movements);
  }
}
