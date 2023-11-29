package com.nttdata.msvc.product.domain.model;

import lombok.Getter;

/**
 * Enumeration representing different types of clients.
 */
@Getter
public enum ClientType {
    /**
     * Represents a personal client
     */
    PERSONAL,
    /**
     * Represents an enterprise client
     */
    ENTERPRISE
}
