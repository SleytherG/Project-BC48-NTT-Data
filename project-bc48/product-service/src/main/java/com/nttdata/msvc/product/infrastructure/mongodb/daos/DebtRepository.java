package com.nttdata.msvc.product.infrastructure.mongodb.daos;

import com.nttdata.msvc.product.domain.model.Debt;
import com.nttdata.msvc.product.infrastructure.mongodb.entities.DebtEntity;
import com.nttdata.msvc.product.infrastructure.mongodb.entities.ProductEntity;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;


public interface DebtRepository extends RxJava3CrudRepository<DebtEntity, String> {

  Flowable<DebtEntity> getDebtEntitiesByClientId(String clientId);
}



