package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.CargoItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
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
 * CreateShipmentRequest
 */


public class CreateShipmentRequest {

  private UUID characterId;

  private String origin;

  private String destination;

  @Valid
  private List<@Valid CargoItem> cargo = new ArrayList<>();

  /**
   * Gets or Sets vehicleType
   */
  public enum VehicleTypeEnum {
    ON_FOOT("ON_FOOT"),
    
    MOTORCYCLE("MOTORCYCLE"),
    
    CAR("CAR"),
    
    TRUCK("TRUCK"),
    
    AERODYNE("AERODYNE");

    private final String value;

    VehicleTypeEnum(String value) {
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
    public static VehicleTypeEnum fromValue(String value) {
      for (VehicleTypeEnum b : VehicleTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private VehicleTypeEnum vehicleType;

  private JsonNullable<String> routeId = JsonNullable.<String>undefined();

  /**
   * Gets or Sets insurancePlan
   */
  public enum InsurancePlanEnum {
    NONE("NONE"),
    
    BASIC("BASIC"),
    
    STANDARD("STANDARD"),
    
    PREMIUM("PREMIUM");

    private final String value;

    InsurancePlanEnum(String value) {
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
    public static InsurancePlanEnum fromValue(String value) {
      for (InsurancePlanEnum b : InsurancePlanEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private InsurancePlanEnum insurancePlan = InsurancePlanEnum.NONE;

  /**
   * Gets or Sets priority
   */
  public enum PriorityEnum {
    NORMAL("NORMAL"),
    
    EXPRESS("EXPRESS");

    private final String value;

    PriorityEnum(String value) {
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
    public static PriorityEnum fromValue(String value) {
      for (PriorityEnum b : PriorityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PriorityEnum priority = PriorityEnum.NORMAL;

  public CreateShipmentRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateShipmentRequest(UUID characterId, String origin, String destination, List<@Valid CargoItem> cargo, VehicleTypeEnum vehicleType) {
    this.characterId = characterId;
    this.origin = origin;
    this.destination = destination;
    this.cargo = cargo;
    this.vehicleType = vehicleType;
  }

  public CreateShipmentRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public CreateShipmentRequest origin(String origin) {
    this.origin = origin;
    return this;
  }

  /**
   * Get origin
   * @return origin
   */
  @NotNull 
  @Schema(name = "origin", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("origin")
  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public CreateShipmentRequest destination(String destination) {
    this.destination = destination;
    return this;
  }

  /**
   * Get destination
   * @return destination
   */
  @NotNull 
  @Schema(name = "destination", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("destination")
  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public CreateShipmentRequest cargo(List<@Valid CargoItem> cargo) {
    this.cargo = cargo;
    return this;
  }

  public CreateShipmentRequest addCargoItem(CargoItem cargoItem) {
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
  @NotNull @Valid 
  @Schema(name = "cargo", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cargo")
  public List<@Valid CargoItem> getCargo() {
    return cargo;
  }

  public void setCargo(List<@Valid CargoItem> cargo) {
    this.cargo = cargo;
  }

  public CreateShipmentRequest vehicleType(VehicleTypeEnum vehicleType) {
    this.vehicleType = vehicleType;
    return this;
  }

  /**
   * Get vehicleType
   * @return vehicleType
   */
  @NotNull 
  @Schema(name = "vehicle_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("vehicle_type")
  public VehicleTypeEnum getVehicleType() {
    return vehicleType;
  }

  public void setVehicleType(VehicleTypeEnum vehicleType) {
    this.vehicleType = vehicleType;
  }

  public CreateShipmentRequest routeId(String routeId) {
    this.routeId = JsonNullable.of(routeId);
    return this;
  }

  /**
   * Get routeId
   * @return routeId
   */
  
  @Schema(name = "route_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("route_id")
  public JsonNullable<String> getRouteId() {
    return routeId;
  }

  public void setRouteId(JsonNullable<String> routeId) {
    this.routeId = routeId;
  }

  public CreateShipmentRequest insurancePlan(InsurancePlanEnum insurancePlan) {
    this.insurancePlan = insurancePlan;
    return this;
  }

  /**
   * Get insurancePlan
   * @return insurancePlan
   */
  
  @Schema(name = "insurance_plan", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("insurance_plan")
  public InsurancePlanEnum getInsurancePlan() {
    return insurancePlan;
  }

  public void setInsurancePlan(InsurancePlanEnum insurancePlan) {
    this.insurancePlan = insurancePlan;
  }

  public CreateShipmentRequest priority(PriorityEnum priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public PriorityEnum getPriority() {
    return priority;
  }

  public void setPriority(PriorityEnum priority) {
    this.priority = priority;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateShipmentRequest createShipmentRequest = (CreateShipmentRequest) o;
    return Objects.equals(this.characterId, createShipmentRequest.characterId) &&
        Objects.equals(this.origin, createShipmentRequest.origin) &&
        Objects.equals(this.destination, createShipmentRequest.destination) &&
        Objects.equals(this.cargo, createShipmentRequest.cargo) &&
        Objects.equals(this.vehicleType, createShipmentRequest.vehicleType) &&
        equalsNullable(this.routeId, createShipmentRequest.routeId) &&
        Objects.equals(this.insurancePlan, createShipmentRequest.insurancePlan) &&
        Objects.equals(this.priority, createShipmentRequest.priority);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, origin, destination, cargo, vehicleType, hashCodeNullable(routeId), insurancePlan, priority);
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
    sb.append("class CreateShipmentRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    origin: ").append(toIndentedString(origin)).append("\n");
    sb.append("    destination: ").append(toIndentedString(destination)).append("\n");
    sb.append("    cargo: ").append(toIndentedString(cargo)).append("\n");
    sb.append("    vehicleType: ").append(toIndentedString(vehicleType)).append("\n");
    sb.append("    routeId: ").append(toIndentedString(routeId)).append("\n");
    sb.append("    insurancePlan: ").append(toIndentedString(insurancePlan)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
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

