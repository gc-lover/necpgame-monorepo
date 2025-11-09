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
 * RegionMapDistrictsInner
 */

@JsonTypeName("RegionMap_districts_inner")

public class RegionMapDistrictsInner {

  private @Nullable String districtId;

  private @Nullable String districtName;

  private @Nullable Integer mainQuests;

  private @Nullable Integer sideQuests;

  private @Nullable Integer gigs;

  private @Nullable Integer ncpdScanners;

  public RegionMapDistrictsInner districtId(@Nullable String districtId) {
    this.districtId = districtId;
    return this;
  }

  /**
   * Get districtId
   * @return districtId
   */
  
  @Schema(name = "district_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("district_id")
  public @Nullable String getDistrictId() {
    return districtId;
  }

  public void setDistrictId(@Nullable String districtId) {
    this.districtId = districtId;
  }

  public RegionMapDistrictsInner districtName(@Nullable String districtName) {
    this.districtName = districtName;
    return this;
  }

  /**
   * Get districtName
   * @return districtName
   */
  
  @Schema(name = "district_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("district_name")
  public @Nullable String getDistrictName() {
    return districtName;
  }

  public void setDistrictName(@Nullable String districtName) {
    this.districtName = districtName;
  }

  public RegionMapDistrictsInner mainQuests(@Nullable Integer mainQuests) {
    this.mainQuests = mainQuests;
    return this;
  }

  /**
   * Get mainQuests
   * @return mainQuests
   */
  
  @Schema(name = "main_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("main_quests")
  public @Nullable Integer getMainQuests() {
    return mainQuests;
  }

  public void setMainQuests(@Nullable Integer mainQuests) {
    this.mainQuests = mainQuests;
  }

  public RegionMapDistrictsInner sideQuests(@Nullable Integer sideQuests) {
    this.sideQuests = sideQuests;
    return this;
  }

  /**
   * Get sideQuests
   * @return sideQuests
   */
  
  @Schema(name = "side_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("side_quests")
  public @Nullable Integer getSideQuests() {
    return sideQuests;
  }

  public void setSideQuests(@Nullable Integer sideQuests) {
    this.sideQuests = sideQuests;
  }

  public RegionMapDistrictsInner gigs(@Nullable Integer gigs) {
    this.gigs = gigs;
    return this;
  }

  /**
   * Get gigs
   * @return gigs
   */
  
  @Schema(name = "gigs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gigs")
  public @Nullable Integer getGigs() {
    return gigs;
  }

  public void setGigs(@Nullable Integer gigs) {
    this.gigs = gigs;
  }

  public RegionMapDistrictsInner ncpdScanners(@Nullable Integer ncpdScanners) {
    this.ncpdScanners = ncpdScanners;
    return this;
  }

  /**
   * Get ncpdScanners
   * @return ncpdScanners
   */
  
  @Schema(name = "ncpd_scanners", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ncpd_scanners")
  public @Nullable Integer getNcpdScanners() {
    return ncpdScanners;
  }

  public void setNcpdScanners(@Nullable Integer ncpdScanners) {
    this.ncpdScanners = ncpdScanners;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegionMapDistrictsInner regionMapDistrictsInner = (RegionMapDistrictsInner) o;
    return Objects.equals(this.districtId, regionMapDistrictsInner.districtId) &&
        Objects.equals(this.districtName, regionMapDistrictsInner.districtName) &&
        Objects.equals(this.mainQuests, regionMapDistrictsInner.mainQuests) &&
        Objects.equals(this.sideQuests, regionMapDistrictsInner.sideQuests) &&
        Objects.equals(this.gigs, regionMapDistrictsInner.gigs) &&
        Objects.equals(this.ncpdScanners, regionMapDistrictsInner.ncpdScanners);
  }

  @Override
  public int hashCode() {
    return Objects.hash(districtId, districtName, mainQuests, sideQuests, gigs, ncpdScanners);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegionMapDistrictsInner {\n");
    sb.append("    districtId: ").append(toIndentedString(districtId)).append("\n");
    sb.append("    districtName: ").append(toIndentedString(districtName)).append("\n");
    sb.append("    mainQuests: ").append(toIndentedString(mainQuests)).append("\n");
    sb.append("    sideQuests: ").append(toIndentedString(sideQuests)).append("\n");
    sb.append("    gigs: ").append(toIndentedString(gigs)).append("\n");
    sb.append("    ncpdScanners: ").append(toIndentedString(ncpdScanners)).append("\n");
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

