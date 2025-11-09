package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
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
 * AssignTask200Response
 */

@JsonTypeName("assignTask_200_response")

public class AssignTask200Response {

  private @Nullable String taskId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime estimatedCompletion;

  public AssignTask200Response taskId(@Nullable String taskId) {
    this.taskId = taskId;
    return this;
  }

  /**
   * Get taskId
   * @return taskId
   */
  
  @Schema(name = "task_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("task_id")
  public @Nullable String getTaskId() {
    return taskId;
  }

  public void setTaskId(@Nullable String taskId) {
    this.taskId = taskId;
  }

  public AssignTask200Response estimatedCompletion(@Nullable OffsetDateTime estimatedCompletion) {
    this.estimatedCompletion = estimatedCompletion;
    return this;
  }

  /**
   * Get estimatedCompletion
   * @return estimatedCompletion
   */
  @Valid 
  @Schema(name = "estimated_completion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_completion")
  public @Nullable OffsetDateTime getEstimatedCompletion() {
    return estimatedCompletion;
  }

  public void setEstimatedCompletion(@Nullable OffsetDateTime estimatedCompletion) {
    this.estimatedCompletion = estimatedCompletion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AssignTask200Response assignTask200Response = (AssignTask200Response) o;
    return Objects.equals(this.taskId, assignTask200Response.taskId) &&
        Objects.equals(this.estimatedCompletion, assignTask200Response.estimatedCompletion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(taskId, estimatedCompletion);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssignTask200Response {\n");
    sb.append("    taskId: ").append(toIndentedString(taskId)).append("\n");
    sb.append("    estimatedCompletion: ").append(toIndentedString(estimatedCompletion)).append("\n");
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

