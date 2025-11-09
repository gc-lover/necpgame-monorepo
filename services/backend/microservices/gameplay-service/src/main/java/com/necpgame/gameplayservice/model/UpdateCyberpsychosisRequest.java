package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UpdateCyberpsychosisRequest
 */

@JsonTypeName("updateCyberpsychosis_request")

public class UpdateCyberpsychosisRequest {

  private String characterId;

  /**
   * Gets or Sets eventType
   */
  public enum EventTypeEnum {
    IMPLANT_INSTALLED("implant_installed"),
    
    COMBAT_STRESS("combat_stress"),
    
    IMPLANT_DAMAGED("implant_damaged"),
    
    LIMIT_EXCEEDED("limit_exceeded"),
    
    HACKING("hacking"),
    
    CRITICAL_EVENT("critical_event");

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

  private @Nullable Object eventData;

  public UpdateCyberpsychosisRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UpdateCyberpsychosisRequest(String characterId) {
    this.characterId = characterId;
  }

  public UpdateCyberpsychosisRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public UpdateCyberpsychosisRequest eventType(@Nullable EventTypeEnum eventType) {
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

  public UpdateCyberpsychosisRequest eventData(@Nullable Object eventData) {
    this.eventData = eventData;
    return this;
  }

  /**
   * Get eventData
   * @return eventData
   */
  
  @Schema(name = "event_data", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_data")
  public @Nullable Object getEventData() {
    return eventData;
  }

  public void setEventData(@Nullable Object eventData) {
    this.eventData = eventData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateCyberpsychosisRequest updateCyberpsychosisRequest = (UpdateCyberpsychosisRequest) o;
    return Objects.equals(this.characterId, updateCyberpsychosisRequest.characterId) &&
        Objects.equals(this.eventType, updateCyberpsychosisRequest.eventType) &&
        Objects.equals(this.eventData, updateCyberpsychosisRequest.eventData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, eventType, eventData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateCyberpsychosisRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    eventData: ").append(toIndentedString(eventData)).append("\n");
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

