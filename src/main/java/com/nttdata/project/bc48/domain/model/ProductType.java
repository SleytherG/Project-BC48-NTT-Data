package com.nttdata.project.bc48.domain.model;


import lombok.Getter;

@Getter
public enum ProductType {
    Passive(new String[]{"CUENTA AHORRO", "CUENTA CORRIENTE", "PLAZO FIJO"}),
    Active(new String[]{"CREDITO PERSONAL", "CREDITO EMPRESARIAL", "TARJETA DE CREDITO"});

    private final String[] products;

    ProductType(String[] products) {
        this.products = products;
    }

}
