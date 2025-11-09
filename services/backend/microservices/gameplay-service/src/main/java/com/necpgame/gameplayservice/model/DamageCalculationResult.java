package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.DamageCalculationResultModifiersInner;
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
 * DamageCalculationResult
 */


public class DamageCalculationResult {

  private @Nullable BigDecimal baseDamage;

  private @Nullable BigDecimal bodyPartMultiplier;

  private @Nullable BigDecimal armorReduction;

  private @Nullable BigDecimal typeEffectiveness;

  private @Nullable BigDecimal finalDamage;

  private @Nullable BigDecimal estimatedTtk;

  @Valid
  private List<@Valid DamageCalculationResultModifiersInner> modifiers = new ArrayList<>();

  public DamageCalculationResult baseDamage(@Nullable BigDecimal baseDamage) {
    this.baseDamage = baseDamage;
    return this;
  }

  /**
   * Get baseDamage
   * @return baseDamage
   */
  @Valid 
  @Schema(name = "base_damage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base_damage")
  public @Nullable BigDecimal getBaseDamage() {
    return baseDamage;
  }

  public void setBaseDamage(@Nullable BigDecimal baseDamage) {
    this.baseDamage = baseDamage;
  }

  public DamageCalculationResult bodyPartMultiplier(@Nullable BigDecimal bodyPartMultiplier) {
    this.bodyPartMultiplier = bodyPartMultiplier;
    return this;
  }

  /**
   * Модификатор части тела (head=2.0, torso=1.0, limbs=0.7)
   * @return bodyPartMultiplier
   */
  @Valid 
  @Schema(name = "body_part_multiplier", description = "Модификатор части тела (head=2.0, torso=1.0, limbs=0.7)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("body_part_multiplier")
  public @Nullable BigDecimal getBodyPartMultiplier() {
    return bodyPartMultiplier;
  }

  public void setBodyPartMultiplier(@Nullable BigDecimal bodyPartMultiplier) {
    this.bodyPartMultiplier = bodyPartMultiplier;
  }

  public DamageCalculationResult armorReduction(@Nullable BigDecimal armorReduction) {
    this.armorReduction = armorReduction;
    return this;
  }

  /**
   * Get armorReduction
   * @return armorReduction
   */
  @Valid 
  @Schema(name = "armor_reduction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("armor_reduction")
  public @Nullable BigDecimal getArmorReduction() {
    return armorReduction;
  }

  public void setArmorReduction(@Nullable BigDecimal armorReduction) {
    this.armorReduction = armorReduction;
  }

  public DamageCalculationResult typeEffectiveness(@Nullable BigDecimal typeEffectiveness) {
    this.typeEffectiveness = typeEffectiveness;
    return this;
  }

  /**
   * Эффективность типа урона против цели
   * @return typeEffectiveness
   */
  @Valid 
  @Schema(name = "type_effectiveness", description = "Эффективность типа урона против цели", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type_effectiveness")
  public @Nullable BigDecimal getTypeEffectiveness() {
    return typeEffectiveness;
  }

  public void setTypeEffectiveness(@Nullable BigDecimal typeEffectiveness) {
    this.typeEffectiveness = typeEffectiveness;
  }

  public DamageCalculationResult finalDamage(@Nullable BigDecimal finalDamage) {
    this.finalDamage = finalDamage;
    return this;
  }

  /**
   * Get finalDamage
   * @return finalDamage
   */
  @Valid 
  @Schema(name = "final_damage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("final_damage")
  public @Nullable BigDecimal getFinalDamage() {
    return finalDamage;
  }

  public void setFinalDamage(@Nullable BigDecimal finalDamage) {
    this.finalDamage = finalDamage;
  }

  public DamageCalculationResult estimatedTtk(@Nullable BigDecimal estimatedTtk) {
    this.estimatedTtk = estimatedTtk;
    return this;
  }

  /**
   * Примерное время убийства (секунды)
   * @return estimatedTtk
   */
  @Valid 
  @Schema(name = "estimated_ttk", description = "Примерное время убийства (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_ttk")
  public @Nullable BigDecimal getEstimatedTtk() {
    return estimatedTtk;
  }

  public void setEstimatedTtk(@Nullable BigDecimal estimatedTtk) {
    this.estimatedTtk = estimatedTtk;
  }

  public DamageCalculationResult modifiers(List<@Valid DamageCalculationResultModifiersInner> modifiers) {
    this.modifiers = modifiers;
    return this;
  }

  public DamageCalculationResult addModifiersItem(DamageCalculationResultModifiersInner modifiersItem) {
    if (this.modifiers == null) {
      this.modifiers = new ArrayList<>();
    }
    this.modifiers.add(modifiersItem);
    return this;
  }

  /**
   * Get modifiers
   * @return modifiers
   */
  @Valid 
  @Schema(name = "modifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifiers")
  public List<@Valid DamageCalculationResultModifiersInner> getModifiers() {
    return modifiers;
  }

  public void setModifiers(List<@Valid DamageCalculationResultModifiersInner> modifiers) {
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
    DamageCalculationResult damageCalculationResult = (DamageCalculationResult) o;
    return Objects.equals(this.baseDamage, damageCalculationResult.baseDamage) &&
        Objects.equals(this.bodyPartMultiplier, damageCalculationResult.bodyPartMultiplier) &&
        Objects.equals(this.armorReduction, damageCalculationResult.armorReduction) &&
        Objects.equals(this.typeEffectiveness, damageCalculationResult.typeEffectiveness) &&
        Objects.equals(this.finalDamage, damageCalculationResult.finalDamage) &&
        Objects.equals(this.estimatedTtk, damageCalculationResult.estimatedTtk) &&
        Objects.equals(this.modifiers, damageCalculationResult.modifiers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseDamage, bodyPartMultiplier, armorReduction, typeEffectiveness, finalDamage, estimatedTtk, modifiers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DamageCalculationResult {\n");
    sb.append("    baseDamage: ").append(toIndentedString(baseDamage)).append("\n");
    sb.append("    bodyPartMultiplier: ").append(toIndentedString(bodyPartMultiplier)).append("\n");
    sb.append("    armorReduction: ").append(toIndentedString(armorReduction)).append("\n");
    sb.append("    typeEffectiveness: ").append(toIndentedString(typeEffectiveness)).append("\n");
    sb.append("    finalDamage: ").append(toIndentedString(finalDamage)).append("\n");
    sb.append("    estimatedTtk: ").append(toIndentedString(estimatedTtk)).append("\n");
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

