package com.necpgame.gameplayservice.model;

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
 * CosmeticSettingsRotationSchedule
 */

@JsonTypeName("CosmeticSettings_rotationSchedule")

public class CosmeticSettingsRotationSchedule {

  private @Nullable String dailyUpdateAt;

  /**
   * Gets or Sets weeklyUpdateDay
   */
  public enum WeeklyUpdateDayEnum {
    MONDAY("monday"),
    
    TUESDAY("tuesday"),
    
    WEDNESDAY("wednesday"),
    
    THURSDAY("thursday"),
    
    FRIDAY("friday"),
    
    SATURDAY("saturday"),
    
    SUNDAY("sunday");

    private final String value;

    WeeklyUpdateDayEnum(String value) {
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
    public static WeeklyUpdateDayEnum fromValue(String value) {
      for (WeeklyUpdateDayEnum b : WeeklyUpdateDayEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable WeeklyUpdateDayEnum weeklyUpdateDay;

  public CosmeticSettingsRotationSchedule dailyUpdateAt(@Nullable String dailyUpdateAt) {
    this.dailyUpdateAt = dailyUpdateAt;
    return this;
  }

  /**
   * Get dailyUpdateAt
   * @return dailyUpdateAt
   */
  @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$") 
  @Schema(name = "dailyUpdateAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dailyUpdateAt")
  public @Nullable String getDailyUpdateAt() {
    return dailyUpdateAt;
  }

  public void setDailyUpdateAt(@Nullable String dailyUpdateAt) {
    this.dailyUpdateAt = dailyUpdateAt;
  }

  public CosmeticSettingsRotationSchedule weeklyUpdateDay(@Nullable WeeklyUpdateDayEnum weeklyUpdateDay) {
    this.weeklyUpdateDay = weeklyUpdateDay;
    return this;
  }

  /**
   * Get weeklyUpdateDay
   * @return weeklyUpdateDay
   */
  
  @Schema(name = "weeklyUpdateDay", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weeklyUpdateDay")
  public @Nullable WeeklyUpdateDayEnum getWeeklyUpdateDay() {
    return weeklyUpdateDay;
  }

  public void setWeeklyUpdateDay(@Nullable WeeklyUpdateDayEnum weeklyUpdateDay) {
    this.weeklyUpdateDay = weeklyUpdateDay;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CosmeticSettingsRotationSchedule cosmeticSettingsRotationSchedule = (CosmeticSettingsRotationSchedule) o;
    return Objects.equals(this.dailyUpdateAt, cosmeticSettingsRotationSchedule.dailyUpdateAt) &&
        Objects.equals(this.weeklyUpdateDay, cosmeticSettingsRotationSchedule.weeklyUpdateDay);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dailyUpdateAt, weeklyUpdateDay);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CosmeticSettingsRotationSchedule {\n");
    sb.append("    dailyUpdateAt: ").append(toIndentedString(dailyUpdateAt)).append("\n");
    sb.append("    weeklyUpdateDay: ").append(toIndentedString(weeklyUpdateDay)).append("\n");
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

