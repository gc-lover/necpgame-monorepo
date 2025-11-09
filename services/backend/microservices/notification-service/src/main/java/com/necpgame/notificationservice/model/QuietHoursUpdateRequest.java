package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * QuietHoursUpdateRequest
 */


public class QuietHoursUpdateRequest {

  private @Nullable String start;

  private @Nullable String end;

  private @Nullable String timezone;

  private @Nullable Boolean suppressCritical;

  public QuietHoursUpdateRequest start(@Nullable String start) {
    this.start = start;
    return this;
  }

  /**
   * Get start
   * @return start
   */
  @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$") 
  @Schema(name = "start", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("start")
  public @Nullable String getStart() {
    return start;
  }

  public void setStart(@Nullable String start) {
    this.start = start;
  }

  public QuietHoursUpdateRequest end(@Nullable String end) {
    this.end = end;
    return this;
  }

  /**
   * Get end
   * @return end
   */
  @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$") 
  @Schema(name = "end", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("end")
  public @Nullable String getEnd() {
    return end;
  }

  public void setEnd(@Nullable String end) {
    this.end = end;
  }

  public QuietHoursUpdateRequest timezone(@Nullable String timezone) {
    this.timezone = timezone;
    return this;
  }

  /**
   * Get timezone
   * @return timezone
   */
  
  @Schema(name = "timezone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timezone")
  public @Nullable String getTimezone() {
    return timezone;
  }

  public void setTimezone(@Nullable String timezone) {
    this.timezone = timezone;
  }

  public QuietHoursUpdateRequest suppressCritical(@Nullable Boolean suppressCritical) {
    this.suppressCritical = suppressCritical;
    return this;
  }

  /**
   * Get suppressCritical
   * @return suppressCritical
   */
  
  @Schema(name = "suppressCritical", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("suppressCritical")
  public @Nullable Boolean getSuppressCritical() {
    return suppressCritical;
  }

  public void setSuppressCritical(@Nullable Boolean suppressCritical) {
    this.suppressCritical = suppressCritical;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuietHoursUpdateRequest quietHoursUpdateRequest = (QuietHoursUpdateRequest) o;
    return Objects.equals(this.start, quietHoursUpdateRequest.start) &&
        Objects.equals(this.end, quietHoursUpdateRequest.end) &&
        Objects.equals(this.timezone, quietHoursUpdateRequest.timezone) &&
        Objects.equals(this.suppressCritical, quietHoursUpdateRequest.suppressCritical);
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, end, timezone, suppressCritical);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuietHoursUpdateRequest {\n");
    sb.append("    start: ").append(toIndentedString(start)).append("\n");
    sb.append("    end: ").append(toIndentedString(end)).append("\n");
    sb.append("    timezone: ").append(toIndentedString(timezone)).append("\n");
    sb.append("    suppressCritical: ").append(toIndentedString(suppressCritical)).append("\n");
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

