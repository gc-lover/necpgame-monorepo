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
 * UpdateLeaderboardScore200Response
 */

@JsonTypeName("updateLeaderboardScore_200_response")

public class UpdateLeaderboardScore200Response {

  private @Nullable Integer newRank;

  private @Nullable Integer previousRank;

  private @Nullable Integer rankChange;

  public UpdateLeaderboardScore200Response newRank(@Nullable Integer newRank) {
    this.newRank = newRank;
    return this;
  }

  /**
   * Get newRank
   * @return newRank
   */
  
  @Schema(name = "new_rank", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("new_rank")
  public @Nullable Integer getNewRank() {
    return newRank;
  }

  public void setNewRank(@Nullable Integer newRank) {
    this.newRank = newRank;
  }

  public UpdateLeaderboardScore200Response previousRank(@Nullable Integer previousRank) {
    this.previousRank = previousRank;
    return this;
  }

  /**
   * Get previousRank
   * @return previousRank
   */
  
  @Schema(name = "previous_rank", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previous_rank")
  public @Nullable Integer getPreviousRank() {
    return previousRank;
  }

  public void setPreviousRank(@Nullable Integer previousRank) {
    this.previousRank = previousRank;
  }

  public UpdateLeaderboardScore200Response rankChange(@Nullable Integer rankChange) {
    this.rankChange = rankChange;
    return this;
  }

  /**
   * Get rankChange
   * @return rankChange
   */
  
  @Schema(name = "rank_change", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rank_change")
  public @Nullable Integer getRankChange() {
    return rankChange;
  }

  public void setRankChange(@Nullable Integer rankChange) {
    this.rankChange = rankChange;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateLeaderboardScore200Response updateLeaderboardScore200Response = (UpdateLeaderboardScore200Response) o;
    return Objects.equals(this.newRank, updateLeaderboardScore200Response.newRank) &&
        Objects.equals(this.previousRank, updateLeaderboardScore200Response.previousRank) &&
        Objects.equals(this.rankChange, updateLeaderboardScore200Response.rankChange);
  }

  @Override
  public int hashCode() {
    return Objects.hash(newRank, previousRank, rankChange);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateLeaderboardScore200Response {\n");
    sb.append("    newRank: ").append(toIndentedString(newRank)).append("\n");
    sb.append("    previousRank: ").append(toIndentedString(previousRank)).append("\n");
    sb.append("    rankChange: ").append(toIndentedString(rankChange)).append("\n");
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

