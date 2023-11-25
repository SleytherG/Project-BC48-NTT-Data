package com.nttdata.project.bc48.infrastructure.mongodb.persistence;

import com.nttdata.project.bc48.domain.exceptions.ConflictException;
import com.nttdata.project.bc48.domain.model.Client;
import com.nttdata.project.bc48.domain.persistence.ClientPersistence;
import com.nttdata.project.bc48.infrastructure.mongodb.daos.ClientRepository;
import com.nttdata.project.bc48.infrastructure.mongodb.entities.ClientEntity;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.webjars.NotFoundException;

@Repository
public class ClientPersistenceMongodb implements ClientPersistence {

    private ClientRepository clientRepository;

    public ClientPersistenceMongodb(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Flowable<Client> findAll() {
        return clientRepository
                .findAll()
                .map(ClientEntity::toClient);
    }

    @Override
    public Maybe<Client> findById(String clientId) {
        return clientRepository
                .findById(clientId)
                .switchIfEmpty(Maybe.error(new ConflictException("Client does not exist: " + clientId)))
                .map(ClientEntity::toClient);
    }

    @Override
    public Maybe<Client> create(Client client) {
        ClientEntity clientEntity = ClientEntity.toClientEntity(client);
        return Maybe.fromSingle(clientRepository.save(clientEntity))
                .map(ClientEntity::toClient);
    }

    @Override
    public Maybe<Client> update(Client client) {
        return clientRepository
                .findById(client.getId())
                .switchIfEmpty(Maybe.error(new NotFoundException("Non existent client: " + client.getId())))
                .flatMapSingle((clientEntity) -> {
                    BeanUtils.copyProperties(client, clientEntity);
                    return clientRepository.save(clientEntity);
                })
                .map(ClientEntity::toClient);
    }

    @Override
    public Maybe<Void> delete(String clientId) {
        return clientRepository
                .findById(clientId)
                .switchIfEmpty(Maybe.error(new NotFoundException("Non existent client: " + clientId)))
                .flatMap(clientEntity -> {
                    return clientRepository.deleteById(clientEntity.getId()).andThen(Maybe.empty());
                });
    }
}
