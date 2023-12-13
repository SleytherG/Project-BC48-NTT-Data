package com.nttdata.msvc.product.infrastructure.api.dtos;

import com.nttdata.msvc.product.infrastructure.mongodb.entities.ProductEntity;
import io.reactivex.rxjava3.core.Flowable;
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
