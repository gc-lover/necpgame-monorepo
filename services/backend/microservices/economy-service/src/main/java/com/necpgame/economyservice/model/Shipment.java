package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.Arrays;
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
 * Shipment
 */


public class Shipment {

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

  public Shipment shipmentId(@Nullable UUID shipmentId) {
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

  public Shipment characterId(@Nullable UUID characterId) {
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

  public Shipment status(@Nullable StatusEnum status) {
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

  public Shipment origin(@Nullable String origin) {
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

  public Shipment destination(@Nullable String destination) {
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

  public Shipment vehicleType(@Nullable String vehicleType) {
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

  public Shipment estimatedDelivery(@Nullable OffsetDateTime estimatedDelivery) {
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

  public Shipment actualDelivery(OffsetDateTime actualDelivery) {
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

  public Shipment createdAt(@Nullable OffsetDateTime createdAt) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Shipment shipment = (Shipment) o;
    return Objects.equals(this.shipmentId, shipment.shipmentId) &&
        Objects.equals(this.characterId, shipment.characterId) &&
        Objects.equals(this.status, shipment.status) &&
        Objects.equals(this.origin, shipment.origin) &&
        Objects.equals(this.destination, shipment.destination) &&
        Objects.equals(this.vehicleType, shipment.vehicleType) &&
        Objects.equals(this.estimatedDelivery, shipment.estimatedDelivery) &&
        equalsNullable(this.actualDelivery, shipment.actualDelivery) &&
        Objects.equals(this.createdAt, shipment.createdAt);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(shipmentId, characterId, status, origin, destination, vehicleType, estimatedDelivery, hashCodeNullable(actualDelivery), createdAt);
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
    sb.append("class Shipment {\n");
    sb.append("    shipmentId: ").append(toIndentedString(shipmentId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    origin: ").append(toIndentedString(origin)).append("\n");
    sb.append("    destination: ").append(toIndentedString(destination)).append("\n");
    sb.append("    vehicleType: ").append(toIndentedString(vehicleType)).append("\n");
    sb.append("    estimatedDelivery: ").append(toIndentedString(estimatedDelivery)).append("\n");
    sb.append("    actualDelivery: ").append(toIndentedString(actualDelivery)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

