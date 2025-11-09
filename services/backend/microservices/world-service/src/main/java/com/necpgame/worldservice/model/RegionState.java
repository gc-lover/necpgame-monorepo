package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
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
 * RegionState
 */


public class RegionState {

  private @Nullable String regionId;

  private @Nullable String name;

  private @Nullable String dominantFaction;

  private @Nullable BigDecimal stability;

  private @Nullable BigDecimal prosperity;

  private @Nullable BigDecimal crimeRate;

  private @Nullable BigDecimal playerActivity;

  @Valid
  private List<String> activeEvents = new ArrayList<>();

  public RegionState regionId(@Nullable String regionId) {
    this.regionId = regionId;
    return this;
  }

  /**
   * Get regionId
   * @return regionId
   */
  
  @Schema(name = "region_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region_id")
  public @Nullable String getRegionId() {
    return regionId;
  }

  public void setRegionId(@Nullable String regionId) {
    this.regionId = regionId;
  }

  public RegionState name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public RegionState dominantFaction(@Nullable String dominantFaction) {
    this.dominantFaction = dominantFaction;
    return this;
  }

  /**
   * Get dominantFaction
   * @return dominantFaction
   */
  
  @Schema(name = "dominant_faction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dominant_faction")
  public @Nullable String getDominantFaction() {
    return dominantFaction;
  }

  public void setDominantFaction(@Nullable String dominantFaction) {
    this.dominantFaction = dominantFaction;
  }

  public RegionState stability(@Nullable BigDecimal stability) {
    this.stability = stability;
    return this;
  }

  /**
   * Стабильность региона (0-100)
   * @return stability
   */
  @Valid 
  @Schema(name = "stability", description = "Стабильность региона (0-100)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stability")
  public @Nullable BigDecimal getStability() {
    return stability;
  }

  public void setStability(@Nullable BigDecimal stability) {
    this.stability = stability;
  }

  public RegionState prosperity(@Nullable BigDecimal prosperity) {
    this.prosperity = prosperity;
    return this;
  }

  /**
   * Get prosperity
   * @return prosperity
   */
  @Valid 
  @Schema(name = "prosperity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("prosperity")
  public @Nullable BigDecimal getProsperity() {
    return prosperity;
  }

  public void setProsperity(@Nullable BigDecimal prosperity) {
    this.prosperity = prosperity;
  }

  public RegionState crimeRate(@Nullable BigDecimal crimeRate) {
    this.crimeRate = crimeRate;
    return this;
  }

  /**
   * Get crimeRate
   * @return crimeRate
   */
  @Valid 
  @Schema(name = "crime_rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("crime_rate")
  public @Nullable BigDecimal getCrimeRate() {
    return crimeRate;
  }

  public void setCrimeRate(@Nullable BigDecimal crimeRate) {
    this.crimeRate = crimeRate;
  }

  public RegionState playerActivity(@Nullable BigDecimal playerActivity) {
    this.playerActivity = playerActivity;
    return this;
  }

  /**
   * Get playerActivity
   * @return playerActivity
   */
  @Valid 
  @Schema(name = "player_activity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_activity")
  public @Nullable BigDecimal getPlayerActivity() {
    return playerActivity;
  }

  public void setPlayerActivity(@Nullable BigDecimal playerActivity) {
    this.playerActivity = playerActivity;
  }

  public RegionState activeEvents(List<String> activeEvents) {
    this.activeEvents = activeEvents;
    return this;
  }

  public RegionState addActiveEventsItem(String activeEventsItem) {
    if (this.activeEvents == null) {
      this.activeEvents = new ArrayList<>();
    }
    this.activeEvents.add(activeEventsItem);
    return this;
  }

  /**
   * Get activeEvents
   * @return activeEvents
   */
  
  @Schema(name = "active_events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_events")
  public List<String> getActiveEvents() {
    return activeEvents;
  }

  public void setActiveEvents(List<String> activeEvents) {
    this.activeEvents = activeEvents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegionState regionState = (RegionState) o;
    return Objects.equals(this.regionId, regionState.regionId) &&
        Objects.equals(this.name, regionState.name) &&
        Objects.equals(this.dominantFaction, regionState.dominantFaction) &&
        Objects.equals(this.stability, regionState.stability) &&
        Objects.equals(this.prosperity, regionState.prosperity) &&
        Objects.equals(this.crimeRate, regionState.crimeRate) &&
        Objects.equals(this.playerActivity, regionState.playerActivity) &&
        Objects.equals(this.activeEvents, regionState.activeEvents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(regionId, name, dominantFaction, stability, prosperity, crimeRate, playerActivity, activeEvents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegionState {\n");
    sb.append("    regionId: ").append(toIndentedString(regionId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    dominantFaction: ").append(toIndentedString(dominantFaction)).append("\n");
    sb.append("    stability: ").append(toIndentedString(stability)).append("\n");
    sb.append("    prosperity: ").append(toIndentedString(prosperity)).append("\n");
    sb.append("    crimeRate: ").append(toIndentedString(crimeRate)).append("\n");
    sb.append("    playerActivity: ").append(toIndentedString(playerActivity)).append("\n");
    sb.append("    activeEvents: ").append(toIndentedString(activeEvents)).append("\n");
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

