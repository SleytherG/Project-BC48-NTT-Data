package com.nttdata.msvc.product.domain.model;


import lombok.Getter;

/**
 * Enumeration representing different types of products
 */
@Getter
public enum ProductType {
    /**
     * Represents passive products.
     */
    Passive(new String[]{"CUENTA AHORRO", "CUENTA CORRIENTE", "PLAZO FIJO"}),

    /**
     * Represents active products.
     */
    Active(new String[]{"CREDITO PERSONAL", "CREDITO EMPRESARIAL", "TARJETA DE CREDITO"});

    /**
     * Array of product names associated with the product type.
     * -- GETTER --
     *  Gets the array of product names associated with the product type.
     *
     * @return The array of product names.

     */
    private final String[] products;

    /**
     * Constructs a new ProductType with the specified array of product names.
     *
     * @param products The array of product names.
     */
    ProductType(final String[] products) {
        this.products = products;
    }

}
