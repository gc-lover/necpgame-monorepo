package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.AbilitySlotAssignment;
import com.necpgame.gameplayservice.model.CooldownInfo;
import com.necpgame.gameplayservice.model.ModSlotAssignment;
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
 * CompanionLoadout
 */


public class CompanionLoadout {

  @Valid
  private List<@Valid AbilitySlotAssignment> abilitySlots = new ArrayList<>();

  @Valid
  private List<@Valid ModSlotAssignment> modSlots = new ArrayList<>();

  @Valid
  private List<@Valid CooldownInfo> cooldownState = new ArrayList<>();

  @Valid
  private List<String> passiveEffects = new ArrayList<>();

  public CompanionLoadout abilitySlots(List<@Valid AbilitySlotAssignment> abilitySlots) {
    this.abilitySlots = abilitySlots;
    return this;
  }

  public CompanionLoadout addAbilitySlotsItem(AbilitySlotAssignment abilitySlotsItem) {
    if (this.abilitySlots == null) {
      this.abilitySlots = new ArrayList<>();
    }
    this.abilitySlots.add(abilitySlotsItem);
    return this;
  }

  /**
   * Get abilitySlots
   * @return abilitySlots
   */
  @Valid 
  @Schema(name = "abilitySlots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("abilitySlots")
  public List<@Valid AbilitySlotAssignment> getAbilitySlots() {
    return abilitySlots;
  }

  public void setAbilitySlots(List<@Valid AbilitySlotAssignment> abilitySlots) {
    this.abilitySlots = abilitySlots;
  }

  public CompanionLoadout modSlots(List<@Valid ModSlotAssignment> modSlots) {
    this.modSlots = modSlots;
    return this;
  }

  public CompanionLoadout addModSlotsItem(ModSlotAssignment modSlotsItem) {
    if (this.modSlots == null) {
      this.modSlots = new ArrayList<>();
    }
    this.modSlots.add(modSlotsItem);
    return this;
  }

  /**
   * Get modSlots
   * @return modSlots
   */
  @Valid 
  @Schema(name = "modSlots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modSlots")
  public List<@Valid ModSlotAssignment> getModSlots() {
    return modSlots;
  }

  public void setModSlots(List<@Valid ModSlotAssignment> modSlots) {
    this.modSlots = modSlots;
  }

  public CompanionLoadout cooldownState(List<@Valid CooldownInfo> cooldownState) {
    this.cooldownState = cooldownState;
    return this;
  }

  public CompanionLoadout addCooldownStateItem(CooldownInfo cooldownStateItem) {
    if (this.cooldownState == null) {
      this.cooldownState = new ArrayList<>();
    }
    this.cooldownState.add(cooldownStateItem);
    return this;
  }

  /**
   * Get cooldownState
   * @return cooldownState
   */
  @Valid 
  @Schema(name = "cooldownState", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldownState")
  public List<@Valid CooldownInfo> getCooldownState() {
    return cooldownState;
  }

  public void setCooldownState(List<@Valid CooldownInfo> cooldownState) {
    this.cooldownState = cooldownState;
  }

  public CompanionLoadout passiveEffects(List<String> passiveEffects) {
    this.passiveEffects = passiveEffects;
    return this;
  }

  public CompanionLoadout addPassiveEffectsItem(String passiveEffectsItem) {
    if (this.passiveEffects == null) {
      this.passiveEffects = new ArrayList<>();
    }
    this.passiveEffects.add(passiveEffectsItem);
    return this;
  }

  /**
   * Get passiveEffects
   * @return passiveEffects
   */
  
  @Schema(name = "passiveEffects", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("passiveEffects")
  public List<String> getPassiveEffects() {
    return passiveEffects;
  }

  public void setPassiveEffects(List<String> passiveEffects) {
    this.passiveEffects = passiveEffects;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompanionLoadout companionLoadout = (CompanionLoadout) o;
    return Objects.equals(this.abilitySlots, companionLoadout.abilitySlots) &&
        Objects.equals(this.modSlots, companionLoadout.modSlots) &&
        Objects.equals(this.cooldownState, companionLoadout.cooldownState) &&
        Objects.equals(this.passiveEffects, companionLoadout.passiveEffects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitySlots, modSlots, cooldownState, passiveEffects);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompanionLoadout {\n");
    sb.append("    abilitySlots: ").append(toIndentedString(abilitySlots)).append("\n");
    sb.append("    modSlots: ").append(toIndentedString(modSlots)).append("\n");
    sb.append("    cooldownState: ").append(toIndentedString(cooldownState)).append("\n");
    sb.append("    passiveEffects: ").append(toIndentedString(passiveEffects)).append("\n");
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

