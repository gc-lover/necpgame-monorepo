package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
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
 * PerformanceAlertRule
 */


public class PerformanceAlertRule {

  private @Nullable String ruleName;

  /**
   * Gets or Sets metric
   */
  public enum MetricEnum {
    CPU("cpu"),
    
    MEMORY("memory"),
    
    LATENCY("latency"),
    
    ERROR_RATE("error_rate");

    private final String value;

    MetricEnum(String value) {
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
    public static MetricEnum fromValue(String value) {
      for (MetricEnum b : MetricEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private MetricEnum metric;

  private BigDecimal threshold;

  /**
   * Gets or Sets condition
   */
  public enum ConditionEnum {
    GREATER_THAN("greater_than"),
    
    LESS_THAN("less_than");

    private final String value;

    ConditionEnum(String value) {
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
    public static ConditionEnum fromValue(String value) {
      for (ConditionEnum b : ConditionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ConditionEnum condition;

  @Valid
  private List<String> notificationChannels = new ArrayList<>();

  public PerformanceAlertRule() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PerformanceAlertRule(MetricEnum metric, BigDecimal threshold, ConditionEnum condition) {
    this.metric = metric;
    this.threshold = threshold;
    this.condition = condition;
  }

  public PerformanceAlertRule ruleName(@Nullable String ruleName) {
    this.ruleName = ruleName;
    return this;
  }

  /**
   * Get ruleName
   * @return ruleName
   */
  
  @Schema(name = "rule_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rule_name")
  public @Nullable String getRuleName() {
    return ruleName;
  }

  public void setRuleName(@Nullable String ruleName) {
    this.ruleName = ruleName;
  }

  public PerformanceAlertRule metric(MetricEnum metric) {
    this.metric = metric;
    return this;
  }

  /**
   * Get metric
   * @return metric
   */
  @NotNull 
  @Schema(name = "metric", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("metric")
  public MetricEnum getMetric() {
    return metric;
  }

  public void setMetric(MetricEnum metric) {
    this.metric = metric;
  }

  public PerformanceAlertRule threshold(BigDecimal threshold) {
    this.threshold = threshold;
    return this;
  }

  /**
   * Get threshold
   * @return threshold
   */
  @NotNull @Valid 
  @Schema(name = "threshold", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("threshold")
  public BigDecimal getThreshold() {
    return threshold;
  }

  public void setThreshold(BigDecimal threshold) {
    this.threshold = threshold;
  }

  public PerformanceAlertRule condition(ConditionEnum condition) {
    this.condition = condition;
    return this;
  }

  /**
   * Get condition
   * @return condition
   */
  @NotNull 
  @Schema(name = "condition", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("condition")
  public ConditionEnum getCondition() {
    return condition;
  }

  public void setCondition(ConditionEnum condition) {
    this.condition = condition;
  }

  public PerformanceAlertRule notificationChannels(List<String> notificationChannels) {
    this.notificationChannels = notificationChannels;
    return this;
  }

  public PerformanceAlertRule addNotificationChannelsItem(String notificationChannelsItem) {
    if (this.notificationChannels == null) {
      this.notificationChannels = new ArrayList<>();
    }
    this.notificationChannels.add(notificationChannelsItem);
    return this;
  }

  /**
   * Get notificationChannels
   * @return notificationChannels
   */
  
  @Schema(name = "notification_channels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notification_channels")
  public List<String> getNotificationChannels() {
    return notificationChannels;
  }

  public void setNotificationChannels(List<String> notificationChannels) {
    this.notificationChannels = notificationChannels;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformanceAlertRule performanceAlertRule = (PerformanceAlertRule) o;
    return Objects.equals(this.ruleName, performanceAlertRule.ruleName) &&
        Objects.equals(this.metric, performanceAlertRule.metric) &&
        Objects.equals(this.threshold, performanceAlertRule.threshold) &&
        Objects.equals(this.condition, performanceAlertRule.condition) &&
        Objects.equals(this.notificationChannels, performanceAlertRule.notificationChannels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ruleName, metric, threshold, condition, notificationChannels);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformanceAlertRule {\n");
    sb.append("    ruleName: ").append(toIndentedString(ruleName)).append("\n");
    sb.append("    metric: ").append(toIndentedString(metric)).append("\n");
    sb.append("    threshold: ").append(toIndentedString(threshold)).append("\n");
    sb.append("    condition: ").append(toIndentedString(condition)).append("\n");
    sb.append("    notificationChannels: ").append(toIndentedString(notificationChannels)).append("\n");
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

