package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.DistrictPopulationState;
import com.necpgame.worldservice.model.PopulationDiff;
import com.necpgame.worldservice.model.PopulationMetric;
import java.math.BigDecimal;
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
 * CityPopulationProfile
 */


public class CityPopulationProfile {

  private UUID cityId;

  private @Nullable String cityName;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private Integer populationTotal;

  private BigDecimal capacityUsage;

  private BigDecimal densityScore;

  private @Nullable BigDecimal energyLoad;

  private @Nullable BigDecimal eventPressure;

  private @Nullable Integer populationTarget;

  @Valid
  private List<@Valid DistrictPopulationState> segments = new ArrayList<>();

  @Valid
  private List<@Valid PopulationMetric> metrics = new ArrayList<>();

  private @Nullable PopulationDiff diff;

  public CityPopulationProfile() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CityPopulationProfile(UUID cityId, OffsetDateTime timestamp, Integer populationTotal, BigDecimal capacityUsage, BigDecimal densityScore, List<@Valid DistrictPopulationState> segments) {
    this.cityId = cityId;
    this.timestamp = timestamp;
    this.populationTotal = populationTotal;
    this.capacityUsage = capacityUsage;
    this.densityScore = densityScore;
    this.segments = segments;
  }

  public CityPopulationProfile cityId(UUID cityId) {
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

  public CityPopulationProfile cityName(@Nullable String cityName) {
    this.cityName = cityName;
    return this;
  }

  /**
   * Get cityName
   * @return cityName
   */
  
  @Schema(name = "cityName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cityName")
  public @Nullable String getCityName() {
    return cityName;
  }

  public void setCityName(@Nullable String cityName) {
    this.cityName = cityName;
  }

  public CityPopulationProfile timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @NotNull @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public CityPopulationProfile populationTotal(Integer populationTotal) {
    this.populationTotal = populationTotal;
    return this;
  }

  /**
   * Get populationTotal
   * minimum: 0
   * @return populationTotal
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "populationTotal", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("populationTotal")
  public Integer getPopulationTotal() {
    return populationTotal;
  }

  public void setPopulationTotal(Integer populationTotal) {
    this.populationTotal = populationTotal;
  }

  public CityPopulationProfile capacityUsage(BigDecimal capacityUsage) {
    this.capacityUsage = capacityUsage;
    return this;
  }

  /**
   * Get capacityUsage
   * minimum: 0
   * maximum: 1
   * @return capacityUsage
   */
  @NotNull @Valid @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "capacityUsage", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("capacityUsage")
  public BigDecimal getCapacityUsage() {
    return capacityUsage;
  }

  public void setCapacityUsage(BigDecimal capacityUsage) {
    this.capacityUsage = capacityUsage;
  }

  public CityPopulationProfile densityScore(BigDecimal densityScore) {
    this.densityScore = densityScore;
    return this;
  }

  /**
   * Get densityScore
   * minimum: 0
   * maximum: 1
   * @return densityScore
   */
  @NotNull @Valid @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "densityScore", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("densityScore")
  public BigDecimal getDensityScore() {
    return densityScore;
  }

  public void setDensityScore(BigDecimal densityScore) {
    this.densityScore = densityScore;
  }

  public CityPopulationProfile energyLoad(@Nullable BigDecimal energyLoad) {
    this.energyLoad = energyLoad;
    return this;
  }

  /**
   * Get energyLoad
   * @return energyLoad
   */
  @Valid 
  @Schema(name = "energyLoad", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energyLoad")
  public @Nullable BigDecimal getEnergyLoad() {
    return energyLoad;
  }

  public void setEnergyLoad(@Nullable BigDecimal energyLoad) {
    this.energyLoad = energyLoad;
  }

  public CityPopulationProfile eventPressure(@Nullable BigDecimal eventPressure) {
    this.eventPressure = eventPressure;
    return this;
  }

  /**
   * Get eventPressure
   * @return eventPressure
   */
  @Valid 
  @Schema(name = "eventPressure", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eventPressure")
  public @Nullable BigDecimal getEventPressure() {
    return eventPressure;
  }

  public void setEventPressure(@Nullable BigDecimal eventPressure) {
    this.eventPressure = eventPressure;
  }

  public CityPopulationProfile populationTarget(@Nullable Integer populationTarget) {
    this.populationTarget = populationTarget;
    return this;
  }

  /**
   * Get populationTarget
   * @return populationTarget
   */
  
  @Schema(name = "populationTarget", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("populationTarget")
  public @Nullable Integer getPopulationTarget() {
    return populationTarget;
  }

  public void setPopulationTarget(@Nullable Integer populationTarget) {
    this.populationTarget = populationTarget;
  }

  public CityPopulationProfile segments(List<@Valid DistrictPopulationState> segments) {
    this.segments = segments;
    return this;
  }

  public CityPopulationProfile addSegmentsItem(DistrictPopulationState segmentsItem) {
    if (this.segments == null) {
      this.segments = new ArrayList<>();
    }
    this.segments.add(segmentsItem);
    return this;
  }

  /**
   * Get segments
   * @return segments
   */
  @NotNull @Valid 
  @Schema(name = "segments", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("segments")
  public List<@Valid DistrictPopulationState> getSegments() {
    return segments;
  }

  public void setSegments(List<@Valid DistrictPopulationState> segments) {
    this.segments = segments;
  }

  public CityPopulationProfile metrics(List<@Valid PopulationMetric> metrics) {
    this.metrics = metrics;
    return this;
  }

  public CityPopulationProfile addMetricsItem(PopulationMetric metricsItem) {
    if (this.metrics == null) {
      this.metrics = new ArrayList<>();
    }
    this.metrics.add(metricsItem);
    return this;
  }

  /**
   * Get metrics
   * @return metrics
   */
  @Valid 
  @Schema(name = "metrics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metrics")
  public List<@Valid PopulationMetric> getMetrics() {
    return metrics;
  }

  public void setMetrics(List<@Valid PopulationMetric> metrics) {
    this.metrics = metrics;
  }

  public CityPopulationProfile diff(@Nullable PopulationDiff diff) {
    this.diff = diff;
    return this;
  }

  /**
   * Get diff
   * @return diff
   */
  @Valid 
  @Schema(name = "diff", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("diff")
  public @Nullable PopulationDiff getDiff() {
    return diff;
  }

  public void setDiff(@Nullable PopulationDiff diff) {
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
    CityPopulationProfile cityPopulationProfile = (CityPopulationProfile) o;
    return Objects.equals(this.cityId, cityPopulationProfile.cityId) &&
        Objects.equals(this.cityName, cityPopulationProfile.cityName) &&
        Objects.equals(this.timestamp, cityPopulationProfile.timestamp) &&
        Objects.equals(this.populationTotal, cityPopulationProfile.populationTotal) &&
        Objects.equals(this.capacityUsage, cityPopulationProfile.capacityUsage) &&
        Objects.equals(this.densityScore, cityPopulationProfile.densityScore) &&
        Objects.equals(this.energyLoad, cityPopulationProfile.energyLoad) &&
        Objects.equals(this.eventPressure, cityPopulationProfile.eventPressure) &&
        Objects.equals(this.populationTarget, cityPopulationProfile.populationTarget) &&
        Objects.equals(this.segments, cityPopulationProfile.segments) &&
        Objects.equals(this.metrics, cityPopulationProfile.metrics) &&
        Objects.equals(this.diff, cityPopulationProfile.diff);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cityId, cityName, timestamp, populationTotal, capacityUsage, densityScore, energyLoad, eventPressure, populationTarget, segments, metrics, diff);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CityPopulationProfile {\n");
    sb.append("    cityId: ").append(toIndentedString(cityId)).append("\n");
    sb.append("    cityName: ").append(toIndentedString(cityName)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    populationTotal: ").append(toIndentedString(populationTotal)).append("\n");
    sb.append("    capacityUsage: ").append(toIndentedString(capacityUsage)).append("\n");
    sb.append("    densityScore: ").append(toIndentedString(densityScore)).append("\n");
    sb.append("    energyLoad: ").append(toIndentedString(energyLoad)).append("\n");
    sb.append("    eventPressure: ").append(toIndentedString(eventPressure)).append("\n");
    sb.append("    populationTarget: ").append(toIndentedString(populationTarget)).append("\n");
    sb.append("    segments: ").append(toIndentedString(segments)).append("\n");
    sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
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

