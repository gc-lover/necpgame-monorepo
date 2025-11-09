package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * PortfolioInvestmentsByTypeValue
 */

@JsonTypeName("Portfolio_investments_by_type_value")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-08T01:01:47.984013400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class PortfolioInvestmentsByTypeValue {

  private @Nullable Integer count;

  private @Nullable Integer totalValue;

  public PortfolioInvestmentsByTypeValue count(@Nullable Integer count) {
    this.count = count;
    return this;
  }

  /**
   * Get count
   * @return count
   */
  
  @Schema(name = "count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("count")
  public @Nullable Integer getCount() {
    return count;
  }

  public void setCount(@Nullable Integer count) {
    this.count = count;
  }

  public PortfolioInvestmentsByTypeValue totalValue(@Nullable Integer totalValue) {
    this.totalValue = totalValue;
    return this;
  }

  /**
   * Get totalValue
   * @return totalValue
   */
  
  @Schema(name = "total_value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_value")
  public @Nullable Integer getTotalValue() {
    return totalValue;
  }

  public void setTotalValue(@Nullable Integer totalValue) {
    this.totalValue = totalValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PortfolioInvestmentsByTypeValue portfolioInvestmentsByTypeValue = (PortfolioInvestmentsByTypeValue) o;
    return Objects.equals(this.count, portfolioInvestmentsByTypeValue.count) &&
        Objects.equals(this.totalValue, portfolioInvestmentsByTypeValue.totalValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(count, totalValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PortfolioInvestmentsByTypeValue {\n");
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
    sb.append("    totalValue: ").append(toIndentedString(totalValue)).append("\n");
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

