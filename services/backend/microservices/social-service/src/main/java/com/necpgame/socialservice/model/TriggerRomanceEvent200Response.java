package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.TriggerRomanceEvent200ResponseRelationshipChanges;
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
 * TriggerRomanceEvent200Response
 */

@JsonTypeName("triggerRomanceEvent_200_response")

public class TriggerRomanceEvent200Response {

  private @Nullable String eventId;

  private @Nullable Boolean success;

  /**
   * Gets or Sets outcome
   */
  public enum OutcomeEnum {
    SUCCESS("success"),
    
    NEUTRAL("neutral"),
    
    FAILURE("failure");

    private final String value;

    OutcomeEnum(String value) {
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
    public static OutcomeEnum fromValue(String value) {
      for (OutcomeEnum b : OutcomeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable OutcomeEnum outcome;

  private @Nullable TriggerRomanceEvent200ResponseRelationshipChanges relationshipChanges;

  @Valid
  private List<String> unlockedEvents = new ArrayList<>();

  public TriggerRomanceEvent200Response eventId(@Nullable String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  
  @Schema(name = "event_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_id")
  public @Nullable String getEventId() {
    return eventId;
  }

  public void setEventId(@Nullable String eventId) {
    this.eventId = eventId;
  }

  public TriggerRomanceEvent200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public TriggerRomanceEvent200Response outcome(@Nullable OutcomeEnum outcome) {
    this.outcome = outcome;
    return this;
  }

  /**
   * Get outcome
   * @return outcome
   */
  
  @Schema(name = "outcome", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outcome")
  public @Nullable OutcomeEnum getOutcome() {
    return outcome;
  }

  public void setOutcome(@Nullable OutcomeEnum outcome) {
    this.outcome = outcome;
  }

  public TriggerRomanceEvent200Response relationshipChanges(@Nullable TriggerRomanceEvent200ResponseRelationshipChanges relationshipChanges) {
    this.relationshipChanges = relationshipChanges;
    return this;
  }

  /**
   * Get relationshipChanges
   * @return relationshipChanges
   */
  @Valid 
  @Schema(name = "relationship_changes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationship_changes")
  public @Nullable TriggerRomanceEvent200ResponseRelationshipChanges getRelationshipChanges() {
    return relationshipChanges;
  }

  public void setRelationshipChanges(@Nullable TriggerRomanceEvent200ResponseRelationshipChanges relationshipChanges) {
    this.relationshipChanges = relationshipChanges;
  }

  public TriggerRomanceEvent200Response unlockedEvents(List<String> unlockedEvents) {
    this.unlockedEvents = unlockedEvents;
    return this;
  }

  public TriggerRomanceEvent200Response addUnlockedEventsItem(String unlockedEventsItem) {
    if (this.unlockedEvents == null) {
      this.unlockedEvents = new ArrayList<>();
    }
    this.unlockedEvents.add(unlockedEventsItem);
    return this;
  }

  /**
   * События разблокированные этим
   * @return unlockedEvents
   */
  
  @Schema(name = "unlocked_events", description = "События разблокированные этим", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    TriggerRomanceEvent200Response triggerRomanceEvent200Response = (TriggerRomanceEvent200Response) o;
    return Objects.equals(this.eventId, triggerRomanceEvent200Response.eventId) &&
        Objects.equals(this.success, triggerRomanceEvent200Response.success) &&
        Objects.equals(this.outcome, triggerRomanceEvent200Response.outcome) &&
        Objects.equals(this.relationshipChanges, triggerRomanceEvent200Response.relationshipChanges) &&
        Objects.equals(this.unlockedEvents, triggerRomanceEvent200Response.unlockedEvents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, success, outcome, relationshipChanges, unlockedEvents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TriggerRomanceEvent200Response {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    outcome: ").append(toIndentedString(outcome)).append("\n");
    sb.append("    relationshipChanges: ").append(toIndentedString(relationshipChanges)).append("\n");
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

