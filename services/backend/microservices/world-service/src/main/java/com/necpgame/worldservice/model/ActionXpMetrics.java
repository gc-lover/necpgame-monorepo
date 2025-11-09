package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.ActionXpMetricsTopSkillsInner;
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
 * ActionXpMetrics
 */


public class ActionXpMetrics {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime windowStart;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime windowEnd;

  private @Nullable BigDecimal totalXp;

  private @Nullable BigDecimal averageMultiplier;

  private @Nullable BigDecimal fatigueOverflowRate;

  @Valid
  private List<@Valid ActionXpMetricsTopSkillsInner> topSkills = new ArrayList<>();

  @Valid
  private List<String> alerts = new ArrayList<>();

  public ActionXpMetrics() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ActionXpMetrics(OffsetDateTime windowStart, OffsetDateTime windowEnd, List<@Valid ActionXpMetricsTopSkillsInner> topSkills) {
    this.windowStart = windowStart;
    this.windowEnd = windowEnd;
    this.topSkills = topSkills;
  }

  public ActionXpMetrics windowStart(OffsetDateTime windowStart) {
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

  public ActionXpMetrics windowEnd(OffsetDateTime windowEnd) {
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

  public ActionXpMetrics totalXp(@Nullable BigDecimal totalXp) {
    this.totalXp = totalXp;
    return this;
  }

  /**
   * Get totalXp
   * @return totalXp
   */
  @Valid 
  @Schema(name = "totalXp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalXp")
  public @Nullable BigDecimal getTotalXp() {
    return totalXp;
  }

  public void setTotalXp(@Nullable BigDecimal totalXp) {
    this.totalXp = totalXp;
  }

  public ActionXpMetrics averageMultiplier(@Nullable BigDecimal averageMultiplier) {
    this.averageMultiplier = averageMultiplier;
    return this;
  }

  /**
   * Get averageMultiplier
   * @return averageMultiplier
   */
  @Valid 
  @Schema(name = "averageMultiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("averageMultiplier")
  public @Nullable BigDecimal getAverageMultiplier() {
    return averageMultiplier;
  }

  public void setAverageMultiplier(@Nullable BigDecimal averageMultiplier) {
    this.averageMultiplier = averageMultiplier;
  }

  public ActionXpMetrics fatigueOverflowRate(@Nullable BigDecimal fatigueOverflowRate) {
    this.fatigueOverflowRate = fatigueOverflowRate;
    return this;
  }

  /**
   * Get fatigueOverflowRate
   * @return fatigueOverflowRate
   */
  @Valid 
  @Schema(name = "fatigueOverflowRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fatigueOverflowRate")
  public @Nullable BigDecimal getFatigueOverflowRate() {
    return fatigueOverflowRate;
  }

  public void setFatigueOverflowRate(@Nullable BigDecimal fatigueOverflowRate) {
    this.fatigueOverflowRate = fatigueOverflowRate;
  }

  public ActionXpMetrics topSkills(List<@Valid ActionXpMetricsTopSkillsInner> topSkills) {
    this.topSkills = topSkills;
    return this;
  }

  public ActionXpMetrics addTopSkillsItem(ActionXpMetricsTopSkillsInner topSkillsItem) {
    if (this.topSkills == null) {
      this.topSkills = new ArrayList<>();
    }
    this.topSkills.add(topSkillsItem);
    return this;
  }

  /**
   * Get topSkills
   * @return topSkills
   */
  @NotNull @Valid 
  @Schema(name = "topSkills", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("topSkills")
  public List<@Valid ActionXpMetricsTopSkillsInner> getTopSkills() {
    return topSkills;
  }

  public void setTopSkills(List<@Valid ActionXpMetricsTopSkillsInner> topSkills) {
    this.topSkills = topSkills;
  }

  public ActionXpMetrics alerts(List<String> alerts) {
    this.alerts = alerts;
    return this;
  }

  public ActionXpMetrics addAlertsItem(String alertsItem) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActionXpMetrics actionXpMetrics = (ActionXpMetrics) o;
    return Objects.equals(this.windowStart, actionXpMetrics.windowStart) &&
        Objects.equals(this.windowEnd, actionXpMetrics.windowEnd) &&
        Objects.equals(this.totalXp, actionXpMetrics.totalXp) &&
        Objects.equals(this.averageMultiplier, actionXpMetrics.averageMultiplier) &&
        Objects.equals(this.fatigueOverflowRate, actionXpMetrics.fatigueOverflowRate) &&
        Objects.equals(this.topSkills, actionXpMetrics.topSkills) &&
        Objects.equals(this.alerts, actionXpMetrics.alerts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(windowStart, windowEnd, totalXp, averageMultiplier, fatigueOverflowRate, topSkills, alerts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActionXpMetrics {\n");
    sb.append("    windowStart: ").append(toIndentedString(windowStart)).append("\n");
    sb.append("    windowEnd: ").append(toIndentedString(windowEnd)).append("\n");
    sb.append("    totalXp: ").append(toIndentedString(totalXp)).append("\n");
    sb.append("    averageMultiplier: ").append(toIndentedString(averageMultiplier)).append("\n");
    sb.append("    fatigueOverflowRate: ").append(toIndentedString(fatigueOverflowRate)).append("\n");
    sb.append("    topSkills: ").append(toIndentedString(topSkills)).append("\n");
    sb.append("    alerts: ").append(toIndentedString(alerts)).append("\n");
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

