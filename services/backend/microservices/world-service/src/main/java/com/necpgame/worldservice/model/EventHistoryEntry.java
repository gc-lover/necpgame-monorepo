package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EventHistoryEntry
 */


public class EventHistoryEntry {

  private @Nullable UUID instanceId;

  private @Nullable String eventId;

  private @Nullable String eventName;

  private @Nullable String choiceMade;

  private @Nullable String outcomeAchieved;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime triggeredAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime resolvedAt;

  private @Nullable String consequencesSummary;

  public EventHistoryEntry instanceId(@Nullable UUID instanceId) {
    this.instanceId = instanceId;
    return this;
  }

  /**
   * Get instanceId
   * @return instanceId
   */
  @Valid 
  @Schema(name = "instance_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("instance_id")
  public @Nullable UUID getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(@Nullable UUID instanceId) {
    this.instanceId = instanceId;
  }

  public EventHistoryEntry eventId(@Nullable String eventId) {
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

  public EventHistoryEntry eventName(@Nullable String eventName) {
    this.eventName = eventName;
    return this;
  }

  /**
   * Get eventName
   * @return eventName
   */
  
  @Schema(name = "event_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_name")
  public @Nullable String getEventName() {
    return eventName;
  }

  public void setEventName(@Nullable String eventName) {
    this.eventName = eventName;
  }

  public EventHistoryEntry choiceMade(@Nullable String choiceMade) {
    this.choiceMade = choiceMade;
    return this;
  }

  /**
   * Get choiceMade
   * @return choiceMade
   */
  
  @Schema(name = "choice_made", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("choice_made")
  public @Nullable String getChoiceMade() {
    return choiceMade;
  }

  public void setChoiceMade(@Nullable String choiceMade) {
    this.choiceMade = choiceMade;
  }

  public EventHistoryEntry outcomeAchieved(@Nullable String outcomeAchieved) {
    this.outcomeAchieved = outcomeAchieved;
    return this;
  }

  /**
   * Get outcomeAchieved
   * @return outcomeAchieved
   */
  
  @Schema(name = "outcome_achieved", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outcome_achieved")
  public @Nullable String getOutcomeAchieved() {
    return outcomeAchieved;
  }

  public void setOutcomeAchieved(@Nullable String outcomeAchieved) {
    this.outcomeAchieved = outcomeAchieved;
  }

  public EventHistoryEntry triggeredAt(@Nullable OffsetDateTime triggeredAt) {
    this.triggeredAt = triggeredAt;
    return this;
  }

  /**
   * Get triggeredAt
   * @return triggeredAt
   */
  @Valid 
  @Schema(name = "triggered_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("triggered_at")
  public @Nullable OffsetDateTime getTriggeredAt() {
    return triggeredAt;
  }

  public void setTriggeredAt(@Nullable OffsetDateTime triggeredAt) {
    this.triggeredAt = triggeredAt;
  }

  public EventHistoryEntry resolvedAt(@Nullable OffsetDateTime resolvedAt) {
    this.resolvedAt = resolvedAt;
    return this;
  }

  /**
   * Get resolvedAt
   * @return resolvedAt
   */
  @Valid 
  @Schema(name = "resolved_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resolved_at")
  public @Nullable OffsetDateTime getResolvedAt() {
    return resolvedAt;
  }

  public void setResolvedAt(@Nullable OffsetDateTime resolvedAt) {
    this.resolvedAt = resolvedAt;
  }

  public EventHistoryEntry consequencesSummary(@Nullable String consequencesSummary) {
    this.consequencesSummary = consequencesSummary;
    return this;
  }

  /**
   * Get consequencesSummary
   * @return consequencesSummary
   */
  
  @Schema(name = "consequences_summary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequences_summary")
  public @Nullable String getConsequencesSummary() {
    return consequencesSummary;
  }

  public void setConsequencesSummary(@Nullable String consequencesSummary) {
    this.consequencesSummary = consequencesSummary;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventHistoryEntry eventHistoryEntry = (EventHistoryEntry) o;
    return Objects.equals(this.instanceId, eventHistoryEntry.instanceId) &&
        Objects.equals(this.eventId, eventHistoryEntry.eventId) &&
        Objects.equals(this.eventName, eventHistoryEntry.eventName) &&
        Objects.equals(this.choiceMade, eventHistoryEntry.choiceMade) &&
        Objects.equals(this.outcomeAchieved, eventHistoryEntry.outcomeAchieved) &&
        Objects.equals(this.triggeredAt, eventHistoryEntry.triggeredAt) &&
        Objects.equals(this.resolvedAt, eventHistoryEntry.resolvedAt) &&
        Objects.equals(this.consequencesSummary, eventHistoryEntry.consequencesSummary);
  }

  @Override
  public int hashCode() {
    return Objects.hash(instanceId, eventId, eventName, choiceMade, outcomeAchieved, triggeredAt, resolvedAt, consequencesSummary);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventHistoryEntry {\n");
    sb.append("    instanceId: ").append(toIndentedString(instanceId)).append("\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    eventName: ").append(toIndentedString(eventName)).append("\n");
    sb.append("    choiceMade: ").append(toIndentedString(choiceMade)).append("\n");
    sb.append("    outcomeAchieved: ").append(toIndentedString(outcomeAchieved)).append("\n");
    sb.append("    triggeredAt: ").append(toIndentedString(triggeredAt)).append("\n");
    sb.append("    resolvedAt: ").append(toIndentedString(resolvedAt)).append("\n");
    sb.append("    consequencesSummary: ").append(toIndentedString(consequencesSummary)).append("\n");
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

