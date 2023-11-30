package com.nttdata.msvc.product.domain.persistence;


import com.nttdata.msvc.product.domain.model.Movement;
import com.nttdata.msvc.product.domain.model.Product;
import com.nttdata.msvc.product.infrastructure.api.dtos.*;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface for interacting with product persistence.
 */
@Repository
public interface ProductPersistence {

    /**
     * Retrieves all products.
     *
     * @return A flowable of products.
     */
    Flowable<Product> findAll();

    /**
     * Retrieves a product by its identifier.
     *
     * @param productId The identifier of the product to retrieve.
     * @return Maybe containing the product, if found.
     */
    Maybe<Product> findById(String productId);

    /**
     * Creates a new product.
     *
     * @param product The product to create.
     * @return Single containing the created product.
     */
    @Transactional
    Single<Product> create(Product product);

    /**
     * Updates an existing product.
     *
     * @param product The product to update.
     * @return Maybe containing the updated product, if found.
     */
    Maybe<Product> update(Product product);

    /**
     * Deletes a product by its identifier.
     *
     * @param productId The identifier of the product to delete.
     * @return Maybe indicating success or failure.
     */
    Maybe<Void> delete(String productId);

    /**
     * Deposits funds into a product.
     *
     * @param depositRequestDTO The deposit request data transfer object.
     * @return Maybe containing the response data transfer object.
     */
    Maybe<ProductResponseDTO> deposit(DepositRequestDTO depositRequestDTO);

    /**
     * Withdraws funds from a product.
     *
     * @param withdrawalRequestDTO The withdrawal request data transfer object.
     * @return Maybe containing the response data transfer object.
     */
    Maybe<ProductResponseDTO> withdrawal(WithdrawalRequestDTO withdrawalRequestDTO);

    /**
     * Pays a credit product.
     *
     * @param payCreditProductRequestDTO The pay credit product request data transfer object.
     * @return Maybe containing the response data transfer object.
     */
    Maybe<ProductResponseDTO> payCreditProduct(PayCreditProductRequestDTO payCreditProductRequestDTO);

    /**
     * Charges consumption according to a credit line.
     *
     * @param chargeConsumptionDTO The charge consumption data transfer object.
     * @return Maybe containing the response data transfer object.
     */
    Maybe<ProductResponseDTO> chargeConsumptionAccordCreditLine(ChargeConsumptionDTO chargeConsumptionDTO);

    /**
     * Gets the available balance for a product.
     *
     * @param product The product for which to retrieve the available balance.
     * @return Maybe containing the response data transfer object.
     */
    Maybe<ProductBalanceResponseDTO> getAvailableBalancePerProduct(Product product);

    /**
     * Retrieves movements associated with a product.
     *
     * @param product The movement for which to retrieve associated movements.
     * @return A flowable of movements.
     */
    Flowable<Movement> getMovementsFromProduct(Product product);
}
