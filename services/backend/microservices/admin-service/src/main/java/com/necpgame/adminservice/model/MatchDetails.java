package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.MatchDetailsTeamsInner;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * MatchDetails
 */


public class MatchDetails {

  private @Nullable String matchId;

  private @Nullable String activityType;

  @Valid
  private List<@Valid MatchDetailsTeamsInner> teams = new ArrayList<>();

  private @Nullable BigDecimal averageRating;

  private @Nullable String server;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  public MatchDetails matchId(@Nullable String matchId) {
    this.matchId = matchId;
    return this;
  }

  /**
   * Get matchId
   * @return matchId
   */
  
  @Schema(name = "match_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("match_id")
  public @Nullable String getMatchId() {
    return matchId;
  }

  public void setMatchId(@Nullable String matchId) {
    this.matchId = matchId;
  }

  public MatchDetails activityType(@Nullable String activityType) {
    this.activityType = activityType;
    return this;
  }

  /**
   * Get activityType
   * @return activityType
   */
  
  @Schema(name = "activity_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activity_type")
  public @Nullable String getActivityType() {
    return activityType;
  }

  public void setActivityType(@Nullable String activityType) {
    this.activityType = activityType;
  }

  public MatchDetails teams(List<@Valid MatchDetailsTeamsInner> teams) {
    this.teams = teams;
    return this;
  }

  public MatchDetails addTeamsItem(MatchDetailsTeamsInner teamsItem) {
    if (this.teams == null) {
      this.teams = new ArrayList<>();
    }
    this.teams.add(teamsItem);
    return this;
  }

  /**
   * Get teams
   * @return teams
   */
  @Valid 
  @Schema(name = "teams", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("teams")
  public List<@Valid MatchDetailsTeamsInner> getTeams() {
    return teams;
  }

  public void setTeams(List<@Valid MatchDetailsTeamsInner> teams) {
    this.teams = teams;
  }

  public MatchDetails averageRating(@Nullable BigDecimal averageRating) {
    this.averageRating = averageRating;
    return this;
  }

  /**
   * Get averageRating
   * @return averageRating
   */
  @Valid 
  @Schema(name = "average_rating", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("average_rating")
  public @Nullable BigDecimal getAverageRating() {
    return averageRating;
  }

  public void setAverageRating(@Nullable BigDecimal averageRating) {
    this.averageRating = averageRating;
  }

  public MatchDetails server(@Nullable String server) {
    this.server = server;
    return this;
  }

  /**
   * Get server
   * @return server
   */
  
  @Schema(name = "server", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("server")
  public @Nullable String getServer() {
    return server;
  }

  public void setServer(@Nullable String server) {
    this.server = server;
  }

  public MatchDetails createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "created_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_at")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchDetails matchDetails = (MatchDetails) o;
    return Objects.equals(this.matchId, matchDetails.matchId) &&
        Objects.equals(this.activityType, matchDetails.activityType) &&
        Objects.equals(this.teams, matchDetails.teams) &&
        Objects.equals(this.averageRating, matchDetails.averageRating) &&
        Objects.equals(this.server, matchDetails.server) &&
        Objects.equals(this.createdAt, matchDetails.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(matchId, activityType, teams, averageRating, server, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchDetails {\n");
    sb.append("    matchId: ").append(toIndentedString(matchId)).append("\n");
    sb.append("    activityType: ").append(toIndentedString(activityType)).append("\n");
    sb.append("    teams: ").append(toIndentedString(teams)).append("\n");
    sb.append("    averageRating: ").append(toIndentedString(averageRating)).append("\n");
    sb.append("    server: ").append(toIndentedString(server)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

