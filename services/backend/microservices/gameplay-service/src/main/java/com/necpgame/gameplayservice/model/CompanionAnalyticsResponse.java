package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.AbilityUsageStat;
import com.necpgame.gameplayservice.model.CompanionAnalyticsResponseFilters;
import com.necpgame.gameplayservice.model.CompanionAnalyticsResponseMissionPerformance;
import com.necpgame.gameplayservice.model.CompanionUsageStat;
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
 * CompanionAnalyticsResponse
 */


public class CompanionAnalyticsResponse {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime snapshotAt;

  private @Nullable CompanionAnalyticsResponseFilters filters;

  @Valid
  private List<@Valid CompanionUsageStat> usageStats = new ArrayList<>();

  @Valid
  private List<@Valid AbilityUsageStat> abilityUsage = new ArrayList<>();

  private @Nullable CompanionAnalyticsResponseMissionPerformance missionPerformance;

  public CompanionAnalyticsResponse snapshotAt(@Nullable OffsetDateTime snapshotAt) {
    this.snapshotAt = snapshotAt;
    return this;
  }

  /**
   * Get snapshotAt
   * @return snapshotAt
   */
  @Valid 
  @Schema(name = "snapshotAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("snapshotAt")
  public @Nullable OffsetDateTime getSnapshotAt() {
    return snapshotAt;
  }

  public void setSnapshotAt(@Nullable OffsetDateTime snapshotAt) {
    this.snapshotAt = snapshotAt;
  }

  public CompanionAnalyticsResponse filters(@Nullable CompanionAnalyticsResponseFilters filters) {
    this.filters = filters;
    return this;
  }

  /**
   * Get filters
   * @return filters
   */
  @Valid 
  @Schema(name = "filters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("filters")
  public @Nullable CompanionAnalyticsResponseFilters getFilters() {
    return filters;
  }

  public void setFilters(@Nullable CompanionAnalyticsResponseFilters filters) {
    this.filters = filters;
  }

  public CompanionAnalyticsResponse usageStats(List<@Valid CompanionUsageStat> usageStats) {
    this.usageStats = usageStats;
    return this;
  }

  public CompanionAnalyticsResponse addUsageStatsItem(CompanionUsageStat usageStatsItem) {
    if (this.usageStats == null) {
      this.usageStats = new ArrayList<>();
    }
    this.usageStats.add(usageStatsItem);
    return this;
  }

  /**
   * Get usageStats
   * @return usageStats
   */
  @Valid 
  @Schema(name = "usageStats", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("usageStats")
  public List<@Valid CompanionUsageStat> getUsageStats() {
    return usageStats;
  }

  public void setUsageStats(List<@Valid CompanionUsageStat> usageStats) {
    this.usageStats = usageStats;
  }

  public CompanionAnalyticsResponse abilityUsage(List<@Valid AbilityUsageStat> abilityUsage) {
    this.abilityUsage = abilityUsage;
    return this;
  }

  public CompanionAnalyticsResponse addAbilityUsageItem(AbilityUsageStat abilityUsageItem) {
    if (this.abilityUsage == null) {
      this.abilityUsage = new ArrayList<>();
    }
    this.abilityUsage.add(abilityUsageItem);
    return this;
  }

  /**
   * Get abilityUsage
   * @return abilityUsage
   */
  @Valid 
  @Schema(name = "abilityUsage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("abilityUsage")
  public List<@Valid AbilityUsageStat> getAbilityUsage() {
    return abilityUsage;
  }

  public void setAbilityUsage(List<@Valid AbilityUsageStat> abilityUsage) {
    this.abilityUsage = abilityUsage;
  }

  public CompanionAnalyticsResponse missionPerformance(@Nullable CompanionAnalyticsResponseMissionPerformance missionPerformance) {
    this.missionPerformance = missionPerformance;
    return this;
  }

  /**
   * Get missionPerformance
   * @return missionPerformance
   */
  @Valid 
  @Schema(name = "missionPerformance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("missionPerformance")
  public @Nullable CompanionAnalyticsResponseMissionPerformance getMissionPerformance() {
    return missionPerformance;
  }

  public void setMissionPerformance(@Nullable CompanionAnalyticsResponseMissionPerformance missionPerformance) {
    this.missionPerformance = missionPerformance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompanionAnalyticsResponse companionAnalyticsResponse = (CompanionAnalyticsResponse) o;
    return Objects.equals(this.snapshotAt, companionAnalyticsResponse.snapshotAt) &&
        Objects.equals(this.filters, companionAnalyticsResponse.filters) &&
        Objects.equals(this.usageStats, companionAnalyticsResponse.usageStats) &&
        Objects.equals(this.abilityUsage, companionAnalyticsResponse.abilityUsage) &&
        Objects.equals(this.missionPerformance, companionAnalyticsResponse.missionPerformance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(snapshotAt, filters, usageStats, abilityUsage, missionPerformance);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompanionAnalyticsResponse {\n");
    sb.append("    snapshotAt: ").append(toIndentedString(snapshotAt)).append("\n");
    sb.append("    filters: ").append(toIndentedString(filters)).append("\n");
    sb.append("    usageStats: ").append(toIndentedString(usageStats)).append("\n");
    sb.append("    abilityUsage: ").append(toIndentedString(abilityUsage)).append("\n");
    sb.append("    missionPerformance: ").append(toIndentedString(missionPerformance)).append("\n");
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

