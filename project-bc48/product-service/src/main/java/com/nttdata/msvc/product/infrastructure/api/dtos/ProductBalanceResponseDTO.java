package com.nttdata.msvc.product.infrastructure.api.dtos;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "balanceResponseDTOBuilder")
public class ProductBalanceResponseDTO extends ProductResponseDTO {

    private String idProduct;
    private String balanceAvailable;


    public ProductBalanceResponseDTO( String message, String status, String idProduct, String balanceAvailable) {
        super(message, status);
        this.idProduct = idProduct;
        this.balanceAvailable = balanceAvailable;
    }
}
