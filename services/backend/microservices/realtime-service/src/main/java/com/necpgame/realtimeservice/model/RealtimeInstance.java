package com.necpgame.realtimeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.realtimeservice.model.InstanceHealth;
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
 * RealtimeInstance
 */


public class RealtimeInstance {

  private String instanceId;

  private String region;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ONLINE("ONLINE"),
    
    MAINTENANCE("MAINTENANCE"),
    
    DRAINING("DRAINING"),
    
    OFFLINE("OFFLINE");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;

  /**
   * Gets or Sets tickRate
   */
  public enum TickRateEnum {
    NUMBER_20(20),
    
    NUMBER_30(30),
    
    NUMBER_60(60);

    private final Integer value;

    TickRateEnum(Integer value) {
      this.value = value;
    }

    @JsonValue
    public Integer getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TickRateEnum fromValue(Integer value) {
      for (TickRateEnum b : TickRateEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TickRateEnum tickRate;

  private Integer maxZones;

  private Integer maxPlayers;

  private Integer currentPlayers;

  private @Nullable BigDecimal cpuLoadPercent;

  private @Nullable Integer memoryUsageMb;

  @Valid
  private List<String> tags = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startedAt;

  @Valid
  private List<String> activeZones = new ArrayList<>();

  private @Nullable InstanceHealth health;

  public RealtimeInstance() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RealtimeInstance(String instanceId, String region, StatusEnum status, TickRateEnum tickRate, Integer maxZones, Integer maxPlayers, Integer currentPlayers) {
    this.instanceId = instanceId;
    this.region = region;
    this.status = status;
    this.tickRate = tickRate;
    this.maxZones = maxZones;
    this.maxPlayers = maxPlayers;
    this.currentPlayers = currentPlayers;
  }

  public RealtimeInstance instanceId(String instanceId) {
    this.instanceId = instanceId;
    return this;
  }

  /**
   * Get instanceId
   * @return instanceId
   */
  @NotNull 
  @Schema(name = "instanceId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("instanceId")
  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  public RealtimeInstance region(String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  @NotNull 
  @Schema(name = "region", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("region")
  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public RealtimeInstance status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public RealtimeInstance tickRate(TickRateEnum tickRate) {
    this.tickRate = tickRate;
    return this;
  }

  /**
   * Get tickRate
   * @return tickRate
   */
  @NotNull 
  @Schema(name = "tickRate", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tickRate")
  public TickRateEnum getTickRate() {
    return tickRate;
  }

  public void setTickRate(TickRateEnum tickRate) {
    this.tickRate = tickRate;
  }

  public RealtimeInstance maxZones(Integer maxZones) {
    this.maxZones = maxZones;
    return this;
  }

  /**
   * Get maxZones
   * minimum: 1
   * maximum: 12
   * @return maxZones
   */
  @NotNull @Min(value = 1) @Max(value = 12) 
  @Schema(name = "maxZones", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maxZones")
  public Integer getMaxZones() {
    return maxZones;
  }

  public void setMaxZones(Integer maxZones) {
    this.maxZones = maxZones;
  }

  public RealtimeInstance maxPlayers(Integer maxPlayers) {
    this.maxPlayers = maxPlayers;
    return this;
  }

  /**
   * Get maxPlayers
   * minimum: 100
   * maximum: 2000
   * @return maxPlayers
   */
  @NotNull @Min(value = 100) @Max(value = 2000) 
  @Schema(name = "maxPlayers", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maxPlayers")
  public Integer getMaxPlayers() {
    return maxPlayers;
  }

  public void setMaxPlayers(Integer maxPlayers) {
    this.maxPlayers = maxPlayers;
  }

  public RealtimeInstance currentPlayers(Integer currentPlayers) {
    this.currentPlayers = currentPlayers;
    return this;
  }

  /**
   * Get currentPlayers
   * minimum: 0
   * @return currentPlayers
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "currentPlayers", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("currentPlayers")
  public Integer getCurrentPlayers() {
    return currentPlayers;
  }

  public void setCurrentPlayers(Integer currentPlayers) {
    this.currentPlayers = currentPlayers;
  }

  public RealtimeInstance cpuLoadPercent(@Nullable BigDecimal cpuLoadPercent) {
    this.cpuLoadPercent = cpuLoadPercent;
    return this;
  }

  /**
   * Get cpuLoadPercent
   * minimum: 0
   * maximum: 100
   * @return cpuLoadPercent
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "cpuLoadPercent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cpuLoadPercent")
  public @Nullable BigDecimal getCpuLoadPercent() {
    return cpuLoadPercent;
  }

  public void setCpuLoadPercent(@Nullable BigDecimal cpuLoadPercent) {
    this.cpuLoadPercent = cpuLoadPercent;
  }

  public RealtimeInstance memoryUsageMb(@Nullable Integer memoryUsageMb) {
    this.memoryUsageMb = memoryUsageMb;
    return this;
  }

  /**
   * Get memoryUsageMb
   * @return memoryUsageMb
   */
  
  @Schema(name = "memoryUsageMb", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("memoryUsageMb")
  public @Nullable Integer getMemoryUsageMb() {
    return memoryUsageMb;
  }

  public void setMemoryUsageMb(@Nullable Integer memoryUsageMb) {
    this.memoryUsageMb = memoryUsageMb;
  }

  public RealtimeInstance tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public RealtimeInstance addTagsItem(String tagsItem) {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tagsItem);
    return this;
  }

  /**
   * Get tags
   * @return tags
   */
  
  @Schema(name = "tags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tags")
  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public RealtimeInstance startedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  /**
   * Get startedAt
   * @return startedAt
   */
  @Valid 
  @Schema(name = "startedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startedAt")
  public @Nullable OffsetDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public RealtimeInstance activeZones(List<String> activeZones) {
    this.activeZones = activeZones;
    return this;
  }

  public RealtimeInstance addActiveZonesItem(String activeZonesItem) {
    if (this.activeZones == null) {
      this.activeZones = new ArrayList<>();
    }
    this.activeZones.add(activeZonesItem);
    return this;
  }

  /**
   * Get activeZones
   * @return activeZones
   */
  
  @Schema(name = "activeZones", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activeZones")
  public List<String> getActiveZones() {
    return activeZones;
  }

  public void setActiveZones(List<String> activeZones) {
    this.activeZones = activeZones;
  }

  public RealtimeInstance health(@Nullable InstanceHealth health) {
    this.health = health;
    return this;
  }

  /**
   * Get health
   * @return health
   */
  @Valid 
  @Schema(name = "health", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("health")
  public @Nullable InstanceHealth getHealth() {
    return health;
  }

  public void setHealth(@Nullable InstanceHealth health) {
    this.health = health;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RealtimeInstance realtimeInstance = (RealtimeInstance) o;
    return Objects.equals(this.instanceId, realtimeInstance.instanceId) &&
        Objects.equals(this.region, realtimeInstance.region) &&
        Objects.equals(this.status, realtimeInstance.status) &&
        Objects.equals(this.tickRate, realtimeInstance.tickRate) &&
        Objects.equals(this.maxZones, realtimeInstance.maxZones) &&
        Objects.equals(this.maxPlayers, realtimeInstance.maxPlayers) &&
        Objects.equals(this.currentPlayers, realtimeInstance.currentPlayers) &&
        Objects.equals(this.cpuLoadPercent, realtimeInstance.cpuLoadPercent) &&
        Objects.equals(this.memoryUsageMb, realtimeInstance.memoryUsageMb) &&
        Objects.equals(this.tags, realtimeInstance.tags) &&
        Objects.equals(this.startedAt, realtimeInstance.startedAt) &&
        Objects.equals(this.activeZones, realtimeInstance.activeZones) &&
        Objects.equals(this.health, realtimeInstance.health);
  }

  @Override
  public int hashCode() {
    return Objects.hash(instanceId, region, status, tickRate, maxZones, maxPlayers, currentPlayers, cpuLoadPercent, memoryUsageMb, tags, startedAt, activeZones, health);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RealtimeInstance {\n");
    sb.append("    instanceId: ").append(toIndentedString(instanceId)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    tickRate: ").append(toIndentedString(tickRate)).append("\n");
    sb.append("    maxZones: ").append(toIndentedString(maxZones)).append("\n");
    sb.append("    maxPlayers: ").append(toIndentedString(maxPlayers)).append("\n");
    sb.append("    currentPlayers: ").append(toIndentedString(currentPlayers)).append("\n");
    sb.append("    cpuLoadPercent: ").append(toIndentedString(cpuLoadPercent)).append("\n");
    sb.append("    memoryUsageMb: ").append(toIndentedString(memoryUsageMb)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    activeZones: ").append(toIndentedString(activeZones)).append("\n");
    sb.append("    health: ").append(toIndentedString(health)).append("\n");
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

