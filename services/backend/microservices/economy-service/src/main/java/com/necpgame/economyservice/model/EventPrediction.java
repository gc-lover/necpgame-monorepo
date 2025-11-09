package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * EventPrediction
 */


public class EventPrediction {

  private @Nullable String eventType;

  private @Nullable Float probability;

  private @Nullable String estimatedTimeframe;

  @Valid
  private List<String> triggers = new ArrayList<>();

  /**
   * Gets or Sets potentialImpact
   */
  public enum PotentialImpactEnum {
    MINOR("MINOR"),
    
    MODERATE("MODERATE"),
    
    MAJOR("MAJOR");

    private final String value;

    PotentialImpactEnum(String value) {
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
    public static PotentialImpactEnum fromValue(String value) {
      for (PotentialImpactEnum b : PotentialImpactEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PotentialImpactEnum potentialImpact;

  public EventPrediction eventType(@Nullable String eventType) {
    this.eventType = eventType;
    return this;
  }

  /**
   * Get eventType
   * @return eventType
   */
  
  @Schema(name = "event_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_type")
  public @Nullable String getEventType() {
    return eventType;
  }

  public void setEventType(@Nullable String eventType) {
    this.eventType = eventType;
  }

  public EventPrediction probability(@Nullable Float probability) {
    this.probability = probability;
    return this;
  }

  /**
   * Вероятность (0-1)
   * @return probability
   */
  
  @Schema(name = "probability", description = "Вероятность (0-1)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("probability")
  public @Nullable Float getProbability() {
    return probability;
  }

  public void setProbability(@Nullable Float probability) {
    this.probability = probability;
  }

  public EventPrediction estimatedTimeframe(@Nullable String estimatedTimeframe) {
    this.estimatedTimeframe = estimatedTimeframe;
    return this;
  }

  /**
   * Get estimatedTimeframe
   * @return estimatedTimeframe
   */
  
  @Schema(name = "estimated_timeframe", example = "7-14 days", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_timeframe")
  public @Nullable String getEstimatedTimeframe() {
    return estimatedTimeframe;
  }

  public void setEstimatedTimeframe(@Nullable String estimatedTimeframe) {
    this.estimatedTimeframe = estimatedTimeframe;
  }

  public EventPrediction triggers(List<String> triggers) {
    this.triggers = triggers;
    return this;
  }

  public EventPrediction addTriggersItem(String triggersItem) {
    if (this.triggers == null) {
      this.triggers = new ArrayList<>();
    }
    this.triggers.add(triggersItem);
    return this;
  }

  /**
   * Что может вызвать событие
   * @return triggers
   */
  
  @Schema(name = "triggers", description = "Что может вызвать событие", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("triggers")
  public List<String> getTriggers() {
    return triggers;
  }

  public void setTriggers(List<String> triggers) {
    this.triggers = triggers;
  }

  public EventPrediction potentialImpact(@Nullable PotentialImpactEnum potentialImpact) {
    this.potentialImpact = potentialImpact;
    return this;
  }

  /**
   * Get potentialImpact
   * @return potentialImpact
   */
  
  @Schema(name = "potential_impact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("potential_impact")
  public @Nullable PotentialImpactEnum getPotentialImpact() {
    return potentialImpact;
  }

  public void setPotentialImpact(@Nullable PotentialImpactEnum potentialImpact) {
    this.potentialImpact = potentialImpact;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventPrediction eventPrediction = (EventPrediction) o;
    return Objects.equals(this.eventType, eventPrediction.eventType) &&
        Objects.equals(this.probability, eventPrediction.probability) &&
        Objects.equals(this.estimatedTimeframe, eventPrediction.estimatedTimeframe) &&
        Objects.equals(this.triggers, eventPrediction.triggers) &&
        Objects.equals(this.potentialImpact, eventPrediction.potentialImpact);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventType, probability, estimatedTimeframe, triggers, potentialImpact);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventPrediction {\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    probability: ").append(toIndentedString(probability)).append("\n");
    sb.append("    estimatedTimeframe: ").append(toIndentedString(estimatedTimeframe)).append("\n");
    sb.append("    triggers: ").append(toIndentedString(triggers)).append("\n");
    sb.append("    potentialImpact: ").append(toIndentedString(potentialImpact)).append("\n");
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

