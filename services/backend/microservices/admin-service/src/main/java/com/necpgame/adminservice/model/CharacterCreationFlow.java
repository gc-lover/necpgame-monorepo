package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.CharacterCreationFlowStepsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CharacterCreationFlow
 */


public class CharacterCreationFlow {

  @Valid
  private List<@Valid CharacterCreationFlowStepsInner> steps = new ArrayList<>();

  private @Nullable Integer totalSteps;

  private @Nullable Integer estimatedTimeMinutes;

  public CharacterCreationFlow steps(List<@Valid CharacterCreationFlowStepsInner> steps) {
    this.steps = steps;
    return this;
  }

  public CharacterCreationFlow addStepsItem(CharacterCreationFlowStepsInner stepsItem) {
    if (this.steps == null) {
      this.steps = new ArrayList<>();
    }
    this.steps.add(stepsItem);
    return this;
  }

  /**
   * Get steps
   * @return steps
   */
  @Valid 
  @Schema(name = "steps", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("steps")
  public List<@Valid CharacterCreationFlowStepsInner> getSteps() {
    return steps;
  }

  public void setSteps(List<@Valid CharacterCreationFlowStepsInner> steps) {
    this.steps = steps;
  }

  public CharacterCreationFlow totalSteps(@Nullable Integer totalSteps) {
    this.totalSteps = totalSteps;
    return this;
  }

  /**
   * Get totalSteps
   * @return totalSteps
   */
  
  @Schema(name = "total_steps", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_steps")
  public @Nullable Integer getTotalSteps() {
    return totalSteps;
  }

  public void setTotalSteps(@Nullable Integer totalSteps) {
    this.totalSteps = totalSteps;
  }

  public CharacterCreationFlow estimatedTimeMinutes(@Nullable Integer estimatedTimeMinutes) {
    this.estimatedTimeMinutes = estimatedTimeMinutes;
    return this;
  }

  /**
   * Get estimatedTimeMinutes
   * @return estimatedTimeMinutes
   */
  
  @Schema(name = "estimated_time_minutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_time_minutes")
  public @Nullable Integer getEstimatedTimeMinutes() {
    return estimatedTimeMinutes;
  }

  public void setEstimatedTimeMinutes(@Nullable Integer estimatedTimeMinutes) {
    this.estimatedTimeMinutes = estimatedTimeMinutes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterCreationFlow characterCreationFlow = (CharacterCreationFlow) o;
    return Objects.equals(this.steps, characterCreationFlow.steps) &&
        Objects.equals(this.totalSteps, characterCreationFlow.totalSteps) &&
        Objects.equals(this.estimatedTimeMinutes, characterCreationFlow.estimatedTimeMinutes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(steps, totalSteps, estimatedTimeMinutes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterCreationFlow {\n");
    sb.append("    steps: ").append(toIndentedString(steps)).append("\n");
    sb.append("    totalSteps: ").append(toIndentedString(totalSteps)).append("\n");
    sb.append("    estimatedTimeMinutes: ").append(toIndentedString(estimatedTimeMinutes)).append("\n");
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

