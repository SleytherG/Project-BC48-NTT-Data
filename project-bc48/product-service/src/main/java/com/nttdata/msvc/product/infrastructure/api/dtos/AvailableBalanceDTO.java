package com.nttdata.msvc.product.infrastructure.api.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailableBalanceDTO {

  private String availableBalance;
  private String productName;
  private String idClient;

}
