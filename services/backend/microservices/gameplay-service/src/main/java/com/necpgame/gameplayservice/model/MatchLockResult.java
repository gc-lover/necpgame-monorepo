package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * MatchLockResult
 */


public class MatchLockResult {

  private UUID matchId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    LOCKED("LOCKED"),
    
    FAILED("FAILED");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;

  private @Nullable String voiceLobbyId;

  private @Nullable String sessionServerId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lockedAt;

  public MatchLockResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MatchLockResult(UUID matchId, StatusEnum status) {
    this.matchId = matchId;
    this.status = status;
  }

  public MatchLockResult matchId(UUID matchId) {
    this.matchId = matchId;
    return this;
  }

  /**
   * Get matchId
   * @return matchId
   */
  @NotNull @Valid 
  @Schema(name = "matchId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("matchId")
  public UUID getMatchId() {
    return matchId;
  }

  public void setMatchId(UUID matchId) {
    this.matchId = matchId;
  }

  public MatchLockResult status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public MatchLockResult voiceLobbyId(@Nullable String voiceLobbyId) {
    this.voiceLobbyId = voiceLobbyId;
    return this;
  }

  /**
   * Get voiceLobbyId
   * @return voiceLobbyId
   */
  
  @Schema(name = "voiceLobbyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("voiceLobbyId")
  public @Nullable String getVoiceLobbyId() {
    return voiceLobbyId;
  }

  public void setVoiceLobbyId(@Nullable String voiceLobbyId) {
    this.voiceLobbyId = voiceLobbyId;
  }

  public MatchLockResult sessionServerId(@Nullable String sessionServerId) {
    this.sessionServerId = sessionServerId;
    return this;
  }

  /**
   * Get sessionServerId
   * @return sessionServerId
   */
  
  @Schema(name = "sessionServerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sessionServerId")
  public @Nullable String getSessionServerId() {
    return sessionServerId;
  }

  public void setSessionServerId(@Nullable String sessionServerId) {
    this.sessionServerId = sessionServerId;
  }

  public MatchLockResult lockedAt(@Nullable OffsetDateTime lockedAt) {
    this.lockedAt = lockedAt;
    return this;
  }

  /**
   * Get lockedAt
   * @return lockedAt
   */
  @Valid 
  @Schema(name = "lockedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lockedAt")
  public @Nullable OffsetDateTime getLockedAt() {
    return lockedAt;
  }

  public void setLockedAt(@Nullable OffsetDateTime lockedAt) {
    this.lockedAt = lockedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchLockResult matchLockResult = (MatchLockResult) o;
    return Objects.equals(this.matchId, matchLockResult.matchId) &&
        Objects.equals(this.status, matchLockResult.status) &&
        Objects.equals(this.voiceLobbyId, matchLockResult.voiceLobbyId) &&
        Objects.equals(this.sessionServerId, matchLockResult.sessionServerId) &&
        Objects.equals(this.lockedAt, matchLockResult.lockedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(matchId, status, voiceLobbyId, sessionServerId, lockedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchLockResult {\n");
    sb.append("    matchId: ").append(toIndentedString(matchId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    voiceLobbyId: ").append(toIndentedString(voiceLobbyId)).append("\n");
    sb.append("    sessionServerId: ").append(toIndentedString(sessionServerId)).append("\n");
    sb.append("    lockedAt: ").append(toIndentedString(lockedAt)).append("\n");
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

