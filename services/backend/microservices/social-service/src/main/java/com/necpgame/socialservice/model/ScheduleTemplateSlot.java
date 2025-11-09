package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ScheduleTemplateSlot
 */


public class ScheduleTemplateSlot {

  private String timeRange;

  private String activity;

  @Valid
  private List<String> locationHints = new ArrayList<>();

  private @Nullable String notes;

  public ScheduleTemplateSlot() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ScheduleTemplateSlot(String timeRange, String activity) {
    this.timeRange = timeRange;
    this.activity = activity;
  }

  public ScheduleTemplateSlot timeRange(String timeRange) {
    this.timeRange = timeRange;
    return this;
  }

  /**
   * Get timeRange
   * @return timeRange
   */
  @NotNull 
  @Schema(name = "timeRange", example = "06:00-10:00", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timeRange")
  public String getTimeRange() {
    return timeRange;
  }

  public void setTimeRange(String timeRange) {
    this.timeRange = timeRange;
  }

  public ScheduleTemplateSlot activity(String activity) {
    this.activity = activity;
    return this;
  }

  /**
   * Get activity
   * @return activity
   */
  @NotNull 
  @Schema(name = "activity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("activity")
  public String getActivity() {
    return activity;
  }

  public void setActivity(String activity) {
    this.activity = activity;
  }

  public ScheduleTemplateSlot locationHints(List<String> locationHints) {
    this.locationHints = locationHints;
    return this;
  }

  public ScheduleTemplateSlot addLocationHintsItem(String locationHintsItem) {
    if (this.locationHints == null) {
      this.locationHints = new ArrayList<>();
    }
    this.locationHints.add(locationHintsItem);
    return this;
  }

  /**
   * Get locationHints
   * @return locationHints
   */
  
  @Schema(name = "locationHints", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locationHints")
  public List<String> getLocationHints() {
    return locationHints;
  }

  public void setLocationHints(List<String> locationHints) {
    this.locationHints = locationHints;
  }

  public ScheduleTemplateSlot notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScheduleTemplateSlot scheduleTemplateSlot = (ScheduleTemplateSlot) o;
    return Objects.equals(this.timeRange, scheduleTemplateSlot.timeRange) &&
        Objects.equals(this.activity, scheduleTemplateSlot.activity) &&
        Objects.equals(this.locationHints, scheduleTemplateSlot.locationHints) &&
        Objects.equals(this.notes, scheduleTemplateSlot.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeRange, activity, locationHints, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduleTemplateSlot {\n");
    sb.append("    timeRange: ").append(toIndentedString(timeRange)).append("\n");
    sb.append("    activity: ").append(toIndentedString(activity)).append("\n");
    sb.append("    locationHints: ").append(toIndentedString(locationHints)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
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

