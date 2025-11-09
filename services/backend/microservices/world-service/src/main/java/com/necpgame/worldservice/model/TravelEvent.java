package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * TravelEvent
 */


public class TravelEvent {

  private @Nullable String eventId;

  private @Nullable String name;

  private @Nullable String period;

  @Valid
  private List<String> locationTypes = new ArrayList<>();

  private @Nullable Float triggerChance;

  private @Nullable String description;

  @Valid
  private List<Object> choices = new ArrayList<>();

  @Valid
  private List<Object> outcomes = new ArrayList<>();

  public TravelEvent eventId(@Nullable String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  
  @Schema(name = "event_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_id")
  public @Nullable String getEventId() {
    return eventId;
  }

  public void setEventId(@Nullable String eventId) {
    this.eventId = eventId;
  }

  public TravelEvent name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public TravelEvent period(@Nullable String period) {
    this.period = period;
    return this;
  }

  /**
   * Get period
   * @return period
   */
  
  @Schema(name = "period", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("period")
  public @Nullable String getPeriod() {
    return period;
  }

  public void setPeriod(@Nullable String period) {
    this.period = period;
  }

  public TravelEvent locationTypes(List<String> locationTypes) {
    this.locationTypes = locationTypes;
    return this;
  }

  public TravelEvent addLocationTypesItem(String locationTypesItem) {
    if (this.locationTypes == null) {
      this.locationTypes = new ArrayList<>();
    }
    this.locationTypes.add(locationTypesItem);
    return this;
  }

  /**
   * Get locationTypes
   * @return locationTypes
   */
  
  @Schema(name = "location_types", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location_types")
  public List<String> getLocationTypes() {
    return locationTypes;
  }

  public void setLocationTypes(List<String> locationTypes) {
    this.locationTypes = locationTypes;
  }

  public TravelEvent triggerChance(@Nullable Float triggerChance) {
    this.triggerChance = triggerChance;
    return this;
  }

  /**
   * Get triggerChance
   * @return triggerChance
   */
  
  @Schema(name = "trigger_chance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trigger_chance")
  public @Nullable Float getTriggerChance() {
    return triggerChance;
  }

  public void setTriggerChance(@Nullable Float triggerChance) {
    this.triggerChance = triggerChance;
  }

  public TravelEvent description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public TravelEvent choices(List<Object> choices) {
    this.choices = choices;
    return this;
  }

  public TravelEvent addChoicesItem(Object choicesItem) {
    if (this.choices == null) {
      this.choices = new ArrayList<>();
    }
    this.choices.add(choicesItem);
    return this;
  }

  /**
   * Get choices
   * @return choices
   */
  
  @Schema(name = "choices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("choices")
  public List<Object> getChoices() {
    return choices;
  }

  public void setChoices(List<Object> choices) {
    this.choices = choices;
  }

  public TravelEvent outcomes(List<Object> outcomes) {
    this.outcomes = outcomes;
    return this;
  }

  public TravelEvent addOutcomesItem(Object outcomesItem) {
    if (this.outcomes == null) {
      this.outcomes = new ArrayList<>();
    }
    this.outcomes.add(outcomesItem);
    return this;
  }

  /**
   * Get outcomes
   * @return outcomes
   */
  
  @Schema(name = "outcomes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outcomes")
  public List<Object> getOutcomes() {
    return outcomes;
  }

  public void setOutcomes(List<Object> outcomes) {
    this.outcomes = outcomes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TravelEvent travelEvent = (TravelEvent) o;
    return Objects.equals(this.eventId, travelEvent.eventId) &&
        Objects.equals(this.name, travelEvent.name) &&
        Objects.equals(this.period, travelEvent.period) &&
        Objects.equals(this.locationTypes, travelEvent.locationTypes) &&
        Objects.equals(this.triggerChance, travelEvent.triggerChance) &&
        Objects.equals(this.description, travelEvent.description) &&
        Objects.equals(this.choices, travelEvent.choices) &&
        Objects.equals(this.outcomes, travelEvent.outcomes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, name, period, locationTypes, triggerChance, description, choices, outcomes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TravelEvent {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
    sb.append("    locationTypes: ").append(toIndentedString(locationTypes)).append("\n");
    sb.append("    triggerChance: ").append(toIndentedString(triggerChance)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    choices: ").append(toIndentedString(choices)).append("\n");
    sb.append("    outcomes: ").append(toIndentedString(outcomes)).append("\n");
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

