package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * SendHeartbeatRequest
 */

@JsonTypeName("sendHeartbeat_request")

public class SendHeartbeatRequest {

  private String sessionId;

  /**
   * Gets or Sets activity
   */
  public enum ActivityEnum {
    ACTIVE("active"),
    
    IDLE("idle"),
    
    AFK("afk");

    private final String value;

    ActivityEnum(String value) {
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
    public static ActivityEnum fromValue(String value) {
      for (ActivityEnum b : ActivityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ActivityEnum activity = ActivityEnum.ACTIVE;

  private @Nullable String location;

  public SendHeartbeatRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SendHeartbeatRequest(String sessionId) {
    this.sessionId = sessionId;
  }

  public SendHeartbeatRequest sessionId(String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  @NotNull 
  @Schema(name = "session_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("session_id")
  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public SendHeartbeatRequest activity(ActivityEnum activity) {
    this.activity = activity;
    return this;
  }

  /**
   * Get activity
   * @return activity
   */
  
  @Schema(name = "activity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activity")
  public ActivityEnum getActivity() {
    return activity;
  }

  public void setActivity(ActivityEnum activity) {
    this.activity = activity;
  }

  public SendHeartbeatRequest location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Текущая локация персонажа
   * @return location
   */
  
  @Schema(name = "location", description = "Текущая локация персонажа", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SendHeartbeatRequest sendHeartbeatRequest = (SendHeartbeatRequest) o;
    return Objects.equals(this.sessionId, sendHeartbeatRequest.sessionId) &&
        Objects.equals(this.activity, sendHeartbeatRequest.activity) &&
        Objects.equals(this.location, sendHeartbeatRequest.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, activity, location);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SendHeartbeatRequest {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    activity: ").append(toIndentedString(activity)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
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

