package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.worldservice.model.TravelEventInstance;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GenerateTravelEvent200Response
 */

@JsonTypeName("generateTravelEvent_200_response")

public class GenerateTravelEvent200Response {

  private @Nullable Boolean eventGenerated;

  private @Nullable TravelEventInstance event;

  public GenerateTravelEvent200Response eventGenerated(@Nullable Boolean eventGenerated) {
    this.eventGenerated = eventGenerated;
    return this;
  }

  /**
   * Get eventGenerated
   * @return eventGenerated
   */
  
  @Schema(name = "event_generated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_generated")
  public @Nullable Boolean getEventGenerated() {
    return eventGenerated;
  }

  public void setEventGenerated(@Nullable Boolean eventGenerated) {
    this.eventGenerated = eventGenerated;
  }

  public GenerateTravelEvent200Response event(@Nullable TravelEventInstance event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
   */
  @Valid 
  @Schema(name = "event", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event")
  public @Nullable TravelEventInstance getEvent() {
    return event;
  }

  public void setEvent(@Nullable TravelEventInstance event) {
    this.event = event;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateTravelEvent200Response generateTravelEvent200Response = (GenerateTravelEvent200Response) o;
    return Objects.equals(this.eventGenerated, generateTravelEvent200Response.eventGenerated) &&
        Objects.equals(this.event, generateTravelEvent200Response.event);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventGenerated, event);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateTravelEvent200Response {\n");
    sb.append("    eventGenerated: ").append(toIndentedString(eventGenerated)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
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

