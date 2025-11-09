package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RatingSeasonStats
 */


public class RatingSeasonStats {

  private @Nullable String seasonId;

  private @Nullable Integer matchesCompleted;

  private @Nullable Integer disputesOpened;

  private @Nullable Integer disputesLost;

  private @Nullable Integer perfectOrders;

  @Valid
  private List<String> awards = new ArrayList<>();

  public RatingSeasonStats seasonId(@Nullable String seasonId) {
    this.seasonId = seasonId;
    return this;
  }

  /**
   * Get seasonId
   * @return seasonId
   */
  
  @Schema(name = "seasonId", example = "2077-Q4", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seasonId")
  public @Nullable String getSeasonId() {
    return seasonId;
  }

  public void setSeasonId(@Nullable String seasonId) {
    this.seasonId = seasonId;
  }

  public RatingSeasonStats matchesCompleted(@Nullable Integer matchesCompleted) {
    this.matchesCompleted = matchesCompleted;
    return this;
  }

  /**
   * Get matchesCompleted
   * minimum: 0
   * @return matchesCompleted
   */
  @Min(value = 0) 
  @Schema(name = "matchesCompleted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("matchesCompleted")
  public @Nullable Integer getMatchesCompleted() {
    return matchesCompleted;
  }

  public void setMatchesCompleted(@Nullable Integer matchesCompleted) {
    this.matchesCompleted = matchesCompleted;
  }

  public RatingSeasonStats disputesOpened(@Nullable Integer disputesOpened) {
    this.disputesOpened = disputesOpened;
    return this;
  }

  /**
   * Get disputesOpened
   * minimum: 0
   * @return disputesOpened
   */
  @Min(value = 0) 
  @Schema(name = "disputesOpened", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("disputesOpened")
  public @Nullable Integer getDisputesOpened() {
    return disputesOpened;
  }

  public void setDisputesOpened(@Nullable Integer disputesOpened) {
    this.disputesOpened = disputesOpened;
  }

  public RatingSeasonStats disputesLost(@Nullable Integer disputesLost) {
    this.disputesLost = disputesLost;
    return this;
  }

  /**
   * Get disputesLost
   * minimum: 0
   * @return disputesLost
   */
  @Min(value = 0) 
  @Schema(name = "disputesLost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("disputesLost")
  public @Nullable Integer getDisputesLost() {
    return disputesLost;
  }

  public void setDisputesLost(@Nullable Integer disputesLost) {
    this.disputesLost = disputesLost;
  }

  public RatingSeasonStats perfectOrders(@Nullable Integer perfectOrders) {
    this.perfectOrders = perfectOrders;
    return this;
  }

  /**
   * Get perfectOrders
   * minimum: 0
   * @return perfectOrders
   */
  @Min(value = 0) 
  @Schema(name = "perfectOrders", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("perfectOrders")
  public @Nullable Integer getPerfectOrders() {
    return perfectOrders;
  }

  public void setPerfectOrders(@Nullable Integer perfectOrders) {
    this.perfectOrders = perfectOrders;
  }

  public RatingSeasonStats awards(List<String> awards) {
    this.awards = awards;
    return this;
  }

  public RatingSeasonStats addAwardsItem(String awardsItem) {
    if (this.awards == null) {
      this.awards = new ArrayList<>();
    }
    this.awards.add(awardsItem);
    return this;
  }

  /**
   * Get awards
   * @return awards
   */
  
  @Schema(name = "awards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("awards")
  public List<String> getAwards() {
    return awards;
  }

  public void setAwards(List<String> awards) {
    this.awards = awards;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatingSeasonStats ratingSeasonStats = (RatingSeasonStats) o;
    return Objects.equals(this.seasonId, ratingSeasonStats.seasonId) &&
        Objects.equals(this.matchesCompleted, ratingSeasonStats.matchesCompleted) &&
        Objects.equals(this.disputesOpened, ratingSeasonStats.disputesOpened) &&
        Objects.equals(this.disputesLost, ratingSeasonStats.disputesLost) &&
        Objects.equals(this.perfectOrders, ratingSeasonStats.perfectOrders) &&
        Objects.equals(this.awards, ratingSeasonStats.awards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seasonId, matchesCompleted, disputesOpened, disputesLost, perfectOrders, awards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatingSeasonStats {\n");
    sb.append("    seasonId: ").append(toIndentedString(seasonId)).append("\n");
    sb.append("    matchesCompleted: ").append(toIndentedString(matchesCompleted)).append("\n");
    sb.append("    disputesOpened: ").append(toIndentedString(disputesOpened)).append("\n");
    sb.append("    disputesLost: ").append(toIndentedString(disputesLost)).append("\n");
    sb.append("    perfectOrders: ").append(toIndentedString(perfectOrders)).append("\n");
    sb.append("    awards: ").append(toIndentedString(awards)).append("\n");
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

