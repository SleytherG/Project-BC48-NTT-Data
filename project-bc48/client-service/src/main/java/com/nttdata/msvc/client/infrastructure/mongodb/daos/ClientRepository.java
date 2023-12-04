package com.nttdata.msvc.client.infrastructure.mongodb.daos;


import com.nttdata.msvc.client.infrastructure.mongodb.entities.ClientEntity;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;

public interface ClientRepository extends RxJava3CrudRepository<ClientEntity, String> {
}
