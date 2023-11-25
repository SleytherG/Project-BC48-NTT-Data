package com.nttdata.project.bc48.infrastructure.mongodb.daos;


import com.nttdata.project.bc48.domain.model.Client;
import com.nttdata.project.bc48.infrastructure.mongodb.entities.ClientEntity;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.SingleSource;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;

public interface ClientRepository extends RxJava3CrudRepository<ClientEntity, String> {
//public interface ClientRepository extends ReactiveMongoRepository<ClientEntity, String> {

//    Mono<Client> save(Client client);
//    Mono<Client> findById(String id);
//    Flux<Client> findAll();
}
