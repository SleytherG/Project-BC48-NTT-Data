package com.nttdata.project.bc48.infrastructure.mongodb.persistence;

import com.nttdata.project.bc48.domain.model.Movement;
import com.nttdata.project.bc48.domain.persistence.MovementPersistence;
import com.nttdata.project.bc48.infrastructure.mongodb.daos.MovementRepository;
import com.nttdata.project.bc48.infrastructure.mongodb.entities.MovementEntity;
import com.nttdata.project.bc48.infrastructure.mongodb.entities.ProductEntity;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.webjars.NotFoundException;

@Repository
public class MovementPersistenceMongodb implements MovementPersistence {

    final private MovementRepository movementRepository;

    public MovementPersistenceMongodb(MovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    @Override
    public Flowable<Movement> findAll() {
        return movementRepository
                .findAll()
                .map(MovementEntity::toMovement);
    }

    @Override
    public Maybe<Movement> findById(String movementId) {
        return movementRepository
                .findById(movementId)
                .switchIfEmpty(Maybe.error(new NotFoundException("Movement does not exists")))
                .map(MovementEntity::toMovement);
    }

    @Override
    public Single<Movement> create(Movement movement) {
        MovementEntity movementEntity = MovementEntity.toMovementEntity(movement);
        return movementRepository
                .save(movementEntity)
                .map(MovementEntity::toMovement);
    }

    @Override
    public Maybe<Movement> update(Movement movement) {
        return movementRepository
                .findById(movement.getId())
                .switchIfEmpty(Maybe.error(new NotFoundException("Movement does not exists")))
                .flatMapSingle(movementEntity -> {
                    BeanUtils.copyProperties(movement, movementEntity);
                    return movementRepository.save(movementEntity);
                })
                .map(MovementEntity::toMovement);
    }

    @Override
    public Maybe<Void> delete(String movementId) {
        return movementRepository
                .findById(movementId)
                .switchIfEmpty(Maybe.error(new NotFoundException("Movement does not exists")))
                .flatMap(movementEntity -> movementRepository.deleteById(movementEntity.getId()).andThen(Maybe.empty()));
    }

    @Override
    public Maybe<Movement> findByProductId(String productId) {
        return movementRepository
                .findByIdProduct(productId)
                .switchIfEmpty(Maybe.error(new NotFoundException("Movement searched by ID product does not exist")))
                .map(MovementEntity::toMovement);
    }
}
