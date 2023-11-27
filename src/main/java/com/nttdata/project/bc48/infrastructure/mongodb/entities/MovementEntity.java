package com.nttdata.project.bc48.infrastructure.mongodb.entities;

import com.nttdata.project.bc48.domain.model.Movement;
import com.nttdata.project.bc48.domain.model.MovementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "movements")
public class MovementEntity {

    @Id
    private String id;
    private String idProduct;
    private String amount;
    private String description;
    private MovementType movementType;
    private String operationDate;
    private String observation;

    public Movement toMovement() {
        Movement movement = new Movement();
        BeanUtils.copyProperties(this, movement);
        return movement;
    }

    public static MovementEntity toMovementEntity(Movement movement) {
        MovementEntity movementEntity = new MovementEntity();
        BeanUtils.copyProperties(movement, movementEntity);
        return movementEntity;
    }
}
