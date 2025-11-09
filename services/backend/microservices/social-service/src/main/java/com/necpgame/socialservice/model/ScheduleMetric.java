package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
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
 * ScheduleMetric
 */


public class ScheduleMetric {

  private @Nullable String metricId;

  private @Nullable BigDecimal value;

  private @Nullable BigDecimal threshold;

  /**
   * Gets or Sets trend
   */
  public enum TrendEnum {
    UP("up"),
    
    DOWN("down"),
    
    FLAT("flat");

    private final String value;

    TrendEnum(String value) {
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
    public static TrendEnum fromValue(String value) {
      for (TrendEnum b : TrendEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TrendEnum trend;

  private @Nullable String window;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  public ScheduleMetric metricId(@Nullable String metricId) {
    this.metricId = metricId;
    return this;
  }

  /**
   * Get metricId
   * @return metricId
   */
  
  @Schema(name = "metricId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metricId")
  public @Nullable String getMetricId() {
    return metricId;
  }

  public void setMetricId(@Nullable String metricId) {
    this.metricId = metricId;
  }

  public ScheduleMetric value(@Nullable BigDecimal value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  @Valid 
  @Schema(name = "value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("value")
  public @Nullable BigDecimal getValue() {
    return value;
  }

  public void setValue(@Nullable BigDecimal value) {
    this.value = value;
  }

  public ScheduleMetric threshold(@Nullable BigDecimal threshold) {
    this.threshold = threshold;
    return this;
  }

  /**
   * Get threshold
   * @return threshold
   */
  @Valid 
  @Schema(name = "threshold", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("threshold")
  public @Nullable BigDecimal getThreshold() {
    return threshold;
  }

  public void setThreshold(@Nullable BigDecimal threshold) {
    this.threshold = threshold;
  }

  public ScheduleMetric trend(@Nullable TrendEnum trend) {
    this.trend = trend;
    return this;
  }

  /**
   * Get trend
   * @return trend
   */
  
  @Schema(name = "trend", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trend")
  public @Nullable TrendEnum getTrend() {
    return trend;
  }

  public void setTrend(@Nullable TrendEnum trend) {
    this.trend = trend;
  }

  public ScheduleMetric window(@Nullable String window) {
    this.window = window;
    return this;
  }

  /**
   * Get window
   * @return window
   */
  
  @Schema(name = "window", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("window")
  public @Nullable String getWindow() {
    return window;
  }

  public void setWindow(@Nullable String window) {
    this.window = window;
  }

  public ScheduleMetric timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScheduleMetric scheduleMetric = (ScheduleMetric) o;
    return Objects.equals(this.metricId, scheduleMetric.metricId) &&
        Objects.equals(this.value, scheduleMetric.value) &&
        Objects.equals(this.threshold, scheduleMetric.threshold) &&
        Objects.equals(this.trend, scheduleMetric.trend) &&
        Objects.equals(this.window, scheduleMetric.window) &&
        Objects.equals(this.timestamp, scheduleMetric.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(metricId, value, threshold, trend, window, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduleMetric {\n");
    sb.append("    metricId: ").append(toIndentedString(metricId)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    threshold: ").append(toIndentedString(threshold)).append("\n");
    sb.append("    trend: ").append(toIndentedString(trend)).append("\n");
    sb.append("    window: ").append(toIndentedString(window)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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

