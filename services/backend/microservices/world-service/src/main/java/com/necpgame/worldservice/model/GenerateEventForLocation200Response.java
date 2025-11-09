package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.worldservice.model.TriggeredEventInstance;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GenerateEventForLocation200Response
 */

@JsonTypeName("generateEventForLocation_200_response")

public class GenerateEventForLocation200Response {

  private @Nullable Boolean eventGenerated;

  private @Nullable TriggeredEventInstance event;

  private @Nullable Float generationChanceWas;

  public GenerateEventForLocation200Response eventGenerated(@Nullable Boolean eventGenerated) {
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

  public GenerateEventForLocation200Response event(@Nullable TriggeredEventInstance event) {
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
  public @Nullable TriggeredEventInstance getEvent() {
    return event;
  }

  public void setEvent(@Nullable TriggeredEventInstance event) {
    this.event = event;
  }

  public GenerateEventForLocation200Response generationChanceWas(@Nullable Float generationChanceWas) {
    this.generationChanceWas = generationChanceWas;
    return this;
  }

  /**
   * Get generationChanceWas
   * @return generationChanceWas
   */
  
  @Schema(name = "generation_chance_was", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("generation_chance_was")
  public @Nullable Float getGenerationChanceWas() {
    return generationChanceWas;
  }

  public void setGenerationChanceWas(@Nullable Float generationChanceWas) {
    this.generationChanceWas = generationChanceWas;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateEventForLocation200Response generateEventForLocation200Response = (GenerateEventForLocation200Response) o;
    return Objects.equals(this.eventGenerated, generateEventForLocation200Response.eventGenerated) &&
        Objects.equals(this.event, generateEventForLocation200Response.event) &&
        Objects.equals(this.generationChanceWas, generateEventForLocation200Response.generationChanceWas);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventGenerated, event, generationChanceWas);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateEventForLocation200Response {\n");
    sb.append("    eventGenerated: ").append(toIndentedString(eventGenerated)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    generationChanceWas: ").append(toIndentedString(generationChanceWas)).append("\n");
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

