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
 * OptimizationResultOptimizedScheduleInner
 */

@JsonTypeName("OptimizationResult_optimized_schedule_inner")

public class OptimizationResultOptimizedScheduleInner {

  private @Nullable Integer stageNumber;

  private @Nullable Integer quantity;

  private @Nullable String startTime;

  public OptimizationResultOptimizedScheduleInner stageNumber(@Nullable Integer stageNumber) {
    this.stageNumber = stageNumber;
    return this;
  }

  /**
   * Get stageNumber
   * @return stageNumber
   */
  
  @Schema(name = "stage_number", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stage_number")
  public @Nullable Integer getStageNumber() {
    return stageNumber;
  }

  public void setStageNumber(@Nullable Integer stageNumber) {
    this.stageNumber = stageNumber;
  }

  public OptimizationResultOptimizedScheduleInner quantity(@Nullable Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public @Nullable Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(@Nullable Integer quantity) {
    this.quantity = quantity;
  }

  public OptimizationResultOptimizedScheduleInner startTime(@Nullable String startTime) {
    this.startTime = startTime;
    return this;
  }

  /**
   * Get startTime
   * @return startTime
   */
  
  @Schema(name = "start_time", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("start_time")
  public @Nullable String getStartTime() {
    return startTime;
  }

  public void setStartTime(@Nullable String startTime) {
    this.startTime = startTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OptimizationResultOptimizedScheduleInner optimizationResultOptimizedScheduleInner = (OptimizationResultOptimizedScheduleInner) o;
    return Objects.equals(this.stageNumber, optimizationResultOptimizedScheduleInner.stageNumber) &&
        Objects.equals(this.quantity, optimizationResultOptimizedScheduleInner.quantity) &&
        Objects.equals(this.startTime, optimizationResultOptimizedScheduleInner.startTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stageNumber, quantity, startTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OptimizationResultOptimizedScheduleInner {\n");
    sb.append("    stageNumber: ").append(toIndentedString(stageNumber)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
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

