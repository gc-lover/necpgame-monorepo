package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AssignTaskRequest
 */

@JsonTypeName("assignTask_request")

public class AssignTaskRequest {

  /**
   * Gets or Sets taskType
   */
  public enum TaskTypeEnum {
    MANUAL("manual"),
    
    TEMPLATE("template"),
    
    AUTOMATED("automated");

    private final String value;

    TaskTypeEnum(String value) {
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
    public static TaskTypeEnum fromValue(String value) {
      for (TaskTypeEnum b : TaskTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TaskTypeEnum taskType;

  private @Nullable String scenarioId;

  private @Nullable Object parameters;

  public AssignTaskRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AssignTaskRequest(TaskTypeEnum taskType) {
    this.taskType = taskType;
  }

  public AssignTaskRequest taskType(TaskTypeEnum taskType) {
    this.taskType = taskType;
    return this;
  }

  /**
   * Get taskType
   * @return taskType
   */
  @NotNull 
  @Schema(name = "task_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("task_type")
  public TaskTypeEnum getTaskType() {
    return taskType;
  }

  public void setTaskType(TaskTypeEnum taskType) {
    this.taskType = taskType;
  }

  public AssignTaskRequest scenarioId(@Nullable String scenarioId) {
    this.scenarioId = scenarioId;
    return this;
  }

  /**
   * Get scenarioId
   * @return scenarioId
   */
  
  @Schema(name = "scenario_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scenario_id")
  public @Nullable String getScenarioId() {
    return scenarioId;
  }

  public void setScenarioId(@Nullable String scenarioId) {
    this.scenarioId = scenarioId;
  }

  public AssignTaskRequest parameters(@Nullable Object parameters) {
    this.parameters = parameters;
    return this;
  }

  /**
   * Get parameters
   * @return parameters
   */
  
  @Schema(name = "parameters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("parameters")
  public @Nullable Object getParameters() {
    return parameters;
  }

  public void setParameters(@Nullable Object parameters) {
    this.parameters = parameters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AssignTaskRequest assignTaskRequest = (AssignTaskRequest) o;
    return Objects.equals(this.taskType, assignTaskRequest.taskType) &&
        Objects.equals(this.scenarioId, assignTaskRequest.scenarioId) &&
        Objects.equals(this.parameters, assignTaskRequest.parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(taskType, scenarioId, parameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssignTaskRequest {\n");
    sb.append("    taskType: ").append(toIndentedString(taskType)).append("\n");
    sb.append("    scenarioId: ").append(toIndentedString(scenarioId)).append("\n");
    sb.append("    parameters: ").append(toIndentedString(parameters)).append("\n");
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

