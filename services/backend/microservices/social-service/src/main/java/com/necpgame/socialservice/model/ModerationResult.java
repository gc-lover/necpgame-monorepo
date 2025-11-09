package com.necpgame.socialservice.model;

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
 * ModerationResult
 */


public class ModerationResult {

  private @Nullable String lobbyId;

  private @Nullable String action;

  private @Nullable String targetPlayerId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    APPLIED("applied"),
    
    REJECTED("rejected");

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

  private @Nullable StatusEnum status;

  private @Nullable String message;

  public ModerationResult lobbyId(@Nullable String lobbyId) {
    this.lobbyId = lobbyId;
    return this;
  }

  /**
   * Get lobbyId
   * @return lobbyId
   */
  
  @Schema(name = "lobbyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lobbyId")
  public @Nullable String getLobbyId() {
    return lobbyId;
  }

  public void setLobbyId(@Nullable String lobbyId) {
    this.lobbyId = lobbyId;
  }

  public ModerationResult action(@Nullable String action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  
  @Schema(name = "action", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("action")
  public @Nullable String getAction() {
    return action;
  }

  public void setAction(@Nullable String action) {
    this.action = action;
  }

  public ModerationResult targetPlayerId(@Nullable String targetPlayerId) {
    this.targetPlayerId = targetPlayerId;
    return this;
  }

  /**
   * Get targetPlayerId
   * @return targetPlayerId
   */
  
  @Schema(name = "targetPlayerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("targetPlayerId")
  public @Nullable String getTargetPlayerId() {
    return targetPlayerId;
  }

  public void setTargetPlayerId(@Nullable String targetPlayerId) {
    this.targetPlayerId = targetPlayerId;
  }

  public ModerationResult status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public ModerationResult message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  
  @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModerationResult moderationResult = (ModerationResult) o;
    return Objects.equals(this.lobbyId, moderationResult.lobbyId) &&
        Objects.equals(this.action, moderationResult.action) &&
        Objects.equals(this.targetPlayerId, moderationResult.targetPlayerId) &&
        Objects.equals(this.status, moderationResult.status) &&
        Objects.equals(this.message, moderationResult.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lobbyId, action, targetPlayerId, status, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModerationResult {\n");
    sb.append("    lobbyId: ").append(toIndentedString(lobbyId)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    targetPlayerId: ").append(toIndentedString(targetPlayerId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

