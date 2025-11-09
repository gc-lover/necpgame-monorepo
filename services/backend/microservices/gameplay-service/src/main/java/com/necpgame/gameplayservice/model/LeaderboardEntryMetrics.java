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
 * LeaderboardEntryMetrics
 */

@JsonTypeName("LeaderboardEntry_metrics")

public class LeaderboardEntryMetrics {

  private @Nullable Integer missionsCompleted;

  private @Nullable Integer arenaWins;

  private @Nullable Integer bondingScore;

  public LeaderboardEntryMetrics missionsCompleted(@Nullable Integer missionsCompleted) {
    this.missionsCompleted = missionsCompleted;
    return this;
  }

  /**
   * Get missionsCompleted
   * @return missionsCompleted
   */
  
  @Schema(name = "missionsCompleted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("missionsCompleted")
  public @Nullable Integer getMissionsCompleted() {
    return missionsCompleted;
  }

  public void setMissionsCompleted(@Nullable Integer missionsCompleted) {
    this.missionsCompleted = missionsCompleted;
  }

  public LeaderboardEntryMetrics arenaWins(@Nullable Integer arenaWins) {
    this.arenaWins = arenaWins;
    return this;
  }

  /**
   * Get arenaWins
   * @return arenaWins
   */
  
  @Schema(name = "arenaWins", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("arenaWins")
  public @Nullable Integer getArenaWins() {
    return arenaWins;
  }

  public void setArenaWins(@Nullable Integer arenaWins) {
    this.arenaWins = arenaWins;
  }

  public LeaderboardEntryMetrics bondingScore(@Nullable Integer bondingScore) {
    this.bondingScore = bondingScore;
    return this;
  }

  /**
   * Get bondingScore
   * @return bondingScore
   */
  
  @Schema(name = "bondingScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bondingScore")
  public @Nullable Integer getBondingScore() {
    return bondingScore;
  }

  public void setBondingScore(@Nullable Integer bondingScore) {
    this.bondingScore = bondingScore;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LeaderboardEntryMetrics leaderboardEntryMetrics = (LeaderboardEntryMetrics) o;
    return Objects.equals(this.missionsCompleted, leaderboardEntryMetrics.missionsCompleted) &&
        Objects.equals(this.arenaWins, leaderboardEntryMetrics.arenaWins) &&
        Objects.equals(this.bondingScore, leaderboardEntryMetrics.bondingScore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(missionsCompleted, arenaWins, bondingScore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeaderboardEntryMetrics {\n");
    sb.append("    missionsCompleted: ").append(toIndentedString(missionsCompleted)).append("\n");
    sb.append("    arenaWins: ").append(toIndentedString(arenaWins)).append("\n");
    sb.append("    bondingScore: ").append(toIndentedString(bondingScore)).append("\n");
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

