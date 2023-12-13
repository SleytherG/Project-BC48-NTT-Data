package com.nttdata.msvc.client.infrastructure.api.resources;

import com.nttdata.msvc.client.domain.model.Client;
import com.nttdata.msvc.client.domain.services.ClientService;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ClientResource.CLIENTS)
public class ClientResource {

    public static final String CLIENTS = "/clients";
    public static final String CLIENT_ID = "/{clientId}";

    private final ClientService clientService;

    public ClientResource(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Get all clients from DB
     * @return Flowable<Client>
     */
    @Operation(summary = "Get all clients from DB")
    @GetMapping(produces = {"application/json"})
    public Flowable<Client> findAll() {
        return clientService.findAll();
    }

    /**
     * Create a client and save it to DB
     * Request Body Client Class
     * @return Maybe<Client>
     */
    @Operation(summary = "Create a client and save it to DB")
    @PostMapping(produces = {"application/json"})
    public Single<Client> create(@RequestBody Client client) {
        return this.clientService.create(client);
    }

    /**
     * Update a Client from DB
     * @return Maybe<Client>
     *
     */
    @Operation(summary = "Update a client from DB")
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
    @Operation(summary = "Find a client by ID from DB")
    @GetMapping(CLIENT_ID)
    public Maybe<Client> findById(@PathVariable String clientId) {
        return this.clientService.findById(clientId);
    }

    /**
     *  Delete a client from DB
     * Path variable clientId
     * @return Maybe<Void>
     */
    @Operation(summary = "Delete a client by ID from DB")
    @DeleteMapping(CLIENT_ID)
    public Maybe<Void> delete(@PathVariable String clientId) {
        return this.clientService.delete(clientId);
    }
}
