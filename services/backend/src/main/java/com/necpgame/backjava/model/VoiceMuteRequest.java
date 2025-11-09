package com.necpgame.backjava.model;

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
 * VoiceMuteRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class VoiceMuteRequest {

  /**
   * Gets or Sets action
   */
  public enum ActionEnum {
    MUTE("mute"),
    
    UNMUTE("unmute");

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

  private @Nullable String reason;

  private @Nullable Integer durationSeconds;

  private @Nullable String moderatorId;

  public VoiceMuteRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public VoiceMuteRequest(ActionEnum action) {
    this.action = action;
  }

  public VoiceMuteRequest action(ActionEnum action) {
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

  public VoiceMuteRequest reason(@Nullable String reason) {
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

  public VoiceMuteRequest durationSeconds(@Nullable Integer durationSeconds) {
    this.durationSeconds = durationSeconds;
    return this;
  }

  /**
   * Get durationSeconds
   * minimum: 30
   * @return durationSeconds
   */
  @Min(value = 30) 
  @Schema(name = "durationSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durationSeconds")
  public @Nullable Integer getDurationSeconds() {
    return durationSeconds;
  }

  public void setDurationSeconds(@Nullable Integer durationSeconds) {
    this.durationSeconds = durationSeconds;
  }

  public VoiceMuteRequest moderatorId(@Nullable String moderatorId) {
    this.moderatorId = moderatorId;
    return this;
  }

  /**
   * Get moderatorId
   * @return moderatorId
   */
  
  @Schema(name = "moderatorId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("moderatorId")
  public @Nullable String getModeratorId() {
    return moderatorId;
  }

  public void setModeratorId(@Nullable String moderatorId) {
    this.moderatorId = moderatorId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoiceMuteRequest voiceMuteRequest = (VoiceMuteRequest) o;
    return Objects.equals(this.action, voiceMuteRequest.action) &&
        Objects.equals(this.reason, voiceMuteRequest.reason) &&
        Objects.equals(this.durationSeconds, voiceMuteRequest.durationSeconds) &&
        Objects.equals(this.moderatorId, voiceMuteRequest.moderatorId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, reason, durationSeconds, moderatorId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoiceMuteRequest {\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    durationSeconds: ").append(toIndentedString(durationSeconds)).append("\n");
    sb.append("    moderatorId: ").append(toIndentedString(moderatorId)).append("\n");
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

