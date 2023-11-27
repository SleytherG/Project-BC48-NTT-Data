package com.nttdata.project.bc48.infrastructure.mongodb.persistence;

import com.nttdata.project.bc48.domain.exceptions.ConflictException;
import com.nttdata.project.bc48.domain.exceptions.ProductAlreadyExistsException;
import com.nttdata.project.bc48.domain.model.Movement;
import com.nttdata.project.bc48.domain.model.Product;
import com.nttdata.project.bc48.domain.model.ProductType;
import com.nttdata.project.bc48.domain.persistence.ProductPersistence;
import com.nttdata.project.bc48.infrastructure.api.dtos.*;
import com.nttdata.project.bc48.infrastructure.mongodb.daos.ClientRepository;
import com.nttdata.project.bc48.infrastructure.mongodb.daos.ProductRepository;
import com.nttdata.project.bc48.infrastructure.mongodb.entities.ClientEntity;
import com.nttdata.project.bc48.infrastructure.mongodb.entities.ProductEntity;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.webjars.NotFoundException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.nttdata.project.bc48.utils.Constants.*;

@Repository
public class ProductPersistenceMongodb implements ProductPersistence {

    final private ProductRepository productRepository;
    final private ClientRepository clientRepository;

    public ProductPersistenceMongodb(ProductRepository productRepository, ClientRepository clientRepository) {
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Flowable<Product> findAll() {
        return productRepository
                .findAll()
                .map(productEntity -> ProductEntity.toProduct(productEntity));
    }

    @Override
    public Maybe<Product> findById(String productId) {
        return productRepository
                .findById(productId)
                .switchIfEmpty(Maybe.error(new NotFoundException("Product does not exist: " + productId)))
                .map(productEntity -> ProductEntity.toProduct(productEntity));
    }

    @Override
    public Single<Product> create(Product product) {
        return productRepository
                .findProductEntitiesByIdClient(product.getIdClient())
                .toList()
                .flatMap(productEntities -> Single.fromMaybe(validateClientAndCreateProduct(product, productEntities)));
    }

    private Maybe<Product> validateClientAndCreateProduct(Product product, List<ProductEntity> productsEntities) {
        return clientRepository
                .findById(product.getId())
                .switchIfEmpty(Maybe.error(new NotFoundException("Client does not exist")))
                .flatMap(clientEntity -> {
                    if (clientEntity.getClientType().equals(PERSONAL)) {
                        return createProductForPersonalClient(product, productsEntities);
                    } else if (clientEntity.getClientType().equals(ENTERPRISE)) {
                        return createProductForEnterpriseClient(product, clientEntity);
                    } else {
                        return Maybe.error(new IllegalArgumentException("Invalid client type"));
                    }
                });
    }

    private Maybe<Product> createProductForPersonalClient(Product product, List<ProductEntity> productsEntities) {
        if (productsEntities.stream().anyMatch(p -> p.getProductType().equals(product.getProductType()))) {
            return Maybe.error(new ProductAlreadyExistsException("Product type already exists for the client"));
        } else {
            ProductEntity productEntity = ProductEntity.toProductPersonal(product);
            return productRepository.save(productEntity).map(p -> ProductEntity.toProduct(productEntity)).toMaybe();
        }
    }

    private Maybe<Product> createProductForEnterpriseClient(Product product, ClientEntity clientEntity) {
        return Single.just(product)
                .map(p -> p.getProductType().toString().toUpperCase())
                .filter(productType ->
                        !(productType.equals(ProductType.Passive.getProducts()[0].toUpperCase()) ||
                                productType.equals(ProductType.Passive.getProducts()[2].toUpperCase())))
                .switchIfEmpty(Single.error(new ConflictException("Invalid product for enterprise client")))
                .map(ignored -> ProductEntity.toProductEnterprise(product))
                .flatMap(productEntity -> Single.fromCallable(() -> productRepository.save(productEntity)))
                .flatMap(productEntitySingle -> productEntitySingle.map(productEntity -> ProductEntity.toProduct(productEntity)))
                .toMaybe();
    }

    @Override
    public Maybe<Product> update(Product product) {
        return productRepository
                .findById(product.getId())
                .switchIfEmpty(Maybe.error(new NotFoundException("Non existent product: " + product.getId())))
                .flatMapSingle(productEntity -> {
                    BeanUtils.copyProperties(product, productEntity);
                    return productRepository.save(productEntity);
                })
                .map(productEntity -> ProductEntity.toProduct(productEntity));
    }

    @Override
    public Maybe<Void> delete(String productId) {
        return productRepository
                .findById(productId)
                .switchIfEmpty(Maybe.error(new NotFoundException("Non existent product: " + productId)))
                .flatMap(productEntity -> productRepository.deleteById(productId).andThen(Maybe.empty()));
    }

    @Override
    public Maybe<ProductResponseDTO> deposit(DepositRequestDTO depositRequestDTO) {
        return null;
    }

    @Override
    public Maybe<ProductResponseDTO> withdrawal(WithdrawalRequestDTO withdrawalRequestDTO) {
        return null;
    }

    @Override
    public Maybe<ProductResponseDTO> payCreditProduct(PayCreditProductRequestDTO payCreditProductRequestDTO) {
        return null;
    }

    @Override
    public Maybe<ProductResponseDTO> chargeConsumptionAccordCreditLine(ChargeConsumptionDTO chargeConsumptionDTO) {
        return null;
    }

    @Override
    public Maybe<ProductBalanceResponseDTO> getAvailableBalancePerProduct(Product product) {
        return null;
    }

    @Override
    public Flowable<Movement> getMovementsFromProduct(Movement movement) {
        return null;
    }

}
