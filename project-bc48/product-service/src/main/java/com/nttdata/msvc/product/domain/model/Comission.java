package com.nttdata.msvc.product.domain.model;

import com.nttdata.msvc.product.infrastructure.mongodb.entities.ProductEntity;
import lombok.*;
import org.springframework.beans.BeanUtils;

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
