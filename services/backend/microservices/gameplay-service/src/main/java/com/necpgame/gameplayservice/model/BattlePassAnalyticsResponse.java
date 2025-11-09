package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.BattlePassAnalyticsResponseRetentionMetrics;
import com.necpgame.gameplayservice.model.BattlePassAnalyticsResponseXpDistributionInner;
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
 * BattlePassAnalyticsResponse
 */


public class BattlePassAnalyticsResponse {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime generatedAt;

  private @Nullable String seasonId;

  private @Nullable String range;

  private @Nullable Integer activePlayers;

  private @Nullable BigDecimal premiumConversionRate;

  private @Nullable BigDecimal averageLevel;

  @Valid
  private List<@Valid BattlePassAnalyticsResponseXpDistributionInner> xpDistribution = new ArrayList<>();

  private @Nullable BattlePassAnalyticsResponseRetentionMetrics retentionMetrics;

  public BattlePassAnalyticsResponse generatedAt(@Nullable OffsetDateTime generatedAt) {
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

  public BattlePassAnalyticsResponse seasonId(@Nullable String seasonId) {
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

  public BattlePassAnalyticsResponse range(@Nullable String range) {
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

  public BattlePassAnalyticsResponse activePlayers(@Nullable Integer activePlayers) {
    this.activePlayers = activePlayers;
    return this;
  }

  /**
   * Get activePlayers
   * @return activePlayers
   */
  
  @Schema(name = "activePlayers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activePlayers")
  public @Nullable Integer getActivePlayers() {
    return activePlayers;
  }

  public void setActivePlayers(@Nullable Integer activePlayers) {
    this.activePlayers = activePlayers;
  }

  public BattlePassAnalyticsResponse premiumConversionRate(@Nullable BigDecimal premiumConversionRate) {
    this.premiumConversionRate = premiumConversionRate;
    return this;
  }

  /**
   * Get premiumConversionRate
   * @return premiumConversionRate
   */
  @Valid 
  @Schema(name = "premiumConversionRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("premiumConversionRate")
  public @Nullable BigDecimal getPremiumConversionRate() {
    return premiumConversionRate;
  }

  public void setPremiumConversionRate(@Nullable BigDecimal premiumConversionRate) {
    this.premiumConversionRate = premiumConversionRate;
  }

  public BattlePassAnalyticsResponse averageLevel(@Nullable BigDecimal averageLevel) {
    this.averageLevel = averageLevel;
    return this;
  }

  /**
   * Get averageLevel
   * @return averageLevel
   */
  @Valid 
  @Schema(name = "averageLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("averageLevel")
  public @Nullable BigDecimal getAverageLevel() {
    return averageLevel;
  }

  public void setAverageLevel(@Nullable BigDecimal averageLevel) {
    this.averageLevel = averageLevel;
  }

  public BattlePassAnalyticsResponse xpDistribution(List<@Valid BattlePassAnalyticsResponseXpDistributionInner> xpDistribution) {
    this.xpDistribution = xpDistribution;
    return this;
  }

  public BattlePassAnalyticsResponse addXpDistributionItem(BattlePassAnalyticsResponseXpDistributionInner xpDistributionItem) {
    if (this.xpDistribution == null) {
      this.xpDistribution = new ArrayList<>();
    }
    this.xpDistribution.add(xpDistributionItem);
    return this;
  }

  /**
   * Get xpDistribution
   * @return xpDistribution
   */
  @Valid 
  @Schema(name = "xpDistribution", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("xpDistribution")
  public List<@Valid BattlePassAnalyticsResponseXpDistributionInner> getXpDistribution() {
    return xpDistribution;
  }

  public void setXpDistribution(List<@Valid BattlePassAnalyticsResponseXpDistributionInner> xpDistribution) {
    this.xpDistribution = xpDistribution;
  }

  public BattlePassAnalyticsResponse retentionMetrics(@Nullable BattlePassAnalyticsResponseRetentionMetrics retentionMetrics) {
    this.retentionMetrics = retentionMetrics;
    return this;
  }

  /**
   * Get retentionMetrics
   * @return retentionMetrics
   */
  @Valid 
  @Schema(name = "retentionMetrics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("retentionMetrics")
  public @Nullable BattlePassAnalyticsResponseRetentionMetrics getRetentionMetrics() {
    return retentionMetrics;
  }

  public void setRetentionMetrics(@Nullable BattlePassAnalyticsResponseRetentionMetrics retentionMetrics) {
    this.retentionMetrics = retentionMetrics;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BattlePassAnalyticsResponse battlePassAnalyticsResponse = (BattlePassAnalyticsResponse) o;
    return Objects.equals(this.generatedAt, battlePassAnalyticsResponse.generatedAt) &&
        Objects.equals(this.seasonId, battlePassAnalyticsResponse.seasonId) &&
        Objects.equals(this.range, battlePassAnalyticsResponse.range) &&
        Objects.equals(this.activePlayers, battlePassAnalyticsResponse.activePlayers) &&
        Objects.equals(this.premiumConversionRate, battlePassAnalyticsResponse.premiumConversionRate) &&
        Objects.equals(this.averageLevel, battlePassAnalyticsResponse.averageLevel) &&
        Objects.equals(this.xpDistribution, battlePassAnalyticsResponse.xpDistribution) &&
        Objects.equals(this.retentionMetrics, battlePassAnalyticsResponse.retentionMetrics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(generatedAt, seasonId, range, activePlayers, premiumConversionRate, averageLevel, xpDistribution, retentionMetrics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BattlePassAnalyticsResponse {\n");
    sb.append("    generatedAt: ").append(toIndentedString(generatedAt)).append("\n");
    sb.append("    seasonId: ").append(toIndentedString(seasonId)).append("\n");
    sb.append("    range: ").append(toIndentedString(range)).append("\n");
    sb.append("    activePlayers: ").append(toIndentedString(activePlayers)).append("\n");
    sb.append("    premiumConversionRate: ").append(toIndentedString(premiumConversionRate)).append("\n");
    sb.append("    averageLevel: ").append(toIndentedString(averageLevel)).append("\n");
    sb.append("    xpDistribution: ").append(toIndentedString(xpDistribution)).append("\n");
    sb.append("    retentionMetrics: ").append(toIndentedString(retentionMetrics)).append("\n");
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

