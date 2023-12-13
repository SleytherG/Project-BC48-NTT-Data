package com.nttdata.msvc.product.domain.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CheckingAccount extends Account {

  /**
   * Array of holders associated with the checking account.
   */
  private List<String> holders;

  /**
   * Array of signatories associated with the checking account.
   */
  private List<String> signatories;
}
