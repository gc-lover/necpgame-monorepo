package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * RiskAlertThreshold
 */


public class RiskAlertThreshold {

  /**
   * Gets or Sets metric
   */
  public enum MetricEnum {
    RISK_SCORE("riskScore"),
    
    ESCROW_AMOUNT("escrowAmount"),
    
    DISPUTE_RATE("disputeRate"),
    
    RATING_DELTA("ratingDelta");

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

  /**
   * Gets or Sets operator
   */
  public enum OperatorEnum {
    GT("gt"),
    
    GTE("gte"),
    
    LT("lt"),
    
    LTE("lte"),
    
    EQ("eq");

    private final String value;

    OperatorEnum(String value) {
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
    public static OperatorEnum fromValue(String value) {
      for (OperatorEnum b : OperatorEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private OperatorEnum operator;

  private Float value;

  private @Nullable Integer coolDownMinutes;

  public RiskAlertThreshold() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RiskAlertThreshold(MetricEnum metric, OperatorEnum operator, Float value) {
    this.metric = metric;
    this.operator = operator;
    this.value = value;
  }

  public RiskAlertThreshold metric(MetricEnum metric) {
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

  public RiskAlertThreshold operator(OperatorEnum operator) {
    this.operator = operator;
    return this;
  }

  /**
   * Get operator
   * @return operator
   */
  @NotNull 
  @Schema(name = "operator", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("operator")
  public OperatorEnum getOperator() {
    return operator;
  }

  public void setOperator(OperatorEnum operator) {
    this.operator = operator;
  }

  public RiskAlertThreshold value(Float value) {
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

  public RiskAlertThreshold coolDownMinutes(@Nullable Integer coolDownMinutes) {
    this.coolDownMinutes = coolDownMinutes;
    return this;
  }

  /**
   * Get coolDownMinutes
   * minimum: 0
   * @return coolDownMinutes
   */
  @Min(value = 0) 
  @Schema(name = "coolDownMinutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("coolDownMinutes")
  public @Nullable Integer getCoolDownMinutes() {
    return coolDownMinutes;
  }

  public void setCoolDownMinutes(@Nullable Integer coolDownMinutes) {
    this.coolDownMinutes = coolDownMinutes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiskAlertThreshold riskAlertThreshold = (RiskAlertThreshold) o;
    return Objects.equals(this.metric, riskAlertThreshold.metric) &&
        Objects.equals(this.operator, riskAlertThreshold.operator) &&
        Objects.equals(this.value, riskAlertThreshold.value) &&
        Objects.equals(this.coolDownMinutes, riskAlertThreshold.coolDownMinutes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(metric, operator, value, coolDownMinutes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiskAlertThreshold {\n");
    sb.append("    metric: ").append(toIndentedString(metric)).append("\n");
    sb.append("    operator: ").append(toIndentedString(operator)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    coolDownMinutes: ").append(toIndentedString(coolDownMinutes)).append("\n");
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

