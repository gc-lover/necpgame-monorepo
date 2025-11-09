package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * OptimizeChainRequest
 */

@JsonTypeName("optimizeChain_request")

public class OptimizeChainRequest {

  private @Nullable String chainId;

  /**
   * Gets or Sets goal
   */
  public enum GoalEnum {
    MAX_PROFIT("MAX_PROFIT"),
    
    MIN_TIME("MIN_TIME"),
    
    MIN_COST("MIN_COST"),
    
    MAX_QUALITY("MAX_QUALITY");

    private final String value;

    GoalEnum(String value) {
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
    public static GoalEnum fromValue(String value) {
      for (GoalEnum b : GoalEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable GoalEnum goal;

  private @Nullable Object availableResources;

  public OptimizeChainRequest chainId(@Nullable String chainId) {
    this.chainId = chainId;
    return this;
  }

  /**
   * Get chainId
   * @return chainId
   */
  
  @Schema(name = "chain_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("chain_id")
  public @Nullable String getChainId() {
    return chainId;
  }

  public void setChainId(@Nullable String chainId) {
    this.chainId = chainId;
  }

  public OptimizeChainRequest goal(@Nullable GoalEnum goal) {
    this.goal = goal;
    return this;
  }

  /**
   * Get goal
   * @return goal
   */
  
  @Schema(name = "goal", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("goal")
  public @Nullable GoalEnum getGoal() {
    return goal;
  }

  public void setGoal(@Nullable GoalEnum goal) {
    this.goal = goal;
  }

  public OptimizeChainRequest availableResources(@Nullable Object availableResources) {
    this.availableResources = availableResources;
    return this;
  }

  /**
   * Get availableResources
   * @return availableResources
   */
  
  @Schema(name = "available_resources", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available_resources")
  public @Nullable Object getAvailableResources() {
    return availableResources;
  }

  public void setAvailableResources(@Nullable Object availableResources) {
    this.availableResources = availableResources;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OptimizeChainRequest optimizeChainRequest = (OptimizeChainRequest) o;
    return Objects.equals(this.chainId, optimizeChainRequest.chainId) &&
        Objects.equals(this.goal, optimizeChainRequest.goal) &&
        Objects.equals(this.availableResources, optimizeChainRequest.availableResources);
  }

  @Override
  public int hashCode() {
    return Objects.hash(chainId, goal, availableResources);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OptimizeChainRequest {\n");
    sb.append("    chainId: ").append(toIndentedString(chainId)).append("\n");
    sb.append("    goal: ").append(toIndentedString(goal)).append("\n");
    sb.append("    availableResources: ").append(toIndentedString(availableResources)).append("\n");
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

