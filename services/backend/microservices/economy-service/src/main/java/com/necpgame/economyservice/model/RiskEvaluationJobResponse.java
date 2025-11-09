package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * RiskEvaluationJobResponse
 */


public class RiskEvaluationJobResponse {

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

  private @Nullable Integer processed;

  private @Nullable Integer failed;

  @Valid
  private List<String> errors = new ArrayList<>();

  private @Nullable UUID requestedBy;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime finishedAt;

  public RiskEvaluationJobResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RiskEvaluationJobResponse(UUID jobId, StatusEnum status) {
    this.jobId = jobId;
    this.status = status;
  }

  public RiskEvaluationJobResponse jobId(UUID jobId) {
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

  public RiskEvaluationJobResponse status(StatusEnum status) {
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

  public RiskEvaluationJobResponse processed(@Nullable Integer processed) {
    this.processed = processed;
    return this;
  }

  /**
   * Get processed
   * minimum: 0
   * @return processed
   */
  @Min(value = 0) 
  @Schema(name = "processed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("processed")
  public @Nullable Integer getProcessed() {
    return processed;
  }

  public void setProcessed(@Nullable Integer processed) {
    this.processed = processed;
  }

  public RiskEvaluationJobResponse failed(@Nullable Integer failed) {
    this.failed = failed;
    return this;
  }

  /**
   * Get failed
   * minimum: 0
   * @return failed
   */
  @Min(value = 0) 
  @Schema(name = "failed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("failed")
  public @Nullable Integer getFailed() {
    return failed;
  }

  public void setFailed(@Nullable Integer failed) {
    this.failed = failed;
  }

  public RiskEvaluationJobResponse errors(List<String> errors) {
    this.errors = errors;
    return this;
  }

  public RiskEvaluationJobResponse addErrorsItem(String errorsItem) {
    if (this.errors == null) {
      this.errors = new ArrayList<>();
    }
    this.errors.add(errorsItem);
    return this;
  }

  /**
   * Get errors
   * @return errors
   */
  
  @Schema(name = "errors", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("errors")
  public List<String> getErrors() {
    return errors;
  }

  public void setErrors(List<String> errors) {
    this.errors = errors;
  }

  public RiskEvaluationJobResponse requestedBy(@Nullable UUID requestedBy) {
    this.requestedBy = requestedBy;
    return this;
  }

  /**
   * Get requestedBy
   * @return requestedBy
   */
  @Valid 
  @Schema(name = "requestedBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requestedBy")
  public @Nullable UUID getRequestedBy() {
    return requestedBy;
  }

  public void setRequestedBy(@Nullable UUID requestedBy) {
    this.requestedBy = requestedBy;
  }

  public RiskEvaluationJobResponse createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public RiskEvaluationJobResponse startedAt(@Nullable OffsetDateTime startedAt) {
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

  public RiskEvaluationJobResponse finishedAt(@Nullable OffsetDateTime finishedAt) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiskEvaluationJobResponse riskEvaluationJobResponse = (RiskEvaluationJobResponse) o;
    return Objects.equals(this.jobId, riskEvaluationJobResponse.jobId) &&
        Objects.equals(this.status, riskEvaluationJobResponse.status) &&
        Objects.equals(this.processed, riskEvaluationJobResponse.processed) &&
        Objects.equals(this.failed, riskEvaluationJobResponse.failed) &&
        Objects.equals(this.errors, riskEvaluationJobResponse.errors) &&
        Objects.equals(this.requestedBy, riskEvaluationJobResponse.requestedBy) &&
        Objects.equals(this.createdAt, riskEvaluationJobResponse.createdAt) &&
        Objects.equals(this.startedAt, riskEvaluationJobResponse.startedAt) &&
        Objects.equals(this.finishedAt, riskEvaluationJobResponse.finishedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jobId, status, processed, failed, errors, requestedBy, createdAt, startedAt, finishedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiskEvaluationJobResponse {\n");
    sb.append("    jobId: ").append(toIndentedString(jobId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    processed: ").append(toIndentedString(processed)).append("\n");
    sb.append("    failed: ").append(toIndentedString(failed)).append("\n");
    sb.append("    errors: ").append(toIndentedString(errors)).append("\n");
    sb.append("    requestedBy: ").append(toIndentedString(requestedBy)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    finishedAt: ").append(toIndentedString(finishedAt)).append("\n");
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

