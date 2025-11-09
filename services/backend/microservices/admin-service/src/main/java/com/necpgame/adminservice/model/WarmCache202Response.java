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
 * WarmCache202Response
 */

@JsonTypeName("warmCache_202_response")

public class WarmCache202Response {

  private @Nullable String jobId;

  private @Nullable Integer estimatedTime;

  public WarmCache202Response jobId(@Nullable String jobId) {
    this.jobId = jobId;
    return this;
  }

  /**
   * Get jobId
   * @return jobId
   */
  
  @Schema(name = "job_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("job_id")
  public @Nullable String getJobId() {
    return jobId;
  }

  public void setJobId(@Nullable String jobId) {
    this.jobId = jobId;
  }

  public WarmCache202Response estimatedTime(@Nullable Integer estimatedTime) {
    this.estimatedTime = estimatedTime;
    return this;
  }

  /**
   * Get estimatedTime
   * @return estimatedTime
   */
  
  @Schema(name = "estimated_time", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_time")
  public @Nullable Integer getEstimatedTime() {
    return estimatedTime;
  }

  public void setEstimatedTime(@Nullable Integer estimatedTime) {
    this.estimatedTime = estimatedTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WarmCache202Response warmCache202Response = (WarmCache202Response) o;
    return Objects.equals(this.jobId, warmCache202Response.jobId) &&
        Objects.equals(this.estimatedTime, warmCache202Response.estimatedTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jobId, estimatedTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarmCache202Response {\n");
    sb.append("    jobId: ").append(toIndentedString(jobId)).append("\n");
    sb.append("    estimatedTime: ").append(toIndentedString(estimatedTime)).append("\n");
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

