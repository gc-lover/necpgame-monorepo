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
 * GatewayMetricsTopRoutesInner
 */

@JsonTypeName("GatewayMetrics_top_routes_inner")

public class GatewayMetricsTopRoutesInner {

  private @Nullable String route;

  private @Nullable Integer requests;

  public GatewayMetricsTopRoutesInner route(@Nullable String route) {
    this.route = route;
    return this;
  }

  /**
   * Get route
   * @return route
   */
  
  @Schema(name = "route", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("route")
  public @Nullable String getRoute() {
    return route;
  }

  public void setRoute(@Nullable String route) {
    this.route = route;
  }

  public GatewayMetricsTopRoutesInner requests(@Nullable Integer requests) {
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
    GatewayMetricsTopRoutesInner gatewayMetricsTopRoutesInner = (GatewayMetricsTopRoutesInner) o;
    return Objects.equals(this.route, gatewayMetricsTopRoutesInner.route) &&
        Objects.equals(this.requests, gatewayMetricsTopRoutesInner.requests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(route, requests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GatewayMetricsTopRoutesInner {\n");
    sb.append("    route: ").append(toIndentedString(route)).append("\n");
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

