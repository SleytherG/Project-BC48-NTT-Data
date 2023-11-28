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
    Deposit,

    /**
     * Represents a pay movement.
     */
    Pay,

    /**
     * Represents a withdrawal movement.
     */
    Withdrawal
}
