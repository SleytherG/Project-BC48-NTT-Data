package com.nttdata.msvc.product.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    private String id;
    private String firstName;
    private String lastName;
    private String documentType;
    private String documentNumber;
    private ClientType clientType;
}
