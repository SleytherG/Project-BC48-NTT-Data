package com.nttdata.msvc.product.domain.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Debt {

  private String id;
  private String clientId;
  private String amount;
  private String payDayLimit;

}
