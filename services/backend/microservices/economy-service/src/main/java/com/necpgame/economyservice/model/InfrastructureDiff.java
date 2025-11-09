package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.DistrictInfrastructureSummary;
import com.necpgame.economyservice.model.InfrastructureInstanceChange;
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
 * InfrastructureDiff
 */


public class InfrastructureDiff {

  private UUID districtId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime baselineTimestamp;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime currentTimestamp;

  private DistrictInfrastructureSummary baseline;

  private DistrictInfrastructureSummary current;

  @Valid
  private List<@Valid InfrastructureInstanceChange> changedInstances = new ArrayList<>();

  public InfrastructureDiff() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InfrastructureDiff(UUID districtId, DistrictInfrastructureSummary baseline, DistrictInfrastructureSummary current) {
    this.districtId = districtId;
    this.baseline = baseline;
    this.current = current;
  }

  public InfrastructureDiff districtId(UUID districtId) {
    this.districtId = districtId;
    return this;
  }

  /**
   * Get districtId
   * @return districtId
   */
  @NotNull @Valid 
  @Schema(name = "districtId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("districtId")
  public UUID getDistrictId() {
    return districtId;
  }

  public void setDistrictId(UUID districtId) {
    this.districtId = districtId;
  }

  public InfrastructureDiff baselineTimestamp(@Nullable OffsetDateTime baselineTimestamp) {
    this.baselineTimestamp = baselineTimestamp;
    return this;
  }

  /**
   * Get baselineTimestamp
   * @return baselineTimestamp
   */
  @Valid 
  @Schema(name = "baselineTimestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("baselineTimestamp")
  public @Nullable OffsetDateTime getBaselineTimestamp() {
    return baselineTimestamp;
  }

  public void setBaselineTimestamp(@Nullable OffsetDateTime baselineTimestamp) {
    this.baselineTimestamp = baselineTimestamp;
  }

  public InfrastructureDiff currentTimestamp(@Nullable OffsetDateTime currentTimestamp) {
    this.currentTimestamp = currentTimestamp;
    return this;
  }

  /**
   * Get currentTimestamp
   * @return currentTimestamp
   */
  @Valid 
  @Schema(name = "currentTimestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currentTimestamp")
  public @Nullable OffsetDateTime getCurrentTimestamp() {
    return currentTimestamp;
  }

  public void setCurrentTimestamp(@Nullable OffsetDateTime currentTimestamp) {
    this.currentTimestamp = currentTimestamp;
  }

  public InfrastructureDiff baseline(DistrictInfrastructureSummary baseline) {
    this.baseline = baseline;
    return this;
  }

  /**
   * Get baseline
   * @return baseline
   */
  @NotNull @Valid 
  @Schema(name = "baseline", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("baseline")
  public DistrictInfrastructureSummary getBaseline() {
    return baseline;
  }

  public void setBaseline(DistrictInfrastructureSummary baseline) {
    this.baseline = baseline;
  }

  public InfrastructureDiff current(DistrictInfrastructureSummary current) {
    this.current = current;
    return this;
  }

  /**
   * Get current
   * @return current
   */
  @NotNull @Valid 
  @Schema(name = "current", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("current")
  public DistrictInfrastructureSummary getCurrent() {
    return current;
  }

  public void setCurrent(DistrictInfrastructureSummary current) {
    this.current = current;
  }

  public InfrastructureDiff changedInstances(List<@Valid InfrastructureInstanceChange> changedInstances) {
    this.changedInstances = changedInstances;
    return this;
  }

  public InfrastructureDiff addChangedInstancesItem(InfrastructureInstanceChange changedInstancesItem) {
    if (this.changedInstances == null) {
      this.changedInstances = new ArrayList<>();
    }
    this.changedInstances.add(changedInstancesItem);
    return this;
  }

  /**
   * Get changedInstances
   * @return changedInstances
   */
  @Valid 
  @Schema(name = "changedInstances", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("changedInstances")
  public List<@Valid InfrastructureInstanceChange> getChangedInstances() {
    return changedInstances;
  }

  public void setChangedInstances(List<@Valid InfrastructureInstanceChange> changedInstances) {
    this.changedInstances = changedInstances;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InfrastructureDiff infrastructureDiff = (InfrastructureDiff) o;
    return Objects.equals(this.districtId, infrastructureDiff.districtId) &&
        Objects.equals(this.baselineTimestamp, infrastructureDiff.baselineTimestamp) &&
        Objects.equals(this.currentTimestamp, infrastructureDiff.currentTimestamp) &&
        Objects.equals(this.baseline, infrastructureDiff.baseline) &&
        Objects.equals(this.current, infrastructureDiff.current) &&
        Objects.equals(this.changedInstances, infrastructureDiff.changedInstances);
  }

  @Override
  public int hashCode() {
    return Objects.hash(districtId, baselineTimestamp, currentTimestamp, baseline, current, changedInstances);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InfrastructureDiff {\n");
    sb.append("    districtId: ").append(toIndentedString(districtId)).append("\n");
    sb.append("    baselineTimestamp: ").append(toIndentedString(baselineTimestamp)).append("\n");
    sb.append("    currentTimestamp: ").append(toIndentedString(currentTimestamp)).append("\n");
    sb.append("    baseline: ").append(toIndentedString(baseline)).append("\n");
    sb.append("    current: ").append(toIndentedString(current)).append("\n");
    sb.append("    changedInstances: ").append(toIndentedString(changedInstances)).append("\n");
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

