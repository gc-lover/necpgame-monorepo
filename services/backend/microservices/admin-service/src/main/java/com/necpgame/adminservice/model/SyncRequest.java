package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SyncRequest
 */


public class SyncRequest {

  private UUID characterId;

  private Integer clientVersion;

  @Valid
  private Map<String, Object> clientState = new HashMap<>();

  public SyncRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SyncRequest(UUID characterId, Integer clientVersion) {
    this.characterId = characterId;
    this.clientVersion = clientVersion;
  }

  public SyncRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public SyncRequest clientVersion(Integer clientVersion) {
    this.clientVersion = clientVersion;
    return this;
  }

  /**
   * Get clientVersion
   * @return clientVersion
   */
  @NotNull 
  @Schema(name = "client_version", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("client_version")
  public Integer getClientVersion() {
    return clientVersion;
  }

  public void setClientVersion(Integer clientVersion) {
    this.clientVersion = clientVersion;
  }

  public SyncRequest clientState(Map<String, Object> clientState) {
    this.clientState = clientState;
    return this;
  }

  public SyncRequest putClientStateItem(String key, Object clientStateItem) {
    if (this.clientState == null) {
      this.clientState = new HashMap<>();
    }
    this.clientState.put(key, clientStateItem);
    return this;
  }

  /**
   * Состояние на клиенте
   * @return clientState
   */
  
  @Schema(name = "client_state", description = "Состояние на клиенте", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("client_state")
  public Map<String, Object> getClientState() {
    return clientState;
  }

  public void setClientState(Map<String, Object> clientState) {
    this.clientState = clientState;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SyncRequest syncRequest = (SyncRequest) o;
    return Objects.equals(this.characterId, syncRequest.characterId) &&
        Objects.equals(this.clientVersion, syncRequest.clientVersion) &&
        Objects.equals(this.clientState, syncRequest.clientState);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, clientVersion, clientState);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SyncRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    clientVersion: ").append(toIndentedString(clientVersion)).append("\n");
    sb.append("    clientState: ").append(toIndentedString(clientState)).append("\n");
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

