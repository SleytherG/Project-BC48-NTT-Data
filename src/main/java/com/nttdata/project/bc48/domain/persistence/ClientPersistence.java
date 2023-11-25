package com.nttdata.project.bc48.domain.persistence;


import com.nttdata.project.bc48.domain.model.Client;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientPersistence {

//    Mono<Client> save(Client client);
//    Mono<Client> findById(String id);
//    Flux<Client> findAll();

    Flowable<Client> findAll();
    Maybe<Client> findById(String clientId);
    Maybe<Client> create(Client client);
    Maybe<Client> update(Client client);
    Maybe<Void> delete(String clientId);
}
