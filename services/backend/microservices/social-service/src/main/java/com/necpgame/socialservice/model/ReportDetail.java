package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ReportDetailAllOfHistory;
import com.necpgame.socialservice.model.ReportPriority;
import com.necpgame.socialservice.model.ReportStatus;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * ReportDetail
 */


public class ReportDetail {

  private UUID reportId;

  private ReportStatus status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  private @Nullable ReportPriority priority;

  private @Nullable UUID assignedModeratorId;

  /**
   * Gets or Sets resolution
   */
  public enum ResolutionEnum {
    WARN("WARN"),
    
    BAN("BAN"),
    
    NO_ACTION("NO_ACTION");

    private final String value;

    ResolutionEnum(String value) {
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
    public static ResolutionEnum fromValue(String value) {
      for (ResolutionEnum b : ResolutionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ResolutionEnum resolution;

  private @Nullable String notes;

  private @Nullable UUID appliedBanId;

  @Valid
  private List<@Valid ReportDetailAllOfHistory> history = new ArrayList<>();

  public ReportDetail() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReportDetail(UUID reportId, ReportStatus status, OffsetDateTime createdAt) {
    this.reportId = reportId;
    this.status = status;
    this.createdAt = createdAt;
  }

  public ReportDetail reportId(UUID reportId) {
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

  public ReportDetail status(ReportStatus status) {
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

  public ReportDetail createdAt(OffsetDateTime createdAt) {
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

  public ReportDetail priority(@Nullable ReportPriority priority) {
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

  public ReportDetail assignedModeratorId(@Nullable UUID assignedModeratorId) {
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

  public ReportDetail resolution(@Nullable ResolutionEnum resolution) {
    this.resolution = resolution;
    return this;
  }

  /**
   * Get resolution
   * @return resolution
   */
  
  @Schema(name = "resolution", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resolution")
  public @Nullable ResolutionEnum getResolution() {
    return resolution;
  }

  public void setResolution(@Nullable ResolutionEnum resolution) {
    this.resolution = resolution;
  }

  public ReportDetail notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  public ReportDetail appliedBanId(@Nullable UUID appliedBanId) {
    this.appliedBanId = appliedBanId;
    return this;
  }

  /**
   * Get appliedBanId
   * @return appliedBanId
   */
  @Valid 
  @Schema(name = "appliedBanId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("appliedBanId")
  public @Nullable UUID getAppliedBanId() {
    return appliedBanId;
  }

  public void setAppliedBanId(@Nullable UUID appliedBanId) {
    this.appliedBanId = appliedBanId;
  }

  public ReportDetail history(List<@Valid ReportDetailAllOfHistory> history) {
    this.history = history;
    return this;
  }

  public ReportDetail addHistoryItem(ReportDetailAllOfHistory historyItem) {
    if (this.history == null) {
      this.history = new ArrayList<>();
    }
    this.history.add(historyItem);
    return this;
  }

  /**
   * Get history
   * @return history
   */
  @Valid 
  @Schema(name = "history", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("history")
  public List<@Valid ReportDetailAllOfHistory> getHistory() {
    return history;
  }

  public void setHistory(List<@Valid ReportDetailAllOfHistory> history) {
    this.history = history;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReportDetail reportDetail = (ReportDetail) o;
    return Objects.equals(this.reportId, reportDetail.reportId) &&
        Objects.equals(this.status, reportDetail.status) &&
        Objects.equals(this.createdAt, reportDetail.createdAt) &&
        Objects.equals(this.priority, reportDetail.priority) &&
        Objects.equals(this.assignedModeratorId, reportDetail.assignedModeratorId) &&
        Objects.equals(this.resolution, reportDetail.resolution) &&
        Objects.equals(this.notes, reportDetail.notes) &&
        Objects.equals(this.appliedBanId, reportDetail.appliedBanId) &&
        Objects.equals(this.history, reportDetail.history);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reportId, status, createdAt, priority, assignedModeratorId, resolution, notes, appliedBanId, history);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReportDetail {\n");
    sb.append("    reportId: ").append(toIndentedString(reportId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    assignedModeratorId: ").append(toIndentedString(assignedModeratorId)).append("\n");
    sb.append("    resolution: ").append(toIndentedString(resolution)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
    sb.append("    appliedBanId: ").append(toIndentedString(appliedBanId)).append("\n");
    sb.append("    history: ").append(toIndentedString(history)).append("\n");
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

