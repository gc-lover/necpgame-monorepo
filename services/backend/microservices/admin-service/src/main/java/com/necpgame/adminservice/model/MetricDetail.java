package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.MetricDetailMetadata;
import com.necpgame.adminservice.model.MetricPoint;
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
 * MetricDetail
 */


public class MetricDetail {

  private String metricId;

  private @Nullable String factionId;

  @Valid
  private List<@Valid MetricPoint> buckets = new ArrayList<>();

  private @Nullable MetricDetailMetadata metadata;

  public MetricDetail() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MetricDetail(String metricId, List<@Valid MetricPoint> buckets) {
    this.metricId = metricId;
    this.buckets = buckets;
  }

  public MetricDetail metricId(String metricId) {
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

  public MetricDetail factionId(@Nullable String factionId) {
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

  public MetricDetail buckets(List<@Valid MetricPoint> buckets) {
    this.buckets = buckets;
    return this;
  }

  public MetricDetail addBucketsItem(MetricPoint bucketsItem) {
    if (this.buckets == null) {
      this.buckets = new ArrayList<>();
    }
    this.buckets.add(bucketsItem);
    return this;
  }

  /**
   * Get buckets
   * @return buckets
   */
  @NotNull @Valid 
  @Schema(name = "buckets", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("buckets")
  public List<@Valid MetricPoint> getBuckets() {
    return buckets;
  }

  public void setBuckets(List<@Valid MetricPoint> buckets) {
    this.buckets = buckets;
  }

  public MetricDetail metadata(@Nullable MetricDetailMetadata metadata) {
    this.metadata = metadata;
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   */
  @Valid 
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public @Nullable MetricDetailMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(@Nullable MetricDetailMetadata metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetricDetail metricDetail = (MetricDetail) o;
    return Objects.equals(this.metricId, metricDetail.metricId) &&
        Objects.equals(this.factionId, metricDetail.factionId) &&
        Objects.equals(this.buckets, metricDetail.buckets) &&
        Objects.equals(this.metadata, metricDetail.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(metricId, factionId, buckets, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MetricDetail {\n");
    sb.append("    metricId: ").append(toIndentedString(metricId)).append("\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    buckets: ").append(toIndentedString(buckets)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

