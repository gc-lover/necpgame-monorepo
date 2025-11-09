package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.DistrictInfrastructureSummary;
import com.necpgame.economyservice.model.InfrastructureMetric;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * InfrastructureSummaryResponse
 */


public class InfrastructureSummaryResponse {

  private UUID cityId;

  @Valid
  private List<@Valid InfrastructureMetric> metrics = new ArrayList<>();

  @Valid
  private List<@Valid DistrictInfrastructureSummary> districts = new ArrayList<>();

  public InfrastructureSummaryResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InfrastructureSummaryResponse(UUID cityId, List<@Valid DistrictInfrastructureSummary> districts) {
    this.cityId = cityId;
    this.districts = districts;
  }

  public InfrastructureSummaryResponse cityId(UUID cityId) {
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

  public InfrastructureSummaryResponse metrics(List<@Valid InfrastructureMetric> metrics) {
    this.metrics = metrics;
    return this;
  }

  public InfrastructureSummaryResponse addMetricsItem(InfrastructureMetric metricsItem) {
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
  public List<@Valid InfrastructureMetric> getMetrics() {
    return metrics;
  }

  public void setMetrics(List<@Valid InfrastructureMetric> metrics) {
    this.metrics = metrics;
  }

  public InfrastructureSummaryResponse districts(List<@Valid DistrictInfrastructureSummary> districts) {
    this.districts = districts;
    return this;
  }

  public InfrastructureSummaryResponse addDistrictsItem(DistrictInfrastructureSummary districtsItem) {
    if (this.districts == null) {
      this.districts = new ArrayList<>();
    }
    this.districts.add(districtsItem);
    return this;
  }

  /**
   * Get districts
   * @return districts
   */
  @NotNull @Valid 
  @Schema(name = "districts", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("districts")
  public List<@Valid DistrictInfrastructureSummary> getDistricts() {
    return districts;
  }

  public void setDistricts(List<@Valid DistrictInfrastructureSummary> districts) {
    this.districts = districts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InfrastructureSummaryResponse infrastructureSummaryResponse = (InfrastructureSummaryResponse) o;
    return Objects.equals(this.cityId, infrastructureSummaryResponse.cityId) &&
        Objects.equals(this.metrics, infrastructureSummaryResponse.metrics) &&
        Objects.equals(this.districts, infrastructureSummaryResponse.districts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cityId, metrics, districts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InfrastructureSummaryResponse {\n");
    sb.append("    cityId: ").append(toIndentedString(cityId)).append("\n");
    sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
    sb.append("    districts: ").append(toIndentedString(districts)).append("\n");
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

