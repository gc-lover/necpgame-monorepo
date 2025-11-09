package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * InteractWithNPCRequest
 */

@JsonTypeName("interactWithNPC_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:49:00.930667100+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class InteractWithNPCRequest {

  private UUID characterId;

  /**
   * Gets or Sets action
   */
  public enum ActionEnum {
    TALK("talk"),
    
    TRADE("trade"),
    
    QUEST("quest"),
    
    ATTACK("attack");

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

  public InteractWithNPCRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InteractWithNPCRequest(UUID characterId, ActionEnum action) {
    this.characterId = characterId;
    this.action = action;
  }

  public InteractWithNPCRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public InteractWithNPCRequest action(ActionEnum action) {
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
    InteractWithNPCRequest interactWithNPCRequest = (InteractWithNPCRequest) o;
    return Objects.equals(this.characterId, interactWithNPCRequest.characterId) &&
        Objects.equals(this.action, interactWithNPCRequest.action);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, action);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InteractWithNPCRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
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


