package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AdminDashboardServerHealth
 */

@JsonTypeName("AdminDashboard_server_health")

public class AdminDashboardServerHealth {

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    HEALTHY("HEALTHY"),
    
    DEGRADED("DEGRADED"),
    
    DOWN("DOWN");

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

  private @Nullable BigDecimal cpuUsage;

  private @Nullable BigDecimal memoryUsage;

  public AdminDashboardServerHealth status(@Nullable StatusEnum status) {
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

  public AdminDashboardServerHealth cpuUsage(@Nullable BigDecimal cpuUsage) {
    this.cpuUsage = cpuUsage;
    return this;
  }

  /**
   * Get cpuUsage
   * @return cpuUsage
   */
  @Valid 
  @Schema(name = "cpu_usage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cpu_usage")
  public @Nullable BigDecimal getCpuUsage() {
    return cpuUsage;
  }

  public void setCpuUsage(@Nullable BigDecimal cpuUsage) {
    this.cpuUsage = cpuUsage;
  }

  public AdminDashboardServerHealth memoryUsage(@Nullable BigDecimal memoryUsage) {
    this.memoryUsage = memoryUsage;
    return this;
  }

  /**
   * Get memoryUsage
   * @return memoryUsage
   */
  @Valid 
  @Schema(name = "memory_usage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("memory_usage")
  public @Nullable BigDecimal getMemoryUsage() {
    return memoryUsage;
  }

  public void setMemoryUsage(@Nullable BigDecimal memoryUsage) {
    this.memoryUsage = memoryUsage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AdminDashboardServerHealth adminDashboardServerHealth = (AdminDashboardServerHealth) o;
    return Objects.equals(this.status, adminDashboardServerHealth.status) &&
        Objects.equals(this.cpuUsage, adminDashboardServerHealth.cpuUsage) &&
        Objects.equals(this.memoryUsage, adminDashboardServerHealth.memoryUsage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, cpuUsage, memoryUsage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdminDashboardServerHealth {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    cpuUsage: ").append(toIndentedString(cpuUsage)).append("\n");
    sb.append("    memoryUsage: ").append(toIndentedString(memoryUsage)).append("\n");
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

