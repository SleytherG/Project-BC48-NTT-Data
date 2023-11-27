package com.nttdata.project.bc48.infrastructure.api.dtos;

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
    private String idDestinationProduct;
    private String amount;
}
