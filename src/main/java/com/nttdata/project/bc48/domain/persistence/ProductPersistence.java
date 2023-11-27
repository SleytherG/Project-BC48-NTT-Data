package com.nttdata.project.bc48.domain.persistence;

import com.nttdata.project.bc48.domain.model.Movement;
import com.nttdata.project.bc48.domain.model.Product;
import com.nttdata.project.bc48.infrastructure.api.dtos.*;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPersistence {

    Flowable<Product> findAll();
    Maybe<Product> findById(String productId);
    Single<Product> create(Product product);
    Maybe<Product> update(Product product);
    Maybe<Void> delete(String productId);
    Maybe<ProductResponseDTO> deposit(DepositRequestDTO depositRequestDTO);
    Maybe<ProductResponseDTO> withdrawal(WithdrawalRequestDTO withdrawalRequestDTO);
    Maybe<ProductResponseDTO> payCreditProduct(PayCreditProductRequestDTO payCreditProductRequestDTO);
    Maybe<ProductResponseDTO> chargeConsumptionAccordCreditLine(ChargeConsumptionDTO chargeConsumptionDTO);
    Maybe<ProductBalanceResponseDTO> getAvailableBalancePerProduct(Product product);
    Flowable<Movement> getMovementsFromProduct(Movement movement);
}
