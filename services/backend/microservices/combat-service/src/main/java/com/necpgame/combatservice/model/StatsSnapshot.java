package com.necpgame.combatservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * StatsSnapshot
 */


public class StatsSnapshot {

  private @Nullable BigDecimal health;

  private @Nullable BigDecimal healthMax;

  private @Nullable BigDecimal shield;

  private @Nullable BigDecimal stamina;

  private @Nullable BigDecimal haste;

  private @Nullable BigDecimal critChance;

  @Valid
  private Map<String, Object> modifiers = new HashMap<>();

  public StatsSnapshot health(@Nullable BigDecimal health) {
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
  public @Nullable BigDecimal getHealth() {
    return health;
  }

  public void setHealth(@Nullable BigDecimal health) {
    this.health = health;
  }

  public StatsSnapshot healthMax(@Nullable BigDecimal healthMax) {
    this.healthMax = healthMax;
    return this;
  }

  /**
   * Get healthMax
   * @return healthMax
   */
  @Valid 
  @Schema(name = "healthMax", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("healthMax")
  public @Nullable BigDecimal getHealthMax() {
    return healthMax;
  }

  public void setHealthMax(@Nullable BigDecimal healthMax) {
    this.healthMax = healthMax;
  }

  public StatsSnapshot shield(@Nullable BigDecimal shield) {
    this.shield = shield;
    return this;
  }

  /**
   * Get shield
   * @return shield
   */
  @Valid 
  @Schema(name = "shield", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("shield")
  public @Nullable BigDecimal getShield() {
    return shield;
  }

  public void setShield(@Nullable BigDecimal shield) {
    this.shield = shield;
  }

  public StatsSnapshot stamina(@Nullable BigDecimal stamina) {
    this.stamina = stamina;
    return this;
  }

  /**
   * Get stamina
   * @return stamina
   */
  @Valid 
  @Schema(name = "stamina", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stamina")
  public @Nullable BigDecimal getStamina() {
    return stamina;
  }

  public void setStamina(@Nullable BigDecimal stamina) {
    this.stamina = stamina;
  }

  public StatsSnapshot haste(@Nullable BigDecimal haste) {
    this.haste = haste;
    return this;
  }

  /**
   * Get haste
   * @return haste
   */
  @Valid 
  @Schema(name = "haste", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("haste")
  public @Nullable BigDecimal getHaste() {
    return haste;
  }

  public void setHaste(@Nullable BigDecimal haste) {
    this.haste = haste;
  }

  public StatsSnapshot critChance(@Nullable BigDecimal critChance) {
    this.critChance = critChance;
    return this;
  }

  /**
   * Get critChance
   * @return critChance
   */
  @Valid 
  @Schema(name = "critChance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("critChance")
  public @Nullable BigDecimal getCritChance() {
    return critChance;
  }

  public void setCritChance(@Nullable BigDecimal critChance) {
    this.critChance = critChance;
  }

  public StatsSnapshot modifiers(Map<String, Object> modifiers) {
    this.modifiers = modifiers;
    return this;
  }

  public StatsSnapshot putModifiersItem(String key, Object modifiersItem) {
    if (this.modifiers == null) {
      this.modifiers = new HashMap<>();
    }
    this.modifiers.put(key, modifiersItem);
    return this;
  }

  /**
   * Get modifiers
   * @return modifiers
   */
  
  @Schema(name = "modifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifiers")
  public Map<String, Object> getModifiers() {
    return modifiers;
  }

  public void setModifiers(Map<String, Object> modifiers) {
    this.modifiers = modifiers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatsSnapshot statsSnapshot = (StatsSnapshot) o;
    return Objects.equals(this.health, statsSnapshot.health) &&
        Objects.equals(this.healthMax, statsSnapshot.healthMax) &&
        Objects.equals(this.shield, statsSnapshot.shield) &&
        Objects.equals(this.stamina, statsSnapshot.stamina) &&
        Objects.equals(this.haste, statsSnapshot.haste) &&
        Objects.equals(this.critChance, statsSnapshot.critChance) &&
        Objects.equals(this.modifiers, statsSnapshot.modifiers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(health, healthMax, shield, stamina, haste, critChance, modifiers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatsSnapshot {\n");
    sb.append("    health: ").append(toIndentedString(health)).append("\n");
    sb.append("    healthMax: ").append(toIndentedString(healthMax)).append("\n");
    sb.append("    shield: ").append(toIndentedString(shield)).append("\n");
    sb.append("    stamina: ").append(toIndentedString(stamina)).append("\n");
    sb.append("    haste: ").append(toIndentedString(haste)).append("\n");
    sb.append("    critChance: ").append(toIndentedString(critChance)).append("\n");
    sb.append("    modifiers: ").append(toIndentedString(modifiers)).append("\n");
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

