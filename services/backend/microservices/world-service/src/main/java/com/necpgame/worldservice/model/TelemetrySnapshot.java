package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * TelemetrySnapshot
 */


public class TelemetrySnapshot {

  private @Nullable Float participationRate;

  private @Nullable Float retentionRate;

  private @Nullable Float completionRate;

  private @Nullable Float eventImpactScore;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime issuedAt;

  public TelemetrySnapshot participationRate(@Nullable Float participationRate) {
    this.participationRate = participationRate;
    return this;
  }

  /**
   * Get participationRate
   * minimum: 0
   * maximum: 1
   * @return participationRate
   */
  @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "participationRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participationRate")
  public @Nullable Float getParticipationRate() {
    return participationRate;
  }

  public void setParticipationRate(@Nullable Float participationRate) {
    this.participationRate = participationRate;
  }

  public TelemetrySnapshot retentionRate(@Nullable Float retentionRate) {
    this.retentionRate = retentionRate;
    return this;
  }

  /**
   * Get retentionRate
   * minimum: 0
   * maximum: 1
   * @return retentionRate
   */
  @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "retentionRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("retentionRate")
  public @Nullable Float getRetentionRate() {
    return retentionRate;
  }

  public void setRetentionRate(@Nullable Float retentionRate) {
    this.retentionRate = retentionRate;
  }

  public TelemetrySnapshot completionRate(@Nullable Float completionRate) {
    this.completionRate = completionRate;
    return this;
  }

  /**
   * Get completionRate
   * minimum: 0
   * maximum: 1
   * @return completionRate
   */
  @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "completionRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completionRate")
  public @Nullable Float getCompletionRate() {
    return completionRate;
  }

  public void setCompletionRate(@Nullable Float completionRate) {
    this.completionRate = completionRate;
  }

  public TelemetrySnapshot eventImpactScore(@Nullable Float eventImpactScore) {
    this.eventImpactScore = eventImpactScore;
    return this;
  }

  /**
   * Get eventImpactScore
   * @return eventImpactScore
   */
  
  @Schema(name = "eventImpactScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eventImpactScore")
  public @Nullable Float getEventImpactScore() {
    return eventImpactScore;
  }

  public void setEventImpactScore(@Nullable Float eventImpactScore) {
    this.eventImpactScore = eventImpactScore;
  }

  public TelemetrySnapshot issuedAt(@Nullable OffsetDateTime issuedAt) {
    this.issuedAt = issuedAt;
    return this;
  }

  /**
   * Get issuedAt
   * @return issuedAt
   */
  @Valid 
  @Schema(name = "issuedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("issuedAt")
  public @Nullable OffsetDateTime getIssuedAt() {
    return issuedAt;
  }

  public void setIssuedAt(@Nullable OffsetDateTime issuedAt) {
    this.issuedAt = issuedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TelemetrySnapshot telemetrySnapshot = (TelemetrySnapshot) o;
    return Objects.equals(this.participationRate, telemetrySnapshot.participationRate) &&
        Objects.equals(this.retentionRate, telemetrySnapshot.retentionRate) &&
        Objects.equals(this.completionRate, telemetrySnapshot.completionRate) &&
        Objects.equals(this.eventImpactScore, telemetrySnapshot.eventImpactScore) &&
        Objects.equals(this.issuedAt, telemetrySnapshot.issuedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(participationRate, retentionRate, completionRate, eventImpactScore, issuedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TelemetrySnapshot {\n");
    sb.append("    participationRate: ").append(toIndentedString(participationRate)).append("\n");
    sb.append("    retentionRate: ").append(toIndentedString(retentionRate)).append("\n");
    sb.append("    completionRate: ").append(toIndentedString(completionRate)).append("\n");
    sb.append("    eventImpactScore: ").append(toIndentedString(eventImpactScore)).append("\n");
    sb.append("    issuedAt: ").append(toIndentedString(issuedAt)).append("\n");
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

