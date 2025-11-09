package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.InfrastructureJobLog;
import java.math.BigDecimal;
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
 * InfrastructureRecalcJob
 */


public class InfrastructureRecalcJob {

  private UUID jobId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    QUEUED("queued"),
    
    RUNNING("running"),
    
    COMPLETED("completed"),
    
    FAILED("failed"),
    
    CANCELLED("cancelled");

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

  private StatusEnum status;

  private @Nullable String trigger;

  private @Nullable String priority;

  private @Nullable Boolean dryRun;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime submittedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime finishedAt;

  private @Nullable BigDecimal progress;

  private @Nullable Integer affectedDistricts;

  private @Nullable String summary;

  @Valid
  private List<@Valid InfrastructureJobLog> logs = new ArrayList<>();

  public InfrastructureRecalcJob() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InfrastructureRecalcJob(UUID jobId, StatusEnum status, OffsetDateTime submittedAt) {
    this.jobId = jobId;
    this.status = status;
    this.submittedAt = submittedAt;
  }

  public InfrastructureRecalcJob jobId(UUID jobId) {
    this.jobId = jobId;
    return this;
  }

  /**
   * Get jobId
   * @return jobId
   */
  @NotNull @Valid 
  @Schema(name = "jobId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("jobId")
  public UUID getJobId() {
    return jobId;
  }

  public void setJobId(UUID jobId) {
    this.jobId = jobId;
  }

  public InfrastructureRecalcJob status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public InfrastructureRecalcJob trigger(@Nullable String trigger) {
    this.trigger = trigger;
    return this;
  }

  /**
   * Get trigger
   * @return trigger
   */
  
  @Schema(name = "trigger", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trigger")
  public @Nullable String getTrigger() {
    return trigger;
  }

  public void setTrigger(@Nullable String trigger) {
    this.trigger = trigger;
  }

  public InfrastructureRecalcJob priority(@Nullable String priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public @Nullable String getPriority() {
    return priority;
  }

  public void setPriority(@Nullable String priority) {
    this.priority = priority;
  }

  public InfrastructureRecalcJob dryRun(@Nullable Boolean dryRun) {
    this.dryRun = dryRun;
    return this;
  }

  /**
   * Get dryRun
   * @return dryRun
   */
  
  @Schema(name = "dryRun", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dryRun")
  public @Nullable Boolean getDryRun() {
    return dryRun;
  }

  public void setDryRun(@Nullable Boolean dryRun) {
    this.dryRun = dryRun;
  }

  public InfrastructureRecalcJob submittedAt(OffsetDateTime submittedAt) {
    this.submittedAt = submittedAt;
    return this;
  }

  /**
   * Get submittedAt
   * @return submittedAt
   */
  @NotNull @Valid 
  @Schema(name = "submittedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("submittedAt")
  public OffsetDateTime getSubmittedAt() {
    return submittedAt;
  }

  public void setSubmittedAt(OffsetDateTime submittedAt) {
    this.submittedAt = submittedAt;
  }

  public InfrastructureRecalcJob startedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  /**
   * Get startedAt
   * @return startedAt
   */
  @Valid 
  @Schema(name = "startedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startedAt")
  public @Nullable OffsetDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public InfrastructureRecalcJob finishedAt(@Nullable OffsetDateTime finishedAt) {
    this.finishedAt = finishedAt;
    return this;
  }

  /**
   * Get finishedAt
   * @return finishedAt
   */
  @Valid 
  @Schema(name = "finishedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("finishedAt")
  public @Nullable OffsetDateTime getFinishedAt() {
    return finishedAt;
  }

  public void setFinishedAt(@Nullable OffsetDateTime finishedAt) {
    this.finishedAt = finishedAt;
  }

  public InfrastructureRecalcJob progress(@Nullable BigDecimal progress) {
    this.progress = progress;
    return this;
  }

  /**
   * Get progress
   * @return progress
   */
  @Valid 
  @Schema(name = "progress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progress")
  public @Nullable BigDecimal getProgress() {
    return progress;
  }

  public void setProgress(@Nullable BigDecimal progress) {
    this.progress = progress;
  }

  public InfrastructureRecalcJob affectedDistricts(@Nullable Integer affectedDistricts) {
    this.affectedDistricts = affectedDistricts;
    return this;
  }

  /**
   * Get affectedDistricts
   * @return affectedDistricts
   */
  
  @Schema(name = "affectedDistricts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affectedDistricts")
  public @Nullable Integer getAffectedDistricts() {
    return affectedDistricts;
  }

  public void setAffectedDistricts(@Nullable Integer affectedDistricts) {
    this.affectedDistricts = affectedDistricts;
  }

  public InfrastructureRecalcJob summary(@Nullable String summary) {
    this.summary = summary;
    return this;
  }

  /**
   * Get summary
   * @return summary
   */
  
  @Schema(name = "summary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("summary")
  public @Nullable String getSummary() {
    return summary;
  }

  public void setSummary(@Nullable String summary) {
    this.summary = summary;
  }

  public InfrastructureRecalcJob logs(List<@Valid InfrastructureJobLog> logs) {
    this.logs = logs;
    return this;
  }

  public InfrastructureRecalcJob addLogsItem(InfrastructureJobLog logsItem) {
    if (this.logs == null) {
      this.logs = new ArrayList<>();
    }
    this.logs.add(logsItem);
    return this;
  }

  /**
   * Get logs
   * @return logs
   */
  @Valid 
  @Schema(name = "logs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("logs")
  public List<@Valid InfrastructureJobLog> getLogs() {
    return logs;
  }

  public void setLogs(List<@Valid InfrastructureJobLog> logs) {
    this.logs = logs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InfrastructureRecalcJob infrastructureRecalcJob = (InfrastructureRecalcJob) o;
    return Objects.equals(this.jobId, infrastructureRecalcJob.jobId) &&
        Objects.equals(this.status, infrastructureRecalcJob.status) &&
        Objects.equals(this.trigger, infrastructureRecalcJob.trigger) &&
        Objects.equals(this.priority, infrastructureRecalcJob.priority) &&
        Objects.equals(this.dryRun, infrastructureRecalcJob.dryRun) &&
        Objects.equals(this.submittedAt, infrastructureRecalcJob.submittedAt) &&
        Objects.equals(this.startedAt, infrastructureRecalcJob.startedAt) &&
        Objects.equals(this.finishedAt, infrastructureRecalcJob.finishedAt) &&
        Objects.equals(this.progress, infrastructureRecalcJob.progress) &&
        Objects.equals(this.affectedDistricts, infrastructureRecalcJob.affectedDistricts) &&
        Objects.equals(this.summary, infrastructureRecalcJob.summary) &&
        Objects.equals(this.logs, infrastructureRecalcJob.logs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jobId, status, trigger, priority, dryRun, submittedAt, startedAt, finishedAt, progress, affectedDistricts, summary, logs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InfrastructureRecalcJob {\n");
    sb.append("    jobId: ").append(toIndentedString(jobId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    trigger: ").append(toIndentedString(trigger)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    dryRun: ").append(toIndentedString(dryRun)).append("\n");
    sb.append("    submittedAt: ").append(toIndentedString(submittedAt)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    finishedAt: ").append(toIndentedString(finishedAt)).append("\n");
    sb.append("    progress: ").append(toIndentedString(progress)).append("\n");
    sb.append("    affectedDistricts: ").append(toIndentedString(affectedDistricts)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    logs: ").append(toIndentedString(logs)).append("\n");
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

