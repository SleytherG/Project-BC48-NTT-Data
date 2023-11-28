package com.nttdata.msvc.product.infrastructure.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "productResponseDTOBuilder")
public class ProductResponseDTO {

    private String code;
    private String message;
    private String status;

}
