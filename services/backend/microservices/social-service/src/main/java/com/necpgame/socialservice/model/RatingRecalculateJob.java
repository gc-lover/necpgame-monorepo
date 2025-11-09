package com.necpgame.socialservice.model;

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
 * RatingRecalculateJob
 */


public class RatingRecalculateJob {

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
   * Gets or Sets role
   */
  public enum RoleEnum {
    EXECUTOR("executor"),
    
    CLIENT("client"),
    
    BOTH("both");

    private final String value;

    RoleEnum(String value) {
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
    public static RoleEnum fromValue(String value) {
      for (RoleEnum b : RoleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RoleEnum role;

  private @Nullable Integer processedCount;

  private @Nullable Integer failedCount;

  @Valid
  private List<String> errors = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime finishedAt;

  private @Nullable UUID requestedBy;

  public RatingRecalculateJob() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RatingRecalculateJob(UUID jobId, StatusEnum status, OffsetDateTime createdAt) {
    this.jobId = jobId;
    this.status = status;
    this.createdAt = createdAt;
  }

  public RatingRecalculateJob jobId(UUID jobId) {
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

  public RatingRecalculateJob status(StatusEnum status) {
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

  public RatingRecalculateJob role(@Nullable RoleEnum role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable RoleEnum getRole() {
    return role;
  }

  public void setRole(@Nullable RoleEnum role) {
    this.role = role;
  }

  public RatingRecalculateJob processedCount(@Nullable Integer processedCount) {
    this.processedCount = processedCount;
    return this;
  }

  /**
   * Get processedCount
   * minimum: 0
   * @return processedCount
   */
  @Min(value = 0) 
  @Schema(name = "processedCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("processedCount")
  public @Nullable Integer getProcessedCount() {
    return processedCount;
  }

  public void setProcessedCount(@Nullable Integer processedCount) {
    this.processedCount = processedCount;
  }

  public RatingRecalculateJob failedCount(@Nullable Integer failedCount) {
    this.failedCount = failedCount;
    return this;
  }

  /**
   * Get failedCount
   * minimum: 0
   * @return failedCount
   */
  @Min(value = 0) 
  @Schema(name = "failedCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("failedCount")
  public @Nullable Integer getFailedCount() {
    return failedCount;
  }

  public void setFailedCount(@Nullable Integer failedCount) {
    this.failedCount = failedCount;
  }

  public RatingRecalculateJob errors(List<String> errors) {
    this.errors = errors;
    return this;
  }

  public RatingRecalculateJob addErrorsItem(String errorsItem) {
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

  public RatingRecalculateJob createdAt(OffsetDateTime createdAt) {
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

  public RatingRecalculateJob startedAt(@Nullable OffsetDateTime startedAt) {
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

  public RatingRecalculateJob finishedAt(@Nullable OffsetDateTime finishedAt) {
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

  public RatingRecalculateJob requestedBy(@Nullable UUID requestedBy) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatingRecalculateJob ratingRecalculateJob = (RatingRecalculateJob) o;
    return Objects.equals(this.jobId, ratingRecalculateJob.jobId) &&
        Objects.equals(this.status, ratingRecalculateJob.status) &&
        Objects.equals(this.role, ratingRecalculateJob.role) &&
        Objects.equals(this.processedCount, ratingRecalculateJob.processedCount) &&
        Objects.equals(this.failedCount, ratingRecalculateJob.failedCount) &&
        Objects.equals(this.errors, ratingRecalculateJob.errors) &&
        Objects.equals(this.createdAt, ratingRecalculateJob.createdAt) &&
        Objects.equals(this.startedAt, ratingRecalculateJob.startedAt) &&
        Objects.equals(this.finishedAt, ratingRecalculateJob.finishedAt) &&
        Objects.equals(this.requestedBy, ratingRecalculateJob.requestedBy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jobId, status, role, processedCount, failedCount, errors, createdAt, startedAt, finishedAt, requestedBy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatingRecalculateJob {\n");
    sb.append("    jobId: ").append(toIndentedString(jobId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    processedCount: ").append(toIndentedString(processedCount)).append("\n");
    sb.append("    failedCount: ").append(toIndentedString(failedCount)).append("\n");
    sb.append("    errors: ").append(toIndentedString(errors)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    finishedAt: ").append(toIndentedString(finishedAt)).append("\n");
    sb.append("    requestedBy: ").append(toIndentedString(requestedBy)).append("\n");
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

