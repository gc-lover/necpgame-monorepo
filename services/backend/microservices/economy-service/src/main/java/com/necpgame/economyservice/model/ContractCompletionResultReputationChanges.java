package com.necpgame.economyservice.model;

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
 * ContractCompletionResultReputationChanges
 */

@JsonTypeName("ContractCompletionResult_reputation_changes")

public class ContractCompletionResultReputationChanges {

  private @Nullable Integer creator;

  private @Nullable Integer executor;

  public ContractCompletionResultReputationChanges creator(@Nullable Integer creator) {
    this.creator = creator;
    return this;
  }

  /**
   * Get creator
   * @return creator
   */
  
  @Schema(name = "creator", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("creator")
  public @Nullable Integer getCreator() {
    return creator;
  }

  public void setCreator(@Nullable Integer creator) {
    this.creator = creator;
  }

  public ContractCompletionResultReputationChanges executor(@Nullable Integer executor) {
    this.executor = executor;
    return this;
  }

  /**
   * Get executor
   * @return executor
   */
  
  @Schema(name = "executor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("executor")
  public @Nullable Integer getExecutor() {
    return executor;
  }

  public void setExecutor(@Nullable Integer executor) {
    this.executor = executor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContractCompletionResultReputationChanges contractCompletionResultReputationChanges = (ContractCompletionResultReputationChanges) o;
    return Objects.equals(this.creator, contractCompletionResultReputationChanges.creator) &&
        Objects.equals(this.executor, contractCompletionResultReputationChanges.executor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(creator, executor);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContractCompletionResultReputationChanges {\n");
    sb.append("    creator: ").append(toIndentedString(creator)).append("\n");
    sb.append("    executor: ").append(toIndentedString(executor)).append("\n");
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

