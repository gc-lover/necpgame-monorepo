package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
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
 * RomanceRelationshipDetailedAllOfRelationshipHistory
 */

@JsonTypeName("RomanceRelationshipDetailed_allOf_relationship_history")

public class RomanceRelationshipDetailedAllOfRelationshipHistory {

  private @Nullable String eventId;

  private @Nullable String eventName;

  private @Nullable String choiceMade;

  private @Nullable String outcome;

  private @Nullable Integer affectionChange;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime date;

  public RomanceRelationshipDetailedAllOfRelationshipHistory eventId(@Nullable String eventId) {
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

  public RomanceRelationshipDetailedAllOfRelationshipHistory eventName(@Nullable String eventName) {
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

  public RomanceRelationshipDetailedAllOfRelationshipHistory choiceMade(@Nullable String choiceMade) {
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

  public RomanceRelationshipDetailedAllOfRelationshipHistory outcome(@Nullable String outcome) {
    this.outcome = outcome;
    return this;
  }

  /**
   * Get outcome
   * @return outcome
   */
  
  @Schema(name = "outcome", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outcome")
  public @Nullable String getOutcome() {
    return outcome;
  }

  public void setOutcome(@Nullable String outcome) {
    this.outcome = outcome;
  }

  public RomanceRelationshipDetailedAllOfRelationshipHistory affectionChange(@Nullable Integer affectionChange) {
    this.affectionChange = affectionChange;
    return this;
  }

  /**
   * Get affectionChange
   * @return affectionChange
   */
  
  @Schema(name = "affection_change", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affection_change")
  public @Nullable Integer getAffectionChange() {
    return affectionChange;
  }

  public void setAffectionChange(@Nullable Integer affectionChange) {
    this.affectionChange = affectionChange;
  }

  public RomanceRelationshipDetailedAllOfRelationshipHistory date(@Nullable OffsetDateTime date) {
    this.date = date;
    return this;
  }

  /**
   * Get date
   * @return date
   */
  @Valid 
  @Schema(name = "date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("date")
  public @Nullable OffsetDateTime getDate() {
    return date;
  }

  public void setDate(@Nullable OffsetDateTime date) {
    this.date = date;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RomanceRelationshipDetailedAllOfRelationshipHistory romanceRelationshipDetailedAllOfRelationshipHistory = (RomanceRelationshipDetailedAllOfRelationshipHistory) o;
    return Objects.equals(this.eventId, romanceRelationshipDetailedAllOfRelationshipHistory.eventId) &&
        Objects.equals(this.eventName, romanceRelationshipDetailedAllOfRelationshipHistory.eventName) &&
        Objects.equals(this.choiceMade, romanceRelationshipDetailedAllOfRelationshipHistory.choiceMade) &&
        Objects.equals(this.outcome, romanceRelationshipDetailedAllOfRelationshipHistory.outcome) &&
        Objects.equals(this.affectionChange, romanceRelationshipDetailedAllOfRelationshipHistory.affectionChange) &&
        Objects.equals(this.date, romanceRelationshipDetailedAllOfRelationshipHistory.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, eventName, choiceMade, outcome, affectionChange, date);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RomanceRelationshipDetailedAllOfRelationshipHistory {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    eventName: ").append(toIndentedString(eventName)).append("\n");
    sb.append("    choiceMade: ").append(toIndentedString(choiceMade)).append("\n");
    sb.append("    outcome: ").append(toIndentedString(outcome)).append("\n");
    sb.append("    affectionChange: ").append(toIndentedString(affectionChange)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
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

