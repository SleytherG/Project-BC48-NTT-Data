package com.nttdata.msvc.product.infrastructure.mongodb.daos;

import com.nttdata.msvc.product.infrastructure.mongodb.entities.ProductEntity;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;


public interface ProductRepository extends RxJava3CrudRepository<ProductEntity, String> {

    Flowable<ProductEntity> findProductEntitiesByIdClient(String idClient);
    Single<ProductEntity> findProductEntityByIdClient(String idClient);
//    Maybe<ProductResponseDTO> deposit(DepositRequestDTO depositRequestDTO);
//    Maybe<ProductResponseDTO> withdrawal(WithdrawalRequestDTO withdrawalRequestDTO);
}



