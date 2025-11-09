package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.UUID;
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
 * RestoreQueueEntry
 */


public class RestoreQueueEntry {

  private @Nullable UUID characterId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime queuedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  private @Nullable String reason;

  public RestoreQueueEntry characterId(@Nullable UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @Valid 
  @Schema(name = "characterId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("characterId")
  public @Nullable UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable UUID characterId) {
    this.characterId = characterId;
  }

  public RestoreQueueEntry queuedAt(@Nullable OffsetDateTime queuedAt) {
    this.queuedAt = queuedAt;
    return this;
  }

  /**
   * Get queuedAt
   * @return queuedAt
   */
  @Valid 
  @Schema(name = "queuedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("queuedAt")
  public @Nullable OffsetDateTime getQueuedAt() {
    return queuedAt;
  }

  public void setQueuedAt(@Nullable OffsetDateTime queuedAt) {
    this.queuedAt = queuedAt;
  }

  public RestoreQueueEntry expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public RestoreQueueEntry reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RestoreQueueEntry restoreQueueEntry = (RestoreQueueEntry) o;
    return Objects.equals(this.characterId, restoreQueueEntry.characterId) &&
        Objects.equals(this.queuedAt, restoreQueueEntry.queuedAt) &&
        Objects.equals(this.expiresAt, restoreQueueEntry.expiresAt) &&
        Objects.equals(this.reason, restoreQueueEntry.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, queuedAt, expiresAt, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RestoreQueueEntry {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    queuedAt: ").append(toIndentedString(queuedAt)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

