package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GenerateEventRequest
 */


public class GenerateEventRequest {

  private String era;

  private String location;

  private JsonNullable<String> eventType = JsonNullable.<String>undefined();

  private Boolean forceGenerate = false;

  public GenerateEventRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GenerateEventRequest(String era, String location) {
    this.era = era;
    this.location = location;
  }

  public GenerateEventRequest era(String era) {
    this.era = era;
    return this;
  }

  /**
   * Get era
   * @return era
   */
  @NotNull 
  @Schema(name = "era", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("era")
  public String getEra() {
    return era;
  }

  public void setEra(String era) {
    this.era = era;
  }

  public GenerateEventRequest location(String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  @NotNull 
  @Schema(name = "location", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("location")
  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public GenerateEventRequest eventType(String eventType) {
    this.eventType = JsonNullable.of(eventType);
    return this;
  }

  /**
   * Get eventType
   * @return eventType
   */
  
  @Schema(name = "event_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_type")
  public JsonNullable<String> getEventType() {
    return eventType;
  }

  public void setEventType(JsonNullable<String> eventType) {
    this.eventType = eventType;
  }

  public GenerateEventRequest forceGenerate(Boolean forceGenerate) {
    this.forceGenerate = forceGenerate;
    return this;
  }

  /**
   * Для тестирования
   * @return forceGenerate
   */
  
  @Schema(name = "force_generate", description = "Для тестирования", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("force_generate")
  public Boolean getForceGenerate() {
    return forceGenerate;
  }

  public void setForceGenerate(Boolean forceGenerate) {
    this.forceGenerate = forceGenerate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateEventRequest generateEventRequest = (GenerateEventRequest) o;
    return Objects.equals(this.era, generateEventRequest.era) &&
        Objects.equals(this.location, generateEventRequest.location) &&
        equalsNullable(this.eventType, generateEventRequest.eventType) &&
        Objects.equals(this.forceGenerate, generateEventRequest.forceGenerate);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(era, location, hashCodeNullable(eventType), forceGenerate);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateEventRequest {\n");
    sb.append("    era: ").append(toIndentedString(era)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    forceGenerate: ").append(toIndentedString(forceGenerate)).append("\n");
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

