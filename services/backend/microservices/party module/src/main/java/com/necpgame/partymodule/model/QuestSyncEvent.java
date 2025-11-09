package com.necpgame.partymodule.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.partymodule.model.QuestSyncEventSharedObjectivesInner;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * QuestSyncEvent
 */


public class QuestSyncEvent {

  private @Nullable String questId;

  private @Nullable String step;

  private @Nullable Integer progress;

  @Valid
  private List<@Valid QuestSyncEventSharedObjectivesInner> sharedObjectives = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public QuestSyncEvent questId(@Nullable String questId) {
    this.questId = questId;
    return this;
  }

  /**
   * Get questId
   * @return questId
   */
  
  @Schema(name = "questId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("questId")
  public @Nullable String getQuestId() {
    return questId;
  }

  public void setQuestId(@Nullable String questId) {
    this.questId = questId;
  }

  public QuestSyncEvent step(@Nullable String step) {
    this.step = step;
    return this;
  }

  /**
   * Get step
   * @return step
   */
  
  @Schema(name = "step", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("step")
  public @Nullable String getStep() {
    return step;
  }

  public void setStep(@Nullable String step) {
    this.step = step;
  }

  public QuestSyncEvent progress(@Nullable Integer progress) {
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

  public QuestSyncEvent sharedObjectives(List<@Valid QuestSyncEventSharedObjectivesInner> sharedObjectives) {
    this.sharedObjectives = sharedObjectives;
    return this;
  }

  public QuestSyncEvent addSharedObjectivesItem(QuestSyncEventSharedObjectivesInner sharedObjectivesItem) {
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

  public QuestSyncEvent updatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public @Nullable OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestSyncEvent questSyncEvent = (QuestSyncEvent) o;
    return Objects.equals(this.questId, questSyncEvent.questId) &&
        Objects.equals(this.step, questSyncEvent.step) &&
        Objects.equals(this.progress, questSyncEvent.progress) &&
        Objects.equals(this.sharedObjectives, questSyncEvent.sharedObjectives) &&
        Objects.equals(this.updatedAt, questSyncEvent.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, step, progress, sharedObjectives, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestSyncEvent {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    step: ").append(toIndentedString(step)).append("\n");
    sb.append("    progress: ").append(toIndentedString(progress)).append("\n");
    sb.append("    sharedObjectives: ").append(toIndentedString(sharedObjectives)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

