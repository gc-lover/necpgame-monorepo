package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.PopulationJobLog;
import com.necpgame.worldservice.model.PopulationMetric;
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
 * PopulationRecalcJob
 */


public class PopulationRecalcJob {

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

  private @Nullable UUID cityId;

  /**
   * Gets or Sets trigger
   */
  public enum TriggerEnum {
    EVENT("event"),
    
    MANUAL("manual"),
    
    PLAYER_IMPACT("player-impact"),
    
    SCHEDULE("schedule");

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

  private @Nullable String resultSummary;

  @Valid
  private List<@Valid PopulationJobLog> logs = new ArrayList<>();

  @Valid
  private List<@Valid PopulationMetric> metrics = new ArrayList<>();

  public PopulationRecalcJob() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PopulationRecalcJob(UUID jobId, StatusEnum status, OffsetDateTime submittedAt) {
    this.jobId = jobId;
    this.status = status;
    this.submittedAt = submittedAt;
  }

  public PopulationRecalcJob jobId(UUID jobId) {
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

  public PopulationRecalcJob status(StatusEnum status) {
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

  public PopulationRecalcJob cityId(@Nullable UUID cityId) {
    this.cityId = cityId;
    return this;
  }

  /**
   * Get cityId
   * @return cityId
   */
  @Valid 
  @Schema(name = "cityId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cityId")
  public @Nullable UUID getCityId() {
    return cityId;
  }

  public void setCityId(@Nullable UUID cityId) {
    this.cityId = cityId;
  }

  public PopulationRecalcJob trigger(@Nullable TriggerEnum trigger) {
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

  public PopulationRecalcJob dryRun(@Nullable Boolean dryRun) {
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

  public PopulationRecalcJob priority(@Nullable PriorityEnum priority) {
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

  public PopulationRecalcJob submittedAt(OffsetDateTime submittedAt) {
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

  public PopulationRecalcJob startedAt(@Nullable OffsetDateTime startedAt) {
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

  public PopulationRecalcJob finishedAt(@Nullable OffsetDateTime finishedAt) {
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

  public PopulationRecalcJob progress(@Nullable BigDecimal progress) {
    this.progress = progress;
    return this;
  }

  /**
   * Get progress
   * minimum: 0
   * maximum: 1
   * @return progress
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "progress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progress")
  public @Nullable BigDecimal getProgress() {
    return progress;
  }

  public void setProgress(@Nullable BigDecimal progress) {
    this.progress = progress;
  }

  public PopulationRecalcJob resultSummary(@Nullable String resultSummary) {
    this.resultSummary = resultSummary;
    return this;
  }

  /**
   * Get resultSummary
   * @return resultSummary
   */
  
  @Schema(name = "resultSummary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resultSummary")
  public @Nullable String getResultSummary() {
    return resultSummary;
  }

  public void setResultSummary(@Nullable String resultSummary) {
    this.resultSummary = resultSummary;
  }

  public PopulationRecalcJob logs(List<@Valid PopulationJobLog> logs) {
    this.logs = logs;
    return this;
  }

  public PopulationRecalcJob addLogsItem(PopulationJobLog logsItem) {
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
  public List<@Valid PopulationJobLog> getLogs() {
    return logs;
  }

  public void setLogs(List<@Valid PopulationJobLog> logs) {
    this.logs = logs;
  }

  public PopulationRecalcJob metrics(List<@Valid PopulationMetric> metrics) {
    this.metrics = metrics;
    return this;
  }

  public PopulationRecalcJob addMetricsItem(PopulationMetric metricsItem) {
    if (this.metrics == null) {
      this.metrics = new ArrayList<>();
    }
    this.metrics.add(metricsItem);
    return this;
  }

  /**
   * Get metrics
   * @return metrics
   */
  @Valid 
  @Schema(name = "metrics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metrics")
  public List<@Valid PopulationMetric> getMetrics() {
    return metrics;
  }

  public void setMetrics(List<@Valid PopulationMetric> metrics) {
    this.metrics = metrics;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PopulationRecalcJob populationRecalcJob = (PopulationRecalcJob) o;
    return Objects.equals(this.jobId, populationRecalcJob.jobId) &&
        Objects.equals(this.status, populationRecalcJob.status) &&
        Objects.equals(this.cityId, populationRecalcJob.cityId) &&
        Objects.equals(this.trigger, populationRecalcJob.trigger) &&
        Objects.equals(this.dryRun, populationRecalcJob.dryRun) &&
        Objects.equals(this.priority, populationRecalcJob.priority) &&
        Objects.equals(this.submittedAt, populationRecalcJob.submittedAt) &&
        Objects.equals(this.startedAt, populationRecalcJob.startedAt) &&
        Objects.equals(this.finishedAt, populationRecalcJob.finishedAt) &&
        Objects.equals(this.progress, populationRecalcJob.progress) &&
        Objects.equals(this.resultSummary, populationRecalcJob.resultSummary) &&
        Objects.equals(this.logs, populationRecalcJob.logs) &&
        Objects.equals(this.metrics, populationRecalcJob.metrics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jobId, status, cityId, trigger, dryRun, priority, submittedAt, startedAt, finishedAt, progress, resultSummary, logs, metrics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PopulationRecalcJob {\n");
    sb.append("    jobId: ").append(toIndentedString(jobId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    cityId: ").append(toIndentedString(cityId)).append("\n");
    sb.append("    trigger: ").append(toIndentedString(trigger)).append("\n");
    sb.append("    dryRun: ").append(toIndentedString(dryRun)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    submittedAt: ").append(toIndentedString(submittedAt)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    finishedAt: ").append(toIndentedString(finishedAt)).append("\n");
    sb.append("    progress: ").append(toIndentedString(progress)).append("\n");
    sb.append("    resultSummary: ").append(toIndentedString(resultSummary)).append("\n");
    sb.append("    logs: ").append(toIndentedString(logs)).append("\n");
    sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
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

