package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.RewardAnalyticsResponseTopRewardsInner;
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
 * RewardAnalyticsResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class RewardAnalyticsResponse {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime generatedAt;

  private @Nullable String seasonId;

  private @Nullable String range;

  private @Nullable Integer totalClaims;

  private @Nullable Integer premiumClaims;

  private @Nullable Integer rerollUsage;

  private @Nullable BigDecimal challengeCompletionRate;

  private @Nullable BigDecimal boostActivationRate;

  @Valid
  private List<@Valid RewardAnalyticsResponseTopRewardsInner> topRewards = new ArrayList<>();

  public RewardAnalyticsResponse generatedAt(@Nullable OffsetDateTime generatedAt) {
    this.generatedAt = generatedAt;
    return this;
  }

  /**
   * Get generatedAt
   * @return generatedAt
   */
  @Valid 
  @Schema(name = "generatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("generatedAt")
  public @Nullable OffsetDateTime getGeneratedAt() {
    return generatedAt;
  }

  public void setGeneratedAt(@Nullable OffsetDateTime generatedAt) {
    this.generatedAt = generatedAt;
  }

  public RewardAnalyticsResponse seasonId(@Nullable String seasonId) {
    this.seasonId = seasonId;
    return this;
  }

  /**
   * Get seasonId
   * @return seasonId
   */
  
  @Schema(name = "seasonId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seasonId")
  public @Nullable String getSeasonId() {
    return seasonId;
  }

  public void setSeasonId(@Nullable String seasonId) {
    this.seasonId = seasonId;
  }

  public RewardAnalyticsResponse range(@Nullable String range) {
    this.range = range;
    return this;
  }

  /**
   * Get range
   * @return range
   */
  
  @Schema(name = "range", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("range")
  public @Nullable String getRange() {
    return range;
  }

  public void setRange(@Nullable String range) {
    this.range = range;
  }

  public RewardAnalyticsResponse totalClaims(@Nullable Integer totalClaims) {
    this.totalClaims = totalClaims;
    return this;
  }

  /**
   * Get totalClaims
   * @return totalClaims
   */
  
  @Schema(name = "totalClaims", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalClaims")
  public @Nullable Integer getTotalClaims() {
    return totalClaims;
  }

  public void setTotalClaims(@Nullable Integer totalClaims) {
    this.totalClaims = totalClaims;
  }

  public RewardAnalyticsResponse premiumClaims(@Nullable Integer premiumClaims) {
    this.premiumClaims = premiumClaims;
    return this;
  }

  /**
   * Get premiumClaims
   * @return premiumClaims
   */
  
  @Schema(name = "premiumClaims", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("premiumClaims")
  public @Nullable Integer getPremiumClaims() {
    return premiumClaims;
  }

  public void setPremiumClaims(@Nullable Integer premiumClaims) {
    this.premiumClaims = premiumClaims;
  }

  public RewardAnalyticsResponse rerollUsage(@Nullable Integer rerollUsage) {
    this.rerollUsage = rerollUsage;
    return this;
  }

  /**
   * Get rerollUsage
   * @return rerollUsage
   */
  
  @Schema(name = "rerollUsage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rerollUsage")
  public @Nullable Integer getRerollUsage() {
    return rerollUsage;
  }

  public void setRerollUsage(@Nullable Integer rerollUsage) {
    this.rerollUsage = rerollUsage;
  }

  public RewardAnalyticsResponse challengeCompletionRate(@Nullable BigDecimal challengeCompletionRate) {
    this.challengeCompletionRate = challengeCompletionRate;
    return this;
  }

  /**
   * Get challengeCompletionRate
   * @return challengeCompletionRate
   */
  @Valid 
  @Schema(name = "challengeCompletionRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("challengeCompletionRate")
  public @Nullable BigDecimal getChallengeCompletionRate() {
    return challengeCompletionRate;
  }

  public void setChallengeCompletionRate(@Nullable BigDecimal challengeCompletionRate) {
    this.challengeCompletionRate = challengeCompletionRate;
  }

  public RewardAnalyticsResponse boostActivationRate(@Nullable BigDecimal boostActivationRate) {
    this.boostActivationRate = boostActivationRate;
    return this;
  }

  /**
   * Get boostActivationRate
   * @return boostActivationRate
   */
  @Valid 
  @Schema(name = "boostActivationRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("boostActivationRate")
  public @Nullable BigDecimal getBoostActivationRate() {
    return boostActivationRate;
  }

  public void setBoostActivationRate(@Nullable BigDecimal boostActivationRate) {
    this.boostActivationRate = boostActivationRate;
  }

  public RewardAnalyticsResponse topRewards(List<@Valid RewardAnalyticsResponseTopRewardsInner> topRewards) {
    this.topRewards = topRewards;
    return this;
  }

  public RewardAnalyticsResponse addTopRewardsItem(RewardAnalyticsResponseTopRewardsInner topRewardsItem) {
    if (this.topRewards == null) {
      this.topRewards = new ArrayList<>();
    }
    this.topRewards.add(topRewardsItem);
    return this;
  }

  /**
   * Get topRewards
   * @return topRewards
   */
  @Valid 
  @Schema(name = "topRewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("topRewards")
  public List<@Valid RewardAnalyticsResponseTopRewardsInner> getTopRewards() {
    return topRewards;
  }

  public void setTopRewards(List<@Valid RewardAnalyticsResponseTopRewardsInner> topRewards) {
    this.topRewards = topRewards;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RewardAnalyticsResponse rewardAnalyticsResponse = (RewardAnalyticsResponse) o;
    return Objects.equals(this.generatedAt, rewardAnalyticsResponse.generatedAt) &&
        Objects.equals(this.seasonId, rewardAnalyticsResponse.seasonId) &&
        Objects.equals(this.range, rewardAnalyticsResponse.range) &&
        Objects.equals(this.totalClaims, rewardAnalyticsResponse.totalClaims) &&
        Objects.equals(this.premiumClaims, rewardAnalyticsResponse.premiumClaims) &&
        Objects.equals(this.rerollUsage, rewardAnalyticsResponse.rerollUsage) &&
        Objects.equals(this.challengeCompletionRate, rewardAnalyticsResponse.challengeCompletionRate) &&
        Objects.equals(this.boostActivationRate, rewardAnalyticsResponse.boostActivationRate) &&
        Objects.equals(this.topRewards, rewardAnalyticsResponse.topRewards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(generatedAt, seasonId, range, totalClaims, premiumClaims, rerollUsage, challengeCompletionRate, boostActivationRate, topRewards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RewardAnalyticsResponse {\n");
    sb.append("    generatedAt: ").append(toIndentedString(generatedAt)).append("\n");
    sb.append("    seasonId: ").append(toIndentedString(seasonId)).append("\n");
    sb.append("    range: ").append(toIndentedString(range)).append("\n");
    sb.append("    totalClaims: ").append(toIndentedString(totalClaims)).append("\n");
    sb.append("    premiumClaims: ").append(toIndentedString(premiumClaims)).append("\n");
    sb.append("    rerollUsage: ").append(toIndentedString(rerollUsage)).append("\n");
    sb.append("    challengeCompletionRate: ").append(toIndentedString(challengeCompletionRate)).append("\n");
    sb.append("    boostActivationRate: ").append(toIndentedString(boostActivationRate)).append("\n");
    sb.append("    topRewards: ").append(toIndentedString(topRewards)).append("\n");
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

