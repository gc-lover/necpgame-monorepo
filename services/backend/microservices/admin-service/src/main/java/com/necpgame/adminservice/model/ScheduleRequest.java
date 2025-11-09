package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.ScheduleRequestRecurrence;
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
 * ScheduleRequest
 */


public class ScheduleRequest {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endAt;

  private @Nullable String timezone;

  private @Nullable ScheduleRequestRecurrence recurrence;

  /**
   * Gets or Sets fallbackChannels
   */
  public enum FallbackChannelsEnum {
    EMAIL("email"),
    
    PUSH("push"),
    
    WEB_PORTAL("web_portal");

    private final String value;

    FallbackChannelsEnum(String value) {
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
    public static FallbackChannelsEnum fromValue(String value) {
      for (FallbackChannelsEnum b : FallbackChannelsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<FallbackChannelsEnum> fallbackChannels = new ArrayList<>();

  public ScheduleRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ScheduleRequest(OffsetDateTime startAt) {
    this.startAt = startAt;
  }

  public ScheduleRequest startAt(OffsetDateTime startAt) {
    this.startAt = startAt;
    return this;
  }

  /**
   * Get startAt
   * @return startAt
   */
  @NotNull @Valid 
  @Schema(name = "startAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("startAt")
  public OffsetDateTime getStartAt() {
    return startAt;
  }

  public void setStartAt(OffsetDateTime startAt) {
    this.startAt = startAt;
  }

  public ScheduleRequest endAt(@Nullable OffsetDateTime endAt) {
    this.endAt = endAt;
    return this;
  }

  /**
   * Get endAt
   * @return endAt
   */
  @Valid 
  @Schema(name = "endAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endAt")
  public @Nullable OffsetDateTime getEndAt() {
    return endAt;
  }

  public void setEndAt(@Nullable OffsetDateTime endAt) {
    this.endAt = endAt;
  }

  public ScheduleRequest timezone(@Nullable String timezone) {
    this.timezone = timezone;
    return this;
  }

  /**
   * Get timezone
   * @return timezone
   */
  
  @Schema(name = "timezone", example = "Europe/Berlin", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timezone")
  public @Nullable String getTimezone() {
    return timezone;
  }

  public void setTimezone(@Nullable String timezone) {
    this.timezone = timezone;
  }

  public ScheduleRequest recurrence(@Nullable ScheduleRequestRecurrence recurrence) {
    this.recurrence = recurrence;
    return this;
  }

  /**
   * Get recurrence
   * @return recurrence
   */
  @Valid 
  @Schema(name = "recurrence", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recurrence")
  public @Nullable ScheduleRequestRecurrence getRecurrence() {
    return recurrence;
  }

  public void setRecurrence(@Nullable ScheduleRequestRecurrence recurrence) {
    this.recurrence = recurrence;
  }

  public ScheduleRequest fallbackChannels(List<FallbackChannelsEnum> fallbackChannels) {
    this.fallbackChannels = fallbackChannels;
    return this;
  }

  public ScheduleRequest addFallbackChannelsItem(FallbackChannelsEnum fallbackChannelsItem) {
    if (this.fallbackChannels == null) {
      this.fallbackChannels = new ArrayList<>();
    }
    this.fallbackChannels.add(fallbackChannelsItem);
    return this;
  }

  /**
   * Get fallbackChannels
   * @return fallbackChannels
   */
  
  @Schema(name = "fallbackChannels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fallbackChannels")
  public List<FallbackChannelsEnum> getFallbackChannels() {
    return fallbackChannels;
  }

  public void setFallbackChannels(List<FallbackChannelsEnum> fallbackChannels) {
    this.fallbackChannels = fallbackChannels;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScheduleRequest scheduleRequest = (ScheduleRequest) o;
    return Objects.equals(this.startAt, scheduleRequest.startAt) &&
        Objects.equals(this.endAt, scheduleRequest.endAt) &&
        Objects.equals(this.timezone, scheduleRequest.timezone) &&
        Objects.equals(this.recurrence, scheduleRequest.recurrence) &&
        Objects.equals(this.fallbackChannels, scheduleRequest.fallbackChannels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startAt, endAt, timezone, recurrence, fallbackChannels);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduleRequest {\n");
    sb.append("    startAt: ").append(toIndentedString(startAt)).append("\n");
    sb.append("    endAt: ").append(toIndentedString(endAt)).append("\n");
    sb.append("    timezone: ").append(toIndentedString(timezone)).append("\n");
    sb.append("    recurrence: ").append(toIndentedString(recurrence)).append("\n");
    sb.append("    fallbackChannels: ").append(toIndentedString(fallbackChannels)).append("\n");
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

