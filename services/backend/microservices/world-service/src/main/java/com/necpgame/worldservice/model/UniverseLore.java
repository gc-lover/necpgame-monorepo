package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.UniverseLoreSimulationLore;
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
 * UniverseLore
 */


public class UniverseLore {

  private @Nullable String title;

  private @Nullable String setting;

  private @Nullable String timePeriod;

  @Valid
  private List<String> keyEvents = new ArrayList<>();

  private @Nullable UniverseLoreSimulationLore simulationLore;

  private @Nullable Integer majorFactionsCount;

  private @Nullable Integer locationsCount;

  public UniverseLore title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", example = "Cyberpunk Universe", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public UniverseLore setting(@Nullable String setting) {
    this.setting = setting;
    return this;
  }

  /**
   * Get setting
   * @return setting
   */
  
  @Schema(name = "setting", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("setting")
  public @Nullable String getSetting() {
    return setting;
  }

  public void setSetting(@Nullable String setting) {
    this.setting = setting;
  }

  public UniverseLore timePeriod(@Nullable String timePeriod) {
    this.timePeriod = timePeriod;
    return this;
  }

  /**
   * Get timePeriod
   * @return timePeriod
   */
  
  @Schema(name = "time_period", example = "2020-2093", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_period")
  public @Nullable String getTimePeriod() {
    return timePeriod;
  }

  public void setTimePeriod(@Nullable String timePeriod) {
    this.timePeriod = timePeriod;
  }

  public UniverseLore keyEvents(List<String> keyEvents) {
    this.keyEvents = keyEvents;
    return this;
  }

  public UniverseLore addKeyEventsItem(String keyEventsItem) {
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

  public UniverseLore simulationLore(@Nullable UniverseLoreSimulationLore simulationLore) {
    this.simulationLore = simulationLore;
    return this;
  }

  /**
   * Get simulationLore
   * @return simulationLore
   */
  @Valid 
  @Schema(name = "simulation_lore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("simulation_lore")
  public @Nullable UniverseLoreSimulationLore getSimulationLore() {
    return simulationLore;
  }

  public void setSimulationLore(@Nullable UniverseLoreSimulationLore simulationLore) {
    this.simulationLore = simulationLore;
  }

  public UniverseLore majorFactionsCount(@Nullable Integer majorFactionsCount) {
    this.majorFactionsCount = majorFactionsCount;
    return this;
  }

  /**
   * Get majorFactionsCount
   * @return majorFactionsCount
   */
  
  @Schema(name = "major_factions_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("major_factions_count")
  public @Nullable Integer getMajorFactionsCount() {
    return majorFactionsCount;
  }

  public void setMajorFactionsCount(@Nullable Integer majorFactionsCount) {
    this.majorFactionsCount = majorFactionsCount;
  }

  public UniverseLore locationsCount(@Nullable Integer locationsCount) {
    this.locationsCount = locationsCount;
    return this;
  }

  /**
   * Get locationsCount
   * @return locationsCount
   */
  
  @Schema(name = "locations_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locations_count")
  public @Nullable Integer getLocationsCount() {
    return locationsCount;
  }

  public void setLocationsCount(@Nullable Integer locationsCount) {
    this.locationsCount = locationsCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UniverseLore universeLore = (UniverseLore) o;
    return Objects.equals(this.title, universeLore.title) &&
        Objects.equals(this.setting, universeLore.setting) &&
        Objects.equals(this.timePeriod, universeLore.timePeriod) &&
        Objects.equals(this.keyEvents, universeLore.keyEvents) &&
        Objects.equals(this.simulationLore, universeLore.simulationLore) &&
        Objects.equals(this.majorFactionsCount, universeLore.majorFactionsCount) &&
        Objects.equals(this.locationsCount, universeLore.locationsCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, setting, timePeriod, keyEvents, simulationLore, majorFactionsCount, locationsCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UniverseLore {\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    setting: ").append(toIndentedString(setting)).append("\n");
    sb.append("    timePeriod: ").append(toIndentedString(timePeriod)).append("\n");
    sb.append("    keyEvents: ").append(toIndentedString(keyEvents)).append("\n");
    sb.append("    simulationLore: ").append(toIndentedString(simulationLore)).append("\n");
    sb.append("    majorFactionsCount: ").append(toIndentedString(majorFactionsCount)).append("\n");
    sb.append("    locationsCount: ").append(toIndentedString(locationsCount)).append("\n");
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

