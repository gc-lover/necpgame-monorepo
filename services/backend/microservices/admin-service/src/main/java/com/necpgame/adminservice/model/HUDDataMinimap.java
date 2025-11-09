package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.HUDDataMinimapNearbyPoiInner;
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
 * HUDDataMinimap
 */

@JsonTypeName("HUDData_minimap")

public class HUDDataMinimap {

  private @Nullable String location;

  @Valid
  private List<@Valid HUDDataMinimapNearbyPoiInner> nearbyPoi = new ArrayList<>();

  public HUDDataMinimap location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public HUDDataMinimap nearbyPoi(List<@Valid HUDDataMinimapNearbyPoiInner> nearbyPoi) {
    this.nearbyPoi = nearbyPoi;
    return this;
  }

  public HUDDataMinimap addNearbyPoiItem(HUDDataMinimapNearbyPoiInner nearbyPoiItem) {
    if (this.nearbyPoi == null) {
      this.nearbyPoi = new ArrayList<>();
    }
    this.nearbyPoi.add(nearbyPoiItem);
    return this;
  }

  /**
   * Get nearbyPoi
   * @return nearbyPoi
   */
  @Valid 
  @Schema(name = "nearby_poi", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nearby_poi")
  public List<@Valid HUDDataMinimapNearbyPoiInner> getNearbyPoi() {
    return nearbyPoi;
  }

  public void setNearbyPoi(List<@Valid HUDDataMinimapNearbyPoiInner> nearbyPoi) {
    this.nearbyPoi = nearbyPoi;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HUDDataMinimap huDDataMinimap = (HUDDataMinimap) o;
    return Objects.equals(this.location, huDDataMinimap.location) &&
        Objects.equals(this.nearbyPoi, huDDataMinimap.nearbyPoi);
  }

  @Override
  public int hashCode() {
    return Objects.hash(location, nearbyPoi);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HUDDataMinimap {\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    nearbyPoi: ").append(toIndentedString(nearbyPoi)).append("\n");
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

