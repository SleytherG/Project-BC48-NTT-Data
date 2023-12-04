package com.nttdata.msvc.movement.domain.persistence;

import com.nttdata.msvc.movement.domain.model.Movement;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementPersistence {

    Flowable<Movement> findAll();
    Maybe<Movement> findById(String movementId);
    Single<Movement> create(Movement movement);
    Maybe<Movement> update(Movement movement);
    Maybe<Void> delete(String movementId);
    Maybe<Movement> findByProductId(String productId);
}
