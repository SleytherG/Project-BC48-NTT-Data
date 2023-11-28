package com.nttdata.msvc.product.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a movement related to a product.
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Movement {

    /**
     * Unique identifier for the movement.
     */
    private String id;

    /**
     * Identifier of the associated product.
     */
    private String idProduct;

    /**
     * Amount associated with the movement.
     */
    private String amount;

    /**
     * Description of the movement.
     */
    private String description;

    /**
     * Type of the movement (e.g., deposit, pay, withdrawal).
     */
    private MovementType movementType;

    /**
     * Date of the operation.
     */
    private String operationDate;

    /**
     * Additional observation for the movement.
     */
    private String observation;

}
