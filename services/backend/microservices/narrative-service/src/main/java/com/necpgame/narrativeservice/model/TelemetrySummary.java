package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * TelemetrySummary
 */


public class TelemetrySummary {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime recordedAt;

  private @Nullable UUID attemptId;

  @Valid
  private Map<String, Integer> outcomes = new HashMap<>();

  @Valid
  private List<String> tutorialShown = new ArrayList<>();

  public TelemetrySummary recordedAt(@Nullable OffsetDateTime recordedAt) {
    this.recordedAt = recordedAt;
    return this;
  }

  /**
   * Get recordedAt
   * @return recordedAt
   */
  @Valid 
  @Schema(name = "recordedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recordedAt")
  public @Nullable OffsetDateTime getRecordedAt() {
    return recordedAt;
  }

  public void setRecordedAt(@Nullable OffsetDateTime recordedAt) {
    this.recordedAt = recordedAt;
  }

  public TelemetrySummary attemptId(@Nullable UUID attemptId) {
    this.attemptId = attemptId;
    return this;
  }

  /**
   * Get attemptId
   * @return attemptId
   */
  @Valid 
  @Schema(name = "attemptId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attemptId")
  public @Nullable UUID getAttemptId() {
    return attemptId;
  }

  public void setAttemptId(@Nullable UUID attemptId) {
    this.attemptId = attemptId;
  }

  public TelemetrySummary outcomes(Map<String, Integer> outcomes) {
    this.outcomes = outcomes;
    return this;
  }

  public TelemetrySummary putOutcomesItem(String key, Integer outcomesItem) {
    if (this.outcomes == null) {
      this.outcomes = new HashMap<>();
    }
    this.outcomes.put(key, outcomesItem);
    return this;
  }

  /**
   * Get outcomes
   * @return outcomes
   */
  
  @Schema(name = "outcomes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outcomes")
  public Map<String, Integer> getOutcomes() {
    return outcomes;
  }

  public void setOutcomes(Map<String, Integer> outcomes) {
    this.outcomes = outcomes;
  }

  public TelemetrySummary tutorialShown(List<String> tutorialShown) {
    this.tutorialShown = tutorialShown;
    return this;
  }

  public TelemetrySummary addTutorialShownItem(String tutorialShownItem) {
    if (this.tutorialShown == null) {
      this.tutorialShown = new ArrayList<>();
    }
    this.tutorialShown.add(tutorialShownItem);
    return this;
  }

  /**
   * Get tutorialShown
   * @return tutorialShown
   */
  
  @Schema(name = "tutorialShown", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tutorialShown")
  public List<String> getTutorialShown() {
    return tutorialShown;
  }

  public void setTutorialShown(List<String> tutorialShown) {
    this.tutorialShown = tutorialShown;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TelemetrySummary telemetrySummary = (TelemetrySummary) o;
    return Objects.equals(this.recordedAt, telemetrySummary.recordedAt) &&
        Objects.equals(this.attemptId, telemetrySummary.attemptId) &&
        Objects.equals(this.outcomes, telemetrySummary.outcomes) &&
        Objects.equals(this.tutorialShown, telemetrySummary.tutorialShown);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordedAt, attemptId, outcomes, tutorialShown);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TelemetrySummary {\n");
    sb.append("    recordedAt: ").append(toIndentedString(recordedAt)).append("\n");
    sb.append("    attemptId: ").append(toIndentedString(attemptId)).append("\n");
    sb.append("    outcomes: ").append(toIndentedString(outcomes)).append("\n");
    sb.append("    tutorialShown: ").append(toIndentedString(tutorialShown)).append("\n");
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

