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
    Personal,
    /**
     * Represents an enterprise client
     */
    Enterprise
}
