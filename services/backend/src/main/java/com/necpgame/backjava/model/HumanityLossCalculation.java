package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.HumanityLossCalculationModifiers;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Р Р°СЃС‡РµС‚ РїРѕС‚РµСЂРё С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё РїСЂРё СѓСЃС‚Р°РЅРѕРІРєРµ РёРјРїР»Р°РЅС‚Р°. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -&gt; РЎРёСЃС‚РµРјР° С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё 
 */

@Schema(name = "HumanityLossCalculation", description = "Р Р°СЃС‡РµС‚ РїРѕС‚РµСЂРё С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё РїСЂРё СѓСЃС‚Р°РЅРѕРІРєРµ РёРјРїР»Р°РЅС‚Р°. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -> РЎРёСЃС‚РµРјР° С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class HumanityLossCalculation {

  private Float baseLoss;

  private @Nullable HumanityLossCalculationModifiers modifiers;

  private Float totalLoss;

  private Float newHumanityLevel;

  public HumanityLossCalculation() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public HumanityLossCalculation(Float baseLoss, Float totalLoss, Float newHumanityLevel) {
    this.baseLoss = baseLoss;
    this.totalLoss = totalLoss;
    this.newHumanityLevel = newHumanityLevel;
  }

  public HumanityLossCalculation baseLoss(Float baseLoss) {
    this.baseLoss = baseLoss;
    return this;
  }

  /**
   * Р‘Р°Р·РѕРІР°СЏ РїРѕС‚РµСЂСЏ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё (Р·Р°РІРёСЃРёС‚ РѕС‚ С‚РёРїР° Рё РєР°С‡РµСЃС‚РІР° РёРјРїР»Р°РЅС‚Р°)
   * minimum: 0
   * @return baseLoss
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "base_loss", description = "Р‘Р°Р·РѕРІР°СЏ РїРѕС‚РµСЂСЏ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё (Р·Р°РІРёСЃРёС‚ РѕС‚ С‚РёРїР° Рё РєР°С‡РµСЃС‚РІР° РёРјРїР»Р°РЅС‚Р°)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("base_loss")
  public Float getBaseLoss() {
    return baseLoss;
  }

  public void setBaseLoss(Float baseLoss) {
    this.baseLoss = baseLoss;
  }

  public HumanityLossCalculation modifiers(@Nullable HumanityLossCalculationModifiers modifiers) {
    this.modifiers = modifiers;
    return this;
  }

  /**
   * Get modifiers
   * @return modifiers
   */
  @Valid 
  @Schema(name = "modifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifiers")
  public @Nullable HumanityLossCalculationModifiers getModifiers() {
    return modifiers;
  }

  public void setModifiers(@Nullable HumanityLossCalculationModifiers modifiers) {
    this.modifiers = modifiers;
  }

  public HumanityLossCalculation totalLoss(Float totalLoss) {
    this.totalLoss = totalLoss;
    return this;
  }

  /**
   * РС‚РѕРіРѕРІР°СЏ РїРѕС‚РµСЂСЏ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё
   * minimum: 0
   * @return totalLoss
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "total_loss", description = "РС‚РѕРіРѕРІР°СЏ РїРѕС‚РµСЂСЏ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("total_loss")
  public Float getTotalLoss() {
    return totalLoss;
  }

  public void setTotalLoss(Float totalLoss) {
    this.totalLoss = totalLoss;
  }

  public HumanityLossCalculation newHumanityLevel(Float newHumanityLevel) {
    this.newHumanityLevel = newHumanityLevel;
    return this;
  }

  /**
   * РќРѕРІС‹Р№ СѓСЂРѕРІРµРЅСЊ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё РїРѕСЃР»Рµ РїРѕС‚РµСЂРё
   * minimum: 0
   * maximum: 100
   * @return newHumanityLevel
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "new_humanity_level", description = "РќРѕРІС‹Р№ СѓСЂРѕРІРµРЅСЊ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё РїРѕСЃР»Рµ РїРѕС‚РµСЂРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("new_humanity_level")
  public Float getNewHumanityLevel() {
    return newHumanityLevel;
  }

  public void setNewHumanityLevel(Float newHumanityLevel) {
    this.newHumanityLevel = newHumanityLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HumanityLossCalculation humanityLossCalculation = (HumanityLossCalculation) o;
    return Objects.equals(this.baseLoss, humanityLossCalculation.baseLoss) &&
        Objects.equals(this.modifiers, humanityLossCalculation.modifiers) &&
        Objects.equals(this.totalLoss, humanityLossCalculation.totalLoss) &&
        Objects.equals(this.newHumanityLevel, humanityLossCalculation.newHumanityLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseLoss, modifiers, totalLoss, newHumanityLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HumanityLossCalculation {\n");
    sb.append("    baseLoss: ").append(toIndentedString(baseLoss)).append("\n");
    sb.append("    modifiers: ").append(toIndentedString(modifiers)).append("\n");
    sb.append("    totalLoss: ").append(toIndentedString(totalLoss)).append("\n");
    sb.append("    newHumanityLevel: ").append(toIndentedString(newHumanityLevel)).append("\n");
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

