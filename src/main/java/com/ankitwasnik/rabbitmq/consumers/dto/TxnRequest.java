package com.ankitwasnik.rabbitmq.consumers.dto;

import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TxnRequest {

  @NotNull
  private UUID referenceId;
  @NotBlank
  private String responseCode;
  @NotNull
  private Double amount;
}
