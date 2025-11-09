package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CompanionAnalyticsResponseMissionPerformance
 */

@JsonTypeName("CompanionAnalyticsResponse_missionPerformance")

public class CompanionAnalyticsResponseMissionPerformance {

  private @Nullable BigDecimal successRate;

  private @Nullable BigDecimal averageDuration;

  private @Nullable BigDecimal lootValueMean;

  private @Nullable BigDecimal missionsPerDay;

  public CompanionAnalyticsResponseMissionPerformance successRate(@Nullable BigDecimal successRate) {
    this.successRate = successRate;
    return this;
  }

  /**
   * Get successRate
   * @return successRate
   */
  @Valid 
  @Schema(name = "successRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("successRate")
  public @Nullable BigDecimal getSuccessRate() {
    return successRate;
  }

  public void setSuccessRate(@Nullable BigDecimal successRate) {
    this.successRate = successRate;
  }

  public CompanionAnalyticsResponseMissionPerformance averageDuration(@Nullable BigDecimal averageDuration) {
    this.averageDuration = averageDuration;
    return this;
  }

  /**
   * Get averageDuration
   * @return averageDuration
   */
  @Valid 
  @Schema(name = "averageDuration", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("averageDuration")
  public @Nullable BigDecimal getAverageDuration() {
    return averageDuration;
  }

  public void setAverageDuration(@Nullable BigDecimal averageDuration) {
    this.averageDuration = averageDuration;
  }

  public CompanionAnalyticsResponseMissionPerformance lootValueMean(@Nullable BigDecimal lootValueMean) {
    this.lootValueMean = lootValueMean;
    return this;
  }

  /**
   * Get lootValueMean
   * @return lootValueMean
   */
  @Valid 
  @Schema(name = "lootValueMean", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lootValueMean")
  public @Nullable BigDecimal getLootValueMean() {
    return lootValueMean;
  }

  public void setLootValueMean(@Nullable BigDecimal lootValueMean) {
    this.lootValueMean = lootValueMean;
  }

  public CompanionAnalyticsResponseMissionPerformance missionsPerDay(@Nullable BigDecimal missionsPerDay) {
    this.missionsPerDay = missionsPerDay;
    return this;
  }

  /**
   * Get missionsPerDay
   * @return missionsPerDay
   */
  @Valid 
  @Schema(name = "missionsPerDay", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("missionsPerDay")
  public @Nullable BigDecimal getMissionsPerDay() {
    return missionsPerDay;
  }

  public void setMissionsPerDay(@Nullable BigDecimal missionsPerDay) {
    this.missionsPerDay = missionsPerDay;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompanionAnalyticsResponseMissionPerformance companionAnalyticsResponseMissionPerformance = (CompanionAnalyticsResponseMissionPerformance) o;
    return Objects.equals(this.successRate, companionAnalyticsResponseMissionPerformance.successRate) &&
        Objects.equals(this.averageDuration, companionAnalyticsResponseMissionPerformance.averageDuration) &&
        Objects.equals(this.lootValueMean, companionAnalyticsResponseMissionPerformance.lootValueMean) &&
        Objects.equals(this.missionsPerDay, companionAnalyticsResponseMissionPerformance.missionsPerDay);
  }

  @Override
  public int hashCode() {
    return Objects.hash(successRate, averageDuration, lootValueMean, missionsPerDay);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompanionAnalyticsResponseMissionPerformance {\n");
    sb.append("    successRate: ").append(toIndentedString(successRate)).append("\n");
    sb.append("    averageDuration: ").append(toIndentedString(averageDuration)).append("\n");
    sb.append("    lootValueMean: ").append(toIndentedString(lootValueMean)).append("\n");
    sb.append("    missionsPerDay: ").append(toIndentedString(missionsPerDay)).append("\n");
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

