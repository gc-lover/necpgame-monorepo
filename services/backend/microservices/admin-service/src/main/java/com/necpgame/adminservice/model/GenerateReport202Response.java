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
 * GenerateReport202Response
 */

@JsonTypeName("generateReport_202_response")

public class GenerateReport202Response {

  private @Nullable String reportId;

  private @Nullable Integer estimatedTimeMinutes;

  public GenerateReport202Response reportId(@Nullable String reportId) {
    this.reportId = reportId;
    return this;
  }

  /**
   * Get reportId
   * @return reportId
   */
  
  @Schema(name = "report_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("report_id")
  public @Nullable String getReportId() {
    return reportId;
  }

  public void setReportId(@Nullable String reportId) {
    this.reportId = reportId;
  }

  public GenerateReport202Response estimatedTimeMinutes(@Nullable Integer estimatedTimeMinutes) {
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
    GenerateReport202Response generateReport202Response = (GenerateReport202Response) o;
    return Objects.equals(this.reportId, generateReport202Response.reportId) &&
        Objects.equals(this.estimatedTimeMinutes, generateReport202Response.estimatedTimeMinutes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reportId, estimatedTimeMinutes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateReport202Response {\n");
    sb.append("    reportId: ").append(toIndentedString(reportId)).append("\n");
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

