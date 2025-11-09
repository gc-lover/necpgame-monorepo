package com.necpgame.adminservice.model;

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
 * SessionInfo
 */


public class SessionInfo {

  private @Nullable String sessionId;

  private @Nullable String characterId;

  private @Nullable String characterName;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastHeartbeat;

  private @Nullable BigDecimal duration;

  public SessionInfo sessionId(@Nullable String sessionId) {
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

  public SessionInfo characterId(@Nullable String characterId) {
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

  public SessionInfo characterName(@Nullable String characterName) {
    this.characterName = characterName;
    return this;
  }

  /**
   * Get characterName
   * @return characterName
   */
  
  @Schema(name = "character_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_name")
  public @Nullable String getCharacterName() {
    return characterName;
  }

  public void setCharacterName(@Nullable String characterName) {
    this.characterName = characterName;
  }

  public SessionInfo createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "created_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_at")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public SessionInfo lastHeartbeat(@Nullable OffsetDateTime lastHeartbeat) {
    this.lastHeartbeat = lastHeartbeat;
    return this;
  }

  /**
   * Get lastHeartbeat
   * @return lastHeartbeat
   */
  @Valid 
  @Schema(name = "last_heartbeat", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_heartbeat")
  public @Nullable OffsetDateTime getLastHeartbeat() {
    return lastHeartbeat;
  }

  public void setLastHeartbeat(@Nullable OffsetDateTime lastHeartbeat) {
    this.lastHeartbeat = lastHeartbeat;
  }

  public SessionInfo duration(@Nullable BigDecimal duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Длительность сессии (секунды)
   * @return duration
   */
  @Valid 
  @Schema(name = "duration", description = "Длительность сессии (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration")
  public @Nullable BigDecimal getDuration() {
    return duration;
  }

  public void setDuration(@Nullable BigDecimal duration) {
    this.duration = duration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SessionInfo sessionInfo = (SessionInfo) o;
    return Objects.equals(this.sessionId, sessionInfo.sessionId) &&
        Objects.equals(this.characterId, sessionInfo.characterId) &&
        Objects.equals(this.characterName, sessionInfo.characterName) &&
        Objects.equals(this.createdAt, sessionInfo.createdAt) &&
        Objects.equals(this.lastHeartbeat, sessionInfo.lastHeartbeat) &&
        Objects.equals(this.duration, sessionInfo.duration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, characterId, characterName, createdAt, lastHeartbeat, duration);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SessionInfo {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    characterName: ").append(toIndentedString(characterName)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    lastHeartbeat: ").append(toIndentedString(lastHeartbeat)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
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

