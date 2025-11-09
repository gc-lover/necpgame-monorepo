package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.lootservice.model.LootRoll;
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
 * RollCompletedEvent
 */


public class RollCompletedEvent {

  private @Nullable LootRoll roll;

  private @Nullable String winnerId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime occurredAt;

  public RollCompletedEvent roll(@Nullable LootRoll roll) {
    this.roll = roll;
    return this;
  }

  /**
   * Get roll
   * @return roll
   */
  @Valid 
  @Schema(name = "roll", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roll")
  public @Nullable LootRoll getRoll() {
    return roll;
  }

  public void setRoll(@Nullable LootRoll roll) {
    this.roll = roll;
  }

  public RollCompletedEvent winnerId(@Nullable String winnerId) {
    this.winnerId = winnerId;
    return this;
  }

  /**
   * Get winnerId
   * @return winnerId
   */
  
  @Schema(name = "winnerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("winnerId")
  public @Nullable String getWinnerId() {
    return winnerId;
  }

  public void setWinnerId(@Nullable String winnerId) {
    this.winnerId = winnerId;
  }

  public RollCompletedEvent occurredAt(@Nullable OffsetDateTime occurredAt) {
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
    RollCompletedEvent rollCompletedEvent = (RollCompletedEvent) o;
    return Objects.equals(this.roll, rollCompletedEvent.roll) &&
        Objects.equals(this.winnerId, rollCompletedEvent.winnerId) &&
        Objects.equals(this.occurredAt, rollCompletedEvent.occurredAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roll, winnerId, occurredAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RollCompletedEvent {\n");
    sb.append("    roll: ").append(toIndentedString(roll)).append("\n");
    sb.append("    winnerId: ").append(toIndentedString(winnerId)).append("\n");
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

