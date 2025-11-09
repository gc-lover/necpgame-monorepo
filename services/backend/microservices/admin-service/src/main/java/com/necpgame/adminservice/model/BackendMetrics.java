package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
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
 * BackendMetrics
 */


public class BackendMetrics {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  private @Nullable Integer averageCodeCoverage;

  private @Nullable Integer averagePerformanceScore;

  private @Nullable Integer totalApiEndpoints;

  private @Nullable Integer averageResponseTime;

  private @Nullable Float errorRate;

  private @Nullable Float uptime;

  public BackendMetrics timestamp(@Nullable OffsetDateTime timestamp) {
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

  public BackendMetrics averageCodeCoverage(@Nullable Integer averageCodeCoverage) {
    this.averageCodeCoverage = averageCodeCoverage;
    return this;
  }

  /**
   * Средний code coverage %
   * @return averageCodeCoverage
   */
  
  @Schema(name = "average_code_coverage", example = "87", description = "Средний code coverage %", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("average_code_coverage")
  public @Nullable Integer getAverageCodeCoverage() {
    return averageCodeCoverage;
  }

  public void setAverageCodeCoverage(@Nullable Integer averageCodeCoverage) {
    this.averageCodeCoverage = averageCodeCoverage;
  }

  public BackendMetrics averagePerformanceScore(@Nullable Integer averagePerformanceScore) {
    this.averagePerformanceScore = averagePerformanceScore;
    return this;
  }

  /**
   * Get averagePerformanceScore
   * @return averagePerformanceScore
   */
  
  @Schema(name = "average_performance_score", example = "90", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("average_performance_score")
  public @Nullable Integer getAveragePerformanceScore() {
    return averagePerformanceScore;
  }

  public void setAveragePerformanceScore(@Nullable Integer averagePerformanceScore) {
    this.averagePerformanceScore = averagePerformanceScore;
  }

  public BackendMetrics totalApiEndpoints(@Nullable Integer totalApiEndpoints) {
    this.totalApiEndpoints = totalApiEndpoints;
    return this;
  }

  /**
   * Get totalApiEndpoints
   * @return totalApiEndpoints
   */
  
  @Schema(name = "total_api_endpoints", example = "295", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_api_endpoints")
  public @Nullable Integer getTotalApiEndpoints() {
    return totalApiEndpoints;
  }

  public void setTotalApiEndpoints(@Nullable Integer totalApiEndpoints) {
    this.totalApiEndpoints = totalApiEndpoints;
  }

  public BackendMetrics averageResponseTime(@Nullable Integer averageResponseTime) {
    this.averageResponseTime = averageResponseTime;
    return this;
  }

  /**
   * Среднее время ответа (ms)
   * @return averageResponseTime
   */
  
  @Schema(name = "average_response_time", example = "120", description = "Среднее время ответа (ms)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("average_response_time")
  public @Nullable Integer getAverageResponseTime() {
    return averageResponseTime;
  }

  public void setAverageResponseTime(@Nullable Integer averageResponseTime) {
    this.averageResponseTime = averageResponseTime;
  }

  public BackendMetrics errorRate(@Nullable Float errorRate) {
    this.errorRate = errorRate;
    return this;
  }

  /**
   * Error rate %
   * @return errorRate
   */
  
  @Schema(name = "error_rate", example = "0.5", description = "Error rate %", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("error_rate")
  public @Nullable Float getErrorRate() {
    return errorRate;
  }

  public void setErrorRate(@Nullable Float errorRate) {
    this.errorRate = errorRate;
  }

  public BackendMetrics uptime(@Nullable Float uptime) {
    this.uptime = uptime;
    return this;
  }

  /**
   * Uptime %
   * @return uptime
   */
  
  @Schema(name = "uptime", example = "99.95", description = "Uptime %", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uptime")
  public @Nullable Float getUptime() {
    return uptime;
  }

  public void setUptime(@Nullable Float uptime) {
    this.uptime = uptime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BackendMetrics backendMetrics = (BackendMetrics) o;
    return Objects.equals(this.timestamp, backendMetrics.timestamp) &&
        Objects.equals(this.averageCodeCoverage, backendMetrics.averageCodeCoverage) &&
        Objects.equals(this.averagePerformanceScore, backendMetrics.averagePerformanceScore) &&
        Objects.equals(this.totalApiEndpoints, backendMetrics.totalApiEndpoints) &&
        Objects.equals(this.averageResponseTime, backendMetrics.averageResponseTime) &&
        Objects.equals(this.errorRate, backendMetrics.errorRate) &&
        Objects.equals(this.uptime, backendMetrics.uptime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, averageCodeCoverage, averagePerformanceScore, totalApiEndpoints, averageResponseTime, errorRate, uptime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BackendMetrics {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    averageCodeCoverage: ").append(toIndentedString(averageCodeCoverage)).append("\n");
    sb.append("    averagePerformanceScore: ").append(toIndentedString(averagePerformanceScore)).append("\n");
    sb.append("    totalApiEndpoints: ").append(toIndentedString(totalApiEndpoints)).append("\n");
    sb.append("    averageResponseTime: ").append(toIndentedString(averageResponseTime)).append("\n");
    sb.append("    errorRate: ").append(toIndentedString(errorRate)).append("\n");
    sb.append("    uptime: ").append(toIndentedString(uptime)).append("\n");
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

