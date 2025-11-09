package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RecordImpactRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class RecordImpactRequest {

  private UUID characterId;

  /**
   * Gets or Sets actionType
   */
  public enum ActionTypeEnum {
    QUEST_CHOICE("QUEST_CHOICE"),
    
    FACTION_ACTION("FACTION_ACTION"),
    
    NPC_INTERACTION("NPC_INTERACTION"),
    
    WORLD_EVENT("WORLD_EVENT"),
    
    ECONOMIC_ACTION("ECONOMIC_ACTION");

    private final String value;

    ActionTypeEnum(String value) {
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
    public static ActionTypeEnum fromValue(String value) {
      for (ActionTypeEnum b : ActionTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ActionTypeEnum actionType;

  private @Nullable String actionId;

  @Valid
  private Map<String, Object> impactData = new HashMap<>();

  public RecordImpactRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RecordImpactRequest(UUID characterId, ActionTypeEnum actionType) {
    this.characterId = characterId;
    this.actionType = actionType;
  }

  public RecordImpactRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public RecordImpactRequest actionType(ActionTypeEnum actionType) {
    this.actionType = actionType;
    return this;
  }

  /**
   * Get actionType
   * @return actionType
   */
  @NotNull 
  @Schema(name = "action_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("action_type")
  public ActionTypeEnum getActionType() {
    return actionType;
  }

  public void setActionType(ActionTypeEnum actionType) {
    this.actionType = actionType;
  }

  public RecordImpactRequest actionId(@Nullable String actionId) {
    this.actionId = actionId;
    return this;
  }

  /**
   * Get actionId
   * @return actionId
   */
  
  @Schema(name = "action_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("action_id")
  public @Nullable String getActionId() {
    return actionId;
  }

  public void setActionId(@Nullable String actionId) {
    this.actionId = actionId;
  }

  public RecordImpactRequest impactData(Map<String, Object> impactData) {
    this.impactData = impactData;
    return this;
  }

  public RecordImpactRequest putImpactDataItem(String key, Object impactDataItem) {
    if (this.impactData == null) {
      this.impactData = new HashMap<>();
    }
    this.impactData.put(key, impactDataItem);
    return this;
  }

  /**
   * Get impactData
   * @return impactData
   */
  
  @Schema(name = "impact_data", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("impact_data")
  public Map<String, Object> getImpactData() {
    return impactData;
  }

  public void setImpactData(Map<String, Object> impactData) {
    this.impactData = impactData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RecordImpactRequest recordImpactRequest = (RecordImpactRequest) o;
    return Objects.equals(this.characterId, recordImpactRequest.characterId) &&
        Objects.equals(this.actionType, recordImpactRequest.actionType) &&
        Objects.equals(this.actionId, recordImpactRequest.actionId) &&
        Objects.equals(this.impactData, recordImpactRequest.impactData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, actionType, actionId, impactData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RecordImpactRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    actionType: ").append(toIndentedString(actionType)).append("\n");
    sb.append("    actionId: ").append(toIndentedString(actionId)).append("\n");
    sb.append("    impactData: ").append(toIndentedString(impactData)).append("\n");
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

