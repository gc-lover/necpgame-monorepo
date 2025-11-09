package com.necpgame.adminservice.model;

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
 * AppealRequest
 */


public class AppealRequest {

  private UUID banId;

  private UUID playerId;

  private String reason;

  private @Nullable String evidence;

  public AppealRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AppealRequest(UUID banId, UUID playerId, String reason) {
    this.banId = banId;
    this.playerId = playerId;
    this.reason = reason;
  }

  public AppealRequest banId(UUID banId) {
    this.banId = banId;
    return this;
  }

  /**
   * Get banId
   * @return banId
   */
  @NotNull @Valid 
  @Schema(name = "ban_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ban_id")
  public UUID getBanId() {
    return banId;
  }

  public void setBanId(UUID banId) {
    this.banId = banId;
  }

  public AppealRequest playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "player_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("player_id")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public AppealRequest reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @NotNull 
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public AppealRequest evidence(@Nullable String evidence) {
    this.evidence = evidence;
    return this;
  }

  /**
   * Get evidence
   * @return evidence
   */
  
  @Schema(name = "evidence", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("evidence")
  public @Nullable String getEvidence() {
    return evidence;
  }

  public void setEvidence(@Nullable String evidence) {
    this.evidence = evidence;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AppealRequest appealRequest = (AppealRequest) o;
    return Objects.equals(this.banId, appealRequest.banId) &&
        Objects.equals(this.playerId, appealRequest.playerId) &&
        Objects.equals(this.reason, appealRequest.reason) &&
        Objects.equals(this.evidence, appealRequest.evidence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(banId, playerId, reason, evidence);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AppealRequest {\n");
    sb.append("    banId: ").append(toIndentedString(banId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    evidence: ").append(toIndentedString(evidence)).append("\n");
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

