package com.necpgame.sessionservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.sessionservice.model.ClientInfo;
import com.necpgame.sessionservice.model.RestoreState;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ReconnectRequest
 */


public class ReconnectRequest {

  private String reconnectToken;

  private @Nullable ClientInfo clientInfo;

  private @Nullable RestoreState restoreState;

  public ReconnectRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReconnectRequest(String reconnectToken) {
    this.reconnectToken = reconnectToken;
  }

  public ReconnectRequest reconnectToken(String reconnectToken) {
    this.reconnectToken = reconnectToken;
    return this;
  }

  /**
   * Get reconnectToken
   * @return reconnectToken
   */
  @NotNull 
  @Schema(name = "reconnectToken", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reconnectToken")
  public String getReconnectToken() {
    return reconnectToken;
  }

  public void setReconnectToken(String reconnectToken) {
    this.reconnectToken = reconnectToken;
  }

  public ReconnectRequest clientInfo(@Nullable ClientInfo clientInfo) {
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

  public ReconnectRequest restoreState(@Nullable RestoreState restoreState) {
    this.restoreState = restoreState;
    return this;
  }

  /**
   * Get restoreState
   * @return restoreState
   */
  @Valid 
  @Schema(name = "restoreState", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("restoreState")
  public @Nullable RestoreState getRestoreState() {
    return restoreState;
  }

  public void setRestoreState(@Nullable RestoreState restoreState) {
    this.restoreState = restoreState;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReconnectRequest reconnectRequest = (ReconnectRequest) o;
    return Objects.equals(this.reconnectToken, reconnectRequest.reconnectToken) &&
        Objects.equals(this.clientInfo, reconnectRequest.clientInfo) &&
        Objects.equals(this.restoreState, reconnectRequest.restoreState);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reconnectToken, clientInfo, restoreState);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReconnectRequest {\n");
    sb.append("    reconnectToken: ").append(toIndentedString(reconnectToken)).append("\n");
    sb.append("    clientInfo: ").append(toIndentedString(clientInfo)).append("\n");
    sb.append("    restoreState: ").append(toIndentedString(restoreState)).append("\n");
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

