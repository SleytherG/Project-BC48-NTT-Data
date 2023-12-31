package com.nttdata.project.bc48.infrastructure.mongodb.entities;

import com.nttdata.project.bc48.domain.model.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "clients")
public class ClientEntity {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String documentType;
    private String documentNumber;
    private String clientType;

    public Client toClient() {
        Client client = new Client();
        BeanUtils.copyProperties(this, client);
        return client;
    }

    public static ClientEntity toClientEntity(Client client) {
        ClientEntity clientEntity = new ClientEntity();
        BeanUtils.copyProperties(client, clientEntity);
        return clientEntity;
    }
}
