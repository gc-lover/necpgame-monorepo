package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.IndividualEnergyLimits;
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
 * Информация об энергетическом пуле игрока. Источник: .BRAIN/02-gameplay/combat/combat-implants-limits.md -&gt; Энергетический лимит 
 */

@Schema(name = "EnergyPoolInfo", description = "Информация об энергетическом пуле игрока. Источник: .BRAIN/02-gameplay/combat/combat-implants-limits.md -> Энергетический лимит ")

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
   * Общий энергетический пул
   * minimum: 0
   * @return totalPool
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "total_pool", description = "Общий энергетический пул", requiredMode = Schema.RequiredMode.REQUIRED)
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
   * Использовано энергии
   * minimum: 0
   * @return used
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "used", description = "Использовано энергии", requiredMode = Schema.RequiredMode.REQUIRED)
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
   * Доступно энергии
   * minimum: 0
   * @return available
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "available", description = "Доступно энергии", requiredMode = Schema.RequiredMode.REQUIRED)
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
   * Скорость восстановления энергии в единицах/сек
   * minimum: 0
   * @return regenRate
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "regen_rate", description = "Скорость восстановления энергии в единицах/сек", requiredMode = Schema.RequiredMode.REQUIRED)
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
   * Текущий уровень энергии
   * minimum: 0
   * @return currentLevel
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "current_level", description = "Текущий уровень энергии", requiredMode = Schema.RequiredMode.REQUIRED)
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
   * Максимальный уровень энергии
   * minimum: 0
   * @return maxLevel
   */
  @DecimalMin(value = "0") 
  @Schema(name = "max_level", description = "Максимальный уровень энергии", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
   * Индивидуальные ограничения для сложных имплантов
   * @return individualLimits
   */
  @Valid 
  @Schema(name = "individual_limits", description = "Индивидуальные ограничения для сложных имплантов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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

