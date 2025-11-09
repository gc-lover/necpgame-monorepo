package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CompletePlayerOrderRequest
 */

@JsonTypeName("completePlayerOrder_request")

public class CompletePlayerOrderRequest {

  private @Nullable UUID executorId;

  private @Nullable Object completionProof;

  public CompletePlayerOrderRequest executorId(@Nullable UUID executorId) {
    this.executorId = executorId;
    return this;
  }

  /**
   * Get executorId
   * @return executorId
   */
  @Valid 
  @Schema(name = "executor_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("executor_id")
  public @Nullable UUID getExecutorId() {
    return executorId;
  }

  public void setExecutorId(@Nullable UUID executorId) {
    this.executorId = executorId;
  }

  public CompletePlayerOrderRequest completionProof(@Nullable Object completionProof) {
    this.completionProof = completionProof;
    return this;
  }

  /**
   * Доказательство выполнения
   * @return completionProof
   */
  
  @Schema(name = "completion_proof", description = "Доказательство выполнения", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completion_proof")
  public @Nullable Object getCompletionProof() {
    return completionProof;
  }

  public void setCompletionProof(@Nullable Object completionProof) {
    this.completionProof = completionProof;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompletePlayerOrderRequest completePlayerOrderRequest = (CompletePlayerOrderRequest) o;
    return Objects.equals(this.executorId, completePlayerOrderRequest.executorId) &&
        Objects.equals(this.completionProof, completePlayerOrderRequest.completionProof);
  }

  @Override
  public int hashCode() {
    return Objects.hash(executorId, completionProof);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompletePlayerOrderRequest {\n");
    sb.append("    executorId: ").append(toIndentedString(executorId)).append("\n");
    sb.append("    completionProof: ").append(toIndentedString(completionProof)).append("\n");
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

