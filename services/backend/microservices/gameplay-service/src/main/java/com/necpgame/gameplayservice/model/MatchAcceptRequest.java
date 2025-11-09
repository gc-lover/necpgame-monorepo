package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * MatchAcceptRequest
 */


public class MatchAcceptRequest {

  private UUID playerId;

  private @Nullable UUID partyId;

  private @Nullable Integer clientLatencyMs;

  private String readyCheckToken;

  public MatchAcceptRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MatchAcceptRequest(UUID playerId, String readyCheckToken) {
    this.playerId = playerId;
    this.readyCheckToken = readyCheckToken;
  }

  public MatchAcceptRequest playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public MatchAcceptRequest partyId(@Nullable UUID partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  @Valid 
  @Schema(name = "partyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partyId")
  public @Nullable UUID getPartyId() {
    return partyId;
  }

  public void setPartyId(@Nullable UUID partyId) {
    this.partyId = partyId;
  }

  public MatchAcceptRequest clientLatencyMs(@Nullable Integer clientLatencyMs) {
    this.clientLatencyMs = clientLatencyMs;
    return this;
  }

  /**
   * Get clientLatencyMs
   * minimum: 0
   * @return clientLatencyMs
   */
  @Min(value = 0) 
  @Schema(name = "clientLatencyMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("clientLatencyMs")
  public @Nullable Integer getClientLatencyMs() {
    return clientLatencyMs;
  }

  public void setClientLatencyMs(@Nullable Integer clientLatencyMs) {
    this.clientLatencyMs = clientLatencyMs;
  }

  public MatchAcceptRequest readyCheckToken(String readyCheckToken) {
    this.readyCheckToken = readyCheckToken;
    return this;
  }

  /**
   * Get readyCheckToken
   * @return readyCheckToken
   */
  @NotNull @Size(min = 16, max = 64) 
  @Schema(name = "readyCheckToken", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("readyCheckToken")
  public String getReadyCheckToken() {
    return readyCheckToken;
  }

  public void setReadyCheckToken(String readyCheckToken) {
    this.readyCheckToken = readyCheckToken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchAcceptRequest matchAcceptRequest = (MatchAcceptRequest) o;
    return Objects.equals(this.playerId, matchAcceptRequest.playerId) &&
        Objects.equals(this.partyId, matchAcceptRequest.partyId) &&
        Objects.equals(this.clientLatencyMs, matchAcceptRequest.clientLatencyMs) &&
        Objects.equals(this.readyCheckToken, matchAcceptRequest.readyCheckToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, partyId, clientLatencyMs, readyCheckToken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchAcceptRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    clientLatencyMs: ").append(toIndentedString(clientLatencyMs)).append("\n");
    sb.append("    readyCheckToken: ").append(toIndentedString(readyCheckToken)).append("\n");
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

