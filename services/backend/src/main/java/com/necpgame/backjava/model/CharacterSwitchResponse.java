package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.CharacterSwitchedEvent;
import com.necpgame.backjava.model.StateSnapshotRef;
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
 * CharacterSwitchResponse
 */


public class CharacterSwitchResponse {

  private UUID activeCharacterId;

  private StateSnapshotRef snapshot;

  @Valid
  private List<@Valid CharacterSwitchedEvent> events = new ArrayList<>();

  public CharacterSwitchResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterSwitchResponse(UUID activeCharacterId, StateSnapshotRef snapshot, List<@Valid CharacterSwitchedEvent> events) {
    this.activeCharacterId = activeCharacterId;
    this.snapshot = snapshot;
    this.events = events;
  }

  public CharacterSwitchResponse activeCharacterId(UUID activeCharacterId) {
    this.activeCharacterId = activeCharacterId;
    return this;
  }

  /**
   * Get activeCharacterId
   * @return activeCharacterId
   */
  @NotNull @Valid 
  @Schema(name = "activeCharacterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("activeCharacterId")
  public UUID getActiveCharacterId() {
    return activeCharacterId;
  }

  public void setActiveCharacterId(UUID activeCharacterId) {
    this.activeCharacterId = activeCharacterId;
  }

  public CharacterSwitchResponse snapshot(StateSnapshotRef snapshot) {
    this.snapshot = snapshot;
    return this;
  }

  /**
   * Get snapshot
   * @return snapshot
   */
  @NotNull @Valid 
  @Schema(name = "snapshot", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("snapshot")
  public StateSnapshotRef getSnapshot() {
    return snapshot;
  }

  public void setSnapshot(StateSnapshotRef snapshot) {
    this.snapshot = snapshot;
  }

  public CharacterSwitchResponse events(List<@Valid CharacterSwitchedEvent> events) {
    this.events = events;
    return this;
  }

  public CharacterSwitchResponse addEventsItem(CharacterSwitchedEvent eventsItem) {
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
  public List<@Valid CharacterSwitchedEvent> getEvents() {
    return events;
  }

  public void setEvents(List<@Valid CharacterSwitchedEvent> events) {
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
    CharacterSwitchResponse characterSwitchResponse = (CharacterSwitchResponse) o;
    return Objects.equals(this.activeCharacterId, characterSwitchResponse.activeCharacterId) &&
        Objects.equals(this.snapshot, characterSwitchResponse.snapshot) &&
        Objects.equals(this.events, characterSwitchResponse.events);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activeCharacterId, snapshot, events);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterSwitchResponse {\n");
    sb.append("    activeCharacterId: ").append(toIndentedString(activeCharacterId)).append("\n");
    sb.append("    snapshot: ").append(toIndentedString(snapshot)).append("\n");
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

