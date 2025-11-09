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
 * AbilityCost
 */


public class AbilityCost {

  private @Nullable BigDecimal energy;

  private @Nullable BigDecimal health;

  private @Nullable Integer ammo;

  private @Nullable BigDecimal charge;

  private @Nullable BigDecimal heat;

  public AbilityCost energy(@Nullable BigDecimal energy) {
    this.energy = energy;
    return this;
  }

  /**
   * Энергия (от имплантов/кибердеки)
   * @return energy
   */
  @Valid 
  @Schema(name = "energy", description = "Энергия (от имплантов/кибердеки)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energy")
  public @Nullable BigDecimal getEnergy() {
    return energy;
  }

  public void setEnergy(@Nullable BigDecimal energy) {
    this.energy = energy;
  }

  public AbilityCost health(@Nullable BigDecimal health) {
    this.health = health;
    return this;
  }

  /**
   * Здоровье (риск киберпсихоза)
   * @return health
   */
  @Valid 
  @Schema(name = "health", description = "Здоровье (риск киберпсихоза)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("health")
  public @Nullable BigDecimal getHealth() {
    return health;
  }

  public void setHealth(@Nullable BigDecimal health) {
    this.health = health;
  }

  public AbilityCost ammo(@Nullable Integer ammo) {
    this.ammo = ammo;
    return this;
  }

  /**
   * Боеприпасы способности
   * @return ammo
   */
  
  @Schema(name = "ammo", description = "Боеприпасы способности", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ammo")
  public @Nullable Integer getAmmo() {
    return ammo;
  }

  public void setAmmo(@Nullable Integer ammo) {
    this.ammo = ammo;
  }

  public AbilityCost charge(@Nullable BigDecimal charge) {
    this.charge = charge;
    return this;
  }

  /**
   * Заряд (для ультимативных способностей)
   * @return charge
   */
  @Valid 
  @Schema(name = "charge", description = "Заряд (для ультимативных способностей)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("charge")
  public @Nullable BigDecimal getCharge() {
    return charge;
  }

  public void setCharge(@Nullable BigDecimal charge) {
    this.charge = charge;
  }

  public AbilityCost heat(@Nullable BigDecimal heat) {
    this.heat = heat;
    return this;
  }

  /**
   * Перегрев системы (влияет на киберпсихоз)
   * @return heat
   */
  @Valid 
  @Schema(name = "heat", description = "Перегрев системы (влияет на киберпсихоз)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("heat")
  public @Nullable BigDecimal getHeat() {
    return heat;
  }

  public void setHeat(@Nullable BigDecimal heat) {
    this.heat = heat;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbilityCost abilityCost = (AbilityCost) o;
    return Objects.equals(this.energy, abilityCost.energy) &&
        Objects.equals(this.health, abilityCost.health) &&
        Objects.equals(this.ammo, abilityCost.ammo) &&
        Objects.equals(this.charge, abilityCost.charge) &&
        Objects.equals(this.heat, abilityCost.heat);
  }

  @Override
  public int hashCode() {
    return Objects.hash(energy, health, ammo, charge, heat);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbilityCost {\n");
    sb.append("    energy: ").append(toIndentedString(energy)).append("\n");
    sb.append("    health: ").append(toIndentedString(health)).append("\n");
    sb.append("    ammo: ").append(toIndentedString(ammo)).append("\n");
    sb.append("    charge: ").append(toIndentedString(charge)).append("\n");
    sb.append("    heat: ").append(toIndentedString(heat)).append("\n");
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

