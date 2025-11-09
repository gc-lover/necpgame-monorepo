package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * RollEvent
 */


public class RollEvent {

  private @Nullable LootRoll roll;

  /**
   * Gets or Sets event
   */
  public enum EventEnum {
    STARTED("STARTED"),
    
    UPDATED("UPDATED");

    private final String value;

    EventEnum(String value) {
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
    public static EventEnum fromValue(String value) {
      for (EventEnum b : EventEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable EventEnum event;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime occurredAt;

  public RollEvent roll(@Nullable LootRoll roll) {
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

  public RollEvent event(@Nullable EventEnum event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
   */
  
  @Schema(name = "event", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event")
  public @Nullable EventEnum getEvent() {
    return event;
  }

  public void setEvent(@Nullable EventEnum event) {
    this.event = event;
  }

  public RollEvent occurredAt(@Nullable OffsetDateTime occurredAt) {
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
    RollEvent rollEvent = (RollEvent) o;
    return Objects.equals(this.roll, rollEvent.roll) &&
        Objects.equals(this.event, rollEvent.event) &&
        Objects.equals(this.occurredAt, rollEvent.occurredAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roll, event, occurredAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RollEvent {\n");
    sb.append("    roll: ").append(toIndentedString(roll)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
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

