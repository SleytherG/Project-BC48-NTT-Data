package com.nttdata.msvc.product.infrastructure.mongodb.entities;

import com.nttdata.msvc.product.domain.model.Product;
import io.reactivex.rxjava3.core.Single;
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

    public Product toProduct() {
        Product product = new Product();
        BeanUtils.copyProperties(this, product);
        return product;
    }

    public static Product toProduct(ProductEntity productEntity) {
        Product product = new Product();
        BeanUtils.copyProperties(productEntity, product);
        return product;
    }

    public static ProductEntity toProductEntity(Product product) {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(product, productEntity);
        return productEntity;
    }

    public static ProductEntity toProductPersonal(Product product) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductType(product.getProductType());
        productEntity.setIdClient(product.getIdClient());
        productEntity.setAvailableBalance(product.getAvailableBalance());
        return productEntity;
    }

    public static ProductEntity toProductEnterprise(Product product) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductType(product.getProductType());
        productEntity.setIdClient(product.getIdClient());
        productEntity.setAvailableBalance(product.getAvailableBalance());
        productEntity.setHolders(product.getHolders());
        productEntity.setSignatories(product.getSignatories());
        return productEntity;
    }


}
