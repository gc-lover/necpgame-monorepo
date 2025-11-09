package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RomanceChoiceResult
 */


public class RomanceChoiceResult {

  private @Nullable UUID instanceId;

  private @Nullable String choiceMade;

  private @Nullable Integer affectionChange;

  private @Nullable Integer trustChange;

  private @Nullable String outcome;

  private @Nullable Boolean stageProgressed;

  private JsonNullable<String> newStage = JsonNullable.<String>undefined();

  @Valid
  private List<String> unlockedEvents = new ArrayList<>();

  public RomanceChoiceResult instanceId(@Nullable UUID instanceId) {
    this.instanceId = instanceId;
    return this;
  }

  /**
   * Get instanceId
   * @return instanceId
   */
  @Valid 
  @Schema(name = "instance_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("instance_id")
  public @Nullable UUID getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(@Nullable UUID instanceId) {
    this.instanceId = instanceId;
  }

  public RomanceChoiceResult choiceMade(@Nullable String choiceMade) {
    this.choiceMade = choiceMade;
    return this;
  }

  /**
   * Get choiceMade
   * @return choiceMade
   */
  
  @Schema(name = "choice_made", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("choice_made")
  public @Nullable String getChoiceMade() {
    return choiceMade;
  }

  public void setChoiceMade(@Nullable String choiceMade) {
    this.choiceMade = choiceMade;
  }

  public RomanceChoiceResult affectionChange(@Nullable Integer affectionChange) {
    this.affectionChange = affectionChange;
    return this;
  }

  /**
   * Get affectionChange
   * @return affectionChange
   */
  
  @Schema(name = "affection_change", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affection_change")
  public @Nullable Integer getAffectionChange() {
    return affectionChange;
  }

  public void setAffectionChange(@Nullable Integer affectionChange) {
    this.affectionChange = affectionChange;
  }

  public RomanceChoiceResult trustChange(@Nullable Integer trustChange) {
    this.trustChange = trustChange;
    return this;
  }

  /**
   * Get trustChange
   * @return trustChange
   */
  
  @Schema(name = "trust_change", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trust_change")
  public @Nullable Integer getTrustChange() {
    return trustChange;
  }

  public void setTrustChange(@Nullable Integer trustChange) {
    this.trustChange = trustChange;
  }

  public RomanceChoiceResult outcome(@Nullable String outcome) {
    this.outcome = outcome;
    return this;
  }

  /**
   * Get outcome
   * @return outcome
   */
  
  @Schema(name = "outcome", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outcome")
  public @Nullable String getOutcome() {
    return outcome;
  }

  public void setOutcome(@Nullable String outcome) {
    this.outcome = outcome;
  }

  public RomanceChoiceResult stageProgressed(@Nullable Boolean stageProgressed) {
    this.stageProgressed = stageProgressed;
    return this;
  }

  /**
   * Get stageProgressed
   * @return stageProgressed
   */
  
  @Schema(name = "stage_progressed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stage_progressed")
  public @Nullable Boolean getStageProgressed() {
    return stageProgressed;
  }

  public void setStageProgressed(@Nullable Boolean stageProgressed) {
    this.stageProgressed = stageProgressed;
  }

  public RomanceChoiceResult newStage(String newStage) {
    this.newStage = JsonNullable.of(newStage);
    return this;
  }

  /**
   * Get newStage
   * @return newStage
   */
  
  @Schema(name = "new_stage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("new_stage")
  public JsonNullable<String> getNewStage() {
    return newStage;
  }

  public void setNewStage(JsonNullable<String> newStage) {
    this.newStage = newStage;
  }

  public RomanceChoiceResult unlockedEvents(List<String> unlockedEvents) {
    this.unlockedEvents = unlockedEvents;
    return this;
  }

  public RomanceChoiceResult addUnlockedEventsItem(String unlockedEventsItem) {
    if (this.unlockedEvents == null) {
      this.unlockedEvents = new ArrayList<>();
    }
    this.unlockedEvents.add(unlockedEventsItem);
    return this;
  }

  /**
   * Get unlockedEvents
   * @return unlockedEvents
   */
  
  @Schema(name = "unlocked_events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocked_events")
  public List<String> getUnlockedEvents() {
    return unlockedEvents;
  }

  public void setUnlockedEvents(List<String> unlockedEvents) {
    this.unlockedEvents = unlockedEvents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RomanceChoiceResult romanceChoiceResult = (RomanceChoiceResult) o;
    return Objects.equals(this.instanceId, romanceChoiceResult.instanceId) &&
        Objects.equals(this.choiceMade, romanceChoiceResult.choiceMade) &&
        Objects.equals(this.affectionChange, romanceChoiceResult.affectionChange) &&
        Objects.equals(this.trustChange, romanceChoiceResult.trustChange) &&
        Objects.equals(this.outcome, romanceChoiceResult.outcome) &&
        Objects.equals(this.stageProgressed, romanceChoiceResult.stageProgressed) &&
        equalsNullable(this.newStage, romanceChoiceResult.newStage) &&
        Objects.equals(this.unlockedEvents, romanceChoiceResult.unlockedEvents);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(instanceId, choiceMade, affectionChange, trustChange, outcome, stageProgressed, hashCodeNullable(newStage), unlockedEvents);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RomanceChoiceResult {\n");
    sb.append("    instanceId: ").append(toIndentedString(instanceId)).append("\n");
    sb.append("    choiceMade: ").append(toIndentedString(choiceMade)).append("\n");
    sb.append("    affectionChange: ").append(toIndentedString(affectionChange)).append("\n");
    sb.append("    trustChange: ").append(toIndentedString(trustChange)).append("\n");
    sb.append("    outcome: ").append(toIndentedString(outcome)).append("\n");
    sb.append("    stageProgressed: ").append(toIndentedString(stageProgressed)).append("\n");
    sb.append("    newStage: ").append(toIndentedString(newStage)).append("\n");
    sb.append("    unlockedEvents: ").append(toIndentedString(unlockedEvents)).append("\n");
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

