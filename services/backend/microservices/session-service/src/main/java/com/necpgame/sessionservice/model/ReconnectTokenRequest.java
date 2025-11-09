package com.necpgame.sessionservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.sessionservice.model.ClientInfo;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ReconnectTokenRequest
 */


public class ReconnectTokenRequest {

  private String sessionId;

  /**
   * Gets or Sets disconnectReason
   */
  public enum DisconnectReasonEnum {
    NETWORK("network"),
    
    SERVER_SHUTDOWN("server_shutdown"),
    
    CLIENT_EXIT("client_exit"),
    
    UNKNOWN("unknown");

    private final String value;

    DisconnectReasonEnum(String value) {
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
    public static DisconnectReasonEnum fromValue(String value) {
      for (DisconnectReasonEnum b : DisconnectReasonEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable DisconnectReasonEnum disconnectReason;

  private @Nullable ClientInfo clientInfo;

  public ReconnectTokenRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReconnectTokenRequest(String sessionId) {
    this.sessionId = sessionId;
  }

  public ReconnectTokenRequest sessionId(String sessionId) {
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

  public ReconnectTokenRequest disconnectReason(@Nullable DisconnectReasonEnum disconnectReason) {
    this.disconnectReason = disconnectReason;
    return this;
  }

  /**
   * Get disconnectReason
   * @return disconnectReason
   */
  
  @Schema(name = "disconnectReason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("disconnectReason")
  public @Nullable DisconnectReasonEnum getDisconnectReason() {
    return disconnectReason;
  }

  public void setDisconnectReason(@Nullable DisconnectReasonEnum disconnectReason) {
    this.disconnectReason = disconnectReason;
  }

  public ReconnectTokenRequest clientInfo(@Nullable ClientInfo clientInfo) {
    this.clientInfo = clientInfo;
    return this;
  }

  /**
   * Get clientInfo
   * @return clientInfo
   */
  @Valid 
  @Schema(name = "clientInfo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("clientInfo")
  public @Nullable ClientInfo getClientInfo() {
    return clientInfo;
  }

  public void setClientInfo(@Nullable ClientInfo clientInfo) {
    this.clientInfo = clientInfo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReconnectTokenRequest reconnectTokenRequest = (ReconnectTokenRequest) o;
    return Objects.equals(this.sessionId, reconnectTokenRequest.sessionId) &&
        Objects.equals(this.disconnectReason, reconnectTokenRequest.disconnectReason) &&
        Objects.equals(this.clientInfo, reconnectTokenRequest.clientInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, disconnectReason, clientInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReconnectTokenRequest {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    disconnectReason: ").append(toIndentedString(disconnectReason)).append("\n");
    sb.append("    clientInfo: ").append(toIndentedString(clientInfo)).append("\n");
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

