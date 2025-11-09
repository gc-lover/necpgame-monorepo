package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.StatusEffect;
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
 * DamageResult
 */


public class DamageResult {

  private @Nullable Integer damageDealt;

  private @Nullable Integer damageBlocked;

  private @Nullable Boolean isCritical;

  private @Nullable Integer targetHpBefore;

  private @Nullable Integer targetHpAfter;

  private @Nullable Boolean targetKilled;

  @Valid
  private List<@Valid StatusEffect> statusEffectsApplied = new ArrayList<>();

  public DamageResult damageDealt(@Nullable Integer damageDealt) {
    this.damageDealt = damageDealt;
    return this;
  }

  /**
   * Финальный урон после защиты
   * @return damageDealt
   */
  
  @Schema(name = "damage_dealt", description = "Финальный урон после защиты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("damage_dealt")
  public @Nullable Integer getDamageDealt() {
    return damageDealt;
  }

  public void setDamageDealt(@Nullable Integer damageDealt) {
    this.damageDealt = damageDealt;
  }

  public DamageResult damageBlocked(@Nullable Integer damageBlocked) {
    this.damageBlocked = damageBlocked;
    return this;
  }

  /**
   * Урон, заблокированный броней/щитом
   * @return damageBlocked
   */
  
  @Schema(name = "damage_blocked", description = "Урон, заблокированный броней/щитом", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("damage_blocked")
  public @Nullable Integer getDamageBlocked() {
    return damageBlocked;
  }

  public void setDamageBlocked(@Nullable Integer damageBlocked) {
    this.damageBlocked = damageBlocked;
  }

  public DamageResult isCritical(@Nullable Boolean isCritical) {
    this.isCritical = isCritical;
    return this;
  }

  /**
   * Get isCritical
   * @return isCritical
   */
  
  @Schema(name = "is_critical", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_critical")
  public @Nullable Boolean getIsCritical() {
    return isCritical;
  }

  public void setIsCritical(@Nullable Boolean isCritical) {
    this.isCritical = isCritical;
  }

  public DamageResult targetHpBefore(@Nullable Integer targetHpBefore) {
    this.targetHpBefore = targetHpBefore;
    return this;
  }

  /**
   * Get targetHpBefore
   * @return targetHpBefore
   */
  
  @Schema(name = "target_hp_before", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_hp_before")
  public @Nullable Integer getTargetHpBefore() {
    return targetHpBefore;
  }

  public void setTargetHpBefore(@Nullable Integer targetHpBefore) {
    this.targetHpBefore = targetHpBefore;
  }

  public DamageResult targetHpAfter(@Nullable Integer targetHpAfter) {
    this.targetHpAfter = targetHpAfter;
    return this;
  }

  /**
   * Get targetHpAfter
   * @return targetHpAfter
   */
  
  @Schema(name = "target_hp_after", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_hp_after")
  public @Nullable Integer getTargetHpAfter() {
    return targetHpAfter;
  }

  public void setTargetHpAfter(@Nullable Integer targetHpAfter) {
    this.targetHpAfter = targetHpAfter;
  }

  public DamageResult targetKilled(@Nullable Boolean targetKilled) {
    this.targetKilled = targetKilled;
    return this;
  }

  /**
   * Get targetKilled
   * @return targetKilled
   */
  
  @Schema(name = "target_killed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_killed")
  public @Nullable Boolean getTargetKilled() {
    return targetKilled;
  }

  public void setTargetKilled(@Nullable Boolean targetKilled) {
    this.targetKilled = targetKilled;
  }

  public DamageResult statusEffectsApplied(List<@Valid StatusEffect> statusEffectsApplied) {
    this.statusEffectsApplied = statusEffectsApplied;
    return this;
  }

  public DamageResult addStatusEffectsAppliedItem(StatusEffect statusEffectsAppliedItem) {
    if (this.statusEffectsApplied == null) {
      this.statusEffectsApplied = new ArrayList<>();
    }
    this.statusEffectsApplied.add(statusEffectsAppliedItem);
    return this;
  }

  /**
   * Get statusEffectsApplied
   * @return statusEffectsApplied
   */
  @Valid 
  @Schema(name = "status_effects_applied", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status_effects_applied")
  public List<@Valid StatusEffect> getStatusEffectsApplied() {
    return statusEffectsApplied;
  }

  public void setStatusEffectsApplied(List<@Valid StatusEffect> statusEffectsApplied) {
    this.statusEffectsApplied = statusEffectsApplied;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DamageResult damageResult = (DamageResult) o;
    return Objects.equals(this.damageDealt, damageResult.damageDealt) &&
        Objects.equals(this.damageBlocked, damageResult.damageBlocked) &&
        Objects.equals(this.isCritical, damageResult.isCritical) &&
        Objects.equals(this.targetHpBefore, damageResult.targetHpBefore) &&
        Objects.equals(this.targetHpAfter, damageResult.targetHpAfter) &&
        Objects.equals(this.targetKilled, damageResult.targetKilled) &&
        Objects.equals(this.statusEffectsApplied, damageResult.statusEffectsApplied);
  }

  @Override
  public int hashCode() {
    return Objects.hash(damageDealt, damageBlocked, isCritical, targetHpBefore, targetHpAfter, targetKilled, statusEffectsApplied);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DamageResult {\n");
    sb.append("    damageDealt: ").append(toIndentedString(damageDealt)).append("\n");
    sb.append("    damageBlocked: ").append(toIndentedString(damageBlocked)).append("\n");
    sb.append("    isCritical: ").append(toIndentedString(isCritical)).append("\n");
    sb.append("    targetHpBefore: ").append(toIndentedString(targetHpBefore)).append("\n");
    sb.append("    targetHpAfter: ").append(toIndentedString(targetHpAfter)).append("\n");
    sb.append("    targetKilled: ").append(toIndentedString(targetKilled)).append("\n");
    sb.append("    statusEffectsApplied: ").append(toIndentedString(statusEffectsApplied)).append("\n");
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

