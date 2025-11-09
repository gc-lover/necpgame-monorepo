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
 * ExecuteOrderViaNPCRequest
 */

@JsonTypeName("executeOrderViaNPC_request")

public class ExecuteOrderViaNPCRequest {

  private @Nullable UUID executorId;

  private @Nullable UUID hiredNpcId;

  public ExecuteOrderViaNPCRequest executorId(@Nullable UUID executorId) {
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

  public ExecuteOrderViaNPCRequest hiredNpcId(@Nullable UUID hiredNpcId) {
    this.hiredNpcId = hiredNpcId;
    return this;
  }

  /**
   * Get hiredNpcId
   * @return hiredNpcId
   */
  @Valid 
  @Schema(name = "hired_npc_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hired_npc_id")
  public @Nullable UUID getHiredNpcId() {
    return hiredNpcId;
  }

  public void setHiredNpcId(@Nullable UUID hiredNpcId) {
    this.hiredNpcId = hiredNpcId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExecuteOrderViaNPCRequest executeOrderViaNPCRequest = (ExecuteOrderViaNPCRequest) o;
    return Objects.equals(this.executorId, executeOrderViaNPCRequest.executorId) &&
        Objects.equals(this.hiredNpcId, executeOrderViaNPCRequest.hiredNpcId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(executorId, hiredNpcId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExecuteOrderViaNPCRequest {\n");
    sb.append("    executorId: ").append(toIndentedString(executorId)).append("\n");
    sb.append("    hiredNpcId: ").append(toIndentedString(hiredNpcId)).append("\n");
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

