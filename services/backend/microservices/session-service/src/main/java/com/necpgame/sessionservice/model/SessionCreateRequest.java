package com.necpgame.sessionservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * SessionCreateRequest
 */


public class SessionCreateRequest {

  private String playerId;

  private String accountId;

  private @Nullable String characterId;

  private String authToken;

  private @Nullable ClientInfo clientInfo;

  private @Nullable String ipAddress;

  public SessionCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SessionCreateRequest(String playerId, String accountId, String authToken) {
    this.playerId = playerId;
    this.accountId = accountId;
    this.authToken = authToken;
  }

  public SessionCreateRequest playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public SessionCreateRequest accountId(String accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * Get accountId
   * @return accountId
   */
  @NotNull 
  @Schema(name = "accountId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("accountId")
  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public SessionCreateRequest characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "characterId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("characterId")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public SessionCreateRequest authToken(String authToken) {
    this.authToken = authToken;
    return this;
  }

  /**
   * Get authToken
   * @return authToken
   */
  @NotNull 
  @Schema(name = "authToken", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("authToken")
  public String getAuthToken() {
    return authToken;
  }

  public void setAuthToken(String authToken) {
    this.authToken = authToken;
  }

  public SessionCreateRequest clientInfo(@Nullable ClientInfo clientInfo) {
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

  public SessionCreateRequest ipAddress(@Nullable String ipAddress) {
    this.ipAddress = ipAddress;
    return this;
  }

  /**
   * Get ipAddress
   * @return ipAddress
   */
  
  @Schema(name = "ipAddress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ipAddress")
  public @Nullable String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(@Nullable String ipAddress) {
    this.ipAddress = ipAddress;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SessionCreateRequest sessionCreateRequest = (SessionCreateRequest) o;
    return Objects.equals(this.playerId, sessionCreateRequest.playerId) &&
        Objects.equals(this.accountId, sessionCreateRequest.accountId) &&
        Objects.equals(this.characterId, sessionCreateRequest.characterId) &&
        Objects.equals(this.authToken, sessionCreateRequest.authToken) &&
        Objects.equals(this.clientInfo, sessionCreateRequest.clientInfo) &&
        Objects.equals(this.ipAddress, sessionCreateRequest.ipAddress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, accountId, characterId, authToken, clientInfo, ipAddress);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SessionCreateRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    authToken: ").append(toIndentedString(authToken)).append("\n");
    sb.append("    clientInfo: ").append(toIndentedString(clientInfo)).append("\n");
    sb.append("    ipAddress: ").append(toIndentedString(ipAddress)).append("\n");
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

