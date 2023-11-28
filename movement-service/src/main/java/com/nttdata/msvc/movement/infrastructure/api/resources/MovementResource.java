package com.nttdata.msvc.movement.infrastructure.api.resources;

import com.nttdata.msvc.movement.domain.model.Movement;
import com.nttdata.msvc.movement.domain.services.MovementService;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(MovementResource.MOVEMENTS)
public class MovementResource {

    public static final String MOVEMENTS = "/movements";
    public static final String MOVEMENT_ID = "/{movementId}";
    public static final String PRODUCT_ID = "/{productId}";
    public static final String PRODUCT = "/product";

    final private MovementService movementService;

    public MovementResource(MovementService movementService) {
        this.movementService = movementService;
    }

    @GetMapping(produces = {"application/json"})
    public Flowable<Movement> findAll() {
        return movementService.findAll();
    }

    @PostMapping(produces = {"application/json"})
    public Single<Movement> create(@RequestBody Movement movement) {
        return movementService.create(movement);
    }

    @PutMapping(produces = {"application/json"})
    public Maybe<Movement> update(@RequestBody Movement movement) {
        return movementService.update(movement);
    }

    @GetMapping(MOVEMENT_ID)
    public Maybe<Movement> findById(@PathVariable String movementId) {
        return movementService.findById(movementId);
    }

    @DeleteMapping(MOVEMENT_ID)
    public Maybe<Void> delete(@PathVariable String movementId) {
        return movementService.delete(movementId);
    }

    @GetMapping(PRODUCT + PRODUCT_ID)
    public Maybe<Movement> findByProductId(@PathVariable String productId) {
        return movementService.findByProductId(productId);
    }

}
