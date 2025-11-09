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
 * GatewayRouteRateLimit
 */

@JsonTypeName("GatewayRoute_rate_limit")

public class GatewayRouteRateLimit {

  private @Nullable Integer requestsPerMinute;

  private @Nullable Integer burst;

  public GatewayRouteRateLimit requestsPerMinute(@Nullable Integer requestsPerMinute) {
    this.requestsPerMinute = requestsPerMinute;
    return this;
  }

  /**
   * Get requestsPerMinute
   * @return requestsPerMinute
   */
  
  @Schema(name = "requests_per_minute", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requests_per_minute")
  public @Nullable Integer getRequestsPerMinute() {
    return requestsPerMinute;
  }

  public void setRequestsPerMinute(@Nullable Integer requestsPerMinute) {
    this.requestsPerMinute = requestsPerMinute;
  }

  public GatewayRouteRateLimit burst(@Nullable Integer burst) {
    this.burst = burst;
    return this;
  }

  /**
   * Get burst
   * @return burst
   */
  
  @Schema(name = "burst", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("burst")
  public @Nullable Integer getBurst() {
    return burst;
  }

  public void setBurst(@Nullable Integer burst) {
    this.burst = burst;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GatewayRouteRateLimit gatewayRouteRateLimit = (GatewayRouteRateLimit) o;
    return Objects.equals(this.requestsPerMinute, gatewayRouteRateLimit.requestsPerMinute) &&
        Objects.equals(this.burst, gatewayRouteRateLimit.burst);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestsPerMinute, burst);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GatewayRouteRateLimit {\n");
    sb.append("    requestsPerMinute: ").append(toIndentedString(requestsPerMinute)).append("\n");
    sb.append("    burst: ").append(toIndentedString(burst)).append("\n");
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

