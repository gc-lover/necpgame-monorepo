package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Ручная корректировка бюджета оператором.
 */

@Schema(name = "BudgetEstimateRequest_manualAdjustment", description = "Ручная корректировка бюджета оператором.")
@JsonTypeName("BudgetEstimateRequest_manualAdjustment")

public class BudgetEstimateRequestManualAdjustment {

  private BigDecimal amount;

  private String reason;

  public BudgetEstimateRequestManualAdjustment() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BudgetEstimateRequestManualAdjustment(BigDecimal amount, String reason) {
    this.amount = amount;
    this.reason = reason;
  }

  public BudgetEstimateRequestManualAdjustment amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Корректирующая сумма.
   * @return amount
   */
  @NotNull @Valid 
  @Schema(name = "amount", description = "Корректирующая сумма.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("amount")
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public BudgetEstimateRequestManualAdjustment reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Обоснование корректировки.
   * @return reason
   */
  @NotNull 
  @Schema(name = "reason", description = "Обоснование корректировки.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BudgetEstimateRequestManualAdjustment budgetEstimateRequestManualAdjustment = (BudgetEstimateRequestManualAdjustment) o;
    return Objects.equals(this.amount, budgetEstimateRequestManualAdjustment.amount) &&
        Objects.equals(this.reason, budgetEstimateRequestManualAdjustment.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BudgetEstimateRequestManualAdjustment {\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

