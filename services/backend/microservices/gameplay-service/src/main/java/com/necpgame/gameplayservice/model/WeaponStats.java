package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * WeaponStats
 */


public class WeaponStats {

  private @Nullable BigDecimal damage;

  private @Nullable BigDecimal fireRate;

  private @Nullable Integer magazineSize;

  private @Nullable BigDecimal reloadTime;

  private @Nullable BigDecimal accuracy;

  private @Nullable BigDecimal rangeEffective;

  private @Nullable BigDecimal rangeMax;

  private @Nullable BigDecimal movementPenalty;

  private @Nullable BigDecimal critChance;

  private @Nullable BigDecimal critDamage;

  private @Nullable BigDecimal penetration;

  public WeaponStats damage(@Nullable BigDecimal damage) {
    this.damage = damage;
    return this;
  }

  /**
   * Get damage
   * @return damage
   */
  @Valid 
  @Schema(name = "damage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("damage")
  public @Nullable BigDecimal getDamage() {
    return damage;
  }

  public void setDamage(@Nullable BigDecimal damage) {
    this.damage = damage;
  }

  public WeaponStats fireRate(@Nullable BigDecimal fireRate) {
    this.fireRate = fireRate;
    return this;
  }

  /**
   * Выстрелов/ударов в секунду
   * @return fireRate
   */
  @Valid 
  @Schema(name = "fire_rate", description = "Выстрелов/ударов в секунду", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fire_rate")
  public @Nullable BigDecimal getFireRate() {
    return fireRate;
  }

  public void setFireRate(@Nullable BigDecimal fireRate) {
    this.fireRate = fireRate;
  }

  public WeaponStats magazineSize(@Nullable Integer magazineSize) {
    this.magazineSize = magazineSize;
    return this;
  }

  /**
   * Get magazineSize
   * @return magazineSize
   */
  
  @Schema(name = "magazine_size", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("magazine_size")
  public @Nullable Integer getMagazineSize() {
    return magazineSize;
  }

  public void setMagazineSize(@Nullable Integer magazineSize) {
    this.magazineSize = magazineSize;
  }

  public WeaponStats reloadTime(@Nullable BigDecimal reloadTime) {
    this.reloadTime = reloadTime;
    return this;
  }

  /**
   * Время перезарядки (секунды)
   * @return reloadTime
   */
  @Valid 
  @Schema(name = "reload_time", description = "Время перезарядки (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reload_time")
  public @Nullable BigDecimal getReloadTime() {
    return reloadTime;
  }

  public void setReloadTime(@Nullable BigDecimal reloadTime) {
    this.reloadTime = reloadTime;
  }

  public WeaponStats accuracy(@Nullable BigDecimal accuracy) {
    this.accuracy = accuracy;
    return this;
  }

  /**
   * Точность (%)
   * @return accuracy
   */
  @Valid 
  @Schema(name = "accuracy", description = "Точность (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("accuracy")
  public @Nullable BigDecimal getAccuracy() {
    return accuracy;
  }

  public void setAccuracy(@Nullable BigDecimal accuracy) {
    this.accuracy = accuracy;
  }

  public WeaponStats rangeEffective(@Nullable BigDecimal rangeEffective) {
    this.rangeEffective = rangeEffective;
    return this;
  }

  /**
   * Эффективная дальность (м)
   * @return rangeEffective
   */
  @Valid 
  @Schema(name = "range_effective", description = "Эффективная дальность (м)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("range_effective")
  public @Nullable BigDecimal getRangeEffective() {
    return rangeEffective;
  }

  public void setRangeEffective(@Nullable BigDecimal rangeEffective) {
    this.rangeEffective = rangeEffective;
  }

  public WeaponStats rangeMax(@Nullable BigDecimal rangeMax) {
    this.rangeMax = rangeMax;
    return this;
  }

  /**
   * Максимальная дальность (м)
   * @return rangeMax
   */
  @Valid 
  @Schema(name = "range_max", description = "Максимальная дальность (м)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("range_max")
  public @Nullable BigDecimal getRangeMax() {
    return rangeMax;
  }

  public void setRangeMax(@Nullable BigDecimal rangeMax) {
    this.rangeMax = rangeMax;
  }

  public WeaponStats movementPenalty(@Nullable BigDecimal movementPenalty) {
    this.movementPenalty = movementPenalty;
    return this;
  }

  /**
   * Штраф к скорости при движении (%)
   * @return movementPenalty
   */
  @Valid 
  @Schema(name = "movement_penalty", description = "Штраф к скорости при движении (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("movement_penalty")
  public @Nullable BigDecimal getMovementPenalty() {
    return movementPenalty;
  }

  public void setMovementPenalty(@Nullable BigDecimal movementPenalty) {
    this.movementPenalty = movementPenalty;
  }

  public WeaponStats critChance(@Nullable BigDecimal critChance) {
    this.critChance = critChance;
    return this;
  }

  /**
   * Get critChance
   * @return critChance
   */
  @Valid 
  @Schema(name = "crit_chance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("crit_chance")
  public @Nullable BigDecimal getCritChance() {
    return critChance;
  }

  public void setCritChance(@Nullable BigDecimal critChance) {
    this.critChance = critChance;
  }

  public WeaponStats critDamage(@Nullable BigDecimal critDamage) {
    this.critDamage = critDamage;
    return this;
  }

  /**
   * Get critDamage
   * @return critDamage
   */
  @Valid 
  @Schema(name = "crit_damage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("crit_damage")
  public @Nullable BigDecimal getCritDamage() {
    return critDamage;
  }

  public void setCritDamage(@Nullable BigDecimal critDamage) {
    this.critDamage = critDamage;
  }

  public WeaponStats penetration(@Nullable BigDecimal penetration) {
    this.penetration = penetration;
    return this;
  }

  /**
   * Проникающая способность
   * @return penetration
   */
  @Valid 
  @Schema(name = "penetration", description = "Проникающая способность", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("penetration")
  public @Nullable BigDecimal getPenetration() {
    return penetration;
  }

  public void setPenetration(@Nullable BigDecimal penetration) {
    this.penetration = penetration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WeaponStats weaponStats = (WeaponStats) o;
    return Objects.equals(this.damage, weaponStats.damage) &&
        Objects.equals(this.fireRate, weaponStats.fireRate) &&
        Objects.equals(this.magazineSize, weaponStats.magazineSize) &&
        Objects.equals(this.reloadTime, weaponStats.reloadTime) &&
        Objects.equals(this.accuracy, weaponStats.accuracy) &&
        Objects.equals(this.rangeEffective, weaponStats.rangeEffective) &&
        Objects.equals(this.rangeMax, weaponStats.rangeMax) &&
        Objects.equals(this.movementPenalty, weaponStats.movementPenalty) &&
        Objects.equals(this.critChance, weaponStats.critChance) &&
        Objects.equals(this.critDamage, weaponStats.critDamage) &&
        Objects.equals(this.penetration, weaponStats.penetration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(damage, fireRate, magazineSize, reloadTime, accuracy, rangeEffective, rangeMax, movementPenalty, critChance, critDamage, penetration);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WeaponStats {\n");
    sb.append("    damage: ").append(toIndentedString(damage)).append("\n");
    sb.append("    fireRate: ").append(toIndentedString(fireRate)).append("\n");
    sb.append("    magazineSize: ").append(toIndentedString(magazineSize)).append("\n");
    sb.append("    reloadTime: ").append(toIndentedString(reloadTime)).append("\n");
    sb.append("    accuracy: ").append(toIndentedString(accuracy)).append("\n");
    sb.append("    rangeEffective: ").append(toIndentedString(rangeEffective)).append("\n");
    sb.append("    rangeMax: ").append(toIndentedString(rangeMax)).append("\n");
    sb.append("    movementPenalty: ").append(toIndentedString(movementPenalty)).append("\n");
    sb.append("    critChance: ").append(toIndentedString(critChance)).append("\n");
    sb.append("    critDamage: ").append(toIndentedString(critDamage)).append("\n");
    sb.append("    penetration: ").append(toIndentedString(penetration)).append("\n");
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

