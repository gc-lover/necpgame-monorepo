package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class AssignTaskRequest {

  /**
   * Gets or Sets taskType
   */
  public enum TaskTypeEnum {
    COMBAT_SUPPORT("COMBAT_SUPPORT"),
    
    CRAFTING("CRAFTING"),
    
    TRADING("TRADING"),
    
    PROTECTION("PROTECTION"),
    
    TRANSPORT("TRANSPORT"),
    
    GATHERING("GATHERING");

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

  @Valid
  private Map<String, Object> taskDetails = new HashMap<>();

  private @Nullable Integer durationHours;

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

  public AssignTaskRequest taskDetails(Map<String, Object> taskDetails) {
    this.taskDetails = taskDetails;
    return this;
  }

  public AssignTaskRequest putTaskDetailsItem(String key, Object taskDetailsItem) {
    if (this.taskDetails == null) {
      this.taskDetails = new HashMap<>();
    }
    this.taskDetails.put(key, taskDetailsItem);
    return this;
  }

  /**
   * Get taskDetails
   * @return taskDetails
   */
  
  @Schema(name = "task_details", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("task_details")
  public Map<String, Object> getTaskDetails() {
    return taskDetails;
  }

  public void setTaskDetails(Map<String, Object> taskDetails) {
    this.taskDetails = taskDetails;
  }

  public AssignTaskRequest durationHours(@Nullable Integer durationHours) {
    this.durationHours = durationHours;
    return this;
  }

  /**
   * Get durationHours
   * @return durationHours
   */
  
  @Schema(name = "duration_hours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration_hours")
  public @Nullable Integer getDurationHours() {
    return durationHours;
  }

  public void setDurationHours(@Nullable Integer durationHours) {
    this.durationHours = durationHours;
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
        Objects.equals(this.taskDetails, assignTaskRequest.taskDetails) &&
        Objects.equals(this.durationHours, assignTaskRequest.durationHours);
  }

  @Override
  public int hashCode() {
    return Objects.hash(taskType, taskDetails, durationHours);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssignTaskRequest {\n");
    sb.append("    taskType: ").append(toIndentedString(taskType)).append("\n");
    sb.append("    taskDetails: ").append(toIndentedString(taskDetails)).append("\n");
    sb.append("    durationHours: ").append(toIndentedString(durationHours)).append("\n");
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

