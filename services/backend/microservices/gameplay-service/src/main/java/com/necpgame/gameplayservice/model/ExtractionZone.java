package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ExtractionZone
 */


public class ExtractionZone {

  private @Nullable String zoneId;

  private @Nullable String name;

  /**
   * Gets or Sets riskLevel
   */
  public enum RiskLevelEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high"),
    
    EXTREME("extreme");

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

  private @Nullable BigDecimal timeLimit;

  private @Nullable String lootQuality;

  private @Nullable String factionControl;

  public ExtractionZone zoneId(@Nullable String zoneId) {
    this.zoneId = zoneId;
    return this;
  }

  /**
   * Get zoneId
   * @return zoneId
   */
  
  @Schema(name = "zone_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zone_id")
  public @Nullable String getZoneId() {
    return zoneId;
  }

  public void setZoneId(@Nullable String zoneId) {
    this.zoneId = zoneId;
  }

  public ExtractionZone name(@Nullable String name) {
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

  public ExtractionZone riskLevel(@Nullable RiskLevelEnum riskLevel) {
    this.riskLevel = riskLevel;
    return this;
  }

  /**
   * Get riskLevel
   * @return riskLevel
   */
  
  @Schema(name = "risk_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("risk_level")
  public @Nullable RiskLevelEnum getRiskLevel() {
    return riskLevel;
  }

  public void setRiskLevel(@Nullable RiskLevelEnum riskLevel) {
    this.riskLevel = riskLevel;
  }

  public ExtractionZone timeLimit(@Nullable BigDecimal timeLimit) {
    this.timeLimit = timeLimit;
    return this;
  }

  /**
   * Лимит времени в зоне (минуты)
   * @return timeLimit
   */
  @Valid 
  @Schema(name = "time_limit", description = "Лимит времени в зоне (минуты)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_limit")
  public @Nullable BigDecimal getTimeLimit() {
    return timeLimit;
  }

  public void setTimeLimit(@Nullable BigDecimal timeLimit) {
    this.timeLimit = timeLimit;
  }

  public ExtractionZone lootQuality(@Nullable String lootQuality) {
    this.lootQuality = lootQuality;
    return this;
  }

  /**
   * Качество лута в зоне
   * @return lootQuality
   */
  
  @Schema(name = "loot_quality", description = "Качество лута в зоне", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loot_quality")
  public @Nullable String getLootQuality() {
    return lootQuality;
  }

  public void setLootQuality(@Nullable String lootQuality) {
    this.lootQuality = lootQuality;
  }

  public ExtractionZone factionControl(@Nullable String factionControl) {
    this.factionControl = factionControl;
    return this;
  }

  /**
   * Контролирующая фракция
   * @return factionControl
   */
  
  @Schema(name = "faction_control", description = "Контролирующая фракция", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_control")
  public @Nullable String getFactionControl() {
    return factionControl;
  }

  public void setFactionControl(@Nullable String factionControl) {
    this.factionControl = factionControl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExtractionZone extractionZone = (ExtractionZone) o;
    return Objects.equals(this.zoneId, extractionZone.zoneId) &&
        Objects.equals(this.name, extractionZone.name) &&
        Objects.equals(this.riskLevel, extractionZone.riskLevel) &&
        Objects.equals(this.timeLimit, extractionZone.timeLimit) &&
        Objects.equals(this.lootQuality, extractionZone.lootQuality) &&
        Objects.equals(this.factionControl, extractionZone.factionControl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(zoneId, name, riskLevel, timeLimit, lootQuality, factionControl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExtractionZone {\n");
    sb.append("    zoneId: ").append(toIndentedString(zoneId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    riskLevel: ").append(toIndentedString(riskLevel)).append("\n");
    sb.append("    timeLimit: ").append(toIndentedString(timeLimit)).append("\n");
    sb.append("    lootQuality: ").append(toIndentedString(lootQuality)).append("\n");
    sb.append("    factionControl: ").append(toIndentedString(factionControl)).append("\n");
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

