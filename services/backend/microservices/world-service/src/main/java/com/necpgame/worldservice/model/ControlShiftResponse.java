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
 * ControlShiftResponse
 */


public class ControlShiftResponse {

  private UUID eventId;

  private UUID timelineEntryId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime scheduledAt;

  private @Nullable String approvedBy;

  private UUID auditId;

  public ControlShiftResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ControlShiftResponse(UUID eventId, UUID timelineEntryId, OffsetDateTime scheduledAt, UUID auditId) {
    this.eventId = eventId;
    this.timelineEntryId = timelineEntryId;
    this.scheduledAt = scheduledAt;
    this.auditId = auditId;
  }

  public ControlShiftResponse eventId(UUID eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  @NotNull @Valid 
  @Schema(name = "eventId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eventId")
  public UUID getEventId() {
    return eventId;
  }

  public void setEventId(UUID eventId) {
    this.eventId = eventId;
  }

  public ControlShiftResponse timelineEntryId(UUID timelineEntryId) {
    this.timelineEntryId = timelineEntryId;
    return this;
  }

  /**
   * Get timelineEntryId
   * @return timelineEntryId
   */
  @NotNull @Valid 
  @Schema(name = "timelineEntryId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timelineEntryId")
  public UUID getTimelineEntryId() {
    return timelineEntryId;
  }

  public void setTimelineEntryId(UUID timelineEntryId) {
    this.timelineEntryId = timelineEntryId;
  }

  public ControlShiftResponse scheduledAt(OffsetDateTime scheduledAt) {
    this.scheduledAt = scheduledAt;
    return this;
  }

  /**
   * Get scheduledAt
   * @return scheduledAt
   */
  @NotNull @Valid 
  @Schema(name = "scheduledAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("scheduledAt")
  public OffsetDateTime getScheduledAt() {
    return scheduledAt;
  }

  public void setScheduledAt(OffsetDateTime scheduledAt) {
    this.scheduledAt = scheduledAt;
  }

  public ControlShiftResponse approvedBy(@Nullable String approvedBy) {
    this.approvedBy = approvedBy;
    return this;
  }

  /**
   * Get approvedBy
   * @return approvedBy
   */
  
  @Schema(name = "approvedBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("approvedBy")
  public @Nullable String getApprovedBy() {
    return approvedBy;
  }

  public void setApprovedBy(@Nullable String approvedBy) {
    this.approvedBy = approvedBy;
  }

  public ControlShiftResponse auditId(UUID auditId) {
    this.auditId = auditId;
    return this;
  }

  /**
   * Get auditId
   * @return auditId
   */
  @NotNull @Valid 
  @Schema(name = "auditId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("auditId")
  public UUID getAuditId() {
    return auditId;
  }

  public void setAuditId(UUID auditId) {
    this.auditId = auditId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ControlShiftResponse controlShiftResponse = (ControlShiftResponse) o;
    return Objects.equals(this.eventId, controlShiftResponse.eventId) &&
        Objects.equals(this.timelineEntryId, controlShiftResponse.timelineEntryId) &&
        Objects.equals(this.scheduledAt, controlShiftResponse.scheduledAt) &&
        Objects.equals(this.approvedBy, controlShiftResponse.approvedBy) &&
        Objects.equals(this.auditId, controlShiftResponse.auditId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, timelineEntryId, scheduledAt, approvedBy, auditId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ControlShiftResponse {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    timelineEntryId: ").append(toIndentedString(timelineEntryId)).append("\n");
    sb.append("    scheduledAt: ").append(toIndentedString(scheduledAt)).append("\n");
    sb.append("    approvedBy: ").append(toIndentedString(approvedBy)).append("\n");
    sb.append("    auditId: ").append(toIndentedString(auditId)).append("\n");
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

