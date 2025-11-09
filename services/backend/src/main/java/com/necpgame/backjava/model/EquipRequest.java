package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * EquipRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:50:45.778329200+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class EquipRequest {

  private UUID characterId;

  private String itemId;

  /**
   * РўРёРї СЃР»РѕС‚Р° СЌРєРёРїРёСЂРѕРІРєРё
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

  public EquipRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EquipRequest(UUID characterId, String itemId, SlotTypeEnum slotType) {
    this.characterId = characterId;
    this.itemId = itemId;
    this.slotType = slotType;
  }

  public EquipRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * ID РїРµСЂСЃРѕРЅР°Р¶Р°
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", example = "550e8400-e29b-41d4-a716-446655440000", description = "ID РїРµСЂСЃРѕРЅР°Р¶Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public EquipRequest itemId(String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * ID РїСЂРµРґРјРµС‚Р° РґР»СЏ СЌРєРёРїРёСЂРѕРІРєРё
   * @return itemId
   */
  @NotNull 
  @Schema(name = "itemId", example = "item_pistol_01", description = "ID РїСЂРµРґРјРµС‚Р° РґР»СЏ СЌРєРёРїРёСЂРѕРІРєРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("itemId")
  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public EquipRequest slotType(SlotTypeEnum slotType) {
    this.slotType = slotType;
    return this;
  }

  /**
   * РўРёРї СЃР»РѕС‚Р° СЌРєРёРїРёСЂРѕРІРєРё
   * @return slotType
   */
  @NotNull 
  @Schema(name = "slotType", example = "weapon_primary", description = "РўРёРї СЃР»РѕС‚Р° СЌРєРёРїРёСЂРѕРІРєРё", requiredMode = Schema.RequiredMode.REQUIRED)
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
    EquipRequest equipRequest = (EquipRequest) o;
    return Objects.equals(this.characterId, equipRequest.characterId) &&
        Objects.equals(this.itemId, equipRequest.itemId) &&
        Objects.equals(this.slotType, equipRequest.slotType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, itemId, slotType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EquipRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
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


