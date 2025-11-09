package com.necpgame.inventoryservice.model;

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
 * UnequipItemRequest
 */

@JsonTypeName("unequipItem_request")

public class UnequipItemRequest {

  private UUID characterId;

  /**
   * Тип слота экипировки
   */
  public enum SlotTypeEnum {
    HEAD("head"),
    
    BODY("body"),
    
    HANDS("hands"),
    
    LEGS("legs"),
    
    WEAPON_PRIMARY("weapon_primary"),
    
    WEAPON_SECONDARY("weapon_secondary"),
    
    IMPLANT_1("implant_1"),
    
    IMPLANT_2("implant_2"),
    
    IMPLANT_3("implant_3");

    private final String value;

    SlotTypeEnum(String value) {
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
    public static SlotTypeEnum fromValue(String value) {
      for (SlotTypeEnum b : SlotTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SlotTypeEnum slotType;

  public UnequipItemRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UnequipItemRequest(UUID characterId, SlotTypeEnum slotType) {
    this.characterId = characterId;
    this.slotType = slotType;
  }

  public UnequipItemRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * ID персонажа
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", description = "ID персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public UnequipItemRequest slotType(SlotTypeEnum slotType) {
    this.slotType = slotType;
    return this;
  }

  /**
   * Тип слота экипировки
   * @return slotType
   */
  @NotNull 
  @Schema(name = "slotType", description = "Тип слота экипировки", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slotType")
  public SlotTypeEnum getSlotType() {
    return slotType;
  }

  public void setSlotType(SlotTypeEnum slotType) {
    this.slotType = slotType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UnequipItemRequest unequipItemRequest = (UnequipItemRequest) o;
    return Objects.equals(this.characterId, unequipItemRequest.characterId) &&
        Objects.equals(this.slotType, unequipItemRequest.slotType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, slotType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UnequipItemRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    slotType: ").append(toIndentedString(slotType)).append("\n");
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

