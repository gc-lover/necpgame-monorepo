package com.necpgame.worldservice.model;

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
 * ImpactRecalculateJob
 */


public class ImpactRecalculateJob {

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
   * Gets or Sets scope
   */
  public enum ScopeEnum {
    GLOBAL("global"),
    
    CITIES("cities"),
    
    FACTIONS("factions"),
    
    CUSTOM("custom");

    private final String value;

    ScopeEnum(String value) {
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
    public static ScopeEnum fromValue(String value) {
      for (ScopeEnum b : ScopeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ScopeEnum scope;

  private @Nullable Integer processed;

  private @Nullable Integer failed;

  @Valid
  private List<String> errors = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime finishedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  private @Nullable UUID requestedBy;

  public ImpactRecalculateJob() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ImpactRecalculateJob(UUID jobId, StatusEnum status, ScopeEnum scope, OffsetDateTime createdAt) {
    this.jobId = jobId;
    this.status = status;
    this.scope = scope;
    this.createdAt = createdAt;
  }

  public ImpactRecalculateJob jobId(UUID jobId) {
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

  public ImpactRecalculateJob status(StatusEnum status) {
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

  public ImpactRecalculateJob scope(ScopeEnum scope) {
    this.scope = scope;
    return this;
  }

  /**
   * Get scope
   * @return scope
   */
  @NotNull 
  @Schema(name = "scope", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("scope")
  public ScopeEnum getScope() {
    return scope;
  }

  public void setScope(ScopeEnum scope) {
    this.scope = scope;
  }

  public ImpactRecalculateJob processed(@Nullable Integer processed) {
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

  public ImpactRecalculateJob failed(@Nullable Integer failed) {
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

  public ImpactRecalculateJob errors(List<String> errors) {
    this.errors = errors;
    return this;
  }

  public ImpactRecalculateJob addErrorsItem(String errorsItem) {
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

  public ImpactRecalculateJob startedAt(@Nullable OffsetDateTime startedAt) {
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

  public ImpactRecalculateJob finishedAt(@Nullable OffsetDateTime finishedAt) {
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

  public ImpactRecalculateJob createdAt(OffsetDateTime createdAt) {
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

  public ImpactRecalculateJob requestedBy(@Nullable UUID requestedBy) {
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
    ImpactRecalculateJob impactRecalculateJob = (ImpactRecalculateJob) o;
    return Objects.equals(this.jobId, impactRecalculateJob.jobId) &&
        Objects.equals(this.status, impactRecalculateJob.status) &&
        Objects.equals(this.scope, impactRecalculateJob.scope) &&
        Objects.equals(this.processed, impactRecalculateJob.processed) &&
        Objects.equals(this.failed, impactRecalculateJob.failed) &&
        Objects.equals(this.errors, impactRecalculateJob.errors) &&
        Objects.equals(this.startedAt, impactRecalculateJob.startedAt) &&
        Objects.equals(this.finishedAt, impactRecalculateJob.finishedAt) &&
        Objects.equals(this.createdAt, impactRecalculateJob.createdAt) &&
        Objects.equals(this.requestedBy, impactRecalculateJob.requestedBy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jobId, status, scope, processed, failed, errors, startedAt, finishedAt, createdAt, requestedBy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImpactRecalculateJob {\n");
    sb.append("    jobId: ").append(toIndentedString(jobId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
    sb.append("    processed: ").append(toIndentedString(processed)).append("\n");
    sb.append("    failed: ").append(toIndentedString(failed)).append("\n");
    sb.append("    errors: ").append(toIndentedString(errors)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    finishedAt: ").append(toIndentedString(finishedAt)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

