package com.nttdata.msvc.product.domain.exceptions;

/**
 * Exception thrown when attempting to create a product that already exists.
 */
public class ProductAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /**
     * Description for the ProductAlreadyExistsException
     */
    private static final String DESCRIPTION = "Conflict Exception";

    /**
     * Constructs a new ProductAlreadyExistsException with the specified detail message
     * @param detail The detail message
     */
    public ProductAlreadyExistsException(final String detail) {
        super(DESCRIPTION + ". " + detail);
    }
}
