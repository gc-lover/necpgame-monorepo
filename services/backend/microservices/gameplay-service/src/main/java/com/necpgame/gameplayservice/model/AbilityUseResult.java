package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.AbilityUseResultEffectsAppliedInner;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AbilityUseResult
 */


public class AbilityUseResult {

  private @Nullable Boolean success;

  @Valid
  private List<@Valid AbilityUseResultEffectsAppliedInner> effectsApplied = new ArrayList<>();

  private @Nullable BigDecimal cooldownStarted;

  private @Nullable BigDecimal energyConsumed;

  private @Nullable BigDecimal heatGenerated;

  private @Nullable BigDecimal cyberpsychosisRiskIncrease;

  public AbilityUseResult success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public AbilityUseResult effectsApplied(List<@Valid AbilityUseResultEffectsAppliedInner> effectsApplied) {
    this.effectsApplied = effectsApplied;
    return this;
  }

  public AbilityUseResult addEffectsAppliedItem(AbilityUseResultEffectsAppliedInner effectsAppliedItem) {
    if (this.effectsApplied == null) {
      this.effectsApplied = new ArrayList<>();
    }
    this.effectsApplied.add(effectsAppliedItem);
    return this;
  }

  /**
   * Get effectsApplied
   * @return effectsApplied
   */
  @Valid 
  @Schema(name = "effects_applied", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effects_applied")
  public List<@Valid AbilityUseResultEffectsAppliedInner> getEffectsApplied() {
    return effectsApplied;
  }

  public void setEffectsApplied(List<@Valid AbilityUseResultEffectsAppliedInner> effectsApplied) {
    this.effectsApplied = effectsApplied;
  }

  public AbilityUseResult cooldownStarted(@Nullable BigDecimal cooldownStarted) {
    this.cooldownStarted = cooldownStarted;
    return this;
  }

  /**
   * Длительность кулдауна
   * @return cooldownStarted
   */
  @Valid 
  @Schema(name = "cooldown_started", description = "Длительность кулдауна", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldown_started")
  public @Nullable BigDecimal getCooldownStarted() {
    return cooldownStarted;
  }

  public void setCooldownStarted(@Nullable BigDecimal cooldownStarted) {
    this.cooldownStarted = cooldownStarted;
  }

  public AbilityUseResult energyConsumed(@Nullable BigDecimal energyConsumed) {
    this.energyConsumed = energyConsumed;
    return this;
  }

  /**
   * Get energyConsumed
   * @return energyConsumed
   */
  @Valid 
  @Schema(name = "energy_consumed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energy_consumed")
  public @Nullable BigDecimal getEnergyConsumed() {
    return energyConsumed;
  }

  public void setEnergyConsumed(@Nullable BigDecimal energyConsumed) {
    this.energyConsumed = energyConsumed;
  }

  public AbilityUseResult heatGenerated(@Nullable BigDecimal heatGenerated) {
    this.heatGenerated = heatGenerated;
    return this;
  }

  /**
   * Перегрев системы
   * @return heatGenerated
   */
  @Valid 
  @Schema(name = "heat_generated", description = "Перегрев системы", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("heat_generated")
  public @Nullable BigDecimal getHeatGenerated() {
    return heatGenerated;
  }

  public void setHeatGenerated(@Nullable BigDecimal heatGenerated) {
    this.heatGenerated = heatGenerated;
  }

  public AbilityUseResult cyberpsychosisRiskIncrease(@Nullable BigDecimal cyberpsychosisRiskIncrease) {
    this.cyberpsychosisRiskIncrease = cyberpsychosisRiskIncrease;
    return this;
  }

  /**
   * Увеличение риска киберпсихоза
   * @return cyberpsychosisRiskIncrease
   */
  @Valid 
  @Schema(name = "cyberpsychosis_risk_increase", description = "Увеличение риска киберпсихоза", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cyberpsychosis_risk_increase")
  public @Nullable BigDecimal getCyberpsychosisRiskIncrease() {
    return cyberpsychosisRiskIncrease;
  }

  public void setCyberpsychosisRiskIncrease(@Nullable BigDecimal cyberpsychosisRiskIncrease) {
    this.cyberpsychosisRiskIncrease = cyberpsychosisRiskIncrease;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbilityUseResult abilityUseResult = (AbilityUseResult) o;
    return Objects.equals(this.success, abilityUseResult.success) &&
        Objects.equals(this.effectsApplied, abilityUseResult.effectsApplied) &&
        Objects.equals(this.cooldownStarted, abilityUseResult.cooldownStarted) &&
        Objects.equals(this.energyConsumed, abilityUseResult.energyConsumed) &&
        Objects.equals(this.heatGenerated, abilityUseResult.heatGenerated) &&
        Objects.equals(this.cyberpsychosisRiskIncrease, abilityUseResult.cyberpsychosisRiskIncrease);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, effectsApplied, cooldownStarted, energyConsumed, heatGenerated, cyberpsychosisRiskIncrease);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbilityUseResult {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    effectsApplied: ").append(toIndentedString(effectsApplied)).append("\n");
    sb.append("    cooldownStarted: ").append(toIndentedString(cooldownStarted)).append("\n");
    sb.append("    energyConsumed: ").append(toIndentedString(energyConsumed)).append("\n");
    sb.append("    heatGenerated: ").append(toIndentedString(heatGenerated)).append("\n");
    sb.append("    cyberpsychosisRiskIncrease: ").append(toIndentedString(cyberpsychosisRiskIncrease)).append("\n");
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

