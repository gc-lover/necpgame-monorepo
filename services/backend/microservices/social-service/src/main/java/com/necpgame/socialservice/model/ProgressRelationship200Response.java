package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * ProgressRelationship200Response
 */

@JsonTypeName("progressRelationship_200_response")

public class ProgressRelationship200Response {

  private @Nullable String previousStage;

  private @Nullable String newStage;

  @Valid
  private List<String> unlockedEvents = new ArrayList<>();

  public ProgressRelationship200Response previousStage(@Nullable String previousStage) {
    this.previousStage = previousStage;
    return this;
  }

  /**
   * Get previousStage
   * @return previousStage
   */
  
  @Schema(name = "previous_stage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previous_stage")
  public @Nullable String getPreviousStage() {
    return previousStage;
  }

  public void setPreviousStage(@Nullable String previousStage) {
    this.previousStage = previousStage;
  }

  public ProgressRelationship200Response newStage(@Nullable String newStage) {
    this.newStage = newStage;
    return this;
  }

  /**
   * Get newStage
   * @return newStage
   */
  
  @Schema(name = "new_stage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("new_stage")
  public @Nullable String getNewStage() {
    return newStage;
  }

  public void setNewStage(@Nullable String newStage) {
    this.newStage = newStage;
  }

  public ProgressRelationship200Response unlockedEvents(List<String> unlockedEvents) {
    this.unlockedEvents = unlockedEvents;
    return this;
  }

  public ProgressRelationship200Response addUnlockedEventsItem(String unlockedEventsItem) {
    if (this.unlockedEvents == null) {
      this.unlockedEvents = new ArrayList<>();
    }
    this.unlockedEvents.add(unlockedEventsItem);
    return this;
  }

  /**
   * Get unlockedEvents
   * @return unlockedEvents
   */
  
  @Schema(name = "unlocked_events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocked_events")
  public List<String> getUnlockedEvents() {
    return unlockedEvents;
  }

  public void setUnlockedEvents(List<String> unlockedEvents) {
    this.unlockedEvents = unlockedEvents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProgressRelationship200Response progressRelationship200Response = (ProgressRelationship200Response) o;
    return Objects.equals(this.previousStage, progressRelationship200Response.previousStage) &&
        Objects.equals(this.newStage, progressRelationship200Response.newStage) &&
        Objects.equals(this.unlockedEvents, progressRelationship200Response.unlockedEvents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(previousStage, newStage, unlockedEvents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProgressRelationship200Response {\n");
    sb.append("    previousStage: ").append(toIndentedString(previousStage)).append("\n");
    sb.append("    newStage: ").append(toIndentedString(newStage)).append("\n");
    sb.append("    unlockedEvents: ").append(toIndentedString(unlockedEvents)).append("\n");
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

