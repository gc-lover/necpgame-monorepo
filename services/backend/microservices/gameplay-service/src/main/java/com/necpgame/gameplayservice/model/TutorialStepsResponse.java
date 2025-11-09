package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.TutorialStep;
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
 * TutorialStepsResponse
 */


public class TutorialStepsResponse {

  @Valid
  private List<@Valid TutorialStep> steps = new ArrayList<>();

  private Integer currentStep;

  private Integer totalSteps;

  private Boolean canSkip;

  public TutorialStepsResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TutorialStepsResponse(List<@Valid TutorialStep> steps, Integer currentStep, Integer totalSteps, Boolean canSkip) {
    this.steps = steps;
    this.currentStep = currentStep;
    this.totalSteps = totalSteps;
    this.canSkip = canSkip;
  }

  public TutorialStepsResponse steps(List<@Valid TutorialStep> steps) {
    this.steps = steps;
    return this;
  }

  public TutorialStepsResponse addStepsItem(TutorialStep stepsItem) {
    if (this.steps == null) {
      this.steps = new ArrayList<>();
    }
    this.steps.add(stepsItem);
    return this;
  }

  /**
   * Список шагов туториала
   * @return steps
   */
  @NotNull @Valid 
  @Schema(name = "steps", description = "Список шагов туториала", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("steps")
  public List<@Valid TutorialStep> getSteps() {
    return steps;
  }

  public void setSteps(List<@Valid TutorialStep> steps) {
    this.steps = steps;
  }

  public TutorialStepsResponse currentStep(Integer currentStep) {
    this.currentStep = currentStep;
    return this;
  }

  /**
   * Текущий шаг туториала (0-based индекс)
   * minimum: 0
   * @return currentStep
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "currentStep", example = "0", description = "Текущий шаг туториала (0-based индекс)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("currentStep")
  public Integer getCurrentStep() {
    return currentStep;
  }

  public void setCurrentStep(Integer currentStep) {
    this.currentStep = currentStep;
  }

  public TutorialStepsResponse totalSteps(Integer totalSteps) {
    this.totalSteps = totalSteps;
    return this;
  }

  /**
   * Общее количество шагов
   * minimum: 1
   * @return totalSteps
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "totalSteps", example = "4", description = "Общее количество шагов", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("totalSteps")
  public Integer getTotalSteps() {
    return totalSteps;
  }

  public void setTotalSteps(Integer totalSteps) {
    this.totalSteps = totalSteps;
  }

  public TutorialStepsResponse canSkip(Boolean canSkip) {
    this.canSkip = canSkip;
    return this;
  }

  /**
   * Можно ли пропустить туториал
   * @return canSkip
   */
  @NotNull 
  @Schema(name = "canSkip", example = "true", description = "Можно ли пропустить туториал", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("canSkip")
  public Boolean getCanSkip() {
    return canSkip;
  }

  public void setCanSkip(Boolean canSkip) {
    this.canSkip = canSkip;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TutorialStepsResponse tutorialStepsResponse = (TutorialStepsResponse) o;
    return Objects.equals(this.steps, tutorialStepsResponse.steps) &&
        Objects.equals(this.currentStep, tutorialStepsResponse.currentStep) &&
        Objects.equals(this.totalSteps, tutorialStepsResponse.totalSteps) &&
        Objects.equals(this.canSkip, tutorialStepsResponse.canSkip);
  }

  @Override
  public int hashCode() {
    return Objects.hash(steps, currentStep, totalSteps, canSkip);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TutorialStepsResponse {\n");
    sb.append("    steps: ").append(toIndentedString(steps)).append("\n");
    sb.append("    currentStep: ").append(toIndentedString(currentStep)).append("\n");
    sb.append("    totalSteps: ").append(toIndentedString(totalSteps)).append("\n");
    sb.append("    canSkip: ").append(toIndentedString(canSkip)).append("\n");
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

