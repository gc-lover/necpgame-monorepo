package com.necpgame.socialservice.model;

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
 * RomanceEventInfo
 */


public class RomanceEventInfo {

  private @Nullable String eventId;

  private @Nullable String eventName;

  /**
   * Gets or Sets eventType
   */
  public enum EventTypeEnum {
    CONVERSATION("conversation"),
    
    ACTIVITY("activity"),
    
    GIFT("gift"),
    
    QUEST("quest"),
    
    INTIMATE("intimate"),
    
    CONFLICT("conflict"),
    
    MILESTONE("milestone");

    private final String value;

    EventTypeEnum(String value) {
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
    public static EventTypeEnum fromValue(String value) {
      for (EventTypeEnum b : EventTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable EventTypeEnum eventType;

  private @Nullable String description;

  private @Nullable Integer requiredStage;

  private @Nullable Integer durationMinutes;

  @Valid
  private List<String> locationRequirements = new ArrayList<>();

  @Valid
  private List<String> timeRequirements = new ArrayList<>();

  @Valid
  private List<String> prerequisites = new ArrayList<>();

  private @Nullable Integer affectionImpact;

  private @Nullable Integer trustImpact;

  @Valid
  private List<String> tags = new ArrayList<>();

  public RomanceEventInfo eventId(@Nullable String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  
  @Schema(name = "event_id", example = "romantic_dinner", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_id")
  public @Nullable String getEventId() {
    return eventId;
  }

  public void setEventId(@Nullable String eventId) {
    this.eventId = eventId;
  }

  public RomanceEventInfo eventName(@Nullable String eventName) {
    this.eventName = eventName;
    return this;
  }

  /**
   * Get eventName
   * @return eventName
   */
  
  @Schema(name = "event_name", example = "Romantic Dinner Under Stars", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_name")
  public @Nullable String getEventName() {
    return eventName;
  }

  public void setEventName(@Nullable String eventName) {
    this.eventName = eventName;
  }

  public RomanceEventInfo eventType(@Nullable EventTypeEnum eventType) {
    this.eventType = eventType;
    return this;
  }

  /**
   * Get eventType
   * @return eventType
   */
  
  @Schema(name = "event_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_type")
  public @Nullable EventTypeEnum getEventType() {
    return eventType;
  }

  public void setEventType(@Nullable EventTypeEnum eventType) {
    this.eventType = eventType;
  }

  public RomanceEventInfo description(@Nullable String description) {
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

  public RomanceEventInfo requiredStage(@Nullable Integer requiredStage) {
    this.requiredStage = requiredStage;
    return this;
  }

  /**
   * Минимальная стадия отношений
   * minimum: 1
   * maximum: 9
   * @return requiredStage
   */
  @Min(value = 1) @Max(value = 9) 
  @Schema(name = "required_stage", description = "Минимальная стадия отношений", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_stage")
  public @Nullable Integer getRequiredStage() {
    return requiredStage;
  }

  public void setRequiredStage(@Nullable Integer requiredStage) {
    this.requiredStage = requiredStage;
  }

  public RomanceEventInfo durationMinutes(@Nullable Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
    return this;
  }

  /**
   * Длительность события
   * @return durationMinutes
   */
  
  @Schema(name = "duration_minutes", description = "Длительность события", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration_minutes")
  public @Nullable Integer getDurationMinutes() {
    return durationMinutes;
  }

  public void setDurationMinutes(@Nullable Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  public RomanceEventInfo locationRequirements(List<String> locationRequirements) {
    this.locationRequirements = locationRequirements;
    return this;
  }

  public RomanceEventInfo addLocationRequirementsItem(String locationRequirementsItem) {
    if (this.locationRequirements == null) {
      this.locationRequirements = new ArrayList<>();
    }
    this.locationRequirements.add(locationRequirementsItem);
    return this;
  }

  /**
   * Get locationRequirements
   * @return locationRequirements
   */
  
  @Schema(name = "location_requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location_requirements")
  public List<String> getLocationRequirements() {
    return locationRequirements;
  }

  public void setLocationRequirements(List<String> locationRequirements) {
    this.locationRequirements = locationRequirements;
  }

  public RomanceEventInfo timeRequirements(List<String> timeRequirements) {
    this.timeRequirements = timeRequirements;
    return this;
  }

  public RomanceEventInfo addTimeRequirementsItem(String timeRequirementsItem) {
    if (this.timeRequirements == null) {
      this.timeRequirements = new ArrayList<>();
    }
    this.timeRequirements.add(timeRequirementsItem);
    return this;
  }

  /**
   * Get timeRequirements
   * @return timeRequirements
   */
  
  @Schema(name = "time_requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_requirements")
  public List<String> getTimeRequirements() {
    return timeRequirements;
  }

  public void setTimeRequirements(List<String> timeRequirements) {
    this.timeRequirements = timeRequirements;
  }

  public RomanceEventInfo prerequisites(List<String> prerequisites) {
    this.prerequisites = prerequisites;
    return this;
  }

  public RomanceEventInfo addPrerequisitesItem(String prerequisitesItem) {
    if (this.prerequisites == null) {
      this.prerequisites = new ArrayList<>();
    }
    this.prerequisites.add(prerequisitesItem);
    return this;
  }

  /**
   * Необходимые предыдущие события
   * @return prerequisites
   */
  
  @Schema(name = "prerequisites", description = "Необходимые предыдущие события", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("prerequisites")
  public List<String> getPrerequisites() {
    return prerequisites;
  }

  public void setPrerequisites(List<String> prerequisites) {
    this.prerequisites = prerequisites;
  }

  public RomanceEventInfo affectionImpact(@Nullable Integer affectionImpact) {
    this.affectionImpact = affectionImpact;
    return this;
  }

  /**
   * Влияние на affection (-100 to +100)
   * @return affectionImpact
   */
  
  @Schema(name = "affection_impact", description = "Влияние на affection (-100 to +100)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affection_impact")
  public @Nullable Integer getAffectionImpact() {
    return affectionImpact;
  }

  public void setAffectionImpact(@Nullable Integer affectionImpact) {
    this.affectionImpact = affectionImpact;
  }

  public RomanceEventInfo trustImpact(@Nullable Integer trustImpact) {
    this.trustImpact = trustImpact;
    return this;
  }

  /**
   * Влияние на trust (-100 to +100)
   * @return trustImpact
   */
  
  @Schema(name = "trust_impact", description = "Влияние на trust (-100 to +100)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trust_impact")
  public @Nullable Integer getTrustImpact() {
    return trustImpact;
  }

  public void setTrustImpact(@Nullable Integer trustImpact) {
    this.trustImpact = trustImpact;
  }

  public RomanceEventInfo tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public RomanceEventInfo addTagsItem(String tagsItem) {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tagsItem);
    return this;
  }

  /**
   * Get tags
   * @return tags
   */
  
  @Schema(name = "tags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tags")
  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RomanceEventInfo romanceEventInfo = (RomanceEventInfo) o;
    return Objects.equals(this.eventId, romanceEventInfo.eventId) &&
        Objects.equals(this.eventName, romanceEventInfo.eventName) &&
        Objects.equals(this.eventType, romanceEventInfo.eventType) &&
        Objects.equals(this.description, romanceEventInfo.description) &&
        Objects.equals(this.requiredStage, romanceEventInfo.requiredStage) &&
        Objects.equals(this.durationMinutes, romanceEventInfo.durationMinutes) &&
        Objects.equals(this.locationRequirements, romanceEventInfo.locationRequirements) &&
        Objects.equals(this.timeRequirements, romanceEventInfo.timeRequirements) &&
        Objects.equals(this.prerequisites, romanceEventInfo.prerequisites) &&
        Objects.equals(this.affectionImpact, romanceEventInfo.affectionImpact) &&
        Objects.equals(this.trustImpact, romanceEventInfo.trustImpact) &&
        Objects.equals(this.tags, romanceEventInfo.tags);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, eventName, eventType, description, requiredStage, durationMinutes, locationRequirements, timeRequirements, prerequisites, affectionImpact, trustImpact, tags);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RomanceEventInfo {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    eventName: ").append(toIndentedString(eventName)).append("\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    requiredStage: ").append(toIndentedString(requiredStage)).append("\n");
    sb.append("    durationMinutes: ").append(toIndentedString(durationMinutes)).append("\n");
    sb.append("    locationRequirements: ").append(toIndentedString(locationRequirements)).append("\n");
    sb.append("    timeRequirements: ").append(toIndentedString(timeRequirements)).append("\n");
    sb.append("    prerequisites: ").append(toIndentedString(prerequisites)).append("\n");
    sb.append("    affectionImpact: ").append(toIndentedString(affectionImpact)).append("\n");
    sb.append("    trustImpact: ").append(toIndentedString(trustImpact)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
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

