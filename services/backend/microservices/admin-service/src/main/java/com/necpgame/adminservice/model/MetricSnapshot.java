package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.MetricSnapshotThreshold;
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
 * MetricSnapshot
 */


public class MetricSnapshot {

  private String metricId;

  private @Nullable String factionId;

  private Float value;

  private MetricUnit unit;

  private String period;

  private String source;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime updatedAt;

  private @Nullable Float trend;

  private @Nullable MetricSnapshotThreshold threshold;

  public MetricSnapshot() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MetricSnapshot(String metricId, Float value, MetricUnit unit, String period, String source, OffsetDateTime updatedAt) {
    this.metricId = metricId;
    this.value = value;
    this.unit = unit;
    this.period = period;
    this.source = source;
    this.updatedAt = updatedAt;
  }

  public MetricSnapshot metricId(String metricId) {
    this.metricId = metricId;
    return this;
  }

  /**
   * Get metricId
   * @return metricId
   */
  @NotNull 
  @Schema(name = "metricId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("metricId")
  public String getMetricId() {
    return metricId;
  }

  public void setMetricId(String metricId) {
    this.metricId = metricId;
  }

  public MetricSnapshot factionId(@Nullable String factionId) {
    this.factionId = factionId;
    return this;
  }

  /**
   * Get factionId
   * @return factionId
   */
  
  @Schema(name = "factionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factionId")
  public @Nullable String getFactionId() {
    return factionId;
  }

  public void setFactionId(@Nullable String factionId) {
    this.factionId = factionId;
  }

  public MetricSnapshot value(Float value) {
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

  public MetricSnapshot unit(MetricUnit unit) {
    this.unit = unit;
    return this;
  }

  /**
   * Get unit
   * @return unit
   */
  @NotNull @Valid 
  @Schema(name = "unit", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("unit")
  public MetricUnit getUnit() {
    return unit;
  }

  public void setUnit(MetricUnit unit) {
    this.unit = unit;
  }

  public MetricSnapshot period(String period) {
    this.period = period;
    return this;
  }

  /**
   * Get period
   * @return period
   */
  @NotNull 
  @Schema(name = "period", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("period")
  public String getPeriod() {
    return period;
  }

  public void setPeriod(String period) {
    this.period = period;
  }

  public MetricSnapshot source(String source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  @NotNull 
  @Schema(name = "source", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("source")
  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public MetricSnapshot updatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @NotNull @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("updatedAt")
  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public MetricSnapshot trend(@Nullable Float trend) {
    this.trend = trend;
    return this;
  }

  /**
   * Get trend
   * @return trend
   */
  
  @Schema(name = "trend", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trend")
  public @Nullable Float getTrend() {
    return trend;
  }

  public void setTrend(@Nullable Float trend) {
    this.trend = trend;
  }

  public MetricSnapshot threshold(@Nullable MetricSnapshotThreshold threshold) {
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
  public @Nullable MetricSnapshotThreshold getThreshold() {
    return threshold;
  }

  public void setThreshold(@Nullable MetricSnapshotThreshold threshold) {
    this.threshold = threshold;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetricSnapshot metricSnapshot = (MetricSnapshot) o;
    return Objects.equals(this.metricId, metricSnapshot.metricId) &&
        Objects.equals(this.factionId, metricSnapshot.factionId) &&
        Objects.equals(this.value, metricSnapshot.value) &&
        Objects.equals(this.unit, metricSnapshot.unit) &&
        Objects.equals(this.period, metricSnapshot.period) &&
        Objects.equals(this.source, metricSnapshot.source) &&
        Objects.equals(this.updatedAt, metricSnapshot.updatedAt) &&
        Objects.equals(this.trend, metricSnapshot.trend) &&
        Objects.equals(this.threshold, metricSnapshot.threshold);
  }

  @Override
  public int hashCode() {
    return Objects.hash(metricId, factionId, value, unit, period, source, updatedAt, trend, threshold);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MetricSnapshot {\n");
    sb.append("    metricId: ").append(toIndentedString(metricId)).append("\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    unit: ").append(toIndentedString(unit)).append("\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    trend: ").append(toIndentedString(trend)).append("\n");
    sb.append("    threshold: ").append(toIndentedString(threshold)).append("\n");
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

