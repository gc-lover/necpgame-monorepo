package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.CraftingStationBonuses;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CraftingStation
 */


public class CraftingStation {

  private @Nullable String stationId;

  private @Nullable String name;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    WEAPONS_BENCH("WEAPONS_BENCH"),
    
    ARMOR_BENCH("ARMOR_BENCH"),
    
    CYBERWARE_LAB("CYBERWARE_LAB"),
    
    CHEMISTRY_LAB("CHEMISTRY_LAB"),
    
    GENERAL_WORKBENCH("GENERAL_WORKBENCH");

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

  private @Nullable String locationId;

  private @Nullable Boolean available;

  private @Nullable CraftingStationBonuses bonuses;

  public CraftingStation stationId(@Nullable String stationId) {
    this.stationId = stationId;
    return this;
  }

  /**
   * Get stationId
   * @return stationId
   */
  
  @Schema(name = "station_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("station_id")
  public @Nullable String getStationId() {
    return stationId;
  }

  public void setStationId(@Nullable String stationId) {
    this.stationId = stationId;
  }

  public CraftingStation name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", example = "Legendary Weapons Bench", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public CraftingStation type(@Nullable TypeEnum type) {
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

  public CraftingStation locationId(@Nullable String locationId) {
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

  public CraftingStation available(@Nullable Boolean available) {
    this.available = available;
    return this;
  }

  /**
   * Get available
   * @return available
   */
  
  @Schema(name = "available", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available")
  public @Nullable Boolean getAvailable() {
    return available;
  }

  public void setAvailable(@Nullable Boolean available) {
    this.available = available;
  }

  public CraftingStation bonuses(@Nullable CraftingStationBonuses bonuses) {
    this.bonuses = bonuses;
    return this;
  }

  /**
   * Get bonuses
   * @return bonuses
   */
  @Valid 
  @Schema(name = "bonuses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonuses")
  public @Nullable CraftingStationBonuses getBonuses() {
    return bonuses;
  }

  public void setBonuses(@Nullable CraftingStationBonuses bonuses) {
    this.bonuses = bonuses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CraftingStation craftingStation = (CraftingStation) o;
    return Objects.equals(this.stationId, craftingStation.stationId) &&
        Objects.equals(this.name, craftingStation.name) &&
        Objects.equals(this.type, craftingStation.type) &&
        Objects.equals(this.locationId, craftingStation.locationId) &&
        Objects.equals(this.available, craftingStation.available) &&
        Objects.equals(this.bonuses, craftingStation.bonuses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stationId, name, type, locationId, available, bonuses);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CraftingStation {\n");
    sb.append("    stationId: ").append(toIndentedString(stationId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
    sb.append("    available: ").append(toIndentedString(available)).append("\n");
    sb.append("    bonuses: ").append(toIndentedString(bonuses)).append("\n");
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

