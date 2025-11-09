package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.CyberspaceZone;
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
 * GetCyberspaceZones200Response
 */

@JsonTypeName("getCyberspaceZones_200_response")

public class GetCyberspaceZones200Response {

  @Valid
  private List<@Valid CyberspaceZone> zones = new ArrayList<>();

  public GetCyberspaceZones200Response zones(List<@Valid CyberspaceZone> zones) {
    this.zones = zones;
    return this;
  }

  public GetCyberspaceZones200Response addZonesItem(CyberspaceZone zonesItem) {
    if (this.zones == null) {
      this.zones = new ArrayList<>();
    }
    this.zones.add(zonesItem);
    return this;
  }

  /**
   * Get zones
   * @return zones
   */
  @Valid 
  @Schema(name = "zones", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zones")
  public List<@Valid CyberspaceZone> getZones() {
    return zones;
  }

  public void setZones(List<@Valid CyberspaceZone> zones) {
    this.zones = zones;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCyberspaceZones200Response getCyberspaceZones200Response = (GetCyberspaceZones200Response) o;
    return Objects.equals(this.zones, getCyberspaceZones200Response.zones);
  }

  @Override
  public int hashCode() {
    return Objects.hash(zones);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCyberspaceZones200Response {\n");
    sb.append("    zones: ").append(toIndentedString(zones)).append("\n");
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

