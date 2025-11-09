package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.LeaderboardEntry;
import com.necpgame.gameplayservice.model.SeasonStatus;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * SeasonSummary
 */


public class SeasonSummary {

  private String leagueId;

  private @Nullable String seasonName;

  private SeasonStatus seasonStatus;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endsAt;

  private @Nullable Integer averageRating;

  private @Nullable Integer medianRating;

  @Valid
  private Map<String, Float> distribution = new HashMap<>();

  @Valid
  private List<@Valid LeaderboardEntry> topPlayers = new ArrayList<>();

  public SeasonSummary() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SeasonSummary(String leagueId, SeasonStatus seasonStatus) {
    this.leagueId = leagueId;
    this.seasonStatus = seasonStatus;
  }

  public SeasonSummary leagueId(String leagueId) {
    this.leagueId = leagueId;
    return this;
  }

  /**
   * Get leagueId
   * @return leagueId
   */
  @NotNull 
  @Schema(name = "leagueId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("leagueId")
  public String getLeagueId() {
    return leagueId;
  }

  public void setLeagueId(String leagueId) {
    this.leagueId = leagueId;
  }

  public SeasonSummary seasonName(@Nullable String seasonName) {
    this.seasonName = seasonName;
    return this;
  }

  /**
   * Get seasonName
   * @return seasonName
   */
  
  @Schema(name = "seasonName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seasonName")
  public @Nullable String getSeasonName() {
    return seasonName;
  }

  public void setSeasonName(@Nullable String seasonName) {
    this.seasonName = seasonName;
  }

  public SeasonSummary seasonStatus(SeasonStatus seasonStatus) {
    this.seasonStatus = seasonStatus;
    return this;
  }

  /**
   * Get seasonStatus
   * @return seasonStatus
   */
  @NotNull @Valid 
  @Schema(name = "seasonStatus", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("seasonStatus")
  public SeasonStatus getSeasonStatus() {
    return seasonStatus;
  }

  public void setSeasonStatus(SeasonStatus seasonStatus) {
    this.seasonStatus = seasonStatus;
  }

  public SeasonSummary startedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  /**
   * Get startedAt
   * @return startedAt
   */
  @Valid 
  @Schema(name = "startedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startedAt")
  public @Nullable OffsetDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public SeasonSummary endsAt(@Nullable OffsetDateTime endsAt) {
    this.endsAt = endsAt;
    return this;
  }

  /**
   * Get endsAt
   * @return endsAt
   */
  @Valid 
  @Schema(name = "endsAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endsAt")
  public @Nullable OffsetDateTime getEndsAt() {
    return endsAt;
  }

  public void setEndsAt(@Nullable OffsetDateTime endsAt) {
    this.endsAt = endsAt;
  }

  public SeasonSummary averageRating(@Nullable Integer averageRating) {
    this.averageRating = averageRating;
    return this;
  }

  /**
   * Get averageRating
   * @return averageRating
   */
  
  @Schema(name = "averageRating", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("averageRating")
  public @Nullable Integer getAverageRating() {
    return averageRating;
  }

  public void setAverageRating(@Nullable Integer averageRating) {
    this.averageRating = averageRating;
  }

  public SeasonSummary medianRating(@Nullable Integer medianRating) {
    this.medianRating = medianRating;
    return this;
  }

  /**
   * Get medianRating
   * @return medianRating
   */
  
  @Schema(name = "medianRating", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("medianRating")
  public @Nullable Integer getMedianRating() {
    return medianRating;
  }

  public void setMedianRating(@Nullable Integer medianRating) {
    this.medianRating = medianRating;
  }

  public SeasonSummary distribution(Map<String, Float> distribution) {
    this.distribution = distribution;
    return this;
  }

  public SeasonSummary putDistributionItem(String key, Float distributionItem) {
    if (this.distribution == null) {
      this.distribution = new HashMap<>();
    }
    this.distribution.put(key, distributionItem);
    return this;
  }

  /**
   * Get distribution
   * @return distribution
   */
  
  @Schema(name = "distribution", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("distribution")
  public Map<String, Float> getDistribution() {
    return distribution;
  }

  public void setDistribution(Map<String, Float> distribution) {
    this.distribution = distribution;
  }

  public SeasonSummary topPlayers(List<@Valid LeaderboardEntry> topPlayers) {
    this.topPlayers = topPlayers;
    return this;
  }

  public SeasonSummary addTopPlayersItem(LeaderboardEntry topPlayersItem) {
    if (this.topPlayers == null) {
      this.topPlayers = new ArrayList<>();
    }
    this.topPlayers.add(topPlayersItem);
    return this;
  }

  /**
   * Get topPlayers
   * @return topPlayers
   */
  @Valid 
  @Schema(name = "topPlayers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("topPlayers")
  public List<@Valid LeaderboardEntry> getTopPlayers() {
    return topPlayers;
  }

  public void setTopPlayers(List<@Valid LeaderboardEntry> topPlayers) {
    this.topPlayers = topPlayers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SeasonSummary seasonSummary = (SeasonSummary) o;
    return Objects.equals(this.leagueId, seasonSummary.leagueId) &&
        Objects.equals(this.seasonName, seasonSummary.seasonName) &&
        Objects.equals(this.seasonStatus, seasonSummary.seasonStatus) &&
        Objects.equals(this.startedAt, seasonSummary.startedAt) &&
        Objects.equals(this.endsAt, seasonSummary.endsAt) &&
        Objects.equals(this.averageRating, seasonSummary.averageRating) &&
        Objects.equals(this.medianRating, seasonSummary.medianRating) &&
        Objects.equals(this.distribution, seasonSummary.distribution) &&
        Objects.equals(this.topPlayers, seasonSummary.topPlayers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(leagueId, seasonName, seasonStatus, startedAt, endsAt, averageRating, medianRating, distribution, topPlayers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SeasonSummary {\n");
    sb.append("    leagueId: ").append(toIndentedString(leagueId)).append("\n");
    sb.append("    seasonName: ").append(toIndentedString(seasonName)).append("\n");
    sb.append("    seasonStatus: ").append(toIndentedString(seasonStatus)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    endsAt: ").append(toIndentedString(endsAt)).append("\n");
    sb.append("    averageRating: ").append(toIndentedString(averageRating)).append("\n");
    sb.append("    medianRating: ").append(toIndentedString(medianRating)).append("\n");
    sb.append("    distribution: ").append(toIndentedString(distribution)).append("\n");
    sb.append("    topPlayers: ").append(toIndentedString(topPlayers)).append("\n");
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

