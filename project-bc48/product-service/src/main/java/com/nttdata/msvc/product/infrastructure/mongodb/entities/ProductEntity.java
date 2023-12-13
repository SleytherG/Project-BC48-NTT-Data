package com.nttdata.msvc.product.infrastructure.mongodb.entities;

import com.nttdata.msvc.product.domain.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "products")
public class ProductEntity {

    @Id
    private String id;
    private String productType;
    private String idClient;
    private String availableBalance;
    private List<String> holders;
    private List<String> signatories;
    private String productTypeDescription;
    private String availableTransactions;
    private String clientType;
    private List<Product> associatedAccount;

    public com.nttdata.msvc.product.domain.model.Product toProduct() {
        com.nttdata.msvc.product.domain.model.Product product = new com.nttdata.msvc.product.domain.model.Product();
        BeanUtils.copyProperties(this, product);
        return product;
    }

    public static com.nttdata.msvc.product.domain.model.Product toProduct(ProductEntity productEntity) {
        com.nttdata.msvc.product.domain.model.Product product = new com.nttdata.msvc.product.domain.model.Product();
        BeanUtils.copyProperties(productEntity, product);
        return product;
    }

    public static ProductEntity toProductEntity(com.nttdata.msvc.product.domain.model.Product product) {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(product, productEntity);
        return productEntity;
    }

    public static ProductEntity toProductPersonal(com.nttdata.msvc.product.domain.model.Product product) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductType(product.getProductType());
        productEntity.setIdClient(product.getIdClient());
        productEntity.setAvailableBalance(product.getAvailableBalance());
        return productEntity;
    }

    public static ProductEntity toProductEnterprise(com.nttdata.msvc.product.domain.model.Product product) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductType(product.getProductType());
        productEntity.setIdClient(product.getIdClient());
        productEntity.setAvailableBalance(product.getAvailableBalance());
        productEntity.setHolders(product.getHolders());
        productEntity.setSignatories(product.getSignatories());
        return productEntity;
    }


}
