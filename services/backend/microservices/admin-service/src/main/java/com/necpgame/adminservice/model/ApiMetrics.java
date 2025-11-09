package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.ApiMetricsRequestsByEndpointInner;
import com.necpgame.adminservice.model.ApiMetricsTopEndpointsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ApiMetrics
 */


public class ApiMetrics {

  private @Nullable String timeRange;

  private @Nullable Integer totalRequests;

  private @Nullable Integer successfulRequests;

  private @Nullable Integer failedRequests;

  private @Nullable Integer averageResponseTime;

  @Valid
  private List<@Valid ApiMetricsRequestsByEndpointInner> requestsByEndpoint = new ArrayList<>();

  @Valid
  private Map<String, Integer> errorsByCode = new HashMap<>();

  @Valid
  private List<@Valid ApiMetricsTopEndpointsInner> topEndpoints = new ArrayList<>();

  public ApiMetrics timeRange(@Nullable String timeRange) {
    this.timeRange = timeRange;
    return this;
  }

  /**
   * Get timeRange
   * @return timeRange
   */
  
  @Schema(name = "time_range", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_range")
  public @Nullable String getTimeRange() {
    return timeRange;
  }

  public void setTimeRange(@Nullable String timeRange) {
    this.timeRange = timeRange;
  }

  public ApiMetrics totalRequests(@Nullable Integer totalRequests) {
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

  public ApiMetrics successfulRequests(@Nullable Integer successfulRequests) {
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

  public ApiMetrics failedRequests(@Nullable Integer failedRequests) {
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

  public ApiMetrics averageResponseTime(@Nullable Integer averageResponseTime) {
    this.averageResponseTime = averageResponseTime;
    return this;
  }

  /**
   * In milliseconds
   * @return averageResponseTime
   */
  
  @Schema(name = "average_response_time", description = "In milliseconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("average_response_time")
  public @Nullable Integer getAverageResponseTime() {
    return averageResponseTime;
  }

  public void setAverageResponseTime(@Nullable Integer averageResponseTime) {
    this.averageResponseTime = averageResponseTime;
  }

  public ApiMetrics requestsByEndpoint(List<@Valid ApiMetricsRequestsByEndpointInner> requestsByEndpoint) {
    this.requestsByEndpoint = requestsByEndpoint;
    return this;
  }

  public ApiMetrics addRequestsByEndpointItem(ApiMetricsRequestsByEndpointInner requestsByEndpointItem) {
    if (this.requestsByEndpoint == null) {
      this.requestsByEndpoint = new ArrayList<>();
    }
    this.requestsByEndpoint.add(requestsByEndpointItem);
    return this;
  }

  /**
   * Get requestsByEndpoint
   * @return requestsByEndpoint
   */
  @Valid 
  @Schema(name = "requests_by_endpoint", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requests_by_endpoint")
  public List<@Valid ApiMetricsRequestsByEndpointInner> getRequestsByEndpoint() {
    return requestsByEndpoint;
  }

  public void setRequestsByEndpoint(List<@Valid ApiMetricsRequestsByEndpointInner> requestsByEndpoint) {
    this.requestsByEndpoint = requestsByEndpoint;
  }

  public ApiMetrics errorsByCode(Map<String, Integer> errorsByCode) {
    this.errorsByCode = errorsByCode;
    return this;
  }

  public ApiMetrics putErrorsByCodeItem(String key, Integer errorsByCodeItem) {
    if (this.errorsByCode == null) {
      this.errorsByCode = new HashMap<>();
    }
    this.errorsByCode.put(key, errorsByCodeItem);
    return this;
  }

  /**
   * Get errorsByCode
   * @return errorsByCode
   */
  
  @Schema(name = "errors_by_code", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("errors_by_code")
  public Map<String, Integer> getErrorsByCode() {
    return errorsByCode;
  }

  public void setErrorsByCode(Map<String, Integer> errorsByCode) {
    this.errorsByCode = errorsByCode;
  }

  public ApiMetrics topEndpoints(List<@Valid ApiMetricsTopEndpointsInner> topEndpoints) {
    this.topEndpoints = topEndpoints;
    return this;
  }

  public ApiMetrics addTopEndpointsItem(ApiMetricsTopEndpointsInner topEndpointsItem) {
    if (this.topEndpoints == null) {
      this.topEndpoints = new ArrayList<>();
    }
    this.topEndpoints.add(topEndpointsItem);
    return this;
  }

  /**
   * Most used endpoints
   * @return topEndpoints
   */
  @Valid 
  @Schema(name = "top_endpoints", description = "Most used endpoints", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("top_endpoints")
  public List<@Valid ApiMetricsTopEndpointsInner> getTopEndpoints() {
    return topEndpoints;
  }

  public void setTopEndpoints(List<@Valid ApiMetricsTopEndpointsInner> topEndpoints) {
    this.topEndpoints = topEndpoints;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiMetrics apiMetrics = (ApiMetrics) o;
    return Objects.equals(this.timeRange, apiMetrics.timeRange) &&
        Objects.equals(this.totalRequests, apiMetrics.totalRequests) &&
        Objects.equals(this.successfulRequests, apiMetrics.successfulRequests) &&
        Objects.equals(this.failedRequests, apiMetrics.failedRequests) &&
        Objects.equals(this.averageResponseTime, apiMetrics.averageResponseTime) &&
        Objects.equals(this.requestsByEndpoint, apiMetrics.requestsByEndpoint) &&
        Objects.equals(this.errorsByCode, apiMetrics.errorsByCode) &&
        Objects.equals(this.topEndpoints, apiMetrics.topEndpoints);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeRange, totalRequests, successfulRequests, failedRequests, averageResponseTime, requestsByEndpoint, errorsByCode, topEndpoints);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiMetrics {\n");
    sb.append("    timeRange: ").append(toIndentedString(timeRange)).append("\n");
    sb.append("    totalRequests: ").append(toIndentedString(totalRequests)).append("\n");
    sb.append("    successfulRequests: ").append(toIndentedString(successfulRequests)).append("\n");
    sb.append("    failedRequests: ").append(toIndentedString(failedRequests)).append("\n");
    sb.append("    averageResponseTime: ").append(toIndentedString(averageResponseTime)).append("\n");
    sb.append("    requestsByEndpoint: ").append(toIndentedString(requestsByEndpoint)).append("\n");
    sb.append("    errorsByCode: ").append(toIndentedString(errorsByCode)).append("\n");
    sb.append("    topEndpoints: ").append(toIndentedString(topEndpoints)).append("\n");
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

