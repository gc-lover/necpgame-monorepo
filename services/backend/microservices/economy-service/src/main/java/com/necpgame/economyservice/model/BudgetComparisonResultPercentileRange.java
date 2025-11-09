package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * BudgetComparisonResultPercentileRange
 */

@JsonTypeName("BudgetComparisonResult_percentileRange")

public class BudgetComparisonResultPercentileRange {

  private @Nullable Float p25;

  private @Nullable Float p75;

  public BudgetComparisonResultPercentileRange p25(@Nullable Float p25) {
    this.p25 = p25;
    return this;
  }

  /**
   * Get p25
   * @return p25
   */
  
  @Schema(name = "p25", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("p25")
  public @Nullable Float getP25() {
    return p25;
  }

  public void setP25(@Nullable Float p25) {
    this.p25 = p25;
  }

  public BudgetComparisonResultPercentileRange p75(@Nullable Float p75) {
    this.p75 = p75;
    return this;
  }

  /**
   * Get p75
   * @return p75
   */
  
  @Schema(name = "p75", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("p75")
  public @Nullable Float getP75() {
    return p75;
  }

  public void setP75(@Nullable Float p75) {
    this.p75 = p75;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BudgetComparisonResultPercentileRange budgetComparisonResultPercentileRange = (BudgetComparisonResultPercentileRange) o;
    return Objects.equals(this.p25, budgetComparisonResultPercentileRange.p25) &&
        Objects.equals(this.p75, budgetComparisonResultPercentileRange.p75);
  }

  @Override
  public int hashCode() {
    return Objects.hash(p25, p75);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BudgetComparisonResultPercentileRange {\n");
    sb.append("    p25: ").append(toIndentedString(p25)).append("\n");
    sb.append("    p75: ").append(toIndentedString(p75)).append("\n");
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

