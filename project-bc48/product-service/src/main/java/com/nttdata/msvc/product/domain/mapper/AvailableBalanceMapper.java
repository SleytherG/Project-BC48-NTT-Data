package com.nttdata.msvc.product.domain.mapper;

import com.nttdata.msvc.product.domain.model.Product;
import com.nttdata.msvc.product.infrastructure.api.dtos.AvailableBalanceDTO;
import com.nttdata.msvc.product.infrastructure.mongodb.entities.ProductEntity;
import io.reactivex.rxjava3.core.Flowable;
import org.springframework.stereotype.Component;

@Component
public class AvailableBalanceMapper {

  public final Flowable<AvailableBalanceDTO> mapToAvailableBalanceDTO(ProductEntity product) {
    AvailableBalanceDTO availableBalanceDTO = AvailableBalanceDTO
      .builder()
      .availableBalance(product.getAvailableBalance())
      .productName(product.getProductTypeDescription())
      .idClient(product.getIdClient())
      .build();
    return Flowable.just(availableBalanceDTO);
  }
}
