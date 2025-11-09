package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TriggerEventRequest
 */


public class TriggerEventRequest {

  private String eventId;

  private UUID characterId;

  private String locationId;

  private Boolean overrideChance = false;

  public TriggerEventRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TriggerEventRequest(String eventId, UUID characterId, String locationId) {
    this.eventId = eventId;
    this.characterId = characterId;
    this.locationId = locationId;
  }

  public TriggerEventRequest eventId(String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  @NotNull 
  @Schema(name = "event_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("event_id")
  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public TriggerEventRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public TriggerEventRequest locationId(String locationId) {
    this.locationId = locationId;
    return this;
  }

  /**
   * Get locationId
   * @return locationId
   */
  @NotNull 
  @Schema(name = "location_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("location_id")
  public String getLocationId() {
    return locationId;
  }

  public void setLocationId(String locationId) {
    this.locationId = locationId;
  }

  public TriggerEventRequest overrideChance(Boolean overrideChance) {
    this.overrideChance = overrideChance;
    return this;
  }

  /**
   * Для тестирования - заставить событие произойти
   * @return overrideChance
   */
  
  @Schema(name = "override_chance", description = "Для тестирования - заставить событие произойти", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("override_chance")
  public Boolean getOverrideChance() {
    return overrideChance;
  }

  public void setOverrideChance(Boolean overrideChance) {
    this.overrideChance = overrideChance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TriggerEventRequest triggerEventRequest = (TriggerEventRequest) o;
    return Objects.equals(this.eventId, triggerEventRequest.eventId) &&
        Objects.equals(this.characterId, triggerEventRequest.characterId) &&
        Objects.equals(this.locationId, triggerEventRequest.locationId) &&
        Objects.equals(this.overrideChance, triggerEventRequest.overrideChance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, characterId, locationId, overrideChance);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TriggerEventRequest {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
    sb.append("    overrideChance: ").append(toIndentedString(overrideChance)).append("\n");
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

