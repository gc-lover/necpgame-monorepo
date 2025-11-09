package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.GatewayRouteRateLimit;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GatewayRoute
 */


public class GatewayRoute {

  private @Nullable String routeId;

  private @Nullable String pathPattern;

  private @Nullable String targetService;

  /**
   * Gets or Sets loadBalancing
   */
  public enum LoadBalancingEnum {
    ROUND_ROBIN("round_robin"),
    
    LEAST_CONNECTIONS("least_connections"),
    
    IP_HASH("ip_hash");

    private final String value;

    LoadBalancingEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static LoadBalancingEnum fromValue(String value) {
      for (LoadBalancingEnum b : LoadBalancingEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable LoadBalancingEnum loadBalancing;

  private @Nullable GatewayRouteRateLimit rateLimit;

  private @Nullable Boolean cacheEnabled;

  private @Nullable Integer cacheTtl;

  public GatewayRoute routeId(@Nullable String routeId) {
    this.routeId = routeId;
    return this;
  }

  /**
   * Get routeId
   * @return routeId
   */
  
  @Schema(name = "route_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("route_id")
  public @Nullable String getRouteId() {
    return routeId;
  }

  public void setRouteId(@Nullable String routeId) {
    this.routeId = routeId;
  }

  public GatewayRoute pathPattern(@Nullable String pathPattern) {
    this.pathPattern = pathPattern;
    return this;
  }

  /**
   * Get pathPattern
   * @return pathPattern
   */
  
  @Schema(name = "path_pattern", example = "/api/v1/characters/_*", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("path_pattern")
  public @Nullable String getPathPattern() {
    return pathPattern;
  }

  public void setPathPattern(@Nullable String pathPattern) {
    this.pathPattern = pathPattern;
  }

  public GatewayRoute targetService(@Nullable String targetService) {
    this.targetService = targetService;
    return this;
  }

  /**
   * Get targetService
   * @return targetService
   */
  
  @Schema(name = "target_service", example = "character-service:8082", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_service")
  public @Nullable String getTargetService() {
    return targetService;
  }

  public void setTargetService(@Nullable String targetService) {
    this.targetService = targetService;
  }

  public GatewayRoute loadBalancing(@Nullable LoadBalancingEnum loadBalancing) {
    this.loadBalancing = loadBalancing;
    return this;
  }

  /**
   * Get loadBalancing
   * @return loadBalancing
   */
  
  @Schema(name = "load_balancing", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("load_balancing")
  public @Nullable LoadBalancingEnum getLoadBalancing() {
    return loadBalancing;
  }

  public void setLoadBalancing(@Nullable LoadBalancingEnum loadBalancing) {
    this.loadBalancing = loadBalancing;
  }

  public GatewayRoute rateLimit(@Nullable GatewayRouteRateLimit rateLimit) {
    this.rateLimit = rateLimit;
    return this;
  }

  /**
   * Get rateLimit
   * @return rateLimit
   */
  @Valid 
  @Schema(name = "rate_limit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rate_limit")
  public @Nullable GatewayRouteRateLimit getRateLimit() {
    return rateLimit;
  }

  public void setRateLimit(@Nullable GatewayRouteRateLimit rateLimit) {
    this.rateLimit = rateLimit;
  }

  public GatewayRoute cacheEnabled(@Nullable Boolean cacheEnabled) {
    this.cacheEnabled = cacheEnabled;
    return this;
  }

  /**
   * Get cacheEnabled
   * @return cacheEnabled
   */
  
  @Schema(name = "cache_enabled", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cache_enabled")
  public @Nullable Boolean getCacheEnabled() {
    return cacheEnabled;
  }

  public void setCacheEnabled(@Nullable Boolean cacheEnabled) {
    this.cacheEnabled = cacheEnabled;
  }

  public GatewayRoute cacheTtl(@Nullable Integer cacheTtl) {
    this.cacheTtl = cacheTtl;
    return this;
  }

  /**
   * Cache TTL in seconds
   * @return cacheTtl
   */
  
  @Schema(name = "cache_ttl", description = "Cache TTL in seconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cache_ttl")
  public @Nullable Integer getCacheTtl() {
    return cacheTtl;
  }

  public void setCacheTtl(@Nullable Integer cacheTtl) {
    this.cacheTtl = cacheTtl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GatewayRoute gatewayRoute = (GatewayRoute) o;
    return Objects.equals(this.routeId, gatewayRoute.routeId) &&
        Objects.equals(this.pathPattern, gatewayRoute.pathPattern) &&
        Objects.equals(this.targetService, gatewayRoute.targetService) &&
        Objects.equals(this.loadBalancing, gatewayRoute.loadBalancing) &&
        Objects.equals(this.rateLimit, gatewayRoute.rateLimit) &&
        Objects.equals(this.cacheEnabled, gatewayRoute.cacheEnabled) &&
        Objects.equals(this.cacheTtl, gatewayRoute.cacheTtl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(routeId, pathPattern, targetService, loadBalancing, rateLimit, cacheEnabled, cacheTtl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GatewayRoute {\n");
    sb.append("    routeId: ").append(toIndentedString(routeId)).append("\n");
    sb.append("    pathPattern: ").append(toIndentedString(pathPattern)).append("\n");
    sb.append("    targetService: ").append(toIndentedString(targetService)).append("\n");
    sb.append("    loadBalancing: ").append(toIndentedString(loadBalancing)).append("\n");
    sb.append("    rateLimit: ").append(toIndentedString(rateLimit)).append("\n");
    sb.append("    cacheEnabled: ").append(toIndentedString(cacheEnabled)).append("\n");
    sb.append("    cacheTtl: ").append(toIndentedString(cacheTtl)).append("\n");
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

