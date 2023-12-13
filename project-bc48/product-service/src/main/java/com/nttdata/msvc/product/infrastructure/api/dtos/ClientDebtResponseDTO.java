package com.nttdata.msvc.product.infrastructure.api.dtos;

import com.nttdata.msvc.product.domain.mapper.DebtMapper;
import com.nttdata.msvc.product.domain.model.Client;
import com.nttdata.msvc.product.domain.model.Debt;
import com.nttdata.msvc.product.infrastructure.mongodb.entities.DebtEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ClientDebtResponseDTO {

  private Client client;
  private List<Debt> debts;


  public static ClientDebtResponseDTO mapClientAndDebtsToClientDebtResponseDTO(Client clientToMap, List<Debt> debtsToMap) {
    return ClientDebtResponseDTO
      .builder()
      .client(clientToMap)
      .debts(debtsToMap)
      .build();
  }

}
