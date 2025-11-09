package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.narrativeservice.model.RaidStatusSanityLevels;
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
 * RaidStatus
 */


public class RaidStatus {

  private @Nullable String raidId;

  /**
   * Gets or Sets phase
   */
  public enum PhaseEnum {
    INFILTRATION("infiltration"),
    
    DEEP_ZONE("deep_zone"),
    
    CORE_BREACH("core_breach");

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

  private @Nullable BigDecimal phaseProgress;

  private @Nullable Integer playersAlive;

  private @Nullable Integer playersTotal;

  private @Nullable RaidStatusSanityLevels sanityLevels;

  private @Nullable Integer anomaliesActive;

  private @Nullable Integer aiEntitiesDefeated;

  private @Nullable BigDecimal timeElapsed;

  public RaidStatus raidId(@Nullable String raidId) {
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

  public RaidStatus phase(@Nullable PhaseEnum phase) {
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

  public RaidStatus phaseProgress(@Nullable BigDecimal phaseProgress) {
    this.phaseProgress = phaseProgress;
    return this;
  }

  /**
   * Прогресс текущей фазы (%)
   * minimum: 0
   * maximum: 100
   * @return phaseProgress
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "phase_progress", description = "Прогресс текущей фазы (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phase_progress")
  public @Nullable BigDecimal getPhaseProgress() {
    return phaseProgress;
  }

  public void setPhaseProgress(@Nullable BigDecimal phaseProgress) {
    this.phaseProgress = phaseProgress;
  }

  public RaidStatus playersAlive(@Nullable Integer playersAlive) {
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

  public RaidStatus playersTotal(@Nullable Integer playersTotal) {
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

  public RaidStatus sanityLevels(@Nullable RaidStatusSanityLevels sanityLevels) {
    this.sanityLevels = sanityLevels;
    return this;
  }

  /**
   * Get sanityLevels
   * @return sanityLevels
   */
  @Valid 
  @Schema(name = "sanity_levels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sanity_levels")
  public @Nullable RaidStatusSanityLevels getSanityLevels() {
    return sanityLevels;
  }

  public void setSanityLevels(@Nullable RaidStatusSanityLevels sanityLevels) {
    this.sanityLevels = sanityLevels;
  }

  public RaidStatus anomaliesActive(@Nullable Integer anomaliesActive) {
    this.anomaliesActive = anomaliesActive;
    return this;
  }

  /**
   * Get anomaliesActive
   * @return anomaliesActive
   */
  
  @Schema(name = "anomalies_active", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("anomalies_active")
  public @Nullable Integer getAnomaliesActive() {
    return anomaliesActive;
  }

  public void setAnomaliesActive(@Nullable Integer anomaliesActive) {
    this.anomaliesActive = anomaliesActive;
  }

  public RaidStatus aiEntitiesDefeated(@Nullable Integer aiEntitiesDefeated) {
    this.aiEntitiesDefeated = aiEntitiesDefeated;
    return this;
  }

  /**
   * Get aiEntitiesDefeated
   * @return aiEntitiesDefeated
   */
  
  @Schema(name = "ai_entities_defeated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ai_entities_defeated")
  public @Nullable Integer getAiEntitiesDefeated() {
    return aiEntitiesDefeated;
  }

  public void setAiEntitiesDefeated(@Nullable Integer aiEntitiesDefeated) {
    this.aiEntitiesDefeated = aiEntitiesDefeated;
  }

  public RaidStatus timeElapsed(@Nullable BigDecimal timeElapsed) {
    this.timeElapsed = timeElapsed;
    return this;
  }

  /**
   * Время в рейде (минуты)
   * @return timeElapsed
   */
  @Valid 
  @Schema(name = "time_elapsed", description = "Время в рейде (минуты)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    RaidStatus raidStatus = (RaidStatus) o;
    return Objects.equals(this.raidId, raidStatus.raidId) &&
        Objects.equals(this.phase, raidStatus.phase) &&
        Objects.equals(this.phaseProgress, raidStatus.phaseProgress) &&
        Objects.equals(this.playersAlive, raidStatus.playersAlive) &&
        Objects.equals(this.playersTotal, raidStatus.playersTotal) &&
        Objects.equals(this.sanityLevels, raidStatus.sanityLevels) &&
        Objects.equals(this.anomaliesActive, raidStatus.anomaliesActive) &&
        Objects.equals(this.aiEntitiesDefeated, raidStatus.aiEntitiesDefeated) &&
        Objects.equals(this.timeElapsed, raidStatus.timeElapsed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(raidId, phase, phaseProgress, playersAlive, playersTotal, sanityLevels, anomaliesActive, aiEntitiesDefeated, timeElapsed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RaidStatus {\n");
    sb.append("    raidId: ").append(toIndentedString(raidId)).append("\n");
    sb.append("    phase: ").append(toIndentedString(phase)).append("\n");
    sb.append("    phaseProgress: ").append(toIndentedString(phaseProgress)).append("\n");
    sb.append("    playersAlive: ").append(toIndentedString(playersAlive)).append("\n");
    sb.append("    playersTotal: ").append(toIndentedString(playersTotal)).append("\n");
    sb.append("    sanityLevels: ").append(toIndentedString(sanityLevels)).append("\n");
    sb.append("    anomaliesActive: ").append(toIndentedString(anomaliesActive)).append("\n");
    sb.append("    aiEntitiesDefeated: ").append(toIndentedString(aiEntitiesDefeated)).append("\n");
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

