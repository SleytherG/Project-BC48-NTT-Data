package com.nttdata.msvc.client.domain.persistence;


import com.nttdata.msvc.client.domain.model.Client;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientPersistence {

    Flowable<Client> findAll();
    Maybe<Client> findById(String clientId);
    Single<Client> create(Client client);
    Maybe<Client> update(Client client);
    Maybe<Void> delete(String clientId);
}
