package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * EquipRequest
 */


public class EquipRequest {

  private String playerId;

  /**
   * Gets or Sets slotType
   */
  public enum SlotTypeEnum {
    CHARACTER_SKIN("character_skin"),
    
    WEAPON_SKIN("weapon_skin"),
    
    EMOTE_SLOT("emote_slot"),
    
    TITLE("title"),
    
    NAMEPLATE("nameplate"),
    
    POSE("pose");

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

  private String itemId;

  private @Nullable Integer slotIndex;

  public EquipRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EquipRequest(String playerId, SlotTypeEnum slotType, String itemId) {
    this.playerId = playerId;
    this.slotType = slotType;
    this.itemId = itemId;
  }

  public EquipRequest playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public EquipRequest slotType(SlotTypeEnum slotType) {
    this.slotType = slotType;
    return this;
  }

  /**
   * Get slotType
   * @return slotType
   */
  @NotNull 
  @Schema(name = "slotType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slotType")
  public SlotTypeEnum getSlotType() {
    return slotType;
  }

  public void setSlotType(SlotTypeEnum slotType) {
    this.slotType = slotType;
  }

  public EquipRequest itemId(String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @NotNull 
  @Schema(name = "itemId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("itemId")
  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public EquipRequest slotIndex(@Nullable Integer slotIndex) {
    this.slotIndex = slotIndex;
    return this;
  }

  /**
   * Индекс для эмот-колеса
   * minimum: 0
   * @return slotIndex
   */
  @Min(value = 0) 
  @Schema(name = "slotIndex", description = "Индекс для эмот-колеса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slotIndex")
  public @Nullable Integer getSlotIndex() {
    return slotIndex;
  }

  public void setSlotIndex(@Nullable Integer slotIndex) {
    this.slotIndex = slotIndex;
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
    return Objects.equals(this.playerId, equipRequest.playerId) &&
        Objects.equals(this.slotType, equipRequest.slotType) &&
        Objects.equals(this.itemId, equipRequest.itemId) &&
        Objects.equals(this.slotIndex, equipRequest.slotIndex);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, slotType, itemId, slotIndex);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EquipRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    slotType: ").append(toIndentedString(slotType)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    slotIndex: ").append(toIndentedString(slotIndex)).append("\n");
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

