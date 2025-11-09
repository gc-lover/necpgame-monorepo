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
 * GetRateLimits200ResponseEndpointLimitsInner
 */

@JsonTypeName("getRateLimits_200_response_endpoint_limits_inner")

public class GetRateLimits200ResponseEndpointLimitsInner {

  private @Nullable String endpoint;

  private @Nullable Integer limit;

  private @Nullable String window;

  public GetRateLimits200ResponseEndpointLimitsInner endpoint(@Nullable String endpoint) {
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

  public GetRateLimits200ResponseEndpointLimitsInner limit(@Nullable Integer limit) {
    this.limit = limit;
    return this;
  }

  /**
   * Get limit
   * @return limit
   */
  
  @Schema(name = "limit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("limit")
  public @Nullable Integer getLimit() {
    return limit;
  }

  public void setLimit(@Nullable Integer limit) {
    this.limit = limit;
  }

  public GetRateLimits200ResponseEndpointLimitsInner window(@Nullable String window) {
    this.window = window;
    return this;
  }

  /**
   * Get window
   * @return window
   */
  
  @Schema(name = "window", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("window")
  public @Nullable String getWindow() {
    return window;
  }

  public void setWindow(@Nullable String window) {
    this.window = window;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetRateLimits200ResponseEndpointLimitsInner getRateLimits200ResponseEndpointLimitsInner = (GetRateLimits200ResponseEndpointLimitsInner) o;
    return Objects.equals(this.endpoint, getRateLimits200ResponseEndpointLimitsInner.endpoint) &&
        Objects.equals(this.limit, getRateLimits200ResponseEndpointLimitsInner.limit) &&
        Objects.equals(this.window, getRateLimits200ResponseEndpointLimitsInner.window);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endpoint, limit, window);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetRateLimits200ResponseEndpointLimitsInner {\n");
    sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
    sb.append("    limit: ").append(toIndentedString(limit)).append("\n");
    sb.append("    window: ").append(toIndentedString(window)).append("\n");
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

