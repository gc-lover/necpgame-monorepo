package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetQueueStatus200Response
 */

@JsonTypeName("getQueueStatus_200_response")

public class GetQueueStatus200Response {

  private @Nullable Boolean inQueue;

  private @Nullable String queueId;

  private @Nullable String activityType;

  private @Nullable Integer position;

  private @Nullable BigDecimal estimatedWaitTime;

  private @Nullable BigDecimal timeInQueue;

  public GetQueueStatus200Response inQueue(@Nullable Boolean inQueue) {
    this.inQueue = inQueue;
    return this;
  }

  /**
   * Get inQueue
   * @return inQueue
   */
  
  @Schema(name = "in_queue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("in_queue")
  public @Nullable Boolean getInQueue() {
    return inQueue;
  }

  public void setInQueue(@Nullable Boolean inQueue) {
    this.inQueue = inQueue;
  }

  public GetQueueStatus200Response queueId(@Nullable String queueId) {
    this.queueId = queueId;
    return this;
  }

  /**
   * Get queueId
   * @return queueId
   */
  
  @Schema(name = "queue_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("queue_id")
  public @Nullable String getQueueId() {
    return queueId;
  }

  public void setQueueId(@Nullable String queueId) {
    this.queueId = queueId;
  }

  public GetQueueStatus200Response activityType(@Nullable String activityType) {
    this.activityType = activityType;
    return this;
  }

  /**
   * Get activityType
   * @return activityType
   */
  
  @Schema(name = "activity_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activity_type")
  public @Nullable String getActivityType() {
    return activityType;
  }

  public void setActivityType(@Nullable String activityType) {
    this.activityType = activityType;
  }

  public GetQueueStatus200Response position(@Nullable Integer position) {
    this.position = position;
    return this;
  }

  /**
   * Get position
   * @return position
   */
  
  @Schema(name = "position", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("position")
  public @Nullable Integer getPosition() {
    return position;
  }

  public void setPosition(@Nullable Integer position) {
    this.position = position;
  }

  public GetQueueStatus200Response estimatedWaitTime(@Nullable BigDecimal estimatedWaitTime) {
    this.estimatedWaitTime = estimatedWaitTime;
    return this;
  }

  /**
   * Get estimatedWaitTime
   * @return estimatedWaitTime
   */
  @Valid 
  @Schema(name = "estimated_wait_time", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_wait_time")
  public @Nullable BigDecimal getEstimatedWaitTime() {
    return estimatedWaitTime;
  }

  public void setEstimatedWaitTime(@Nullable BigDecimal estimatedWaitTime) {
    this.estimatedWaitTime = estimatedWaitTime;
  }

  public GetQueueStatus200Response timeInQueue(@Nullable BigDecimal timeInQueue) {
    this.timeInQueue = timeInQueue;
    return this;
  }

  /**
   * Время в очереди (секунды)
   * @return timeInQueue
   */
  @Valid 
  @Schema(name = "time_in_queue", description = "Время в очереди (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_in_queue")
  public @Nullable BigDecimal getTimeInQueue() {
    return timeInQueue;
  }

  public void setTimeInQueue(@Nullable BigDecimal timeInQueue) {
    this.timeInQueue = timeInQueue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetQueueStatus200Response getQueueStatus200Response = (GetQueueStatus200Response) o;
    return Objects.equals(this.inQueue, getQueueStatus200Response.inQueue) &&
        Objects.equals(this.queueId, getQueueStatus200Response.queueId) &&
        Objects.equals(this.activityType, getQueueStatus200Response.activityType) &&
        Objects.equals(this.position, getQueueStatus200Response.position) &&
        Objects.equals(this.estimatedWaitTime, getQueueStatus200Response.estimatedWaitTime) &&
        Objects.equals(this.timeInQueue, getQueueStatus200Response.timeInQueue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inQueue, queueId, activityType, position, estimatedWaitTime, timeInQueue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetQueueStatus200Response {\n");
    sb.append("    inQueue: ").append(toIndentedString(inQueue)).append("\n");
    sb.append("    queueId: ").append(toIndentedString(queueId)).append("\n");
    sb.append("    activityType: ").append(toIndentedString(activityType)).append("\n");
    sb.append("    position: ").append(toIndentedString(position)).append("\n");
    sb.append("    estimatedWaitTime: ").append(toIndentedString(estimatedWaitTime)).append("\n");
    sb.append("    timeInQueue: ").append(toIndentedString(timeInQueue)).append("\n");
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

