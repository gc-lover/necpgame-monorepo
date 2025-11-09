package com.necpgame.adminservice.model;

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
 * MetricSnapshotThreshold
 */

@JsonTypeName("MetricSnapshot_threshold")

public class MetricSnapshotThreshold {

  private @Nullable Float lower;

  private @Nullable Float upper;

  public MetricSnapshotThreshold lower(@Nullable Float lower) {
    this.lower = lower;
    return this;
  }

  /**
   * Get lower
   * @return lower
   */
  
  @Schema(name = "lower", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lower")
  public @Nullable Float getLower() {
    return lower;
  }

  public void setLower(@Nullable Float lower) {
    this.lower = lower;
  }

  public MetricSnapshotThreshold upper(@Nullable Float upper) {
    this.upper = upper;
    return this;
  }

  /**
   * Get upper
   * @return upper
   */
  
  @Schema(name = "upper", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("upper")
  public @Nullable Float getUpper() {
    return upper;
  }

  public void setUpper(@Nullable Float upper) {
    this.upper = upper;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetricSnapshotThreshold metricSnapshotThreshold = (MetricSnapshotThreshold) o;
    return Objects.equals(this.lower, metricSnapshotThreshold.lower) &&
        Objects.equals(this.upper, metricSnapshotThreshold.upper);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lower, upper);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MetricSnapshotThreshold {\n");
    sb.append("    lower: ").append(toIndentedString(lower)).append("\n");
    sb.append("    upper: ").append(toIndentedString(upper)).append("\n");
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

