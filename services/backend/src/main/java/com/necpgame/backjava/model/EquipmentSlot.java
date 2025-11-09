package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import com.necpgame.backjava.model.InventoryItem;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EquipmentSlot
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:50:45.778329200+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class EquipmentSlot {

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

  private String slotName;

  private Boolean isEmpty;

  private @Nullable InventoryItem item;

  @Valid
  private Map<String, Integer> bonuses = new HashMap<>();

  public EquipmentSlot() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EquipmentSlot(SlotTypeEnum slotType, String slotName, Boolean isEmpty) {
    this.slotType = slotType;
    this.slotName = slotName;
    this.isEmpty = isEmpty;
  }

  public EquipmentSlot slotType(SlotTypeEnum slotType) {
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

  public EquipmentSlot slotName(String slotName) {
    this.slotName = slotName;
    return this;
  }

  /**
   * РќР°Р·РІР°РЅРёРµ СЃР»РѕС‚Р° РґР»СЏ РѕС‚РѕР±СЂР°Р¶РµРЅРёСЏ
   * @return slotName
   */
  @NotNull 
  @Schema(name = "slotName", example = "РћСЃРЅРѕРІРЅРѕРµ РѕСЂСѓР¶РёРµ", description = "РќР°Р·РІР°РЅРёРµ СЃР»РѕС‚Р° РґР»СЏ РѕС‚РѕР±СЂР°Р¶РµРЅРёСЏ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slotName")
  public String getSlotName() {
    return slotName;
  }

  public void setSlotName(String slotName) {
    this.slotName = slotName;
  }

  public EquipmentSlot isEmpty(Boolean isEmpty) {
    this.isEmpty = isEmpty;
    return this;
  }

  /**
   * РџСѓСЃС‚РѕР№ Р»Рё СЃР»РѕС‚
   * @return isEmpty
   */
  @NotNull 
  @Schema(name = "isEmpty", example = "false", description = "РџСѓСЃС‚РѕР№ Р»Рё СЃР»РѕС‚", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("isEmpty")
  public Boolean getIsEmpty() {
    return isEmpty;
  }

  public void setIsEmpty(Boolean isEmpty) {
    this.isEmpty = isEmpty;
  }

  public EquipmentSlot item(@Nullable InventoryItem item) {
    this.item = item;
    return this;
  }

  /**
   * Get item
   * @return item
   */
  @Valid 
  @Schema(name = "item", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item")
  public @Nullable InventoryItem getItem() {
    return item;
  }

  public void setItem(@Nullable InventoryItem item) {
    this.item = item;
  }

  public EquipmentSlot bonuses(Map<String, Integer> bonuses) {
    this.bonuses = bonuses;
    return this;
  }

  public EquipmentSlot putBonusesItem(String key, Integer bonusesItem) {
    if (this.bonuses == null) {
      this.bonuses = new HashMap<>();
    }
    this.bonuses.put(key, bonusesItem);
    return this;
  }

  /**
   * Р‘РѕРЅСѓСЃС‹ РѕС‚ СЌРєРёРїРёСЂРѕРІР°РЅРЅРѕРіРѕ РїСЂРµРґРјРµС‚Р°
   * @return bonuses
   */
  
  @Schema(name = "bonuses", example = "{\"damage\":25,\"accuracy\":10}", description = "Р‘РѕРЅСѓСЃС‹ РѕС‚ СЌРєРёРїРёСЂРѕРІР°РЅРЅРѕРіРѕ РїСЂРµРґРјРµС‚Р°", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonuses")
  public Map<String, Integer> getBonuses() {
    return bonuses;
  }

  public void setBonuses(Map<String, Integer> bonuses) {
    this.bonuses = bonuses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EquipmentSlot equipmentSlot = (EquipmentSlot) o;
    return Objects.equals(this.slotType, equipmentSlot.slotType) &&
        Objects.equals(this.slotName, equipmentSlot.slotName) &&
        Objects.equals(this.isEmpty, equipmentSlot.isEmpty) &&
        Objects.equals(this.item, equipmentSlot.item) &&
        Objects.equals(this.bonuses, equipmentSlot.bonuses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(slotType, slotName, isEmpty, item, bonuses);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EquipmentSlot {\n");
    sb.append("    slotType: ").append(toIndentedString(slotType)).append("\n");
    sb.append("    slotName: ").append(toIndentedString(slotName)).append("\n");
    sb.append("    isEmpty: ").append(toIndentedString(isEmpty)).append("\n");
    sb.append("    item: ").append(toIndentedString(item)).append("\n");
    sb.append("    bonuses: ").append(toIndentedString(bonuses)).append("\n");
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


