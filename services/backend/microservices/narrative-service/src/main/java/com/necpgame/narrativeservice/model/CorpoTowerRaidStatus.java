package com.necpgame.narrativeservice.model;

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
 * CorpoTowerRaidStatus
 */


public class CorpoTowerRaidStatus {

  private @Nullable String raidId;

  private @Nullable String targetCorporation;

  /**
   * Gets or Sets phase
   */
  public enum PhaseEnum {
    INFILTRATION("infiltration"),
    
    COMBAT_FLOORS("combat_floors"),
    
    CEO_BOSS("ceo_boss");

    private final String value;

    PhaseEnum(String value) {
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
    public static PhaseEnum fromValue(String value) {
      for (PhaseEnum b : PhaseEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PhaseEnum phase;

  private @Nullable Integer currentFloor;

  private @Nullable Integer totalFloors;

  private @Nullable BigDecimal phaseProgress;

  private @Nullable Integer playersAlive;

  private @Nullable Integer playersTotal;

  /**
   * Gets or Sets approachUsed
   */
  public enum ApproachUsedEnum {
    STEALTH("stealth"),
    
    COMBAT("combat"),
    
    MIXED("mixed");

    private final String value;

    ApproachUsedEnum(String value) {
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
    public static ApproachUsedEnum fromValue(String value) {
      for (ApproachUsedEnum b : ApproachUsedEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ApproachUsedEnum approachUsed;

  private @Nullable Integer enemiesDefeated;

  private @Nullable BigDecimal timeElapsed;

  public CorpoTowerRaidStatus raidId(@Nullable String raidId) {
    this.raidId = raidId;
    return this;
  }

  /**
   * Get raidId
   * @return raidId
   */
  
  @Schema(name = "raid_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("raid_id")
  public @Nullable String getRaidId() {
    return raidId;
  }

  public void setRaidId(@Nullable String raidId) {
    this.raidId = raidId;
  }

  public CorpoTowerRaidStatus targetCorporation(@Nullable String targetCorporation) {
    this.targetCorporation = targetCorporation;
    return this;
  }

  /**
   * Get targetCorporation
   * @return targetCorporation
   */
  
  @Schema(name = "target_corporation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_corporation")
  public @Nullable String getTargetCorporation() {
    return targetCorporation;
  }

  public void setTargetCorporation(@Nullable String targetCorporation) {
    this.targetCorporation = targetCorporation;
  }

  public CorpoTowerRaidStatus phase(@Nullable PhaseEnum phase) {
    this.phase = phase;
    return this;
  }

  /**
   * Get phase
   * @return phase
   */
  
  @Schema(name = "phase", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phase")
  public @Nullable PhaseEnum getPhase() {
    return phase;
  }

  public void setPhase(@Nullable PhaseEnum phase) {
    this.phase = phase;
  }

  public CorpoTowerRaidStatus currentFloor(@Nullable Integer currentFloor) {
    this.currentFloor = currentFloor;
    return this;
  }

  /**
   * Get currentFloor
   * @return currentFloor
   */
  
  @Schema(name = "current_floor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_floor")
  public @Nullable Integer getCurrentFloor() {
    return currentFloor;
  }

  public void setCurrentFloor(@Nullable Integer currentFloor) {
    this.currentFloor = currentFloor;
  }

  public CorpoTowerRaidStatus totalFloors(@Nullable Integer totalFloors) {
    this.totalFloors = totalFloors;
    return this;
  }

  /**
   * Get totalFloors
   * @return totalFloors
   */
  
  @Schema(name = "total_floors", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_floors")
  public @Nullable Integer getTotalFloors() {
    return totalFloors;
  }

  public void setTotalFloors(@Nullable Integer totalFloors) {
    this.totalFloors = totalFloors;
  }

  public CorpoTowerRaidStatus phaseProgress(@Nullable BigDecimal phaseProgress) {
    this.phaseProgress = phaseProgress;
    return this;
  }

  /**
   * Get phaseProgress
   * @return phaseProgress
   */
  @Valid 
  @Schema(name = "phase_progress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phase_progress")
  public @Nullable BigDecimal getPhaseProgress() {
    return phaseProgress;
  }

  public void setPhaseProgress(@Nullable BigDecimal phaseProgress) {
    this.phaseProgress = phaseProgress;
  }

  public CorpoTowerRaidStatus playersAlive(@Nullable Integer playersAlive) {
    this.playersAlive = playersAlive;
    return this;
  }

  /**
   * Get playersAlive
   * @return playersAlive
   */
  
  @Schema(name = "players_alive", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("players_alive")
  public @Nullable Integer getPlayersAlive() {
    return playersAlive;
  }

  public void setPlayersAlive(@Nullable Integer playersAlive) {
    this.playersAlive = playersAlive;
  }

  public CorpoTowerRaidStatus playersTotal(@Nullable Integer playersTotal) {
    this.playersTotal = playersTotal;
    return this;
  }

  /**
   * Get playersTotal
   * @return playersTotal
   */
  
  @Schema(name = "players_total", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("players_total")
  public @Nullable Integer getPlayersTotal() {
    return playersTotal;
  }

  public void setPlayersTotal(@Nullable Integer playersTotal) {
    this.playersTotal = playersTotal;
  }

  public CorpoTowerRaidStatus approachUsed(@Nullable ApproachUsedEnum approachUsed) {
    this.approachUsed = approachUsed;
    return this;
  }

  /**
   * Get approachUsed
   * @return approachUsed
   */
  
  @Schema(name = "approach_used", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("approach_used")
  public @Nullable ApproachUsedEnum getApproachUsed() {
    return approachUsed;
  }

  public void setApproachUsed(@Nullable ApproachUsedEnum approachUsed) {
    this.approachUsed = approachUsed;
  }

  public CorpoTowerRaidStatus enemiesDefeated(@Nullable Integer enemiesDefeated) {
    this.enemiesDefeated = enemiesDefeated;
    return this;
  }

  /**
   * Get enemiesDefeated
   * @return enemiesDefeated
   */
  
  @Schema(name = "enemies_defeated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enemies_defeated")
  public @Nullable Integer getEnemiesDefeated() {
    return enemiesDefeated;
  }

  public void setEnemiesDefeated(@Nullable Integer enemiesDefeated) {
    this.enemiesDefeated = enemiesDefeated;
  }

  public CorpoTowerRaidStatus timeElapsed(@Nullable BigDecimal timeElapsed) {
    this.timeElapsed = timeElapsed;
    return this;
  }

  /**
   * Get timeElapsed
   * @return timeElapsed
   */
  @Valid 
  @Schema(name = "time_elapsed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_elapsed")
  public @Nullable BigDecimal getTimeElapsed() {
    return timeElapsed;
  }

  public void setTimeElapsed(@Nullable BigDecimal timeElapsed) {
    this.timeElapsed = timeElapsed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CorpoTowerRaidStatus corpoTowerRaidStatus = (CorpoTowerRaidStatus) o;
    return Objects.equals(this.raidId, corpoTowerRaidStatus.raidId) &&
        Objects.equals(this.targetCorporation, corpoTowerRaidStatus.targetCorporation) &&
        Objects.equals(this.phase, corpoTowerRaidStatus.phase) &&
        Objects.equals(this.currentFloor, corpoTowerRaidStatus.currentFloor) &&
        Objects.equals(this.totalFloors, corpoTowerRaidStatus.totalFloors) &&
        Objects.equals(this.phaseProgress, corpoTowerRaidStatus.phaseProgress) &&
        Objects.equals(this.playersAlive, corpoTowerRaidStatus.playersAlive) &&
        Objects.equals(this.playersTotal, corpoTowerRaidStatus.playersTotal) &&
        Objects.equals(this.approachUsed, corpoTowerRaidStatus.approachUsed) &&
        Objects.equals(this.enemiesDefeated, corpoTowerRaidStatus.enemiesDefeated) &&
        Objects.equals(this.timeElapsed, corpoTowerRaidStatus.timeElapsed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(raidId, targetCorporation, phase, currentFloor, totalFloors, phaseProgress, playersAlive, playersTotal, approachUsed, enemiesDefeated, timeElapsed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CorpoTowerRaidStatus {\n");
    sb.append("    raidId: ").append(toIndentedString(raidId)).append("\n");
    sb.append("    targetCorporation: ").append(toIndentedString(targetCorporation)).append("\n");
    sb.append("    phase: ").append(toIndentedString(phase)).append("\n");
    sb.append("    currentFloor: ").append(toIndentedString(currentFloor)).append("\n");
    sb.append("    totalFloors: ").append(toIndentedString(totalFloors)).append("\n");
    sb.append("    phaseProgress: ").append(toIndentedString(phaseProgress)).append("\n");
    sb.append("    playersAlive: ").append(toIndentedString(playersAlive)).append("\n");
    sb.append("    playersTotal: ").append(toIndentedString(playersTotal)).append("\n");
    sb.append("    approachUsed: ").append(toIndentedString(approachUsed)).append("\n");
    sb.append("    enemiesDefeated: ").append(toIndentedString(enemiesDefeated)).append("\n");
    sb.append("    timeElapsed: ").append(toIndentedString(timeElapsed)).append("\n");
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

