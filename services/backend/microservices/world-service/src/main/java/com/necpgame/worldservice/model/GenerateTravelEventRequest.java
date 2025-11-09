package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * GenerateTravelEventRequest
 */

@JsonTypeName("generateTravelEvent_request")

public class GenerateTravelEventRequest {

  private UUID characterId;

  private String origin;

  private String destination;

  private String transportMode;

  private @Nullable String timeOfDay;

  public GenerateTravelEventRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GenerateTravelEventRequest(UUID characterId, String origin, String destination, String transportMode) {
    this.characterId = characterId;
    this.origin = origin;
    this.destination = destination;
    this.transportMode = transportMode;
  }

  public GenerateTravelEventRequest characterId(UUID characterId) {
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

  public GenerateTravelEventRequest origin(String origin) {
    this.origin = origin;
    return this;
  }

  /**
   * Get origin
   * @return origin
   */
  @NotNull 
  @Schema(name = "origin", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("origin")
  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public GenerateTravelEventRequest destination(String destination) {
    this.destination = destination;
    return this;
  }

  /**
   * Get destination
   * @return destination
   */
  @NotNull 
  @Schema(name = "destination", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("destination")
  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public GenerateTravelEventRequest transportMode(String transportMode) {
    this.transportMode = transportMode;
    return this;
  }

  /**
   * Get transportMode
   * @return transportMode
   */
  @NotNull 
  @Schema(name = "transport_mode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("transport_mode")
  public String getTransportMode() {
    return transportMode;
  }

  public void setTransportMode(String transportMode) {
    this.transportMode = transportMode;
  }

  public GenerateTravelEventRequest timeOfDay(@Nullable String timeOfDay) {
    this.timeOfDay = timeOfDay;
    return this;
  }

  /**
   * Get timeOfDay
   * @return timeOfDay
   */
  
  @Schema(name = "time_of_day", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_of_day")
  public @Nullable String getTimeOfDay() {
    return timeOfDay;
  }

  public void setTimeOfDay(@Nullable String timeOfDay) {
    this.timeOfDay = timeOfDay;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateTravelEventRequest generateTravelEventRequest = (GenerateTravelEventRequest) o;
    return Objects.equals(this.characterId, generateTravelEventRequest.characterId) &&
        Objects.equals(this.origin, generateTravelEventRequest.origin) &&
        Objects.equals(this.destination, generateTravelEventRequest.destination) &&
        Objects.equals(this.transportMode, generateTravelEventRequest.transportMode) &&
        Objects.equals(this.timeOfDay, generateTravelEventRequest.timeOfDay);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, origin, destination, transportMode, timeOfDay);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateTravelEventRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    origin: ").append(toIndentedString(origin)).append("\n");
    sb.append("    destination: ").append(toIndentedString(destination)).append("\n");
    sb.append("    transportMode: ").append(toIndentedString(transportMode)).append("\n");
    sb.append("    timeOfDay: ").append(toIndentedString(timeOfDay)).append("\n");
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

