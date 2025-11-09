package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * RollHistoryEntry
 */


public class RollHistoryEntry {

  private @Nullable String playerId;

  private @Nullable String rollType;

  private @Nullable Integer value;

  private @Nullable Integer bonus;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  public RollHistoryEntry playerId(@Nullable String playerId) {
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

  public RollHistoryEntry rollType(@Nullable String rollType) {
    this.rollType = rollType;
    return this;
  }

  /**
   * Get rollType
   * @return rollType
   */
  
  @Schema(name = "rollType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rollType")
  public @Nullable String getRollType() {
    return rollType;
  }

  public void setRollType(@Nullable String rollType) {
    this.rollType = rollType;
  }

  public RollHistoryEntry value(@Nullable Integer value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  
  @Schema(name = "value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("value")
  public @Nullable Integer getValue() {
    return value;
  }

  public void setValue(@Nullable Integer value) {
    this.value = value;
  }

  public RollHistoryEntry bonus(@Nullable Integer bonus) {
    this.bonus = bonus;
    return this;
  }

  /**
   * Get bonus
   * @return bonus
   */
  
  @Schema(name = "bonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonus")
  public @Nullable Integer getBonus() {
    return bonus;
  }

  public void setBonus(@Nullable Integer bonus) {
    this.bonus = bonus;
  }

  public RollHistoryEntry timestamp(@Nullable OffsetDateTime timestamp) {
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
    RollHistoryEntry rollHistoryEntry = (RollHistoryEntry) o;
    return Objects.equals(this.playerId, rollHistoryEntry.playerId) &&
        Objects.equals(this.rollType, rollHistoryEntry.rollType) &&
        Objects.equals(this.value, rollHistoryEntry.value) &&
        Objects.equals(this.bonus, rollHistoryEntry.bonus) &&
        Objects.equals(this.timestamp, rollHistoryEntry.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, rollType, value, bonus, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RollHistoryEntry {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    rollType: ").append(toIndentedString(rollType)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    bonus: ").append(toIndentedString(bonus)).append("\n");
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

