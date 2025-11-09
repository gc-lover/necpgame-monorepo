package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.IndividualEnergyLimits;
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
 * РРЅС„РѕСЂРјР°С†РёСЏ РѕР± СЌРЅРµСЂРіРµС‚РёС‡РµСЃРєРѕРј РїСѓР»Рµ РёРіСЂРѕРєР°. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-implants-limits.md -&gt; Р­РЅРµСЂРіРµС‚РёС‡РµСЃРєРёР№ Р»РёРјРёС‚ 
 */

@Schema(name = "EnergyPoolInfo", description = "РРЅС„РѕСЂРјР°С†РёСЏ РѕР± СЌРЅРµСЂРіРµС‚РёС‡РµСЃРєРѕРј РїСѓР»Рµ РёРіСЂРѕРєР°. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-implants-limits.md -> Р­РЅРµСЂРіРµС‚РёС‡РµСЃРєРёР№ Р»РёРјРёС‚ ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:51:47.912860600+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class EnergyPoolInfo {

  private Float totalPool;

  private Float used;

  private Float available;

  private Float regenRate;

  private Float currentLevel;

  private @Nullable Float maxLevel;

  @Valid
  private List<@Valid IndividualEnergyLimits> individualLimits = new ArrayList<>();

  public EnergyPoolInfo() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EnergyPoolInfo(Float totalPool, Float used, Float available, Float regenRate, Float currentLevel) {
    this.totalPool = totalPool;
    this.used = used;
    this.available = available;
    this.regenRate = regenRate;
    this.currentLevel = currentLevel;
  }

  public EnergyPoolInfo totalPool(Float totalPool) {
    this.totalPool = totalPool;
    return this;
  }

  /**
   * РћР±С‰РёР№ СЌРЅРµСЂРіРµС‚РёС‡РµСЃРєРёР№ РїСѓР»
   * minimum: 0
   * @return totalPool
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "total_pool", description = "РћР±С‰РёР№ СЌРЅРµСЂРіРµС‚РёС‡РµСЃРєРёР№ РїСѓР»", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("total_pool")
  public Float getTotalPool() {
    return totalPool;
  }

  public void setTotalPool(Float totalPool) {
    this.totalPool = totalPool;
  }

  public EnergyPoolInfo used(Float used) {
    this.used = used;
    return this;
  }

  /**
   * РСЃРїРѕР»СЊР·РѕРІР°РЅРѕ СЌРЅРµСЂРіРёРё
   * minimum: 0
   * @return used
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "used", description = "РСЃРїРѕР»СЊР·РѕРІР°РЅРѕ СЌРЅРµСЂРіРёРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("used")
  public Float getUsed() {
    return used;
  }

  public void setUsed(Float used) {
    this.used = used;
  }

  public EnergyPoolInfo available(Float available) {
    this.available = available;
    return this;
  }

  /**
   * Р”РѕСЃС‚СѓРїРЅРѕ СЌРЅРµСЂРіРёРё
   * minimum: 0
   * @return available
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "available", description = "Р”РѕСЃС‚СѓРїРЅРѕ СЌРЅРµСЂРіРёРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("available")
  public Float getAvailable() {
    return available;
  }

  public void setAvailable(Float available) {
    this.available = available;
  }

  public EnergyPoolInfo regenRate(Float regenRate) {
    this.regenRate = regenRate;
    return this;
  }

  /**
   * РЎРєРѕСЂРѕСЃС‚СЊ РІРѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёСЏ СЌРЅРµСЂРіРёРё РІ РµРґРёРЅРёС†Р°С…/СЃРµРє
   * minimum: 0
   * @return regenRate
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "regen_rate", description = "РЎРєРѕСЂРѕСЃС‚СЊ РІРѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёСЏ СЌРЅРµСЂРіРёРё РІ РµРґРёРЅРёС†Р°С…/СЃРµРє", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("regen_rate")
  public Float getRegenRate() {
    return regenRate;
  }

  public void setRegenRate(Float regenRate) {
    this.regenRate = regenRate;
  }

  public EnergyPoolInfo currentLevel(Float currentLevel) {
    this.currentLevel = currentLevel;
    return this;
  }

  /**
   * РўРµРєСѓС‰РёР№ СѓСЂРѕРІРµРЅСЊ СЌРЅРµСЂРіРёРё
   * minimum: 0
   * @return currentLevel
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "current_level", description = "РўРµРєСѓС‰РёР№ СѓСЂРѕРІРµРЅСЊ СЌРЅРµСЂРіРёРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("current_level")
  public Float getCurrentLevel() {
    return currentLevel;
  }

  public void setCurrentLevel(Float currentLevel) {
    this.currentLevel = currentLevel;
  }

  public EnergyPoolInfo maxLevel(@Nullable Float maxLevel) {
    this.maxLevel = maxLevel;
    return this;
  }

  /**
   * РњР°РєСЃРёРјР°Р»СЊРЅС‹Р№ СѓСЂРѕРІРµРЅСЊ СЌРЅРµСЂРіРёРё
   * minimum: 0
   * @return maxLevel
   */
  @DecimalMin(value = "0") 
  @Schema(name = "max_level", description = "РњР°РєСЃРёРјР°Р»СЊРЅС‹Р№ СѓСЂРѕРІРµРЅСЊ СЌРЅРµСЂРіРёРё", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_level")
  public @Nullable Float getMaxLevel() {
    return maxLevel;
  }

  public void setMaxLevel(@Nullable Float maxLevel) {
    this.maxLevel = maxLevel;
  }

  public EnergyPoolInfo individualLimits(List<@Valid IndividualEnergyLimits> individualLimits) {
    this.individualLimits = individualLimits;
    return this;
  }

  public EnergyPoolInfo addIndividualLimitsItem(IndividualEnergyLimits individualLimitsItem) {
    if (this.individualLimits == null) {
      this.individualLimits = new ArrayList<>();
    }
    this.individualLimits.add(individualLimitsItem);
    return this;
  }

  /**
   * РРЅРґРёРІРёРґСѓР°Р»СЊРЅС‹Рµ РѕРіСЂР°РЅРёС‡РµРЅРёСЏ РґР»СЏ СЃР»РѕР¶РЅС‹С… РёРјРїР»Р°РЅС‚РѕРІ
   * @return individualLimits
   */
  @Valid 
  @Schema(name = "individual_limits", description = "РРЅРґРёРІРёРґСѓР°Р»СЊРЅС‹Рµ РѕРіСЂР°РЅРёС‡РµРЅРёСЏ РґР»СЏ СЃР»РѕР¶РЅС‹С… РёРјРїР»Р°РЅС‚РѕРІ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("individual_limits")
  public List<@Valid IndividualEnergyLimits> getIndividualLimits() {
    return individualLimits;
  }

  public void setIndividualLimits(List<@Valid IndividualEnergyLimits> individualLimits) {
    this.individualLimits = individualLimits;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnergyPoolInfo energyPoolInfo = (EnergyPoolInfo) o;
    return Objects.equals(this.totalPool, energyPoolInfo.totalPool) &&
        Objects.equals(this.used, energyPoolInfo.used) &&
        Objects.equals(this.available, energyPoolInfo.available) &&
        Objects.equals(this.regenRate, energyPoolInfo.regenRate) &&
        Objects.equals(this.currentLevel, energyPoolInfo.currentLevel) &&
        Objects.equals(this.maxLevel, energyPoolInfo.maxLevel) &&
        Objects.equals(this.individualLimits, energyPoolInfo.individualLimits);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalPool, used, available, regenRate, currentLevel, maxLevel, individualLimits);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnergyPoolInfo {\n");
    sb.append("    totalPool: ").append(toIndentedString(totalPool)).append("\n");
    sb.append("    used: ").append(toIndentedString(used)).append("\n");
    sb.append("    available: ").append(toIndentedString(available)).append("\n");
    sb.append("    regenRate: ").append(toIndentedString(regenRate)).append("\n");
    sb.append("    currentLevel: ").append(toIndentedString(currentLevel)).append("\n");
    sb.append("    maxLevel: ").append(toIndentedString(maxLevel)).append("\n");
    sb.append("    individualLimits: ").append(toIndentedString(individualLimits)).append("\n");
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

