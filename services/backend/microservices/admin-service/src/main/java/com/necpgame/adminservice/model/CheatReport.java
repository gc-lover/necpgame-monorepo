package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * CheatReport
 */


public class CheatReport {

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

  public CheatReport reportId(@Nullable UUID reportId) {
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

  public CheatReport reportedPlayerId(@Nullable UUID reportedPlayerId) {
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

  public CheatReport cheatType(@Nullable String cheatType) {
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

  public CheatReport severity(@Nullable String severity) {
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

  public CheatReport status(@Nullable StatusEnum status) {
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

  public CheatReport autoDetected(@Nullable Boolean autoDetected) {
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

  public CheatReport createdAt(@Nullable OffsetDateTime createdAt) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CheatReport cheatReport = (CheatReport) o;
    return Objects.equals(this.reportId, cheatReport.reportId) &&
        Objects.equals(this.reportedPlayerId, cheatReport.reportedPlayerId) &&
        Objects.equals(this.cheatType, cheatReport.cheatType) &&
        Objects.equals(this.severity, cheatReport.severity) &&
        Objects.equals(this.status, cheatReport.status) &&
        Objects.equals(this.autoDetected, cheatReport.autoDetected) &&
        Objects.equals(this.createdAt, cheatReport.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reportId, reportedPlayerId, cheatType, severity, status, autoDetected, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CheatReport {\n");
    sb.append("    reportId: ").append(toIndentedString(reportId)).append("\n");
    sb.append("    reportedPlayerId: ").append(toIndentedString(reportedPlayerId)).append("\n");
    sb.append("    cheatType: ").append(toIndentedString(cheatType)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    autoDetected: ").append(toIndentedString(autoDetected)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

