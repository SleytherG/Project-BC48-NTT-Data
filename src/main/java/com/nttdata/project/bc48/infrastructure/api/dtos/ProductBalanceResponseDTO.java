package com.nttdata.project.bc48.infrastructure.api.dtos;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "balanceResponseDTOBuilder")
public class ProductBalanceResponseDTO extends ProductResponseDTO {

    private String idProduct;
    private String balanceAvailable;


    public ProductBalanceResponseDTO(String code, String message, String status, String idProduct, String balanceAvailable) {
        super(code, message, status);
        this.idProduct = idProduct;
        this.balanceAvailable = balanceAvailable;
    }
}
