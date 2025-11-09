package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.ImpactMagnitude;
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
 * ImpactHistoryEntry
 */


public class ImpactHistoryEntry {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private String status;

  private @Nullable ImpactMagnitude magnitude;

  public ImpactHistoryEntry() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ImpactHistoryEntry(OffsetDateTime timestamp, String status) {
    this.timestamp = timestamp;
    this.status = status;
  }

  public ImpactHistoryEntry timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @NotNull @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public ImpactHistoryEntry status(String status) {
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
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public ImpactHistoryEntry magnitude(@Nullable ImpactMagnitude magnitude) {
    this.magnitude = magnitude;
    return this;
  }

  /**
   * Get magnitude
   * @return magnitude
   */
  @Valid 
  @Schema(name = "magnitude", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("magnitude")
  public @Nullable ImpactMagnitude getMagnitude() {
    return magnitude;
  }

  public void setMagnitude(@Nullable ImpactMagnitude magnitude) {
    this.magnitude = magnitude;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImpactHistoryEntry impactHistoryEntry = (ImpactHistoryEntry) o;
    return Objects.equals(this.timestamp, impactHistoryEntry.timestamp) &&
        Objects.equals(this.status, impactHistoryEntry.status) &&
        Objects.equals(this.magnitude, impactHistoryEntry.magnitude);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, status, magnitude);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImpactHistoryEntry {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    magnitude: ").append(toIndentedString(magnitude)).append("\n");
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

