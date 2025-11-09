package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RegionMapZonesInner
 */

@JsonTypeName("RegionMap_zones_inner")

public class RegionMapZonesInner {

  private @Nullable String zoneId;

  private @Nullable String zoneName;

  private @Nullable Integer quests;

  public RegionMapZonesInner zoneId(@Nullable String zoneId) {
    this.zoneId = zoneId;
    return this;
  }

  /**
   * Get zoneId
   * @return zoneId
   */
  
  @Schema(name = "zone_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zone_id")
  public @Nullable String getZoneId() {
    return zoneId;
  }

  public void setZoneId(@Nullable String zoneId) {
    this.zoneId = zoneId;
  }

  public RegionMapZonesInner zoneName(@Nullable String zoneName) {
    this.zoneName = zoneName;
    return this;
  }

  /**
   * Get zoneName
   * @return zoneName
   */
  
  @Schema(name = "zone_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zone_name")
  public @Nullable String getZoneName() {
    return zoneName;
  }

  public void setZoneName(@Nullable String zoneName) {
    this.zoneName = zoneName;
  }

  public RegionMapZonesInner quests(@Nullable Integer quests) {
    this.quests = quests;
    return this;
  }

  /**
   * Get quests
   * @return quests
   */
  
  @Schema(name = "quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quests")
  public @Nullable Integer getQuests() {
    return quests;
  }

  public void setQuests(@Nullable Integer quests) {
    this.quests = quests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegionMapZonesInner regionMapZonesInner = (RegionMapZonesInner) o;
    return Objects.equals(this.zoneId, regionMapZonesInner.zoneId) &&
        Objects.equals(this.zoneName, regionMapZonesInner.zoneName) &&
        Objects.equals(this.quests, regionMapZonesInner.quests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(zoneId, zoneName, quests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegionMapZonesInner {\n");
    sb.append("    zoneId: ").append(toIndentedString(zoneId)).append("\n");
    sb.append("    zoneName: ").append(toIndentedString(zoneName)).append("\n");
    sb.append("    quests: ").append(toIndentedString(quests)).append("\n");
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

