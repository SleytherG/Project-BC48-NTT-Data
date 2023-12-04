package com.nttdata.msvc.product.infrastructure.mongodb.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(value = "comissions")
public class ComissionEntity {

  @Id
  private String id;
  private String amount;
  private String idClient;
  private String idProduct;
}
