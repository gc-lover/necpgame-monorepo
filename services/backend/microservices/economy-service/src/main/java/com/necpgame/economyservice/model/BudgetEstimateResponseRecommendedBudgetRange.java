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
 * Рекомендованный диапазон бюджета.
 */

@Schema(name = "BudgetEstimateResponse_recommendedBudgetRange", description = "Рекомендованный диапазон бюджета.")
@JsonTypeName("BudgetEstimateResponse_recommendedBudgetRange")

public class BudgetEstimateResponseRecommendedBudgetRange {

  private BigDecimal min;

  private BigDecimal max;

  public BudgetEstimateResponseRecommendedBudgetRange() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BudgetEstimateResponseRecommendedBudgetRange(BigDecimal min, BigDecimal max) {
    this.min = min;
    this.max = max;
  }

  public BudgetEstimateResponseRecommendedBudgetRange min(BigDecimal min) {
    this.min = min;
    return this;
  }

  /**
   * Get min
   * minimum: 0
   * @return min
   */
  @NotNull @Valid @DecimalMin(value = "0") 
  @Schema(name = "min", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("min")
  public BigDecimal getMin() {
    return min;
  }

  public void setMin(BigDecimal min) {
    this.min = min;
  }

  public BudgetEstimateResponseRecommendedBudgetRange max(BigDecimal max) {
    this.max = max;
    return this;
  }

  /**
   * Get max
   * minimum: 0
   * @return max
   */
  @NotNull @Valid @DecimalMin(value = "0") 
  @Schema(name = "max", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("max")
  public BigDecimal getMax() {
    return max;
  }

  public void setMax(BigDecimal max) {
    this.max = max;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BudgetEstimateResponseRecommendedBudgetRange budgetEstimateResponseRecommendedBudgetRange = (BudgetEstimateResponseRecommendedBudgetRange) o;
    return Objects.equals(this.min, budgetEstimateResponseRecommendedBudgetRange.min) &&
        Objects.equals(this.max, budgetEstimateResponseRecommendedBudgetRange.max);
  }

  @Override
  public int hashCode() {
    return Objects.hash(min, max);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BudgetEstimateResponseRecommendedBudgetRange {\n");
    sb.append("    min: ").append(toIndentedString(min)).append("\n");
    sb.append("    max: ").append(toIndentedString(max)).append("\n");
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

