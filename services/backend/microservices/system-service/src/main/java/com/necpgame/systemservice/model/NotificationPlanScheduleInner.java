package com.necpgame.systemservice.model;

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
 * NotificationPlanScheduleInner
 */

@JsonTypeName("NotificationPlan_schedule_inner")

public class NotificationPlanScheduleInner {

  private Integer offsetMinutes;

  private String channel;

  public NotificationPlanScheduleInner() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NotificationPlanScheduleInner(Integer offsetMinutes, String channel) {
    this.offsetMinutes = offsetMinutes;
    this.channel = channel;
  }

  public NotificationPlanScheduleInner offsetMinutes(Integer offsetMinutes) {
    this.offsetMinutes = offsetMinutes;
    return this;
  }

  /**
   * Get offsetMinutes
   * @return offsetMinutes
   */
  @NotNull 
  @Schema(name = "offsetMinutes", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("offsetMinutes")
  public Integer getOffsetMinutes() {
    return offsetMinutes;
  }

  public void setOffsetMinutes(Integer offsetMinutes) {
    this.offsetMinutes = offsetMinutes;
  }

  public NotificationPlanScheduleInner channel(String channel) {
    this.channel = channel;
    return this;
  }

  /**
   * Get channel
   * @return channel
   */
  @NotNull 
  @Schema(name = "channel", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channel")
  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationPlanScheduleInner notificationPlanScheduleInner = (NotificationPlanScheduleInner) o;
    return Objects.equals(this.offsetMinutes, notificationPlanScheduleInner.offsetMinutes) &&
        Objects.equals(this.channel, notificationPlanScheduleInner.channel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(offsetMinutes, channel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationPlanScheduleInner {\n");
    sb.append("    offsetMinutes: ").append(toIndentedString(offsetMinutes)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
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

