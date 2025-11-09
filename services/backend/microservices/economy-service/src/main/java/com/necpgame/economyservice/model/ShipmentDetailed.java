package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.CargoItem;
import com.necpgame.economyservice.model.Incident;
import com.necpgame.economyservice.model.Insurance;
import com.necpgame.economyservice.model.Route;
import com.necpgame.economyservice.model.ShipmentDetailedAllOfTrackingHistory;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ShipmentDetailed
 */


public class ShipmentDetailed {

  private @Nullable UUID shipmentId;

  private @Nullable UUID characterId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("PENDING"),
    
    IN_TRANSIT("IN_TRANSIT"),
    
    DELIVERED("DELIVERED"),
    
    FAILED("FAILED"),
    
    CANCELLED("CANCELLED");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  private @Nullable String origin;

  private @Nullable String destination;

  private @Nullable String vehicleType;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime estimatedDelivery;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> actualDelivery = JsonNullable.<OffsetDateTime>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  @Valid
  private List<@Valid CargoItem> cargo = new ArrayList<>();

  private @Nullable Route route;

  private JsonNullable<String> currentLocation = JsonNullable.<String>undefined();

  private @Nullable Float progressPercentage;

  private @Nullable Insurance insurance;

  @Valid
  private List<@Valid Incident> incidents = new ArrayList<>();

  @Valid
  private List<@Valid ShipmentDetailedAllOfTrackingHistory> trackingHistory = new ArrayList<>();

  public ShipmentDetailed shipmentId(@Nullable UUID shipmentId) {
    this.shipmentId = shipmentId;
    return this;
  }

  /**
   * Get shipmentId
   * @return shipmentId
   */
  @Valid 
  @Schema(name = "shipment_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("shipment_id")
  public @Nullable UUID getShipmentId() {
    return shipmentId;
  }

  public void setShipmentId(@Nullable UUID shipmentId) {
    this.shipmentId = shipmentId;
  }

  public ShipmentDetailed characterId(@Nullable UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable UUID characterId) {
    this.characterId = characterId;
  }

  public ShipmentDetailed status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public ShipmentDetailed origin(@Nullable String origin) {
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

  public ShipmentDetailed destination(@Nullable String destination) {
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

  public ShipmentDetailed vehicleType(@Nullable String vehicleType) {
    this.vehicleType = vehicleType;
    return this;
  }

  /**
   * Get vehicleType
   * @return vehicleType
   */
  
  @Schema(name = "vehicle_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("vehicle_type")
  public @Nullable String getVehicleType() {
    return vehicleType;
  }

  public void setVehicleType(@Nullable String vehicleType) {
    this.vehicleType = vehicleType;
  }

  public ShipmentDetailed estimatedDelivery(@Nullable OffsetDateTime estimatedDelivery) {
    this.estimatedDelivery = estimatedDelivery;
    return this;
  }

  /**
   * Get estimatedDelivery
   * @return estimatedDelivery
   */
  @Valid 
  @Schema(name = "estimated_delivery", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_delivery")
  public @Nullable OffsetDateTime getEstimatedDelivery() {
    return estimatedDelivery;
  }

  public void setEstimatedDelivery(@Nullable OffsetDateTime estimatedDelivery) {
    this.estimatedDelivery = estimatedDelivery;
  }

  public ShipmentDetailed actualDelivery(OffsetDateTime actualDelivery) {
    this.actualDelivery = JsonNullable.of(actualDelivery);
    return this;
  }

  /**
   * Get actualDelivery
   * @return actualDelivery
   */
  @Valid 
  @Schema(name = "actual_delivery", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actual_delivery")
  public JsonNullable<OffsetDateTime> getActualDelivery() {
    return actualDelivery;
  }

  public void setActualDelivery(JsonNullable<OffsetDateTime> actualDelivery) {
    this.actualDelivery = actualDelivery;
  }

  public ShipmentDetailed createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "created_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_at")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public ShipmentDetailed cargo(List<@Valid CargoItem> cargo) {
    this.cargo = cargo;
    return this;
  }

  public ShipmentDetailed addCargoItem(CargoItem cargoItem) {
    if (this.cargo == null) {
      this.cargo = new ArrayList<>();
    }
    this.cargo.add(cargoItem);
    return this;
  }

  /**
   * Get cargo
   * @return cargo
   */
  @Valid 
  @Schema(name = "cargo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cargo")
  public List<@Valid CargoItem> getCargo() {
    return cargo;
  }

  public void setCargo(List<@Valid CargoItem> cargo) {
    this.cargo = cargo;
  }

  public ShipmentDetailed route(@Nullable Route route) {
    this.route = route;
    return this;
  }

  /**
   * Get route
   * @return route
   */
  @Valid 
  @Schema(name = "route", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("route")
  public @Nullable Route getRoute() {
    return route;
  }

  public void setRoute(@Nullable Route route) {
    this.route = route;
  }

  public ShipmentDetailed currentLocation(String currentLocation) {
    this.currentLocation = JsonNullable.of(currentLocation);
    return this;
  }

  /**
   * Get currentLocation
   * @return currentLocation
   */
  
  @Schema(name = "current_location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_location")
  public JsonNullable<String> getCurrentLocation() {
    return currentLocation;
  }

  public void setCurrentLocation(JsonNullable<String> currentLocation) {
    this.currentLocation = currentLocation;
  }

  public ShipmentDetailed progressPercentage(@Nullable Float progressPercentage) {
    this.progressPercentage = progressPercentage;
    return this;
  }

  /**
   * Get progressPercentage
   * @return progressPercentage
   */
  
  @Schema(name = "progress_percentage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progress_percentage")
  public @Nullable Float getProgressPercentage() {
    return progressPercentage;
  }

  public void setProgressPercentage(@Nullable Float progressPercentage) {
    this.progressPercentage = progressPercentage;
  }

  public ShipmentDetailed insurance(@Nullable Insurance insurance) {
    this.insurance = insurance;
    return this;
  }

  /**
   * Get insurance
   * @return insurance
   */
  @Valid 
  @Schema(name = "insurance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("insurance")
  public @Nullable Insurance getInsurance() {
    return insurance;
  }

  public void setInsurance(@Nullable Insurance insurance) {
    this.insurance = insurance;
  }

  public ShipmentDetailed incidents(List<@Valid Incident> incidents) {
    this.incidents = incidents;
    return this;
  }

  public ShipmentDetailed addIncidentsItem(Incident incidentsItem) {
    if (this.incidents == null) {
      this.incidents = new ArrayList<>();
    }
    this.incidents.add(incidentsItem);
    return this;
  }

  /**
   * Get incidents
   * @return incidents
   */
  @Valid 
  @Schema(name = "incidents", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("incidents")
  public List<@Valid Incident> getIncidents() {
    return incidents;
  }

  public void setIncidents(List<@Valid Incident> incidents) {
    this.incidents = incidents;
  }

  public ShipmentDetailed trackingHistory(List<@Valid ShipmentDetailedAllOfTrackingHistory> trackingHistory) {
    this.trackingHistory = trackingHistory;
    return this;
  }

  public ShipmentDetailed addTrackingHistoryItem(ShipmentDetailedAllOfTrackingHistory trackingHistoryItem) {
    if (this.trackingHistory == null) {
      this.trackingHistory = new ArrayList<>();
    }
    this.trackingHistory.add(trackingHistoryItem);
    return this;
  }

  /**
   * Get trackingHistory
   * @return trackingHistory
   */
  @Valid 
  @Schema(name = "tracking_history", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tracking_history")
  public List<@Valid ShipmentDetailedAllOfTrackingHistory> getTrackingHistory() {
    return trackingHistory;
  }

  public void setTrackingHistory(List<@Valid ShipmentDetailedAllOfTrackingHistory> trackingHistory) {
    this.trackingHistory = trackingHistory;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShipmentDetailed shipmentDetailed = (ShipmentDetailed) o;
    return Objects.equals(this.shipmentId, shipmentDetailed.shipmentId) &&
        Objects.equals(this.characterId, shipmentDetailed.characterId) &&
        Objects.equals(this.status, shipmentDetailed.status) &&
        Objects.equals(this.origin, shipmentDetailed.origin) &&
        Objects.equals(this.destination, shipmentDetailed.destination) &&
        Objects.equals(this.vehicleType, shipmentDetailed.vehicleType) &&
        Objects.equals(this.estimatedDelivery, shipmentDetailed.estimatedDelivery) &&
        equalsNullable(this.actualDelivery, shipmentDetailed.actualDelivery) &&
        Objects.equals(this.createdAt, shipmentDetailed.createdAt) &&
        Objects.equals(this.cargo, shipmentDetailed.cargo) &&
        Objects.equals(this.route, shipmentDetailed.route) &&
        equalsNullable(this.currentLocation, shipmentDetailed.currentLocation) &&
        Objects.equals(this.progressPercentage, shipmentDetailed.progressPercentage) &&
        Objects.equals(this.insurance, shipmentDetailed.insurance) &&
        Objects.equals(this.incidents, shipmentDetailed.incidents) &&
        Objects.equals(this.trackingHistory, shipmentDetailed.trackingHistory);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(shipmentId, characterId, status, origin, destination, vehicleType, estimatedDelivery, hashCodeNullable(actualDelivery), createdAt, cargo, route, hashCodeNullable(currentLocation), progressPercentage, insurance, incidents, trackingHistory);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShipmentDetailed {\n");
    sb.append("    shipmentId: ").append(toIndentedString(shipmentId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    origin: ").append(toIndentedString(origin)).append("\n");
    sb.append("    destination: ").append(toIndentedString(destination)).append("\n");
    sb.append("    vehicleType: ").append(toIndentedString(vehicleType)).append("\n");
    sb.append("    estimatedDelivery: ").append(toIndentedString(estimatedDelivery)).append("\n");
    sb.append("    actualDelivery: ").append(toIndentedString(actualDelivery)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    cargo: ").append(toIndentedString(cargo)).append("\n");
    sb.append("    route: ").append(toIndentedString(route)).append("\n");
    sb.append("    currentLocation: ").append(toIndentedString(currentLocation)).append("\n");
    sb.append("    progressPercentage: ").append(toIndentedString(progressPercentage)).append("\n");
    sb.append("    insurance: ").append(toIndentedString(insurance)).append("\n");
    sb.append("    incidents: ").append(toIndentedString(incidents)).append("\n");
    sb.append("    trackingHistory: ").append(toIndentedString(trackingHistory)).append("\n");
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

