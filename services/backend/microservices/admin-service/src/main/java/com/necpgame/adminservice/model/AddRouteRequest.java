package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.AddRouteRequestRateLimit;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AddRouteRequest
 */

@JsonTypeName("addRoute_request")

public class AddRouteRequest {

  private String pathPattern;

  private String targetService;

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

  private @Nullable AddRouteRequestRateLimit rateLimit;

  public AddRouteRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AddRouteRequest(String pathPattern, String targetService) {
    this.pathPattern = pathPattern;
    this.targetService = targetService;
  }

  public AddRouteRequest pathPattern(String pathPattern) {
    this.pathPattern = pathPattern;
    return this;
  }

  /**
   * Get pathPattern
   * @return pathPattern
   */
  @NotNull 
  @Schema(name = "path_pattern", example = "/api/v1/characters/_*", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("path_pattern")
  public String getPathPattern() {
    return pathPattern;
  }

  public void setPathPattern(String pathPattern) {
    this.pathPattern = pathPattern;
  }

  public AddRouteRequest targetService(String targetService) {
    this.targetService = targetService;
    return this;
  }

  /**
   * Get targetService
   * @return targetService
   */
  @NotNull 
  @Schema(name = "target_service", example = "character-service", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target_service")
  public String getTargetService() {
    return targetService;
  }

  public void setTargetService(String targetService) {
    this.targetService = targetService;
  }

  public AddRouteRequest loadBalancing(@Nullable LoadBalancingEnum loadBalancing) {
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

  public AddRouteRequest rateLimit(@Nullable AddRouteRequestRateLimit rateLimit) {
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
  public @Nullable AddRouteRequestRateLimit getRateLimit() {
    return rateLimit;
  }

  public void setRateLimit(@Nullable AddRouteRequestRateLimit rateLimit) {
    this.rateLimit = rateLimit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AddRouteRequest addRouteRequest = (AddRouteRequest) o;
    return Objects.equals(this.pathPattern, addRouteRequest.pathPattern) &&
        Objects.equals(this.targetService, addRouteRequest.targetService) &&
        Objects.equals(this.loadBalancing, addRouteRequest.loadBalancing) &&
        Objects.equals(this.rateLimit, addRouteRequest.rateLimit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pathPattern, targetService, loadBalancing, rateLimit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AddRouteRequest {\n");
    sb.append("    pathPattern: ").append(toIndentedString(pathPattern)).append("\n");
    sb.append("    targetService: ").append(toIndentedString(targetService)).append("\n");
    sb.append("    loadBalancing: ").append(toIndentedString(loadBalancing)).append("\n");
    sb.append("    rateLimit: ").append(toIndentedString(rateLimit)).append("\n");
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

