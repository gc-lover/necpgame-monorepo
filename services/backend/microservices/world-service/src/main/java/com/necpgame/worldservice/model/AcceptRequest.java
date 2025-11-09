package com.necpgame.worldservice.model;

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
 * AcceptRequest
 */


public class AcceptRequest {

  private UUID playerId;

  private @Nullable UUID partyId;

  private @Nullable UUID guildId;

  private @Nullable String entryContext;

  private Boolean forceOverride = false;

  public AcceptRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AcceptRequest(UUID playerId) {
    this.playerId = playerId;
  }

  public AcceptRequest playerId(UUID playerId) {
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

  public AcceptRequest partyId(@Nullable UUID partyId) {
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

  public AcceptRequest guildId(@Nullable UUID guildId) {
    this.guildId = guildId;
    return this;
  }

  /**
   * Get guildId
   * @return guildId
   */
  @Valid 
  @Schema(name = "guildId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guildId")
  public @Nullable UUID getGuildId() {
    return guildId;
  }

  public void setGuildId(@Nullable UUID guildId) {
    this.guildId = guildId;
  }

  public AcceptRequest entryContext(@Nullable String entryContext) {
    this.entryContext = entryContext;
    return this;
  }

  /**
   * Get entryContext
   * @return entryContext
   */
  
  @Schema(name = "entryContext", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entryContext")
  public @Nullable String getEntryContext() {
    return entryContext;
  }

  public void setEntryContext(@Nullable String entryContext) {
    this.entryContext = entryContext;
  }

  public AcceptRequest forceOverride(Boolean forceOverride) {
    this.forceOverride = forceOverride;
    return this;
  }

  /**
   * Get forceOverride
   * @return forceOverride
   */
  
  @Schema(name = "forceOverride", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("forceOverride")
  public Boolean getForceOverride() {
    return forceOverride;
  }

  public void setForceOverride(Boolean forceOverride) {
    this.forceOverride = forceOverride;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AcceptRequest acceptRequest = (AcceptRequest) o;
    return Objects.equals(this.playerId, acceptRequest.playerId) &&
        Objects.equals(this.partyId, acceptRequest.partyId) &&
        Objects.equals(this.guildId, acceptRequest.guildId) &&
        Objects.equals(this.entryContext, acceptRequest.entryContext) &&
        Objects.equals(this.forceOverride, acceptRequest.forceOverride);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, partyId, guildId, entryContext, forceOverride);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AcceptRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    guildId: ").append(toIndentedString(guildId)).append("\n");
    sb.append("    entryContext: ").append(toIndentedString(entryContext)).append("\n");
    sb.append("    forceOverride: ").append(toIndentedString(forceOverride)).append("\n");
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

