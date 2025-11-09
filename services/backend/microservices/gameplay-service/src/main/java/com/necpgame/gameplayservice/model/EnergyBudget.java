package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.EnergyBudgetBonuses;
import com.necpgame.gameplayservice.model.EnergyBudgetImplantsConsumptionInner;
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
 * EnergyBudget
 */


public class EnergyBudget {

  private @Nullable String characterId;

  private @Nullable BigDecimal totalEnergyPool;

  private @Nullable BigDecimal energyConsumed;

  private @Nullable BigDecimal energyAvailable;

  private @Nullable BigDecimal energyRegenerationRate;

  @Valid
  private List<@Valid EnergyBudgetImplantsConsumptionInner> implantsConsumption = new ArrayList<>();

  private @Nullable EnergyBudgetBonuses bonuses;

  private @Nullable BigDecimal cyberpsychosisRisk;

  public EnergyBudget characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public EnergyBudget totalEnergyPool(@Nullable BigDecimal totalEnergyPool) {
    this.totalEnergyPool = totalEnergyPool;
    return this;
  }

  /**
   * Общий энергетический пул
   * @return totalEnergyPool
   */
  @Valid 
  @Schema(name = "total_energy_pool", description = "Общий энергетический пул", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_energy_pool")
  public @Nullable BigDecimal getTotalEnergyPool() {
    return totalEnergyPool;
  }

  public void setTotalEnergyPool(@Nullable BigDecimal totalEnergyPool) {
    this.totalEnergyPool = totalEnergyPool;
  }

  public EnergyBudget energyConsumed(@Nullable BigDecimal energyConsumed) {
    this.energyConsumed = energyConsumed;
    return this;
  }

  /**
   * Потребление энергии имплантами
   * @return energyConsumed
   */
  @Valid 
  @Schema(name = "energy_consumed", description = "Потребление энергии имплантами", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energy_consumed")
  public @Nullable BigDecimal getEnergyConsumed() {
    return energyConsumed;
  }

  public void setEnergyConsumed(@Nullable BigDecimal energyConsumed) {
    this.energyConsumed = energyConsumed;
  }

  public EnergyBudget energyAvailable(@Nullable BigDecimal energyAvailable) {
    this.energyAvailable = energyAvailable;
    return this;
  }

  /**
   * Доступная энергия
   * @return energyAvailable
   */
  @Valid 
  @Schema(name = "energy_available", description = "Доступная энергия", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energy_available")
  public @Nullable BigDecimal getEnergyAvailable() {
    return energyAvailable;
  }

  public void setEnergyAvailable(@Nullable BigDecimal energyAvailable) {
    this.energyAvailable = energyAvailable;
  }

  public EnergyBudget energyRegenerationRate(@Nullable BigDecimal energyRegenerationRate) {
    this.energyRegenerationRate = energyRegenerationRate;
    return this;
  }

  /**
   * Скорость восстановления энергии в секунду
   * @return energyRegenerationRate
   */
  @Valid 
  @Schema(name = "energy_regeneration_rate", description = "Скорость восстановления энергии в секунду", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energy_regeneration_rate")
  public @Nullable BigDecimal getEnergyRegenerationRate() {
    return energyRegenerationRate;
  }

  public void setEnergyRegenerationRate(@Nullable BigDecimal energyRegenerationRate) {
    this.energyRegenerationRate = energyRegenerationRate;
  }

  public EnergyBudget implantsConsumption(List<@Valid EnergyBudgetImplantsConsumptionInner> implantsConsumption) {
    this.implantsConsumption = implantsConsumption;
    return this;
  }

  public EnergyBudget addImplantsConsumptionItem(EnergyBudgetImplantsConsumptionInner implantsConsumptionItem) {
    if (this.implantsConsumption == null) {
      this.implantsConsumption = new ArrayList<>();
    }
    this.implantsConsumption.add(implantsConsumptionItem);
    return this;
  }

  /**
   * Потребление энергии по имплантам
   * @return implantsConsumption
   */
  @Valid 
  @Schema(name = "implants_consumption", description = "Потребление энергии по имплантам", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implants_consumption")
  public List<@Valid EnergyBudgetImplantsConsumptionInner> getImplantsConsumption() {
    return implantsConsumption;
  }

  public void setImplantsConsumption(List<@Valid EnergyBudgetImplantsConsumptionInner> implantsConsumption) {
    this.implantsConsumption = implantsConsumption;
  }

  public EnergyBudget bonuses(@Nullable EnergyBudgetBonuses bonuses) {
    this.bonuses = bonuses;
    return this;
  }

  /**
   * Get bonuses
   * @return bonuses
   */
  @Valid 
  @Schema(name = "bonuses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonuses")
  public @Nullable EnergyBudgetBonuses getBonuses() {
    return bonuses;
  }

  public void setBonuses(@Nullable EnergyBudgetBonuses bonuses) {
    this.bonuses = bonuses;
  }

  public EnergyBudget cyberpsychosisRisk(@Nullable BigDecimal cyberpsychosisRisk) {
    this.cyberpsychosisRisk = cyberpsychosisRisk;
    return this;
  }

  /**
   * Риск киберпсихоза от энергопотребления
   * @return cyberpsychosisRisk
   */
  @Valid 
  @Schema(name = "cyberpsychosis_risk", description = "Риск киберпсихоза от энергопотребления", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cyberpsychosis_risk")
  public @Nullable BigDecimal getCyberpsychosisRisk() {
    return cyberpsychosisRisk;
  }

  public void setCyberpsychosisRisk(@Nullable BigDecimal cyberpsychosisRisk) {
    this.cyberpsychosisRisk = cyberpsychosisRisk;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnergyBudget energyBudget = (EnergyBudget) o;
    return Objects.equals(this.characterId, energyBudget.characterId) &&
        Objects.equals(this.totalEnergyPool, energyBudget.totalEnergyPool) &&
        Objects.equals(this.energyConsumed, energyBudget.energyConsumed) &&
        Objects.equals(this.energyAvailable, energyBudget.energyAvailable) &&
        Objects.equals(this.energyRegenerationRate, energyBudget.energyRegenerationRate) &&
        Objects.equals(this.implantsConsumption, energyBudget.implantsConsumption) &&
        Objects.equals(this.bonuses, energyBudget.bonuses) &&
        Objects.equals(this.cyberpsychosisRisk, energyBudget.cyberpsychosisRisk);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, totalEnergyPool, energyConsumed, energyAvailable, energyRegenerationRate, implantsConsumption, bonuses, cyberpsychosisRisk);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnergyBudget {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    totalEnergyPool: ").append(toIndentedString(totalEnergyPool)).append("\n");
    sb.append("    energyConsumed: ").append(toIndentedString(energyConsumed)).append("\n");
    sb.append("    energyAvailable: ").append(toIndentedString(energyAvailable)).append("\n");
    sb.append("    energyRegenerationRate: ").append(toIndentedString(energyRegenerationRate)).append("\n");
    sb.append("    implantsConsumption: ").append(toIndentedString(implantsConsumption)).append("\n");
    sb.append("    bonuses: ").append(toIndentedString(bonuses)).append("\n");
    sb.append("    cyberpsychosisRisk: ").append(toIndentedString(cyberpsychosisRisk)).append("\n");
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

