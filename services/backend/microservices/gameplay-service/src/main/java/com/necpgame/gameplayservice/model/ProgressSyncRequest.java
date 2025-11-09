package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ProgressSyncRequest
 */


public class ProgressSyncRequest {

  private String playerId;

  private String seasonId;

  private @Nullable String revertSeasonId;

  public ProgressSyncRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ProgressSyncRequest(String playerId, String seasonId) {
    this.playerId = playerId;
    this.seasonId = seasonId;
  }

  public ProgressSyncRequest playerId(String playerId) {
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

  public ProgressSyncRequest seasonId(String seasonId) {
    this.seasonId = seasonId;
    return this;
  }

  /**
   * Get seasonId
   * @return seasonId
   */
  @NotNull 
  @Schema(name = "seasonId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("seasonId")
  public String getSeasonId() {
    return seasonId;
  }

  public void setSeasonId(String seasonId) {
    this.seasonId = seasonId;
  }

  public ProgressSyncRequest revertSeasonId(@Nullable String revertSeasonId) {
    this.revertSeasonId = revertSeasonId;
    return this;
  }

  /**
   * Get revertSeasonId
   * @return revertSeasonId
   */
  
  @Schema(name = "revertSeasonId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("revertSeasonId")
  public @Nullable String getRevertSeasonId() {
    return revertSeasonId;
  }

  public void setRevertSeasonId(@Nullable String revertSeasonId) {
    this.revertSeasonId = revertSeasonId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProgressSyncRequest progressSyncRequest = (ProgressSyncRequest) o;
    return Objects.equals(this.playerId, progressSyncRequest.playerId) &&
        Objects.equals(this.seasonId, progressSyncRequest.seasonId) &&
        Objects.equals(this.revertSeasonId, progressSyncRequest.revertSeasonId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, seasonId, revertSeasonId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProgressSyncRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    seasonId: ").append(toIndentedString(seasonId)).append("\n");
    sb.append("    revertSeasonId: ").append(toIndentedString(revertSeasonId)).append("\n");
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

