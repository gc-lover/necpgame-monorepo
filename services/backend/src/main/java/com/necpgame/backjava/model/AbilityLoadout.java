package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * AbilityLoadout
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T22:49:04.787810800+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class AbilityLoadout {

  private String characterId;

  private @Nullable String qSlot;

  private @Nullable String eSlot;

  private @Nullable String rSlot;

  @Valid
  private List<String> passiveSlots = new ArrayList<>();

  @Valid
  private List<String> cyberdeckSlots = new ArrayList<>();

  private @Nullable BigDecimal energyBudgetUsed;

  private @Nullable BigDecimal energyBudgetMax;

  private @Nullable BigDecimal heatLevel;

  public AbilityLoadout() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AbilityLoadout(String characterId) {
    this.characterId = characterId;
  }

  public AbilityLoadout characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public AbilityLoadout qSlot(@Nullable String qSlot) {
    this.qSlot = qSlot;
    return this;
  }

  /**
   * ID способности в Q слоте
   * @return qSlot
   */
  
  @Schema(name = "q_slot", description = "ID способности в Q слоте", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("q_slot")
  public @Nullable String getqSlot() {
    return qSlot;
  }

  public void setqSlot(@Nullable String qSlot) {
    this.qSlot = qSlot;
  }

  public AbilityLoadout eSlot(@Nullable String eSlot) {
    this.eSlot = eSlot;
    return this;
  }

  /**
   * ID способности в E слоте
   * @return eSlot
   */
  
  @Schema(name = "e_slot", description = "ID способности в E слоте", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("e_slot")
  public @Nullable String geteSlot() {
    return eSlot;
  }

  public void seteSlot(@Nullable String eSlot) {
    this.eSlot = eSlot;
  }

  public AbilityLoadout rSlot(@Nullable String rSlot) {
    this.rSlot = rSlot;
    return this;
  }

  /**
   * ID способности в R слоте
   * @return rSlot
   */
  
  @Schema(name = "r_slot", description = "ID способности в R слоте", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("r_slot")
  public @Nullable String getrSlot() {
    return rSlot;
  }

  public void setrSlot(@Nullable String rSlot) {
    this.rSlot = rSlot;
  }

  public AbilityLoadout passiveSlots(List<String> passiveSlots) {
    this.passiveSlots = passiveSlots;
    return this;
  }

  public AbilityLoadout addPassiveSlotsItem(String passiveSlotsItem) {
    if (this.passiveSlots == null) {
      this.passiveSlots = new ArrayList<>();
    }
    this.passiveSlots.add(passiveSlotsItem);
    return this;
  }

  /**
   * Пассивные способности
   * @return passiveSlots
   */
  
  @Schema(name = "passive_slots", description = "Пассивные способности", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("passive_slots")
  public List<String> getPassiveSlots() {
    return passiveSlots;
  }

  public void setPassiveSlots(List<String> passiveSlots) {
    this.passiveSlots = passiveSlots;
  }

  public AbilityLoadout cyberdeckSlots(List<String> cyberdeckSlots) {
    this.cyberdeckSlots = cyberdeckSlots;
    return this;
  }

  public AbilityLoadout addCyberdeckSlotsItem(String cyberdeckSlotsItem) {
    if (this.cyberdeckSlots == null) {
      this.cyberdeckSlots = new ArrayList<>();
    }
    this.cyberdeckSlots.add(cyberdeckSlotsItem);
    return this;
  }

  /**
   * Хакерские способности от кибердеки
   * @return cyberdeckSlots
   */
  
  @Schema(name = "cyberdeck_slots", description = "Хакерские способности от кибердеки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cyberdeck_slots")
  public List<String> getCyberdeckSlots() {
    return cyberdeckSlots;
  }

  public void setCyberdeckSlots(List<String> cyberdeckSlots) {
    this.cyberdeckSlots = cyberdeckSlots;
  }

  public AbilityLoadout energyBudgetUsed(@Nullable BigDecimal energyBudgetUsed) {
    this.energyBudgetUsed = energyBudgetUsed;
    return this;
  }

  /**
   * Использованный энергобюджет
   * @return energyBudgetUsed
   */
  @Valid 
  @Schema(name = "energy_budget_used", description = "Использованный энергобюджет", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energy_budget_used")
  public @Nullable BigDecimal getEnergyBudgetUsed() {
    return energyBudgetUsed;
  }

  public void setEnergyBudgetUsed(@Nullable BigDecimal energyBudgetUsed) {
    this.energyBudgetUsed = energyBudgetUsed;
  }

  public AbilityLoadout energyBudgetMax(@Nullable BigDecimal energyBudgetMax) {
    this.energyBudgetMax = energyBudgetMax;
    return this;
  }

  /**
   * Максимальный энергобюджет
   * @return energyBudgetMax
   */
  @Valid 
  @Schema(name = "energy_budget_max", description = "Максимальный энергобюджет", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energy_budget_max")
  public @Nullable BigDecimal getEnergyBudgetMax() {
    return energyBudgetMax;
  }

  public void setEnergyBudgetMax(@Nullable BigDecimal energyBudgetMax) {
    this.energyBudgetMax = energyBudgetMax;
  }

  public AbilityLoadout heatLevel(@Nullable BigDecimal heatLevel) {
    this.heatLevel = heatLevel;
    return this;
  }

  /**
   * Текущий уровень перегрева системы
   * @return heatLevel
   */
  @Valid 
  @Schema(name = "heat_level", description = "Текущий уровень перегрева системы", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("heat_level")
  public @Nullable BigDecimal getHeatLevel() {
    return heatLevel;
  }

  public void setHeatLevel(@Nullable BigDecimal heatLevel) {
    this.heatLevel = heatLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbilityLoadout abilityLoadout = (AbilityLoadout) o;
    return Objects.equals(this.characterId, abilityLoadout.characterId) &&
        Objects.equals(this.qSlot, abilityLoadout.qSlot) &&
        Objects.equals(this.eSlot, abilityLoadout.eSlot) &&
        Objects.equals(this.rSlot, abilityLoadout.rSlot) &&
        Objects.equals(this.passiveSlots, abilityLoadout.passiveSlots) &&
        Objects.equals(this.cyberdeckSlots, abilityLoadout.cyberdeckSlots) &&
        Objects.equals(this.energyBudgetUsed, abilityLoadout.energyBudgetUsed) &&
        Objects.equals(this.energyBudgetMax, abilityLoadout.energyBudgetMax) &&
        Objects.equals(this.heatLevel, abilityLoadout.heatLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, qSlot, eSlot, rSlot, passiveSlots, cyberdeckSlots, energyBudgetUsed, energyBudgetMax, heatLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbilityLoadout {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    qSlot: ").append(toIndentedString(qSlot)).append("\n");
    sb.append("    eSlot: ").append(toIndentedString(eSlot)).append("\n");
    sb.append("    rSlot: ").append(toIndentedString(rSlot)).append("\n");
    sb.append("    passiveSlots: ").append(toIndentedString(passiveSlots)).append("\n");
    sb.append("    cyberdeckSlots: ").append(toIndentedString(cyberdeckSlots)).append("\n");
    sb.append("    energyBudgetUsed: ").append(toIndentedString(energyBudgetUsed)).append("\n");
    sb.append("    energyBudgetMax: ").append(toIndentedString(energyBudgetMax)).append("\n");
    sb.append("    heatLevel: ").append(toIndentedString(heatLevel)).append("\n");
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

