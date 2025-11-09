package com.necpgame.combatservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
 * Action
 */


public class Action {

  private @Nullable String actionId;

  /**
   * Gets or Sets category
   */
  public enum CategoryEnum {
    ABILITY("ABILITY"),
    
    ITEM("ITEM"),
    
    MOVE("MOVE"),
    
    DEFEND("DEFEND"),
    
    CUSTOM("CUSTOM");

    private final String value;

    CategoryEnum(String value) {
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
    public static CategoryEnum fromValue(String value) {
      for (CategoryEnum b : CategoryEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable CategoryEnum category;

  private @Nullable String abilityId;

  private @Nullable String casterId;

  @Valid
  private List<String> targetIds = new ArrayList<>();

  private @Nullable Integer castTimeMs;

  private @Nullable Integer channelDurationMs;

  @Valid
  private Map<String, Object> parameters = new HashMap<>();

  public Action actionId(@Nullable String actionId) {
    this.actionId = actionId;
    return this;
  }

  /**
   * Get actionId
   * @return actionId
   */
  
  @Schema(name = "actionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actionId")
  public @Nullable String getActionId() {
    return actionId;
  }

  public void setActionId(@Nullable String actionId) {
    this.actionId = actionId;
  }

  public Action category(@Nullable CategoryEnum category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable CategoryEnum getCategory() {
    return category;
  }

  public void setCategory(@Nullable CategoryEnum category) {
    this.category = category;
  }

  public Action abilityId(@Nullable String abilityId) {
    this.abilityId = abilityId;
    return this;
  }

  /**
   * Get abilityId
   * @return abilityId
   */
  
  @Schema(name = "abilityId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("abilityId")
  public @Nullable String getAbilityId() {
    return abilityId;
  }

  public void setAbilityId(@Nullable String abilityId) {
    this.abilityId = abilityId;
  }

  public Action casterId(@Nullable String casterId) {
    this.casterId = casterId;
    return this;
  }

  /**
   * Get casterId
   * @return casterId
   */
  
  @Schema(name = "casterId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("casterId")
  public @Nullable String getCasterId() {
    return casterId;
  }

  public void setCasterId(@Nullable String casterId) {
    this.casterId = casterId;
  }

  public Action targetIds(List<String> targetIds) {
    this.targetIds = targetIds;
    return this;
  }

  public Action addTargetIdsItem(String targetIdsItem) {
    if (this.targetIds == null) {
      this.targetIds = new ArrayList<>();
    }
    this.targetIds.add(targetIdsItem);
    return this;
  }

  /**
   * Get targetIds
   * @return targetIds
   */
  
  @Schema(name = "targetIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("targetIds")
  public List<String> getTargetIds() {
    return targetIds;
  }

  public void setTargetIds(List<String> targetIds) {
    this.targetIds = targetIds;
  }

  public Action castTimeMs(@Nullable Integer castTimeMs) {
    this.castTimeMs = castTimeMs;
    return this;
  }

  /**
   * Get castTimeMs
   * @return castTimeMs
   */
  
  @Schema(name = "castTimeMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("castTimeMs")
  public @Nullable Integer getCastTimeMs() {
    return castTimeMs;
  }

  public void setCastTimeMs(@Nullable Integer castTimeMs) {
    this.castTimeMs = castTimeMs;
  }

  public Action channelDurationMs(@Nullable Integer channelDurationMs) {
    this.channelDurationMs = channelDurationMs;
    return this;
  }

  /**
   * Get channelDurationMs
   * @return channelDurationMs
   */
  
  @Schema(name = "channelDurationMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channelDurationMs")
  public @Nullable Integer getChannelDurationMs() {
    return channelDurationMs;
  }

  public void setChannelDurationMs(@Nullable Integer channelDurationMs) {
    this.channelDurationMs = channelDurationMs;
  }

  public Action parameters(Map<String, Object> parameters) {
    this.parameters = parameters;
    return this;
  }

  public Action putParametersItem(String key, Object parametersItem) {
    if (this.parameters == null) {
      this.parameters = new HashMap<>();
    }
    this.parameters.put(key, parametersItem);
    return this;
  }

  /**
   * Get parameters
   * @return parameters
   */
  
  @Schema(name = "parameters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("parameters")
  public Map<String, Object> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, Object> parameters) {
    this.parameters = parameters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Action action = (Action) o;
    return Objects.equals(this.actionId, action.actionId) &&
        Objects.equals(this.category, action.category) &&
        Objects.equals(this.abilityId, action.abilityId) &&
        Objects.equals(this.casterId, action.casterId) &&
        Objects.equals(this.targetIds, action.targetIds) &&
        Objects.equals(this.castTimeMs, action.castTimeMs) &&
        Objects.equals(this.channelDurationMs, action.channelDurationMs) &&
        Objects.equals(this.parameters, action.parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(actionId, category, abilityId, casterId, targetIds, castTimeMs, channelDurationMs, parameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Action {\n");
    sb.append("    actionId: ").append(toIndentedString(actionId)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    abilityId: ").append(toIndentedString(abilityId)).append("\n");
    sb.append("    casterId: ").append(toIndentedString(casterId)).append("\n");
    sb.append("    targetIds: ").append(toIndentedString(targetIds)).append("\n");
    sb.append("    castTimeMs: ").append(toIndentedString(castTimeMs)).append("\n");
    sb.append("    channelDurationMs: ").append(toIndentedString(channelDurationMs)).append("\n");
    sb.append("    parameters: ").append(toIndentedString(parameters)).append("\n");
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

