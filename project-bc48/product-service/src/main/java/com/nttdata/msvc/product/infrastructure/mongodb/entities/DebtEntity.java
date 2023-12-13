package com.nttdata.msvc.product.infrastructure.mongodb.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(value = "debts")
public class DebtEntity {

  @Id
  private String id;
  private String clientId;
  private String amount;
  private String payDayLimit;

}
