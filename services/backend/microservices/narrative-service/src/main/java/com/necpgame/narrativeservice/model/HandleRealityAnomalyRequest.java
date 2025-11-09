package com.necpgame.narrativeservice.model;

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
 * HandleRealityAnomalyRequest
 */

@JsonTypeName("handleRealityAnomaly_request")

public class HandleRealityAnomalyRequest {

  private String characterId;

  private String anomalyId;

  /**
   * Gets or Sets action
   */
  public enum ActionEnum {
    STABILIZE("stabilize"),
    
    NEUTRALIZE("neutralize"),
    
    BYPASS("bypass");

    private final String value;

    ActionEnum(String value) {
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
    public static ActionEnum fromValue(String value) {
      for (ActionEnum b : ActionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ActionEnum action;

  public HandleRealityAnomalyRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public HandleRealityAnomalyRequest(String characterId, String anomalyId, ActionEnum action) {
    this.characterId = characterId;
    this.anomalyId = anomalyId;
    this.action = action;
  }

  public HandleRealityAnomalyRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public HandleRealityAnomalyRequest anomalyId(String anomalyId) {
    this.anomalyId = anomalyId;
    return this;
  }

  /**
   * Get anomalyId
   * @return anomalyId
   */
  @NotNull 
  @Schema(name = "anomaly_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("anomaly_id")
  public String getAnomalyId() {
    return anomalyId;
  }

  public void setAnomalyId(String anomalyId) {
    this.anomalyId = anomalyId;
  }

  public HandleRealityAnomalyRequest action(ActionEnum action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  @NotNull 
  @Schema(name = "action", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("action")
  public ActionEnum getAction() {
    return action;
  }

  public void setAction(ActionEnum action) {
    this.action = action;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HandleRealityAnomalyRequest handleRealityAnomalyRequest = (HandleRealityAnomalyRequest) o;
    return Objects.equals(this.characterId, handleRealityAnomalyRequest.characterId) &&
        Objects.equals(this.anomalyId, handleRealityAnomalyRequest.anomalyId) &&
        Objects.equals(this.action, handleRealityAnomalyRequest.action);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, anomalyId, action);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HandleRealityAnomalyRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    anomalyId: ").append(toIndentedString(anomalyId)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
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

