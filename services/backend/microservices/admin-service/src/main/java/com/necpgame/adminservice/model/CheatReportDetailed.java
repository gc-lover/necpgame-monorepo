package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CheatReportDetailed
 */


public class CheatReportDetailed {

  private @Nullable UUID reportId;

  private @Nullable UUID reportedPlayerId;

  private @Nullable String cheatType;

  private @Nullable String severity;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("PENDING"),
    
    UNDER_REVIEW("UNDER_REVIEW"),
    
    CONFIRMED("CONFIRMED"),
    
    DISMISSED("DISMISSED");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  private @Nullable Boolean autoDetected;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  private @Nullable Object evidence;

  private JsonNullable<UUID> reviewerId = JsonNullable.<UUID>undefined();

  private JsonNullable<String> reviewNotes = JsonNullable.<String>undefined();

  private JsonNullable<String> actionTaken = JsonNullable.<String>undefined();

  public CheatReportDetailed reportId(@Nullable UUID reportId) {
    this.reportId = reportId;
    return this;
  }

  /**
   * Get reportId
   * @return reportId
   */
  @Valid 
  @Schema(name = "report_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("report_id")
  public @Nullable UUID getReportId() {
    return reportId;
  }

  public void setReportId(@Nullable UUID reportId) {
    this.reportId = reportId;
  }

  public CheatReportDetailed reportedPlayerId(@Nullable UUID reportedPlayerId) {
    this.reportedPlayerId = reportedPlayerId;
    return this;
  }

  /**
   * Get reportedPlayerId
   * @return reportedPlayerId
   */
  @Valid 
  @Schema(name = "reported_player_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reported_player_id")
  public @Nullable UUID getReportedPlayerId() {
    return reportedPlayerId;
  }

  public void setReportedPlayerId(@Nullable UUID reportedPlayerId) {
    this.reportedPlayerId = reportedPlayerId;
  }

  public CheatReportDetailed cheatType(@Nullable String cheatType) {
    this.cheatType = cheatType;
    return this;
  }

  /**
   * Get cheatType
   * @return cheatType
   */
  
  @Schema(name = "cheat_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cheat_type")
  public @Nullable String getCheatType() {
    return cheatType;
  }

  public void setCheatType(@Nullable String cheatType) {
    this.cheatType = cheatType;
  }

  public CheatReportDetailed severity(@Nullable String severity) {
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

  public CheatReportDetailed status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public CheatReportDetailed autoDetected(@Nullable Boolean autoDetected) {
    this.autoDetected = autoDetected;
    return this;
  }

  /**
   * Get autoDetected
   * @return autoDetected
   */
  
  @Schema(name = "auto_detected", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("auto_detected")
  public @Nullable Boolean getAutoDetected() {
    return autoDetected;
  }

  public void setAutoDetected(@Nullable Boolean autoDetected) {
    this.autoDetected = autoDetected;
  }

  public CheatReportDetailed createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "created_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_at")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public CheatReportDetailed evidence(@Nullable Object evidence) {
    this.evidence = evidence;
    return this;
  }

  /**
   * Get evidence
   * @return evidence
   */
  
  @Schema(name = "evidence", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("evidence")
  public @Nullable Object getEvidence() {
    return evidence;
  }

  public void setEvidence(@Nullable Object evidence) {
    this.evidence = evidence;
  }

  public CheatReportDetailed reviewerId(UUID reviewerId) {
    this.reviewerId = JsonNullable.of(reviewerId);
    return this;
  }

  /**
   * Get reviewerId
   * @return reviewerId
   */
  @Valid 
  @Schema(name = "reviewer_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reviewer_id")
  public JsonNullable<UUID> getReviewerId() {
    return reviewerId;
  }

  public void setReviewerId(JsonNullable<UUID> reviewerId) {
    this.reviewerId = reviewerId;
  }

  public CheatReportDetailed reviewNotes(String reviewNotes) {
    this.reviewNotes = JsonNullable.of(reviewNotes);
    return this;
  }

  /**
   * Get reviewNotes
   * @return reviewNotes
   */
  
  @Schema(name = "review_notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("review_notes")
  public JsonNullable<String> getReviewNotes() {
    return reviewNotes;
  }

  public void setReviewNotes(JsonNullable<String> reviewNotes) {
    this.reviewNotes = reviewNotes;
  }

  public CheatReportDetailed actionTaken(String actionTaken) {
    this.actionTaken = JsonNullable.of(actionTaken);
    return this;
  }

  /**
   * Get actionTaken
   * @return actionTaken
   */
  
  @Schema(name = "action_taken", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("action_taken")
  public JsonNullable<String> getActionTaken() {
    return actionTaken;
  }

  public void setActionTaken(JsonNullable<String> actionTaken) {
    this.actionTaken = actionTaken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CheatReportDetailed cheatReportDetailed = (CheatReportDetailed) o;
    return Objects.equals(this.reportId, cheatReportDetailed.reportId) &&
        Objects.equals(this.reportedPlayerId, cheatReportDetailed.reportedPlayerId) &&
        Objects.equals(this.cheatType, cheatReportDetailed.cheatType) &&
        Objects.equals(this.severity, cheatReportDetailed.severity) &&
        Objects.equals(this.status, cheatReportDetailed.status) &&
        Objects.equals(this.autoDetected, cheatReportDetailed.autoDetected) &&
        Objects.equals(this.createdAt, cheatReportDetailed.createdAt) &&
        Objects.equals(this.evidence, cheatReportDetailed.evidence) &&
        equalsNullable(this.reviewerId, cheatReportDetailed.reviewerId) &&
        equalsNullable(this.reviewNotes, cheatReportDetailed.reviewNotes) &&
        equalsNullable(this.actionTaken, cheatReportDetailed.actionTaken);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(reportId, reportedPlayerId, cheatType, severity, status, autoDetected, createdAt, evidence, hashCodeNullable(reviewerId), hashCodeNullable(reviewNotes), hashCodeNullable(actionTaken));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CheatReportDetailed {\n");
    sb.append("    reportId: ").append(toIndentedString(reportId)).append("\n");
    sb.append("    reportedPlayerId: ").append(toIndentedString(reportedPlayerId)).append("\n");
    sb.append("    cheatType: ").append(toIndentedString(cheatType)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    autoDetected: ").append(toIndentedString(autoDetected)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    evidence: ").append(toIndentedString(evidence)).append("\n");
    sb.append("    reviewerId: ").append(toIndentedString(reviewerId)).append("\n");
    sb.append("    reviewNotes: ").append(toIndentedString(reviewNotes)).append("\n");
    sb.append("    actionTaken: ").append(toIndentedString(actionTaken)).append("\n");
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

