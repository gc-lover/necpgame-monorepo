package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
 * PlayerOrderBudgetHistoryItem
 */


public class PlayerOrderBudgetHistoryItem {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime calculatedAt;

  private BigDecimal baseReward;

  private @Nullable BigDecimal escrow;

  private @Nullable BigDecimal commission;

  private @Nullable BigDecimal medianDeviation;

  public PlayerOrderBudgetHistoryItem() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderBudgetHistoryItem(OffsetDateTime calculatedAt, BigDecimal baseReward) {
    this.calculatedAt = calculatedAt;
    this.baseReward = baseReward;
  }

  public PlayerOrderBudgetHistoryItem calculatedAt(OffsetDateTime calculatedAt) {
    this.calculatedAt = calculatedAt;
    return this;
  }

  /**
   * Get calculatedAt
   * @return calculatedAt
   */
  @NotNull @Valid 
  @Schema(name = "calculatedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("calculatedAt")
  public OffsetDateTime getCalculatedAt() {
    return calculatedAt;
  }

  public void setCalculatedAt(OffsetDateTime calculatedAt) {
    this.calculatedAt = calculatedAt;
  }

  public PlayerOrderBudgetHistoryItem baseReward(BigDecimal baseReward) {
    this.baseReward = baseReward;
    return this;
  }

  /**
   * Get baseReward
   * @return baseReward
   */
  @NotNull @Valid 
  @Schema(name = "baseReward", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("baseReward")
  public BigDecimal getBaseReward() {
    return baseReward;
  }

  public void setBaseReward(BigDecimal baseReward) {
    this.baseReward = baseReward;
  }

  public PlayerOrderBudgetHistoryItem escrow(@Nullable BigDecimal escrow) {
    this.escrow = escrow;
    return this;
  }

  /**
   * Get escrow
   * @return escrow
   */
  @Valid 
  @Schema(name = "escrow", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("escrow")
  public @Nullable BigDecimal getEscrow() {
    return escrow;
  }

  public void setEscrow(@Nullable BigDecimal escrow) {
    this.escrow = escrow;
  }

  public PlayerOrderBudgetHistoryItem commission(@Nullable BigDecimal commission) {
    this.commission = commission;
    return this;
  }

  /**
   * Get commission
   * @return commission
   */
  @Valid 
  @Schema(name = "commission", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("commission")
  public @Nullable BigDecimal getCommission() {
    return commission;
  }

  public void setCommission(@Nullable BigDecimal commission) {
    this.commission = commission;
  }

  public PlayerOrderBudgetHistoryItem medianDeviation(@Nullable BigDecimal medianDeviation) {
    this.medianDeviation = medianDeviation;
    return this;
  }

  /**
   * Отклонение от медианы на момент пересчёта.
   * @return medianDeviation
   */
  @Valid 
  @Schema(name = "medianDeviation", description = "Отклонение от медианы на момент пересчёта.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("medianDeviation")
  public @Nullable BigDecimal getMedianDeviation() {
    return medianDeviation;
  }

  public void setMedianDeviation(@Nullable BigDecimal medianDeviation) {
    this.medianDeviation = medianDeviation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderBudgetHistoryItem playerOrderBudgetHistoryItem = (PlayerOrderBudgetHistoryItem) o;
    return Objects.equals(this.calculatedAt, playerOrderBudgetHistoryItem.calculatedAt) &&
        Objects.equals(this.baseReward, playerOrderBudgetHistoryItem.baseReward) &&
        Objects.equals(this.escrow, playerOrderBudgetHistoryItem.escrow) &&
        Objects.equals(this.commission, playerOrderBudgetHistoryItem.commission) &&
        Objects.equals(this.medianDeviation, playerOrderBudgetHistoryItem.medianDeviation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(calculatedAt, baseReward, escrow, commission, medianDeviation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderBudgetHistoryItem {\n");
    sb.append("    calculatedAt: ").append(toIndentedString(calculatedAt)).append("\n");
    sb.append("    baseReward: ").append(toIndentedString(baseReward)).append("\n");
    sb.append("    escrow: ").append(toIndentedString(escrow)).append("\n");
    sb.append("    commission: ").append(toIndentedString(commission)).append("\n");
    sb.append("    medianDeviation: ").append(toIndentedString(medianDeviation)).append("\n");
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

