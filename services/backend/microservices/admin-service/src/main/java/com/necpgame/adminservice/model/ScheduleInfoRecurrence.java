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
 * ScheduleInfoRecurrence
 */

@JsonTypeName("ScheduleInfo_recurrence")

public class ScheduleInfoRecurrence {

  private @Nullable String pattern;

  private @Nullable Integer occurrences;

  public ScheduleInfoRecurrence pattern(@Nullable String pattern) {
    this.pattern = pattern;
    return this;
  }

  /**
   * Get pattern
   * @return pattern
   */
  
  @Schema(name = "pattern", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pattern")
  public @Nullable String getPattern() {
    return pattern;
  }

  public void setPattern(@Nullable String pattern) {
    this.pattern = pattern;
  }

  public ScheduleInfoRecurrence occurrences(@Nullable Integer occurrences) {
    this.occurrences = occurrences;
    return this;
  }

  /**
   * Get occurrences
   * @return occurrences
   */
  
  @Schema(name = "occurrences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("occurrences")
  public @Nullable Integer getOccurrences() {
    return occurrences;
  }

  public void setOccurrences(@Nullable Integer occurrences) {
    this.occurrences = occurrences;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScheduleInfoRecurrence scheduleInfoRecurrence = (ScheduleInfoRecurrence) o;
    return Objects.equals(this.pattern, scheduleInfoRecurrence.pattern) &&
        Objects.equals(this.occurrences, scheduleInfoRecurrence.occurrences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pattern, occurrences);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduleInfoRecurrence {\n");
    sb.append("    pattern: ").append(toIndentedString(pattern)).append("\n");
    sb.append("    occurrences: ").append(toIndentedString(occurrences)).append("\n");
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

