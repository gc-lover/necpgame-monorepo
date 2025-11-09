package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.ChronicleEventRef;
import com.necpgame.worldservice.model.FactionControlInfluenceBreakdownInner;
import java.math.BigDecimal;
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
 * FactionControl
 */


public class FactionControl {

  private UUID factionId;

  private UUID regionId;

  private Integer controlScore;

  private UUID ownerFactionId;

  private @Nullable BigDecimal stabilityIndex;

  /**
   * Gets or Sets trend
   */
  public enum TrendEnum {
    RISING("rising"),
    
    STABLE("stable"),
    
    FALLING("falling");

    private final String value;

    TrendEnum(String value) {
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
    public static TrendEnum fromValue(String value) {
      for (TrendEnum b : TrendEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TrendEnum trend;

  @Valid
  private List<@Valid ChronicleEventRef> pendingEvents = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime lastChange;

  private @Nullable Integer conflictLevel;

  @Valid
  private List<@Valid FactionControlInfluenceBreakdownInner> influenceBreakdown = new ArrayList<>();

  public FactionControl() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FactionControl(UUID factionId, UUID regionId, Integer controlScore, UUID ownerFactionId, List<@Valid ChronicleEventRef> pendingEvents, OffsetDateTime lastChange) {
    this.factionId = factionId;
    this.regionId = regionId;
    this.controlScore = controlScore;
    this.ownerFactionId = ownerFactionId;
    this.pendingEvents = pendingEvents;
    this.lastChange = lastChange;
  }

  public FactionControl factionId(UUID factionId) {
    this.factionId = factionId;
    return this;
  }

  /**
   * Get factionId
   * @return factionId
   */
  @NotNull @Valid 
  @Schema(name = "factionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("factionId")
  public UUID getFactionId() {
    return factionId;
  }

  public void setFactionId(UUID factionId) {
    this.factionId = factionId;
  }

  public FactionControl regionId(UUID regionId) {
    this.regionId = regionId;
    return this;
  }

  /**
   * Get regionId
   * @return regionId
   */
  @NotNull @Valid 
  @Schema(name = "regionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("regionId")
  public UUID getRegionId() {
    return regionId;
  }

  public void setRegionId(UUID regionId) {
    this.regionId = regionId;
  }

  public FactionControl controlScore(Integer controlScore) {
    this.controlScore = controlScore;
    return this;
  }

  /**
   * Get controlScore
   * minimum: -100
   * maximum: 100
   * @return controlScore
   */
  @NotNull @Min(value = -100) @Max(value = 100) 
  @Schema(name = "controlScore", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("controlScore")
  public Integer getControlScore() {
    return controlScore;
  }

  public void setControlScore(Integer controlScore) {
    this.controlScore = controlScore;
  }

  public FactionControl ownerFactionId(UUID ownerFactionId) {
    this.ownerFactionId = ownerFactionId;
    return this;
  }

  /**
   * Get ownerFactionId
   * @return ownerFactionId
   */
  @NotNull @Valid 
  @Schema(name = "ownerFactionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ownerFactionId")
  public UUID getOwnerFactionId() {
    return ownerFactionId;
  }

  public void setOwnerFactionId(UUID ownerFactionId) {
    this.ownerFactionId = ownerFactionId;
  }

  public FactionControl stabilityIndex(@Nullable BigDecimal stabilityIndex) {
    this.stabilityIndex = stabilityIndex;
    return this;
  }

  /**
   * Get stabilityIndex
   * minimum: 0
   * maximum: 1
   * @return stabilityIndex
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "stabilityIndex", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stabilityIndex")
  public @Nullable BigDecimal getStabilityIndex() {
    return stabilityIndex;
  }

  public void setStabilityIndex(@Nullable BigDecimal stabilityIndex) {
    this.stabilityIndex = stabilityIndex;
  }

  public FactionControl trend(@Nullable TrendEnum trend) {
    this.trend = trend;
    return this;
  }

  /**
   * Get trend
   * @return trend
   */
  
  @Schema(name = "trend", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trend")
  public @Nullable TrendEnum getTrend() {
    return trend;
  }

  public void setTrend(@Nullable TrendEnum trend) {
    this.trend = trend;
  }

  public FactionControl pendingEvents(List<@Valid ChronicleEventRef> pendingEvents) {
    this.pendingEvents = pendingEvents;
    return this;
  }

  public FactionControl addPendingEventsItem(ChronicleEventRef pendingEventsItem) {
    if (this.pendingEvents == null) {
      this.pendingEvents = new ArrayList<>();
    }
    this.pendingEvents.add(pendingEventsItem);
    return this;
  }

  /**
   * Get pendingEvents
   * @return pendingEvents
   */
  @NotNull @Valid 
  @Schema(name = "pendingEvents", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("pendingEvents")
  public List<@Valid ChronicleEventRef> getPendingEvents() {
    return pendingEvents;
  }

  public void setPendingEvents(List<@Valid ChronicleEventRef> pendingEvents) {
    this.pendingEvents = pendingEvents;
  }

  public FactionControl lastChange(OffsetDateTime lastChange) {
    this.lastChange = lastChange;
    return this;
  }

  /**
   * Get lastChange
   * @return lastChange
   */
  @NotNull @Valid 
  @Schema(name = "lastChange", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lastChange")
  public OffsetDateTime getLastChange() {
    return lastChange;
  }

  public void setLastChange(OffsetDateTime lastChange) {
    this.lastChange = lastChange;
  }

  public FactionControl conflictLevel(@Nullable Integer conflictLevel) {
    this.conflictLevel = conflictLevel;
    return this;
  }

  /**
   * Get conflictLevel
   * minimum: 0
   * maximum: 5
   * @return conflictLevel
   */
  @Min(value = 0) @Max(value = 5) 
  @Schema(name = "conflictLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conflictLevel")
  public @Nullable Integer getConflictLevel() {
    return conflictLevel;
  }

  public void setConflictLevel(@Nullable Integer conflictLevel) {
    this.conflictLevel = conflictLevel;
  }

  public FactionControl influenceBreakdown(List<@Valid FactionControlInfluenceBreakdownInner> influenceBreakdown) {
    this.influenceBreakdown = influenceBreakdown;
    return this;
  }

  public FactionControl addInfluenceBreakdownItem(FactionControlInfluenceBreakdownInner influenceBreakdownItem) {
    if (this.influenceBreakdown == null) {
      this.influenceBreakdown = new ArrayList<>();
    }
    this.influenceBreakdown.add(influenceBreakdownItem);
    return this;
  }

  /**
   * Get influenceBreakdown
   * @return influenceBreakdown
   */
  @Valid 
  @Schema(name = "influenceBreakdown", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("influenceBreakdown")
  public List<@Valid FactionControlInfluenceBreakdownInner> getInfluenceBreakdown() {
    return influenceBreakdown;
  }

  public void setInfluenceBreakdown(List<@Valid FactionControlInfluenceBreakdownInner> influenceBreakdown) {
    this.influenceBreakdown = influenceBreakdown;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FactionControl factionControl = (FactionControl) o;
    return Objects.equals(this.factionId, factionControl.factionId) &&
        Objects.equals(this.regionId, factionControl.regionId) &&
        Objects.equals(this.controlScore, factionControl.controlScore) &&
        Objects.equals(this.ownerFactionId, factionControl.ownerFactionId) &&
        Objects.equals(this.stabilityIndex, factionControl.stabilityIndex) &&
        Objects.equals(this.trend, factionControl.trend) &&
        Objects.equals(this.pendingEvents, factionControl.pendingEvents) &&
        Objects.equals(this.lastChange, factionControl.lastChange) &&
        Objects.equals(this.conflictLevel, factionControl.conflictLevel) &&
        Objects.equals(this.influenceBreakdown, factionControl.influenceBreakdown);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factionId, regionId, controlScore, ownerFactionId, stabilityIndex, trend, pendingEvents, lastChange, conflictLevel, influenceBreakdown);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FactionControl {\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    regionId: ").append(toIndentedString(regionId)).append("\n");
    sb.append("    controlScore: ").append(toIndentedString(controlScore)).append("\n");
    sb.append("    ownerFactionId: ").append(toIndentedString(ownerFactionId)).append("\n");
    sb.append("    stabilityIndex: ").append(toIndentedString(stabilityIndex)).append("\n");
    sb.append("    trend: ").append(toIndentedString(trend)).append("\n");
    sb.append("    pendingEvents: ").append(toIndentedString(pendingEvents)).append("\n");
    sb.append("    lastChange: ").append(toIndentedString(lastChange)).append("\n");
    sb.append("    conflictLevel: ").append(toIndentedString(conflictLevel)).append("\n");
    sb.append("    influenceBreakdown: ").append(toIndentedString(influenceBreakdown)).append("\n");
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

