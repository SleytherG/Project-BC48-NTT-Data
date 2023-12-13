package com.nttdata.msvc.product.domain.mapper;

import com.nttdata.msvc.product.domain.model.Client;
import com.nttdata.msvc.product.infrastructure.mongodb.entities.ClientEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

  public final ClientEntity mapClientToClientEntity(Client client) {
    return ClientEntity.builder()
      .id(client.getId())
      .firstName(client.getFirstName())
      .lastName(client.getLastName())
      .documentType(client.getDocumentType())
      .documentNumber(client.getDocumentNumber())
      .clientType(client.getClientType())
      .build();
  }

  public final Client mapClientEntityToClient(ClientEntity clientEntity) {
    return Client.builder()
      .id(clientEntity.getId())
      .firstName(clientEntity.getFirstName())
      .lastName(clientEntity.getLastName())
      .documentType(clientEntity.getDocumentType())
      .documentNumber(clientEntity.getDocumentNumber())
      .clientType(clientEntity.getClientType())
      .build();
  }
}
