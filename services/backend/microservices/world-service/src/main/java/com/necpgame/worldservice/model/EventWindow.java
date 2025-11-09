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
 * EventWindow
 */


public class EventWindow {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startTimeUtc;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime endTimeUtc;

  private @Nullable Integer durationMinutes;

  public EventWindow() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EventWindow(OffsetDateTime startTimeUtc, OffsetDateTime endTimeUtc) {
    this.startTimeUtc = startTimeUtc;
    this.endTimeUtc = endTimeUtc;
  }

  public EventWindow startTimeUtc(OffsetDateTime startTimeUtc) {
    this.startTimeUtc = startTimeUtc;
    return this;
  }

  /**
   * Get startTimeUtc
   * @return startTimeUtc
   */
  @NotNull @Valid 
  @Schema(name = "startTimeUtc", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("startTimeUtc")
  public OffsetDateTime getStartTimeUtc() {
    return startTimeUtc;
  }

  public void setStartTimeUtc(OffsetDateTime startTimeUtc) {
    this.startTimeUtc = startTimeUtc;
  }

  public EventWindow endTimeUtc(OffsetDateTime endTimeUtc) {
    this.endTimeUtc = endTimeUtc;
    return this;
  }

  /**
   * Get endTimeUtc
   * @return endTimeUtc
   */
  @NotNull @Valid 
  @Schema(name = "endTimeUtc", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("endTimeUtc")
  public OffsetDateTime getEndTimeUtc() {
    return endTimeUtc;
  }

  public void setEndTimeUtc(OffsetDateTime endTimeUtc) {
    this.endTimeUtc = endTimeUtc;
  }

  public EventWindow durationMinutes(@Nullable Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
    return this;
  }

  /**
   * Get durationMinutes
   * minimum: 5
   * @return durationMinutes
   */
  @Min(value = 5) 
  @Schema(name = "durationMinutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durationMinutes")
  public @Nullable Integer getDurationMinutes() {
    return durationMinutes;
  }

  public void setDurationMinutes(@Nullable Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventWindow eventWindow = (EventWindow) o;
    return Objects.equals(this.startTimeUtc, eventWindow.startTimeUtc) &&
        Objects.equals(this.endTimeUtc, eventWindow.endTimeUtc) &&
        Objects.equals(this.durationMinutes, eventWindow.durationMinutes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startTimeUtc, endTimeUtc, durationMinutes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventWindow {\n");
    sb.append("    startTimeUtc: ").append(toIndentedString(startTimeUtc)).append("\n");
    sb.append("    endTimeUtc: ").append(toIndentedString(endTimeUtc)).append("\n");
    sb.append("    durationMinutes: ").append(toIndentedString(durationMinutes)).append("\n");
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

