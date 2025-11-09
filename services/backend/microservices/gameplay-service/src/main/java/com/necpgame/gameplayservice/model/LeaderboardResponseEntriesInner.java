package com.necpgame.gameplayservice.model;

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
 * LeaderboardResponseEntriesInner
 */

@JsonTypeName("LeaderboardResponse_entries_inner")

public class LeaderboardResponseEntriesInner {

  private @Nullable Integer rank;

  private @Nullable String playerId;

  private @Nullable Integer metricValue;

  public LeaderboardResponseEntriesInner rank(@Nullable Integer rank) {
    this.rank = rank;
    return this;
  }

  /**
   * Get rank
   * @return rank
   */
  
  @Schema(name = "rank", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rank")
  public @Nullable Integer getRank() {
    return rank;
  }

  public void setRank(@Nullable Integer rank) {
    this.rank = rank;
  }

  public LeaderboardResponseEntriesInner playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public LeaderboardResponseEntriesInner metricValue(@Nullable Integer metricValue) {
    this.metricValue = metricValue;
    return this;
  }

  /**
   * Get metricValue
   * @return metricValue
   */
  
  @Schema(name = "metricValue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metricValue")
  public @Nullable Integer getMetricValue() {
    return metricValue;
  }

  public void setMetricValue(@Nullable Integer metricValue) {
    this.metricValue = metricValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LeaderboardResponseEntriesInner leaderboardResponseEntriesInner = (LeaderboardResponseEntriesInner) o;
    return Objects.equals(this.rank, leaderboardResponseEntriesInner.rank) &&
        Objects.equals(this.playerId, leaderboardResponseEntriesInner.playerId) &&
        Objects.equals(this.metricValue, leaderboardResponseEntriesInner.metricValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rank, playerId, metricValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeaderboardResponseEntriesInner {\n");
    sb.append("    rank: ").append(toIndentedString(rank)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    metricValue: ").append(toIndentedString(metricValue)).append("\n");
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

