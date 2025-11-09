package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * Presence
 */


public class Presence {

  /**
   * Gets or Sets state
   */
  public enum StateEnum {
    ONLINE("ONLINE"),
    
    AWAY("AWAY"),
    
    IN_MATCH("IN_MATCH"),
    
    OFFLINE("OFFLINE");

    private final String value;

    StateEnum(String value) {
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
    public static StateEnum fromValue(String value) {
      for (StateEnum b : StateEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StateEnum state;

  private @Nullable String activity;

  private @Nullable String sessionId;

  private @Nullable String platform;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastSeen;

  public Presence state(@Nullable StateEnum state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * @return state
   */
  
  @Schema(name = "state", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("state")
  public @Nullable StateEnum getState() {
    return state;
  }

  public void setState(@Nullable StateEnum state) {
    this.state = state;
  }

  public Presence activity(@Nullable String activity) {
    this.activity = activity;
    return this;
  }

  /**
   * Get activity
   * @return activity
   */
  
  @Schema(name = "activity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activity")
  public @Nullable String getActivity() {
    return activity;
  }

  public void setActivity(@Nullable String activity) {
    this.activity = activity;
  }

  public Presence sessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  
  @Schema(name = "sessionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sessionId")
  public @Nullable String getSessionId() {
    return sessionId;
  }

  public void setSessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
  }

  public Presence platform(@Nullable String platform) {
    this.platform = platform;
    return this;
  }

  /**
   * Get platform
   * @return platform
   */
  
  @Schema(name = "platform", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("platform")
  public @Nullable String getPlatform() {
    return platform;
  }

  public void setPlatform(@Nullable String platform) {
    this.platform = platform;
  }

  public Presence lastSeen(@Nullable OffsetDateTime lastSeen) {
    this.lastSeen = lastSeen;
    return this;
  }

  /**
   * Get lastSeen
   * @return lastSeen
   */
  @Valid 
  @Schema(name = "lastSeen", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastSeen")
  public @Nullable OffsetDateTime getLastSeen() {
    return lastSeen;
  }

  public void setLastSeen(@Nullable OffsetDateTime lastSeen) {
    this.lastSeen = lastSeen;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Presence presence = (Presence) o;
    return Objects.equals(this.state, presence.state) &&
        Objects.equals(this.activity, presence.activity) &&
        Objects.equals(this.sessionId, presence.sessionId) &&
        Objects.equals(this.platform, presence.platform) &&
        Objects.equals(this.lastSeen, presence.lastSeen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(state, activity, sessionId, platform, lastSeen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Presence {\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    activity: ").append(toIndentedString(activity)).append("\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    platform: ").append(toIndentedString(platform)).append("\n");
    sb.append("    lastSeen: ").append(toIndentedString(lastSeen)).append("\n");
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

