package com.nttdata.msvc.product.infrastructure.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class WithdrawalRequestDTO {

    private String idOriginProduct;
    private String amount;
}