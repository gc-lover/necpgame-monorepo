package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.RomanceEventAffectionImpact;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RomanceEvent
 */


public class RomanceEvent {

  private @Nullable String eventId;

  private @Nullable String name;

  private @Nullable String stage;

  private @Nullable String description;

  private @Nullable String location;

  private @Nullable Integer durationMinutes;

  private @Nullable RomanceEventAffectionImpact affectionImpact;

  private @Nullable Integer requiredAffection;

  public RomanceEvent eventId(@Nullable String eventId) {
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

  public RomanceEvent name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", example = "Dinner Date at Tom's Diner", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public RomanceEvent stage(@Nullable String stage) {
    this.stage = stage;
    return this;
  }

  /**
   * На какой стадии доступно
   * @return stage
   */
  
  @Schema(name = "stage", description = "На какой стадии доступно", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stage")
  public @Nullable String getStage() {
    return stage;
  }

  public void setStage(@Nullable String stage) {
    this.stage = stage;
  }

  public RomanceEvent description(@Nullable String description) {
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

  public RomanceEvent location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public RomanceEvent durationMinutes(@Nullable Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
    return this;
  }

  /**
   * Get durationMinutes
   * @return durationMinutes
   */
  
  @Schema(name = "duration_minutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration_minutes")
  public @Nullable Integer getDurationMinutes() {
    return durationMinutes;
  }

  public void setDurationMinutes(@Nullable Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  public RomanceEvent affectionImpact(@Nullable RomanceEventAffectionImpact affectionImpact) {
    this.affectionImpact = affectionImpact;
    return this;
  }

  /**
   * Get affectionImpact
   * @return affectionImpact
   */
  @Valid 
  @Schema(name = "affection_impact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affection_impact")
  public @Nullable RomanceEventAffectionImpact getAffectionImpact() {
    return affectionImpact;
  }

  public void setAffectionImpact(@Nullable RomanceEventAffectionImpact affectionImpact) {
    this.affectionImpact = affectionImpact;
  }

  public RomanceEvent requiredAffection(@Nullable Integer requiredAffection) {
    this.requiredAffection = requiredAffection;
    return this;
  }

  /**
   * Минимальный уровень для доступа
   * @return requiredAffection
   */
  
  @Schema(name = "required_affection", description = "Минимальный уровень для доступа", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_affection")
  public @Nullable Integer getRequiredAffection() {
    return requiredAffection;
  }

  public void setRequiredAffection(@Nullable Integer requiredAffection) {
    this.requiredAffection = requiredAffection;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RomanceEvent romanceEvent = (RomanceEvent) o;
    return Objects.equals(this.eventId, romanceEvent.eventId) &&
        Objects.equals(this.name, romanceEvent.name) &&
        Objects.equals(this.stage, romanceEvent.stage) &&
        Objects.equals(this.description, romanceEvent.description) &&
        Objects.equals(this.location, romanceEvent.location) &&
        Objects.equals(this.durationMinutes, romanceEvent.durationMinutes) &&
        Objects.equals(this.affectionImpact, romanceEvent.affectionImpact) &&
        Objects.equals(this.requiredAffection, romanceEvent.requiredAffection);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, name, stage, description, location, durationMinutes, affectionImpact, requiredAffection);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RomanceEvent {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    stage: ").append(toIndentedString(stage)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    durationMinutes: ").append(toIndentedString(durationMinutes)).append("\n");
    sb.append("    affectionImpact: ").append(toIndentedString(affectionImpact)).append("\n");
    sb.append("    requiredAffection: ").append(toIndentedString(requiredAffection)).append("\n");
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

