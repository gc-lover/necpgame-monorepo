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
 * CloseSessionRequest
 */

@JsonTypeName("closeSession_request")

public class CloseSessionRequest {

  private String sessionId;

  /**
   * Gets or Sets reason
   */
  public enum ReasonEnum {
    LOGOUT("logout"),
    
    TIMEOUT("timeout"),
    
    DISCONNECT("disconnect"),
    
    KICK("kick");

    private final String value;

    ReasonEnum(String value) {
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
    public static ReasonEnum fromValue(String value) {
      for (ReasonEnum b : ReasonEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ReasonEnum reason;

  public CloseSessionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CloseSessionRequest(String sessionId) {
    this.sessionId = sessionId;
  }

  public CloseSessionRequest sessionId(String sessionId) {
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

  public CloseSessionRequest reason(@Nullable ReasonEnum reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable ReasonEnum getReason() {
    return reason;
  }

  public void setReason(@Nullable ReasonEnum reason) {
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
    CloseSessionRequest closeSessionRequest = (CloseSessionRequest) o;
    return Objects.equals(this.sessionId, closeSessionRequest.sessionId) &&
        Objects.equals(this.reason, closeSessionRequest.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CloseSessionRequest {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
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

