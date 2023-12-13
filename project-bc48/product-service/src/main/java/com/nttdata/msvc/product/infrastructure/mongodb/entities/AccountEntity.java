package com.nttdata.msvc.product.infrastructure.mongodb.entities;

import com.nttdata.msvc.product.domain.model.Movement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "accounts")
public class AccountEntity {

    @Id
    private String id;
    private String clientId;
    private String accountNumber;
    private String cci;
    private String createdAt;
    private BigDecimal availableBalance;
    private List<Movement> movements;


}
