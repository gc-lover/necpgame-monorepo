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
 * ApiMetricsTopEndpointsInner
 */

@JsonTypeName("ApiMetrics_top_endpoints_inner")

public class ApiMetricsTopEndpointsInner {

  private @Nullable String endpoint;

  private @Nullable Integer requests;

  public ApiMetricsTopEndpointsInner endpoint(@Nullable String endpoint) {
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

  public ApiMetricsTopEndpointsInner requests(@Nullable Integer requests) {
    this.requests = requests;
    return this;
  }

  /**
   * Get requests
   * @return requests
   */
  
  @Schema(name = "requests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requests")
  public @Nullable Integer getRequests() {
    return requests;
  }

  public void setRequests(@Nullable Integer requests) {
    this.requests = requests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiMetricsTopEndpointsInner apiMetricsTopEndpointsInner = (ApiMetricsTopEndpointsInner) o;
    return Objects.equals(this.endpoint, apiMetricsTopEndpointsInner.endpoint) &&
        Objects.equals(this.requests, apiMetricsTopEndpointsInner.requests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endpoint, requests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiMetricsTopEndpointsInner {\n");
    sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
    sb.append("    requests: ").append(toIndentedString(requests)).append("\n");
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

