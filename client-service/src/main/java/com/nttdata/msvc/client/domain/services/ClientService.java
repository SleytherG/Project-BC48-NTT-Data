package com.nttdata.msvc.client.domain.services;

import com.nttdata.msvc.client.domain.model.Client;
import com.nttdata.msvc.client.domain.persistence.ClientPersistence;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.stereotype.Service;


@Service
public class ClientService {

    private final ClientPersistence clientPersistence;

    public ClientService(ClientPersistence clientPersistence) {
        this.clientPersistence = clientPersistence;
    }

    public Flowable<Client> findAll() {
        return clientPersistence.findAll();
    }

    public Single<Client> create(Client client) {
        return clientPersistence.create(client);
    }

    public Maybe<Client> findById(String clientId) {
        return clientPersistence.findById(clientId);
    }

    public Maybe<Void> delete(String clientId) {
        return clientPersistence.delete(clientId);
    }

    public Maybe<Client> update(Client client) {
        return clientPersistence.update(client);
    }
}
