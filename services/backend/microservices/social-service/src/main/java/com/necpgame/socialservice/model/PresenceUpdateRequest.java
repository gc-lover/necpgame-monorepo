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
 * PresenceUpdateRequest
 */


public class PresenceUpdateRequest {

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

  private StateEnum state;

  private @Nullable String activity;

  private @Nullable String sessionId;

  /**
   * Gets or Sets availability
   */
  public enum AvailabilityEnum {
    AVAILABLE("AVAILABLE"),
    
    BUSY("BUSY"),
    
    DO_NOT_DISTURB("DO_NOT_DISTURB");

    private final String value;

    AvailabilityEnum(String value) {
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
    public static AvailabilityEnum fromValue(String value) {
      for (AvailabilityEnum b : AvailabilityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable AvailabilityEnum availability;

  public PresenceUpdateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PresenceUpdateRequest(StateEnum state) {
    this.state = state;
  }

  public PresenceUpdateRequest state(StateEnum state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * @return state
   */
  @NotNull 
  @Schema(name = "state", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("state")
  public StateEnum getState() {
    return state;
  }

  public void setState(StateEnum state) {
    this.state = state;
  }

  public PresenceUpdateRequest activity(@Nullable String activity) {
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

  public PresenceUpdateRequest sessionId(@Nullable String sessionId) {
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

  public PresenceUpdateRequest availability(@Nullable AvailabilityEnum availability) {
    this.availability = availability;
    return this;
  }

  /**
   * Get availability
   * @return availability
   */
  
  @Schema(name = "availability", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("availability")
  public @Nullable AvailabilityEnum getAvailability() {
    return availability;
  }

  public void setAvailability(@Nullable AvailabilityEnum availability) {
    this.availability = availability;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PresenceUpdateRequest presenceUpdateRequest = (PresenceUpdateRequest) o;
    return Objects.equals(this.state, presenceUpdateRequest.state) &&
        Objects.equals(this.activity, presenceUpdateRequest.activity) &&
        Objects.equals(this.sessionId, presenceUpdateRequest.sessionId) &&
        Objects.equals(this.availability, presenceUpdateRequest.availability);
  }

  @Override
  public int hashCode() {
    return Objects.hash(state, activity, sessionId, availability);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PresenceUpdateRequest {\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    activity: ").append(toIndentedString(activity)).append("\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    availability: ").append(toIndentedString(availability)).append("\n");
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

