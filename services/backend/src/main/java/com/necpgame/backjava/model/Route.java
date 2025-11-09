package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * Route
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class Route {

  private @Nullable String routeId;

  private @Nullable String name;

  private @Nullable String origin;

  private @Nullable String destination;

  private @Nullable BigDecimal distanceKm;

  private @Nullable BigDecimal estimatedTimeHours;

  @Valid
  private List<String> vehicleTypes = new ArrayList<>();

  /**
   * Gets or Sets riskLevel
   */
  public enum RiskLevelEnum {
    LOW("LOW"),
    
    MEDIUM("MEDIUM"),
    
    HIGH("HIGH"),
    
    EXTREME("EXTREME");

    private final String value;

    RiskLevelEnum(String value) {
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
    public static RiskLevelEnum fromValue(String value) {
      for (RiskLevelEnum b : RiskLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RiskLevelEnum riskLevel;

  private @Nullable Float costMultiplier;

  public Route routeId(@Nullable String routeId) {
    this.routeId = routeId;
    return this;
  }

  /**
   * Get routeId
   * @return routeId
   */
  
  @Schema(name = "route_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("route_id")
  public @Nullable String getRouteId() {
    return routeId;
  }

  public void setRouteId(@Nullable String routeId) {
    this.routeId = routeId;
  }

  public Route name(@Nullable String name) {
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

  public Route origin(@Nullable String origin) {
    this.origin = origin;
    return this;
  }

  /**
   * Get origin
   * @return origin
   */
  
  @Schema(name = "origin", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("origin")
  public @Nullable String getOrigin() {
    return origin;
  }

  public void setOrigin(@Nullable String origin) {
    this.origin = origin;
  }

  public Route destination(@Nullable String destination) {
    this.destination = destination;
    return this;
  }

  /**
   * Get destination
   * @return destination
   */
  
  @Schema(name = "destination", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("destination")
  public @Nullable String getDestination() {
    return destination;
  }

  public void setDestination(@Nullable String destination) {
    this.destination = destination;
  }

  public Route distanceKm(@Nullable BigDecimal distanceKm) {
    this.distanceKm = distanceKm;
    return this;
  }

  /**
   * Get distanceKm
   * @return distanceKm
   */
  @Valid 
  @Schema(name = "distance_km", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("distance_km")
  public @Nullable BigDecimal getDistanceKm() {
    return distanceKm;
  }

  public void setDistanceKm(@Nullable BigDecimal distanceKm) {
    this.distanceKm = distanceKm;
  }

  public Route estimatedTimeHours(@Nullable BigDecimal estimatedTimeHours) {
    this.estimatedTimeHours = estimatedTimeHours;
    return this;
  }

  /**
   * Get estimatedTimeHours
   * @return estimatedTimeHours
   */
  @Valid 
  @Schema(name = "estimated_time_hours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_time_hours")
  public @Nullable BigDecimal getEstimatedTimeHours() {
    return estimatedTimeHours;
  }

  public void setEstimatedTimeHours(@Nullable BigDecimal estimatedTimeHours) {
    this.estimatedTimeHours = estimatedTimeHours;
  }

  public Route vehicleTypes(List<String> vehicleTypes) {
    this.vehicleTypes = vehicleTypes;
    return this;
  }

  public Route addVehicleTypesItem(String vehicleTypesItem) {
    if (this.vehicleTypes == null) {
      this.vehicleTypes = new ArrayList<>();
    }
    this.vehicleTypes.add(vehicleTypesItem);
    return this;
  }

  /**
   * Get vehicleTypes
   * @return vehicleTypes
   */
  
  @Schema(name = "vehicle_types", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("vehicle_types")
  public List<String> getVehicleTypes() {
    return vehicleTypes;
  }

  public void setVehicleTypes(List<String> vehicleTypes) {
    this.vehicleTypes = vehicleTypes;
  }

  public Route riskLevel(@Nullable RiskLevelEnum riskLevel) {
    this.riskLevel = riskLevel;
    return this;
  }

  /**
   * Get riskLevel
   * @return riskLevel
   */
  
  @Schema(name = "risk_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("risk_level")
  public @Nullable RiskLevelEnum getRiskLevel() {
    return riskLevel;
  }

  public void setRiskLevel(@Nullable RiskLevelEnum riskLevel) {
    this.riskLevel = riskLevel;
  }

  public Route costMultiplier(@Nullable Float costMultiplier) {
    this.costMultiplier = costMultiplier;
    return this;
  }

  /**
   * Get costMultiplier
   * @return costMultiplier
   */
  
  @Schema(name = "cost_multiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cost_multiplier")
  public @Nullable Float getCostMultiplier() {
    return costMultiplier;
  }

  public void setCostMultiplier(@Nullable Float costMultiplier) {
    this.costMultiplier = costMultiplier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Route route = (Route) o;
    return Objects.equals(this.routeId, route.routeId) &&
        Objects.equals(this.name, route.name) &&
        Objects.equals(this.origin, route.origin) &&
        Objects.equals(this.destination, route.destination) &&
        Objects.equals(this.distanceKm, route.distanceKm) &&
        Objects.equals(this.estimatedTimeHours, route.estimatedTimeHours) &&
        Objects.equals(this.vehicleTypes, route.vehicleTypes) &&
        Objects.equals(this.riskLevel, route.riskLevel) &&
        Objects.equals(this.costMultiplier, route.costMultiplier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(routeId, name, origin, destination, distanceKm, estimatedTimeHours, vehicleTypes, riskLevel, costMultiplier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Route {\n");
    sb.append("    routeId: ").append(toIndentedString(routeId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    origin: ").append(toIndentedString(origin)).append("\n");
    sb.append("    destination: ").append(toIndentedString(destination)).append("\n");
    sb.append("    distanceKm: ").append(toIndentedString(distanceKm)).append("\n");
    sb.append("    estimatedTimeHours: ").append(toIndentedString(estimatedTimeHours)).append("\n");
    sb.append("    vehicleTypes: ").append(toIndentedString(vehicleTypes)).append("\n");
    sb.append("    riskLevel: ").append(toIndentedString(riskLevel)).append("\n");
    sb.append("    costMultiplier: ").append(toIndentedString(costMultiplier)).append("\n");
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

