package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.ServiceHealthStatus;
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
 * GetServicesHealth200Response
 */

@JsonTypeName("getServicesHealth_200_response")

public class GetServicesHealth200Response {

  /**
   * Gets or Sets overallStatus
   */
  public enum OverallStatusEnum {
    HEALTHY("healthy"),
    
    DEGRADED("degraded"),
    
    DOWN("down");

    private final String value;

    OverallStatusEnum(String value) {
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
    public static OverallStatusEnum fromValue(String value) {
      for (OverallStatusEnum b : OverallStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable OverallStatusEnum overallStatus;

  @Valid
  private List<@Valid ServiceHealthStatus> services = new ArrayList<>();

  public GetServicesHealth200Response overallStatus(@Nullable OverallStatusEnum overallStatus) {
    this.overallStatus = overallStatus;
    return this;
  }

  /**
   * Get overallStatus
   * @return overallStatus
   */
  
  @Schema(name = "overall_status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("overall_status")
  public @Nullable OverallStatusEnum getOverallStatus() {
    return overallStatus;
  }

  public void setOverallStatus(@Nullable OverallStatusEnum overallStatus) {
    this.overallStatus = overallStatus;
  }

  public GetServicesHealth200Response services(List<@Valid ServiceHealthStatus> services) {
    this.services = services;
    return this;
  }

  public GetServicesHealth200Response addServicesItem(ServiceHealthStatus servicesItem) {
    if (this.services == null) {
      this.services = new ArrayList<>();
    }
    this.services.add(servicesItem);
    return this;
  }

  /**
   * Get services
   * @return services
   */
  @Valid 
  @Schema(name = "services", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("services")
  public List<@Valid ServiceHealthStatus> getServices() {
    return services;
  }

  public void setServices(List<@Valid ServiceHealthStatus> services) {
    this.services = services;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetServicesHealth200Response getServicesHealth200Response = (GetServicesHealth200Response) o;
    return Objects.equals(this.overallStatus, getServicesHealth200Response.overallStatus) &&
        Objects.equals(this.services, getServicesHealth200Response.services);
  }

  @Override
  public int hashCode() {
    return Objects.hash(overallStatus, services);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetServicesHealth200Response {\n");
    sb.append("    overallStatus: ").append(toIndentedString(overallStatus)).append("\n");
    sb.append("    services: ").append(toIndentedString(services)).append("\n");
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

