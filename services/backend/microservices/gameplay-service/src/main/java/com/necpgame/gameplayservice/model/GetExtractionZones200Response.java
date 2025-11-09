package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.ExtractionZone;
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
 * GetExtractionZones200Response
 */

@JsonTypeName("getExtractionZones_200_response")

public class GetExtractionZones200Response {

  @Valid
  private List<@Valid ExtractionZone> zones = new ArrayList<>();

  public GetExtractionZones200Response zones(List<@Valid ExtractionZone> zones) {
    this.zones = zones;
    return this;
  }

  public GetExtractionZones200Response addZonesItem(ExtractionZone zonesItem) {
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
  public List<@Valid ExtractionZone> getZones() {
    return zones;
  }

  public void setZones(List<@Valid ExtractionZone> zones) {
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
    GetExtractionZones200Response getExtractionZones200Response = (GetExtractionZones200Response) o;
    return Objects.equals(this.zones, getExtractionZones200Response.zones);
  }

  @Override
  public int hashCode() {
    return Objects.hash(zones);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetExtractionZones200Response {\n");
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

