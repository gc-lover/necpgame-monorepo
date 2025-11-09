package com.necpgame.combatservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * ReviveRequest
 */


public class ReviveRequest {

  private @Nullable String participantId;

  /**
   * Gets or Sets reviveMethod
   */
  public enum ReviveMethodEnum {
    PLAYER("PLAYER"),
    
    NPC("NPC"),
    
    CHECKPOINT("CHECKPOINT");

    private final String value;

    ReviveMethodEnum(String value) {
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
    public static ReviveMethodEnum fromValue(String value) {
      for (ReviveMethodEnum b : ReviveMethodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ReviveMethodEnum reviveMethod;

  @Valid
  private Map<String, Object> cost = new HashMap<>();

  public ReviveRequest participantId(@Nullable String participantId) {
    this.participantId = participantId;
    return this;
  }

  /**
   * Get participantId
   * @return participantId
   */
  
  @Schema(name = "participantId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participantId")
  public @Nullable String getParticipantId() {
    return participantId;
  }

  public void setParticipantId(@Nullable String participantId) {
    this.participantId = participantId;
  }

  public ReviveRequest reviveMethod(@Nullable ReviveMethodEnum reviveMethod) {
    this.reviveMethod = reviveMethod;
    return this;
  }

  /**
   * Get reviveMethod
   * @return reviveMethod
   */
  
  @Schema(name = "reviveMethod", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reviveMethod")
  public @Nullable ReviveMethodEnum getReviveMethod() {
    return reviveMethod;
  }

  public void setReviveMethod(@Nullable ReviveMethodEnum reviveMethod) {
    this.reviveMethod = reviveMethod;
  }

  public ReviveRequest cost(Map<String, Object> cost) {
    this.cost = cost;
    return this;
  }

  public ReviveRequest putCostItem(String key, Object costItem) {
    if (this.cost == null) {
      this.cost = new HashMap<>();
    }
    this.cost.put(key, costItem);
    return this;
  }

  /**
   * Get cost
   * @return cost
   */
  
  @Schema(name = "cost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cost")
  public Map<String, Object> getCost() {
    return cost;
  }

  public void setCost(Map<String, Object> cost) {
    this.cost = cost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReviveRequest reviveRequest = (ReviveRequest) o;
    return Objects.equals(this.participantId, reviveRequest.participantId) &&
        Objects.equals(this.reviveMethod, reviveRequest.reviveMethod) &&
        Objects.equals(this.cost, reviveRequest.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(participantId, reviveMethod, cost);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReviveRequest {\n");
    sb.append("    participantId: ").append(toIndentedString(participantId)).append("\n");
    sb.append("    reviveMethod: ").append(toIndentedString(reviveMethod)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
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

