package com.necpgame.economyservice.model;

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
 * EconomyEventHistoryEntry
 */


public class EconomyEventHistoryEntry {

  private @Nullable UUID eventId;

  private @Nullable String name;

  private @Nullable String type;

  private @Nullable String severity;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endDate;

  private @Nullable Integer durationDays;

  private @Nullable String impactSummary;

  public EconomyEventHistoryEntry eventId(@Nullable UUID eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  @Valid 
  @Schema(name = "event_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_id")
  public @Nullable UUID getEventId() {
    return eventId;
  }

  public void setEventId(@Nullable UUID eventId) {
    this.eventId = eventId;
  }

  public EconomyEventHistoryEntry name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public EconomyEventHistoryEntry type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public EconomyEventHistoryEntry severity(@Nullable String severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("severity")
  public @Nullable String getSeverity() {
    return severity;
  }

  public void setSeverity(@Nullable String severity) {
    this.severity = severity;
  }

  public EconomyEventHistoryEntry startDate(@Nullable OffsetDateTime startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * Get startDate
   * @return startDate
   */
  @Valid 
  @Schema(name = "start_date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("start_date")
  public @Nullable OffsetDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(@Nullable OffsetDateTime startDate) {
    this.startDate = startDate;
  }

  public EconomyEventHistoryEntry endDate(@Nullable OffsetDateTime endDate) {
    this.endDate = endDate;
    return this;
  }

  /**
   * Get endDate
   * @return endDate
   */
  @Valid 
  @Schema(name = "end_date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("end_date")
  public @Nullable OffsetDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(@Nullable OffsetDateTime endDate) {
    this.endDate = endDate;
  }

  public EconomyEventHistoryEntry durationDays(@Nullable Integer durationDays) {
    this.durationDays = durationDays;
    return this;
  }

  /**
   * Get durationDays
   * @return durationDays
   */
  
  @Schema(name = "duration_days", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration_days")
  public @Nullable Integer getDurationDays() {
    return durationDays;
  }

  public void setDurationDays(@Nullable Integer durationDays) {
    this.durationDays = durationDays;
  }

  public EconomyEventHistoryEntry impactSummary(@Nullable String impactSummary) {
    this.impactSummary = impactSummary;
    return this;
  }

  /**
   * Get impactSummary
   * @return impactSummary
   */
  
  @Schema(name = "impact_summary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("impact_summary")
  public @Nullable String getImpactSummary() {
    return impactSummary;
  }

  public void setImpactSummary(@Nullable String impactSummary) {
    this.impactSummary = impactSummary;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EconomyEventHistoryEntry economyEventHistoryEntry = (EconomyEventHistoryEntry) o;
    return Objects.equals(this.eventId, economyEventHistoryEntry.eventId) &&
        Objects.equals(this.name, economyEventHistoryEntry.name) &&
        Objects.equals(this.type, economyEventHistoryEntry.type) &&
        Objects.equals(this.severity, economyEventHistoryEntry.severity) &&
        Objects.equals(this.startDate, economyEventHistoryEntry.startDate) &&
        Objects.equals(this.endDate, economyEventHistoryEntry.endDate) &&
        Objects.equals(this.durationDays, economyEventHistoryEntry.durationDays) &&
        Objects.equals(this.impactSummary, economyEventHistoryEntry.impactSummary);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, name, type, severity, startDate, endDate, durationDays, impactSummary);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EconomyEventHistoryEntry {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    durationDays: ").append(toIndentedString(durationDays)).append("\n");
    sb.append("    impactSummary: ").append(toIndentedString(impactSummary)).append("\n");
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

