package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.CrisisMitigationPlan;
import com.necpgame.worldservice.model.ImpactTrigger;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
 * PlayerOrderCrisis
 */


public class PlayerOrderCrisis {

  private UUID crisisId;

  @Valid
  private List<UUID> relatedEffectIds = new ArrayList<>();

  private String cityId;

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

  private SeverityEnum severity;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("active"),
    
    MITIGATED("mitigated"),
    
    ESCALATING("escalating"),
    
    RESOLVED("resolved");

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

  @Valid
  private List<@Valid ImpactTrigger> triggers = new ArrayList<>();

  private @Nullable CrisisMitigationPlan mitigationPlan;

  @Valid
  private Map<String, Float> metrics = new HashMap<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime resolvedAt;

  public PlayerOrderCrisis() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderCrisis(UUID crisisId, String cityId, SeverityEnum severity, StatusEnum status, OffsetDateTime startedAt) {
    this.crisisId = crisisId;
    this.cityId = cityId;
    this.severity = severity;
    this.status = status;
    this.startedAt = startedAt;
  }

  public PlayerOrderCrisis crisisId(UUID crisisId) {
    this.crisisId = crisisId;
    return this;
  }

  /**
   * Get crisisId
   * @return crisisId
   */
  @NotNull @Valid 
  @Schema(name = "crisisId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("crisisId")
  public UUID getCrisisId() {
    return crisisId;
  }

  public void setCrisisId(UUID crisisId) {
    this.crisisId = crisisId;
  }

  public PlayerOrderCrisis relatedEffectIds(List<UUID> relatedEffectIds) {
    this.relatedEffectIds = relatedEffectIds;
    return this;
  }

  public PlayerOrderCrisis addRelatedEffectIdsItem(UUID relatedEffectIdsItem) {
    if (this.relatedEffectIds == null) {
      this.relatedEffectIds = new ArrayList<>();
    }
    this.relatedEffectIds.add(relatedEffectIdsItem);
    return this;
  }

  /**
   * Get relatedEffectIds
   * @return relatedEffectIds
   */
  @Valid 
  @Schema(name = "relatedEffectIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relatedEffectIds")
  public List<UUID> getRelatedEffectIds() {
    return relatedEffectIds;
  }

  public void setRelatedEffectIds(List<UUID> relatedEffectIds) {
    this.relatedEffectIds = relatedEffectIds;
  }

  public PlayerOrderCrisis cityId(String cityId) {
    this.cityId = cityId;
    return this;
  }

  /**
   * Get cityId
   * @return cityId
   */
  @NotNull @Pattern(regexp = "^[A-Z0-9\\\\-]{3,24}$") 
  @Schema(name = "cityId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cityId")
  public String getCityId() {
    return cityId;
  }

  public void setCityId(String cityId) {
    this.cityId = cityId;
  }

  public PlayerOrderCrisis severity(SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  @NotNull 
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("severity")
  public SeverityEnum getSeverity() {
    return severity;
  }

  public void setSeverity(SeverityEnum severity) {
    this.severity = severity;
  }

  public PlayerOrderCrisis status(StatusEnum status) {
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

  public PlayerOrderCrisis triggers(List<@Valid ImpactTrigger> triggers) {
    this.triggers = triggers;
    return this;
  }

  public PlayerOrderCrisis addTriggersItem(ImpactTrigger triggersItem) {
    if (this.triggers == null) {
      this.triggers = new ArrayList<>();
    }
    this.triggers.add(triggersItem);
    return this;
  }

  /**
   * Get triggers
   * @return triggers
   */
  @Valid 
  @Schema(name = "triggers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("triggers")
  public List<@Valid ImpactTrigger> getTriggers() {
    return triggers;
  }

  public void setTriggers(List<@Valid ImpactTrigger> triggers) {
    this.triggers = triggers;
  }

  public PlayerOrderCrisis mitigationPlan(@Nullable CrisisMitigationPlan mitigationPlan) {
    this.mitigationPlan = mitigationPlan;
    return this;
  }

  /**
   * Get mitigationPlan
   * @return mitigationPlan
   */
  @Valid 
  @Schema(name = "mitigationPlan", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mitigationPlan")
  public @Nullable CrisisMitigationPlan getMitigationPlan() {
    return mitigationPlan;
  }

  public void setMitigationPlan(@Nullable CrisisMitigationPlan mitigationPlan) {
    this.mitigationPlan = mitigationPlan;
  }

  public PlayerOrderCrisis metrics(Map<String, Float> metrics) {
    this.metrics = metrics;
    return this;
  }

  public PlayerOrderCrisis putMetricsItem(String key, Float metricsItem) {
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

  public PlayerOrderCrisis startedAt(OffsetDateTime startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  /**
   * Get startedAt
   * @return startedAt
   */
  @NotNull @Valid 
  @Schema(name = "startedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("startedAt")
  public OffsetDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(OffsetDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public PlayerOrderCrisis resolvedAt(@Nullable OffsetDateTime resolvedAt) {
    this.resolvedAt = resolvedAt;
    return this;
  }

  /**
   * Get resolvedAt
   * @return resolvedAt
   */
  @Valid 
  @Schema(name = "resolvedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resolvedAt")
  public @Nullable OffsetDateTime getResolvedAt() {
    return resolvedAt;
  }

  public void setResolvedAt(@Nullable OffsetDateTime resolvedAt) {
    this.resolvedAt = resolvedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderCrisis playerOrderCrisis = (PlayerOrderCrisis) o;
    return Objects.equals(this.crisisId, playerOrderCrisis.crisisId) &&
        Objects.equals(this.relatedEffectIds, playerOrderCrisis.relatedEffectIds) &&
        Objects.equals(this.cityId, playerOrderCrisis.cityId) &&
        Objects.equals(this.severity, playerOrderCrisis.severity) &&
        Objects.equals(this.status, playerOrderCrisis.status) &&
        Objects.equals(this.triggers, playerOrderCrisis.triggers) &&
        Objects.equals(this.mitigationPlan, playerOrderCrisis.mitigationPlan) &&
        Objects.equals(this.metrics, playerOrderCrisis.metrics) &&
        Objects.equals(this.startedAt, playerOrderCrisis.startedAt) &&
        Objects.equals(this.resolvedAt, playerOrderCrisis.resolvedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(crisisId, relatedEffectIds, cityId, severity, status, triggers, mitigationPlan, metrics, startedAt, resolvedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderCrisis {\n");
    sb.append("    crisisId: ").append(toIndentedString(crisisId)).append("\n");
    sb.append("    relatedEffectIds: ").append(toIndentedString(relatedEffectIds)).append("\n");
    sb.append("    cityId: ").append(toIndentedString(cityId)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    triggers: ").append(toIndentedString(triggers)).append("\n");
    sb.append("    mitigationPlan: ").append(toIndentedString(mitigationPlan)).append("\n");
    sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    resolvedAt: ").append(toIndentedString(resolvedAt)).append("\n");
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

