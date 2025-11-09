package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * ReportDetailAllOfHistory
 */

@JsonTypeName("ReportDetail_allOf_history")

public class ReportDetailAllOfHistory {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  private @Nullable UUID moderatorId;

  private @Nullable String action;

  private @Nullable String notes;

  public ReportDetailAllOfHistory timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public ReportDetailAllOfHistory moderatorId(@Nullable UUID moderatorId) {
    this.moderatorId = moderatorId;
    return this;
  }

  /**
   * Get moderatorId
   * @return moderatorId
   */
  @Valid 
  @Schema(name = "moderatorId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("moderatorId")
  public @Nullable UUID getModeratorId() {
    return moderatorId;
  }

  public void setModeratorId(@Nullable UUID moderatorId) {
    this.moderatorId = moderatorId;
  }

  public ReportDetailAllOfHistory action(@Nullable String action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  
  @Schema(name = "action", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("action")
  public @Nullable String getAction() {
    return action;
  }

  public void setAction(@Nullable String action) {
    this.action = action;
  }

  public ReportDetailAllOfHistory notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReportDetailAllOfHistory reportDetailAllOfHistory = (ReportDetailAllOfHistory) o;
    return Objects.equals(this.timestamp, reportDetailAllOfHistory.timestamp) &&
        Objects.equals(this.moderatorId, reportDetailAllOfHistory.moderatorId) &&
        Objects.equals(this.action, reportDetailAllOfHistory.action) &&
        Objects.equals(this.notes, reportDetailAllOfHistory.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, moderatorId, action, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReportDetailAllOfHistory {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    moderatorId: ").append(toIndentedString(moderatorId)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
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

