package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * CombatRole
 */


public class CombatRole {

  private @Nullable String roleId;

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable Object primaryAttributes;

  @Valid
  private List<String> requiredImplants = new ArrayList<>();

  private @Nullable Object abilitiesLoadout;

  @Valid
  private List<String> tactics = new ArrayList<>();

  private @Nullable Object synergies;

  private @Nullable Object equipmentPriority;

  public CombatRole roleId(@Nullable String roleId) {
    this.roleId = roleId;
    return this;
  }

  /**
   * Get roleId
   * @return roleId
   */
  
  @Schema(name = "role_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role_id")
  public @Nullable String getRoleId() {
    return roleId;
  }

  public void setRoleId(@Nullable String roleId) {
    this.roleId = roleId;
  }

  public CombatRole name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public CombatRole description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public CombatRole primaryAttributes(@Nullable Object primaryAttributes) {
    this.primaryAttributes = primaryAttributes;
    return this;
  }

  /**
   * Get primaryAttributes
   * @return primaryAttributes
   */
  
  @Schema(name = "primary_attributes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("primary_attributes")
  public @Nullable Object getPrimaryAttributes() {
    return primaryAttributes;
  }

  public void setPrimaryAttributes(@Nullable Object primaryAttributes) {
    this.primaryAttributes = primaryAttributes;
  }

  public CombatRole requiredImplants(List<String> requiredImplants) {
    this.requiredImplants = requiredImplants;
    return this;
  }

  public CombatRole addRequiredImplantsItem(String requiredImplantsItem) {
    if (this.requiredImplants == null) {
      this.requiredImplants = new ArrayList<>();
    }
    this.requiredImplants.add(requiredImplantsItem);
    return this;
  }

  /**
   * Get requiredImplants
   * @return requiredImplants
   */
  
  @Schema(name = "required_implants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_implants")
  public List<String> getRequiredImplants() {
    return requiredImplants;
  }

  public void setRequiredImplants(List<String> requiredImplants) {
    this.requiredImplants = requiredImplants;
  }

  public CombatRole abilitiesLoadout(@Nullable Object abilitiesLoadout) {
    this.abilitiesLoadout = abilitiesLoadout;
    return this;
  }

  /**
   * Get abilitiesLoadout
   * @return abilitiesLoadout
   */
  
  @Schema(name = "abilities_loadout", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("abilities_loadout")
  public @Nullable Object getAbilitiesLoadout() {
    return abilitiesLoadout;
  }

  public void setAbilitiesLoadout(@Nullable Object abilitiesLoadout) {
    this.abilitiesLoadout = abilitiesLoadout;
  }

  public CombatRole tactics(List<String> tactics) {
    this.tactics = tactics;
    return this;
  }

  public CombatRole addTacticsItem(String tacticsItem) {
    if (this.tactics == null) {
      this.tactics = new ArrayList<>();
    }
    this.tactics.add(tacticsItem);
    return this;
  }

  /**
   * Get tactics
   * @return tactics
   */
  
  @Schema(name = "tactics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tactics")
  public List<String> getTactics() {
    return tactics;
  }

  public void setTactics(List<String> tactics) {
    this.tactics = tactics;
  }

  public CombatRole synergies(@Nullable Object synergies) {
    this.synergies = synergies;
    return this;
  }

  /**
   * Get synergies
   * @return synergies
   */
  
  @Schema(name = "synergies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("synergies")
  public @Nullable Object getSynergies() {
    return synergies;
  }

  public void setSynergies(@Nullable Object synergies) {
    this.synergies = synergies;
  }

  public CombatRole equipmentPriority(@Nullable Object equipmentPriority) {
    this.equipmentPriority = equipmentPriority;
    return this;
  }

  /**
   * Get equipmentPriority
   * @return equipmentPriority
   */
  
  @Schema(name = "equipment_priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("equipment_priority")
  public @Nullable Object getEquipmentPriority() {
    return equipmentPriority;
  }

  public void setEquipmentPriority(@Nullable Object equipmentPriority) {
    this.equipmentPriority = equipmentPriority;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CombatRole combatRole = (CombatRole) o;
    return Objects.equals(this.roleId, combatRole.roleId) &&
        Objects.equals(this.name, combatRole.name) &&
        Objects.equals(this.description, combatRole.description) &&
        Objects.equals(this.primaryAttributes, combatRole.primaryAttributes) &&
        Objects.equals(this.requiredImplants, combatRole.requiredImplants) &&
        Objects.equals(this.abilitiesLoadout, combatRole.abilitiesLoadout) &&
        Objects.equals(this.tactics, combatRole.tactics) &&
        Objects.equals(this.synergies, combatRole.synergies) &&
        Objects.equals(this.equipmentPriority, combatRole.equipmentPriority);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roleId, name, description, primaryAttributes, requiredImplants, abilitiesLoadout, tactics, synergies, equipmentPriority);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CombatRole {\n");
    sb.append("    roleId: ").append(toIndentedString(roleId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    primaryAttributes: ").append(toIndentedString(primaryAttributes)).append("\n");
    sb.append("    requiredImplants: ").append(toIndentedString(requiredImplants)).append("\n");
    sb.append("    abilitiesLoadout: ").append(toIndentedString(abilitiesLoadout)).append("\n");
    sb.append("    tactics: ").append(toIndentedString(tactics)).append("\n");
    sb.append("    synergies: ").append(toIndentedString(synergies)).append("\n");
    sb.append("    equipmentPriority: ").append(toIndentedString(equipmentPriority)).append("\n");
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

