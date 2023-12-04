package com.nttdata.msvc.product.domain.exceptions;

/**
 * Exception thrown in case of a conflict
 */
public class ConflictException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /**
     * Exception description for ConflictException
     */
    private static final String DESCRIPTION = "Conflict Exception";

    /**
     * Constructs a new ConflictException with the specified detail message.
     * @param detail The detail message
     */
    public ConflictException(final String detail) {
        super(DESCRIPTION + ". " + detail);
    }
}
