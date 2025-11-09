package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.ContentOverviewQuestsByType;
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
 * ContentOverview
 */


public class ContentOverview {

  private @Nullable String period;

  private @Nullable Integer totalQuests;

  private @Nullable ContentOverviewQuestsByType questsByType;

  private @Nullable Integer totalLocations;

  private @Nullable Integer totalNpcs;

  @Valid
  private List<String> keyEvents = new ArrayList<>();

  private @Nullable Float implementedPercentage;

  public ContentOverview period(@Nullable String period) {
    this.period = period;
    return this;
  }

  /**
   * Get period
   * @return period
   */
  
  @Schema(name = "period", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("period")
  public @Nullable String getPeriod() {
    return period;
  }

  public void setPeriod(@Nullable String period) {
    this.period = period;
  }

  public ContentOverview totalQuests(@Nullable Integer totalQuests) {
    this.totalQuests = totalQuests;
    return this;
  }

  /**
   * Get totalQuests
   * @return totalQuests
   */
  
  @Schema(name = "total_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_quests")
  public @Nullable Integer getTotalQuests() {
    return totalQuests;
  }

  public void setTotalQuests(@Nullable Integer totalQuests) {
    this.totalQuests = totalQuests;
  }

  public ContentOverview questsByType(@Nullable ContentOverviewQuestsByType questsByType) {
    this.questsByType = questsByType;
    return this;
  }

  /**
   * Get questsByType
   * @return questsByType
   */
  @Valid 
  @Schema(name = "quests_by_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quests_by_type")
  public @Nullable ContentOverviewQuestsByType getQuestsByType() {
    return questsByType;
  }

  public void setQuestsByType(@Nullable ContentOverviewQuestsByType questsByType) {
    this.questsByType = questsByType;
  }

  public ContentOverview totalLocations(@Nullable Integer totalLocations) {
    this.totalLocations = totalLocations;
    return this;
  }

  /**
   * Get totalLocations
   * @return totalLocations
   */
  
  @Schema(name = "total_locations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_locations")
  public @Nullable Integer getTotalLocations() {
    return totalLocations;
  }

  public void setTotalLocations(@Nullable Integer totalLocations) {
    this.totalLocations = totalLocations;
  }

  public ContentOverview totalNpcs(@Nullable Integer totalNpcs) {
    this.totalNpcs = totalNpcs;
    return this;
  }

  /**
   * Get totalNpcs
   * @return totalNpcs
   */
  
  @Schema(name = "total_npcs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_npcs")
  public @Nullable Integer getTotalNpcs() {
    return totalNpcs;
  }

  public void setTotalNpcs(@Nullable Integer totalNpcs) {
    this.totalNpcs = totalNpcs;
  }

  public ContentOverview keyEvents(List<String> keyEvents) {
    this.keyEvents = keyEvents;
    return this;
  }

  public ContentOverview addKeyEventsItem(String keyEventsItem) {
    if (this.keyEvents == null) {
      this.keyEvents = new ArrayList<>();
    }
    this.keyEvents.add(keyEventsItem);
    return this;
  }

  /**
   * Get keyEvents
   * @return keyEvents
   */
  
  @Schema(name = "key_events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("key_events")
  public List<String> getKeyEvents() {
    return keyEvents;
  }

  public void setKeyEvents(List<String> keyEvents) {
    this.keyEvents = keyEvents;
  }

  public ContentOverview implementedPercentage(@Nullable Float implementedPercentage) {
    this.implementedPercentage = implementedPercentage;
    return this;
  }

  /**
   * Get implementedPercentage
   * @return implementedPercentage
   */
  
  @Schema(name = "implemented_percentage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implemented_percentage")
  public @Nullable Float getImplementedPercentage() {
    return implementedPercentage;
  }

  public void setImplementedPercentage(@Nullable Float implementedPercentage) {
    this.implementedPercentage = implementedPercentage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContentOverview contentOverview = (ContentOverview) o;
    return Objects.equals(this.period, contentOverview.period) &&
        Objects.equals(this.totalQuests, contentOverview.totalQuests) &&
        Objects.equals(this.questsByType, contentOverview.questsByType) &&
        Objects.equals(this.totalLocations, contentOverview.totalLocations) &&
        Objects.equals(this.totalNpcs, contentOverview.totalNpcs) &&
        Objects.equals(this.keyEvents, contentOverview.keyEvents) &&
        Objects.equals(this.implementedPercentage, contentOverview.implementedPercentage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(period, totalQuests, questsByType, totalLocations, totalNpcs, keyEvents, implementedPercentage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContentOverview {\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
    sb.append("    totalQuests: ").append(toIndentedString(totalQuests)).append("\n");
    sb.append("    questsByType: ").append(toIndentedString(questsByType)).append("\n");
    sb.append("    totalLocations: ").append(toIndentedString(totalLocations)).append("\n");
    sb.append("    totalNpcs: ").append(toIndentedString(totalNpcs)).append("\n");
    sb.append("    keyEvents: ").append(toIndentedString(keyEvents)).append("\n");
    sb.append("    implementedPercentage: ").append(toIndentedString(implementedPercentage)).append("\n");
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

