package com.nttdata.msvc.product.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a product in the system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Product {

    /**
     * Unique identifier for the product.
     */
    private String id;

    /**
     * Type of the product.
     */
    private String productType;

    /**
     * Identifier of the associated client.
     */
    private String idClient;

    /**
     * Available balance for the product.
     */
    private String availableBalance;

    /**
     * Array of holders associated with the product.
     */
    private String[] holders;

    /**
     * Array of signatories associated with the product.
     */
    private String[] signatories;
}



