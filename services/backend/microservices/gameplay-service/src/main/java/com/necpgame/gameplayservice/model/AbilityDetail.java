package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.AbilityDetailCosts;
import com.necpgame.gameplayservice.model.AbilityDetailRequirements;
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
 * AbilityDetail
 */


public class AbilityDetail {

  private @Nullable String id;

  private @Nullable String name;

  private @Nullable String category;

  private @Nullable String slot;

  @Valid
  private List<String> classAffinity = new ArrayList<>();

  private @Nullable AbilityDetailRequirements requirements;

  private @Nullable AbilityDetailCosts costs;

  private @Nullable Object effects;

  @Valid
  private List<Object> synergies = new ArrayList<>();

  private @Nullable Object upgrades;

  public AbilityDetail id(@Nullable String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable String getId() {
    return id;
  }

  public void setId(@Nullable String id) {
    this.id = id;
  }

  public AbilityDetail name(@Nullable String name) {
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

  public AbilityDetail category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  public AbilityDetail slot(@Nullable String slot) {
    this.slot = slot;
    return this;
  }

  /**
   * Get slot
   * @return slot
   */
  
  @Schema(name = "slot", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slot")
  public @Nullable String getSlot() {
    return slot;
  }

  public void setSlot(@Nullable String slot) {
    this.slot = slot;
  }

  public AbilityDetail classAffinity(List<String> classAffinity) {
    this.classAffinity = classAffinity;
    return this;
  }

  public AbilityDetail addClassAffinityItem(String classAffinityItem) {
    if (this.classAffinity == null) {
      this.classAffinity = new ArrayList<>();
    }
    this.classAffinity.add(classAffinityItem);
    return this;
  }

  /**
   * Get classAffinity
   * @return classAffinity
   */
  
  @Schema(name = "class_affinity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class_affinity")
  public List<String> getClassAffinity() {
    return classAffinity;
  }

  public void setClassAffinity(List<String> classAffinity) {
    this.classAffinity = classAffinity;
  }

  public AbilityDetail requirements(@Nullable AbilityDetailRequirements requirements) {
    this.requirements = requirements;
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  @Valid 
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public @Nullable AbilityDetailRequirements getRequirements() {
    return requirements;
  }

  public void setRequirements(@Nullable AbilityDetailRequirements requirements) {
    this.requirements = requirements;
  }

  public AbilityDetail costs(@Nullable AbilityDetailCosts costs) {
    this.costs = costs;
    return this;
  }

  /**
   * Get costs
   * @return costs
   */
  @Valid 
  @Schema(name = "costs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("costs")
  public @Nullable AbilityDetailCosts getCosts() {
    return costs;
  }

  public void setCosts(@Nullable AbilityDetailCosts costs) {
    this.costs = costs;
  }

  public AbilityDetail effects(@Nullable Object effects) {
    this.effects = effects;
    return this;
  }

  /**
   * Get effects
   * @return effects
   */
  
  @Schema(name = "effects", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effects")
  public @Nullable Object getEffects() {
    return effects;
  }

  public void setEffects(@Nullable Object effects) {
    this.effects = effects;
  }

  public AbilityDetail synergies(List<Object> synergies) {
    this.synergies = synergies;
    return this;
  }

  public AbilityDetail addSynergiesItem(Object synergiesItem) {
    if (this.synergies == null) {
      this.synergies = new ArrayList<>();
    }
    this.synergies.add(synergiesItem);
    return this;
  }

  /**
   * Get synergies
   * @return synergies
   */
  
  @Schema(name = "synergies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("synergies")
  public List<Object> getSynergies() {
    return synergies;
  }

  public void setSynergies(List<Object> synergies) {
    this.synergies = synergies;
  }

  public AbilityDetail upgrades(@Nullable Object upgrades) {
    this.upgrades = upgrades;
    return this;
  }

  /**
   * Get upgrades
   * @return upgrades
   */
  
  @Schema(name = "upgrades", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("upgrades")
  public @Nullable Object getUpgrades() {
    return upgrades;
  }

  public void setUpgrades(@Nullable Object upgrades) {
    this.upgrades = upgrades;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbilityDetail abilityDetail = (AbilityDetail) o;
    return Objects.equals(this.id, abilityDetail.id) &&
        Objects.equals(this.name, abilityDetail.name) &&
        Objects.equals(this.category, abilityDetail.category) &&
        Objects.equals(this.slot, abilityDetail.slot) &&
        Objects.equals(this.classAffinity, abilityDetail.classAffinity) &&
        Objects.equals(this.requirements, abilityDetail.requirements) &&
        Objects.equals(this.costs, abilityDetail.costs) &&
        Objects.equals(this.effects, abilityDetail.effects) &&
        Objects.equals(this.synergies, abilityDetail.synergies) &&
        Objects.equals(this.upgrades, abilityDetail.upgrades);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, category, slot, classAffinity, requirements, costs, effects, synergies, upgrades);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbilityDetail {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    slot: ").append(toIndentedString(slot)).append("\n");
    sb.append("    classAffinity: ").append(toIndentedString(classAffinity)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
    sb.append("    costs: ").append(toIndentedString(costs)).append("\n");
    sb.append("    effects: ").append(toIndentedString(effects)).append("\n");
    sb.append("    synergies: ").append(toIndentedString(synergies)).append("\n");
    sb.append("    upgrades: ").append(toIndentedString(upgrades)).append("\n");
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

