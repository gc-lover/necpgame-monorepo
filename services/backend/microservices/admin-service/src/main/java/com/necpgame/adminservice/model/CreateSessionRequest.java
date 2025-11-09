package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CreateSessionRequest
 */

@JsonTypeName("createSession_request")

public class CreateSessionRequest {

  private String accountId;

  private String characterId;

  private @Nullable String clientVersion;

  private @Nullable Object deviceInfo;

  public CreateSessionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateSessionRequest(String accountId, String characterId) {
    this.accountId = accountId;
    this.characterId = characterId;
  }

  public CreateSessionRequest accountId(String accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * Get accountId
   * @return accountId
   */
  @NotNull 
  @Schema(name = "account_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("account_id")
  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public CreateSessionRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public CreateSessionRequest clientVersion(@Nullable String clientVersion) {
    this.clientVersion = clientVersion;
    return this;
  }

  /**
   * Get clientVersion
   * @return clientVersion
   */
  
  @Schema(name = "client_version", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("client_version")
  public @Nullable String getClientVersion() {
    return clientVersion;
  }

  public void setClientVersion(@Nullable String clientVersion) {
    this.clientVersion = clientVersion;
  }

  public CreateSessionRequest deviceInfo(@Nullable Object deviceInfo) {
    this.deviceInfo = deviceInfo;
    return this;
  }

  /**
   * Get deviceInfo
   * @return deviceInfo
   */
  
  @Schema(name = "device_info", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("device_info")
  public @Nullable Object getDeviceInfo() {
    return deviceInfo;
  }

  public void setDeviceInfo(@Nullable Object deviceInfo) {
    this.deviceInfo = deviceInfo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateSessionRequest createSessionRequest = (CreateSessionRequest) o;
    return Objects.equals(this.accountId, createSessionRequest.accountId) &&
        Objects.equals(this.characterId, createSessionRequest.characterId) &&
        Objects.equals(this.clientVersion, createSessionRequest.clientVersion) &&
        Objects.equals(this.deviceInfo, createSessionRequest.deviceInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, characterId, clientVersion, deviceInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateSessionRequest {\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    clientVersion: ").append(toIndentedString(clientVersion)).append("\n");
    sb.append("    deviceInfo: ").append(toIndentedString(deviceInfo)).append("\n");
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

