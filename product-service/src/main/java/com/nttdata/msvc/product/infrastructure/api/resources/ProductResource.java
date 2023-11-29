package com.nttdata.msvc.product.infrastructure.api.resources;

import com.nttdata.msvc.product.domain.model.Movement;
import com.nttdata.msvc.product.domain.model.Product;
import com.nttdata.msvc.product.domain.services.ProductService;
import com.nttdata.msvc.product.infrastructure.api.dtos.*;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ProductResource.PRODUCTS)
public class ProductResource {

    public static final String PRODUCTS = "/products";
    public static final String PRODUCT_ID = "/{productId}";
    public static final String DEPOSIT_TO_PRODUCT = "/depositToProduct";
    public static final String WITHDRAWAL_FROM_PRODUCT = "/withdrawalFromProduct";
    public static final String PAY_CREDIT_PRODUCT = "/payCreditProduct";
    public static final String CHARGE_CONSUMPTION_ACCORD_CREDIT_LINE = "/chargeConsumptionAccordCreditLine";
    public static final String GET_AVAILABLE_BALANCE_PER_PRODUCT = "/getAvailableBalancePerProduct";
    public static final String GET_MOVEMENTS_FROM_PRODUCT = "/getMovementsFromProduct";

    private final ProductService productService;

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

    @PostMapping(DEPOSIT_TO_PRODUCT)
    public Maybe<ProductResponseDTO> deposit(@RequestBody DepositRequestDTO depositRequestDTO) {
        return productService.deposit(depositRequestDTO);
    }

    @PostMapping(WITHDRAWAL_FROM_PRODUCT)
    public Maybe<ProductResponseDTO> withdrawal(@RequestBody WithdrawalRequestDTO withdrawalRequestDTO) {
        return productService.withdrawal(withdrawalRequestDTO);
    }

    @PostMapping(PAY_CREDIT_PRODUCT)
    public Maybe<ProductResponseDTO> payCreditProduct(@RequestBody PayCreditProductRequestDTO payCreditProductRequestDTO) {
        return productService.payCreditProduct(payCreditProductRequestDTO);
    }

    @PostMapping(CHARGE_CONSUMPTION_ACCORD_CREDIT_LINE)
    public Maybe<ProductResponseDTO> chargeConsumptionAccordCreditLine(@RequestBody ChargeConsumptionDTO chargeConsumptionDTO) {
        return productService.chargeConsumptionAccordCreditLine(chargeConsumptionDTO);
    }

    @PostMapping(GET_AVAILABLE_BALANCE_PER_PRODUCT)
    public Maybe<ProductBalanceResponseDTO> getAvailableBalancePerProduct(@RequestBody Product product) {
        return productService.getAvailableBalancePerProduct(product);
    }

    @PostMapping(GET_MOVEMENTS_FROM_PRODUCT)
    public Flowable<Movement> getMovementsFromProduct(@RequestBody Product product) {
        return productService.getMovementsFromProduct(product);
    }
}
