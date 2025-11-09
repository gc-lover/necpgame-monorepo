package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.lootservice.model.PityTimerState;
import com.necpgame.lootservice.model.SimulationBucket;
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
 * LootStatsResponse
 */


public class LootStatsResponse {

  @Valid
  private List<@Valid SimulationBucket> dropRates = new ArrayList<>();

  @Valid
  private List<@Valid PityTimerState> pityHeatmap = new ArrayList<>();

  private @Nullable Integer rareDropNotifications;

  private @Nullable Float averageCompletionTime;

  public LootStatsResponse dropRates(List<@Valid SimulationBucket> dropRates) {
    this.dropRates = dropRates;
    return this;
  }

  public LootStatsResponse addDropRatesItem(SimulationBucket dropRatesItem) {
    if (this.dropRates == null) {
      this.dropRates = new ArrayList<>();
    }
    this.dropRates.add(dropRatesItem);
    return this;
  }

  /**
   * Get dropRates
   * @return dropRates
   */
  @Valid 
  @Schema(name = "dropRates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dropRates")
  public List<@Valid SimulationBucket> getDropRates() {
    return dropRates;
  }

  public void setDropRates(List<@Valid SimulationBucket> dropRates) {
    this.dropRates = dropRates;
  }

  public LootStatsResponse pityHeatmap(List<@Valid PityTimerState> pityHeatmap) {
    this.pityHeatmap = pityHeatmap;
    return this;
  }

  public LootStatsResponse addPityHeatmapItem(PityTimerState pityHeatmapItem) {
    if (this.pityHeatmap == null) {
      this.pityHeatmap = new ArrayList<>();
    }
    this.pityHeatmap.add(pityHeatmapItem);
    return this;
  }

  /**
   * Get pityHeatmap
   * @return pityHeatmap
   */
  @Valid 
  @Schema(name = "pityHeatmap", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pityHeatmap")
  public List<@Valid PityTimerState> getPityHeatmap() {
    return pityHeatmap;
  }

  public void setPityHeatmap(List<@Valid PityTimerState> pityHeatmap) {
    this.pityHeatmap = pityHeatmap;
  }

  public LootStatsResponse rareDropNotifications(@Nullable Integer rareDropNotifications) {
    this.rareDropNotifications = rareDropNotifications;
    return this;
  }

  /**
   * Get rareDropNotifications
   * @return rareDropNotifications
   */
  
  @Schema(name = "rareDropNotifications", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rareDropNotifications")
  public @Nullable Integer getRareDropNotifications() {
    return rareDropNotifications;
  }

  public void setRareDropNotifications(@Nullable Integer rareDropNotifications) {
    this.rareDropNotifications = rareDropNotifications;
  }

  public LootStatsResponse averageCompletionTime(@Nullable Float averageCompletionTime) {
    this.averageCompletionTime = averageCompletionTime;
    return this;
  }

  /**
   * Get averageCompletionTime
   * @return averageCompletionTime
   */
  
  @Schema(name = "averageCompletionTime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("averageCompletionTime")
  public @Nullable Float getAverageCompletionTime() {
    return averageCompletionTime;
  }

  public void setAverageCompletionTime(@Nullable Float averageCompletionTime) {
    this.averageCompletionTime = averageCompletionTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootStatsResponse lootStatsResponse = (LootStatsResponse) o;
    return Objects.equals(this.dropRates, lootStatsResponse.dropRates) &&
        Objects.equals(this.pityHeatmap, lootStatsResponse.pityHeatmap) &&
        Objects.equals(this.rareDropNotifications, lootStatsResponse.rareDropNotifications) &&
        Objects.equals(this.averageCompletionTime, lootStatsResponse.averageCompletionTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dropRates, pityHeatmap, rareDropNotifications, averageCompletionTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootStatsResponse {\n");
    sb.append("    dropRates: ").append(toIndentedString(dropRates)).append("\n");
    sb.append("    pityHeatmap: ").append(toIndentedString(pityHeatmap)).append("\n");
    sb.append("    rareDropNotifications: ").append(toIndentedString(rareDropNotifications)).append("\n");
    sb.append("    averageCompletionTime: ").append(toIndentedString(averageCompletionTime)).append("\n");
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

