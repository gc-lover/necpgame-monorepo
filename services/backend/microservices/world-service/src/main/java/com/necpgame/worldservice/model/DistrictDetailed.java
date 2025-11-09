package com.necpgame.worldservice.model;

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
 * DistrictDetailed
 */


public class DistrictDetailed {

  private @Nullable String districtId;

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable String dangerLevel;

  private @Nullable String controllingFaction;

  private @Nullable Integer population;

  @Valid
  private List<String> keyLocations = new ArrayList<>();

  @Valid
  private List<String> timelineChanges = new ArrayList<>();

  public DistrictDetailed districtId(@Nullable String districtId) {
    this.districtId = districtId;
    return this;
  }

  /**
   * Get districtId
   * @return districtId
   */
  
  @Schema(name = "district_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("district_id")
  public @Nullable String getDistrictId() {
    return districtId;
  }

  public void setDistrictId(@Nullable String districtId) {
    this.districtId = districtId;
  }

  public DistrictDetailed name(@Nullable String name) {
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

  public DistrictDetailed description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public DistrictDetailed dangerLevel(@Nullable String dangerLevel) {
    this.dangerLevel = dangerLevel;
    return this;
  }

  /**
   * Get dangerLevel
   * @return dangerLevel
   */
  
  @Schema(name = "danger_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("danger_level")
  public @Nullable String getDangerLevel() {
    return dangerLevel;
  }

  public void setDangerLevel(@Nullable String dangerLevel) {
    this.dangerLevel = dangerLevel;
  }

  public DistrictDetailed controllingFaction(@Nullable String controllingFaction) {
    this.controllingFaction = controllingFaction;
    return this;
  }

  /**
   * Get controllingFaction
   * @return controllingFaction
   */
  
  @Schema(name = "controlling_faction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("controlling_faction")
  public @Nullable String getControllingFaction() {
    return controllingFaction;
  }

  public void setControllingFaction(@Nullable String controllingFaction) {
    this.controllingFaction = controllingFaction;
  }

  public DistrictDetailed population(@Nullable Integer population) {
    this.population = population;
    return this;
  }

  /**
   * Get population
   * @return population
   */
  
  @Schema(name = "population", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("population")
  public @Nullable Integer getPopulation() {
    return population;
  }

  public void setPopulation(@Nullable Integer population) {
    this.population = population;
  }

  public DistrictDetailed keyLocations(List<String> keyLocations) {
    this.keyLocations = keyLocations;
    return this;
  }

  public DistrictDetailed addKeyLocationsItem(String keyLocationsItem) {
    if (this.keyLocations == null) {
      this.keyLocations = new ArrayList<>();
    }
    this.keyLocations.add(keyLocationsItem);
    return this;
  }

  /**
   * Get keyLocations
   * @return keyLocations
   */
  
  @Schema(name = "key_locations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("key_locations")
  public List<String> getKeyLocations() {
    return keyLocations;
  }

  public void setKeyLocations(List<String> keyLocations) {
    this.keyLocations = keyLocations;
  }

  public DistrictDetailed timelineChanges(List<String> timelineChanges) {
    this.timelineChanges = timelineChanges;
    return this;
  }

  public DistrictDetailed addTimelineChangesItem(String timelineChangesItem) {
    if (this.timelineChanges == null) {
      this.timelineChanges = new ArrayList<>();
    }
    this.timelineChanges.add(timelineChangesItem);
    return this;
  }

  /**
   * Get timelineChanges
   * @return timelineChanges
   */
  
  @Schema(name = "timeline_changes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeline_changes")
  public List<String> getTimelineChanges() {
    return timelineChanges;
  }

  public void setTimelineChanges(List<String> timelineChanges) {
    this.timelineChanges = timelineChanges;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DistrictDetailed districtDetailed = (DistrictDetailed) o;
    return Objects.equals(this.districtId, districtDetailed.districtId) &&
        Objects.equals(this.name, districtDetailed.name) &&
        Objects.equals(this.description, districtDetailed.description) &&
        Objects.equals(this.dangerLevel, districtDetailed.dangerLevel) &&
        Objects.equals(this.controllingFaction, districtDetailed.controllingFaction) &&
        Objects.equals(this.population, districtDetailed.population) &&
        Objects.equals(this.keyLocations, districtDetailed.keyLocations) &&
        Objects.equals(this.timelineChanges, districtDetailed.timelineChanges);
  }

  @Override
  public int hashCode() {
    return Objects.hash(districtId, name, description, dangerLevel, controllingFaction, population, keyLocations, timelineChanges);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DistrictDetailed {\n");
    sb.append("    districtId: ").append(toIndentedString(districtId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    dangerLevel: ").append(toIndentedString(dangerLevel)).append("\n");
    sb.append("    controllingFaction: ").append(toIndentedString(controllingFaction)).append("\n");
    sb.append("    population: ").append(toIndentedString(population)).append("\n");
    sb.append("    keyLocations: ").append(toIndentedString(keyLocations)).append("\n");
    sb.append("    timelineChanges: ").append(toIndentedString(timelineChanges)).append("\n");
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

