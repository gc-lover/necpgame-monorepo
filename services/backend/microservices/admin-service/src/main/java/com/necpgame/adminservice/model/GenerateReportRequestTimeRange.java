package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * GenerateReportRequestTimeRange
 */

@JsonTypeName("generateReport_request_time_range")

public class GenerateReportRequestTimeRange {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime from;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime to;

  public GenerateReportRequestTimeRange from(@Nullable OffsetDateTime from) {
    this.from = from;
    return this;
  }

  /**
   * Get from
   * @return from
   */
  @Valid 
  @Schema(name = "from", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("from")
  public @Nullable OffsetDateTime getFrom() {
    return from;
  }

  public void setFrom(@Nullable OffsetDateTime from) {
    this.from = from;
  }

  public GenerateReportRequestTimeRange to(@Nullable OffsetDateTime to) {
    this.to = to;
    return this;
  }

  /**
   * Get to
   * @return to
   */
  @Valid 
  @Schema(name = "to", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("to")
  public @Nullable OffsetDateTime getTo() {
    return to;
  }

  public void setTo(@Nullable OffsetDateTime to) {
    this.to = to;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateReportRequestTimeRange generateReportRequestTimeRange = (GenerateReportRequestTimeRange) o;
    return Objects.equals(this.from, generateReportRequestTimeRange.from) &&
        Objects.equals(this.to, generateReportRequestTimeRange.to);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateReportRequestTimeRange {\n");
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
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

