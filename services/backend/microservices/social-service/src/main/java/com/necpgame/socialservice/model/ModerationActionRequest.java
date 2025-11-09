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
 * ModerationActionRequest
 */


public class ModerationActionRequest {

  /**
   * Gets or Sets action
   */
  public enum ActionEnum {
    MUTE("mute"),
    
    UNMUTE("unmute"),
    
    KICK("kick"),
    
    INVITE("invite"),
    
    PROMOTE("promote"),
    
    DEMOTE("demote");

    private final String value;

    ActionEnum(String value) {
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
    public static ActionEnum fromValue(String value) {
      for (ActionEnum b : ActionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ActionEnum action;

  private String targetPlayerId;

  private @Nullable Integer durationSeconds;

  private @Nullable String reason;

  private @Nullable Boolean notifyParticipant;

  public ModerationActionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ModerationActionRequest(ActionEnum action, String targetPlayerId) {
    this.action = action;
    this.targetPlayerId = targetPlayerId;
  }

  public ModerationActionRequest action(ActionEnum action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  @NotNull 
  @Schema(name = "action", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("action")
  public ActionEnum getAction() {
    return action;
  }

  public void setAction(ActionEnum action) {
    this.action = action;
  }

  public ModerationActionRequest targetPlayerId(String targetPlayerId) {
    this.targetPlayerId = targetPlayerId;
    return this;
  }

  /**
   * Get targetPlayerId
   * @return targetPlayerId
   */
  @NotNull 
  @Schema(name = "targetPlayerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("targetPlayerId")
  public String getTargetPlayerId() {
    return targetPlayerId;
  }

  public void setTargetPlayerId(String targetPlayerId) {
    this.targetPlayerId = targetPlayerId;
  }

  public ModerationActionRequest durationSeconds(@Nullable Integer durationSeconds) {
    this.durationSeconds = durationSeconds;
    return this;
  }

  /**
   * Get durationSeconds
   * @return durationSeconds
   */
  
  @Schema(name = "durationSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durationSeconds")
  public @Nullable Integer getDurationSeconds() {
    return durationSeconds;
  }

  public void setDurationSeconds(@Nullable Integer durationSeconds) {
    this.durationSeconds = durationSeconds;
  }

  public ModerationActionRequest reason(@Nullable String reason) {
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

  public ModerationActionRequest notifyParticipant(@Nullable Boolean notifyParticipant) {
    this.notifyParticipant = notifyParticipant;
    return this;
  }

  /**
   * Get notifyParticipant
   * @return notifyParticipant
   */
  
  @Schema(name = "notifyParticipant", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notifyParticipant")
  public @Nullable Boolean getNotifyParticipant() {
    return notifyParticipant;
  }

  public void setNotifyParticipant(@Nullable Boolean notifyParticipant) {
    this.notifyParticipant = notifyParticipant;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModerationActionRequest moderationActionRequest = (ModerationActionRequest) o;
    return Objects.equals(this.action, moderationActionRequest.action) &&
        Objects.equals(this.targetPlayerId, moderationActionRequest.targetPlayerId) &&
        Objects.equals(this.durationSeconds, moderationActionRequest.durationSeconds) &&
        Objects.equals(this.reason, moderationActionRequest.reason) &&
        Objects.equals(this.notifyParticipant, moderationActionRequest.notifyParticipant);
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, targetPlayerId, durationSeconds, reason, notifyParticipant);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModerationActionRequest {\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    targetPlayerId: ").append(toIndentedString(targetPlayerId)).append("\n");
    sb.append("    durationSeconds: ").append(toIndentedString(durationSeconds)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    notifyParticipant: ").append(toIndentedString(notifyParticipant)).append("\n");
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

