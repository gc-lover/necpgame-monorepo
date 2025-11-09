package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ReportPriority;
import com.necpgame.socialservice.model.ReportStatus;
import java.time.OffsetDateTime;
import java.util.UUID;
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
 * ReportTicket
 */


public class ReportTicket {

  private UUID reportId;

  private ReportStatus status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  private @Nullable ReportPriority priority;

  private @Nullable UUID assignedModeratorId;

  public ReportTicket() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReportTicket(UUID reportId, ReportStatus status, OffsetDateTime createdAt) {
    this.reportId = reportId;
    this.status = status;
    this.createdAt = createdAt;
  }

  public ReportTicket reportId(UUID reportId) {
    this.reportId = reportId;
    return this;
  }

  /**
   * Get reportId
   * @return reportId
   */
  @NotNull @Valid 
  @Schema(name = "reportId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reportId")
  public UUID getReportId() {
    return reportId;
  }

  public void setReportId(UUID reportId) {
    this.reportId = reportId;
  }

  public ReportTicket status(ReportStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull @Valid 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public ReportStatus getStatus() {
    return status;
  }

  public void setStatus(ReportStatus status) {
    this.status = status;
  }

  public ReportTicket createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @NotNull @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public ReportTicket priority(@Nullable ReportPriority priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  @Valid 
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public @Nullable ReportPriority getPriority() {
    return priority;
  }

  public void setPriority(@Nullable ReportPriority priority) {
    this.priority = priority;
  }

  public ReportTicket assignedModeratorId(@Nullable UUID assignedModeratorId) {
    this.assignedModeratorId = assignedModeratorId;
    return this;
  }

  /**
   * Get assignedModeratorId
   * @return assignedModeratorId
   */
  @Valid 
  @Schema(name = "assignedModeratorId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("assignedModeratorId")
  public @Nullable UUID getAssignedModeratorId() {
    return assignedModeratorId;
  }

  public void setAssignedModeratorId(@Nullable UUID assignedModeratorId) {
    this.assignedModeratorId = assignedModeratorId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReportTicket reportTicket = (ReportTicket) o;
    return Objects.equals(this.reportId, reportTicket.reportId) &&
        Objects.equals(this.status, reportTicket.status) &&
        Objects.equals(this.createdAt, reportTicket.createdAt) &&
        Objects.equals(this.priority, reportTicket.priority) &&
        Objects.equals(this.assignedModeratorId, reportTicket.assignedModeratorId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reportId, status, createdAt, priority, assignedModeratorId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReportTicket {\n");
    sb.append("    reportId: ").append(toIndentedString(reportId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    assignedModeratorId: ").append(toIndentedString(assignedModeratorId)).append("\n");
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

