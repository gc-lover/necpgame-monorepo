package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.InfrastructureAlert;
import com.necpgame.economyservice.model.InfrastructureMetric;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DistrictInfrastructureSummary
 */


public class DistrictInfrastructureSummary {

  private UUID districtId;

  private @Nullable String districtName;

  private @Nullable String profile;

  private Integer capacity;

  private BigDecimal utilization;

  private @Nullable BigDecimal energyLoad;

  private @Nullable BigDecimal staffDemand;

  @Valid
  private List<@Valid InfrastructureAlert> alerts = new ArrayList<>();

  @Valid
  private List<@Valid InfrastructureMetric> metrics = new ArrayList<>();

  public DistrictInfrastructureSummary() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DistrictInfrastructureSummary(UUID districtId, Integer capacity, BigDecimal utilization, List<@Valid InfrastructureMetric> metrics) {
    this.districtId = districtId;
    this.capacity = capacity;
    this.utilization = utilization;
    this.metrics = metrics;
  }

  public DistrictInfrastructureSummary districtId(UUID districtId) {
    this.districtId = districtId;
    return this;
  }

  /**
   * Get districtId
   * @return districtId
   */
  @NotNull @Valid 
  @Schema(name = "districtId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("districtId")
  public UUID getDistrictId() {
    return districtId;
  }

  public void setDistrictId(UUID districtId) {
    this.districtId = districtId;
  }

  public DistrictInfrastructureSummary districtName(@Nullable String districtName) {
    this.districtName = districtName;
    return this;
  }

  /**
   * Get districtName
   * @return districtName
   */
  
  @Schema(name = "districtName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("districtName")
  public @Nullable String getDistrictName() {
    return districtName;
  }

  public void setDistrictName(@Nullable String districtName) {
    this.districtName = districtName;
  }

  public DistrictInfrastructureSummary profile(@Nullable String profile) {
    this.profile = profile;
    return this;
  }

  /**
   * Get profile
   * @return profile
   */
  
  @Schema(name = "profile", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("profile")
  public @Nullable String getProfile() {
    return profile;
  }

  public void setProfile(@Nullable String profile) {
    this.profile = profile;
  }

  public DistrictInfrastructureSummary capacity(Integer capacity) {
    this.capacity = capacity;
    return this;
  }

  /**
   * Get capacity
   * @return capacity
   */
  @NotNull 
  @Schema(name = "capacity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("capacity")
  public Integer getCapacity() {
    return capacity;
  }

  public void setCapacity(Integer capacity) {
    this.capacity = capacity;
  }

  public DistrictInfrastructureSummary utilization(BigDecimal utilization) {
    this.utilization = utilization;
    return this;
  }

  /**
   * Get utilization
   * @return utilization
   */
  @NotNull @Valid 
  @Schema(name = "utilization", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("utilization")
  public BigDecimal getUtilization() {
    return utilization;
  }

  public void setUtilization(BigDecimal utilization) {
    this.utilization = utilization;
  }

  public DistrictInfrastructureSummary energyLoad(@Nullable BigDecimal energyLoad) {
    this.energyLoad = energyLoad;
    return this;
  }

  /**
   * Get energyLoad
   * @return energyLoad
   */
  @Valid 
  @Schema(name = "energyLoad", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energyLoad")
  public @Nullable BigDecimal getEnergyLoad() {
    return energyLoad;
  }

  public void setEnergyLoad(@Nullable BigDecimal energyLoad) {
    this.energyLoad = energyLoad;
  }

  public DistrictInfrastructureSummary staffDemand(@Nullable BigDecimal staffDemand) {
    this.staffDemand = staffDemand;
    return this;
  }

  /**
   * Get staffDemand
   * @return staffDemand
   */
  @Valid 
  @Schema(name = "staffDemand", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("staffDemand")
  public @Nullable BigDecimal getStaffDemand() {
    return staffDemand;
  }

  public void setStaffDemand(@Nullable BigDecimal staffDemand) {
    this.staffDemand = staffDemand;
  }

  public DistrictInfrastructureSummary alerts(List<@Valid InfrastructureAlert> alerts) {
    this.alerts = alerts;
    return this;
  }

  public DistrictInfrastructureSummary addAlertsItem(InfrastructureAlert alertsItem) {
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
  @Valid 
  @Schema(name = "alerts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alerts")
  public List<@Valid InfrastructureAlert> getAlerts() {
    return alerts;
  }

  public void setAlerts(List<@Valid InfrastructureAlert> alerts) {
    this.alerts = alerts;
  }

  public DistrictInfrastructureSummary metrics(List<@Valid InfrastructureMetric> metrics) {
    this.metrics = metrics;
    return this;
  }

  public DistrictInfrastructureSummary addMetricsItem(InfrastructureMetric metricsItem) {
    if (this.metrics == null) {
      this.metrics = new ArrayList<>();
    }
    this.metrics.add(metricsItem);
    return this;
  }

  /**
   * Get metrics
   * @return metrics
   */
  @NotNull @Valid 
  @Schema(name = "metrics", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("metrics")
  public List<@Valid InfrastructureMetric> getMetrics() {
    return metrics;
  }

  public void setMetrics(List<@Valid InfrastructureMetric> metrics) {
    this.metrics = metrics;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DistrictInfrastructureSummary districtInfrastructureSummary = (DistrictInfrastructureSummary) o;
    return Objects.equals(this.districtId, districtInfrastructureSummary.districtId) &&
        Objects.equals(this.districtName, districtInfrastructureSummary.districtName) &&
        Objects.equals(this.profile, districtInfrastructureSummary.profile) &&
        Objects.equals(this.capacity, districtInfrastructureSummary.capacity) &&
        Objects.equals(this.utilization, districtInfrastructureSummary.utilization) &&
        Objects.equals(this.energyLoad, districtInfrastructureSummary.energyLoad) &&
        Objects.equals(this.staffDemand, districtInfrastructureSummary.staffDemand) &&
        Objects.equals(this.alerts, districtInfrastructureSummary.alerts) &&
        Objects.equals(this.metrics, districtInfrastructureSummary.metrics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(districtId, districtName, profile, capacity, utilization, energyLoad, staffDemand, alerts, metrics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DistrictInfrastructureSummary {\n");
    sb.append("    districtId: ").append(toIndentedString(districtId)).append("\n");
    sb.append("    districtName: ").append(toIndentedString(districtName)).append("\n");
    sb.append("    profile: ").append(toIndentedString(profile)).append("\n");
    sb.append("    capacity: ").append(toIndentedString(capacity)).append("\n");
    sb.append("    utilization: ").append(toIndentedString(utilization)).append("\n");
    sb.append("    energyLoad: ").append(toIndentedString(energyLoad)).append("\n");
    sb.append("    staffDemand: ").append(toIndentedString(staffDemand)).append("\n");
    sb.append("    alerts: ").append(toIndentedString(alerts)).append("\n");
    sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
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

