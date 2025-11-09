package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TriggerSecurityScan202Response
 */

@JsonTypeName("triggerSecurityScan_202_response")

public class TriggerSecurityScan202Response {

  private @Nullable String scanId;

  private @Nullable Integer estimatedTimeMinutes;

  public TriggerSecurityScan202Response scanId(@Nullable String scanId) {
    this.scanId = scanId;
    return this;
  }

  /**
   * Get scanId
   * @return scanId
   */
  
  @Schema(name = "scan_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scan_id")
  public @Nullable String getScanId() {
    return scanId;
  }

  public void setScanId(@Nullable String scanId) {
    this.scanId = scanId;
  }

  public TriggerSecurityScan202Response estimatedTimeMinutes(@Nullable Integer estimatedTimeMinutes) {
    this.estimatedTimeMinutes = estimatedTimeMinutes;
    return this;
  }

  /**
   * Get estimatedTimeMinutes
   * @return estimatedTimeMinutes
   */
  
  @Schema(name = "estimated_time_minutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_time_minutes")
  public @Nullable Integer getEstimatedTimeMinutes() {
    return estimatedTimeMinutes;
  }

  public void setEstimatedTimeMinutes(@Nullable Integer estimatedTimeMinutes) {
    this.estimatedTimeMinutes = estimatedTimeMinutes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TriggerSecurityScan202Response triggerSecurityScan202Response = (TriggerSecurityScan202Response) o;
    return Objects.equals(this.scanId, triggerSecurityScan202Response.scanId) &&
        Objects.equals(this.estimatedTimeMinutes, triggerSecurityScan202Response.estimatedTimeMinutes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scanId, estimatedTimeMinutes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TriggerSecurityScan202Response {\n");
    sb.append("    scanId: ").append(toIndentedString(scanId)).append("\n");
    sb.append("    estimatedTimeMinutes: ").append(toIndentedString(estimatedTimeMinutes)).append("\n");
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

