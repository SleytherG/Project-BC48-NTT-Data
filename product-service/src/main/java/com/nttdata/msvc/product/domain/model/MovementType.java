package com.nttdata.msvc.product.domain.model;

import lombok.Getter;

/**
 * Enumeration representing different types of movements.
 */
@Getter
public enum MovementType {
    /**
     * Represents a deposit movement.
     */
    DEPOSIT,

    /**
     * Represents a pay movement.
     */
    PAY,

    /**
     * Represents a withdrawal movement.
     */
    WITHDRAWAL
}
