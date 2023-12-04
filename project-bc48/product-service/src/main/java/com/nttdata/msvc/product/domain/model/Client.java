package com.nttdata.msvc.product.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a client in the system
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    /**
     * Unique identifier for the client
     */
    private String id;
    /**
     * First name of the client
     */
    private String firstName;
    /**
     * Last name of the client
     */
    private String lastName;
    /**
     * Document type of the client
     */
    private String documentType;
    /**
     * Document number of the client
     */
    private String documentNumber;
    /**
     * Type of the client (e.g., personal = 1, enterprise = 2)
     */
    private String clientType;
    /**
     * Description of the client
     */
    private String description;
}
