package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.ReconnectSession200ResponseSessionState;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ReconnectSession200Response
 */

@JsonTypeName("reconnectSession_200_response")

public class ReconnectSession200Response {

  private @Nullable String sessionId;

  private @Nullable Boolean reconnected;

  private @Nullable ReconnectSession200ResponseSessionState sessionState;

  public ReconnectSession200Response sessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  
  @Schema(name = "session_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("session_id")
  public @Nullable String getSessionId() {
    return sessionId;
  }

  public void setSessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
  }

  public ReconnectSession200Response reconnected(@Nullable Boolean reconnected) {
    this.reconnected = reconnected;
    return this;
  }

  /**
   * Get reconnected
   * @return reconnected
   */
  
  @Schema(name = "reconnected", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reconnected")
  public @Nullable Boolean getReconnected() {
    return reconnected;
  }

  public void setReconnected(@Nullable Boolean reconnected) {
    this.reconnected = reconnected;
  }

  public ReconnectSession200Response sessionState(@Nullable ReconnectSession200ResponseSessionState sessionState) {
    this.sessionState = sessionState;
    return this;
  }

  /**
   * Get sessionState
   * @return sessionState
   */
  @Valid 
  @Schema(name = "session_state", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("session_state")
  public @Nullable ReconnectSession200ResponseSessionState getSessionState() {
    return sessionState;
  }

  public void setSessionState(@Nullable ReconnectSession200ResponseSessionState sessionState) {
    this.sessionState = sessionState;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReconnectSession200Response reconnectSession200Response = (ReconnectSession200Response) o;
    return Objects.equals(this.sessionId, reconnectSession200Response.sessionId) &&
        Objects.equals(this.reconnected, reconnectSession200Response.reconnected) &&
        Objects.equals(this.sessionState, reconnectSession200Response.sessionState);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, reconnected, sessionState);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReconnectSession200Response {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    reconnected: ").append(toIndentedString(reconnected)).append("\n");
    sb.append("    sessionState: ").append(toIndentedString(sessionState)).append("\n");
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

