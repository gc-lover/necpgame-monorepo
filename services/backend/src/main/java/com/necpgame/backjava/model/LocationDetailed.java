package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.LocationDetailedAllOfDistricts;
import com.necpgame.backjava.model.LocationDetailedAllOfEconomy;
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
 * LocationDetailed
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LocationDetailed {

  private @Nullable String locationId;

  private @Nullable String name;

  private @Nullable String region;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    CITY("CITY"),
    
    BADLANDS("BADLANDS"),
    
    COMBAT_ZONE("COMBAT_ZONE"),
    
    CORPO_ZONE("CORPO_ZONE");

    private final String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private @Nullable Integer population;

  private @Nullable String dangerLevel;

  private @Nullable String descriptionShort;

  private @Nullable String fullDescription;

  private @Nullable String history;

  @Valid
  private List<@Valid LocationDetailedAllOfDistricts> districts = new ArrayList<>();

  @Valid
  private List<String> controllingFactions = new ArrayList<>();

  @Valid
  private List<String> pointsOfInterest = new ArrayList<>();

  private @Nullable LocationDetailedAllOfEconomy economy;

  public LocationDetailed locationId(@Nullable String locationId) {
    this.locationId = locationId;
    return this;
  }

  /**
   * Get locationId
   * @return locationId
   */
  
  @Schema(name = "location_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location_id")
  public @Nullable String getLocationId() {
    return locationId;
  }

  public void setLocationId(@Nullable String locationId) {
    this.locationId = locationId;
  }

  public LocationDetailed name(@Nullable String name) {
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

  public LocationDetailed region(@Nullable String region) {
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

  public LocationDetailed type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public LocationDetailed population(@Nullable Integer population) {
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

  public LocationDetailed dangerLevel(@Nullable String dangerLevel) {
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

  public LocationDetailed descriptionShort(@Nullable String descriptionShort) {
    this.descriptionShort = descriptionShort;
    return this;
  }

  /**
   * Get descriptionShort
   * @return descriptionShort
   */
  
  @Schema(name = "description_short", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description_short")
  public @Nullable String getDescriptionShort() {
    return descriptionShort;
  }

  public void setDescriptionShort(@Nullable String descriptionShort) {
    this.descriptionShort = descriptionShort;
  }

  public LocationDetailed fullDescription(@Nullable String fullDescription) {
    this.fullDescription = fullDescription;
    return this;
  }

  /**
   * Get fullDescription
   * @return fullDescription
   */
  
  @Schema(name = "full_description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("full_description")
  public @Nullable String getFullDescription() {
    return fullDescription;
  }

  public void setFullDescription(@Nullable String fullDescription) {
    this.fullDescription = fullDescription;
  }

  public LocationDetailed history(@Nullable String history) {
    this.history = history;
    return this;
  }

  /**
   * Get history
   * @return history
   */
  
  @Schema(name = "history", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("history")
  public @Nullable String getHistory() {
    return history;
  }

  public void setHistory(@Nullable String history) {
    this.history = history;
  }

  public LocationDetailed districts(List<@Valid LocationDetailedAllOfDistricts> districts) {
    this.districts = districts;
    return this;
  }

  public LocationDetailed addDistrictsItem(LocationDetailedAllOfDistricts districtsItem) {
    if (this.districts == null) {
      this.districts = new ArrayList<>();
    }
    this.districts.add(districtsItem);
    return this;
  }

  /**
   * Get districts
   * @return districts
   */
  @Valid 
  @Schema(name = "districts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("districts")
  public List<@Valid LocationDetailedAllOfDistricts> getDistricts() {
    return districts;
  }

  public void setDistricts(List<@Valid LocationDetailedAllOfDistricts> districts) {
    this.districts = districts;
  }

  public LocationDetailed controllingFactions(List<String> controllingFactions) {
    this.controllingFactions = controllingFactions;
    return this;
  }

  public LocationDetailed addControllingFactionsItem(String controllingFactionsItem) {
    if (this.controllingFactions == null) {
      this.controllingFactions = new ArrayList<>();
    }
    this.controllingFactions.add(controllingFactionsItem);
    return this;
  }

  /**
   * Get controllingFactions
   * @return controllingFactions
   */
  
  @Schema(name = "controlling_factions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("controlling_factions")
  public List<String> getControllingFactions() {
    return controllingFactions;
  }

  public void setControllingFactions(List<String> controllingFactions) {
    this.controllingFactions = controllingFactions;
  }

  public LocationDetailed pointsOfInterest(List<String> pointsOfInterest) {
    this.pointsOfInterest = pointsOfInterest;
    return this;
  }

  public LocationDetailed addPointsOfInterestItem(String pointsOfInterestItem) {
    if (this.pointsOfInterest == null) {
      this.pointsOfInterest = new ArrayList<>();
    }
    this.pointsOfInterest.add(pointsOfInterestItem);
    return this;
  }

  /**
   * Get pointsOfInterest
   * @return pointsOfInterest
   */
  
  @Schema(name = "points_of_interest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("points_of_interest")
  public List<String> getPointsOfInterest() {
    return pointsOfInterest;
  }

  public void setPointsOfInterest(List<String> pointsOfInterest) {
    this.pointsOfInterest = pointsOfInterest;
  }

  public LocationDetailed economy(@Nullable LocationDetailedAllOfEconomy economy) {
    this.economy = economy;
    return this;
  }

  /**
   * Get economy
   * @return economy
   */
  @Valid 
  @Schema(name = "economy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("economy")
  public @Nullable LocationDetailedAllOfEconomy getEconomy() {
    return economy;
  }

  public void setEconomy(@Nullable LocationDetailedAllOfEconomy economy) {
    this.economy = economy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocationDetailed locationDetailed = (LocationDetailed) o;
    return Objects.equals(this.locationId, locationDetailed.locationId) &&
        Objects.equals(this.name, locationDetailed.name) &&
        Objects.equals(this.region, locationDetailed.region) &&
        Objects.equals(this.type, locationDetailed.type) &&
        Objects.equals(this.population, locationDetailed.population) &&
        Objects.equals(this.dangerLevel, locationDetailed.dangerLevel) &&
        Objects.equals(this.descriptionShort, locationDetailed.descriptionShort) &&
        Objects.equals(this.fullDescription, locationDetailed.fullDescription) &&
        Objects.equals(this.history, locationDetailed.history) &&
        Objects.equals(this.districts, locationDetailed.districts) &&
        Objects.equals(this.controllingFactions, locationDetailed.controllingFactions) &&
        Objects.equals(this.pointsOfInterest, locationDetailed.pointsOfInterest) &&
        Objects.equals(this.economy, locationDetailed.economy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locationId, name, region, type, population, dangerLevel, descriptionShort, fullDescription, history, districts, controllingFactions, pointsOfInterest, economy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LocationDetailed {\n");
    sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    population: ").append(toIndentedString(population)).append("\n");
    sb.append("    dangerLevel: ").append(toIndentedString(dangerLevel)).append("\n");
    sb.append("    descriptionShort: ").append(toIndentedString(descriptionShort)).append("\n");
    sb.append("    fullDescription: ").append(toIndentedString(fullDescription)).append("\n");
    sb.append("    history: ").append(toIndentedString(history)).append("\n");
    sb.append("    districts: ").append(toIndentedString(districts)).append("\n");
    sb.append("    controllingFactions: ").append(toIndentedString(controllingFactions)).append("\n");
    sb.append("    pointsOfInterest: ").append(toIndentedString(pointsOfInterest)).append("\n");
    sb.append("    economy: ").append(toIndentedString(economy)).append("\n");
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

