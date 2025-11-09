package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PerformanceMetricsServicesInner
 */

@JsonTypeName("PerformanceMetrics_services_inner")

public class PerformanceMetricsServicesInner {

  private @Nullable String serviceName;

  private @Nullable BigDecimal cpuUsagePercent;

  private @Nullable Integer memoryUsageMb;

  private @Nullable BigDecimal requestsPerSecond;

  private @Nullable Integer averageResponseTimeMs;

  private @Nullable BigDecimal errorRatePercent;

  public PerformanceMetricsServicesInner serviceName(@Nullable String serviceName) {
    this.serviceName = serviceName;
    return this;
  }

  /**
   * Get serviceName
   * @return serviceName
   */
  
  @Schema(name = "service_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("service_name")
  public @Nullable String getServiceName() {
    return serviceName;
  }

  public void setServiceName(@Nullable String serviceName) {
    this.serviceName = serviceName;
  }

  public PerformanceMetricsServicesInner cpuUsagePercent(@Nullable BigDecimal cpuUsagePercent) {
    this.cpuUsagePercent = cpuUsagePercent;
    return this;
  }

  /**
   * Get cpuUsagePercent
   * @return cpuUsagePercent
   */
  @Valid 
  @Schema(name = "cpu_usage_percent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cpu_usage_percent")
  public @Nullable BigDecimal getCpuUsagePercent() {
    return cpuUsagePercent;
  }

  public void setCpuUsagePercent(@Nullable BigDecimal cpuUsagePercent) {
    this.cpuUsagePercent = cpuUsagePercent;
  }

  public PerformanceMetricsServicesInner memoryUsageMb(@Nullable Integer memoryUsageMb) {
    this.memoryUsageMb = memoryUsageMb;
    return this;
  }

  /**
   * Get memoryUsageMb
   * @return memoryUsageMb
   */
  
  @Schema(name = "memory_usage_mb", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("memory_usage_mb")
  public @Nullable Integer getMemoryUsageMb() {
    return memoryUsageMb;
  }

  public void setMemoryUsageMb(@Nullable Integer memoryUsageMb) {
    this.memoryUsageMb = memoryUsageMb;
  }

  public PerformanceMetricsServicesInner requestsPerSecond(@Nullable BigDecimal requestsPerSecond) {
    this.requestsPerSecond = requestsPerSecond;
    return this;
  }

  /**
   * Get requestsPerSecond
   * @return requestsPerSecond
   */
  @Valid 
  @Schema(name = "requests_per_second", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requests_per_second")
  public @Nullable BigDecimal getRequestsPerSecond() {
    return requestsPerSecond;
  }

  public void setRequestsPerSecond(@Nullable BigDecimal requestsPerSecond) {
    this.requestsPerSecond = requestsPerSecond;
  }

  public PerformanceMetricsServicesInner averageResponseTimeMs(@Nullable Integer averageResponseTimeMs) {
    this.averageResponseTimeMs = averageResponseTimeMs;
    return this;
  }

  /**
   * Get averageResponseTimeMs
   * @return averageResponseTimeMs
   */
  
  @Schema(name = "average_response_time_ms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("average_response_time_ms")
  public @Nullable Integer getAverageResponseTimeMs() {
    return averageResponseTimeMs;
  }

  public void setAverageResponseTimeMs(@Nullable Integer averageResponseTimeMs) {
    this.averageResponseTimeMs = averageResponseTimeMs;
  }

  public PerformanceMetricsServicesInner errorRatePercent(@Nullable BigDecimal errorRatePercent) {
    this.errorRatePercent = errorRatePercent;
    return this;
  }

  /**
   * Get errorRatePercent
   * @return errorRatePercent
   */
  @Valid 
  @Schema(name = "error_rate_percent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("error_rate_percent")
  public @Nullable BigDecimal getErrorRatePercent() {
    return errorRatePercent;
  }

  public void setErrorRatePercent(@Nullable BigDecimal errorRatePercent) {
    this.errorRatePercent = errorRatePercent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformanceMetricsServicesInner performanceMetricsServicesInner = (PerformanceMetricsServicesInner) o;
    return Objects.equals(this.serviceName, performanceMetricsServicesInner.serviceName) &&
        Objects.equals(this.cpuUsagePercent, performanceMetricsServicesInner.cpuUsagePercent) &&
        Objects.equals(this.memoryUsageMb, performanceMetricsServicesInner.memoryUsageMb) &&
        Objects.equals(this.requestsPerSecond, performanceMetricsServicesInner.requestsPerSecond) &&
        Objects.equals(this.averageResponseTimeMs, performanceMetricsServicesInner.averageResponseTimeMs) &&
        Objects.equals(this.errorRatePercent, performanceMetricsServicesInner.errorRatePercent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceName, cpuUsagePercent, memoryUsageMb, requestsPerSecond, averageResponseTimeMs, errorRatePercent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformanceMetricsServicesInner {\n");
    sb.append("    serviceName: ").append(toIndentedString(serviceName)).append("\n");
    sb.append("    cpuUsagePercent: ").append(toIndentedString(cpuUsagePercent)).append("\n");
    sb.append("    memoryUsageMb: ").append(toIndentedString(memoryUsageMb)).append("\n");
    sb.append("    requestsPerSecond: ").append(toIndentedString(requestsPerSecond)).append("\n");
    sb.append("    averageResponseTimeMs: ").append(toIndentedString(averageResponseTimeMs)).append("\n");
    sb.append("    errorRatePercent: ").append(toIndentedString(errorRatePercent)).append("\n");
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

