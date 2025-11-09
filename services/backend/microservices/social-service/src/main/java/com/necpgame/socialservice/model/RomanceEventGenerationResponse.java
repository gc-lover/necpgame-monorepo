package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.RomanceEventGenerationResponseGeneratedEventsInner;
import com.necpgame.socialservice.model.RomanceEventGenerationResponseGenerationMetadata;
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
 * RomanceEventGenerationResponse
 */


public class RomanceEventGenerationResponse {

  @Valid
  private List<@Valid RomanceEventGenerationResponseGeneratedEventsInner> generatedEvents = new ArrayList<>();

  private @Nullable RomanceEventGenerationResponseGenerationMetadata generationMetadata;

  public RomanceEventGenerationResponse generatedEvents(List<@Valid RomanceEventGenerationResponseGeneratedEventsInner> generatedEvents) {
    this.generatedEvents = generatedEvents;
    return this;
  }

  public RomanceEventGenerationResponse addGeneratedEventsItem(RomanceEventGenerationResponseGeneratedEventsInner generatedEventsItem) {
    if (this.generatedEvents == null) {
      this.generatedEvents = new ArrayList<>();
    }
    this.generatedEvents.add(generatedEventsItem);
    return this;
  }

  /**
   * Get generatedEvents
   * @return generatedEvents
   */
  @Valid 
  @Schema(name = "generated_events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("generated_events")
  public List<@Valid RomanceEventGenerationResponseGeneratedEventsInner> getGeneratedEvents() {
    return generatedEvents;
  }

  public void setGeneratedEvents(List<@Valid RomanceEventGenerationResponseGeneratedEventsInner> generatedEvents) {
    this.generatedEvents = generatedEvents;
  }

  public RomanceEventGenerationResponse generationMetadata(@Nullable RomanceEventGenerationResponseGenerationMetadata generationMetadata) {
    this.generationMetadata = generationMetadata;
    return this;
  }

  /**
   * Get generationMetadata
   * @return generationMetadata
   */
  @Valid 
  @Schema(name = "generation_metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("generation_metadata")
  public @Nullable RomanceEventGenerationResponseGenerationMetadata getGenerationMetadata() {
    return generationMetadata;
  }

  public void setGenerationMetadata(@Nullable RomanceEventGenerationResponseGenerationMetadata generationMetadata) {
    this.generationMetadata = generationMetadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RomanceEventGenerationResponse romanceEventGenerationResponse = (RomanceEventGenerationResponse) o;
    return Objects.equals(this.generatedEvents, romanceEventGenerationResponse.generatedEvents) &&
        Objects.equals(this.generationMetadata, romanceEventGenerationResponse.generationMetadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(generatedEvents, generationMetadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RomanceEventGenerationResponse {\n");
    sb.append("    generatedEvents: ").append(toIndentedString(generatedEvents)).append("\n");
    sb.append("    generationMetadata: ").append(toIndentedString(generationMetadata)).append("\n");
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

