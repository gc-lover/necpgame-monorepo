package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * DuplicateEvent
 */


public class DuplicateEvent {

  private @Nullable String playerId;

  private @Nullable String itemId;

  @Valid
  private List<String> locations = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime occurredAt;

  public DuplicateEvent playerId(@Nullable String playerId) {
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

  public DuplicateEvent itemId(@Nullable String itemId) {
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

  public DuplicateEvent locations(List<String> locations) {
    this.locations = locations;
    return this;
  }

  public DuplicateEvent addLocationsItem(String locationsItem) {
    if (this.locations == null) {
      this.locations = new ArrayList<>();
    }
    this.locations.add(locationsItem);
    return this;
  }

  /**
   * Get locations
   * @return locations
   */
  
  @Schema(name = "locations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locations")
  public List<String> getLocations() {
    return locations;
  }

  public void setLocations(List<String> locations) {
    this.locations = locations;
  }

  public DuplicateEvent occurredAt(@Nullable OffsetDateTime occurredAt) {
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
    DuplicateEvent duplicateEvent = (DuplicateEvent) o;
    return Objects.equals(this.playerId, duplicateEvent.playerId) &&
        Objects.equals(this.itemId, duplicateEvent.itemId) &&
        Objects.equals(this.locations, duplicateEvent.locations) &&
        Objects.equals(this.occurredAt, duplicateEvent.occurredAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, itemId, locations, occurredAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DuplicateEvent {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    locations: ").append(toIndentedString(locations)).append("\n");
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

