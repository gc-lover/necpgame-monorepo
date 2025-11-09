package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.ImpactKpi;
import com.necpgame.worldservice.model.ImpactMagnitude;
import com.necpgame.worldservice.model.ImpactTrigger;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * PlayerOrderImpact
 */


public class PlayerOrderImpact {

  private UUID effectId;

  private @Nullable UUID orderId;

  private String cityId;

  /**
   * Gets or Sets effectType
   */
  public enum EffectTypeEnum {
    ECONOMIC("economic"),
    
    SOCIAL("social"),
    
    POLITICAL("political"),
    
    SECURITY("security"),
    
    ENVIRONMENTAL("environmental"),
    
    CULTURAL("cultural");

    private final String value;

    EffectTypeEnum(String value) {
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
    public static EffectTypeEnum fromValue(String value) {
      for (EffectTypeEnum b : EffectTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private EffectTypeEnum effectType;

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

  private ImpactMagnitude magnitude;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("active"),
    
    PENDING("pending"),
    
    RESOLVED("resolved"),
    
    ARCHIVED("archived");

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

  private @Nullable ImpactKpi kpi;

  private @Nullable UUID relatedCrisisId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime decayAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public PlayerOrderImpact() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderImpact(UUID effectId, String cityId, EffectTypeEnum effectType, SeverityEnum severity, ImpactMagnitude magnitude, StatusEnum status, OffsetDateTime createdAt) {
    this.effectId = effectId;
    this.cityId = cityId;
    this.effectType = effectType;
    this.severity = severity;
    this.magnitude = magnitude;
    this.status = status;
    this.createdAt = createdAt;
  }

  public PlayerOrderImpact effectId(UUID effectId) {
    this.effectId = effectId;
    return this;
  }

  /**
   * Get effectId
   * @return effectId
   */
  @NotNull @Valid 
  @Schema(name = "effectId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("effectId")
  public UUID getEffectId() {
    return effectId;
  }

  public void setEffectId(UUID effectId) {
    this.effectId = effectId;
  }

  public PlayerOrderImpact orderId(@Nullable UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Get orderId
   * @return orderId
   */
  @Valid 
  @Schema(name = "orderId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("orderId")
  public @Nullable UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(@Nullable UUID orderId) {
    this.orderId = orderId;
  }

  public PlayerOrderImpact cityId(String cityId) {
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

  public PlayerOrderImpact effectType(EffectTypeEnum effectType) {
    this.effectType = effectType;
    return this;
  }

  /**
   * Get effectType
   * @return effectType
   */
  @NotNull 
  @Schema(name = "effectType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("effectType")
  public EffectTypeEnum getEffectType() {
    return effectType;
  }

  public void setEffectType(EffectTypeEnum effectType) {
    this.effectType = effectType;
  }

  public PlayerOrderImpact severity(SeverityEnum severity) {
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

  public PlayerOrderImpact magnitude(ImpactMagnitude magnitude) {
    this.magnitude = magnitude;
    return this;
  }

  /**
   * Get magnitude
   * @return magnitude
   */
  @NotNull @Valid 
  @Schema(name = "magnitude", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("magnitude")
  public ImpactMagnitude getMagnitude() {
    return magnitude;
  }

  public void setMagnitude(ImpactMagnitude magnitude) {
    this.magnitude = magnitude;
  }

  public PlayerOrderImpact status(StatusEnum status) {
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

  public PlayerOrderImpact triggers(List<@Valid ImpactTrigger> triggers) {
    this.triggers = triggers;
    return this;
  }

  public PlayerOrderImpact addTriggersItem(ImpactTrigger triggersItem) {
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

  public PlayerOrderImpact kpi(@Nullable ImpactKpi kpi) {
    this.kpi = kpi;
    return this;
  }

  /**
   * Get kpi
   * @return kpi
   */
  @Valid 
  @Schema(name = "kpi", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("kpi")
  public @Nullable ImpactKpi getKpi() {
    return kpi;
  }

  public void setKpi(@Nullable ImpactKpi kpi) {
    this.kpi = kpi;
  }

  public PlayerOrderImpact relatedCrisisId(@Nullable UUID relatedCrisisId) {
    this.relatedCrisisId = relatedCrisisId;
    return this;
  }

  /**
   * Get relatedCrisisId
   * @return relatedCrisisId
   */
  @Valid 
  @Schema(name = "relatedCrisisId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relatedCrisisId")
  public @Nullable UUID getRelatedCrisisId() {
    return relatedCrisisId;
  }

  public void setRelatedCrisisId(@Nullable UUID relatedCrisisId) {
    this.relatedCrisisId = relatedCrisisId;
  }

  public PlayerOrderImpact decayAt(@Nullable OffsetDateTime decayAt) {
    this.decayAt = decayAt;
    return this;
  }

  /**
   * Get decayAt
   * @return decayAt
   */
  @Valid 
  @Schema(name = "decayAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("decayAt")
  public @Nullable OffsetDateTime getDecayAt() {
    return decayAt;
  }

  public void setDecayAt(@Nullable OffsetDateTime decayAt) {
    this.decayAt = decayAt;
  }

  public PlayerOrderImpact createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @NotNull @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public PlayerOrderImpact updatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public @Nullable OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderImpact playerOrderImpact = (PlayerOrderImpact) o;
    return Objects.equals(this.effectId, playerOrderImpact.effectId) &&
        Objects.equals(this.orderId, playerOrderImpact.orderId) &&
        Objects.equals(this.cityId, playerOrderImpact.cityId) &&
        Objects.equals(this.effectType, playerOrderImpact.effectType) &&
        Objects.equals(this.severity, playerOrderImpact.severity) &&
        Objects.equals(this.magnitude, playerOrderImpact.magnitude) &&
        Objects.equals(this.status, playerOrderImpact.status) &&
        Objects.equals(this.triggers, playerOrderImpact.triggers) &&
        Objects.equals(this.kpi, playerOrderImpact.kpi) &&
        Objects.equals(this.relatedCrisisId, playerOrderImpact.relatedCrisisId) &&
        Objects.equals(this.decayAt, playerOrderImpact.decayAt) &&
        Objects.equals(this.createdAt, playerOrderImpact.createdAt) &&
        Objects.equals(this.updatedAt, playerOrderImpact.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(effectId, orderId, cityId, effectType, severity, magnitude, status, triggers, kpi, relatedCrisisId, decayAt, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderImpact {\n");
    sb.append("    effectId: ").append(toIndentedString(effectId)).append("\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    cityId: ").append(toIndentedString(cityId)).append("\n");
    sb.append("    effectType: ").append(toIndentedString(effectType)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    magnitude: ").append(toIndentedString(magnitude)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    triggers: ").append(toIndentedString(triggers)).append("\n");
    sb.append("    kpi: ").append(toIndentedString(kpi)).append("\n");
    sb.append("    relatedCrisisId: ").append(toIndentedString(relatedCrisisId)).append("\n");
    sb.append("    decayAt: ").append(toIndentedString(decayAt)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

