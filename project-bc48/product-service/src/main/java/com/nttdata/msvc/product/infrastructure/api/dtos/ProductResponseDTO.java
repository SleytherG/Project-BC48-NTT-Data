package com.nttdata.msvc.product.infrastructure.api.dtos;

import com.nttdata.msvc.product.domain.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "productResponseDTOBuilder")
public class ProductResponseDTO {

    private String message;
    private String status;
    private Product product;

    public ProductResponseDTO(String message, String status) {
        this.message = message;
        this.status = status;
    }



}
