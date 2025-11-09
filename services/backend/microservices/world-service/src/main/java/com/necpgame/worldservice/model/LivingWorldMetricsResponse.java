package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * LivingWorldMetricsResponse
 */


public class LivingWorldMetricsResponse {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime windowStart;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime windowEnd;

  private BigDecimal controlShiftRate;

  private @Nullable BigDecimal controlShiftLimit;

  private BigDecimal fatigueOverflow;

  private BigDecimal routeSurvivalRate;

  @Valid
  private List<String> alerts = new ArrayList<>();

  @Valid
  private List<String> recommendations = new ArrayList<>();

  public LivingWorldMetricsResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LivingWorldMetricsResponse(OffsetDateTime windowStart, OffsetDateTime windowEnd, BigDecimal controlShiftRate, BigDecimal fatigueOverflow, BigDecimal routeSurvivalRate) {
    this.windowStart = windowStart;
    this.windowEnd = windowEnd;
    this.controlShiftRate = controlShiftRate;
    this.fatigueOverflow = fatigueOverflow;
    this.routeSurvivalRate = routeSurvivalRate;
  }

  public LivingWorldMetricsResponse windowStart(OffsetDateTime windowStart) {
    this.windowStart = windowStart;
    return this;
  }

  /**
   * Get windowStart
   * @return windowStart
   */
  @NotNull @Valid 
  @Schema(name = "windowStart", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("windowStart")
  public OffsetDateTime getWindowStart() {
    return windowStart;
  }

  public void setWindowStart(OffsetDateTime windowStart) {
    this.windowStart = windowStart;
  }

  public LivingWorldMetricsResponse windowEnd(OffsetDateTime windowEnd) {
    this.windowEnd = windowEnd;
    return this;
  }

  /**
   * Get windowEnd
   * @return windowEnd
   */
  @NotNull @Valid 
  @Schema(name = "windowEnd", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("windowEnd")
  public OffsetDateTime getWindowEnd() {
    return windowEnd;
  }

  public void setWindowEnd(OffsetDateTime windowEnd) {
    this.windowEnd = windowEnd;
  }

  public LivingWorldMetricsResponse controlShiftRate(BigDecimal controlShiftRate) {
    this.controlShiftRate = controlShiftRate;
    return this;
  }

  /**
   * Get controlShiftRate
   * @return controlShiftRate
   */
  @NotNull @Valid 
  @Schema(name = "controlShiftRate", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("controlShiftRate")
  public BigDecimal getControlShiftRate() {
    return controlShiftRate;
  }

  public void setControlShiftRate(BigDecimal controlShiftRate) {
    this.controlShiftRate = controlShiftRate;
  }

  public LivingWorldMetricsResponse controlShiftLimit(@Nullable BigDecimal controlShiftLimit) {
    this.controlShiftLimit = controlShiftLimit;
    return this;
  }

  /**
   * Get controlShiftLimit
   * @return controlShiftLimit
   */
  @Valid 
  @Schema(name = "controlShiftLimit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("controlShiftLimit")
  public @Nullable BigDecimal getControlShiftLimit() {
    return controlShiftLimit;
  }

  public void setControlShiftLimit(@Nullable BigDecimal controlShiftLimit) {
    this.controlShiftLimit = controlShiftLimit;
  }

  public LivingWorldMetricsResponse fatigueOverflow(BigDecimal fatigueOverflow) {
    this.fatigueOverflow = fatigueOverflow;
    return this;
  }

  /**
   * Get fatigueOverflow
   * @return fatigueOverflow
   */
  @NotNull @Valid 
  @Schema(name = "fatigueOverflow", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fatigueOverflow")
  public BigDecimal getFatigueOverflow() {
    return fatigueOverflow;
  }

  public void setFatigueOverflow(BigDecimal fatigueOverflow) {
    this.fatigueOverflow = fatigueOverflow;
  }

  public LivingWorldMetricsResponse routeSurvivalRate(BigDecimal routeSurvivalRate) {
    this.routeSurvivalRate = routeSurvivalRate;
    return this;
  }

  /**
   * Get routeSurvivalRate
   * @return routeSurvivalRate
   */
  @NotNull @Valid 
  @Schema(name = "routeSurvivalRate", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("routeSurvivalRate")
  public BigDecimal getRouteSurvivalRate() {
    return routeSurvivalRate;
  }

  public void setRouteSurvivalRate(BigDecimal routeSurvivalRate) {
    this.routeSurvivalRate = routeSurvivalRate;
  }

  public LivingWorldMetricsResponse alerts(List<String> alerts) {
    this.alerts = alerts;
    return this;
  }

  public LivingWorldMetricsResponse addAlertsItem(String alertsItem) {
    if (this.alerts == null) {
      this.alerts = new ArrayList<>();
    }
    this.alerts.add(alertsItem);
    return this;
  }

  /**
   * Get alerts
   * @return alerts
   */
  
  @Schema(name = "alerts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alerts")
  public List<String> getAlerts() {
    return alerts;
  }

  public void setAlerts(List<String> alerts) {
    this.alerts = alerts;
  }

  public LivingWorldMetricsResponse recommendations(List<String> recommendations) {
    this.recommendations = recommendations;
    return this;
  }

  public LivingWorldMetricsResponse addRecommendationsItem(String recommendationsItem) {
    if (this.recommendations == null) {
      this.recommendations = new ArrayList<>();
    }
    this.recommendations.add(recommendationsItem);
    return this;
  }

  /**
   * Get recommendations
   * @return recommendations
   */
  
  @Schema(name = "recommendations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendations")
  public List<String> getRecommendations() {
    return recommendations;
  }

  public void setRecommendations(List<String> recommendations) {
    this.recommendations = recommendations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LivingWorldMetricsResponse livingWorldMetricsResponse = (LivingWorldMetricsResponse) o;
    return Objects.equals(this.windowStart, livingWorldMetricsResponse.windowStart) &&
        Objects.equals(this.windowEnd, livingWorldMetricsResponse.windowEnd) &&
        Objects.equals(this.controlShiftRate, livingWorldMetricsResponse.controlShiftRate) &&
        Objects.equals(this.controlShiftLimit, livingWorldMetricsResponse.controlShiftLimit) &&
        Objects.equals(this.fatigueOverflow, livingWorldMetricsResponse.fatigueOverflow) &&
        Objects.equals(this.routeSurvivalRate, livingWorldMetricsResponse.routeSurvivalRate) &&
        Objects.equals(this.alerts, livingWorldMetricsResponse.alerts) &&
        Objects.equals(this.recommendations, livingWorldMetricsResponse.recommendations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(windowStart, windowEnd, controlShiftRate, controlShiftLimit, fatigueOverflow, routeSurvivalRate, alerts, recommendations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LivingWorldMetricsResponse {\n");
    sb.append("    windowStart: ").append(toIndentedString(windowStart)).append("\n");
    sb.append("    windowEnd: ").append(toIndentedString(windowEnd)).append("\n");
    sb.append("    controlShiftRate: ").append(toIndentedString(controlShiftRate)).append("\n");
    sb.append("    controlShiftLimit: ").append(toIndentedString(controlShiftLimit)).append("\n");
    sb.append("    fatigueOverflow: ").append(toIndentedString(fatigueOverflow)).append("\n");
    sb.append("    routeSurvivalRate: ").append(toIndentedString(routeSurvivalRate)).append("\n");
    sb.append("    alerts: ").append(toIndentedString(alerts)).append("\n");
    sb.append("    recommendations: ").append(toIndentedString(recommendations)).append("\n");
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

