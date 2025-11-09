package com.necpgame.backjava.model;

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
 * EquipItemRequest
 */

@JsonTypeName("equipItem_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-08T01:55:07.487632800+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class EquipItemRequest {

  private String itemId;

  /**
   * Gets or Sets slotType
   */
  public enum SlotTypeEnum {
    WEAPON_PRIMARY("weapon_primary"),
    
    WEAPON_SECONDARY("weapon_secondary"),
    
    ARMOR_HEAD("armor_head"),
    
    ARMOR_CHEST("armor_chest"),
    
    ARMOR_LEGS("armor_legs"),
    
    IMPLANT("implant"),
    
    CYBERWARE("cyberware");

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

  public EquipItemRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EquipItemRequest(String itemId, SlotTypeEnum slotType) {
    this.itemId = itemId;
    this.slotType = slotType;
  }

  public EquipItemRequest itemId(String itemId) {
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

  public EquipItemRequest slotType(SlotTypeEnum slotType) {
    this.slotType = slotType;
    return this;
  }

  /**
   * Get slotType
   * @return slotType
   */
  @NotNull 
  @Schema(name = "slot_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slot_type")
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
    EquipItemRequest equipItemRequest = (EquipItemRequest) o;
    return Objects.equals(this.itemId, equipItemRequest.itemId) &&
        Objects.equals(this.slotType, equipItemRequest.slotType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, slotType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EquipItemRequest {\n");
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


