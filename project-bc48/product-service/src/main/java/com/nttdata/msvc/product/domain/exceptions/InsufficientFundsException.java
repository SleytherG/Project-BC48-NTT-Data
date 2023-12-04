package com.nttdata.msvc.product.domain.exceptions;


public class InsufficientFundsException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private static final String DESCRIPTION = "Conflict Exception";

    public InsufficientFundsException(final String detail) {
        super(DESCRIPTION + ". " + detail);
    }
}
