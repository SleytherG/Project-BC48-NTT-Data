package com.nttdata.msvc.product.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private String id;
    private String idClient;
    private String accountNumber;
    private String cci;
    private String createdAt;
    private BigDecimal availableBalance;
    private List<Movement> movements;


}
