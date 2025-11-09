package com.necpgame.worldservice.model;

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
 * EraMechanicsEconomicState
 */

@JsonTypeName("EraMechanics_economic_state")

public class EraMechanicsEconomicState {

  private @Nullable BigDecimal inflationRate;

  private @Nullable BigDecimal averagePricesMultiplier;

  private @Nullable String currencyStability;

  public EraMechanicsEconomicState inflationRate(@Nullable BigDecimal inflationRate) {
    this.inflationRate = inflationRate;
    return this;
  }

  /**
   * Get inflationRate
   * @return inflationRate
   */
  @Valid 
  @Schema(name = "inflation_rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inflation_rate")
  public @Nullable BigDecimal getInflationRate() {
    return inflationRate;
  }

  public void setInflationRate(@Nullable BigDecimal inflationRate) {
    this.inflationRate = inflationRate;
  }

  public EraMechanicsEconomicState averagePricesMultiplier(@Nullable BigDecimal averagePricesMultiplier) {
    this.averagePricesMultiplier = averagePricesMultiplier;
    return this;
  }

  /**
   * Get averagePricesMultiplier
   * @return averagePricesMultiplier
   */
  @Valid 
  @Schema(name = "average_prices_multiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("average_prices_multiplier")
  public @Nullable BigDecimal getAveragePricesMultiplier() {
    return averagePricesMultiplier;
  }

  public void setAveragePricesMultiplier(@Nullable BigDecimal averagePricesMultiplier) {
    this.averagePricesMultiplier = averagePricesMultiplier;
  }

  public EraMechanicsEconomicState currencyStability(@Nullable String currencyStability) {
    this.currencyStability = currencyStability;
    return this;
  }

  /**
   * Get currencyStability
   * @return currencyStability
   */
  
  @Schema(name = "currency_stability", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency_stability")
  public @Nullable String getCurrencyStability() {
    return currencyStability;
  }

  public void setCurrencyStability(@Nullable String currencyStability) {
    this.currencyStability = currencyStability;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EraMechanicsEconomicState eraMechanicsEconomicState = (EraMechanicsEconomicState) o;
    return Objects.equals(this.inflationRate, eraMechanicsEconomicState.inflationRate) &&
        Objects.equals(this.averagePricesMultiplier, eraMechanicsEconomicState.averagePricesMultiplier) &&
        Objects.equals(this.currencyStability, eraMechanicsEconomicState.currencyStability);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inflationRate, averagePricesMultiplier, currencyStability);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EraMechanicsEconomicState {\n");
    sb.append("    inflationRate: ").append(toIndentedString(inflationRate)).append("\n");
    sb.append("    averagePricesMultiplier: ").append(toIndentedString(averagePricesMultiplier)).append("\n");
    sb.append("    currencyStability: ").append(toIndentedString(currencyStability)).append("\n");
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

