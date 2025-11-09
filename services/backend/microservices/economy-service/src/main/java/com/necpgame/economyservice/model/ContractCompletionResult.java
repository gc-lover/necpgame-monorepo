package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.ContractCompletionResultReputationChanges;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ContractCompletionResult
 */


public class ContractCompletionResult {

  private @Nullable UUID contractId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime completedAt;

  private @Nullable Integer paymentReleased;

  private @Nullable Integer collateralReturned;

  private @Nullable ContractCompletionResultReputationChanges reputationChanges;

  private @Nullable Integer bonusesApplied;

  public ContractCompletionResult contractId(@Nullable UUID contractId) {
    this.contractId = contractId;
    return this;
  }

  /**
   * Get contractId
   * @return contractId
   */
  @Valid 
  @Schema(name = "contract_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contract_id")
  public @Nullable UUID getContractId() {
    return contractId;
  }

  public void setContractId(@Nullable UUID contractId) {
    this.contractId = contractId;
  }

  public ContractCompletionResult completedAt(@Nullable OffsetDateTime completedAt) {
    this.completedAt = completedAt;
    return this;
  }

  /**
   * Get completedAt
   * @return completedAt
   */
  @Valid 
  @Schema(name = "completed_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completed_at")
  public @Nullable OffsetDateTime getCompletedAt() {
    return completedAt;
  }

  public void setCompletedAt(@Nullable OffsetDateTime completedAt) {
    this.completedAt = completedAt;
  }

  public ContractCompletionResult paymentReleased(@Nullable Integer paymentReleased) {
    this.paymentReleased = paymentReleased;
    return this;
  }

  /**
   * Get paymentReleased
   * @return paymentReleased
   */
  
  @Schema(name = "payment_released", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("payment_released")
  public @Nullable Integer getPaymentReleased() {
    return paymentReleased;
  }

  public void setPaymentReleased(@Nullable Integer paymentReleased) {
    this.paymentReleased = paymentReleased;
  }

  public ContractCompletionResult collateralReturned(@Nullable Integer collateralReturned) {
    this.collateralReturned = collateralReturned;
    return this;
  }

  /**
   * Get collateralReturned
   * @return collateralReturned
   */
  
  @Schema(name = "collateral_returned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("collateral_returned")
  public @Nullable Integer getCollateralReturned() {
    return collateralReturned;
  }

  public void setCollateralReturned(@Nullable Integer collateralReturned) {
    this.collateralReturned = collateralReturned;
  }

  public ContractCompletionResult reputationChanges(@Nullable ContractCompletionResultReputationChanges reputationChanges) {
    this.reputationChanges = reputationChanges;
    return this;
  }

  /**
   * Get reputationChanges
   * @return reputationChanges
   */
  @Valid 
  @Schema(name = "reputation_changes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_changes")
  public @Nullable ContractCompletionResultReputationChanges getReputationChanges() {
    return reputationChanges;
  }

  public void setReputationChanges(@Nullable ContractCompletionResultReputationChanges reputationChanges) {
    this.reputationChanges = reputationChanges;
  }

  public ContractCompletionResult bonusesApplied(@Nullable Integer bonusesApplied) {
    this.bonusesApplied = bonusesApplied;
    return this;
  }

  /**
   * Get bonusesApplied
   * @return bonusesApplied
   */
  
  @Schema(name = "bonuses_applied", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonuses_applied")
  public @Nullable Integer getBonusesApplied() {
    return bonusesApplied;
  }

  public void setBonusesApplied(@Nullable Integer bonusesApplied) {
    this.bonusesApplied = bonusesApplied;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContractCompletionResult contractCompletionResult = (ContractCompletionResult) o;
    return Objects.equals(this.contractId, contractCompletionResult.contractId) &&
        Objects.equals(this.completedAt, contractCompletionResult.completedAt) &&
        Objects.equals(this.paymentReleased, contractCompletionResult.paymentReleased) &&
        Objects.equals(this.collateralReturned, contractCompletionResult.collateralReturned) &&
        Objects.equals(this.reputationChanges, contractCompletionResult.reputationChanges) &&
        Objects.equals(this.bonusesApplied, contractCompletionResult.bonusesApplied);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contractId, completedAt, paymentReleased, collateralReturned, reputationChanges, bonusesApplied);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContractCompletionResult {\n");
    sb.append("    contractId: ").append(toIndentedString(contractId)).append("\n");
    sb.append("    completedAt: ").append(toIndentedString(completedAt)).append("\n");
    sb.append("    paymentReleased: ").append(toIndentedString(paymentReleased)).append("\n");
    sb.append("    collateralReturned: ").append(toIndentedString(collateralReturned)).append("\n");
    sb.append("    reputationChanges: ").append(toIndentedString(reputationChanges)).append("\n");
    sb.append("    bonusesApplied: ").append(toIndentedString(bonusesApplied)).append("\n");
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

