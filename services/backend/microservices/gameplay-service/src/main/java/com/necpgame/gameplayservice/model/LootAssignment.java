package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.LootItem;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LootAssignment
 */


public class LootAssignment {

  private @Nullable String playerId;

  private @Nullable LootItem item;

  private @Nullable String rollType;

  private @Nullable Integer rollValue;

  public LootAssignment playerId(@Nullable String playerId) {
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

  public LootAssignment item(@Nullable LootItem item) {
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
  public @Nullable LootItem getItem() {
    return item;
  }

  public void setItem(@Nullable LootItem item) {
    this.item = item;
  }

  public LootAssignment rollType(@Nullable String rollType) {
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

  public LootAssignment rollValue(@Nullable Integer rollValue) {
    this.rollValue = rollValue;
    return this;
  }

  /**
   * Get rollValue
   * @return rollValue
   */
  
  @Schema(name = "rollValue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rollValue")
  public @Nullable Integer getRollValue() {
    return rollValue;
  }

  public void setRollValue(@Nullable Integer rollValue) {
    this.rollValue = rollValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootAssignment lootAssignment = (LootAssignment) o;
    return Objects.equals(this.playerId, lootAssignment.playerId) &&
        Objects.equals(this.item, lootAssignment.item) &&
        Objects.equals(this.rollType, lootAssignment.rollType) &&
        Objects.equals(this.rollValue, lootAssignment.rollValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, item, rollType, rollValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootAssignment {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    item: ").append(toIndentedString(item)).append("\n");
    sb.append("    rollType: ").append(toIndentedString(rollType)).append("\n");
    sb.append("    rollValue: ").append(toIndentedString(rollValue)).append("\n");
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

