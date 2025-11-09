package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.ProgressionTriggerResultStageTransition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * Результат триггера прогрессии
 */

@Schema(name = "ProgressionTriggerResult", description = "Результат триггера прогрессии")

public class ProgressionTriggerResult {

  private Float lossApplied;

  private JsonNullable<ProgressionTriggerResultStageTransition> stageTransition = JsonNullable.<ProgressionTriggerResultStageTransition>undefined();

  @Valid
  private List<UUID> symptomsTriggered = new ArrayList<>();

  public ProgressionTriggerResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ProgressionTriggerResult(Float lossApplied, ProgressionTriggerResultStageTransition stageTransition) {
    this.lossApplied = lossApplied;
    this.stageTransition = JsonNullable.of(stageTransition);
  }

  public ProgressionTriggerResult lossApplied(Float lossApplied) {
    this.lossApplied = lossApplied;
    return this;
  }

  /**
   * Примененная потеря человечности
   * minimum: 0
   * @return lossApplied
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "loss_applied", description = "Примененная потеря человечности", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("loss_applied")
  public Float getLossApplied() {
    return lossApplied;
  }

  public void setLossApplied(Float lossApplied) {
    this.lossApplied = lossApplied;
  }

  public ProgressionTriggerResult stageTransition(ProgressionTriggerResultStageTransition stageTransition) {
    this.stageTransition = JsonNullable.of(stageTransition);
    return this;
  }

  /**
   * Get stageTransition
   * @return stageTransition
   */
  @NotNull @Valid 
  @Schema(name = "stage_transition", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("stage_transition")
  public JsonNullable<ProgressionTriggerResultStageTransition> getStageTransition() {
    return stageTransition;
  }

  public void setStageTransition(JsonNullable<ProgressionTriggerResultStageTransition> stageTransition) {
    this.stageTransition = stageTransition;
  }

  public ProgressionTriggerResult symptomsTriggered(List<UUID> symptomsTriggered) {
    this.symptomsTriggered = symptomsTriggered;
    return this;
  }

  public ProgressionTriggerResult addSymptomsTriggeredItem(UUID symptomsTriggeredItem) {
    if (this.symptomsTriggered == null) {
      this.symptomsTriggered = new ArrayList<>();
    }
    this.symptomsTriggered.add(symptomsTriggeredItem);
    return this;
  }

  /**
   * Идентификаторы сработавших симптомов
   * @return symptomsTriggered
   */
  @Valid 
  @Schema(name = "symptoms_triggered", description = "Идентификаторы сработавших симптомов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("symptoms_triggered")
  public List<UUID> getSymptomsTriggered() {
    return symptomsTriggered;
  }

  public void setSymptomsTriggered(List<UUID> symptomsTriggered) {
    this.symptomsTriggered = symptomsTriggered;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProgressionTriggerResult progressionTriggerResult = (ProgressionTriggerResult) o;
    return Objects.equals(this.lossApplied, progressionTriggerResult.lossApplied) &&
        Objects.equals(this.stageTransition, progressionTriggerResult.stageTransition) &&
        Objects.equals(this.symptomsTriggered, progressionTriggerResult.symptomsTriggered);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lossApplied, stageTransition, symptomsTriggered);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProgressionTriggerResult {\n");
    sb.append("    lossApplied: ").append(toIndentedString(lossApplied)).append("\n");
    sb.append("    stageTransition: ").append(toIndentedString(stageTransition)).append("\n");
    sb.append("    symptomsTriggered: ").append(toIndentedString(symptomsTriggered)).append("\n");
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

