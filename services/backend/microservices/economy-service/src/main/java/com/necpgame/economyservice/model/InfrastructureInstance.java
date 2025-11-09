package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.InfrastructureAlert;
import com.necpgame.economyservice.model.InfrastructureMetric;
import com.necpgame.economyservice.model.MaintenanceSchedule;
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
 * InfrastructureInstance
 */


public class InfrastructureInstance {

  private UUID instanceId;

  private UUID districtId;

  private @Nullable String districtName;

  /**
   * Gets or Sets category
   */
  public enum CategoryEnum {
    HOUSING("housing"),
    
    TRANSIT("transit"),
    
    SECURITY("security"),
    
    ENTERTAINMENT("entertainment"),
    
    MEDICAL("medical"),
    
    BLACK_MARKET("black_market"),
    
    CIVIC("civic");

    private final String value;

    CategoryEnum(String value) {
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
    public static CategoryEnum fromValue(String value) {
      for (CategoryEnum b : CategoryEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private CategoryEnum category;

  private @Nullable String templateId;

  private @Nullable String name;

  /**
   * Gets or Sets state
   */
  public enum StateEnum {
    PLANNED("planned"),
    
    ACTIVE("active"),
    
    DEGRADED("degraded"),
    
    OFFLINE("offline");

    private final String value;

    StateEnum(String value) {
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
    public static StateEnum fromValue(String value) {
      for (StateEnum b : StateEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StateEnum state;

  private Integer capacity;

  private BigDecimal utilization;

  @Valid
  private Map<String, Integer> requiredStaff = new HashMap<>();

  private @Nullable Integer energyCost;

  @Valid
  private Map<String, String> openHours = new HashMap<>();

  private @Nullable String ownerFaction;

  /**
   * Gets or Sets riskLevel
   */
  public enum RiskLevelEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high");

    private final String value;

    RiskLevelEnum(String value) {
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
    public static RiskLevelEnum fromValue(String value) {
      for (RiskLevelEnum b : RiskLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RiskLevelEnum riskLevel;

  @Valid
  private List<@Valid InfrastructureAlert> alerts = new ArrayList<>();

  @Valid
  private List<@Valid InfrastructureMetric> metrics = new ArrayList<>();

  private @Nullable MaintenanceSchedule maintenance;

  public InfrastructureInstance() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InfrastructureInstance(UUID instanceId, UUID districtId, CategoryEnum category, StateEnum state, Integer capacity, BigDecimal utilization) {
    this.instanceId = instanceId;
    this.districtId = districtId;
    this.category = category;
    this.state = state;
    this.capacity = capacity;
    this.utilization = utilization;
  }

  public InfrastructureInstance instanceId(UUID instanceId) {
    this.instanceId = instanceId;
    return this;
  }

  /**
   * Get instanceId
   * @return instanceId
   */
  @NotNull @Valid 
  @Schema(name = "instanceId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("instanceId")
  public UUID getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(UUID instanceId) {
    this.instanceId = instanceId;
  }

  public InfrastructureInstance districtId(UUID districtId) {
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

  public InfrastructureInstance districtName(@Nullable String districtName) {
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

  public InfrastructureInstance category(CategoryEnum category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  @NotNull 
  @Schema(name = "category", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("category")
  public CategoryEnum getCategory() {
    return category;
  }

  public void setCategory(CategoryEnum category) {
    this.category = category;
  }

  public InfrastructureInstance templateId(@Nullable String templateId) {
    this.templateId = templateId;
    return this;
  }

  /**
   * Get templateId
   * @return templateId
   */
  
  @Schema(name = "templateId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("templateId")
  public @Nullable String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(@Nullable String templateId) {
    this.templateId = templateId;
  }

  public InfrastructureInstance name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public InfrastructureInstance state(StateEnum state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * @return state
   */
  @NotNull 
  @Schema(name = "state", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("state")
  public StateEnum getState() {
    return state;
  }

  public void setState(StateEnum state) {
    this.state = state;
  }

  public InfrastructureInstance capacity(Integer capacity) {
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

  public InfrastructureInstance utilization(BigDecimal utilization) {
    this.utilization = utilization;
    return this;
  }

  /**
   * Get utilization
   * minimum: 0
   * @return utilization
   */
  @NotNull @Valid @DecimalMin(value = "0") 
  @Schema(name = "utilization", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("utilization")
  public BigDecimal getUtilization() {
    return utilization;
  }

  public void setUtilization(BigDecimal utilization) {
    this.utilization = utilization;
  }

  public InfrastructureInstance requiredStaff(Map<String, Integer> requiredStaff) {
    this.requiredStaff = requiredStaff;
    return this;
  }

  public InfrastructureInstance putRequiredStaffItem(String key, Integer requiredStaffItem) {
    if (this.requiredStaff == null) {
      this.requiredStaff = new HashMap<>();
    }
    this.requiredStaff.put(key, requiredStaffItem);
    return this;
  }

  /**
   * Get requiredStaff
   * @return requiredStaff
   */
  
  @Schema(name = "requiredStaff", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requiredStaff")
  public Map<String, Integer> getRequiredStaff() {
    return requiredStaff;
  }

  public void setRequiredStaff(Map<String, Integer> requiredStaff) {
    this.requiredStaff = requiredStaff;
  }

  public InfrastructureInstance energyCost(@Nullable Integer energyCost) {
    this.energyCost = energyCost;
    return this;
  }

  /**
   * Get energyCost
   * @return energyCost
   */
  
  @Schema(name = "energyCost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energyCost")
  public @Nullable Integer getEnergyCost() {
    return energyCost;
  }

  public void setEnergyCost(@Nullable Integer energyCost) {
    this.energyCost = energyCost;
  }

  public InfrastructureInstance openHours(Map<String, String> openHours) {
    this.openHours = openHours;
    return this;
  }

  public InfrastructureInstance putOpenHoursItem(String key, String openHoursItem) {
    if (this.openHours == null) {
      this.openHours = new HashMap<>();
    }
    this.openHours.put(key, openHoursItem);
    return this;
  }

  /**
   * Get openHours
   * @return openHours
   */
  
  @Schema(name = "openHours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("openHours")
  public Map<String, String> getOpenHours() {
    return openHours;
  }

  public void setOpenHours(Map<String, String> openHours) {
    this.openHours = openHours;
  }

  public InfrastructureInstance ownerFaction(@Nullable String ownerFaction) {
    this.ownerFaction = ownerFaction;
    return this;
  }

  /**
   * Get ownerFaction
   * @return ownerFaction
   */
  
  @Schema(name = "ownerFaction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ownerFaction")
  public @Nullable String getOwnerFaction() {
    return ownerFaction;
  }

  public void setOwnerFaction(@Nullable String ownerFaction) {
    this.ownerFaction = ownerFaction;
  }

  public InfrastructureInstance riskLevel(@Nullable RiskLevelEnum riskLevel) {
    this.riskLevel = riskLevel;
    return this;
  }

  /**
   * Get riskLevel
   * @return riskLevel
   */
  
  @Schema(name = "riskLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("riskLevel")
  public @Nullable RiskLevelEnum getRiskLevel() {
    return riskLevel;
  }

  public void setRiskLevel(@Nullable RiskLevelEnum riskLevel) {
    this.riskLevel = riskLevel;
  }

  public InfrastructureInstance alerts(List<@Valid InfrastructureAlert> alerts) {
    this.alerts = alerts;
    return this;
  }

  public InfrastructureInstance addAlertsItem(InfrastructureAlert alertsItem) {
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

  public InfrastructureInstance metrics(List<@Valid InfrastructureMetric> metrics) {
    this.metrics = metrics;
    return this;
  }

  public InfrastructureInstance addMetricsItem(InfrastructureMetric metricsItem) {
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
  public List<@Valid InfrastructureMetric> getMetrics() {
    return metrics;
  }

  public void setMetrics(List<@Valid InfrastructureMetric> metrics) {
    this.metrics = metrics;
  }

  public InfrastructureInstance maintenance(@Nullable MaintenanceSchedule maintenance) {
    this.maintenance = maintenance;
    return this;
  }

  /**
   * Get maintenance
   * @return maintenance
   */
  @Valid 
  @Schema(name = "maintenance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maintenance")
  public @Nullable MaintenanceSchedule getMaintenance() {
    return maintenance;
  }

  public void setMaintenance(@Nullable MaintenanceSchedule maintenance) {
    this.maintenance = maintenance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InfrastructureInstance infrastructureInstance = (InfrastructureInstance) o;
    return Objects.equals(this.instanceId, infrastructureInstance.instanceId) &&
        Objects.equals(this.districtId, infrastructureInstance.districtId) &&
        Objects.equals(this.districtName, infrastructureInstance.districtName) &&
        Objects.equals(this.category, infrastructureInstance.category) &&
        Objects.equals(this.templateId, infrastructureInstance.templateId) &&
        Objects.equals(this.name, infrastructureInstance.name) &&
        Objects.equals(this.state, infrastructureInstance.state) &&
        Objects.equals(this.capacity, infrastructureInstance.capacity) &&
        Objects.equals(this.utilization, infrastructureInstance.utilization) &&
        Objects.equals(this.requiredStaff, infrastructureInstance.requiredStaff) &&
        Objects.equals(this.energyCost, infrastructureInstance.energyCost) &&
        Objects.equals(this.openHours, infrastructureInstance.openHours) &&
        Objects.equals(this.ownerFaction, infrastructureInstance.ownerFaction) &&
        Objects.equals(this.riskLevel, infrastructureInstance.riskLevel) &&
        Objects.equals(this.alerts, infrastructureInstance.alerts) &&
        Objects.equals(this.metrics, infrastructureInstance.metrics) &&
        Objects.equals(this.maintenance, infrastructureInstance.maintenance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(instanceId, districtId, districtName, category, templateId, name, state, capacity, utilization, requiredStaff, energyCost, openHours, ownerFaction, riskLevel, alerts, metrics, maintenance);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InfrastructureInstance {\n");
    sb.append("    instanceId: ").append(toIndentedString(instanceId)).append("\n");
    sb.append("    districtId: ").append(toIndentedString(districtId)).append("\n");
    sb.append("    districtName: ").append(toIndentedString(districtName)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    capacity: ").append(toIndentedString(capacity)).append("\n");
    sb.append("    utilization: ").append(toIndentedString(utilization)).append("\n");
    sb.append("    requiredStaff: ").append(toIndentedString(requiredStaff)).append("\n");
    sb.append("    energyCost: ").append(toIndentedString(energyCost)).append("\n");
    sb.append("    openHours: ").append(toIndentedString(openHours)).append("\n");
    sb.append("    ownerFaction: ").append(toIndentedString(ownerFaction)).append("\n");
    sb.append("    riskLevel: ").append(toIndentedString(riskLevel)).append("\n");
    sb.append("    alerts: ").append(toIndentedString(alerts)).append("\n");
    sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
    sb.append("    maintenance: ").append(toIndentedString(maintenance)).append("\n");
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

