package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.DistrictChange;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PopulationDiff
 */


public class PopulationDiff {

  private UUID cityId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime baselineTimestamp;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime currentTimestamp;

  private Integer npcDelta;

  private Integer capacityDelta;

  @Valid
  private List<@Valid DistrictChange> districtChanges = new ArrayList<>();

  public PopulationDiff() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PopulationDiff(UUID cityId, OffsetDateTime baselineTimestamp, OffsetDateTime currentTimestamp, Integer npcDelta, Integer capacityDelta, List<@Valid DistrictChange> districtChanges) {
    this.cityId = cityId;
    this.baselineTimestamp = baselineTimestamp;
    this.currentTimestamp = currentTimestamp;
    this.npcDelta = npcDelta;
    this.capacityDelta = capacityDelta;
    this.districtChanges = districtChanges;
  }

  public PopulationDiff cityId(UUID cityId) {
    this.cityId = cityId;
    return this;
  }

  /**
   * Get cityId
   * @return cityId
   */
  @NotNull @Valid 
  @Schema(name = "cityId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cityId")
  public UUID getCityId() {
    return cityId;
  }

  public void setCityId(UUID cityId) {
    this.cityId = cityId;
  }

  public PopulationDiff baselineTimestamp(OffsetDateTime baselineTimestamp) {
    this.baselineTimestamp = baselineTimestamp;
    return this;
  }

  /**
   * Get baselineTimestamp
   * @return baselineTimestamp
   */
  @NotNull @Valid 
  @Schema(name = "baselineTimestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("baselineTimestamp")
  public OffsetDateTime getBaselineTimestamp() {
    return baselineTimestamp;
  }

  public void setBaselineTimestamp(OffsetDateTime baselineTimestamp) {
    this.baselineTimestamp = baselineTimestamp;
  }

  public PopulationDiff currentTimestamp(OffsetDateTime currentTimestamp) {
    this.currentTimestamp = currentTimestamp;
    return this;
  }

  /**
   * Get currentTimestamp
   * @return currentTimestamp
   */
  @NotNull @Valid 
  @Schema(name = "currentTimestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("currentTimestamp")
  public OffsetDateTime getCurrentTimestamp() {
    return currentTimestamp;
  }

  public void setCurrentTimestamp(OffsetDateTime currentTimestamp) {
    this.currentTimestamp = currentTimestamp;
  }

  public PopulationDiff npcDelta(Integer npcDelta) {
    this.npcDelta = npcDelta;
    return this;
  }

  /**
   * Get npcDelta
   * @return npcDelta
   */
  @NotNull 
  @Schema(name = "npcDelta", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("npcDelta")
  public Integer getNpcDelta() {
    return npcDelta;
  }

  public void setNpcDelta(Integer npcDelta) {
    this.npcDelta = npcDelta;
  }

  public PopulationDiff capacityDelta(Integer capacityDelta) {
    this.capacityDelta = capacityDelta;
    return this;
  }

  /**
   * Get capacityDelta
   * @return capacityDelta
   */
  @NotNull 
  @Schema(name = "capacityDelta", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("capacityDelta")
  public Integer getCapacityDelta() {
    return capacityDelta;
  }

  public void setCapacityDelta(Integer capacityDelta) {
    this.capacityDelta = capacityDelta;
  }

  public PopulationDiff districtChanges(List<@Valid DistrictChange> districtChanges) {
    this.districtChanges = districtChanges;
    return this;
  }

  public PopulationDiff addDistrictChangesItem(DistrictChange districtChangesItem) {
    if (this.districtChanges == null) {
      this.districtChanges = new ArrayList<>();
    }
    this.districtChanges.add(districtChangesItem);
    return this;
  }

  /**
   * Get districtChanges
   * @return districtChanges
   */
  @NotNull @Valid 
  @Schema(name = "districtChanges", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("districtChanges")
  public List<@Valid DistrictChange> getDistrictChanges() {
    return districtChanges;
  }

  public void setDistrictChanges(List<@Valid DistrictChange> districtChanges) {
    this.districtChanges = districtChanges;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PopulationDiff populationDiff = (PopulationDiff) o;
    return Objects.equals(this.cityId, populationDiff.cityId) &&
        Objects.equals(this.baselineTimestamp, populationDiff.baselineTimestamp) &&
        Objects.equals(this.currentTimestamp, populationDiff.currentTimestamp) &&
        Objects.equals(this.npcDelta, populationDiff.npcDelta) &&
        Objects.equals(this.capacityDelta, populationDiff.capacityDelta) &&
        Objects.equals(this.districtChanges, populationDiff.districtChanges);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cityId, baselineTimestamp, currentTimestamp, npcDelta, capacityDelta, districtChanges);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PopulationDiff {\n");
    sb.append("    cityId: ").append(toIndentedString(cityId)).append("\n");
    sb.append("    baselineTimestamp: ").append(toIndentedString(baselineTimestamp)).append("\n");
    sb.append("    currentTimestamp: ").append(toIndentedString(currentTimestamp)).append("\n");
    sb.append("    npcDelta: ").append(toIndentedString(npcDelta)).append("\n");
    sb.append("    capacityDelta: ").append(toIndentedString(capacityDelta)).append("\n");
    sb.append("    districtChanges: ").append(toIndentedString(districtChanges)).append("\n");
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

