package com.necpgame.lootservice.model;

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
 * LootItemRequest
 */

@JsonTypeName("lootItem_request")

public class LootItemRequest {

  private String characterId;

  private String itemId;

  /**
   * Для shared loot
   */
  public enum RollTypeEnum {
    NEED("need"),
    
    GREED("greed"),
    
    PASS("pass");

    private final String value;

    RollTypeEnum(String value) {
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
    public static RollTypeEnum fromValue(String value) {
      for (RollTypeEnum b : RollTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RollTypeEnum rollType;

  public LootItemRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LootItemRequest(String characterId, String itemId) {
    this.characterId = characterId;
    this.itemId = itemId;
  }

  public LootItemRequest characterId(String characterId) {
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

  public LootItemRequest itemId(String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @NotNull 
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("item_id")
  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public LootItemRequest rollType(@Nullable RollTypeEnum rollType) {
    this.rollType = rollType;
    return this;
  }

  /**
   * Для shared loot
   * @return rollType
   */
  
  @Schema(name = "roll_type", description = "Для shared loot", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roll_type")
  public @Nullable RollTypeEnum getRollType() {
    return rollType;
  }

  public void setRollType(@Nullable RollTypeEnum rollType) {
    this.rollType = rollType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootItemRequest lootItemRequest = (LootItemRequest) o;
    return Objects.equals(this.characterId, lootItemRequest.characterId) &&
        Objects.equals(this.itemId, lootItemRequest.itemId) &&
        Objects.equals(this.rollType, lootItemRequest.rollType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, itemId, rollType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootItemRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    rollType: ").append(toIndentedString(rollType)).append("\n");
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

