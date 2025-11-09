package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * SendHeartbeat200Response
 */

@JsonTypeName("sendHeartbeat_200_response")

public class SendHeartbeat200Response {

  private @Nullable String sessionId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastHeartbeat;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime serverTime;

  public SendHeartbeat200Response sessionId(@Nullable String sessionId) {
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

  public SendHeartbeat200Response lastHeartbeat(@Nullable OffsetDateTime lastHeartbeat) {
    this.lastHeartbeat = lastHeartbeat;
    return this;
  }

  /**
   * Get lastHeartbeat
   * @return lastHeartbeat
   */
  @Valid 
  @Schema(name = "last_heartbeat", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_heartbeat")
  public @Nullable OffsetDateTime getLastHeartbeat() {
    return lastHeartbeat;
  }

  public void setLastHeartbeat(@Nullable OffsetDateTime lastHeartbeat) {
    this.lastHeartbeat = lastHeartbeat;
  }

  public SendHeartbeat200Response serverTime(@Nullable OffsetDateTime serverTime) {
    this.serverTime = serverTime;
    return this;
  }

  /**
   * Get serverTime
   * @return serverTime
   */
  @Valid 
  @Schema(name = "server_time", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("server_time")
  public @Nullable OffsetDateTime getServerTime() {
    return serverTime;
  }

  public void setServerTime(@Nullable OffsetDateTime serverTime) {
    this.serverTime = serverTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SendHeartbeat200Response sendHeartbeat200Response = (SendHeartbeat200Response) o;
    return Objects.equals(this.sessionId, sendHeartbeat200Response.sessionId) &&
        Objects.equals(this.lastHeartbeat, sendHeartbeat200Response.lastHeartbeat) &&
        Objects.equals(this.serverTime, sendHeartbeat200Response.serverTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, lastHeartbeat, serverTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SendHeartbeat200Response {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    lastHeartbeat: ").append(toIndentedString(lastHeartbeat)).append("\n");
    sb.append("    serverTime: ").append(toIndentedString(serverTime)).append("\n");
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

