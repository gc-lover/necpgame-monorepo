package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.GatewayMetricsTopRoutesInner;
import java.math.BigDecimal;
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
 * GatewayMetrics
 */


public class GatewayMetrics {

  private @Nullable Integer totalRequests;

  private @Nullable Integer successfulRequests;

  private @Nullable Integer failedRequests;

  private @Nullable Integer averageLatencyMs;

  private @Nullable BigDecimal requestsPerSecond;

  @Valid
  private List<@Valid GatewayMetricsTopRoutesInner> topRoutes = new ArrayList<>();

  public GatewayMetrics totalRequests(@Nullable Integer totalRequests) {
    this.totalRequests = totalRequests;
    return this;
  }

  /**
   * Get totalRequests
   * @return totalRequests
   */
  
  @Schema(name = "total_requests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_requests")
  public @Nullable Integer getTotalRequests() {
    return totalRequests;
  }

  public void setTotalRequests(@Nullable Integer totalRequests) {
    this.totalRequests = totalRequests;
  }

  public GatewayMetrics successfulRequests(@Nullable Integer successfulRequests) {
    this.successfulRequests = successfulRequests;
    return this;
  }

  /**
   * Get successfulRequests
   * @return successfulRequests
   */
  
  @Schema(name = "successful_requests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("successful_requests")
  public @Nullable Integer getSuccessfulRequests() {
    return successfulRequests;
  }

  public void setSuccessfulRequests(@Nullable Integer successfulRequests) {
    this.successfulRequests = successfulRequests;
  }

  public GatewayMetrics failedRequests(@Nullable Integer failedRequests) {
    this.failedRequests = failedRequests;
    return this;
  }

  /**
   * Get failedRequests
   * @return failedRequests
   */
  
  @Schema(name = "failed_requests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("failed_requests")
  public @Nullable Integer getFailedRequests() {
    return failedRequests;
  }

  public void setFailedRequests(@Nullable Integer failedRequests) {
    this.failedRequests = failedRequests;
  }

  public GatewayMetrics averageLatencyMs(@Nullable Integer averageLatencyMs) {
    this.averageLatencyMs = averageLatencyMs;
    return this;
  }

  /**
   * Get averageLatencyMs
   * @return averageLatencyMs
   */
  
  @Schema(name = "average_latency_ms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("average_latency_ms")
  public @Nullable Integer getAverageLatencyMs() {
    return averageLatencyMs;
  }

  public void setAverageLatencyMs(@Nullable Integer averageLatencyMs) {
    this.averageLatencyMs = averageLatencyMs;
  }

  public GatewayMetrics requestsPerSecond(@Nullable BigDecimal requestsPerSecond) {
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

  public GatewayMetrics topRoutes(List<@Valid GatewayMetricsTopRoutesInner> topRoutes) {
    this.topRoutes = topRoutes;
    return this;
  }

  public GatewayMetrics addTopRoutesItem(GatewayMetricsTopRoutesInner topRoutesItem) {
    if (this.topRoutes == null) {
      this.topRoutes = new ArrayList<>();
    }
    this.topRoutes.add(topRoutesItem);
    return this;
  }

  /**
   * Get topRoutes
   * @return topRoutes
   */
  @Valid 
  @Schema(name = "top_routes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("top_routes")
  public List<@Valid GatewayMetricsTopRoutesInner> getTopRoutes() {
    return topRoutes;
  }

  public void setTopRoutes(List<@Valid GatewayMetricsTopRoutesInner> topRoutes) {
    this.topRoutes = topRoutes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GatewayMetrics gatewayMetrics = (GatewayMetrics) o;
    return Objects.equals(this.totalRequests, gatewayMetrics.totalRequests) &&
        Objects.equals(this.successfulRequests, gatewayMetrics.successfulRequests) &&
        Objects.equals(this.failedRequests, gatewayMetrics.failedRequests) &&
        Objects.equals(this.averageLatencyMs, gatewayMetrics.averageLatencyMs) &&
        Objects.equals(this.requestsPerSecond, gatewayMetrics.requestsPerSecond) &&
        Objects.equals(this.topRoutes, gatewayMetrics.topRoutes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalRequests, successfulRequests, failedRequests, averageLatencyMs, requestsPerSecond, topRoutes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GatewayMetrics {\n");
    sb.append("    totalRequests: ").append(toIndentedString(totalRequests)).append("\n");
    sb.append("    successfulRequests: ").append(toIndentedString(successfulRequests)).append("\n");
    sb.append("    failedRequests: ").append(toIndentedString(failedRequests)).append("\n");
    sb.append("    averageLatencyMs: ").append(toIndentedString(averageLatencyMs)).append("\n");
    sb.append("    requestsPerSecond: ").append(toIndentedString(requestsPerSecond)).append("\n");
    sb.append("    topRoutes: ").append(toIndentedString(topRoutes)).append("\n");
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

