package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.CombatZone;
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
 * GetZones200Response
 */

@JsonTypeName("getZones_200_response")

public class GetZones200Response {

  @Valid
  private List<@Valid CombatZone> zones = new ArrayList<>();

  public GetZones200Response zones(List<@Valid CombatZone> zones) {
    this.zones = zones;
    return this;
  }

  public GetZones200Response addZonesItem(CombatZone zonesItem) {
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
  public List<@Valid CombatZone> getZones() {
    return zones;
  }

  public void setZones(List<@Valid CombatZone> zones) {
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
    GetZones200Response getZones200Response = (GetZones200Response) o;
    return Objects.equals(this.zones, getZones200Response.zones);
  }

  @Override
  public int hashCode() {
    return Objects.hash(zones);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetZones200Response {\n");
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

