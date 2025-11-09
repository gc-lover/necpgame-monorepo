package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.DistrictQuestsResponseNcpdScannersInner;
import com.necpgame.narrativeservice.model.QuestNode;
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
 * DistrictQuestsResponse
 */


public class DistrictQuestsResponse {

  private @Nullable String districtId;

  private @Nullable String districtName;

  private @Nullable String region;

  @Valid
  private List<@Valid QuestNode> mainQuests = new ArrayList<>();

  @Valid
  private List<@Valid QuestNode> sideQuests = new ArrayList<>();

  @Valid
  private List<@Valid QuestNode> gigs = new ArrayList<>();

  @Valid
  private List<@Valid DistrictQuestsResponseNcpdScannersInner> ncpdScanners = new ArrayList<>();

  private @Nullable Integer totalQuests;

  public DistrictQuestsResponse districtId(@Nullable String districtId) {
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

  public DistrictQuestsResponse districtName(@Nullable String districtName) {
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

  public DistrictQuestsResponse region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", example = "night_city", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  public DistrictQuestsResponse mainQuests(List<@Valid QuestNode> mainQuests) {
    this.mainQuests = mainQuests;
    return this;
  }

  public DistrictQuestsResponse addMainQuestsItem(QuestNode mainQuestsItem) {
    if (this.mainQuests == null) {
      this.mainQuests = new ArrayList<>();
    }
    this.mainQuests.add(mainQuestsItem);
    return this;
  }

  /**
   * Get mainQuests
   * @return mainQuests
   */
  @Valid 
  @Schema(name = "main_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("main_quests")
  public List<@Valid QuestNode> getMainQuests() {
    return mainQuests;
  }

  public void setMainQuests(List<@Valid QuestNode> mainQuests) {
    this.mainQuests = mainQuests;
  }

  public DistrictQuestsResponse sideQuests(List<@Valid QuestNode> sideQuests) {
    this.sideQuests = sideQuests;
    return this;
  }

  public DistrictQuestsResponse addSideQuestsItem(QuestNode sideQuestsItem) {
    if (this.sideQuests == null) {
      this.sideQuests = new ArrayList<>();
    }
    this.sideQuests.add(sideQuestsItem);
    return this;
  }

  /**
   * Get sideQuests
   * @return sideQuests
   */
  @Valid 
  @Schema(name = "side_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("side_quests")
  public List<@Valid QuestNode> getSideQuests() {
    return sideQuests;
  }

  public void setSideQuests(List<@Valid QuestNode> sideQuests) {
    this.sideQuests = sideQuests;
  }

  public DistrictQuestsResponse gigs(List<@Valid QuestNode> gigs) {
    this.gigs = gigs;
    return this;
  }

  public DistrictQuestsResponse addGigsItem(QuestNode gigsItem) {
    if (this.gigs == null) {
      this.gigs = new ArrayList<>();
    }
    this.gigs.add(gigsItem);
    return this;
  }

  /**
   * Get gigs
   * @return gigs
   */
  @Valid 
  @Schema(name = "gigs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gigs")
  public List<@Valid QuestNode> getGigs() {
    return gigs;
  }

  public void setGigs(List<@Valid QuestNode> gigs) {
    this.gigs = gigs;
  }

  public DistrictQuestsResponse ncpdScanners(List<@Valid DistrictQuestsResponseNcpdScannersInner> ncpdScanners) {
    this.ncpdScanners = ncpdScanners;
    return this;
  }

  public DistrictQuestsResponse addNcpdScannersItem(DistrictQuestsResponseNcpdScannersInner ncpdScannersItem) {
    if (this.ncpdScanners == null) {
      this.ncpdScanners = new ArrayList<>();
    }
    this.ncpdScanners.add(ncpdScannersItem);
    return this;
  }

  /**
   * Get ncpdScanners
   * @return ncpdScanners
   */
  @Valid 
  @Schema(name = "ncpd_scanners", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ncpd_scanners")
  public List<@Valid DistrictQuestsResponseNcpdScannersInner> getNcpdScanners() {
    return ncpdScanners;
  }

  public void setNcpdScanners(List<@Valid DistrictQuestsResponseNcpdScannersInner> ncpdScanners) {
    this.ncpdScanners = ncpdScanners;
  }

  public DistrictQuestsResponse totalQuests(@Nullable Integer totalQuests) {
    this.totalQuests = totalQuests;
    return this;
  }

  /**
   * Get totalQuests
   * @return totalQuests
   */
  
  @Schema(name = "total_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_quests")
  public @Nullable Integer getTotalQuests() {
    return totalQuests;
  }

  public void setTotalQuests(@Nullable Integer totalQuests) {
    this.totalQuests = totalQuests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DistrictQuestsResponse districtQuestsResponse = (DistrictQuestsResponse) o;
    return Objects.equals(this.districtId, districtQuestsResponse.districtId) &&
        Objects.equals(this.districtName, districtQuestsResponse.districtName) &&
        Objects.equals(this.region, districtQuestsResponse.region) &&
        Objects.equals(this.mainQuests, districtQuestsResponse.mainQuests) &&
        Objects.equals(this.sideQuests, districtQuestsResponse.sideQuests) &&
        Objects.equals(this.gigs, districtQuestsResponse.gigs) &&
        Objects.equals(this.ncpdScanners, districtQuestsResponse.ncpdScanners) &&
        Objects.equals(this.totalQuests, districtQuestsResponse.totalQuests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(districtId, districtName, region, mainQuests, sideQuests, gigs, ncpdScanners, totalQuests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DistrictQuestsResponse {\n");
    sb.append("    districtId: ").append(toIndentedString(districtId)).append("\n");
    sb.append("    districtName: ").append(toIndentedString(districtName)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    mainQuests: ").append(toIndentedString(mainQuests)).append("\n");
    sb.append("    sideQuests: ").append(toIndentedString(sideQuests)).append("\n");
    sb.append("    gigs: ").append(toIndentedString(gigs)).append("\n");
    sb.append("    ncpdScanners: ").append(toIndentedString(ncpdScanners)).append("\n");
    sb.append("    totalQuests: ").append(toIndentedString(totalQuests)).append("\n");
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

