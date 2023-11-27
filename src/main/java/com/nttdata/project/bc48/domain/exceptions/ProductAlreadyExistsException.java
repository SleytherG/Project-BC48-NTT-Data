package com.nttdata.project.bc48.domain.exceptions;

public class ProductAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String DESCRIPTION = "Conflict Exception";

    public ProductAlreadyExistsException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }
}
