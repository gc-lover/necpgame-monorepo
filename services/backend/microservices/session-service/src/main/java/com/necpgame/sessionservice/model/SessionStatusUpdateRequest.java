package com.necpgame.sessionservice.model;

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
 * SessionStatusUpdateRequest
 */


public class SessionStatusUpdateRequest {

  /**
   * Gets or Sets newStatus
   */
  public enum NewStatusEnum {
    ACTIVE("ACTIVE"),
    
    AFK("AFK"),
    
    DISCONNECTED("DISCONNECTED"),
    
    TERMINATED("TERMINATED");

    private final String value;

    NewStatusEnum(String value) {
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
    public static NewStatusEnum fromValue(String value) {
      for (NewStatusEnum b : NewStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private NewStatusEnum newStatus;

  private @Nullable String reason;

  /**
   * Gets or Sets triggeredBy
   */
  public enum TriggeredByEnum {
    SYSTEM("system"),
    
    OPS("ops"),
    
    PLAYER("player");

    private final String value;

    TriggeredByEnum(String value) {
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
    public static TriggeredByEnum fromValue(String value) {
      for (TriggeredByEnum b : TriggeredByEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TriggeredByEnum triggeredBy;

  public SessionStatusUpdateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SessionStatusUpdateRequest(NewStatusEnum newStatus) {
    this.newStatus = newStatus;
  }

  public SessionStatusUpdateRequest newStatus(NewStatusEnum newStatus) {
    this.newStatus = newStatus;
    return this;
  }

  /**
   * Get newStatus
   * @return newStatus
   */
  @NotNull 
  @Schema(name = "newStatus", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("newStatus")
  public NewStatusEnum getNewStatus() {
    return newStatus;
  }

  public void setNewStatus(NewStatusEnum newStatus) {
    this.newStatus = newStatus;
  }

  public SessionStatusUpdateRequest reason(@Nullable String reason) {
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

  public SessionStatusUpdateRequest triggeredBy(@Nullable TriggeredByEnum triggeredBy) {
    this.triggeredBy = triggeredBy;
    return this;
  }

  /**
   * Get triggeredBy
   * @return triggeredBy
   */
  
  @Schema(name = "triggeredBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("triggeredBy")
  public @Nullable TriggeredByEnum getTriggeredBy() {
    return triggeredBy;
  }

  public void setTriggeredBy(@Nullable TriggeredByEnum triggeredBy) {
    this.triggeredBy = triggeredBy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SessionStatusUpdateRequest sessionStatusUpdateRequest = (SessionStatusUpdateRequest) o;
    return Objects.equals(this.newStatus, sessionStatusUpdateRequest.newStatus) &&
        Objects.equals(this.reason, sessionStatusUpdateRequest.reason) &&
        Objects.equals(this.triggeredBy, sessionStatusUpdateRequest.triggeredBy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(newStatus, reason, triggeredBy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SessionStatusUpdateRequest {\n");
    sb.append("    newStatus: ").append(toIndentedString(newStatus)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    triggeredBy: ").append(toIndentedString(triggeredBy)).append("\n");
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

