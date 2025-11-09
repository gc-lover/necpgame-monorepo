package com.necpgame.socialservice.model;

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
 * UpdateRelationshipRequest
 */

@JsonTypeName("updateRelationship_request")

public class UpdateRelationshipRequest {

  private String characterId;

  private String targetId;

  /**
   * Gets or Sets actionType
   */
  public enum ActionTypeEnum {
    QUEST_TOGETHER("quest_together"),
    
    GIFT("gift"),
    
    DIALOGUE("dialogue"),
    
    HELP("help"),
    
    BETRAYAL("betrayal"),
    
    ROMANCE_EVENT("romance_event");

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

  private @Nullable Object actionData;

  public UpdateRelationshipRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UpdateRelationshipRequest(String characterId, String targetId, ActionTypeEnum actionType) {
    this.characterId = characterId;
    this.targetId = targetId;
    this.actionType = actionType;
  }

  public UpdateRelationshipRequest characterId(String characterId) {
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

  public UpdateRelationshipRequest targetId(String targetId) {
    this.targetId = targetId;
    return this;
  }

  /**
   * Get targetId
   * @return targetId
   */
  @NotNull 
  @Schema(name = "target_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target_id")
  public String getTargetId() {
    return targetId;
  }

  public void setTargetId(String targetId) {
    this.targetId = targetId;
  }

  public UpdateRelationshipRequest actionType(ActionTypeEnum actionType) {
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

  public UpdateRelationshipRequest actionData(@Nullable Object actionData) {
    this.actionData = actionData;
    return this;
  }

  /**
   * Get actionData
   * @return actionData
   */
  
  @Schema(name = "action_data", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("action_data")
  public @Nullable Object getActionData() {
    return actionData;
  }

  public void setActionData(@Nullable Object actionData) {
    this.actionData = actionData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateRelationshipRequest updateRelationshipRequest = (UpdateRelationshipRequest) o;
    return Objects.equals(this.characterId, updateRelationshipRequest.characterId) &&
        Objects.equals(this.targetId, updateRelationshipRequest.targetId) &&
        Objects.equals(this.actionType, updateRelationshipRequest.actionType) &&
        Objects.equals(this.actionData, updateRelationshipRequest.actionData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, targetId, actionType, actionData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateRelationshipRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    actionType: ").append(toIndentedString(actionType)).append("\n");
    sb.append("    actionData: ").append(toIndentedString(actionData)).append("\n");
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

