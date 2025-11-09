package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
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
 * ImpactTrigger
 */


public class ImpactTrigger {

  /**
   * Gets or Sets source
   */
  public enum SourceEnum {
    ORDERS_VOLUME("orders_volume"),
    
    RATINGS_SHIFT("ratings_shift"),
    
    CRISIS_MITIGATION("crisis_mitigation"),
    
    FACTION_DIRECTIVE("faction_directive"),
    
    MANUAL("manual"),
    
    ANALYTICS_THRESHOLD("analytics_threshold");

    private final String value;

    SourceEnum(String value) {
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
    public static SourceEnum fromValue(String value) {
      for (SourceEnum b : SourceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SourceEnum source;

  private String description;

  @Valid
  private Map<String, Float> metrics = new HashMap<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime detectedAt;

  public ImpactTrigger() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ImpactTrigger(SourceEnum source, String description) {
    this.source = source;
    this.description = description;
  }

  public ImpactTrigger source(SourceEnum source) {
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
  public SourceEnum getSource() {
    return source;
  }

  public void setSource(SourceEnum source) {
    this.source = source;
  }

  public ImpactTrigger description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @NotNull 
  @Schema(name = "description", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ImpactTrigger metrics(Map<String, Float> metrics) {
    this.metrics = metrics;
    return this;
  }

  public ImpactTrigger putMetricsItem(String key, Float metricsItem) {
    if (this.metrics == null) {
      this.metrics = new HashMap<>();
    }
    this.metrics.put(key, metricsItem);
    return this;
  }

  /**
   * Get metrics
   * @return metrics
   */
  
  @Schema(name = "metrics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metrics")
  public Map<String, Float> getMetrics() {
    return metrics;
  }

  public void setMetrics(Map<String, Float> metrics) {
    this.metrics = metrics;
  }

  public ImpactTrigger detectedAt(@Nullable OffsetDateTime detectedAt) {
    this.detectedAt = detectedAt;
    return this;
  }

  /**
   * Get detectedAt
   * @return detectedAt
   */
  @Valid 
  @Schema(name = "detectedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("detectedAt")
  public @Nullable OffsetDateTime getDetectedAt() {
    return detectedAt;
  }

  public void setDetectedAt(@Nullable OffsetDateTime detectedAt) {
    this.detectedAt = detectedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImpactTrigger impactTrigger = (ImpactTrigger) o;
    return Objects.equals(this.source, impactTrigger.source) &&
        Objects.equals(this.description, impactTrigger.description) &&
        Objects.equals(this.metrics, impactTrigger.metrics) &&
        Objects.equals(this.detectedAt, impactTrigger.detectedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, description, metrics, detectedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImpactTrigger {\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
    sb.append("    detectedAt: ").append(toIndentedString(detectedAt)).append("\n");
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

