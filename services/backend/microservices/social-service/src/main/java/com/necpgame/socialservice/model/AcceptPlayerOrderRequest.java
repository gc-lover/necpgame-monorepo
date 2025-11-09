package com.necpgame.socialservice.model;

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
 * AcceptPlayerOrderRequest
 */

@JsonTypeName("acceptPlayerOrder_request")

public class AcceptPlayerOrderRequest {

  private String executorId;

  public AcceptPlayerOrderRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AcceptPlayerOrderRequest(String executorId) {
    this.executorId = executorId;
  }

  public AcceptPlayerOrderRequest executorId(String executorId) {
    this.executorId = executorId;
    return this;
  }

  /**
   * Get executorId
   * @return executorId
   */
  @NotNull 
  @Schema(name = "executor_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("executor_id")
  public String getExecutorId() {
    return executorId;
  }

  public void setExecutorId(String executorId) {
    this.executorId = executorId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AcceptPlayerOrderRequest acceptPlayerOrderRequest = (AcceptPlayerOrderRequest) o;
    return Objects.equals(this.executorId, acceptPlayerOrderRequest.executorId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(executorId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AcceptPlayerOrderRequest {\n");
    sb.append("    executorId: ").append(toIndentedString(executorId)).append("\n");
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

