package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.lootservice.model.LootItem;
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
 * LootAssignedEvent
 */


public class LootAssignedEvent {

  private @Nullable String dropId;

  private @Nullable String playerId;

  private @Nullable LootItem item;

  private @Nullable String partyId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime occurredAt;

  public LootAssignedEvent dropId(@Nullable String dropId) {
    this.dropId = dropId;
    return this;
  }

  /**
   * Get dropId
   * @return dropId
   */
  
  @Schema(name = "dropId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dropId")
  public @Nullable String getDropId() {
    return dropId;
  }

  public void setDropId(@Nullable String dropId) {
    this.dropId = dropId;
  }

  public LootAssignedEvent playerId(@Nullable String playerId) {
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

  public LootAssignedEvent item(@Nullable LootItem item) {
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

  public LootAssignedEvent partyId(@Nullable String partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  
  @Schema(name = "partyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partyId")
  public @Nullable String getPartyId() {
    return partyId;
  }

  public void setPartyId(@Nullable String partyId) {
    this.partyId = partyId;
  }

  public LootAssignedEvent occurredAt(@Nullable OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
    return this;
  }

  /**
   * Get occurredAt
   * @return occurredAt
   */
  @Valid 
  @Schema(name = "occurredAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("occurredAt")
  public @Nullable OffsetDateTime getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(@Nullable OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootAssignedEvent lootAssignedEvent = (LootAssignedEvent) o;
    return Objects.equals(this.dropId, lootAssignedEvent.dropId) &&
        Objects.equals(this.playerId, lootAssignedEvent.playerId) &&
        Objects.equals(this.item, lootAssignedEvent.item) &&
        Objects.equals(this.partyId, lootAssignedEvent.partyId) &&
        Objects.equals(this.occurredAt, lootAssignedEvent.occurredAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dropId, playerId, item, partyId, occurredAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootAssignedEvent {\n");
    sb.append("    dropId: ").append(toIndentedString(dropId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    item: ").append(toIndentedString(item)).append("\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    occurredAt: ").append(toIndentedString(occurredAt)).append("\n");
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

