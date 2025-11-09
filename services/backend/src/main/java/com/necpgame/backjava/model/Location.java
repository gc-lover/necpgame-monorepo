package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Location
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class Location {

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

  public Location locationId(@Nullable String locationId) {
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

  public Location name(@Nullable String name) {
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

  public Location region(@Nullable String region) {
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

  public Location type(@Nullable TypeEnum type) {
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

  public Location population(@Nullable Integer population) {
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

  public Location dangerLevel(@Nullable String dangerLevel) {
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

  public Location descriptionShort(@Nullable String descriptionShort) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Location location = (Location) o;
    return Objects.equals(this.locationId, location.locationId) &&
        Objects.equals(this.name, location.name) &&
        Objects.equals(this.region, location.region) &&
        Objects.equals(this.type, location.type) &&
        Objects.equals(this.population, location.population) &&
        Objects.equals(this.dangerLevel, location.dangerLevel) &&
        Objects.equals(this.descriptionShort, location.descriptionShort);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locationId, name, region, type, population, dangerLevel, descriptionShort);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Location {\n");
    sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    population: ").append(toIndentedString(population)).append("\n");
    sb.append("    dangerLevel: ").append(toIndentedString(dangerLevel)).append("\n");
    sb.append("    descriptionShort: ").append(toIndentedString(descriptionShort)).append("\n");
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

