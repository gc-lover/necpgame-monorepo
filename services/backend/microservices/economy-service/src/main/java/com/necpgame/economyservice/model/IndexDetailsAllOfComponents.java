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
 * IndexDetailsAllOfComponents
 */

@JsonTypeName("IndexDetails_allOf_components")

public class IndexDetailsAllOfComponents {

  private @Nullable String ticker;

  private @Nullable BigDecimal weight;

  public IndexDetailsAllOfComponents ticker(@Nullable String ticker) {
    this.ticker = ticker;
    return this;
  }

  /**
   * Get ticker
   * @return ticker
   */
  
  @Schema(name = "ticker", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ticker")
  public @Nullable String getTicker() {
    return ticker;
  }

  public void setTicker(@Nullable String ticker) {
    this.ticker = ticker;
  }

  public IndexDetailsAllOfComponents weight(@Nullable BigDecimal weight) {
    this.weight = weight;
    return this;
  }

  /**
   * Вес в индексе (%)
   * @return weight
   */
  @Valid 
  @Schema(name = "weight", description = "Вес в индексе (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weight")
  public @Nullable BigDecimal getWeight() {
    return weight;
  }

  public void setWeight(@Nullable BigDecimal weight) {
    this.weight = weight;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndexDetailsAllOfComponents indexDetailsAllOfComponents = (IndexDetailsAllOfComponents) o;
    return Objects.equals(this.ticker, indexDetailsAllOfComponents.ticker) &&
        Objects.equals(this.weight, indexDetailsAllOfComponents.weight);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticker, weight);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndexDetailsAllOfComponents {\n");
    sb.append("    ticker: ").append(toIndentedString(ticker)).append("\n");
    sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
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

