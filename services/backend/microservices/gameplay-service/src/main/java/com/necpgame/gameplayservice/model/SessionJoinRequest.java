package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.ParticipantSetup;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SessionJoinRequest
 */


public class SessionJoinRequest {

  private ParticipantSetup participant;

  /**
   * Gets or Sets mode
   */
  public enum ModeEnum {
    ACTIVE("ACTIVE"),
    
    SPECTATOR("SPECTATOR");

    private final String value;

    ModeEnum(String value) {
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
    public static ModeEnum fromValue(String value) {
      for (ModeEnum b : ModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ModeEnum mode;

  private @Nullable String reason;

  public SessionJoinRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SessionJoinRequest(ParticipantSetup participant) {
    this.participant = participant;
  }

  public SessionJoinRequest participant(ParticipantSetup participant) {
    this.participant = participant;
    return this;
  }

  /**
   * Get participant
   * @return participant
   */
  @NotNull @Valid 
  @Schema(name = "participant", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("participant")
  public ParticipantSetup getParticipant() {
    return participant;
  }

  public void setParticipant(ParticipantSetup participant) {
    this.participant = participant;
  }

  public SessionJoinRequest mode(@Nullable ModeEnum mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Get mode
   * @return mode
   */
  
  @Schema(name = "mode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mode")
  public @Nullable ModeEnum getMode() {
    return mode;
  }

  public void setMode(@Nullable ModeEnum mode) {
    this.mode = mode;
  }

  public SessionJoinRequest reason(@Nullable String reason) {
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
    SessionJoinRequest sessionJoinRequest = (SessionJoinRequest) o;
    return Objects.equals(this.participant, sessionJoinRequest.participant) &&
        Objects.equals(this.mode, sessionJoinRequest.mode) &&
        Objects.equals(this.reason, sessionJoinRequest.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(participant, mode, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SessionJoinRequest {\n");
    sb.append("    participant: ").append(toIndentedString(participant)).append("\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
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

