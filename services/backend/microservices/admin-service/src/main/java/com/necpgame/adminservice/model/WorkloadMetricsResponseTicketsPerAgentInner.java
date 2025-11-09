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
 * WorkloadMetricsResponseTicketsPerAgentInner
 */

@JsonTypeName("WorkloadMetricsResponse_ticketsPerAgent_inner")

public class WorkloadMetricsResponseTicketsPerAgentInner {

  private @Nullable String agentId;

  private @Nullable Integer count;

  public WorkloadMetricsResponseTicketsPerAgentInner agentId(@Nullable String agentId) {
    this.agentId = agentId;
    return this;
  }

  /**
   * Get agentId
   * @return agentId
   */
  
  @Schema(name = "agentId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("agentId")
  public @Nullable String getAgentId() {
    return agentId;
  }

  public void setAgentId(@Nullable String agentId) {
    this.agentId = agentId;
  }

  public WorkloadMetricsResponseTicketsPerAgentInner count(@Nullable Integer count) {
    this.count = count;
    return this;
  }

  /**
   * Get count
   * @return count
   */
  
  @Schema(name = "count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("count")
  public @Nullable Integer getCount() {
    return count;
  }

  public void setCount(@Nullable Integer count) {
    this.count = count;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorkloadMetricsResponseTicketsPerAgentInner workloadMetricsResponseTicketsPerAgentInner = (WorkloadMetricsResponseTicketsPerAgentInner) o;
    return Objects.equals(this.agentId, workloadMetricsResponseTicketsPerAgentInner.agentId) &&
        Objects.equals(this.count, workloadMetricsResponseTicketsPerAgentInner.count);
  }

  @Override
  public int hashCode() {
    return Objects.hash(agentId, count);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorkloadMetricsResponseTicketsPerAgentInner {\n");
    sb.append("    agentId: ").append(toIndentedString(agentId)).append("\n");
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
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

