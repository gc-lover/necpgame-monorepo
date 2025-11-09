package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.WorkloadMetricsResponseBacklogTrendInner;
import com.necpgame.adminservice.model.WorkloadMetricsResponseTicketsPerAgentInner;
import java.math.BigDecimal;
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
 * WorkloadMetricsResponse
 */


public class WorkloadMetricsResponse {

  private @Nullable Integer openTickets;

  @Valid
  private List<@Valid WorkloadMetricsResponseTicketsPerAgentInner> ticketsPerAgent = new ArrayList<>();

  private @Nullable BigDecimal averageResponseMinutes;

  @Valid
  private List<@Valid WorkloadMetricsResponseBacklogTrendInner> backlogTrend = new ArrayList<>();

  public WorkloadMetricsResponse openTickets(@Nullable Integer openTickets) {
    this.openTickets = openTickets;
    return this;
  }

  /**
   * Get openTickets
   * @return openTickets
   */
  
  @Schema(name = "openTickets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("openTickets")
  public @Nullable Integer getOpenTickets() {
    return openTickets;
  }

  public void setOpenTickets(@Nullable Integer openTickets) {
    this.openTickets = openTickets;
  }

  public WorkloadMetricsResponse ticketsPerAgent(List<@Valid WorkloadMetricsResponseTicketsPerAgentInner> ticketsPerAgent) {
    this.ticketsPerAgent = ticketsPerAgent;
    return this;
  }

  public WorkloadMetricsResponse addTicketsPerAgentItem(WorkloadMetricsResponseTicketsPerAgentInner ticketsPerAgentItem) {
    if (this.ticketsPerAgent == null) {
      this.ticketsPerAgent = new ArrayList<>();
    }
    this.ticketsPerAgent.add(ticketsPerAgentItem);
    return this;
  }

  /**
   * Get ticketsPerAgent
   * @return ticketsPerAgent
   */
  @Valid 
  @Schema(name = "ticketsPerAgent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ticketsPerAgent")
  public List<@Valid WorkloadMetricsResponseTicketsPerAgentInner> getTicketsPerAgent() {
    return ticketsPerAgent;
  }

  public void setTicketsPerAgent(List<@Valid WorkloadMetricsResponseTicketsPerAgentInner> ticketsPerAgent) {
    this.ticketsPerAgent = ticketsPerAgent;
  }

  public WorkloadMetricsResponse averageResponseMinutes(@Nullable BigDecimal averageResponseMinutes) {
    this.averageResponseMinutes = averageResponseMinutes;
    return this;
  }

  /**
   * Get averageResponseMinutes
   * @return averageResponseMinutes
   */
  @Valid 
  @Schema(name = "averageResponseMinutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("averageResponseMinutes")
  public @Nullable BigDecimal getAverageResponseMinutes() {
    return averageResponseMinutes;
  }

  public void setAverageResponseMinutes(@Nullable BigDecimal averageResponseMinutes) {
    this.averageResponseMinutes = averageResponseMinutes;
  }

  public WorkloadMetricsResponse backlogTrend(List<@Valid WorkloadMetricsResponseBacklogTrendInner> backlogTrend) {
    this.backlogTrend = backlogTrend;
    return this;
  }

  public WorkloadMetricsResponse addBacklogTrendItem(WorkloadMetricsResponseBacklogTrendInner backlogTrendItem) {
    if (this.backlogTrend == null) {
      this.backlogTrend = new ArrayList<>();
    }
    this.backlogTrend.add(backlogTrendItem);
    return this;
  }

  /**
   * Get backlogTrend
   * @return backlogTrend
   */
  @Valid 
  @Schema(name = "backlogTrend", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("backlogTrend")
  public List<@Valid WorkloadMetricsResponseBacklogTrendInner> getBacklogTrend() {
    return backlogTrend;
  }

  public void setBacklogTrend(List<@Valid WorkloadMetricsResponseBacklogTrendInner> backlogTrend) {
    this.backlogTrend = backlogTrend;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorkloadMetricsResponse workloadMetricsResponse = (WorkloadMetricsResponse) o;
    return Objects.equals(this.openTickets, workloadMetricsResponse.openTickets) &&
        Objects.equals(this.ticketsPerAgent, workloadMetricsResponse.ticketsPerAgent) &&
        Objects.equals(this.averageResponseMinutes, workloadMetricsResponse.averageResponseMinutes) &&
        Objects.equals(this.backlogTrend, workloadMetricsResponse.backlogTrend);
  }

  @Override
  public int hashCode() {
    return Objects.hash(openTickets, ticketsPerAgent, averageResponseMinutes, backlogTrend);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorkloadMetricsResponse {\n");
    sb.append("    openTickets: ").append(toIndentedString(openTickets)).append("\n");
    sb.append("    ticketsPerAgent: ").append(toIndentedString(ticketsPerAgent)).append("\n");
    sb.append("    averageResponseMinutes: ").append(toIndentedString(averageResponseMinutes)).append("\n");
    sb.append("    backlogTrend: ").append(toIndentedString(backlogTrend)).append("\n");
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

