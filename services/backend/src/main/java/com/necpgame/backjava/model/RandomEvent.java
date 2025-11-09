package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.TriggerConditions;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RandomEvent
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class RandomEvent {

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

  public RandomEvent eventId(@Nullable String eventId) {
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

  public RandomEvent name(@Nullable String name) {
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

  public RandomEvent description(@Nullable String description) {
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

  public RandomEvent category(@Nullable CategoryEnum category) {
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

  public RandomEvent period(@Nullable String period) {
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

  public RandomEvent baseTriggerChance(@Nullable Float baseTriggerChance) {
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

  public RandomEvent triggerConditions(@Nullable TriggerConditions triggerConditions) {
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

  public RandomEvent possibleOutcomesCount(@Nullable Integer possibleOutcomesCount) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RandomEvent randomEvent = (RandomEvent) o;
    return Objects.equals(this.eventId, randomEvent.eventId) &&
        Objects.equals(this.name, randomEvent.name) &&
        Objects.equals(this.description, randomEvent.description) &&
        Objects.equals(this.category, randomEvent.category) &&
        Objects.equals(this.period, randomEvent.period) &&
        Objects.equals(this.baseTriggerChance, randomEvent.baseTriggerChance) &&
        Objects.equals(this.triggerConditions, randomEvent.triggerConditions) &&
        Objects.equals(this.possibleOutcomesCount, randomEvent.possibleOutcomesCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, name, description, category, period, baseTriggerChance, triggerConditions, possibleOutcomesCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RandomEvent {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
    sb.append("    baseTriggerChance: ").append(toIndentedString(baseTriggerChance)).append("\n");
    sb.append("    triggerConditions: ").append(toIndentedString(triggerConditions)).append("\n");
    sb.append("    possibleOutcomesCount: ").append(toIndentedString(possibleOutcomesCount)).append("\n");
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

