package com.necpgame.partyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.partyservice.model.LootSettings;
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
 * LootEvent
 */


public class LootEvent {

  private @Nullable String partyId;

  private @Nullable LootSettings lootSettings;

  private @Nullable String itemId;

  private @Nullable String winnerMemberId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  public LootEvent partyId(@Nullable String partyId) {
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

  public LootEvent lootSettings(@Nullable LootSettings lootSettings) {
    this.lootSettings = lootSettings;
    return this;
  }

  /**
   * Get lootSettings
   * @return lootSettings
   */
  @Valid 
  @Schema(name = "lootSettings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lootSettings")
  public @Nullable LootSettings getLootSettings() {
    return lootSettings;
  }

  public void setLootSettings(@Nullable LootSettings lootSettings) {
    this.lootSettings = lootSettings;
  }

  public LootEvent itemId(@Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  
  @Schema(name = "itemId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("itemId")
  public @Nullable String getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable String itemId) {
    this.itemId = itemId;
  }

  public LootEvent winnerMemberId(@Nullable String winnerMemberId) {
    this.winnerMemberId = winnerMemberId;
    return this;
  }

  /**
   * Get winnerMemberId
   * @return winnerMemberId
   */
  
  @Schema(name = "winnerMemberId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("winnerMemberId")
  public @Nullable String getWinnerMemberId() {
    return winnerMemberId;
  }

  public void setWinnerMemberId(@Nullable String winnerMemberId) {
    this.winnerMemberId = winnerMemberId;
  }

  public LootEvent timestamp(@Nullable OffsetDateTime timestamp) {
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
    LootEvent lootEvent = (LootEvent) o;
    return Objects.equals(this.partyId, lootEvent.partyId) &&
        Objects.equals(this.lootSettings, lootEvent.lootSettings) &&
        Objects.equals(this.itemId, lootEvent.itemId) &&
        Objects.equals(this.winnerMemberId, lootEvent.winnerMemberId) &&
        Objects.equals(this.timestamp, lootEvent.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(partyId, lootSettings, itemId, winnerMemberId, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootEvent {\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    lootSettings: ").append(toIndentedString(lootSettings)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    winnerMemberId: ").append(toIndentedString(winnerMemberId)).append("\n");
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

