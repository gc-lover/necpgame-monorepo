package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EndpointLatencyStats
 */


public class EndpointLatencyStats {

  private @Nullable String endpoint;

  private @Nullable String method;

  private @Nullable Integer latencyP50Ms;

  private @Nullable Integer latencyP95Ms;

  private @Nullable Integer latencyP99Ms;

  private @Nullable Integer requestsCount;

  private @Nullable Integer errorCount;

  public EndpointLatencyStats endpoint(@Nullable String endpoint) {
    this.endpoint = endpoint;
    return this;
  }

  /**
   * Get endpoint
   * @return endpoint
   */
  
  @Schema(name = "endpoint", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endpoint")
  public @Nullable String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(@Nullable String endpoint) {
    this.endpoint = endpoint;
  }

  public EndpointLatencyStats method(@Nullable String method) {
    this.method = method;
    return this;
  }

  /**
   * Get method
   * @return method
   */
  
  @Schema(name = "method", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("method")
  public @Nullable String getMethod() {
    return method;
  }

  public void setMethod(@Nullable String method) {
    this.method = method;
  }

  public EndpointLatencyStats latencyP50Ms(@Nullable Integer latencyP50Ms) {
    this.latencyP50Ms = latencyP50Ms;
    return this;
  }

  /**
   * Get latencyP50Ms
   * @return latencyP50Ms
   */
  
  @Schema(name = "latency_p50_ms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("latency_p50_ms")
  public @Nullable Integer getLatencyP50Ms() {
    return latencyP50Ms;
  }

  public void setLatencyP50Ms(@Nullable Integer latencyP50Ms) {
    this.latencyP50Ms = latencyP50Ms;
  }

  public EndpointLatencyStats latencyP95Ms(@Nullable Integer latencyP95Ms) {
    this.latencyP95Ms = latencyP95Ms;
    return this;
  }

  /**
   * Get latencyP95Ms
   * @return latencyP95Ms
   */
  
  @Schema(name = "latency_p95_ms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("latency_p95_ms")
  public @Nullable Integer getLatencyP95Ms() {
    return latencyP95Ms;
  }

  public void setLatencyP95Ms(@Nullable Integer latencyP95Ms) {
    this.latencyP95Ms = latencyP95Ms;
  }

  public EndpointLatencyStats latencyP99Ms(@Nullable Integer latencyP99Ms) {
    this.latencyP99Ms = latencyP99Ms;
    return this;
  }

  /**
   * Get latencyP99Ms
   * @return latencyP99Ms
   */
  
  @Schema(name = "latency_p99_ms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("latency_p99_ms")
  public @Nullable Integer getLatencyP99Ms() {
    return latencyP99Ms;
  }

  public void setLatencyP99Ms(@Nullable Integer latencyP99Ms) {
    this.latencyP99Ms = latencyP99Ms;
  }

  public EndpointLatencyStats requestsCount(@Nullable Integer requestsCount) {
    this.requestsCount = requestsCount;
    return this;
  }

  /**
   * Get requestsCount
   * @return requestsCount
   */
  
  @Schema(name = "requests_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requests_count")
  public @Nullable Integer getRequestsCount() {
    return requestsCount;
  }

  public void setRequestsCount(@Nullable Integer requestsCount) {
    this.requestsCount = requestsCount;
  }

  public EndpointLatencyStats errorCount(@Nullable Integer errorCount) {
    this.errorCount = errorCount;
    return this;
  }

  /**
   * Get errorCount
   * @return errorCount
   */
  
  @Schema(name = "error_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("error_count")
  public @Nullable Integer getErrorCount() {
    return errorCount;
  }

  public void setErrorCount(@Nullable Integer errorCount) {
    this.errorCount = errorCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EndpointLatencyStats endpointLatencyStats = (EndpointLatencyStats) o;
    return Objects.equals(this.endpoint, endpointLatencyStats.endpoint) &&
        Objects.equals(this.method, endpointLatencyStats.method) &&
        Objects.equals(this.latencyP50Ms, endpointLatencyStats.latencyP50Ms) &&
        Objects.equals(this.latencyP95Ms, endpointLatencyStats.latencyP95Ms) &&
        Objects.equals(this.latencyP99Ms, endpointLatencyStats.latencyP99Ms) &&
        Objects.equals(this.requestsCount, endpointLatencyStats.requestsCount) &&
        Objects.equals(this.errorCount, endpointLatencyStats.errorCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endpoint, method, latencyP50Ms, latencyP95Ms, latencyP99Ms, requestsCount, errorCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EndpointLatencyStats {\n");
    sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
    sb.append("    method: ").append(toIndentedString(method)).append("\n");
    sb.append("    latencyP50Ms: ").append(toIndentedString(latencyP50Ms)).append("\n");
    sb.append("    latencyP95Ms: ").append(toIndentedString(latencyP95Ms)).append("\n");
    sb.append("    latencyP99Ms: ").append(toIndentedString(latencyP99Ms)).append("\n");
    sb.append("    requestsCount: ").append(toIndentedString(requestsCount)).append("\n");
    sb.append("    errorCount: ").append(toIndentedString(errorCount)).append("\n");
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

