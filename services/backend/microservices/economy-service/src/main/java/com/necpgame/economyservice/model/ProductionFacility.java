package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.ProductionFacilityBonuses;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ProductionFacility
 */


public class ProductionFacility {

  private @Nullable UUID facilityId;

  private @Nullable String name;

  private @Nullable String type;

  private @Nullable String location;

  private @Nullable Integer capacity;

  private @Nullable ProductionFacilityBonuses bonuses;

  private @Nullable Boolean available;

  public ProductionFacility facilityId(@Nullable UUID facilityId) {
    this.facilityId = facilityId;
    return this;
  }

  /**
   * Get facilityId
   * @return facilityId
   */
  @Valid 
  @Schema(name = "facility_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("facility_id")
  public @Nullable UUID getFacilityId() {
    return facilityId;
  }

  public void setFacilityId(@Nullable UUID facilityId) {
    this.facilityId = facilityId;
  }

  public ProductionFacility name(@Nullable String name) {
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

  public ProductionFacility type(@Nullable String type) {
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

  public ProductionFacility location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public ProductionFacility capacity(@Nullable Integer capacity) {
    this.capacity = capacity;
    return this;
  }

  /**
   * Get capacity
   * @return capacity
   */
  
  @Schema(name = "capacity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("capacity")
  public @Nullable Integer getCapacity() {
    return capacity;
  }

  public void setCapacity(@Nullable Integer capacity) {
    this.capacity = capacity;
  }

  public ProductionFacility bonuses(@Nullable ProductionFacilityBonuses bonuses) {
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
  public @Nullable ProductionFacilityBonuses getBonuses() {
    return bonuses;
  }

  public void setBonuses(@Nullable ProductionFacilityBonuses bonuses) {
    this.bonuses = bonuses;
  }

  public ProductionFacility available(@Nullable Boolean available) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductionFacility productionFacility = (ProductionFacility) o;
    return Objects.equals(this.facilityId, productionFacility.facilityId) &&
        Objects.equals(this.name, productionFacility.name) &&
        Objects.equals(this.type, productionFacility.type) &&
        Objects.equals(this.location, productionFacility.location) &&
        Objects.equals(this.capacity, productionFacility.capacity) &&
        Objects.equals(this.bonuses, productionFacility.bonuses) &&
        Objects.equals(this.available, productionFacility.available);
  }

  @Override
  public int hashCode() {
    return Objects.hash(facilityId, name, type, location, capacity, bonuses, available);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductionFacility {\n");
    sb.append("    facilityId: ").append(toIndentedString(facilityId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    capacity: ").append(toIndentedString(capacity)).append("\n");
    sb.append("    bonuses: ").append(toIndentedString(bonuses)).append("\n");
    sb.append("    available: ").append(toIndentedString(available)).append("\n");
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

