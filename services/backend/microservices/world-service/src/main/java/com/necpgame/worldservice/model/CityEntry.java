package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.PopulationRecord;
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
 * CityEntry
 */


public class CityEntry {

  private @Nullable String cityId;

  private @Nullable String name;

  private @Nullable String region;

  @Valid
  private List<@Valid PopulationRecord> populationHistory = new ArrayList<>();

  @Valid
  private List<String> keyDistricts = new ArrayList<>();

  @Valid
  private List<String> controllingFactionsHistory = new ArrayList<>();

  @Valid
  private List<String> majorEvents = new ArrayList<>();

  public CityEntry cityId(@Nullable String cityId) {
    this.cityId = cityId;
    return this;
  }

  /**
   * Get cityId
   * @return cityId
   */
  
  @Schema(name = "city_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("city_id")
  public @Nullable String getCityId() {
    return cityId;
  }

  public void setCityId(@Nullable String cityId) {
    this.cityId = cityId;
  }

  public CityEntry name(@Nullable String name) {
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

  public CityEntry region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  public CityEntry populationHistory(List<@Valid PopulationRecord> populationHistory) {
    this.populationHistory = populationHistory;
    return this;
  }

  public CityEntry addPopulationHistoryItem(PopulationRecord populationHistoryItem) {
    if (this.populationHistory == null) {
      this.populationHistory = new ArrayList<>();
    }
    this.populationHistory.add(populationHistoryItem);
    return this;
  }

  /**
   * Get populationHistory
   * @return populationHistory
   */
  @Valid 
  @Schema(name = "population_history", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("population_history")
  public List<@Valid PopulationRecord> getPopulationHistory() {
    return populationHistory;
  }

  public void setPopulationHistory(List<@Valid PopulationRecord> populationHistory) {
    this.populationHistory = populationHistory;
  }

  public CityEntry keyDistricts(List<String> keyDistricts) {
    this.keyDistricts = keyDistricts;
    return this;
  }

  public CityEntry addKeyDistrictsItem(String keyDistrictsItem) {
    if (this.keyDistricts == null) {
      this.keyDistricts = new ArrayList<>();
    }
    this.keyDistricts.add(keyDistrictsItem);
    return this;
  }

  /**
   * Get keyDistricts
   * @return keyDistricts
   */
  
  @Schema(name = "key_districts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("key_districts")
  public List<String> getKeyDistricts() {
    return keyDistricts;
  }

  public void setKeyDistricts(List<String> keyDistricts) {
    this.keyDistricts = keyDistricts;
  }

  public CityEntry controllingFactionsHistory(List<String> controllingFactionsHistory) {
    this.controllingFactionsHistory = controllingFactionsHistory;
    return this;
  }

  public CityEntry addControllingFactionsHistoryItem(String controllingFactionsHistoryItem) {
    if (this.controllingFactionsHistory == null) {
      this.controllingFactionsHistory = new ArrayList<>();
    }
    this.controllingFactionsHistory.add(controllingFactionsHistoryItem);
    return this;
  }

  /**
   * Get controllingFactionsHistory
   * @return controllingFactionsHistory
   */
  
  @Schema(name = "controlling_factions_history", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("controlling_factions_history")
  public List<String> getControllingFactionsHistory() {
    return controllingFactionsHistory;
  }

  public void setControllingFactionsHistory(List<String> controllingFactionsHistory) {
    this.controllingFactionsHistory = controllingFactionsHistory;
  }

  public CityEntry majorEvents(List<String> majorEvents) {
    this.majorEvents = majorEvents;
    return this;
  }

  public CityEntry addMajorEventsItem(String majorEventsItem) {
    if (this.majorEvents == null) {
      this.majorEvents = new ArrayList<>();
    }
    this.majorEvents.add(majorEventsItem);
    return this;
  }

  /**
   * Get majorEvents
   * @return majorEvents
   */
  
  @Schema(name = "major_events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("major_events")
  public List<String> getMajorEvents() {
    return majorEvents;
  }

  public void setMajorEvents(List<String> majorEvents) {
    this.majorEvents = majorEvents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CityEntry cityEntry = (CityEntry) o;
    return Objects.equals(this.cityId, cityEntry.cityId) &&
        Objects.equals(this.name, cityEntry.name) &&
        Objects.equals(this.region, cityEntry.region) &&
        Objects.equals(this.populationHistory, cityEntry.populationHistory) &&
        Objects.equals(this.keyDistricts, cityEntry.keyDistricts) &&
        Objects.equals(this.controllingFactionsHistory, cityEntry.controllingFactionsHistory) &&
        Objects.equals(this.majorEvents, cityEntry.majorEvents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cityId, name, region, populationHistory, keyDistricts, controllingFactionsHistory, majorEvents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CityEntry {\n");
    sb.append("    cityId: ").append(toIndentedString(cityId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    populationHistory: ").append(toIndentedString(populationHistory)).append("\n");
    sb.append("    keyDistricts: ").append(toIndentedString(keyDistricts)).append("\n");
    sb.append("    controllingFactionsHistory: ").append(toIndentedString(controllingFactionsHistory)).append("\n");
    sb.append("    majorEvents: ").append(toIndentedString(majorEvents)).append("\n");
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

