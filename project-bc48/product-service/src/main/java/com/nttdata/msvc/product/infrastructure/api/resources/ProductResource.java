package com.nttdata.msvc.product.infrastructure.api.resources;

import com.nttdata.msvc.product.domain.model.Comission;
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
    public static final String CREATE_PERSONAL_PRODUCT = "/createPersonalProduct";
    public static final String CREATE_ENTERPRISE_PRODUCT = "/createEnterpriseProduct";
    public static final String GET_ALL_AVAILABLE_BALANCES_FROM_PRODUCT = "/getAllAvailableBalancesFromProduct";
    public static final String GET_ALL_COMISSIONS_OF_CLIENT_PRODUCT = "/getAllComissionsOfClientProduct";

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

    @GetMapping(value = PRODUCT_ID, produces = {"application/json"})
    public Maybe<Product> findById(@PathVariable String productId) {
        return productService.findById(productId);
    }

    @DeleteMapping(value = PRODUCT_ID, produces = {"application/json"})
    public Maybe<Void> delete(@PathVariable String productId) {
        return productService.delete(productId);
    }

    @PostMapping(value = DEPOSIT_TO_PRODUCT, produces = {"application/json"})
    public Maybe<ProductResponseDTO> deposit(@RequestBody DepositRequestDTO depositRequestDTO) {
        return productService.deposit(depositRequestDTO);
    }

    @PostMapping(value = WITHDRAWAL_FROM_PRODUCT, produces = {"application/json"})
    public Maybe<ProductResponseDTO> withdrawal(@RequestBody WithdrawalRequestDTO withdrawalRequestDTO) {
        return productService.withdrawal(withdrawalRequestDTO);
    }

    @PostMapping(value = PAY_CREDIT_PRODUCT, produces = {"application/json"})
    public Maybe<ProductResponseDTO> payCreditProduct(@RequestBody PayCreditProductRequestDTO payCreditProductRequestDTO) {
        return productService.payCreditProduct(payCreditProductRequestDTO);
    }

    @PostMapping(value = CHARGE_CONSUMPTION_ACCORD_CREDIT_LINE, produces = {"application/json"})
    public Maybe<ProductResponseDTO> chargeConsumptionAccordCreditLine(@RequestBody ChargeConsumptionDTO chargeConsumptionDTO) {
        return productService.chargeConsumptionAccordCreditLine(chargeConsumptionDTO);
    }

    @PostMapping(value = GET_AVAILABLE_BALANCE_PER_PRODUCT, produces = {"application/json"})
    public Maybe<ProductBalanceResponseDTO> getAvailableBalancePerProduct(@RequestBody Product product) {
        return productService.getAvailableBalancePerProduct(product);
    }

    @PostMapping(value = GET_MOVEMENTS_FROM_PRODUCT, produces = {"application/json"})
    public Flowable<Movement> getMovementsFromProduct(@RequestBody Product product) {
        return productService.getMovementsFromProduct(product);
    }

    @PostMapping(value = CREATE_PERSONAL_PRODUCT, produces = {"application/json"})
    public Single<Product> createPersonalProduct(@RequestBody Product product) {
        return productService.createPersonalProduct(product);
    }

    @PostMapping(value = CREATE_ENTERPRISE_PRODUCT, produces = {"application/json"})
    public Single<Product> createEnterpriseProduct(@RequestBody Product product) {
        return productService.createEnterpriseProduct(product);
    }

    @PostMapping(value = GET_ALL_AVAILABLE_BALANCES_FROM_PRODUCT, produces = {"application/json"})
    public Flowable<AvailableBalanceDTO> getAllAvailableBalances(@RequestBody AvailableBalanceDTO availableBalanceDTO) {
        return productService.getAllAvailableBalances(availableBalanceDTO.getIdClient());
    }

    @PostMapping(value = GET_ALL_COMISSIONS_OF_CLIENT_PRODUCT, produces = {"application/json"})
    public Flowable<Comission> getAllComissionsOfAClientProduct(@RequestBody Comission comission) {
        return productService.getAllComissionsOfAClientProduct(comission);
    }

}
