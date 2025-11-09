package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.PopulationAlert;
import com.necpgame.worldservice.model.PopulationMetric;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * DistrictPopulationState
 */


public class DistrictPopulationState {

  private UUID districtId;

  private String name;

  /**
   * Gets or Sets segment
   */
  public enum SegmentEnum {
    RESIDENTIAL("residential"),
    
    CORPORATE("corporate"),
    
    INDUSTRIAL("industrial"),
    
    CULTURAL("cultural"),
    
    CRIMINAL("criminal"),
    
    MIXED("mixed");

    private final String value;

    SegmentEnum(String value) {
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
    public static SegmentEnum fromValue(String value) {
      for (SegmentEnum b : SegmentEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SegmentEnum segment;

  private Integer npcCount;

  private Integer capacity;

  private @Nullable BigDecimal capacityUsage;

  private BigDecimal growthRate;

  @Valid
  private List<@Valid PopulationAlert> alerts = new ArrayList<>();

  private @Nullable String dominantFaction;

  @Valid
  private Map<String, BigDecimal> influence = new HashMap<>();

  @Valid
  private List<@Valid PopulationMetric> metrics = new ArrayList<>();

  public DistrictPopulationState() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DistrictPopulationState(UUID districtId, String name, SegmentEnum segment, Integer npcCount, Integer capacity, BigDecimal growthRate) {
    this.districtId = districtId;
    this.name = name;
    this.segment = segment;
    this.npcCount = npcCount;
    this.capacity = capacity;
    this.growthRate = growthRate;
  }

  public DistrictPopulationState districtId(UUID districtId) {
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

  public DistrictPopulationState name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DistrictPopulationState segment(SegmentEnum segment) {
    this.segment = segment;
    return this;
  }

  /**
   * Get segment
   * @return segment
   */
  @NotNull 
  @Schema(name = "segment", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("segment")
  public SegmentEnum getSegment() {
    return segment;
  }

  public void setSegment(SegmentEnum segment) {
    this.segment = segment;
  }

  public DistrictPopulationState npcCount(Integer npcCount) {
    this.npcCount = npcCount;
    return this;
  }

  /**
   * Get npcCount
   * minimum: 0
   * @return npcCount
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "npcCount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("npcCount")
  public Integer getNpcCount() {
    return npcCount;
  }

  public void setNpcCount(Integer npcCount) {
    this.npcCount = npcCount;
  }

  public DistrictPopulationState capacity(Integer capacity) {
    this.capacity = capacity;
    return this;
  }

  /**
   * Get capacity
   * minimum: 0
   * @return capacity
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "capacity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("capacity")
  public Integer getCapacity() {
    return capacity;
  }

  public void setCapacity(Integer capacity) {
    this.capacity = capacity;
  }

  public DistrictPopulationState capacityUsage(@Nullable BigDecimal capacityUsage) {
    this.capacityUsage = capacityUsage;
    return this;
  }

  /**
   * Get capacityUsage
   * minimum: 0
   * maximum: 1
   * @return capacityUsage
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "capacityUsage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("capacityUsage")
  public @Nullable BigDecimal getCapacityUsage() {
    return capacityUsage;
  }

  public void setCapacityUsage(@Nullable BigDecimal capacityUsage) {
    this.capacityUsage = capacityUsage;
  }

  public DistrictPopulationState growthRate(BigDecimal growthRate) {
    this.growthRate = growthRate;
    return this;
  }

  /**
   * Get growthRate
   * @return growthRate
   */
  @NotNull @Valid 
  @Schema(name = "growthRate", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("growthRate")
  public BigDecimal getGrowthRate() {
    return growthRate;
  }

  public void setGrowthRate(BigDecimal growthRate) {
    this.growthRate = growthRate;
  }

  public DistrictPopulationState alerts(List<@Valid PopulationAlert> alerts) {
    this.alerts = alerts;
    return this;
  }

  public DistrictPopulationState addAlertsItem(PopulationAlert alertsItem) {
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
  public List<@Valid PopulationAlert> getAlerts() {
    return alerts;
  }

  public void setAlerts(List<@Valid PopulationAlert> alerts) {
    this.alerts = alerts;
  }

  public DistrictPopulationState dominantFaction(@Nullable String dominantFaction) {
    this.dominantFaction = dominantFaction;
    return this;
  }

  /**
   * Get dominantFaction
   * @return dominantFaction
   */
  
  @Schema(name = "dominantFaction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dominantFaction")
  public @Nullable String getDominantFaction() {
    return dominantFaction;
  }

  public void setDominantFaction(@Nullable String dominantFaction) {
    this.dominantFaction = dominantFaction;
  }

  public DistrictPopulationState influence(Map<String, BigDecimal> influence) {
    this.influence = influence;
    return this;
  }

  public DistrictPopulationState putInfluenceItem(String key, BigDecimal influenceItem) {
    if (this.influence == null) {
      this.influence = new HashMap<>();
    }
    this.influence.put(key, influenceItem);
    return this;
  }

  /**
   * Get influence
   * @return influence
   */
  @Valid 
  @Schema(name = "influence", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("influence")
  public Map<String, BigDecimal> getInfluence() {
    return influence;
  }

  public void setInfluence(Map<String, BigDecimal> influence) {
    this.influence = influence;
  }

  public DistrictPopulationState metrics(List<@Valid PopulationMetric> metrics) {
    this.metrics = metrics;
    return this;
  }

  public DistrictPopulationState addMetricsItem(PopulationMetric metricsItem) {
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
  @Valid 
  @Schema(name = "metrics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metrics")
  public List<@Valid PopulationMetric> getMetrics() {
    return metrics;
  }

  public void setMetrics(List<@Valid PopulationMetric> metrics) {
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
    DistrictPopulationState districtPopulationState = (DistrictPopulationState) o;
    return Objects.equals(this.districtId, districtPopulationState.districtId) &&
        Objects.equals(this.name, districtPopulationState.name) &&
        Objects.equals(this.segment, districtPopulationState.segment) &&
        Objects.equals(this.npcCount, districtPopulationState.npcCount) &&
        Objects.equals(this.capacity, districtPopulationState.capacity) &&
        Objects.equals(this.capacityUsage, districtPopulationState.capacityUsage) &&
        Objects.equals(this.growthRate, districtPopulationState.growthRate) &&
        Objects.equals(this.alerts, districtPopulationState.alerts) &&
        Objects.equals(this.dominantFaction, districtPopulationState.dominantFaction) &&
        Objects.equals(this.influence, districtPopulationState.influence) &&
        Objects.equals(this.metrics, districtPopulationState.metrics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(districtId, name, segment, npcCount, capacity, capacityUsage, growthRate, alerts, dominantFaction, influence, metrics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DistrictPopulationState {\n");
    sb.append("    districtId: ").append(toIndentedString(districtId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    segment: ").append(toIndentedString(segment)).append("\n");
    sb.append("    npcCount: ").append(toIndentedString(npcCount)).append("\n");
    sb.append("    capacity: ").append(toIndentedString(capacity)).append("\n");
    sb.append("    capacityUsage: ").append(toIndentedString(capacityUsage)).append("\n");
    sb.append("    growthRate: ").append(toIndentedString(growthRate)).append("\n");
    sb.append("    alerts: ").append(toIndentedString(alerts)).append("\n");
    sb.append("    dominantFaction: ").append(toIndentedString(dominantFaction)).append("\n");
    sb.append("    influence: ").append(toIndentedString(influence)).append("\n");
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

