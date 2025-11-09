package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * RandomEventDetailedAllOfTimeRestrictions
 */

@JsonTypeName("RandomEventDetailed_allOf_time_restrictions")

public class RandomEventDetailedAllOfTimeRestrictions {

  /**
   * Gets or Sets timeOfDay
   */
  public enum TimeOfDayEnum {
    MORNING("MORNING"),
    
    DAY("DAY"),
    
    EVENING("EVENING"),
    
    NIGHT("NIGHT");

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

  @Valid
  private List<TimeOfDayEnum> timeOfDay = new ArrayList<>();

  @Valid
  private List<String> weather = new ArrayList<>();

  public RandomEventDetailedAllOfTimeRestrictions timeOfDay(List<TimeOfDayEnum> timeOfDay) {
    this.timeOfDay = timeOfDay;
    return this;
  }

  public RandomEventDetailedAllOfTimeRestrictions addTimeOfDayItem(TimeOfDayEnum timeOfDayItem) {
    if (this.timeOfDay == null) {
      this.timeOfDay = new ArrayList<>();
    }
    this.timeOfDay.add(timeOfDayItem);
    return this;
  }

  /**
   * Get timeOfDay
   * @return timeOfDay
   */
  
  @Schema(name = "time_of_day", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_of_day")
  public List<TimeOfDayEnum> getTimeOfDay() {
    return timeOfDay;
  }

  public void setTimeOfDay(List<TimeOfDayEnum> timeOfDay) {
    this.timeOfDay = timeOfDay;
  }

  public RandomEventDetailedAllOfTimeRestrictions weather(List<String> weather) {
    this.weather = weather;
    return this;
  }

  public RandomEventDetailedAllOfTimeRestrictions addWeatherItem(String weatherItem) {
    if (this.weather == null) {
      this.weather = new ArrayList<>();
    }
    this.weather.add(weatherItem);
    return this;
  }

  /**
   * Get weather
   * @return weather
   */
  
  @Schema(name = "weather", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weather")
  public List<String> getWeather() {
    return weather;
  }

  public void setWeather(List<String> weather) {
    this.weather = weather;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RandomEventDetailedAllOfTimeRestrictions randomEventDetailedAllOfTimeRestrictions = (RandomEventDetailedAllOfTimeRestrictions) o;
    return Objects.equals(this.timeOfDay, randomEventDetailedAllOfTimeRestrictions.timeOfDay) &&
        Objects.equals(this.weather, randomEventDetailedAllOfTimeRestrictions.weather);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeOfDay, weather);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RandomEventDetailedAllOfTimeRestrictions {\n");
    sb.append("    timeOfDay: ").append(toIndentedString(timeOfDay)).append("\n");
    sb.append("    weather: ").append(toIndentedString(weather)).append("\n");
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

