package com.necpgame.inventoryservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.inventoryservice.model.WeightInfo;
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
 * WeightEvent
 */


public class WeightEvent {

  private @Nullable String playerId;

  private @Nullable WeightInfo weight;

  private @Nullable Boolean overloaded;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  public WeightEvent playerId(@Nullable String playerId) {
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

  public WeightEvent weight(@Nullable WeightInfo weight) {
    this.weight = weight;
    return this;
  }

  /**
   * Get weight
   * @return weight
   */
  @Valid 
  @Schema(name = "weight", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weight")
  public @Nullable WeightInfo getWeight() {
    return weight;
  }

  public void setWeight(@Nullable WeightInfo weight) {
    this.weight = weight;
  }

  public WeightEvent overloaded(@Nullable Boolean overloaded) {
    this.overloaded = overloaded;
    return this;
  }

  /**
   * Get overloaded
   * @return overloaded
   */
  
  @Schema(name = "overloaded", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("overloaded")
  public @Nullable Boolean getOverloaded() {
    return overloaded;
  }

  public void setOverloaded(@Nullable Boolean overloaded) {
    this.overloaded = overloaded;
  }

  public WeightEvent timestamp(@Nullable OffsetDateTime timestamp) {
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
    WeightEvent weightEvent = (WeightEvent) o;
    return Objects.equals(this.playerId, weightEvent.playerId) &&
        Objects.equals(this.weight, weightEvent.weight) &&
        Objects.equals(this.overloaded, weightEvent.overloaded) &&
        Objects.equals(this.timestamp, weightEvent.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, weight, overloaded, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WeightEvent {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
    sb.append("    overloaded: ").append(toIndentedString(overloaded)).append("\n");
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

