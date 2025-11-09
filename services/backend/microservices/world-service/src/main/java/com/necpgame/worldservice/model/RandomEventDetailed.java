package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.EventChoice;
import com.necpgame.worldservice.model.EventOutcome;
import com.necpgame.worldservice.model.RandomEventDetailedAllOfNpcsInvolved;
import com.necpgame.worldservice.model.RandomEventDetailedAllOfTimeRestrictions;
import com.necpgame.worldservice.model.TriggerConditions;
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
 * RandomEventDetailed
 */


public class RandomEventDetailed {

  private @Nullable String eventId;

  private @Nullable String name;

  private @Nullable String description;

  /**
   * Gets or Sets category
   */
  public enum CategoryEnum {
    COMBAT("COMBAT"),
    
    SOCIAL("SOCIAL"),
    
    ECONOMY("ECONOMY"),
    
    EXPLORATION("EXPLORATION"),
    
    FACTION("FACTION"),
    
    STORY("STORY");

    private final String value;

    CategoryEnum(String value) {
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
    public static CategoryEnum fromValue(String value) {
      for (CategoryEnum b : CategoryEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable CategoryEnum category;

  private @Nullable String period;

  private @Nullable Float baseTriggerChance;

  private @Nullable TriggerConditions triggerConditions;

  private @Nullable Integer possibleOutcomesCount;

  private @Nullable String fullDescription;

  @Valid
  private List<String> triggerLocations = new ArrayList<>();

  private @Nullable RandomEventDetailedAllOfTimeRestrictions timeRestrictions;

  @Valid
  private List<@Valid RandomEventDetailedAllOfNpcsInvolved> npcsInvolved = new ArrayList<>();

  @Valid
  private List<@Valid EventChoice> choices = new ArrayList<>();

  @Valid
  private List<@Valid EventOutcome> outcomes = new ArrayList<>();

  public RandomEventDetailed eventId(@Nullable String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  
  @Schema(name = "event_id", example = "event_badlands_ambush", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_id")
  public @Nullable String getEventId() {
    return eventId;
  }

  public void setEventId(@Nullable String eventId) {
    this.eventId = eventId;
  }

  public RandomEventDetailed name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", example = "Badlands Ambush", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public RandomEventDetailed description(@Nullable String description) {
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

  public RandomEventDetailed category(@Nullable CategoryEnum category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable CategoryEnum getCategory() {
    return category;
  }

  public void setCategory(@Nullable CategoryEnum category) {
    this.category = category;
  }

  public RandomEventDetailed period(@Nullable String period) {
    this.period = period;
    return this;
  }

  /**
   * Get period
   * @return period
   */
  
  @Schema(name = "period", example = "2060-2077", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("period")
  public @Nullable String getPeriod() {
    return period;
  }

  public void setPeriod(@Nullable String period) {
    this.period = period;
  }

  public RandomEventDetailed baseTriggerChance(@Nullable Float baseTriggerChance) {
    this.baseTriggerChance = baseTriggerChance;
    return this;
  }

  /**
   * Базовый шанс появления (0-1)
   * @return baseTriggerChance
   */
  
  @Schema(name = "base_trigger_chance", example = "0.15", description = "Базовый шанс появления (0-1)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base_trigger_chance")
  public @Nullable Float getBaseTriggerChance() {
    return baseTriggerChance;
  }

  public void setBaseTriggerChance(@Nullable Float baseTriggerChance) {
    this.baseTriggerChance = baseTriggerChance;
  }

  public RandomEventDetailed triggerConditions(@Nullable TriggerConditions triggerConditions) {
    this.triggerConditions = triggerConditions;
    return this;
  }

  /**
   * Get triggerConditions
   * @return triggerConditions
   */
  @Valid 
  @Schema(name = "trigger_conditions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trigger_conditions")
  public @Nullable TriggerConditions getTriggerConditions() {
    return triggerConditions;
  }

  public void setTriggerConditions(@Nullable TriggerConditions triggerConditions) {
    this.triggerConditions = triggerConditions;
  }

  public RandomEventDetailed possibleOutcomesCount(@Nullable Integer possibleOutcomesCount) {
    this.possibleOutcomesCount = possibleOutcomesCount;
    return this;
  }

  /**
   * Get possibleOutcomesCount
   * @return possibleOutcomesCount
   */
  
  @Schema(name = "possible_outcomes_count", example = "3", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("possible_outcomes_count")
  public @Nullable Integer getPossibleOutcomesCount() {
    return possibleOutcomesCount;
  }

  public void setPossibleOutcomesCount(@Nullable Integer possibleOutcomesCount) {
    this.possibleOutcomesCount = possibleOutcomesCount;
  }

  public RandomEventDetailed fullDescription(@Nullable String fullDescription) {
    this.fullDescription = fullDescription;
    return this;
  }

  /**
   * Get fullDescription
   * @return fullDescription
   */
  
  @Schema(name = "full_description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("full_description")
  public @Nullable String getFullDescription() {
    return fullDescription;
  }

  public void setFullDescription(@Nullable String fullDescription) {
    this.fullDescription = fullDescription;
  }

  public RandomEventDetailed triggerLocations(List<String> triggerLocations) {
    this.triggerLocations = triggerLocations;
    return this;
  }

  public RandomEventDetailed addTriggerLocationsItem(String triggerLocationsItem) {
    if (this.triggerLocations == null) {
      this.triggerLocations = new ArrayList<>();
    }
    this.triggerLocations.add(triggerLocationsItem);
    return this;
  }

  /**
   * Get triggerLocations
   * @return triggerLocations
   */
  
  @Schema(name = "trigger_locations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trigger_locations")
  public List<String> getTriggerLocations() {
    return triggerLocations;
  }

  public void setTriggerLocations(List<String> triggerLocations) {
    this.triggerLocations = triggerLocations;
  }

  public RandomEventDetailed timeRestrictions(@Nullable RandomEventDetailedAllOfTimeRestrictions timeRestrictions) {
    this.timeRestrictions = timeRestrictions;
    return this;
  }

  /**
   * Get timeRestrictions
   * @return timeRestrictions
   */
  @Valid 
  @Schema(name = "time_restrictions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_restrictions")
  public @Nullable RandomEventDetailedAllOfTimeRestrictions getTimeRestrictions() {
    return timeRestrictions;
  }

  public void setTimeRestrictions(@Nullable RandomEventDetailedAllOfTimeRestrictions timeRestrictions) {
    this.timeRestrictions = timeRestrictions;
  }

  public RandomEventDetailed npcsInvolved(List<@Valid RandomEventDetailedAllOfNpcsInvolved> npcsInvolved) {
    this.npcsInvolved = npcsInvolved;
    return this;
  }

  public RandomEventDetailed addNpcsInvolvedItem(RandomEventDetailedAllOfNpcsInvolved npcsInvolvedItem) {
    if (this.npcsInvolved == null) {
      this.npcsInvolved = new ArrayList<>();
    }
    this.npcsInvolved.add(npcsInvolvedItem);
    return this;
  }

  /**
   * Get npcsInvolved
   * @return npcsInvolved
   */
  @Valid 
  @Schema(name = "npcs_involved", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npcs_involved")
  public List<@Valid RandomEventDetailedAllOfNpcsInvolved> getNpcsInvolved() {
    return npcsInvolved;
  }

  public void setNpcsInvolved(List<@Valid RandomEventDetailedAllOfNpcsInvolved> npcsInvolved) {
    this.npcsInvolved = npcsInvolved;
  }

  public RandomEventDetailed choices(List<@Valid EventChoice> choices) {
    this.choices = choices;
    return this;
  }

  public RandomEventDetailed addChoicesItem(EventChoice choicesItem) {
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
  @Valid 
  @Schema(name = "choices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("choices")
  public List<@Valid EventChoice> getChoices() {
    return choices;
  }

  public void setChoices(List<@Valid EventChoice> choices) {
    this.choices = choices;
  }

  public RandomEventDetailed outcomes(List<@Valid EventOutcome> outcomes) {
    this.outcomes = outcomes;
    return this;
  }

  public RandomEventDetailed addOutcomesItem(EventOutcome outcomesItem) {
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
  @Valid 
  @Schema(name = "outcomes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outcomes")
  public List<@Valid EventOutcome> getOutcomes() {
    return outcomes;
  }

  public void setOutcomes(List<@Valid EventOutcome> outcomes) {
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
    RandomEventDetailed randomEventDetailed = (RandomEventDetailed) o;
    return Objects.equals(this.eventId, randomEventDetailed.eventId) &&
        Objects.equals(this.name, randomEventDetailed.name) &&
        Objects.equals(this.description, randomEventDetailed.description) &&
        Objects.equals(this.category, randomEventDetailed.category) &&
        Objects.equals(this.period, randomEventDetailed.period) &&
        Objects.equals(this.baseTriggerChance, randomEventDetailed.baseTriggerChance) &&
        Objects.equals(this.triggerConditions, randomEventDetailed.triggerConditions) &&
        Objects.equals(this.possibleOutcomesCount, randomEventDetailed.possibleOutcomesCount) &&
        Objects.equals(this.fullDescription, randomEventDetailed.fullDescription) &&
        Objects.equals(this.triggerLocations, randomEventDetailed.triggerLocations) &&
        Objects.equals(this.timeRestrictions, randomEventDetailed.timeRestrictions) &&
        Objects.equals(this.npcsInvolved, randomEventDetailed.npcsInvolved) &&
        Objects.equals(this.choices, randomEventDetailed.choices) &&
        Objects.equals(this.outcomes, randomEventDetailed.outcomes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, name, description, category, period, baseTriggerChance, triggerConditions, possibleOutcomesCount, fullDescription, triggerLocations, timeRestrictions, npcsInvolved, choices, outcomes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RandomEventDetailed {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
    sb.append("    baseTriggerChance: ").append(toIndentedString(baseTriggerChance)).append("\n");
    sb.append("    triggerConditions: ").append(toIndentedString(triggerConditions)).append("\n");
    sb.append("    possibleOutcomesCount: ").append(toIndentedString(possibleOutcomesCount)).append("\n");
    sb.append("    fullDescription: ").append(toIndentedString(fullDescription)).append("\n");
    sb.append("    triggerLocations: ").append(toIndentedString(triggerLocations)).append("\n");
    sb.append("    timeRestrictions: ").append(toIndentedString(timeRestrictions)).append("\n");
    sb.append("    npcsInvolved: ").append(toIndentedString(npcsInvolved)).append("\n");
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

