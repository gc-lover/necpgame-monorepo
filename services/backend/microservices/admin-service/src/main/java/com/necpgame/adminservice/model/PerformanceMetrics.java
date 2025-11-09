package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.PerformanceMetricsServicesInner;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * PerformanceMetrics
 */


public class PerformanceMetrics {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  @Valid
  private List<@Valid PerformanceMetricsServicesInner> services = new ArrayList<>();

  public PerformanceMetrics timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public PerformanceMetrics services(List<@Valid PerformanceMetricsServicesInner> services) {
    this.services = services;
    return this;
  }

  public PerformanceMetrics addServicesItem(PerformanceMetricsServicesInner servicesItem) {
    if (this.services == null) {
      this.services = new ArrayList<>();
    }
    this.services.add(servicesItem);
    return this;
  }

  /**
   * Get services
   * @return services
   */
  @Valid 
  @Schema(name = "services", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("services")
  public List<@Valid PerformanceMetricsServicesInner> getServices() {
    return services;
  }

  public void setServices(List<@Valid PerformanceMetricsServicesInner> services) {
    this.services = services;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformanceMetrics performanceMetrics = (PerformanceMetrics) o;
    return Objects.equals(this.timestamp, performanceMetrics.timestamp) &&
        Objects.equals(this.services, performanceMetrics.services);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, services);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformanceMetrics {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    services: ").append(toIndentedString(services)).append("\n");
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

