package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * Переход статуса инцидента с указанием результатов и времени.
 */

@Schema(name = "IncidentStatusUpdate", description = "Переход статуса инцидента с указанием результатов и времени.")

public class IncidentStatusUpdate {

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACKNOWLEDGED("acknowledged"),
    
    INVESTIGATING("investigating"),
    
    MITIGATED("mitigated"),
    
    RESOLVED("resolved"),
    
    CLOSED("closed");

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

  private StatusEnum status;

  private @Nullable String resolutionSummary;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime resolvedAt;

  public IncidentStatusUpdate() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public IncidentStatusUpdate(StatusEnum status) {
    this.status = status;
  }

  public IncidentStatusUpdate status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public IncidentStatusUpdate resolutionSummary(@Nullable String resolutionSummary) {
    this.resolutionSummary = resolutionSummary;
    return this;
  }

  /**
   * Get resolutionSummary
   * @return resolutionSummary
   */
  
  @Schema(name = "resolution_summary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resolution_summary")
  public @Nullable String getResolutionSummary() {
    return resolutionSummary;
  }

  public void setResolutionSummary(@Nullable String resolutionSummary) {
    this.resolutionSummary = resolutionSummary;
  }

  public IncidentStatusUpdate resolvedAt(@Nullable OffsetDateTime resolvedAt) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IncidentStatusUpdate incidentStatusUpdate = (IncidentStatusUpdate) o;
    return Objects.equals(this.status, incidentStatusUpdate.status) &&
        Objects.equals(this.resolutionSummary, incidentStatusUpdate.resolutionSummary) &&
        Objects.equals(this.resolvedAt, incidentStatusUpdate.resolvedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, resolutionSummary, resolvedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IncidentStatusUpdate {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    resolutionSummary: ").append(toIndentedString(resolutionSummary)).append("\n");
    sb.append("    resolvedAt: ").append(toIndentedString(resolvedAt)).append("\n");
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

