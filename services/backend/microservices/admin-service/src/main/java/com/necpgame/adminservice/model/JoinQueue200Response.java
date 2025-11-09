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
 * JoinQueue200Response
 */

@JsonTypeName("joinQueue_200_response")

public class JoinQueue200Response {

  private @Nullable String queueId;

  private @Nullable Integer position;

  private @Nullable BigDecimal estimatedWaitTime;

  public JoinQueue200Response queueId(@Nullable String queueId) {
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

  public JoinQueue200Response position(@Nullable Integer position) {
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

  public JoinQueue200Response estimatedWaitTime(@Nullable BigDecimal estimatedWaitTime) {
    this.estimatedWaitTime = estimatedWaitTime;
    return this;
  }

  /**
   * Оценка времени ожидания (секунды)
   * @return estimatedWaitTime
   */
  @Valid 
  @Schema(name = "estimated_wait_time", description = "Оценка времени ожидания (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_wait_time")
  public @Nullable BigDecimal getEstimatedWaitTime() {
    return estimatedWaitTime;
  }

  public void setEstimatedWaitTime(@Nullable BigDecimal estimatedWaitTime) {
    this.estimatedWaitTime = estimatedWaitTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JoinQueue200Response joinQueue200Response = (JoinQueue200Response) o;
    return Objects.equals(this.queueId, joinQueue200Response.queueId) &&
        Objects.equals(this.position, joinQueue200Response.position) &&
        Objects.equals(this.estimatedWaitTime, joinQueue200Response.estimatedWaitTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(queueId, position, estimatedWaitTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JoinQueue200Response {\n");
    sb.append("    queueId: ").append(toIndentedString(queueId)).append("\n");
    sb.append("    position: ").append(toIndentedString(position)).append("\n");
    sb.append("    estimatedWaitTime: ").append(toIndentedString(estimatedWaitTime)).append("\n");
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

