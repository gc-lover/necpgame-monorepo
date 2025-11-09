package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.Role;
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
 * MatchParticipant
 */


public class MatchParticipant {

  private UUID playerId;

  private @Nullable UUID partyId;

  private Integer rating;

  private Role role;

  private @Nullable Integer latencyMs;

  private @Nullable String latencyRegion;

  private Boolean smurfFlag = false;

  private Boolean ready = false;

  public MatchParticipant() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MatchParticipant(UUID playerId, Integer rating, Role role) {
    this.playerId = playerId;
    this.rating = rating;
    this.role = role;
  }

  public MatchParticipant playerId(UUID playerId) {
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

  public MatchParticipant partyId(@Nullable UUID partyId) {
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

  public MatchParticipant rating(Integer rating) {
    this.rating = rating;
    return this;
  }

  /**
   * Get rating
   * minimum: 0
   * @return rating
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "rating", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rating")
  public Integer getRating() {
    return rating;
  }

  public void setRating(Integer rating) {
    this.rating = rating;
  }

  public MatchParticipant role(Role role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  @NotNull @Valid 
  @Schema(name = "role", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("role")
  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public MatchParticipant latencyMs(@Nullable Integer latencyMs) {
    this.latencyMs = latencyMs;
    return this;
  }

  /**
   * Get latencyMs
   * minimum: 0
   * @return latencyMs
   */
  @Min(value = 0) 
  @Schema(name = "latencyMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("latencyMs")
  public @Nullable Integer getLatencyMs() {
    return latencyMs;
  }

  public void setLatencyMs(@Nullable Integer latencyMs) {
    this.latencyMs = latencyMs;
  }

  public MatchParticipant latencyRegion(@Nullable String latencyRegion) {
    this.latencyRegion = latencyRegion;
    return this;
  }

  /**
   * Get latencyRegion
   * @return latencyRegion
   */
  
  @Schema(name = "latencyRegion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("latencyRegion")
  public @Nullable String getLatencyRegion() {
    return latencyRegion;
  }

  public void setLatencyRegion(@Nullable String latencyRegion) {
    this.latencyRegion = latencyRegion;
  }

  public MatchParticipant smurfFlag(Boolean smurfFlag) {
    this.smurfFlag = smurfFlag;
    return this;
  }

  /**
   * Get smurfFlag
   * @return smurfFlag
   */
  
  @Schema(name = "smurfFlag", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("smurfFlag")
  public Boolean getSmurfFlag() {
    return smurfFlag;
  }

  public void setSmurfFlag(Boolean smurfFlag) {
    this.smurfFlag = smurfFlag;
  }

  public MatchParticipant ready(Boolean ready) {
    this.ready = ready;
    return this;
  }

  /**
   * Get ready
   * @return ready
   */
  
  @Schema(name = "ready", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ready")
  public Boolean getReady() {
    return ready;
  }

  public void setReady(Boolean ready) {
    this.ready = ready;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchParticipant matchParticipant = (MatchParticipant) o;
    return Objects.equals(this.playerId, matchParticipant.playerId) &&
        Objects.equals(this.partyId, matchParticipant.partyId) &&
        Objects.equals(this.rating, matchParticipant.rating) &&
        Objects.equals(this.role, matchParticipant.role) &&
        Objects.equals(this.latencyMs, matchParticipant.latencyMs) &&
        Objects.equals(this.latencyRegion, matchParticipant.latencyRegion) &&
        Objects.equals(this.smurfFlag, matchParticipant.smurfFlag) &&
        Objects.equals(this.ready, matchParticipant.ready);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, partyId, rating, role, latencyMs, latencyRegion, smurfFlag, ready);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchParticipant {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    latencyMs: ").append(toIndentedString(latencyMs)).append("\n");
    sb.append("    latencyRegion: ").append(toIndentedString(latencyRegion)).append("\n");
    sb.append("    smurfFlag: ").append(toIndentedString(smurfFlag)).append("\n");
    sb.append("    ready: ").append(toIndentedString(ready)).append("\n");
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

