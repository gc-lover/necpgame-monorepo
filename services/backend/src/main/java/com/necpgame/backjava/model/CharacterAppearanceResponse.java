package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.CharacterAppearance;
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
 * CharacterAppearanceResponse
 */


public class CharacterAppearanceResponse {

  private UUID characterId;

  private CharacterAppearance appearance;

  @Valid
  private List<String> events = new ArrayList<>();

  public CharacterAppearanceResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterAppearanceResponse(UUID characterId, CharacterAppearance appearance, List<String> events) {
    this.characterId = characterId;
    this.appearance = appearance;
    this.events = events;
  }

  public CharacterAppearanceResponse characterId(UUID characterId) {
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

  public CharacterAppearanceResponse appearance(CharacterAppearance appearance) {
    this.appearance = appearance;
    return this;
  }

  /**
   * Get appearance
   * @return appearance
   */
  @NotNull @Valid 
  @Schema(name = "appearance", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("appearance")
  public CharacterAppearance getAppearance() {
    return appearance;
  }

  public void setAppearance(CharacterAppearance appearance) {
    this.appearance = appearance;
  }

  public CharacterAppearanceResponse events(List<String> events) {
    this.events = events;
    return this;
  }

  public CharacterAppearanceResponse addEventsItem(String eventsItem) {
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
  @NotNull 
  @Schema(name = "events", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("events")
  public List<String> getEvents() {
    return events;
  }

  public void setEvents(List<String> events) {
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
    CharacterAppearanceResponse characterAppearanceResponse = (CharacterAppearanceResponse) o;
    return Objects.equals(this.characterId, characterAppearanceResponse.characterId) &&
        Objects.equals(this.appearance, characterAppearanceResponse.appearance) &&
        Objects.equals(this.events, characterAppearanceResponse.events);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, appearance, events);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterAppearanceResponse {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    appearance: ").append(toIndentedString(appearance)).append("\n");
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

