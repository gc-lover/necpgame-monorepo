package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * HUDDataMinimapNearbyPoiInner
 */

@JsonTypeName("HUDData_minimap_nearby_poi_inner")

public class HUDDataMinimapNearbyPoiInner {

  private @Nullable String name;

  private @Nullable String type;

  private @Nullable BigDecimal distanceMeters;

  public HUDDataMinimapNearbyPoiInner name(@Nullable String name) {
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

  public HUDDataMinimapNearbyPoiInner type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public HUDDataMinimapNearbyPoiInner distanceMeters(@Nullable BigDecimal distanceMeters) {
    this.distanceMeters = distanceMeters;
    return this;
  }

  /**
   * Get distanceMeters
   * @return distanceMeters
   */
  @Valid 
  @Schema(name = "distance_meters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("distance_meters")
  public @Nullable BigDecimal getDistanceMeters() {
    return distanceMeters;
  }

  public void setDistanceMeters(@Nullable BigDecimal distanceMeters) {
    this.distanceMeters = distanceMeters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HUDDataMinimapNearbyPoiInner huDDataMinimapNearbyPoiInner = (HUDDataMinimapNearbyPoiInner) o;
    return Objects.equals(this.name, huDDataMinimapNearbyPoiInner.name) &&
        Objects.equals(this.type, huDDataMinimapNearbyPoiInner.type) &&
        Objects.equals(this.distanceMeters, huDDataMinimapNearbyPoiInner.distanceMeters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type, distanceMeters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HUDDataMinimapNearbyPoiInner {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    distanceMeters: ").append(toIndentedString(distanceMeters)).append("\n");
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

