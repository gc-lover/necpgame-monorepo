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
 * GetCapacityData200ResponseProjectedCapacity
 */

@JsonTypeName("getCapacityData_200_response_projected_capacity")

public class GetCapacityData200ResponseProjectedCapacity {

  private @Nullable Integer daysUntilCpuLimit;

  private @Nullable Integer daysUntilMemoryLimit;

  public GetCapacityData200ResponseProjectedCapacity daysUntilCpuLimit(@Nullable Integer daysUntilCpuLimit) {
    this.daysUntilCpuLimit = daysUntilCpuLimit;
    return this;
  }

  /**
   * Get daysUntilCpuLimit
   * @return daysUntilCpuLimit
   */
  
  @Schema(name = "days_until_cpu_limit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("days_until_cpu_limit")
  public @Nullable Integer getDaysUntilCpuLimit() {
    return daysUntilCpuLimit;
  }

  public void setDaysUntilCpuLimit(@Nullable Integer daysUntilCpuLimit) {
    this.daysUntilCpuLimit = daysUntilCpuLimit;
  }

  public GetCapacityData200ResponseProjectedCapacity daysUntilMemoryLimit(@Nullable Integer daysUntilMemoryLimit) {
    this.daysUntilMemoryLimit = daysUntilMemoryLimit;
    return this;
  }

  /**
   * Get daysUntilMemoryLimit
   * @return daysUntilMemoryLimit
   */
  
  @Schema(name = "days_until_memory_limit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("days_until_memory_limit")
  public @Nullable Integer getDaysUntilMemoryLimit() {
    return daysUntilMemoryLimit;
  }

  public void setDaysUntilMemoryLimit(@Nullable Integer daysUntilMemoryLimit) {
    this.daysUntilMemoryLimit = daysUntilMemoryLimit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCapacityData200ResponseProjectedCapacity getCapacityData200ResponseProjectedCapacity = (GetCapacityData200ResponseProjectedCapacity) o;
    return Objects.equals(this.daysUntilCpuLimit, getCapacityData200ResponseProjectedCapacity.daysUntilCpuLimit) &&
        Objects.equals(this.daysUntilMemoryLimit, getCapacityData200ResponseProjectedCapacity.daysUntilMemoryLimit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(daysUntilCpuLimit, daysUntilMemoryLimit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCapacityData200ResponseProjectedCapacity {\n");
    sb.append("    daysUntilCpuLimit: ").append(toIndentedString(daysUntilCpuLimit)).append("\n");
    sb.append("    daysUntilMemoryLimit: ").append(toIndentedString(daysUntilMemoryLimit)).append("\n");
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

