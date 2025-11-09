package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.MetricUnit;
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
 * MetricPoint
 */


public class MetricPoint {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private Float value;

  private @Nullable MetricUnit unit;

  private @Nullable Integer sampleSize;

  public MetricPoint() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MetricPoint(OffsetDateTime timestamp, Float value) {
    this.timestamp = timestamp;
    this.value = value;
  }

  public MetricPoint timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @NotNull @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public MetricPoint value(Float value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  @NotNull 
  @Schema(name = "value", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("value")
  public Float getValue() {
    return value;
  }

  public void setValue(Float value) {
    this.value = value;
  }

  public MetricPoint unit(@Nullable MetricUnit unit) {
    this.unit = unit;
    return this;
  }

  /**
   * Get unit
   * @return unit
   */
  @Valid 
  @Schema(name = "unit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unit")
  public @Nullable MetricUnit getUnit() {
    return unit;
  }

  public void setUnit(@Nullable MetricUnit unit) {
    this.unit = unit;
  }

  public MetricPoint sampleSize(@Nullable Integer sampleSize) {
    this.sampleSize = sampleSize;
    return this;
  }

  /**
   * Get sampleSize
   * minimum: 0
   * @return sampleSize
   */
  @Min(value = 0) 
  @Schema(name = "sampleSize", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sampleSize")
  public @Nullable Integer getSampleSize() {
    return sampleSize;
  }

  public void setSampleSize(@Nullable Integer sampleSize) {
    this.sampleSize = sampleSize;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetricPoint metricPoint = (MetricPoint) o;
    return Objects.equals(this.timestamp, metricPoint.timestamp) &&
        Objects.equals(this.value, metricPoint.value) &&
        Objects.equals(this.unit, metricPoint.unit) &&
        Objects.equals(this.sampleSize, metricPoint.sampleSize);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, value, unit, sampleSize);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MetricPoint {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    unit: ").append(toIndentedString(unit)).append("\n");
    sb.append("    sampleSize: ").append(toIndentedString(sampleSize)).append("\n");
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

