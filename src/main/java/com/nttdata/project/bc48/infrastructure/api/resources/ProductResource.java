package com.nttdata.project.bc48.infrastructure.api.resources;

import com.nttdata.project.bc48.domain.model.Product;
import com.nttdata.project.bc48.domain.services.ProductService;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ProductResource.PRODUCTS)
public class ProductResource {

    public static final String PRODUCTS = "/products";
    public static final String PRODUCT_ID = "/{productId}";

    final private ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(produces = {"application/json"})
    public Flowable<Product> findAll() {
        return productService.findAll();
    }

    @PostMapping(produces = {"application/json"})
    public Single<Product> create(@RequestBody Product product) {
        return productService.create(product);
    }

    @PutMapping(produces = {"application/json"})
    public Maybe<Product> update(@RequestBody Product product) {
        return productService.update(product);
    }

    @GetMapping(PRODUCT_ID)
    public Maybe<Product> findById(@PathVariable String productId) {
        return productService.findById(productId);
    }

    @DeleteMapping(PRODUCT_ID)
    public Maybe<Void> delete(@PathVariable String productId) {
        return productService.delete(productId);
    }

}
