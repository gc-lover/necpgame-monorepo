package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.ServiceHealthChecks;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ServiceHealth
 */


public class ServiceHealth {

  private @Nullable String serviceId;

  private @Nullable String serviceName;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    UP("up"),
    
    DEGRADED("degraded"),
    
    DOWN("down");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  private @Nullable Integer responseTime;

  private @Nullable BigDecimal uptime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastCheck;

  private @Nullable ServiceHealthChecks checks;

  public ServiceHealth serviceId(@Nullable String serviceId) {
    this.serviceId = serviceId;
    return this;
  }

  /**
   * Get serviceId
   * @return serviceId
   */
  
  @Schema(name = "service_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("service_id")
  public @Nullable String getServiceId() {
    return serviceId;
  }

  public void setServiceId(@Nullable String serviceId) {
    this.serviceId = serviceId;
  }

  public ServiceHealth serviceName(@Nullable String serviceName) {
    this.serviceName = serviceName;
    return this;
  }

  /**
   * Get serviceName
   * @return serviceName
   */
  
  @Schema(name = "service_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("service_name")
  public @Nullable String getServiceName() {
    return serviceName;
  }

  public void setServiceName(@Nullable String serviceName) {
    this.serviceName = serviceName;
  }

  public ServiceHealth status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public ServiceHealth responseTime(@Nullable Integer responseTime) {
    this.responseTime = responseTime;
    return this;
  }

  /**
   * Response time in ms
   * @return responseTime
   */
  
  @Schema(name = "response_time", description = "Response time in ms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("response_time")
  public @Nullable Integer getResponseTime() {
    return responseTime;
  }

  public void setResponseTime(@Nullable Integer responseTime) {
    this.responseTime = responseTime;
  }

  public ServiceHealth uptime(@Nullable BigDecimal uptime) {
    this.uptime = uptime;
    return this;
  }

  /**
   * Uptime percentage
   * @return uptime
   */
  @Valid 
  @Schema(name = "uptime", description = "Uptime percentage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uptime")
  public @Nullable BigDecimal getUptime() {
    return uptime;
  }

  public void setUptime(@Nullable BigDecimal uptime) {
    this.uptime = uptime;
  }

  public ServiceHealth lastCheck(@Nullable OffsetDateTime lastCheck) {
    this.lastCheck = lastCheck;
    return this;
  }

  /**
   * Get lastCheck
   * @return lastCheck
   */
  @Valid 
  @Schema(name = "last_check", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_check")
  public @Nullable OffsetDateTime getLastCheck() {
    return lastCheck;
  }

  public void setLastCheck(@Nullable OffsetDateTime lastCheck) {
    this.lastCheck = lastCheck;
  }

  public ServiceHealth checks(@Nullable ServiceHealthChecks checks) {
    this.checks = checks;
    return this;
  }

  /**
   * Get checks
   * @return checks
   */
  @Valid 
  @Schema(name = "checks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("checks")
  public @Nullable ServiceHealthChecks getChecks() {
    return checks;
  }

  public void setChecks(@Nullable ServiceHealthChecks checks) {
    this.checks = checks;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceHealth serviceHealth = (ServiceHealth) o;
    return Objects.equals(this.serviceId, serviceHealth.serviceId) &&
        Objects.equals(this.serviceName, serviceHealth.serviceName) &&
        Objects.equals(this.status, serviceHealth.status) &&
        Objects.equals(this.responseTime, serviceHealth.responseTime) &&
        Objects.equals(this.uptime, serviceHealth.uptime) &&
        Objects.equals(this.lastCheck, serviceHealth.lastCheck) &&
        Objects.equals(this.checks, serviceHealth.checks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceId, serviceName, status, responseTime, uptime, lastCheck, checks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceHealth {\n");
    sb.append("    serviceId: ").append(toIndentedString(serviceId)).append("\n");
    sb.append("    serviceName: ").append(toIndentedString(serviceName)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    responseTime: ").append(toIndentedString(responseTime)).append("\n");
    sb.append("    uptime: ").append(toIndentedString(uptime)).append("\n");
    sb.append("    lastCheck: ").append(toIndentedString(lastCheck)).append("\n");
    sb.append("    checks: ").append(toIndentedString(checks)).append("\n");
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

