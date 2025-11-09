package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ApiMetricsRequestsByEndpointInner
 */

@JsonTypeName("ApiMetrics_requests_by_endpoint_inner")

public class ApiMetricsRequestsByEndpointInner {

  private @Nullable String endpoint;

  private @Nullable Integer count;

  private @Nullable Integer avgResponseTime;

  public ApiMetricsRequestsByEndpointInner endpoint(@Nullable String endpoint) {
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

  public ApiMetricsRequestsByEndpointInner count(@Nullable Integer count) {
    this.count = count;
    return this;
  }

  /**
   * Get count
   * @return count
   */
  
  @Schema(name = "count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("count")
  public @Nullable Integer getCount() {
    return count;
  }

  public void setCount(@Nullable Integer count) {
    this.count = count;
  }

  public ApiMetricsRequestsByEndpointInner avgResponseTime(@Nullable Integer avgResponseTime) {
    this.avgResponseTime = avgResponseTime;
    return this;
  }

  /**
   * Get avgResponseTime
   * @return avgResponseTime
   */
  
  @Schema(name = "avg_response_time", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("avg_response_time")
  public @Nullable Integer getAvgResponseTime() {
    return avgResponseTime;
  }

  public void setAvgResponseTime(@Nullable Integer avgResponseTime) {
    this.avgResponseTime = avgResponseTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiMetricsRequestsByEndpointInner apiMetricsRequestsByEndpointInner = (ApiMetricsRequestsByEndpointInner) o;
    return Objects.equals(this.endpoint, apiMetricsRequestsByEndpointInner.endpoint) &&
        Objects.equals(this.count, apiMetricsRequestsByEndpointInner.count) &&
        Objects.equals(this.avgResponseTime, apiMetricsRequestsByEndpointInner.avgResponseTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endpoint, count, avgResponseTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiMetricsRequestsByEndpointInner {\n");
    sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
    sb.append("    avgResponseTime: ").append(toIndentedString(avgResponseTime)).append("\n");
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

