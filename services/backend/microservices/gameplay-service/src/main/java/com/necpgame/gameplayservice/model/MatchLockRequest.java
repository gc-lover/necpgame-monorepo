package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * MatchLockRequest
 */


public class MatchLockRequest {

  private String sessionServerId;

  private @Nullable String voiceLobbyId;

  /**
   * Gets or Sets lockReason
   */
  public enum LockReasonEnum {
    READY("READY"),
    
    TIMEOUT("TIMEOUT"),
    
    FORCE_START("FORCE_START");

    private final String value;

    LockReasonEnum(String value) {
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
    public static LockReasonEnum fromValue(String value) {
      for (LockReasonEnum b : LockReasonEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private LockReasonEnum lockReason;

  private Boolean requiresAntiCheatSync = true;

  public MatchLockRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MatchLockRequest(String sessionServerId, LockReasonEnum lockReason) {
    this.sessionServerId = sessionServerId;
    this.lockReason = lockReason;
  }

  public MatchLockRequest sessionServerId(String sessionServerId) {
    this.sessionServerId = sessionServerId;
    return this;
  }

  /**
   * Get sessionServerId
   * @return sessionServerId
   */
  @NotNull 
  @Schema(name = "sessionServerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("sessionServerId")
  public String getSessionServerId() {
    return sessionServerId;
  }

  public void setSessionServerId(String sessionServerId) {
    this.sessionServerId = sessionServerId;
  }

  public MatchLockRequest voiceLobbyId(@Nullable String voiceLobbyId) {
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

  public MatchLockRequest lockReason(LockReasonEnum lockReason) {
    this.lockReason = lockReason;
    return this;
  }

  /**
   * Get lockReason
   * @return lockReason
   */
  @NotNull 
  @Schema(name = "lockReason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lockReason")
  public LockReasonEnum getLockReason() {
    return lockReason;
  }

  public void setLockReason(LockReasonEnum lockReason) {
    this.lockReason = lockReason;
  }

  public MatchLockRequest requiresAntiCheatSync(Boolean requiresAntiCheatSync) {
    this.requiresAntiCheatSync = requiresAntiCheatSync;
    return this;
  }

  /**
   * Get requiresAntiCheatSync
   * @return requiresAntiCheatSync
   */
  
  @Schema(name = "requiresAntiCheatSync", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requiresAntiCheatSync")
  public Boolean getRequiresAntiCheatSync() {
    return requiresAntiCheatSync;
  }

  public void setRequiresAntiCheatSync(Boolean requiresAntiCheatSync) {
    this.requiresAntiCheatSync = requiresAntiCheatSync;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchLockRequest matchLockRequest = (MatchLockRequest) o;
    return Objects.equals(this.sessionServerId, matchLockRequest.sessionServerId) &&
        Objects.equals(this.voiceLobbyId, matchLockRequest.voiceLobbyId) &&
        Objects.equals(this.lockReason, matchLockRequest.lockReason) &&
        Objects.equals(this.requiresAntiCheatSync, matchLockRequest.requiresAntiCheatSync);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionServerId, voiceLobbyId, lockReason, requiresAntiCheatSync);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchLockRequest {\n");
    sb.append("    sessionServerId: ").append(toIndentedString(sessionServerId)).append("\n");
    sb.append("    voiceLobbyId: ").append(toIndentedString(voiceLobbyId)).append("\n");
    sb.append("    lockReason: ").append(toIndentedString(lockReason)).append("\n");
    sb.append("    requiresAntiCheatSync: ").append(toIndentedString(requiresAntiCheatSync)).append("\n");
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

