package com.nttdata.msvc.product.infrastructure.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DepositRequestDTO {

    private String idOriginProduct;
    private String idDestinationProduct;
    private String amount;
}
