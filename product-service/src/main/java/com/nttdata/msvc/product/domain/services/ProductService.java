package com.nttdata.msvc.product.domain.services;

import com.nttdata.msvc.product.domain.model.Movement;
import com.nttdata.msvc.product.domain.model.Product;
import com.nttdata.msvc.product.domain.persistence.ProductPersistence;
import com.nttdata.msvc.product.infrastructure.api.dtos.ProductResponseDTO;
import com.nttdata.msvc.product.infrastructure.api.dtos.DepositRequestDTO;
import com.nttdata.msvc.product.infrastructure.api.dtos.WithdrawalRequestDTO;
import com.nttdata.msvc.product.infrastructure.api.dtos.PayCreditProductRequestDTO;
import com.nttdata.msvc.product.infrastructure.api.dtos.ChargeConsumptionDTO;
import com.nttdata.msvc.product.infrastructure.api.dtos.ProductBalanceResponseDTO;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.stereotype.Service;

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

    public final Single<Product> create(final Product product) {
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
        return null;
    }

    public final Maybe<ProductResponseDTO> withdrawal(final WithdrawalRequestDTO withdrawalRequestDTO) {
        return null;
    }

    public final Maybe<ProductResponseDTO> payCreditProduct(final PayCreditProductRequestDTO payCreditProductRequestDTO) {
        return null;
    }

    public final Maybe<ProductResponseDTO> chargeConsumptionAccordCreditLine(final ChargeConsumptionDTO chargeConsumptionDTO) {
        return null;
    }

    public final Maybe<ProductBalanceResponseDTO> getAvailableBalancePerProduct(final Product product) {
        return null;
    }

    public final Flowable<Movement> getMovementsFromProduct(final Movement movement) {
        return null;
    }


}