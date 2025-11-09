package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * Текущий контекст
 */

@Schema(name = "RomanceEventGenerationRequest_context", description = "Текущий контекст")
@JsonTypeName("RomanceEventGenerationRequest_context")

public class RomanceEventGenerationRequestContext {

  private @Nullable String location;

  /**
   * Gets or Sets timeOfDay
   */
  public enum TimeOfDayEnum {
    MORNING("morning"),
    
    DAY("day"),
    
    EVENING("evening"),
    
    NIGHT("night");

    private final String value;

    TimeOfDayEnum(String value) {
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
    public static TimeOfDayEnum fromValue(String value) {
      for (TimeOfDayEnum b : TimeOfDayEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TimeOfDayEnum timeOfDay;

  private @Nullable Integer relationshipStage;

  @Valid
  private List<String> recentEvents = new ArrayList<>();

  /**
   * Gets or Sets mood
   */
  public enum MoodEnum {
    HAPPY("happy"),
    
    NEUTRAL("neutral"),
    
    SAD("sad"),
    
    ANGRY("angry");

    private final String value;

    MoodEnum(String value) {
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
    public static MoodEnum fromValue(String value) {
      for (MoodEnum b : MoodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable MoodEnum mood;

  public RomanceEventGenerationRequestContext location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  
  @Schema(name = "location", example = "nomad_camp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public RomanceEventGenerationRequestContext timeOfDay(@Nullable TimeOfDayEnum timeOfDay) {
    this.timeOfDay = timeOfDay;
    return this;
  }

  /**
   * Get timeOfDay
   * @return timeOfDay
   */
  
  @Schema(name = "time_of_day", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_of_day")
  public @Nullable TimeOfDayEnum getTimeOfDay() {
    return timeOfDay;
  }

  public void setTimeOfDay(@Nullable TimeOfDayEnum timeOfDay) {
    this.timeOfDay = timeOfDay;
  }

  public RomanceEventGenerationRequestContext relationshipStage(@Nullable Integer relationshipStage) {
    this.relationshipStage = relationshipStage;
    return this;
  }

  /**
   * Get relationshipStage
   * minimum: 1
   * maximum: 9
   * @return relationshipStage
   */
  @Min(value = 1) @Max(value = 9) 
  @Schema(name = "relationship_stage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationship_stage")
  public @Nullable Integer getRelationshipStage() {
    return relationshipStage;
  }

  public void setRelationshipStage(@Nullable Integer relationshipStage) {
    this.relationshipStage = relationshipStage;
  }

  public RomanceEventGenerationRequestContext recentEvents(List<String> recentEvents) {
    this.recentEvents = recentEvents;
    return this;
  }

  public RomanceEventGenerationRequestContext addRecentEventsItem(String recentEventsItem) {
    if (this.recentEvents == null) {
      this.recentEvents = new ArrayList<>();
    }
    this.recentEvents.add(recentEventsItem);
    return this;
  }

  /**
   * Get recentEvents
   * @return recentEvents
   */
  
  @Schema(name = "recent_events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recent_events")
  public List<String> getRecentEvents() {
    return recentEvents;
  }

  public void setRecentEvents(List<String> recentEvents) {
    this.recentEvents = recentEvents;
  }

  public RomanceEventGenerationRequestContext mood(@Nullable MoodEnum mood) {
    this.mood = mood;
    return this;
  }

  /**
   * Get mood
   * @return mood
   */
  
  @Schema(name = "mood", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mood")
  public @Nullable MoodEnum getMood() {
    return mood;
  }

  public void setMood(@Nullable MoodEnum mood) {
    this.mood = mood;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RomanceEventGenerationRequestContext romanceEventGenerationRequestContext = (RomanceEventGenerationRequestContext) o;
    return Objects.equals(this.location, romanceEventGenerationRequestContext.location) &&
        Objects.equals(this.timeOfDay, romanceEventGenerationRequestContext.timeOfDay) &&
        Objects.equals(this.relationshipStage, romanceEventGenerationRequestContext.relationshipStage) &&
        Objects.equals(this.recentEvents, romanceEventGenerationRequestContext.recentEvents) &&
        Objects.equals(this.mood, romanceEventGenerationRequestContext.mood);
  }

  @Override
  public int hashCode() {
    return Objects.hash(location, timeOfDay, relationshipStage, recentEvents, mood);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RomanceEventGenerationRequestContext {\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    timeOfDay: ").append(toIndentedString(timeOfDay)).append("\n");
    sb.append("    relationshipStage: ").append(toIndentedString(relationshipStage)).append("\n");
    sb.append("    recentEvents: ").append(toIndentedString(recentEvents)).append("\n");
    sb.append("    mood: ").append(toIndentedString(mood)).append("\n");
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

