package com.nttdata.msvc.product.domain.mapper;

import com.nttdata.msvc.product.domain.model.Product;
import com.nttdata.msvc.product.infrastructure.mongodb.entities.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

  public final ProductEntity mapProductToProductEntity(Product product)  {
    return ProductEntity.builder()
      .id(product.getId())
      .productType(product.getProductType())
      .idClient(product.getIdClient())
      .availableBalance(product.getAvailableBalance())
      .holders(product.getHolders())
      .signatories(product.getSignatories())
      .productTypeDescription(product.getProductTypeDescription())
      .availableTransactions(product.getAvailableTransactionsWithoutCost())
      .clientType(product.getClientType())
      .build();
  }

  public final Product mapProductEntityToProduct(ProductEntity productEntity) {
    return Product.builder()
      .id(productEntity.getId())
      .productType(productEntity.getProductType())
      .idClient(productEntity.getIdClient())
      .availableBalance(productEntity.getAvailableBalance())
      .holders(productEntity.getHolders())
      .signatories(productEntity.getSignatories())
      .productTypeDescription(productEntity.getProductTypeDescription())
      .availableTransactionsWithoutCost(productEntity.getAvailableTransactions())
      .clientType(productEntity.getClientType())
      .build();
  }
}
