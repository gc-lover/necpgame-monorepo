package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ScheduleJobLog;
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
 * ScheduleRecalcJob
 */


public class ScheduleRecalcJob {

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

  /**
   * Gets or Sets trigger
   */
  public enum TriggerEnum {
    EVENT("event"),
    
    MANUAL("manual"),
    
    PLAYER_IMPACT("player-impact"),
    
    INFRASTRUCTURE_CHANGE("infrastructure-change");

    private final String value;

    TriggerEnum(String value) {
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
    public static TriggerEnum fromValue(String value) {
      for (TriggerEnum b : TriggerEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TriggerEnum trigger;

  private @Nullable Boolean dryRun;

  /**
   * Gets or Sets priority
   */
  public enum PriorityEnum {
    LOW("low"),
    
    NORMAL("normal"),
    
    HIGH("high"),
    
    URGENT("urgent");

    private final String value;

    PriorityEnum(String value) {
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
    public static PriorityEnum fromValue(String value) {
      for (PriorityEnum b : PriorityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PriorityEnum priority;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime submittedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime finishedAt;

  private @Nullable BigDecimal progress;

  private @Nullable Integer affectedNpcCount;

  @Valid
  private List<@Valid ScheduleJobLog> logs = new ArrayList<>();

  private @Nullable String summary;

  public ScheduleRecalcJob() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ScheduleRecalcJob(UUID jobId, StatusEnum status, OffsetDateTime submittedAt) {
    this.jobId = jobId;
    this.status = status;
    this.submittedAt = submittedAt;
  }

  public ScheduleRecalcJob jobId(UUID jobId) {
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

  public ScheduleRecalcJob status(StatusEnum status) {
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

  public ScheduleRecalcJob trigger(@Nullable TriggerEnum trigger) {
    this.trigger = trigger;
    return this;
  }

  /**
   * Get trigger
   * @return trigger
   */
  
  @Schema(name = "trigger", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trigger")
  public @Nullable TriggerEnum getTrigger() {
    return trigger;
  }

  public void setTrigger(@Nullable TriggerEnum trigger) {
    this.trigger = trigger;
  }

  public ScheduleRecalcJob dryRun(@Nullable Boolean dryRun) {
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

  public ScheduleRecalcJob priority(@Nullable PriorityEnum priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public @Nullable PriorityEnum getPriority() {
    return priority;
  }

  public void setPriority(@Nullable PriorityEnum priority) {
    this.priority = priority;
  }

  public ScheduleRecalcJob submittedAt(OffsetDateTime submittedAt) {
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

  public ScheduleRecalcJob startedAt(@Nullable OffsetDateTime startedAt) {
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

  public ScheduleRecalcJob finishedAt(@Nullable OffsetDateTime finishedAt) {
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

  public ScheduleRecalcJob progress(@Nullable BigDecimal progress) {
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

  public ScheduleRecalcJob affectedNpcCount(@Nullable Integer affectedNpcCount) {
    this.affectedNpcCount = affectedNpcCount;
    return this;
  }

  /**
   * Get affectedNpcCount
   * @return affectedNpcCount
   */
  
  @Schema(name = "affectedNpcCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affectedNpcCount")
  public @Nullable Integer getAffectedNpcCount() {
    return affectedNpcCount;
  }

  public void setAffectedNpcCount(@Nullable Integer affectedNpcCount) {
    this.affectedNpcCount = affectedNpcCount;
  }

  public ScheduleRecalcJob logs(List<@Valid ScheduleJobLog> logs) {
    this.logs = logs;
    return this;
  }

  public ScheduleRecalcJob addLogsItem(ScheduleJobLog logsItem) {
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
  public List<@Valid ScheduleJobLog> getLogs() {
    return logs;
  }

  public void setLogs(List<@Valid ScheduleJobLog> logs) {
    this.logs = logs;
  }

  public ScheduleRecalcJob summary(@Nullable String summary) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScheduleRecalcJob scheduleRecalcJob = (ScheduleRecalcJob) o;
    return Objects.equals(this.jobId, scheduleRecalcJob.jobId) &&
        Objects.equals(this.status, scheduleRecalcJob.status) &&
        Objects.equals(this.trigger, scheduleRecalcJob.trigger) &&
        Objects.equals(this.dryRun, scheduleRecalcJob.dryRun) &&
        Objects.equals(this.priority, scheduleRecalcJob.priority) &&
        Objects.equals(this.submittedAt, scheduleRecalcJob.submittedAt) &&
        Objects.equals(this.startedAt, scheduleRecalcJob.startedAt) &&
        Objects.equals(this.finishedAt, scheduleRecalcJob.finishedAt) &&
        Objects.equals(this.progress, scheduleRecalcJob.progress) &&
        Objects.equals(this.affectedNpcCount, scheduleRecalcJob.affectedNpcCount) &&
        Objects.equals(this.logs, scheduleRecalcJob.logs) &&
        Objects.equals(this.summary, scheduleRecalcJob.summary);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jobId, status, trigger, dryRun, priority, submittedAt, startedAt, finishedAt, progress, affectedNpcCount, logs, summary);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduleRecalcJob {\n");
    sb.append("    jobId: ").append(toIndentedString(jobId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    trigger: ").append(toIndentedString(trigger)).append("\n");
    sb.append("    dryRun: ").append(toIndentedString(dryRun)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    submittedAt: ").append(toIndentedString(submittedAt)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    finishedAt: ").append(toIndentedString(finishedAt)).append("\n");
    sb.append("    progress: ").append(toIndentedString(progress)).append("\n");
    sb.append("    affectedNpcCount: ").append(toIndentedString(affectedNpcCount)).append("\n");
    sb.append("    logs: ").append(toIndentedString(logs)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
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

