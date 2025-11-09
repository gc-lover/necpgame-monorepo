package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TechnicalIndicators
 */


public class TechnicalIndicators {

  private @Nullable String itemId;

  @Valid
  private Map<String, Object> indicators = new HashMap<>();

  public TechnicalIndicators itemId(@Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_id")
  public @Nullable String getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable String itemId) {
    this.itemId = itemId;
  }

  public TechnicalIndicators indicators(Map<String, Object> indicators) {
    this.indicators = indicators;
    return this;
  }

  public TechnicalIndicators putIndicatorsItem(String key, Object indicatorsItem) {
    if (this.indicators == null) {
      this.indicators = new HashMap<>();
    }
    this.indicators.put(key, indicatorsItem);
    return this;
  }

  /**
   * Get indicators
   * @return indicators
   */
  
  @Schema(name = "indicators", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("indicators")
  public Map<String, Object> getIndicators() {
    return indicators;
  }

  public void setIndicators(Map<String, Object> indicators) {
    this.indicators = indicators;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TechnicalIndicators technicalIndicators = (TechnicalIndicators) o;
    return Objects.equals(this.itemId, technicalIndicators.itemId) &&
        Objects.equals(this.indicators, technicalIndicators.indicators);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, indicators);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TechnicalIndicators {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    indicators: ").append(toIndentedString(indicators)).append("\n");
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

