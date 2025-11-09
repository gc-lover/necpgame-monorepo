package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.CraftingStation;
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
 * GetCraftingStations200Response
 */

@JsonTypeName("getCraftingStations_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetCraftingStations200Response {

  @Valid
  private List<@Valid CraftingStation> stations = new ArrayList<>();

  public GetCraftingStations200Response stations(List<@Valid CraftingStation> stations) {
    this.stations = stations;
    return this;
  }

  public GetCraftingStations200Response addStationsItem(CraftingStation stationsItem) {
    if (this.stations == null) {
      this.stations = new ArrayList<>();
    }
    this.stations.add(stationsItem);
    return this;
  }

  /**
   * Get stations
   * @return stations
   */
  @Valid 
  @Schema(name = "stations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stations")
  public List<@Valid CraftingStation> getStations() {
    return stations;
  }

  public void setStations(List<@Valid CraftingStation> stations) {
    this.stations = stations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCraftingStations200Response getCraftingStations200Response = (GetCraftingStations200Response) o;
    return Objects.equals(this.stations, getCraftingStations200Response.stations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCraftingStations200Response {\n");
    sb.append("    stations: ").append(toIndentedString(stations)).append("\n");
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

