package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * CreateSession200Response
 */

@JsonTypeName("createSession_200_response")

public class CreateSession200Response {

  private @Nullable String sessionId;

  private @Nullable String characterId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  private @Nullable Integer heartbeatInterval;

  private @Nullable Integer afkTimeout;

  public CreateSession200Response sessionId(@Nullable String sessionId) {
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

  public CreateSession200Response characterId(@Nullable String characterId) {
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

  public CreateSession200Response createdAt(@Nullable OffsetDateTime createdAt) {
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

  public CreateSession200Response heartbeatInterval(@Nullable Integer heartbeatInterval) {
    this.heartbeatInterval = heartbeatInterval;
    return this;
  }

  /**
   * Интервал heartbeat (секунды)
   * @return heartbeatInterval
   */
  
  @Schema(name = "heartbeat_interval", description = "Интервал heartbeat (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("heartbeat_interval")
  public @Nullable Integer getHeartbeatInterval() {
    return heartbeatInterval;
  }

  public void setHeartbeatInterval(@Nullable Integer heartbeatInterval) {
    this.heartbeatInterval = heartbeatInterval;
  }

  public CreateSession200Response afkTimeout(@Nullable Integer afkTimeout) {
    this.afkTimeout = afkTimeout;
    return this;
  }

  /**
   * Таймаут AFK (секунды)
   * @return afkTimeout
   */
  
  @Schema(name = "afk_timeout", description = "Таймаут AFK (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("afk_timeout")
  public @Nullable Integer getAfkTimeout() {
    return afkTimeout;
  }

  public void setAfkTimeout(@Nullable Integer afkTimeout) {
    this.afkTimeout = afkTimeout;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateSession200Response createSession200Response = (CreateSession200Response) o;
    return Objects.equals(this.sessionId, createSession200Response.sessionId) &&
        Objects.equals(this.characterId, createSession200Response.characterId) &&
        Objects.equals(this.createdAt, createSession200Response.createdAt) &&
        Objects.equals(this.heartbeatInterval, createSession200Response.heartbeatInterval) &&
        Objects.equals(this.afkTimeout, createSession200Response.afkTimeout);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, characterId, createdAt, heartbeatInterval, afkTimeout);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateSession200Response {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    heartbeatInterval: ").append(toIndentedString(heartbeatInterval)).append("\n");
    sb.append("    afkTimeout: ").append(toIndentedString(afkTimeout)).append("\n");
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

