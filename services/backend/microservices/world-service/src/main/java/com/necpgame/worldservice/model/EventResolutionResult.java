package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.EventOutcome;
import com.necpgame.worldservice.model.EventResolutionResultSkillCheckResult;
import java.util.Arrays;
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
 * EventResolutionResult
 */


public class EventResolutionResult {

  private @Nullable UUID instanceId;

  private @Nullable String choiceMade;

  private JsonNullable<EventResolutionResultSkillCheckResult> skillCheckResult = JsonNullable.<EventResolutionResultSkillCheckResult>undefined();

  private @Nullable EventOutcome outcome;

  private @Nullable Object consequencesApplied;

  public EventResolutionResult instanceId(@Nullable UUID instanceId) {
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

  public EventResolutionResult choiceMade(@Nullable String choiceMade) {
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

  public EventResolutionResult skillCheckResult(EventResolutionResultSkillCheckResult skillCheckResult) {
    this.skillCheckResult = JsonNullable.of(skillCheckResult);
    return this;
  }

  /**
   * Get skillCheckResult
   * @return skillCheckResult
   */
  @Valid 
  @Schema(name = "skill_check_result", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_check_result")
  public JsonNullable<EventResolutionResultSkillCheckResult> getSkillCheckResult() {
    return skillCheckResult;
  }

  public void setSkillCheckResult(JsonNullable<EventResolutionResultSkillCheckResult> skillCheckResult) {
    this.skillCheckResult = skillCheckResult;
  }

  public EventResolutionResult outcome(@Nullable EventOutcome outcome) {
    this.outcome = outcome;
    return this;
  }

  /**
   * Get outcome
   * @return outcome
   */
  @Valid 
  @Schema(name = "outcome", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outcome")
  public @Nullable EventOutcome getOutcome() {
    return outcome;
  }

  public void setOutcome(@Nullable EventOutcome outcome) {
    this.outcome = outcome;
  }

  public EventResolutionResult consequencesApplied(@Nullable Object consequencesApplied) {
    this.consequencesApplied = consequencesApplied;
    return this;
  }

  /**
   * Фактически примененные последствия
   * @return consequencesApplied
   */
  
  @Schema(name = "consequences_applied", description = "Фактически примененные последствия", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequences_applied")
  public @Nullable Object getConsequencesApplied() {
    return consequencesApplied;
  }

  public void setConsequencesApplied(@Nullable Object consequencesApplied) {
    this.consequencesApplied = consequencesApplied;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventResolutionResult eventResolutionResult = (EventResolutionResult) o;
    return Objects.equals(this.instanceId, eventResolutionResult.instanceId) &&
        Objects.equals(this.choiceMade, eventResolutionResult.choiceMade) &&
        equalsNullable(this.skillCheckResult, eventResolutionResult.skillCheckResult) &&
        Objects.equals(this.outcome, eventResolutionResult.outcome) &&
        Objects.equals(this.consequencesApplied, eventResolutionResult.consequencesApplied);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(instanceId, choiceMade, hashCodeNullable(skillCheckResult), outcome, consequencesApplied);
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
    sb.append("class EventResolutionResult {\n");
    sb.append("    instanceId: ").append(toIndentedString(instanceId)).append("\n");
    sb.append("    choiceMade: ").append(toIndentedString(choiceMade)).append("\n");
    sb.append("    skillCheckResult: ").append(toIndentedString(skillCheckResult)).append("\n");
    sb.append("    outcome: ").append(toIndentedString(outcome)).append("\n");
    sb.append("    consequencesApplied: ").append(toIndentedString(consequencesApplied)).append("\n");
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

