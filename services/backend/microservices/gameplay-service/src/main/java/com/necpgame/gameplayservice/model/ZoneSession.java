package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
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
 * ZoneSession
 */


public class ZoneSession {

  private @Nullable String sessionId;

  private @Nullable String characterId;

  private @Nullable String zoneId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime enteredAt;

  private @Nullable BigDecimal timeRemaining;

  private @Nullable BigDecimal currentDifficulty;

  public ZoneSession sessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  
  @Schema(name = "session_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("session_id")
  public @Nullable String getSessionId() {
    return sessionId;
  }

  public void setSessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
  }

  public ZoneSession characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public ZoneSession zoneId(@Nullable String zoneId) {
    this.zoneId = zoneId;
    return this;
  }

  /**
   * Get zoneId
   * @return zoneId
   */
  
  @Schema(name = "zone_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zone_id")
  public @Nullable String getZoneId() {
    return zoneId;
  }

  public void setZoneId(@Nullable String zoneId) {
    this.zoneId = zoneId;
  }

  public ZoneSession enteredAt(@Nullable OffsetDateTime enteredAt) {
    this.enteredAt = enteredAt;
    return this;
  }

  /**
   * Get enteredAt
   * @return enteredAt
   */
  @Valid 
  @Schema(name = "entered_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entered_at")
  public @Nullable OffsetDateTime getEnteredAt() {
    return enteredAt;
  }

  public void setEnteredAt(@Nullable OffsetDateTime enteredAt) {
    this.enteredAt = enteredAt;
  }

  public ZoneSession timeRemaining(@Nullable BigDecimal timeRemaining) {
    this.timeRemaining = timeRemaining;
    return this;
  }

  /**
   * Оставшееся время (секунды)
   * @return timeRemaining
   */
  @Valid 
  @Schema(name = "time_remaining", description = "Оставшееся время (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_remaining")
  public @Nullable BigDecimal getTimeRemaining() {
    return timeRemaining;
  }

  public void setTimeRemaining(@Nullable BigDecimal timeRemaining) {
    this.timeRemaining = timeRemaining;
  }

  public ZoneSession currentDifficulty(@Nullable BigDecimal currentDifficulty) {
    this.currentDifficulty = currentDifficulty;
    return this;
  }

  /**
   * Текущая сложность (1-10)
   * @return currentDifficulty
   */
  @Valid 
  @Schema(name = "current_difficulty", description = "Текущая сложность (1-10)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_difficulty")
  public @Nullable BigDecimal getCurrentDifficulty() {
    return currentDifficulty;
  }

  public void setCurrentDifficulty(@Nullable BigDecimal currentDifficulty) {
    this.currentDifficulty = currentDifficulty;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ZoneSession zoneSession = (ZoneSession) o;
    return Objects.equals(this.sessionId, zoneSession.sessionId) &&
        Objects.equals(this.characterId, zoneSession.characterId) &&
        Objects.equals(this.zoneId, zoneSession.zoneId) &&
        Objects.equals(this.enteredAt, zoneSession.enteredAt) &&
        Objects.equals(this.timeRemaining, zoneSession.timeRemaining) &&
        Objects.equals(this.currentDifficulty, zoneSession.currentDifficulty);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, characterId, zoneId, enteredAt, timeRemaining, currentDifficulty);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ZoneSession {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    zoneId: ").append(toIndentedString(zoneId)).append("\n");
    sb.append("    enteredAt: ").append(toIndentedString(enteredAt)).append("\n");
    sb.append("    timeRemaining: ").append(toIndentedString(timeRemaining)).append("\n");
    sb.append("    currentDifficulty: ").append(toIndentedString(currentDifficulty)).append("\n");
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

