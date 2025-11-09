package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AutoSortRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class AutoSortRequest {

  /**
   * Gets or Sets strategy
   */
  public enum StrategyEnum {
    TYPE("TYPE"),
    
    RARITY("RARITY"),
    
    WEIGHT("WEIGHT"),
    
    CUSTOM("CUSTOM");

    private final String value;

    StrategyEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StrategyEnum fromValue(String value) {
      for (StrategyEnum b : StrategyEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StrategyEnum strategy;

  private @Nullable Boolean includeStash;

  public AutoSortRequest strategy(@Nullable StrategyEnum strategy) {
    this.strategy = strategy;
    return this;
  }

  /**
   * Get strategy
   * @return strategy
   */
  
  @Schema(name = "strategy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("strategy")
  public @Nullable StrategyEnum getStrategy() {
    return strategy;
  }

  public void setStrategy(@Nullable StrategyEnum strategy) {
    this.strategy = strategy;
  }

  public AutoSortRequest includeStash(@Nullable Boolean includeStash) {
    this.includeStash = includeStash;
    return this;
  }

  /**
   * Get includeStash
   * @return includeStash
   */
  
  @Schema(name = "includeStash", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("includeStash")
  public @Nullable Boolean getIncludeStash() {
    return includeStash;
  }

  public void setIncludeStash(@Nullable Boolean includeStash) {
    this.includeStash = includeStash;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AutoSortRequest autoSortRequest = (AutoSortRequest) o;
    return Objects.equals(this.strategy, autoSortRequest.strategy) &&
        Objects.equals(this.includeStash, autoSortRequest.includeStash);
  }

  @Override
  public int hashCode() {
    return Objects.hash(strategy, includeStash);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AutoSortRequest {\n");
    sb.append("    strategy: ").append(toIndentedString(strategy)).append("\n");
    sb.append("    includeStash: ").append(toIndentedString(includeStash)).append("\n");
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

