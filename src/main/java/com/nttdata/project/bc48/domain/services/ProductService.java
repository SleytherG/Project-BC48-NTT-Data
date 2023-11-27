package com.nttdata.project.bc48.domain.services;

import com.nttdata.project.bc48.domain.model.Movement;
import com.nttdata.project.bc48.domain.model.Product;
import com.nttdata.project.bc48.domain.persistence.ProductPersistence;
import com.nttdata.project.bc48.infrastructure.api.dtos.*;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductPersistence productPersistence;

    public ProductService(ProductPersistence productPersistence) {
        this.productPersistence = productPersistence;
    }

    public Flowable<Product> findAll() {
        return productPersistence.findAll();
    }

    public Single<Product> create(Product product) {
        return productPersistence.create(product);
    }

    public Maybe<Product> findById(String productId) {
        return productPersistence.findById(productId);
    }

    public Maybe<Product> update(Product product) {
        return productPersistence.update(product);
    }

    public Maybe<Void> delete(String productId) {
        return productPersistence.delete(productId);
    }

    public Maybe<ProductResponseDTO> deposit(DepositRequestDTO depositRequestDTO) {
        return null;
    }

    public Maybe<ProductResponseDTO> withdrawal(WithdrawalRequestDTO withdrawalRequestDTO) {
        return null;
    }

    public Maybe<ProductResponseDTO> payCreditProduct(PayCreditProductRequestDTO payCreditProductRequestDTO) {
        return null;
    }

    public Maybe<ProductResponseDTO> chargeConsumptionAccordCreditLine(ChargeConsumptionDTO chargeConsumptionDTO) {
        return null;
    }

    public Maybe<ProductBalanceResponseDTO> getAvailableBalancePerProduct(Product product) {
        return null;
    }

    public Flowable<Movement> getMovementsFromProduct(Movement movement) {
        return null;
    }


}
