package com.nttdata.msvc.product.infrastructure.mongodb.persistence;

import com.nttdata.msvc.product.domain.exceptions.ConflictException;
import com.nttdata.msvc.product.domain.exceptions.InsufficientFundsException;

import com.nttdata.msvc.product.domain.mapper.AvailableBalanceMapper;
import com.nttdata.msvc.product.domain.mapper.ClientMapper;
import com.nttdata.msvc.product.domain.mapper.DebtMapper;
import com.nttdata.msvc.product.domain.mapper.ProductMapper;
import com.nttdata.msvc.product.domain.model.*;
import com.nttdata.msvc.product.domain.persistence.ProductPersistence;
import com.nttdata.msvc.product.infrastructure.api.dtos.*;
import com.nttdata.msvc.product.infrastructure.mongodb.daos.ClientRepository;
import com.nttdata.msvc.product.infrastructure.mongodb.daos.ComissionRepository;
import com.nttdata.msvc.product.infrastructure.mongodb.daos.DebtRepository;
import com.nttdata.msvc.product.infrastructure.mongodb.daos.ProductRepository;
import com.nttdata.msvc.product.infrastructure.mongodb.entities.ClientEntity;
import com.nttdata.msvc.product.infrastructure.mongodb.entities.ProductEntity;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.webjars.NotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nttdata.msvc.product.utils.Constants.*;


@Repository
@Slf4j
public class ProductPersistenceMongodb implements ProductPersistence {

  private final ProductRepository productRepository;
  private final WebClient.Builder webClientBuilder;
  private final ClientRepository clientRepository;
  private final ComissionRepository comissionRepository;
  private final DebtRepository debtRepository;
  private final ProductMapper productMapper;
  private final ClientMapper clientMapper;
  private final DebtMapper debtMapper;
  private final AvailableBalanceMapper availableBalanceMapper;

  public ProductPersistenceMongodb(
    ProductRepository productRepository,
    WebClient.Builder webClientBuilder,
    ClientRepository clientRepository,
    ComissionRepository comissionRepository,
    DebtRepository debtRepository,
    ClientMapper clientMapper,
    ProductMapper productMapper,
    AvailableBalanceMapper availableBalanceMapper,
    DebtMapper debtMapper) {
    this.productRepository = productRepository;
    this.webClientBuilder = webClientBuilder;
    this.clientRepository = clientRepository;
    this.comissionRepository = comissionRepository;
    this.debtRepository = debtRepository;
    this.productMapper = productMapper;
    this.clientMapper = clientMapper;
    this.debtMapper = debtMapper;
    this.availableBalanceMapper = availableBalanceMapper;
  }

  @Override
  public Flowable<Product> findAll() {
    return productRepository
      .findAll()
      .map(this.productMapper::mapProductEntityToProduct);
  }

  @Override
  public Maybe<Product> findById(String productId) {
    return productRepository
      .findById(productId)
      .switchIfEmpty(Maybe.error(new NotFoundException("Product does not exist: " + productId)))
      .map(this.productMapper::mapProductEntityToProduct);
  }

  @Override
  @Transactional
  public Single<Product> create(Product product) {
    ProductEntity productEntity = ProductEntity.toProductEntity(product);
    return productRepository.save(productEntity)
      .map(this.productMapper::mapProductEntityToProduct);
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
      .map(this.productMapper::mapProductEntityToProduct);
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
        Maybe<Product> originAccount = productRepository
          .findById(depositRequestDTO.getIdOriginProduct())
          .map(productEntity -> ProductEntity.toProduct(productEntity));

        Maybe<Product> destinationAccount = productRepository
          .findById(depositRequestDTO.getIdOriginProduct())
          .map(productEntity -> ProductEntity.toProduct(productEntity));

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

          ProductEntity originEntity = ProductEntity.toProductEntity(origin);
          ProductEntity destinatioEntity = ProductEntity.toProductEntity(destination);
          productRepository.save(originEntity);
          productRepository.save(destinatioEntity);

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
            ProductEntity originEntity = ProductEntity.toProductEntity(origin);
            productRepository.save(originEntity);
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
        Maybe<ProductEntity> originAccount = productRepository.findById(payCreditProductRequestDTO.getIdOriginProduct());
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
        Maybe<ProductEntity> originAccount = productRepository.findById(chargeConsumptionDTO.getIdOriginProduct());

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
        Maybe<ProductEntity> productFounded = productRepository.findById(product.getId());
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
      .doOnNext(productFlow -> log.warn(productFlow.getProductType()))
      .toList()
      .observeOn(Schedulers.io())
      .flatMap((existingProducts) -> {
        List<Debt> debtsFromClient = debtRepository.getDebtEntitiesByClientId(product.getIdClient()).map(this.debtMapper::mapDebtEntityToDebt).toList().blockingGet();
        if ( !debtsFromClient.isEmpty() ) {
          return Single.error(new ConflictException("The client already has debts with the bank, cannot get more products"));
        }
        long savingAccountsFoundedSize = existingProducts.stream().filter(productFiltered -> productFiltered.getProductType().equals(CUENTA_AHORRO)).count();
        long checkingAccountsFoundedSize = existingProducts.stream().filter(productFiltered -> productFiltered.getProductType().equals(CUENTA_CORRIENTE)).count();
        long fixedTermsFoundedSize = existingProducts.stream().filter(productFiltered -> productFiltered.getProductType().equals(PLAZO_FIJO)).count();
        List<ProductEntity> savingAccountsFounded = existingProducts.stream().filter(productFiltered -> productFiltered.getProductType().equals(CUENTA_AHORRO)).collect(Collectors.toList());
        List<ProductEntity> checkingAccountsFounded = existingProducts.stream().filter(productFiltered -> productFiltered.getProductType().equals(CUENTA_CORRIENTE)).collect(Collectors.toList());
        List<ProductEntity> fixedTermsFounded = existingProducts.stream().filter(productFiltered -> productFiltered.getProductType().equals(PLAZO_FIJO)).collect(Collectors.toList());

        if ( !existingProducts.isEmpty() && savingAccountsFoundedSize > 1 ) {
          return Single.error(new ConflictException("The client already has a savings account."));
        } else  if ( !existingProducts.isEmpty() && checkingAccountsFoundedSize > 1 ) {
          return Single.error(new ConflictException("The client already has a checking account."));
        } else if ( !existingProducts.isEmpty() && fixedTermsFoundedSize > 1 ) {
          return Single.error(new ConflictException("The client already has a fixed term product."));
        } else {
          if ( savingAccountsFoundedSize == 0) {
            return validatePersonalClientAndProducts(this.productMapper.mapProductEntityToProduct(savingAccountsFounded.get(0)), product)
              .flatMap(this::create);
          }
          if ( checkingAccountsFoundedSize == 0) {
            return validatePersonalClientAndProducts(this.productMapper.mapProductEntityToProduct(checkingAccountsFounded.get(0)), product)
              .flatMap(this::create);
          }
          if ( fixedTermsFoundedSize == 0) {
            return validatePersonalClientAndProducts(this.productMapper.mapProductEntityToProduct(fixedTermsFounded.get(0)), product)
              .flatMap(this::create);
          }
          return Single.error(new RuntimeException("Could not create a personal product"));
        }
      });
  }

  private Single<Product> validatePersonalClientAndProducts(Product productFounded, Product product) {
    return getClientRequest(product.getIdClient())
      .flatMap(client -> {
        product.setClientType(PERSONAL_DESC);
//        product.setClientType(client.getClientType().equals(PERSONAL) ? PERSONAL_DESC : ENTERPRISE_DESC);
        if (product.getProductType().equals(CUENTA_AHORRO) &&
          client.getClientType().equals(PERSONAL_VIP) &&
          !productFounded.getProductType().equals(TARJETA_CREDITO)) {
          return Single.error(new ConflictException("Personal Vip client has to have a Credit Card with the bank"));
        }
        if (product.getProductType().equals(CUENTA_AHORRO) &&
          client.getClientType().equals(PERSONAL_VIP) &&
          Double.valueOf(productFounded.getAvailableBalance()) >= 500.00
        ) {
          return Single.error(new ConflictException("The save account hasn't enough funds"));
        }
        if (!client.getClientType().equals(PERSONAL)) {
          return Single.error(new ConflictException("The client type is not Personal"));
        } else {
          return Single.just(product);
        }
      })
      .onErrorResumeNext(throwable -> Single.error(new ConflictException("The client type is not Personal")));
  }

  @Override
  public Single<Product> createEnterpriseProduct(Product product) {
    return productRepository
      .findProductEntitiesByIdClient(product.getIdClient())
      .doOnNext(productFlow -> log.warn(productFlow.getProductType()))
      .toList()
      .flatMap(existingProducts -> {
        List<Debt> debtsFromClient = debtRepository.getDebtEntitiesByClientId(product.getIdClient()).map(this.debtMapper::mapDebtEntityToDebt).toList().blockingGet();
        if ( !debtsFromClient.isEmpty() ) {
          return Single.error(new ConflictException("The client already has debts with the bank, cannot get more products"));
        }
        List<ProductEntity> fixedTermsFounded = existingProducts.stream().filter(productFiltered -> productFiltered.getProductType().equals(PLAZO_FIJO)).collect(Collectors.toList());
        if ( product.getProductType().equals(CUENTA_AHORRO) ) {
          return Single.error(new ConflictException("The client cannot open a saving account."));
        } else  if ( product.getProductType().equals(PLAZO_FIJO) ) {
          return Single.error(new ConflictException("The client cannot open a fixed term account."));
        } else {
          return validateEnterpriseClientAndProducts(this.productMapper.mapProductEntityToProduct(fixedTermsFounded.get(0)), product)
            .flatMap(this::create);
        }
      });
  }

  @Override
  public Flowable<AvailableBalanceDTO> getAllAvailableBalances(String idClient) {
    return productRepository
      .findProductEntitiesByIdClient(idClient)
      .switchIfEmpty(Flowable.error(new NotFoundException("There are not products for this client")))
      .flatMap(this.availableBalanceMapper::mapToAvailableBalanceDTO);
  }

  @Override
  public Flowable<Comission> getAllCommissionsOfAClientProduct(Comission comission) {
    return comissionRepository
      .getAllByIdClientAndIdProduct(comission.getIdClient(), comission.getIdProduct());
  }

  @Override
  public Flowable<Product> getAllProductsOfAClientWithClientId(String clientId) {
    return productRepository
      .findProductEntitiesByIdClient(clientId)
      .map(this.productMapper::mapProductEntityToProduct);
  }

  @Override
  public Completable payServiceWithDebitCard(String clientId, String serviceId) {
    return null;
//    return productRepository
//      .findProductEntitiesByIdClient(clientId)
//      .filter(product -> product.getProductType().equals(TARJETA_DEBITO))
//      .onErrorResumeNext(throwable -> {
//        return Completable.error(new RuntimeException("Error processing debit card payment", throwable)).toFlowable();
//      });
  }

  @Override
  public Single<ClientDebtResponseDTO> getAllDebtsFromClient(String clientId) {
    return debtRepository
      .getDebtEntitiesByClientId(clientId)
      .toList()
      .flatMap(debtEntities ->
        clientRepository
        .findById(clientId)
        .flatMap(clientEntity -> {
          Client client = clientMapper.mapClientEntityToClient(clientEntity);
          List<Debt> debts = this.debtMapper.mapListDebtEntitiesToListDebt(debtEntities);
          ClientDebtResponseDTO clientDebtResponseDTO = ClientDebtResponseDTO.mapClientAndDebtsToClientDebtResponseDTO(client, debts);
          return Maybe.just(clientDebtResponseDTO);
        })
        .toSingle())
      .cast(ClientDebtResponseDTO.class); // Ensure the correct type is returned
  }

  @Override
  public Completable associateSavingsAccountWithDebitCard(AssociateSavingAccountDTO associateSavingAccountDTO) {
    return Completable.fromMaybe(productRepository
      .findById(associateSavingAccountDTO.getDebitCardProduct().getId())
      .flatMap(debitCardProduct ->
        productRepository
        .findById(associateSavingAccountDTO.getAccountProduct().getId())
        .map(accountProduct -> {
          List<Product> accounts = Arrays.asList(this.productMapper.mapProductEntityToProduct(accountProduct));
          debitCardProduct.setAssociatedAccount(accounts);
          return debitCardProduct;
        })));
  }

  private Single<Product> validateEnterpriseClientAndProducts(Product productFounded, Product product) {
    return getClientRequest(productFounded.getIdClient())
      .flatMap(client -> {
        if (product.getProductType().equals(CUENTA_CORRIENTE) &&
          client.getClientType().equals(ENTERPRISE_PYME) &&
          !productFounded.getProductType().equals(TARJETA_CREDITO)) {
          return Single.error(new ConflictException("Enterprise PYME client has to have a Credit Card with the bank"));
        }
        if (!client.getClientType().equals(PERSONAL))
          return Single.error(new ConflictException("The client type is not Personal"));
        else return Single.just(productFounded);
      })
      .onErrorResumeNext(throwable -> Single.error(new ConflictException("The client type is not Personal")));
  }

  public Single<Product> getClientSaveProductAndMapToProduct(Product product) {
    return Single.create(emitter -> {
      getClientRequest(product.getIdClient())
        .map(client -> {
          product.setClientType(client.getClientType().equals(PERSONAL) ? PERSONAL_DESC : ENTERPRISE_DESC);
          product.setProductTypeDescription(mapProductTypeToDescription(product.getProductType()));
          emitter.onSuccess(product);
          ProductEntity productEntity = this.productMapper.mapProductToProductEntity(product);
          productRepository.save(productEntity);
          return product;
        }).subscribe();
    });
  }

  public Single<Client> getClientRequest(String clientId) {
    Mono<Client> clientRequest = webClientBuilder.build()
      .get()
      .uri("http://CLIENT-SERVICE/clients/{clientId}", clientId)
      .retrieve()
      .bodyToMono(Client.class);
    return Single.fromPublisher(clientRequest);
  }

  private String mapProductTypeToDescription(String productType) {
    switch (productType) {
      case CUENTA_AHORRO:
        return CUENTA_AHORRO_DESC;
      case CUENTA_CORRIENTE:
        return CUENTA_CORRIENTE_DESC;
      case PLAZO_FIJO:
        return PLAZO_FIJO_DESC;
      default:
        return null;
    }
  }


}
