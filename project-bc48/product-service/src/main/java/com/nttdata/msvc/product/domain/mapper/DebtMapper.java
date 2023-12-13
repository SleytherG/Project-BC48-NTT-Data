package com.nttdata.msvc.product.domain.mapper;

import com.nttdata.msvc.product.domain.model.Client;
import com.nttdata.msvc.product.domain.model.Debt;
import com.nttdata.msvc.product.infrastructure.mongodb.entities.ClientEntity;
import com.nttdata.msvc.product.infrastructure.mongodb.entities.DebtEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DebtMapper {

  public final Debt mapDebtEntityToDebt(DebtEntity debtEntity) {
    return Debt.builder()
      .id(debtEntity.getId())
      .clientId(debtEntity.getClientId())
      .amount(debtEntity.getAmount())
      .payDayLimit(debtEntity.getPayDayLimit())
      .build();
  }

  public final List<Debt> mapListDebtEntitiesToListDebt(List<DebtEntity> debtEntities) {
    return debtEntities
      .stream()
      .map(this::mapDebtEntityToDebt)
      .collect(Collectors.toList());
  }
}
