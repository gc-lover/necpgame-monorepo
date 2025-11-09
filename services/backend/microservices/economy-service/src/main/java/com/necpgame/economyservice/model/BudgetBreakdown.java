package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * BudgetBreakdown
 */


public class BudgetBreakdown {

  private BigDecimal complexityComponent;

  private BigDecimal riskComponent;

  private BigDecimal marketComponent;

  private BigDecimal timeComponent;

  private BigDecimal manualAdjustments;

  private @Nullable String currency;

  public BudgetBreakdown() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BudgetBreakdown(BigDecimal complexityComponent, BigDecimal riskComponent, BigDecimal marketComponent, BigDecimal timeComponent, BigDecimal manualAdjustments) {
    this.complexityComponent = complexityComponent;
    this.riskComponent = riskComponent;
    this.marketComponent = marketComponent;
    this.timeComponent = timeComponent;
    this.manualAdjustments = manualAdjustments;
  }

  public BudgetBreakdown complexityComponent(BigDecimal complexityComponent) {
    this.complexityComponent = complexityComponent;
    return this;
  }

  /**
   * Вклад сложности.
   * @return complexityComponent
   */
  @NotNull @Valid 
  @Schema(name = "complexityComponent", description = "Вклад сложности.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("complexityComponent")
  public BigDecimal getComplexityComponent() {
    return complexityComponent;
  }

  public void setComplexityComponent(BigDecimal complexityComponent) {
    this.complexityComponent = complexityComponent;
  }

  public BudgetBreakdown riskComponent(BigDecimal riskComponent) {
    this.riskComponent = riskComponent;
    return this;
  }

  /**
   * Вклад риска.
   * @return riskComponent
   */
  @NotNull @Valid 
  @Schema(name = "riskComponent", description = "Вклад риска.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("riskComponent")
  public BigDecimal getRiskComponent() {
    return riskComponent;
  }

  public void setRiskComponent(BigDecimal riskComponent) {
    this.riskComponent = riskComponent;
  }

  public BudgetBreakdown marketComponent(BigDecimal marketComponent) {
    this.marketComponent = marketComponent;
    return this;
  }

  /**
   * Вклад рыночного индекса.
   * @return marketComponent
   */
  @NotNull @Valid 
  @Schema(name = "marketComponent", description = "Вклад рыночного индекса.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("marketComponent")
  public BigDecimal getMarketComponent() {
    return marketComponent;
  }

  public void setMarketComponent(BigDecimal marketComponent) {
    this.marketComponent = marketComponent;
  }

  public BudgetBreakdown timeComponent(BigDecimal timeComponent) {
    this.timeComponent = timeComponent;
    return this;
  }

  /**
   * Вклад временного модификатора.
   * @return timeComponent
   */
  @NotNull @Valid 
  @Schema(name = "timeComponent", description = "Вклад временного модификатора.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timeComponent")
  public BigDecimal getTimeComponent() {
    return timeComponent;
  }

  public void setTimeComponent(BigDecimal timeComponent) {
    this.timeComponent = timeComponent;
  }

  public BudgetBreakdown manualAdjustments(BigDecimal manualAdjustments) {
    this.manualAdjustments = manualAdjustments;
    return this;
  }

  /**
   * Сумма ручных корректировок.
   * @return manualAdjustments
   */
  @NotNull @Valid 
  @Schema(name = "manualAdjustments", description = "Сумма ручных корректировок.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("manualAdjustments")
  public BigDecimal getManualAdjustments() {
    return manualAdjustments;
  }

  public void setManualAdjustments(BigDecimal manualAdjustments) {
    this.manualAdjustments = manualAdjustments;
  }

  public BudgetBreakdown currency(@Nullable String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Код валюты breakdown.
   * @return currency
   */
  
  @Schema(name = "currency", description = "Код валюты breakdown.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency")
  public @Nullable String getCurrency() {
    return currency;
  }

  public void setCurrency(@Nullable String currency) {
    this.currency = currency;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BudgetBreakdown budgetBreakdown = (BudgetBreakdown) o;
    return Objects.equals(this.complexityComponent, budgetBreakdown.complexityComponent) &&
        Objects.equals(this.riskComponent, budgetBreakdown.riskComponent) &&
        Objects.equals(this.marketComponent, budgetBreakdown.marketComponent) &&
        Objects.equals(this.timeComponent, budgetBreakdown.timeComponent) &&
        Objects.equals(this.manualAdjustments, budgetBreakdown.manualAdjustments) &&
        Objects.equals(this.currency, budgetBreakdown.currency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(complexityComponent, riskComponent, marketComponent, timeComponent, manualAdjustments, currency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BudgetBreakdown {\n");
    sb.append("    complexityComponent: ").append(toIndentedString(complexityComponent)).append("\n");
    sb.append("    riskComponent: ").append(toIndentedString(riskComponent)).append("\n");
    sb.append("    marketComponent: ").append(toIndentedString(marketComponent)).append("\n");
    sb.append("    timeComponent: ").append(toIndentedString(timeComponent)).append("\n");
    sb.append("    manualAdjustments: ").append(toIndentedString(manualAdjustments)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
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

