package com.nttdata.msvc.product.infrastructure.api.dtos;

import com.nttdata.msvc.product.domain.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AssociateSavingAccountDTO {

  private Product accountProduct;
  private Product debitCardProduct;

}
