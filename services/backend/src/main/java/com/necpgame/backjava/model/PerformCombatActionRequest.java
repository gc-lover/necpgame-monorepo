package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PerformCombatActionRequest
 */

@JsonTypeName("performCombatAction_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T21:22:00.452540100+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class PerformCombatActionRequest {

  private UUID characterId;

  /**
   * Gets or Sets actionType
   */
  public enum ActionTypeEnum {
    ATTACK("attack"),
    
    DEFEND("defend"),
    
    USE_ITEM("use_item"),
    
    ABILITY("ability");

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

  private JsonNullable<String> targetId = JsonNullable.<String>undefined();

  private JsonNullable<String> itemId = JsonNullable.<String>undefined();

  private JsonNullable<String> abilityId = JsonNullable.<String>undefined();

  public PerformCombatActionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PerformCombatActionRequest(UUID characterId, ActionTypeEnum actionType) {
    this.characterId = characterId;
    this.actionType = actionType;
  }

  public PerformCombatActionRequest characterId(UUID characterId) {
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

  public PerformCombatActionRequest actionType(ActionTypeEnum actionType) {
    this.actionType = actionType;
    return this;
  }

  /**
   * Get actionType
   * @return actionType
   */
  @NotNull 
  @Schema(name = "actionType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("actionType")
  public ActionTypeEnum getActionType() {
    return actionType;
  }

  public void setActionType(ActionTypeEnum actionType) {
    this.actionType = actionType;
  }

  public PerformCombatActionRequest targetId(String targetId) {
    this.targetId = JsonNullable.of(targetId);
    return this;
  }

  /**
   * Get targetId
   * @return targetId
   */
  
  @Schema(name = "targetId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("targetId")
  public JsonNullable<String> getTargetId() {
    return targetId;
  }

  public void setTargetId(JsonNullable<String> targetId) {
    this.targetId = targetId;
  }

  public PerformCombatActionRequest itemId(String itemId) {
    this.itemId = JsonNullable.of(itemId);
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  
  @Schema(name = "itemId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("itemId")
  public JsonNullable<String> getItemId() {
    return itemId;
  }

  public void setItemId(JsonNullable<String> itemId) {
    this.itemId = itemId;
  }

  public PerformCombatActionRequest abilityId(String abilityId) {
    this.abilityId = JsonNullable.of(abilityId);
    return this;
  }

  /**
   * Get abilityId
   * @return abilityId
   */
  
  @Schema(name = "abilityId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("abilityId")
  public JsonNullable<String> getAbilityId() {
    return abilityId;
  }

  public void setAbilityId(JsonNullable<String> abilityId) {
    this.abilityId = abilityId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformCombatActionRequest performCombatActionRequest = (PerformCombatActionRequest) o;
    return Objects.equals(this.characterId, performCombatActionRequest.characterId) &&
        Objects.equals(this.actionType, performCombatActionRequest.actionType) &&
        equalsNullable(this.targetId, performCombatActionRequest.targetId) &&
        equalsNullable(this.itemId, performCombatActionRequest.itemId) &&
        equalsNullable(this.abilityId, performCombatActionRequest.abilityId);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, actionType, hashCodeNullable(targetId), hashCodeNullable(itemId), hashCodeNullable(abilityId));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformCombatActionRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    actionType: ").append(toIndentedString(actionType)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    abilityId: ").append(toIndentedString(abilityId)).append("\n");
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

