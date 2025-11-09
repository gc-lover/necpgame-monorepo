package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * CompanionUsageStat
 */


public class CompanionUsageStat {

  private @Nullable String type;

  private @Nullable Integer activeCount;

  private @Nullable BigDecimal avgBondingLevel;

  private @Nullable BigDecimal retentionDay7;

  private @Nullable BigDecimal winContribution;

  public CompanionUsageStat type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public CompanionUsageStat activeCount(@Nullable Integer activeCount) {
    this.activeCount = activeCount;
    return this;
  }

  /**
   * Get activeCount
   * @return activeCount
   */
  
  @Schema(name = "activeCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activeCount")
  public @Nullable Integer getActiveCount() {
    return activeCount;
  }

  public void setActiveCount(@Nullable Integer activeCount) {
    this.activeCount = activeCount;
  }

  public CompanionUsageStat avgBondingLevel(@Nullable BigDecimal avgBondingLevel) {
    this.avgBondingLevel = avgBondingLevel;
    return this;
  }

  /**
   * Get avgBondingLevel
   * @return avgBondingLevel
   */
  @Valid 
  @Schema(name = "avgBondingLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("avgBondingLevel")
  public @Nullable BigDecimal getAvgBondingLevel() {
    return avgBondingLevel;
  }

  public void setAvgBondingLevel(@Nullable BigDecimal avgBondingLevel) {
    this.avgBondingLevel = avgBondingLevel;
  }

  public CompanionUsageStat retentionDay7(@Nullable BigDecimal retentionDay7) {
    this.retentionDay7 = retentionDay7;
    return this;
  }

  /**
   * Get retentionDay7
   * @return retentionDay7
   */
  @Valid 
  @Schema(name = "retentionDay7", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("retentionDay7")
  public @Nullable BigDecimal getRetentionDay7() {
    return retentionDay7;
  }

  public void setRetentionDay7(@Nullable BigDecimal retentionDay7) {
    this.retentionDay7 = retentionDay7;
  }

  public CompanionUsageStat winContribution(@Nullable BigDecimal winContribution) {
    this.winContribution = winContribution;
    return this;
  }

  /**
   * Get winContribution
   * @return winContribution
   */
  @Valid 
  @Schema(name = "winContribution", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("winContribution")
  public @Nullable BigDecimal getWinContribution() {
    return winContribution;
  }

  public void setWinContribution(@Nullable BigDecimal winContribution) {
    this.winContribution = winContribution;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompanionUsageStat companionUsageStat = (CompanionUsageStat) o;
    return Objects.equals(this.type, companionUsageStat.type) &&
        Objects.equals(this.activeCount, companionUsageStat.activeCount) &&
        Objects.equals(this.avgBondingLevel, companionUsageStat.avgBondingLevel) &&
        Objects.equals(this.retentionDay7, companionUsageStat.retentionDay7) &&
        Objects.equals(this.winContribution, companionUsageStat.winContribution);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, activeCount, avgBondingLevel, retentionDay7, winContribution);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompanionUsageStat {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    activeCount: ").append(toIndentedString(activeCount)).append("\n");
    sb.append("    avgBondingLevel: ").append(toIndentedString(avgBondingLevel)).append("\n");
    sb.append("    retentionDay7: ").append(toIndentedString(retentionDay7)).append("\n");
    sb.append("    winContribution: ").append(toIndentedString(winContribution)).append("\n");
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

