package com.nttdata.project.bc48.domain.services;

import com.nttdata.project.bc48.domain.model.Movement;
import com.nttdata.project.bc48.domain.persistence.MovementPersistence;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.stereotype.Service;

@Service
public class MovementService {

    private final MovementPersistence movementPersistence;

    public MovementService(MovementPersistence movementPersistence) {
        this.movementPersistence = movementPersistence;
    }

    public Flowable<Movement> findAll() {
        return movementPersistence.findAll();
    }


    public Maybe<Movement> findById(String movementId) {
        return movementPersistence.findById(movementId);
    }


    public Single<Movement> create(Movement movement) {
        return movementPersistence.create(movement);
    }


    public Maybe<Movement> update(Movement movement) {
        return movementPersistence.update(movement);
    }


    public Maybe<Void> delete(String movementId) {
        return movementPersistence.delete(movementId);
    }

    public Maybe<Movement> findByProductId(String productId) {
        return movementPersistence.findByProductId(productId);
    }
}
