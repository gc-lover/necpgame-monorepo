package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Bottleneck
 */


public class Bottleneck {

  /**
   * Gets or Sets bottleneckType
   */
  public enum BottleneckTypeEnum {
    SLOW_QUERY("slow_query"),
    
    HIGH_LATENCY_ENDPOINT("high_latency_endpoint"),
    
    MEMORY_LEAK("memory_leak"),
    
    CPU_SPIKE("cpu_spike");

    private final String value;

    BottleneckTypeEnum(String value) {
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
    public static BottleneckTypeEnum fromValue(String value) {
      for (BottleneckTypeEnum b : BottleneckTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable BottleneckTypeEnum bottleneckType;

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high"),
    
    CRITICAL("critical");

    private final String value;

    SeverityEnum(String value) {
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
    public static SeverityEnum fromValue(String value) {
      for (SeverityEnum b : SeverityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SeverityEnum severity;

  private @Nullable String description;

  private @Nullable String affectedComponent;

  @Valid
  private Map<String, Object> metrics = new HashMap<>();

  private @Nullable String recommendation;

  public Bottleneck bottleneckType(@Nullable BottleneckTypeEnum bottleneckType) {
    this.bottleneckType = bottleneckType;
    return this;
  }

  /**
   * Get bottleneckType
   * @return bottleneckType
   */
  
  @Schema(name = "bottleneck_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bottleneck_type")
  public @Nullable BottleneckTypeEnum getBottleneckType() {
    return bottleneckType;
  }

  public void setBottleneckType(@Nullable BottleneckTypeEnum bottleneckType) {
    this.bottleneckType = bottleneckType;
  }

  public Bottleneck severity(@Nullable SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("severity")
  public @Nullable SeverityEnum getSeverity() {
    return severity;
  }

  public void setSeverity(@Nullable SeverityEnum severity) {
    this.severity = severity;
  }

  public Bottleneck description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public Bottleneck affectedComponent(@Nullable String affectedComponent) {
    this.affectedComponent = affectedComponent;
    return this;
  }

  /**
   * Get affectedComponent
   * @return affectedComponent
   */
  
  @Schema(name = "affected_component", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affected_component")
  public @Nullable String getAffectedComponent() {
    return affectedComponent;
  }

  public void setAffectedComponent(@Nullable String affectedComponent) {
    this.affectedComponent = affectedComponent;
  }

  public Bottleneck metrics(Map<String, Object> metrics) {
    this.metrics = metrics;
    return this;
  }

  public Bottleneck putMetricsItem(String key, Object metricsItem) {
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
  public Map<String, Object> getMetrics() {
    return metrics;
  }

  public void setMetrics(Map<String, Object> metrics) {
    this.metrics = metrics;
  }

  public Bottleneck recommendation(@Nullable String recommendation) {
    this.recommendation = recommendation;
    return this;
  }

  /**
   * Get recommendation
   * @return recommendation
   */
  
  @Schema(name = "recommendation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendation")
  public @Nullable String getRecommendation() {
    return recommendation;
  }

  public void setRecommendation(@Nullable String recommendation) {
    this.recommendation = recommendation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Bottleneck bottleneck = (Bottleneck) o;
    return Objects.equals(this.bottleneckType, bottleneck.bottleneckType) &&
        Objects.equals(this.severity, bottleneck.severity) &&
        Objects.equals(this.description, bottleneck.description) &&
        Objects.equals(this.affectedComponent, bottleneck.affectedComponent) &&
        Objects.equals(this.metrics, bottleneck.metrics) &&
        Objects.equals(this.recommendation, bottleneck.recommendation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bottleneckType, severity, description, affectedComponent, metrics, recommendation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Bottleneck {\n");
    sb.append("    bottleneckType: ").append(toIndentedString(bottleneckType)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    affectedComponent: ").append(toIndentedString(affectedComponent)).append("\n");
    sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
    sb.append("    recommendation: ").append(toIndentedString(recommendation)).append("\n");
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

