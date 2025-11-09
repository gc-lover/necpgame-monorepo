package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * GetCapacityData200ResponseCurrentCapacity
 */

@JsonTypeName("getCapacityData_200_response_current_capacity")

public class GetCapacityData200ResponseCurrentCapacity {

  private @Nullable BigDecimal cpuUsagePercent;

  private @Nullable BigDecimal memoryUsagePercent;

  private @Nullable BigDecimal diskUsagePercent;

  private @Nullable BigDecimal networkBandwidthMbps;

  public GetCapacityData200ResponseCurrentCapacity cpuUsagePercent(@Nullable BigDecimal cpuUsagePercent) {
    this.cpuUsagePercent = cpuUsagePercent;
    return this;
  }

  /**
   * Get cpuUsagePercent
   * @return cpuUsagePercent
   */
  @Valid 
  @Schema(name = "cpu_usage_percent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cpu_usage_percent")
  public @Nullable BigDecimal getCpuUsagePercent() {
    return cpuUsagePercent;
  }

  public void setCpuUsagePercent(@Nullable BigDecimal cpuUsagePercent) {
    this.cpuUsagePercent = cpuUsagePercent;
  }

  public GetCapacityData200ResponseCurrentCapacity memoryUsagePercent(@Nullable BigDecimal memoryUsagePercent) {
    this.memoryUsagePercent = memoryUsagePercent;
    return this;
  }

  /**
   * Get memoryUsagePercent
   * @return memoryUsagePercent
   */
  @Valid 
  @Schema(name = "memory_usage_percent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("memory_usage_percent")
  public @Nullable BigDecimal getMemoryUsagePercent() {
    return memoryUsagePercent;
  }

  public void setMemoryUsagePercent(@Nullable BigDecimal memoryUsagePercent) {
    this.memoryUsagePercent = memoryUsagePercent;
  }

  public GetCapacityData200ResponseCurrentCapacity diskUsagePercent(@Nullable BigDecimal diskUsagePercent) {
    this.diskUsagePercent = diskUsagePercent;
    return this;
  }

  /**
   * Get diskUsagePercent
   * @return diskUsagePercent
   */
  @Valid 
  @Schema(name = "disk_usage_percent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("disk_usage_percent")
  public @Nullable BigDecimal getDiskUsagePercent() {
    return diskUsagePercent;
  }

  public void setDiskUsagePercent(@Nullable BigDecimal diskUsagePercent) {
    this.diskUsagePercent = diskUsagePercent;
  }

  public GetCapacityData200ResponseCurrentCapacity networkBandwidthMbps(@Nullable BigDecimal networkBandwidthMbps) {
    this.networkBandwidthMbps = networkBandwidthMbps;
    return this;
  }

  /**
   * Get networkBandwidthMbps
   * @return networkBandwidthMbps
   */
  @Valid 
  @Schema(name = "network_bandwidth_mbps", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("network_bandwidth_mbps")
  public @Nullable BigDecimal getNetworkBandwidthMbps() {
    return networkBandwidthMbps;
  }

  public void setNetworkBandwidthMbps(@Nullable BigDecimal networkBandwidthMbps) {
    this.networkBandwidthMbps = networkBandwidthMbps;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCapacityData200ResponseCurrentCapacity getCapacityData200ResponseCurrentCapacity = (GetCapacityData200ResponseCurrentCapacity) o;
    return Objects.equals(this.cpuUsagePercent, getCapacityData200ResponseCurrentCapacity.cpuUsagePercent) &&
        Objects.equals(this.memoryUsagePercent, getCapacityData200ResponseCurrentCapacity.memoryUsagePercent) &&
        Objects.equals(this.diskUsagePercent, getCapacityData200ResponseCurrentCapacity.diskUsagePercent) &&
        Objects.equals(this.networkBandwidthMbps, getCapacityData200ResponseCurrentCapacity.networkBandwidthMbps);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cpuUsagePercent, memoryUsagePercent, diskUsagePercent, networkBandwidthMbps);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCapacityData200ResponseCurrentCapacity {\n");
    sb.append("    cpuUsagePercent: ").append(toIndentedString(cpuUsagePercent)).append("\n");
    sb.append("    memoryUsagePercent: ").append(toIndentedString(memoryUsagePercent)).append("\n");
    sb.append("    diskUsagePercent: ").append(toIndentedString(diskUsagePercent)).append("\n");
    sb.append("    networkBandwidthMbps: ").append(toIndentedString(networkBandwidthMbps)).append("\n");
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

