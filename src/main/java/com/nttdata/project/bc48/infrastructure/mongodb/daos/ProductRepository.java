package com.nttdata.project.bc48.infrastructure.mongodb.daos;

import com.nttdata.project.bc48.domain.model.Client;
import com.nttdata.project.bc48.infrastructure.api.dtos.DepositRequestDTO;
import com.nttdata.project.bc48.infrastructure.api.dtos.ProductResponseDTO;
import com.nttdata.project.bc48.infrastructure.api.dtos.WithdrawalRequestDTO;
import com.nttdata.project.bc48.infrastructure.mongodb.entities.ProductEntity;
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
