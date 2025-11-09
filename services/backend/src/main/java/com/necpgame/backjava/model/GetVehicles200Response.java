package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.Vehicle;
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
 * GetVehicles200Response
 */

@JsonTypeName("getVehicles_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetVehicles200Response {

  @Valid
  private List<@Valid Vehicle> vehicles = new ArrayList<>();

  public GetVehicles200Response vehicles(List<@Valid Vehicle> vehicles) {
    this.vehicles = vehicles;
    return this;
  }

  public GetVehicles200Response addVehiclesItem(Vehicle vehiclesItem) {
    if (this.vehicles == null) {
      this.vehicles = new ArrayList<>();
    }
    this.vehicles.add(vehiclesItem);
    return this;
  }

  /**
   * Get vehicles
   * @return vehicles
   */
  @Valid 
  @Schema(name = "vehicles", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("vehicles")
  public List<@Valid Vehicle> getVehicles() {
    return vehicles;
  }

  public void setVehicles(List<@Valid Vehicle> vehicles) {
    this.vehicles = vehicles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetVehicles200Response getVehicles200Response = (GetVehicles200Response) o;
    return Objects.equals(this.vehicles, getVehicles200Response.vehicles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vehicles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetVehicles200Response {\n");
    sb.append("    vehicles: ").append(toIndentedString(vehicles)).append("\n");
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

