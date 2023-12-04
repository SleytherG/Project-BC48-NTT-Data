package com.nttdata.msvc.product.infrastructure.mongodb.daos;

import com.nttdata.msvc.product.domain.model.Comission;
import io.reactivex.rxjava3.core.Flowable;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;

public interface ComissionRepository extends RxJava3CrudRepository<Comission, String> {

  Flowable<Comission> getAllByIdClientAndIdProduct(String idClient, String idProduct);
}
