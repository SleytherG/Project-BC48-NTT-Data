package com.nttdata.project.bc48.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Product {

    private String id;
    private String productType;
    private String idClient;
    private String availableBalance;
    private String[] holders;
    private String[] signatories;
}



