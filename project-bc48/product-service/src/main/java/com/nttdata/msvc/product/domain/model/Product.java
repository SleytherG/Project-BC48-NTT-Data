package com.nttdata.msvc.product.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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
     * Type of the product
     * 1 => CUENTA DE AHORRO.
     * 2 => CUENTA CORRIENTE.
     * 3 => PLAZO FIJO
     * 4 => CREDITO PERSONAL
     * 5 => CREDITO EMPRESARIAL
     * 6 => TARJETA DE CREDITO
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
    private List<String> holders;

    /**
     * Array of signatories associated with the product.
     */
    private List<String> signatories;
    /**
     * Description of a product
     */
    private String productTypeDescription;

    private String availableTransactionsWithoutCost = "20";

    private String clientType;

}



