package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.ResetExecutionResultErrorsInner;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
 * ResetExecutionResult
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ResetExecutionResult {

  private @Nullable UUID resetId;

  private @Nullable String resetType;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime executionTime;

  private @Nullable Integer affectedPlayers;

  @Valid
  private List<String> itemsReset = new ArrayList<>();

  private @Nullable Integer executionDurationMs;

  @Valid
  private List<@Valid ResetExecutionResultErrorsInner> errors = new ArrayList<>();

  public ResetExecutionResult resetId(@Nullable UUID resetId) {
    this.resetId = resetId;
    return this;
  }

  /**
   * Get resetId
   * @return resetId
   */
  @Valid 
  @Schema(name = "reset_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reset_id")
  public @Nullable UUID getResetId() {
    return resetId;
  }

  public void setResetId(@Nullable UUID resetId) {
    this.resetId = resetId;
  }

  public ResetExecutionResult resetType(@Nullable String resetType) {
    this.resetType = resetType;
    return this;
  }

  /**
   * Get resetType
   * @return resetType
   */
  
  @Schema(name = "reset_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reset_type")
  public @Nullable String getResetType() {
    return resetType;
  }

  public void setResetType(@Nullable String resetType) {
    this.resetType = resetType;
  }

  public ResetExecutionResult executionTime(@Nullable OffsetDateTime executionTime) {
    this.executionTime = executionTime;
    return this;
  }

  /**
   * Get executionTime
   * @return executionTime
   */
  @Valid 
  @Schema(name = "execution_time", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("execution_time")
  public @Nullable OffsetDateTime getExecutionTime() {
    return executionTime;
  }

  public void setExecutionTime(@Nullable OffsetDateTime executionTime) {
    this.executionTime = executionTime;
  }

  public ResetExecutionResult affectedPlayers(@Nullable Integer affectedPlayers) {
    this.affectedPlayers = affectedPlayers;
    return this;
  }

  /**
   * Get affectedPlayers
   * @return affectedPlayers
   */
  
  @Schema(name = "affected_players", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affected_players")
  public @Nullable Integer getAffectedPlayers() {
    return affectedPlayers;
  }

  public void setAffectedPlayers(@Nullable Integer affectedPlayers) {
    this.affectedPlayers = affectedPlayers;
  }

  public ResetExecutionResult itemsReset(List<String> itemsReset) {
    this.itemsReset = itemsReset;
    return this;
  }

  public ResetExecutionResult addItemsResetItem(String itemsResetItem) {
    if (this.itemsReset == null) {
      this.itemsReset = new ArrayList<>();
    }
    this.itemsReset.add(itemsResetItem);
    return this;
  }

  /**
   * Get itemsReset
   * @return itemsReset
   */
  
  @Schema(name = "items_reset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items_reset")
  public List<String> getItemsReset() {
    return itemsReset;
  }

  public void setItemsReset(List<String> itemsReset) {
    this.itemsReset = itemsReset;
  }

  public ResetExecutionResult executionDurationMs(@Nullable Integer executionDurationMs) {
    this.executionDurationMs = executionDurationMs;
    return this;
  }

  /**
   * Get executionDurationMs
   * @return executionDurationMs
   */
  
  @Schema(name = "execution_duration_ms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("execution_duration_ms")
  public @Nullable Integer getExecutionDurationMs() {
    return executionDurationMs;
  }

  public void setExecutionDurationMs(@Nullable Integer executionDurationMs) {
    this.executionDurationMs = executionDurationMs;
  }

  public ResetExecutionResult errors(List<@Valid ResetExecutionResultErrorsInner> errors) {
    this.errors = errors;
    return this;
  }

  public ResetExecutionResult addErrorsItem(ResetExecutionResultErrorsInner errorsItem) {
    if (this.errors == null) {
      this.errors = new ArrayList<>();
    }
    this.errors.add(errorsItem);
    return this;
  }

  /**
   * Get errors
   * @return errors
   */
  @Valid 
  @Schema(name = "errors", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("errors")
  public List<@Valid ResetExecutionResultErrorsInner> getErrors() {
    return errors;
  }

  public void setErrors(List<@Valid ResetExecutionResultErrorsInner> errors) {
    this.errors = errors;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResetExecutionResult resetExecutionResult = (ResetExecutionResult) o;
    return Objects.equals(this.resetId, resetExecutionResult.resetId) &&
        Objects.equals(this.resetType, resetExecutionResult.resetType) &&
        Objects.equals(this.executionTime, resetExecutionResult.executionTime) &&
        Objects.equals(this.affectedPlayers, resetExecutionResult.affectedPlayers) &&
        Objects.equals(this.itemsReset, resetExecutionResult.itemsReset) &&
        Objects.equals(this.executionDurationMs, resetExecutionResult.executionDurationMs) &&
        Objects.equals(this.errors, resetExecutionResult.errors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resetId, resetType, executionTime, affectedPlayers, itemsReset, executionDurationMs, errors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResetExecutionResult {\n");
    sb.append("    resetId: ").append(toIndentedString(resetId)).append("\n");
    sb.append("    resetType: ").append(toIndentedString(resetType)).append("\n");
    sb.append("    executionTime: ").append(toIndentedString(executionTime)).append("\n");
    sb.append("    affectedPlayers: ").append(toIndentedString(affectedPlayers)).append("\n");
    sb.append("    itemsReset: ").append(toIndentedString(itemsReset)).append("\n");
    sb.append("    executionDurationMs: ").append(toIndentedString(executionDurationMs)).append("\n");
    sb.append("    errors: ").append(toIndentedString(errors)).append("\n");
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

