package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.CraftingCalculationMissingComponentsInner;
import com.necpgame.backjava.model.CraftingCalculationQualityChances;
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
 * CraftingCalculation
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CraftingCalculation {

  private @Nullable String recipeId;

  private @Nullable Integer characterSkillLevel;

  private @Nullable BigDecimal baseSuccessRate;

  private @Nullable BigDecimal finalSuccessRate;

  private @Nullable Integer baseTimeSeconds;

  private @Nullable Integer finalTimeSeconds;

  private @Nullable CraftingCalculationQualityChances qualityChances;

  @Valid
  private List<@Valid CraftingCalculationMissingComponentsInner> missingComponents = new ArrayList<>();

  public CraftingCalculation recipeId(@Nullable String recipeId) {
    this.recipeId = recipeId;
    return this;
  }

  /**
   * Get recipeId
   * @return recipeId
   */
  
  @Schema(name = "recipe_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recipe_id")
  public @Nullable String getRecipeId() {
    return recipeId;
  }

  public void setRecipeId(@Nullable String recipeId) {
    this.recipeId = recipeId;
  }

  public CraftingCalculation characterSkillLevel(@Nullable Integer characterSkillLevel) {
    this.characterSkillLevel = characterSkillLevel;
    return this;
  }

  /**
   * Get characterSkillLevel
   * @return characterSkillLevel
   */
  
  @Schema(name = "character_skill_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_skill_level")
  public @Nullable Integer getCharacterSkillLevel() {
    return characterSkillLevel;
  }

  public void setCharacterSkillLevel(@Nullable Integer characterSkillLevel) {
    this.characterSkillLevel = characterSkillLevel;
  }

  public CraftingCalculation baseSuccessRate(@Nullable BigDecimal baseSuccessRate) {
    this.baseSuccessRate = baseSuccessRate;
    return this;
  }

  /**
   * Get baseSuccessRate
   * @return baseSuccessRate
   */
  @Valid 
  @Schema(name = "base_success_rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base_success_rate")
  public @Nullable BigDecimal getBaseSuccessRate() {
    return baseSuccessRate;
  }

  public void setBaseSuccessRate(@Nullable BigDecimal baseSuccessRate) {
    this.baseSuccessRate = baseSuccessRate;
  }

  public CraftingCalculation finalSuccessRate(@Nullable BigDecimal finalSuccessRate) {
    this.finalSuccessRate = finalSuccessRate;
    return this;
  }

  /**
   * С учетом навыков и бонусов
   * @return finalSuccessRate
   */
  @Valid 
  @Schema(name = "final_success_rate", description = "С учетом навыков и бонусов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("final_success_rate")
  public @Nullable BigDecimal getFinalSuccessRate() {
    return finalSuccessRate;
  }

  public void setFinalSuccessRate(@Nullable BigDecimal finalSuccessRate) {
    this.finalSuccessRate = finalSuccessRate;
  }

  public CraftingCalculation baseTimeSeconds(@Nullable Integer baseTimeSeconds) {
    this.baseTimeSeconds = baseTimeSeconds;
    return this;
  }

  /**
   * Get baseTimeSeconds
   * @return baseTimeSeconds
   */
  
  @Schema(name = "base_time_seconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base_time_seconds")
  public @Nullable Integer getBaseTimeSeconds() {
    return baseTimeSeconds;
  }

  public void setBaseTimeSeconds(@Nullable Integer baseTimeSeconds) {
    this.baseTimeSeconds = baseTimeSeconds;
  }

  public CraftingCalculation finalTimeSeconds(@Nullable Integer finalTimeSeconds) {
    this.finalTimeSeconds = finalTimeSeconds;
    return this;
  }

  /**
   * Get finalTimeSeconds
   * @return finalTimeSeconds
   */
  
  @Schema(name = "final_time_seconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("final_time_seconds")
  public @Nullable Integer getFinalTimeSeconds() {
    return finalTimeSeconds;
  }

  public void setFinalTimeSeconds(@Nullable Integer finalTimeSeconds) {
    this.finalTimeSeconds = finalTimeSeconds;
  }

  public CraftingCalculation qualityChances(@Nullable CraftingCalculationQualityChances qualityChances) {
    this.qualityChances = qualityChances;
    return this;
  }

  /**
   * Get qualityChances
   * @return qualityChances
   */
  @Valid 
  @Schema(name = "quality_chances", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quality_chances")
  public @Nullable CraftingCalculationQualityChances getQualityChances() {
    return qualityChances;
  }

  public void setQualityChances(@Nullable CraftingCalculationQualityChances qualityChances) {
    this.qualityChances = qualityChances;
  }

  public CraftingCalculation missingComponents(List<@Valid CraftingCalculationMissingComponentsInner> missingComponents) {
    this.missingComponents = missingComponents;
    return this;
  }

  public CraftingCalculation addMissingComponentsItem(CraftingCalculationMissingComponentsInner missingComponentsItem) {
    if (this.missingComponents == null) {
      this.missingComponents = new ArrayList<>();
    }
    this.missingComponents.add(missingComponentsItem);
    return this;
  }

  /**
   * Get missingComponents
   * @return missingComponents
   */
  @Valid 
  @Schema(name = "missing_components", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("missing_components")
  public List<@Valid CraftingCalculationMissingComponentsInner> getMissingComponents() {
    return missingComponents;
  }

  public void setMissingComponents(List<@Valid CraftingCalculationMissingComponentsInner> missingComponents) {
    this.missingComponents = missingComponents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CraftingCalculation craftingCalculation = (CraftingCalculation) o;
    return Objects.equals(this.recipeId, craftingCalculation.recipeId) &&
        Objects.equals(this.characterSkillLevel, craftingCalculation.characterSkillLevel) &&
        Objects.equals(this.baseSuccessRate, craftingCalculation.baseSuccessRate) &&
        Objects.equals(this.finalSuccessRate, craftingCalculation.finalSuccessRate) &&
        Objects.equals(this.baseTimeSeconds, craftingCalculation.baseTimeSeconds) &&
        Objects.equals(this.finalTimeSeconds, craftingCalculation.finalTimeSeconds) &&
        Objects.equals(this.qualityChances, craftingCalculation.qualityChances) &&
        Objects.equals(this.missingComponents, craftingCalculation.missingComponents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recipeId, characterSkillLevel, baseSuccessRate, finalSuccessRate, baseTimeSeconds, finalTimeSeconds, qualityChances, missingComponents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CraftingCalculation {\n");
    sb.append("    recipeId: ").append(toIndentedString(recipeId)).append("\n");
    sb.append("    characterSkillLevel: ").append(toIndentedString(characterSkillLevel)).append("\n");
    sb.append("    baseSuccessRate: ").append(toIndentedString(baseSuccessRate)).append("\n");
    sb.append("    finalSuccessRate: ").append(toIndentedString(finalSuccessRate)).append("\n");
    sb.append("    baseTimeSeconds: ").append(toIndentedString(baseTimeSeconds)).append("\n");
    sb.append("    finalTimeSeconds: ").append(toIndentedString(finalTimeSeconds)).append("\n");
    sb.append("    qualityChances: ").append(toIndentedString(qualityChances)).append("\n");
    sb.append("    missingComponents: ").append(toIndentedString(missingComponents)).append("\n");
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

