package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Violation
 */


public class Violation {

  private @Nullable UUID violationId;

  private @Nullable UUID playerId;

  private @Nullable String type;

  private @Nullable String severity;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime detectedAt;

  private JsonNullable<String> actionTaken = JsonNullable.<String>undefined();

  public Violation violationId(@Nullable UUID violationId) {
    this.violationId = violationId;
    return this;
  }

  /**
   * Get violationId
   * @return violationId
   */
  @Valid 
  @Schema(name = "violation_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("violation_id")
  public @Nullable UUID getViolationId() {
    return violationId;
  }

  public void setViolationId(@Nullable UUID violationId) {
    this.violationId = violationId;
  }

  public Violation playerId(@Nullable UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @Valid 
  @Schema(name = "player_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_id")
  public @Nullable UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable UUID playerId) {
    this.playerId = playerId;
  }

  public Violation type(@Nullable String type) {
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

  public Violation severity(@Nullable String severity) {
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

  public Violation detectedAt(@Nullable OffsetDateTime detectedAt) {
    this.detectedAt = detectedAt;
    return this;
  }

  /**
   * Get detectedAt
   * @return detectedAt
   */
  @Valid 
  @Schema(name = "detected_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("detected_at")
  public @Nullable OffsetDateTime getDetectedAt() {
    return detectedAt;
  }

  public void setDetectedAt(@Nullable OffsetDateTime detectedAt) {
    this.detectedAt = detectedAt;
  }

  public Violation actionTaken(String actionTaken) {
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
    Violation violation = (Violation) o;
    return Objects.equals(this.violationId, violation.violationId) &&
        Objects.equals(this.playerId, violation.playerId) &&
        Objects.equals(this.type, violation.type) &&
        Objects.equals(this.severity, violation.severity) &&
        Objects.equals(this.detectedAt, violation.detectedAt) &&
        equalsNullable(this.actionTaken, violation.actionTaken);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(violationId, playerId, type, severity, detectedAt, hashCodeNullable(actionTaken));
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
    sb.append("class Violation {\n");
    sb.append("    violationId: ").append(toIndentedString(violationId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    detectedAt: ").append(toIndentedString(detectedAt)).append("\n");
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

