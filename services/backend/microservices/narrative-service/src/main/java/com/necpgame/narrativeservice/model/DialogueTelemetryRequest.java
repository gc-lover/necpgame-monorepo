package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.narrativeservice.model.DialogueTelemetryRequestClient;
import com.necpgame.narrativeservice.model.TelemetryEvent;
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
 * DialogueTelemetryRequest
 */


public class DialogueTelemetryRequest {

  private UUID characterId;

  private String questId;

  private @Nullable UUID sessionId;

  /**
   * Gets or Sets difficulty
   */
  public enum DifficultyEnum {
    STORY("story"),
    
    DEFAULT("default"),
    
    HARDCORE("hardcore");

    private final String value;

    DifficultyEnum(String value) {
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
    public static DifficultyEnum fromValue(String value) {
      for (DifficultyEnum b : DifficultyEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable DifficultyEnum difficulty;

  @Valid
  private List<@Valid TelemetryEvent> events = new ArrayList<>();

  private @Nullable DialogueTelemetryRequestClient client;

  public DialogueTelemetryRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DialogueTelemetryRequest(UUID characterId, String questId, List<@Valid TelemetryEvent> events) {
    this.characterId = characterId;
    this.questId = questId;
    this.events = events;
  }

  public DialogueTelemetryRequest characterId(UUID characterId) {
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

  public DialogueTelemetryRequest questId(String questId) {
    this.questId = questId;
    return this;
  }

  /**
   * Get questId
   * @return questId
   */
  @NotNull 
  @Schema(name = "questId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("questId")
  public String getQuestId() {
    return questId;
  }

  public void setQuestId(String questId) {
    this.questId = questId;
  }

  public DialogueTelemetryRequest sessionId(@Nullable UUID sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  @Valid 
  @Schema(name = "sessionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sessionId")
  public @Nullable UUID getSessionId() {
    return sessionId;
  }

  public void setSessionId(@Nullable UUID sessionId) {
    this.sessionId = sessionId;
  }

  public DialogueTelemetryRequest difficulty(@Nullable DifficultyEnum difficulty) {
    this.difficulty = difficulty;
    return this;
  }

  /**
   * Get difficulty
   * @return difficulty
   */
  
  @Schema(name = "difficulty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("difficulty")
  public @Nullable DifficultyEnum getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(@Nullable DifficultyEnum difficulty) {
    this.difficulty = difficulty;
  }

  public DialogueTelemetryRequest events(List<@Valid TelemetryEvent> events) {
    this.events = events;
    return this;
  }

  public DialogueTelemetryRequest addEventsItem(TelemetryEvent eventsItem) {
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
  public List<@Valid TelemetryEvent> getEvents() {
    return events;
  }

  public void setEvents(List<@Valid TelemetryEvent> events) {
    this.events = events;
  }

  public DialogueTelemetryRequest client(@Nullable DialogueTelemetryRequestClient client) {
    this.client = client;
    return this;
  }

  /**
   * Get client
   * @return client
   */
  @Valid 
  @Schema(name = "client", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("client")
  public @Nullable DialogueTelemetryRequestClient getClient() {
    return client;
  }

  public void setClient(@Nullable DialogueTelemetryRequestClient client) {
    this.client = client;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DialogueTelemetryRequest dialogueTelemetryRequest = (DialogueTelemetryRequest) o;
    return Objects.equals(this.characterId, dialogueTelemetryRequest.characterId) &&
        Objects.equals(this.questId, dialogueTelemetryRequest.questId) &&
        Objects.equals(this.sessionId, dialogueTelemetryRequest.sessionId) &&
        Objects.equals(this.difficulty, dialogueTelemetryRequest.difficulty) &&
        Objects.equals(this.events, dialogueTelemetryRequest.events) &&
        Objects.equals(this.client, dialogueTelemetryRequest.client);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, questId, sessionId, difficulty, events, client);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DialogueTelemetryRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    difficulty: ").append(toIndentedString(difficulty)).append("\n");
    sb.append("    events: ").append(toIndentedString(events)).append("\n");
    sb.append("    client: ").append(toIndentedString(client)).append("\n");
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

