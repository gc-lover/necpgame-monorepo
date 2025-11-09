package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Vehicle
 */


public class Vehicle {

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    ON_FOOT("ON_FOOT"),
    
    MOTORCYCLE("MOTORCYCLE"),
    
    CAR("CAR"),
    
    TRUCK("TRUCK"),
    
    AERODYNE("AERODYNE");

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

  private @Nullable String name;

  private @Nullable BigDecimal speedMultiplier;

  private @Nullable BigDecimal capacityWeight;

  private @Nullable BigDecimal capacityVolume;

  private @Nullable BigDecimal riskModifier;

  private @Nullable BigDecimal costMultiplier;

  public Vehicle type(@Nullable TypeEnum type) {
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

  public Vehicle name(@Nullable String name) {
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

  public Vehicle speedMultiplier(@Nullable BigDecimal speedMultiplier) {
    this.speedMultiplier = speedMultiplier;
    return this;
  }

  /**
   * Get speedMultiplier
   * @return speedMultiplier
   */
  @Valid 
  @Schema(name = "speed_multiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("speed_multiplier")
  public @Nullable BigDecimal getSpeedMultiplier() {
    return speedMultiplier;
  }

  public void setSpeedMultiplier(@Nullable BigDecimal speedMultiplier) {
    this.speedMultiplier = speedMultiplier;
  }

  public Vehicle capacityWeight(@Nullable BigDecimal capacityWeight) {
    this.capacityWeight = capacityWeight;
    return this;
  }

  /**
   * Get capacityWeight
   * @return capacityWeight
   */
  @Valid 
  @Schema(name = "capacity_weight", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("capacity_weight")
  public @Nullable BigDecimal getCapacityWeight() {
    return capacityWeight;
  }

  public void setCapacityWeight(@Nullable BigDecimal capacityWeight) {
    this.capacityWeight = capacityWeight;
  }

  public Vehicle capacityVolume(@Nullable BigDecimal capacityVolume) {
    this.capacityVolume = capacityVolume;
    return this;
  }

  /**
   * Get capacityVolume
   * @return capacityVolume
   */
  @Valid 
  @Schema(name = "capacity_volume", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("capacity_volume")
  public @Nullable BigDecimal getCapacityVolume() {
    return capacityVolume;
  }

  public void setCapacityVolume(@Nullable BigDecimal capacityVolume) {
    this.capacityVolume = capacityVolume;
  }

  public Vehicle riskModifier(@Nullable BigDecimal riskModifier) {
    this.riskModifier = riskModifier;
    return this;
  }

  /**
   * Модификатор риска
   * @return riskModifier
   */
  @Valid 
  @Schema(name = "risk_modifier", description = "Модификатор риска", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("risk_modifier")
  public @Nullable BigDecimal getRiskModifier() {
    return riskModifier;
  }

  public void setRiskModifier(@Nullable BigDecimal riskModifier) {
    this.riskModifier = riskModifier;
  }

  public Vehicle costMultiplier(@Nullable BigDecimal costMultiplier) {
    this.costMultiplier = costMultiplier;
    return this;
  }

  /**
   * Get costMultiplier
   * @return costMultiplier
   */
  @Valid 
  @Schema(name = "cost_multiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cost_multiplier")
  public @Nullable BigDecimal getCostMultiplier() {
    return costMultiplier;
  }

  public void setCostMultiplier(@Nullable BigDecimal costMultiplier) {
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
    Vehicle vehicle = (Vehicle) o;
    return Objects.equals(this.type, vehicle.type) &&
        Objects.equals(this.name, vehicle.name) &&
        Objects.equals(this.speedMultiplier, vehicle.speedMultiplier) &&
        Objects.equals(this.capacityWeight, vehicle.capacityWeight) &&
        Objects.equals(this.capacityVolume, vehicle.capacityVolume) &&
        Objects.equals(this.riskModifier, vehicle.riskModifier) &&
        Objects.equals(this.costMultiplier, vehicle.costMultiplier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, name, speedMultiplier, capacityWeight, capacityVolume, riskModifier, costMultiplier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Vehicle {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    speedMultiplier: ").append(toIndentedString(speedMultiplier)).append("\n");
    sb.append("    capacityWeight: ").append(toIndentedString(capacityWeight)).append("\n");
    sb.append("    capacityVolume: ").append(toIndentedString(capacityVolume)).append("\n");
    sb.append("    riskModifier: ").append(toIndentedString(riskModifier)).append("\n");
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

