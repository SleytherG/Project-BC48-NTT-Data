package com.nttdata.msvc.product.domain.services;

import com.nttdata.msvc.product.domain.model.Comission;
import com.nttdata.msvc.product.domain.model.Debt;
import com.nttdata.msvc.product.domain.model.Movement;
import com.nttdata.msvc.product.domain.model.Product;
import com.nttdata.msvc.product.domain.persistence.ProductPersistence;
import com.nttdata.msvc.product.infrastructure.api.dtos.*;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for handling product-related operations.
 */
@Service
public class ProductService {

  /**
   * The persistence layer for handling product data.
   */
  private final ProductPersistence persistenceProduct;

  /**
   * Constructs a ProductService with the specified ProductPersistence.
   *
   * @param productPersistence The persistence layer for product data.
   */
  public ProductService(ProductPersistence productPersistence) {
    this.persistenceProduct = productPersistence;
  }


  public final Flowable<Product> findAll() {
    return persistenceProduct.findAll();
  }

  @Transactional
  public Single<Product> create(final Product product) {
    return persistenceProduct.create(product);
  }

  public final Maybe<Product> findById(final String productId) {
    return persistenceProduct.findById(productId);
  }

  public final Maybe<Product> update(final Product product) {
    return persistenceProduct.update(product);
  }

  public final Maybe<Void> delete(final String productId) {
    return persistenceProduct.delete(productId);
  }

  public final Maybe<ProductResponseDTO> deposit(final DepositRequestDTO depositRequestDTO) {
    return persistenceProduct.deposit(depositRequestDTO);
  }

  public final Maybe<ProductResponseDTO> withdrawal(final WithdrawalRequestDTO withdrawalRequestDTO) {
    return persistenceProduct.withdrawal(withdrawalRequestDTO);
  }

  public final Maybe<ProductResponseDTO> payCreditProduct(final PayCreditProductRequestDTO payCreditProductRequestDTO) {
    return persistenceProduct.payCreditProduct(payCreditProductRequestDTO);
  }

  public final Maybe<ProductResponseDTO> chargeConsumptionAccordCreditLine(final ChargeConsumptionDTO chargeConsumptionDTO) {
    return persistenceProduct.chargeConsumptionAccordCreditLine(chargeConsumptionDTO);
  }

  public final Maybe<ProductBalanceResponseDTO> getAvailableBalancePerProduct(final Product product) {
    return persistenceProduct.getAvailableBalancePerProduct(product);
  }

  public final Flowable<Movement> getMovementsFromProduct(final Product product) {
    return persistenceProduct.getMovementsFromProduct(product);
  }


  public final Single<Product> createPersonalProduct(final Product product) {
    return persistenceProduct.createPersonalProduct(product);
  }


  public Single<Product> createEnterpriseProduct(Product product) {
    return persistenceProduct.createEnterpriseProduct(product);
  }

  public Flowable<AvailableBalanceDTO> getAllAvailableBalances(String idClient) {
    return persistenceProduct.getAllAvailableBalances(idClient);
  }

  public Flowable<Comission> getAllCommissionsOfAClientProduct(Comission comission) {
    return persistenceProduct.getAllCommissionsOfAClientProduct(comission);
  }

  public Flowable<Product> getAllProductsOfAClientWithClientId(String clientId) {
    return persistenceProduct.getAllProductsOfAClientWithClientId(clientId);
  }

  public Completable payServiceWithDebitCard(String clientId, String serviceId) {
    return persistenceProduct.payServiceWithDebitCard(clientId, serviceId);
  }

  public Single<ClientDebtResponseDTO> getAllDebtsFromClient(String clientId) {
    return persistenceProduct.getAllDebtsFromClient(clientId);
  }

  public Completable associateSavingsAccountWithDebitCard(AssociateSavingAccountDTO associateSavingAccountDTO) {
    return persistenceProduct.associateSavingsAccountWithDebitCard(associateSavingAccountDTO);
  }
}
