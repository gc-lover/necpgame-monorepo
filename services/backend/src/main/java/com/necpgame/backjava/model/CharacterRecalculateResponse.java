package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.CharacterStatsUpdatedEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * CharacterRecalculateResponse
 */


public class CharacterRecalculateResponse {

  private UUID characterId;

  private UUID jobId;

  @Valid
  private List<@Valid CharacterStatsUpdatedEvent> events = new ArrayList<>();

  public CharacterRecalculateResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterRecalculateResponse(UUID characterId, UUID jobId) {
    this.characterId = characterId;
    this.jobId = jobId;
  }

  public CharacterRecalculateResponse characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public CharacterRecalculateResponse jobId(UUID jobId) {
    this.jobId = jobId;
    return this;
  }

  /**
   * Get jobId
   * @return jobId
   */
  @NotNull @Valid 
  @Schema(name = "jobId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("jobId")
  public UUID getJobId() {
    return jobId;
  }

  public void setJobId(UUID jobId) {
    this.jobId = jobId;
  }

  public CharacterRecalculateResponse events(List<@Valid CharacterStatsUpdatedEvent> events) {
    this.events = events;
    return this;
  }

  public CharacterRecalculateResponse addEventsItem(CharacterStatsUpdatedEvent eventsItem) {
    if (this.events == null) {
      this.events = new ArrayList<>();
    }
    this.events.add(eventsItem);
    return this;
  }

  /**
   * Get events
   * @return events
   */
  @Valid 
  @Schema(name = "events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("events")
  public List<@Valid CharacterStatsUpdatedEvent> getEvents() {
    return events;
  }

  public void setEvents(List<@Valid CharacterStatsUpdatedEvent> events) {
    this.events = events;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterRecalculateResponse characterRecalculateResponse = (CharacterRecalculateResponse) o;
    return Objects.equals(this.characterId, characterRecalculateResponse.characterId) &&
        Objects.equals(this.jobId, characterRecalculateResponse.jobId) &&
        Objects.equals(this.events, characterRecalculateResponse.events);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, jobId, events);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterRecalculateResponse {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    jobId: ").append(toIndentedString(jobId)).append("\n");
    sb.append("    events: ").append(toIndentedString(events)).append("\n");
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

