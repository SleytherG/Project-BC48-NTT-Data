package com.nttdata.msvc.movement.infrastructure.mongodb.daos;

import com.nttdata.msvc.movement.infrastructure.mongodb.entities.MovementEntity;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;

public interface MovementRepository extends RxJava3CrudRepository<MovementEntity, String> {

    Maybe<MovementEntity> findByIdProduct(String productId);
}
