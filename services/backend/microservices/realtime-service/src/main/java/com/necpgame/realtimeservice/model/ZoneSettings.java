package com.necpgame.realtimeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * ZoneSettings
 */


public class ZoneSettings {

  private @Nullable Boolean isPvpEnabled;

  private @Nullable Boolean isSafeZone;

  private @Nullable String weatherProfile;

  /**
   * Gets or Sets timeOfDay
   */
  public enum TimeOfDayEnum {
    DAY("day"),
    
    NIGHT("night"),
    
    DUSK("dusk"),
    
    DAWN("dawn");

    private final String value;

    TimeOfDayEnum(String value) {
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
    public static TimeOfDayEnum fromValue(String value) {
      for (TimeOfDayEnum b : TimeOfDayEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TimeOfDayEnum timeOfDay;

  public ZoneSettings isPvpEnabled(@Nullable Boolean isPvpEnabled) {
    this.isPvpEnabled = isPvpEnabled;
    return this;
  }

  /**
   * Get isPvpEnabled
   * @return isPvpEnabled
   */
  
  @Schema(name = "isPvpEnabled", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isPvpEnabled")
  public @Nullable Boolean getIsPvpEnabled() {
    return isPvpEnabled;
  }

  public void setIsPvpEnabled(@Nullable Boolean isPvpEnabled) {
    this.isPvpEnabled = isPvpEnabled;
  }

  public ZoneSettings isSafeZone(@Nullable Boolean isSafeZone) {
    this.isSafeZone = isSafeZone;
    return this;
  }

  /**
   * Get isSafeZone
   * @return isSafeZone
   */
  
  @Schema(name = "isSafeZone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isSafeZone")
  public @Nullable Boolean getIsSafeZone() {
    return isSafeZone;
  }

  public void setIsSafeZone(@Nullable Boolean isSafeZone) {
    this.isSafeZone = isSafeZone;
  }

  public ZoneSettings weatherProfile(@Nullable String weatherProfile) {
    this.weatherProfile = weatherProfile;
    return this;
  }

  /**
   * Get weatherProfile
   * @return weatherProfile
   */
  
  @Schema(name = "weatherProfile", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weatherProfile")
  public @Nullable String getWeatherProfile() {
    return weatherProfile;
  }

  public void setWeatherProfile(@Nullable String weatherProfile) {
    this.weatherProfile = weatherProfile;
  }

  public ZoneSettings timeOfDay(@Nullable TimeOfDayEnum timeOfDay) {
    this.timeOfDay = timeOfDay;
    return this;
  }

  /**
   * Get timeOfDay
   * @return timeOfDay
   */
  
  @Schema(name = "timeOfDay", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeOfDay")
  public @Nullable TimeOfDayEnum getTimeOfDay() {
    return timeOfDay;
  }

  public void setTimeOfDay(@Nullable TimeOfDayEnum timeOfDay) {
    this.timeOfDay = timeOfDay;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ZoneSettings zoneSettings = (ZoneSettings) o;
    return Objects.equals(this.isPvpEnabled, zoneSettings.isPvpEnabled) &&
        Objects.equals(this.isSafeZone, zoneSettings.isSafeZone) &&
        Objects.equals(this.weatherProfile, zoneSettings.weatherProfile) &&
        Objects.equals(this.timeOfDay, zoneSettings.timeOfDay);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isPvpEnabled, isSafeZone, weatherProfile, timeOfDay);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ZoneSettings {\n");
    sb.append("    isPvpEnabled: ").append(toIndentedString(isPvpEnabled)).append("\n");
    sb.append("    isSafeZone: ").append(toIndentedString(isSafeZone)).append("\n");
    sb.append("    weatherProfile: ").append(toIndentedString(weatherProfile)).append("\n");
    sb.append("    timeOfDay: ").append(toIndentedString(timeOfDay)).append("\n");
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

