package com.nttdata.msvc.client.infrastructure.api.resources;

import com.nttdata.msvc.client.domain.model.Client;
import com.nttdata.msvc.client.domain.services.ClientService;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
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

    /**
     * Get all Users from DB
     * @return Flowable<Client>
     */
    @GetMapping(produces = {"application/json"})
    public Flowable<Client> findAll() {
        return clientService.findAll();
    }

    /**
     * Create a client and save it to DB
     * Request Body Client Class
     * @return Maybe<Client>
     */
    @PostMapping(produces = {"application/json"})
    public Single<Client> create(@RequestBody Client client) {
        return this.clientService.create(client);
    }

    /**
     * Update a Client from DB
     * @return Maybe<Client>
     *
     */
    @PutMapping(produces = {"application/json"})
    public Maybe<Client> update(@RequestBody Client client) {
        return this.clientService.update(client);
    }

    /**
     * Find a client by ID from DB
     * Path Variable clientID
     * type String
     * @return Maybe<Client>
     */
    @GetMapping(CLIENT_ID)
    public Maybe<Client> findById(@PathVariable String clientId) {
        return this.clientService.findById(clientId);
    }

    /**
     *  Delete a client from DB
     * Path variable clientId
     * @return Maybe<Void>
     */
    @DeleteMapping(CLIENT_ID)
    public Maybe<Void> delete(@PathVariable String clientId) {
        return this.clientService.delete(clientId);
    }
}
