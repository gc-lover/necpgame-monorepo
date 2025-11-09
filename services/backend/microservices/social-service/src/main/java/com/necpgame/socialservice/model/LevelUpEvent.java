package com.necpgame.socialservice.model;

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
 * LevelUpEvent
 */


public class LevelUpEvent {

  private String playerId;

  private Integer level;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime occurredAt;

  public LevelUpEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LevelUpEvent(String playerId, Integer level) {
    this.playerId = playerId;
    this.level = level;
  }

  public LevelUpEvent playerId(String playerId) {
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

  public LevelUpEvent level(Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  @NotNull 
  @Schema(name = "level", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("level")
  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public LevelUpEvent occurredAt(@Nullable OffsetDateTime occurredAt) {
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
    LevelUpEvent levelUpEvent = (LevelUpEvent) o;
    return Objects.equals(this.playerId, levelUpEvent.playerId) &&
        Objects.equals(this.level, levelUpEvent.level) &&
        Objects.equals(this.occurredAt, levelUpEvent.occurredAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, level, occurredAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LevelUpEvent {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
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

