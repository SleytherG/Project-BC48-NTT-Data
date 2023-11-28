package com.nttdata.msvc.product.infrastructure.mongodb.daos;


import com.nttdata.msvc.product.infrastructure.mongodb.entities.ClientEntity;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;

public interface ClientRepository extends RxJava3CrudRepository<ClientEntity, String> {
//public interface ClientRepository extends ReactiveMongoRepository<ClientEntity, String> {

//    Mono<Client> save(Client client);
//    Mono<Client> findById(String id);
//    Flux<Client> findAll();
}
