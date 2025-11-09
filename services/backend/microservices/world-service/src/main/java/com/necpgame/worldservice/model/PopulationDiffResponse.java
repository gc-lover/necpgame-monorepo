package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.PopulationDiff;
import java.time.OffsetDateTime;
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
 * PopulationDiffResponse
 */


public class PopulationDiffResponse {

  private UUID cityId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime baselineTimestamp;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime currentTimestamp;

  private PopulationDiff diff;

  public PopulationDiffResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PopulationDiffResponse(UUID cityId, PopulationDiff diff) {
    this.cityId = cityId;
    this.diff = diff;
  }

  public PopulationDiffResponse cityId(UUID cityId) {
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

  public PopulationDiffResponse baselineTimestamp(@Nullable OffsetDateTime baselineTimestamp) {
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

  public PopulationDiffResponse currentTimestamp(@Nullable OffsetDateTime currentTimestamp) {
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

  public PopulationDiffResponse diff(PopulationDiff diff) {
    this.diff = diff;
    return this;
  }

  /**
   * Get diff
   * @return diff
   */
  @NotNull @Valid 
  @Schema(name = "diff", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("diff")
  public PopulationDiff getDiff() {
    return diff;
  }

  public void setDiff(PopulationDiff diff) {
    this.diff = diff;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PopulationDiffResponse populationDiffResponse = (PopulationDiffResponse) o;
    return Objects.equals(this.cityId, populationDiffResponse.cityId) &&
        Objects.equals(this.baselineTimestamp, populationDiffResponse.baselineTimestamp) &&
        Objects.equals(this.currentTimestamp, populationDiffResponse.currentTimestamp) &&
        Objects.equals(this.diff, populationDiffResponse.diff);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cityId, baselineTimestamp, currentTimestamp, diff);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PopulationDiffResponse {\n");
    sb.append("    cityId: ").append(toIndentedString(cityId)).append("\n");
    sb.append("    baselineTimestamp: ").append(toIndentedString(baselineTimestamp)).append("\n");
    sb.append("    currentTimestamp: ").append(toIndentedString(currentTimestamp)).append("\n");
    sb.append("    diff: ").append(toIndentedString(diff)).append("\n");
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

