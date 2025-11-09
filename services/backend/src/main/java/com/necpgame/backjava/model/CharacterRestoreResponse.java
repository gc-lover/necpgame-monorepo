package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.CharacterRestoredEvent;
import com.necpgame.backjava.model.CharacterSummary;
import com.necpgame.backjava.model.RestoreQueueEntry;
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
 * CharacterRestoreResponse
 */


public class CharacterRestoreResponse {

  private CharacterSummary character;

  private RestoreQueueEntry restoreQueueEntry;

  @Valid
  private List<@Valid CharacterRestoredEvent> events = new ArrayList<>();

  public CharacterRestoreResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterRestoreResponse(CharacterSummary character, RestoreQueueEntry restoreQueueEntry) {
    this.character = character;
    this.restoreQueueEntry = restoreQueueEntry;
  }

  public CharacterRestoreResponse character(CharacterSummary character) {
    this.character = character;
    return this;
  }

  /**
   * Get character
   * @return character
   */
  @NotNull @Valid 
  @Schema(name = "character", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character")
  public CharacterSummary getCharacter() {
    return character;
  }

  public void setCharacter(CharacterSummary character) {
    this.character = character;
  }

  public CharacterRestoreResponse restoreQueueEntry(RestoreQueueEntry restoreQueueEntry) {
    this.restoreQueueEntry = restoreQueueEntry;
    return this;
  }

  /**
   * Get restoreQueueEntry
   * @return restoreQueueEntry
   */
  @NotNull @Valid 
  @Schema(name = "restoreQueueEntry", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("restoreQueueEntry")
  public RestoreQueueEntry getRestoreQueueEntry() {
    return restoreQueueEntry;
  }

  public void setRestoreQueueEntry(RestoreQueueEntry restoreQueueEntry) {
    this.restoreQueueEntry = restoreQueueEntry;
  }

  public CharacterRestoreResponse events(List<@Valid CharacterRestoredEvent> events) {
    this.events = events;
    return this;
  }

  public CharacterRestoreResponse addEventsItem(CharacterRestoredEvent eventsItem) {
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
  public List<@Valid CharacterRestoredEvent> getEvents() {
    return events;
  }

  public void setEvents(List<@Valid CharacterRestoredEvent> events) {
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
    CharacterRestoreResponse characterRestoreResponse = (CharacterRestoreResponse) o;
    return Objects.equals(this.character, characterRestoreResponse.character) &&
        Objects.equals(this.restoreQueueEntry, characterRestoreResponse.restoreQueueEntry) &&
        Objects.equals(this.events, characterRestoreResponse.events);
  }

  @Override
  public int hashCode() {
    return Objects.hash(character, restoreQueueEntry, events);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterRestoreResponse {\n");
    sb.append("    character: ").append(toIndentedString(character)).append("\n");
    sb.append("    restoreQueueEntry: ").append(toIndentedString(restoreQueueEntry)).append("\n");
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

