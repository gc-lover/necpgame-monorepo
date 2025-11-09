package com.necpgame.inventoryservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.inventoryservice.model.Item;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EquipmentEvent
 */


public class EquipmentEvent {

  private @Nullable String playerId;

  private @Nullable String slotType;

  private @Nullable Item item;

  /**
   * Gets or Sets changeType
   */
  public enum ChangeTypeEnum {
    EQUIPPED("EQUIPPED"),
    
    UNEQUIPPED("UNEQUIPPED");

    private final String value;

    ChangeTypeEnum(String value) {
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
    public static ChangeTypeEnum fromValue(String value) {
      for (ChangeTypeEnum b : ChangeTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ChangeTypeEnum changeType;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  public EquipmentEvent playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public EquipmentEvent slotType(@Nullable String slotType) {
    this.slotType = slotType;
    return this;
  }

  /**
   * Get slotType
   * @return slotType
   */
  
  @Schema(name = "slotType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slotType")
  public @Nullable String getSlotType() {
    return slotType;
  }

  public void setSlotType(@Nullable String slotType) {
    this.slotType = slotType;
  }

  public EquipmentEvent item(@Nullable Item item) {
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
  public @Nullable Item getItem() {
    return item;
  }

  public void setItem(@Nullable Item item) {
    this.item = item;
  }

  public EquipmentEvent changeType(@Nullable ChangeTypeEnum changeType) {
    this.changeType = changeType;
    return this;
  }

  /**
   * Get changeType
   * @return changeType
   */
  
  @Schema(name = "changeType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("changeType")
  public @Nullable ChangeTypeEnum getChangeType() {
    return changeType;
  }

  public void setChangeType(@Nullable ChangeTypeEnum changeType) {
    this.changeType = changeType;
  }

  public EquipmentEvent timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EquipmentEvent equipmentEvent = (EquipmentEvent) o;
    return Objects.equals(this.playerId, equipmentEvent.playerId) &&
        Objects.equals(this.slotType, equipmentEvent.slotType) &&
        Objects.equals(this.item, equipmentEvent.item) &&
        Objects.equals(this.changeType, equipmentEvent.changeType) &&
        Objects.equals(this.timestamp, equipmentEvent.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, slotType, item, changeType, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EquipmentEvent {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    slotType: ").append(toIndentedString(slotType)).append("\n");
    sb.append("    item: ").append(toIndentedString(item)).append("\n");
    sb.append("    changeType: ").append(toIndentedString(changeType)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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

