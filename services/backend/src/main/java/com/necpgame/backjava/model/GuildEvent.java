package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.GuildAttendance;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * GuildEvent
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GuildEvent {

  private @Nullable String eventId;

  private @Nullable String title;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    RAID("raid"),
    
    MEETING("meeting"),
    
    TRAINING("training"),
    
    SOCIAL("social");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime schedule;

  private @Nullable String location;

  private @Nullable Integer maxParticipants;

  @Valid
  private List<@Valid GuildAttendance> attendance = new ArrayList<>();

  public GuildEvent eventId(@Nullable String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  
  @Schema(name = "eventId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eventId")
  public @Nullable String getEventId() {
    return eventId;
  }

  public void setEventId(@Nullable String eventId) {
    this.eventId = eventId;
  }

  public GuildEvent title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public GuildEvent type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public GuildEvent schedule(@Nullable OffsetDateTime schedule) {
    this.schedule = schedule;
    return this;
  }

  /**
   * Get schedule
   * @return schedule
   */
  @Valid 
  @Schema(name = "schedule", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("schedule")
  public @Nullable OffsetDateTime getSchedule() {
    return schedule;
  }

  public void setSchedule(@Nullable OffsetDateTime schedule) {
    this.schedule = schedule;
  }

  public GuildEvent location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public GuildEvent maxParticipants(@Nullable Integer maxParticipants) {
    this.maxParticipants = maxParticipants;
    return this;
  }

  /**
   * Get maxParticipants
   * @return maxParticipants
   */
  
  @Schema(name = "maxParticipants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxParticipants")
  public @Nullable Integer getMaxParticipants() {
    return maxParticipants;
  }

  public void setMaxParticipants(@Nullable Integer maxParticipants) {
    this.maxParticipants = maxParticipants;
  }

  public GuildEvent attendance(List<@Valid GuildAttendance> attendance) {
    this.attendance = attendance;
    return this;
  }

  public GuildEvent addAttendanceItem(GuildAttendance attendanceItem) {
    if (this.attendance == null) {
      this.attendance = new ArrayList<>();
    }
    this.attendance.add(attendanceItem);
    return this;
  }

  /**
   * Get attendance
   * @return attendance
   */
  @Valid 
  @Schema(name = "attendance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attendance")
  public List<@Valid GuildAttendance> getAttendance() {
    return attendance;
  }

  public void setAttendance(List<@Valid GuildAttendance> attendance) {
    this.attendance = attendance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildEvent guildEvent = (GuildEvent) o;
    return Objects.equals(this.eventId, guildEvent.eventId) &&
        Objects.equals(this.title, guildEvent.title) &&
        Objects.equals(this.type, guildEvent.type) &&
        Objects.equals(this.schedule, guildEvent.schedule) &&
        Objects.equals(this.location, guildEvent.location) &&
        Objects.equals(this.maxParticipants, guildEvent.maxParticipants) &&
        Objects.equals(this.attendance, guildEvent.attendance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, title, type, schedule, location, maxParticipants, attendance);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildEvent {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    schedule: ").append(toIndentedString(schedule)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    maxParticipants: ").append(toIndentedString(maxParticipants)).append("\n");
    sb.append("    attendance: ").append(toIndentedString(attendance)).append("\n");
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

