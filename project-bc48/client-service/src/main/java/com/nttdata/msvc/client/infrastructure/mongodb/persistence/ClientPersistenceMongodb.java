package com.nttdata.msvc.client.infrastructure.mongodb.persistence;

import com.nttdata.msvc.client.domain.exceptions.ConflictException;
import com.nttdata.msvc.client.domain.mapper.ClientMapper;
import com.nttdata.msvc.client.domain.model.Client;
import com.nttdata.msvc.client.domain.persistence.ClientPersistence;
import com.nttdata.msvc.client.infrastructure.mongodb.daos.ClientRepository;
import com.nttdata.msvc.client.infrastructure.mongodb.entities.ClientEntity;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.webjars.NotFoundException;

import java.util.stream.Stream;

import static com.nttdata.msvc.client.utils.Constants.*;

@Repository
public class ClientPersistenceMongodb implements ClientPersistence {

  private final ClientRepository clientRepository;
  private final ClientMapper clientMapper;

  public ClientPersistenceMongodb(ClientRepository clientRepository, ClientMapper clientMapper) {
    this.clientRepository = clientRepository;
    this.clientMapper = clientMapper;
  }

  @Override
  public Flowable<Client> findAll() {
    return clientRepository
      .findAll()
      .map(this.clientMapper::mapClientEntityToClient);
  }

  @Override
  public Maybe<Client> findById(String clientId) {
    return clientRepository
      .findById(clientId)
      .switchIfEmpty(Maybe.error(new ConflictException("Client does not exist: " + clientId)))
      .map(this.clientMapper::mapClientEntityToClient);
  }

  @Override
  public Single<Client> create(Client client) {
    ClientEntity clientEntity = this.clientMapper.mapClientToClientEntity(client);
    if (clientEntity.getClientType().equals(PERSONAL)) clientEntity.setClientTypeDescription(PERSONAL_DESC);
    if (clientEntity.getClientType().equals(ENTERPRISE)) clientEntity.setClientTypeDescription(ENTERPRISE_DESC);
    return clientRepository.save(clientEntity)
      .map(this.clientMapper::mapClientEntityToClient);
  }

  @Override
  public Maybe<Client> update(Client client) {
    return clientRepository
      .findById(client.getId())
      .switchIfEmpty(Maybe.error(new NotFoundException("Non existent client: " + client.getId())))
      .flatMapSingle(clientEntity -> {
        BeanUtils.copyProperties(client, clientEntity);
        if (clientEntity.getClientType().equals(PERSONAL)) clientEntity.setClientTypeDescription(PERSONAL_DESC);
        if (clientEntity.getClientType().equals(ENTERPRISE)) clientEntity.setClientTypeDescription(ENTERPRISE_DESC);
        return clientRepository.save(clientEntity);
      })
      .map(this.clientMapper::mapClientEntityToClient);
  }

  @Override
  public Maybe<Void> delete(String clientId) {
    return clientRepository
      .findById(clientId)
      .switchIfEmpty(Maybe.error(new NotFoundException("Non existent client: " + clientId)))
      .flatMap(clientEntity -> clientRepository.deleteById(clientEntity.getId()).andThen(Maybe.empty()));
  }
}
