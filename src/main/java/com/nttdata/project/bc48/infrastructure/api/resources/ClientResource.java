package com.nttdata.project.bc48.infrastructure.api.resources;

import com.nttdata.project.bc48.domain.model.Client;
import com.nttdata.project.bc48.domain.services.ClientService;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ClientResource.CLIENTS)
public class ClientResource {

    public static final String CLIENTS = "/clients";
    public static final String CLIENT_ID = "/{clientId}";

    final private ClientService clientService;

    public ClientResource(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(produces = {"application/json"})
    public Flowable<Client> findAll() {
        return clientService.findAll();
    }

    @PostMapping(produces = {"application/json"})
    public Maybe<Client> create(@RequestBody Client client) {
        return this.clientService.create(client);
    }

    @PutMapping(produces = {"application/json"})
    public Maybe<Client> update(@RequestBody Client client) {
        return this.clientService.update(client);
    }

    @GetMapping(CLIENT_ID)
    public Maybe<Client> findById(@PathVariable String clientId) {
        return this.clientService.findById(clientId);
    }

    @DeleteMapping(CLIENT_ID)
    public Maybe<Void> delete(@PathVariable String clientId) {
        return this.clientService.delete(clientId);
    }
}
