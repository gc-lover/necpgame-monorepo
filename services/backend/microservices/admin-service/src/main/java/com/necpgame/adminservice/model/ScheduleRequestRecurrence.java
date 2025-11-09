package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ScheduleRequestRecurrence
 */

@JsonTypeName("ScheduleRequest_recurrence")

public class ScheduleRequestRecurrence {

  /**
   * Gets or Sets pattern
   */
  public enum PatternEnum {
    NONE("none"),
    
    DAILY("daily"),
    
    WEEKLY("weekly");

    private final String value;

    PatternEnum(String value) {
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
    public static PatternEnum fromValue(String value) {
      for (PatternEnum b : PatternEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PatternEnum pattern;

  private @Nullable Integer occurrences;

  public ScheduleRequestRecurrence pattern(@Nullable PatternEnum pattern) {
    this.pattern = pattern;
    return this;
  }

  /**
   * Get pattern
   * @return pattern
   */
  
  @Schema(name = "pattern", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pattern")
  public @Nullable PatternEnum getPattern() {
    return pattern;
  }

  public void setPattern(@Nullable PatternEnum pattern) {
    this.pattern = pattern;
  }

  public ScheduleRequestRecurrence occurrences(@Nullable Integer occurrences) {
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
    ScheduleRequestRecurrence scheduleRequestRecurrence = (ScheduleRequestRecurrence) o;
    return Objects.equals(this.pattern, scheduleRequestRecurrence.pattern) &&
        Objects.equals(this.occurrences, scheduleRequestRecurrence.occurrences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pattern, occurrences);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduleRequestRecurrence {\n");
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

