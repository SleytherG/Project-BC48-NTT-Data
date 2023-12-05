package com.nttdata.msvc.product.domain.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Comission {


  private String id;
  private String amount;
  private String idClient;
  private String idProduct;


}
