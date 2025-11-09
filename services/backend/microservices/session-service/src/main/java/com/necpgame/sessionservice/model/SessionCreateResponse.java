package com.necpgame.sessionservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.sessionservice.model.SessionPolicies;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SessionCreateResponse
 */


public class SessionCreateResponse {

  private String sessionId;

  private String status;

  private @Nullable String reconnectToken;

  private @Nullable SessionPolicies policies;

  /**
   * Gets or Sets concurrentAction
   */
  public enum ConcurrentActionEnum {
    NONE("none"),
    
    TERMINATED_PREVIOUS("terminated_previous"),
    
    REJECTED("rejected");

    private final String value;

    ConcurrentActionEnum(String value) {
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
    public static ConcurrentActionEnum fromValue(String value) {
      for (ConcurrentActionEnum b : ConcurrentActionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ConcurrentActionEnum concurrentAction;

  public SessionCreateResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SessionCreateResponse(String sessionId, String status) {
    this.sessionId = sessionId;
    this.status = status;
  }

  public SessionCreateResponse sessionId(String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  @NotNull 
  @Schema(name = "sessionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("sessionId")
  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public SessionCreateResponse status(String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public SessionCreateResponse reconnectToken(@Nullable String reconnectToken) {
    this.reconnectToken = reconnectToken;
    return this;
  }

  /**
   * Get reconnectToken
   * @return reconnectToken
   */
  
  @Schema(name = "reconnectToken", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reconnectToken")
  public @Nullable String getReconnectToken() {
    return reconnectToken;
  }

  public void setReconnectToken(@Nullable String reconnectToken) {
    this.reconnectToken = reconnectToken;
  }

  public SessionCreateResponse policies(@Nullable SessionPolicies policies) {
    this.policies = policies;
    return this;
  }

  /**
   * Get policies
   * @return policies
   */
  @Valid 
  @Schema(name = "policies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("policies")
  public @Nullable SessionPolicies getPolicies() {
    return policies;
  }

  public void setPolicies(@Nullable SessionPolicies policies) {
    this.policies = policies;
  }

  public SessionCreateResponse concurrentAction(@Nullable ConcurrentActionEnum concurrentAction) {
    this.concurrentAction = concurrentAction;
    return this;
  }

  /**
   * Get concurrentAction
   * @return concurrentAction
   */
  
  @Schema(name = "concurrentAction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("concurrentAction")
  public @Nullable ConcurrentActionEnum getConcurrentAction() {
    return concurrentAction;
  }

  public void setConcurrentAction(@Nullable ConcurrentActionEnum concurrentAction) {
    this.concurrentAction = concurrentAction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SessionCreateResponse sessionCreateResponse = (SessionCreateResponse) o;
    return Objects.equals(this.sessionId, sessionCreateResponse.sessionId) &&
        Objects.equals(this.status, sessionCreateResponse.status) &&
        Objects.equals(this.reconnectToken, sessionCreateResponse.reconnectToken) &&
        Objects.equals(this.policies, sessionCreateResponse.policies) &&
        Objects.equals(this.concurrentAction, sessionCreateResponse.concurrentAction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, status, reconnectToken, policies, concurrentAction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SessionCreateResponse {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    reconnectToken: ").append(toIndentedString(reconnectToken)).append("\n");
    sb.append("    policies: ").append(toIndentedString(policies)).append("\n");
    sb.append("    concurrentAction: ").append(toIndentedString(concurrentAction)).append("\n");
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

