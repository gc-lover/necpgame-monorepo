package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.AbilitySlotAssignment;
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
 * LoadoutUpdateRequest
 */


public class LoadoutUpdateRequest {

  @Valid
  private List<@Valid AbilitySlotAssignment> abilitySlots = new ArrayList<>();

  @Valid
  private List<@Valid ModSlotAssignment> modSlots = new ArrayList<>();

  @Valid
  private List<String> passiveEffects = new ArrayList<>();

  public LoadoutUpdateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LoadoutUpdateRequest(List<@Valid AbilitySlotAssignment> abilitySlots) {
    this.abilitySlots = abilitySlots;
  }

  public LoadoutUpdateRequest abilitySlots(List<@Valid AbilitySlotAssignment> abilitySlots) {
    this.abilitySlots = abilitySlots;
    return this;
  }

  public LoadoutUpdateRequest addAbilitySlotsItem(AbilitySlotAssignment abilitySlotsItem) {
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
  @NotNull @Valid @Size(max = 5) 
  @Schema(name = "abilitySlots", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("abilitySlots")
  public List<@Valid AbilitySlotAssignment> getAbilitySlots() {
    return abilitySlots;
  }

  public void setAbilitySlots(List<@Valid AbilitySlotAssignment> abilitySlots) {
    this.abilitySlots = abilitySlots;
  }

  public LoadoutUpdateRequest modSlots(List<@Valid ModSlotAssignment> modSlots) {
    this.modSlots = modSlots;
    return this;
  }

  public LoadoutUpdateRequest addModSlotsItem(ModSlotAssignment modSlotsItem) {
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
  @Valid @Size(max = 5) 
  @Schema(name = "modSlots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modSlots")
  public List<@Valid ModSlotAssignment> getModSlots() {
    return modSlots;
  }

  public void setModSlots(List<@Valid ModSlotAssignment> modSlots) {
    this.modSlots = modSlots;
  }

  public LoadoutUpdateRequest passiveEffects(List<String> passiveEffects) {
    this.passiveEffects = passiveEffects;
    return this;
  }

  public LoadoutUpdateRequest addPassiveEffectsItem(String passiveEffectsItem) {
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
    LoadoutUpdateRequest loadoutUpdateRequest = (LoadoutUpdateRequest) o;
    return Objects.equals(this.abilitySlots, loadoutUpdateRequest.abilitySlots) &&
        Objects.equals(this.modSlots, loadoutUpdateRequest.modSlots) &&
        Objects.equals(this.passiveEffects, loadoutUpdateRequest.passiveEffects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitySlots, modSlots, passiveEffects);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoadoutUpdateRequest {\n");
    sb.append("    abilitySlots: ").append(toIndentedString(abilitySlots)).append("\n");
    sb.append("    modSlots: ").append(toIndentedString(modSlots)).append("\n");
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

