package com.nttdata.project.bc48.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Movement {

    private String id;
    private String idProduct;
    private String amount;
    private String description;
    private MovementType movementType;
    private String operationDate;
    private String observation;

}
