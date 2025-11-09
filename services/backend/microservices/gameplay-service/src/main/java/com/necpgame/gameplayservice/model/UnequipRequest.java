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
 * UnequipRequest
 */


public class UnequipRequest {

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

  private @Nullable Integer slotIndex;

  public UnequipRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UnequipRequest(String playerId, SlotTypeEnum slotType) {
    this.playerId = playerId;
    this.slotType = slotType;
  }

  public UnequipRequest playerId(String playerId) {
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

  public UnequipRequest slotType(SlotTypeEnum slotType) {
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

  public UnequipRequest slotIndex(@Nullable Integer slotIndex) {
    this.slotIndex = slotIndex;
    return this;
  }

  /**
   * Get slotIndex
   * @return slotIndex
   */
  
  @Schema(name = "slotIndex", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    UnequipRequest unequipRequest = (UnequipRequest) o;
    return Objects.equals(this.playerId, unequipRequest.playerId) &&
        Objects.equals(this.slotType, unequipRequest.slotType) &&
        Objects.equals(this.slotIndex, unequipRequest.slotIndex);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, slotType, slotIndex);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UnequipRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    slotType: ").append(toIndentedString(slotType)).append("\n");
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

