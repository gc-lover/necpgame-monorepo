package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.CharacterCreatedEvent;
import com.necpgame.backjava.model.CharacterSlotState;
import com.necpgame.backjava.model.CharacterSummary;
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
 * CharacterCreateResponse
 */


public class CharacterCreateResponse {

  private CharacterSummary character;

  private CharacterSlotState slots;

  @Valid
  private List<@Valid CharacterCreatedEvent> events = new ArrayList<>();

  public CharacterCreateResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterCreateResponse(CharacterSummary character, CharacterSlotState slots, List<@Valid CharacterCreatedEvent> events) {
    this.character = character;
    this.slots = slots;
    this.events = events;
  }

  public CharacterCreateResponse character(CharacterSummary character) {
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

  public CharacterCreateResponse slots(CharacterSlotState slots) {
    this.slots = slots;
    return this;
  }

  /**
   * Get slots
   * @return slots
   */
  @NotNull @Valid 
  @Schema(name = "slots", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slots")
  public CharacterSlotState getSlots() {
    return slots;
  }

  public void setSlots(CharacterSlotState slots) {
    this.slots = slots;
  }

  public CharacterCreateResponse events(List<@Valid CharacterCreatedEvent> events) {
    this.events = events;
    return this;
  }

  public CharacterCreateResponse addEventsItem(CharacterCreatedEvent eventsItem) {
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
  @NotNull @Valid 
  @Schema(name = "events", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("events")
  public List<@Valid CharacterCreatedEvent> getEvents() {
    return events;
  }

  public void setEvents(List<@Valid CharacterCreatedEvent> events) {
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
    CharacterCreateResponse characterCreateResponse = (CharacterCreateResponse) o;
    return Objects.equals(this.character, characterCreateResponse.character) &&
        Objects.equals(this.slots, characterCreateResponse.slots) &&
        Objects.equals(this.events, characterCreateResponse.events);
  }

  @Override
  public int hashCode() {
    return Objects.hash(character, slots, events);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterCreateResponse {\n");
    sb.append("    character: ").append(toIndentedString(character)).append("\n");
    sb.append("    slots: ").append(toIndentedString(slots)).append("\n");
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

