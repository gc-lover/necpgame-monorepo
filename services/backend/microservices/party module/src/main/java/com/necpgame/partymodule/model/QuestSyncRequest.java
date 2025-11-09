package com.necpgame.partymodule.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.partymodule.model.QuestSyncEventSharedObjectivesInner;
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
 * QuestSyncRequest
 */


public class QuestSyncRequest {

  private String step;

  private @Nullable Integer progress;

  /**
   * Gets or Sets completionState
   */
  public enum CompletionStateEnum {
    IN_PROGRESS("IN_PROGRESS"),
    
    COMPLETED("COMPLETED");

    private final String value;

    CompletionStateEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static CompletionStateEnum fromValue(String value) {
      for (CompletionStateEnum b : CompletionStateEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable CompletionStateEnum completionState;

  @Valid
  private List<@Valid QuestSyncEventSharedObjectivesInner> sharedObjectives = new ArrayList<>();

  public QuestSyncRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QuestSyncRequest(String step) {
    this.step = step;
  }

  public QuestSyncRequest step(String step) {
    this.step = step;
    return this;
  }

  /**
   * Get step
   * @return step
   */
  @NotNull 
  @Schema(name = "step", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("step")
  public String getStep() {
    return step;
  }

  public void setStep(String step) {
    this.step = step;
  }

  public QuestSyncRequest progress(@Nullable Integer progress) {
    this.progress = progress;
    return this;
  }

  /**
   * Get progress
   * @return progress
   */
  
  @Schema(name = "progress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progress")
  public @Nullable Integer getProgress() {
    return progress;
  }

  public void setProgress(@Nullable Integer progress) {
    this.progress = progress;
  }

  public QuestSyncRequest completionState(@Nullable CompletionStateEnum completionState) {
    this.completionState = completionState;
    return this;
  }

  /**
   * Get completionState
   * @return completionState
   */
  
  @Schema(name = "completionState", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completionState")
  public @Nullable CompletionStateEnum getCompletionState() {
    return completionState;
  }

  public void setCompletionState(@Nullable CompletionStateEnum completionState) {
    this.completionState = completionState;
  }

  public QuestSyncRequest sharedObjectives(List<@Valid QuestSyncEventSharedObjectivesInner> sharedObjectives) {
    this.sharedObjectives = sharedObjectives;
    return this;
  }

  public QuestSyncRequest addSharedObjectivesItem(QuestSyncEventSharedObjectivesInner sharedObjectivesItem) {
    if (this.sharedObjectives == null) {
      this.sharedObjectives = new ArrayList<>();
    }
    this.sharedObjectives.add(sharedObjectivesItem);
    return this;
  }

  /**
   * Get sharedObjectives
   * @return sharedObjectives
   */
  @Valid 
  @Schema(name = "sharedObjectives", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sharedObjectives")
  public List<@Valid QuestSyncEventSharedObjectivesInner> getSharedObjectives() {
    return sharedObjectives;
  }

  public void setSharedObjectives(List<@Valid QuestSyncEventSharedObjectivesInner> sharedObjectives) {
    this.sharedObjectives = sharedObjectives;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestSyncRequest questSyncRequest = (QuestSyncRequest) o;
    return Objects.equals(this.step, questSyncRequest.step) &&
        Objects.equals(this.progress, questSyncRequest.progress) &&
        Objects.equals(this.completionState, questSyncRequest.completionState) &&
        Objects.equals(this.sharedObjectives, questSyncRequest.sharedObjectives);
  }

  @Override
  public int hashCode() {
    return Objects.hash(step, progress, completionState, sharedObjectives);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestSyncRequest {\n");
    sb.append("    step: ").append(toIndentedString(step)).append("\n");
    sb.append("    progress: ").append(toIndentedString(progress)).append("\n");
    sb.append("    completionState: ").append(toIndentedString(completionState)).append("\n");
    sb.append("    sharedObjectives: ").append(toIndentedString(sharedObjectives)).append("\n");
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

