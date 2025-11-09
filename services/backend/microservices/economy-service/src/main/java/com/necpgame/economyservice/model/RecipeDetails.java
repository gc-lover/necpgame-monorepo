package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.RecipeDetailsComponentsInner;
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
 * RecipeDetails
 */


public class RecipeDetails {

  private @Nullable String recipeId;

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable String itemType;

  private @Nullable String tier;

  @Valid
  private List<@Valid RecipeDetailsComponentsInner> components = new ArrayList<>();

  private @Nullable String requiredSkill;

  private @Nullable Integer requiredSkillLevel;

  private @Nullable BigDecimal craftingTime;

  private @Nullable BigDecimal successRateBase;

  @Valid
  private List<Object> possibleResults = new ArrayList<>();

  public RecipeDetails recipeId(@Nullable String recipeId) {
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

  public RecipeDetails name(@Nullable String name) {
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

  public RecipeDetails description(@Nullable String description) {
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

  public RecipeDetails itemType(@Nullable String itemType) {
    this.itemType = itemType;
    return this;
  }

  /**
   * Get itemType
   * @return itemType
   */
  
  @Schema(name = "item_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_type")
  public @Nullable String getItemType() {
    return itemType;
  }

  public void setItemType(@Nullable String itemType) {
    this.itemType = itemType;
  }

  public RecipeDetails tier(@Nullable String tier) {
    this.tier = tier;
    return this;
  }

  /**
   * Get tier
   * @return tier
   */
  
  @Schema(name = "tier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tier")
  public @Nullable String getTier() {
    return tier;
  }

  public void setTier(@Nullable String tier) {
    this.tier = tier;
  }

  public RecipeDetails components(List<@Valid RecipeDetailsComponentsInner> components) {
    this.components = components;
    return this;
  }

  public RecipeDetails addComponentsItem(RecipeDetailsComponentsInner componentsItem) {
    if (this.components == null) {
      this.components = new ArrayList<>();
    }
    this.components.add(componentsItem);
    return this;
  }

  /**
   * Необходимые компоненты
   * @return components
   */
  @Valid 
  @Schema(name = "components", description = "Необходимые компоненты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("components")
  public List<@Valid RecipeDetailsComponentsInner> getComponents() {
    return components;
  }

  public void setComponents(List<@Valid RecipeDetailsComponentsInner> components) {
    this.components = components;
  }

  public RecipeDetails requiredSkill(@Nullable String requiredSkill) {
    this.requiredSkill = requiredSkill;
    return this;
  }

  /**
   * Get requiredSkill
   * @return requiredSkill
   */
  
  @Schema(name = "required_skill", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_skill")
  public @Nullable String getRequiredSkill() {
    return requiredSkill;
  }

  public void setRequiredSkill(@Nullable String requiredSkill) {
    this.requiredSkill = requiredSkill;
  }

  public RecipeDetails requiredSkillLevel(@Nullable Integer requiredSkillLevel) {
    this.requiredSkillLevel = requiredSkillLevel;
    return this;
  }

  /**
   * Get requiredSkillLevel
   * @return requiredSkillLevel
   */
  
  @Schema(name = "required_skill_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_skill_level")
  public @Nullable Integer getRequiredSkillLevel() {
    return requiredSkillLevel;
  }

  public void setRequiredSkillLevel(@Nullable Integer requiredSkillLevel) {
    this.requiredSkillLevel = requiredSkillLevel;
  }

  public RecipeDetails craftingTime(@Nullable BigDecimal craftingTime) {
    this.craftingTime = craftingTime;
    return this;
  }

  /**
   * Время крафта (секунды)
   * @return craftingTime
   */
  @Valid 
  @Schema(name = "crafting_time", description = "Время крафта (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("crafting_time")
  public @Nullable BigDecimal getCraftingTime() {
    return craftingTime;
  }

  public void setCraftingTime(@Nullable BigDecimal craftingTime) {
    this.craftingTime = craftingTime;
  }

  public RecipeDetails successRateBase(@Nullable BigDecimal successRateBase) {
    this.successRateBase = successRateBase;
    return this;
  }

  /**
   * Базовый шанс успеха (%)
   * @return successRateBase
   */
  @Valid 
  @Schema(name = "success_rate_base", description = "Базовый шанс успеха (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success_rate_base")
  public @Nullable BigDecimal getSuccessRateBase() {
    return successRateBase;
  }

  public void setSuccessRateBase(@Nullable BigDecimal successRateBase) {
    this.successRateBase = successRateBase;
  }

  public RecipeDetails possibleResults(List<Object> possibleResults) {
    this.possibleResults = possibleResults;
    return this;
  }

  public RecipeDetails addPossibleResultsItem(Object possibleResultsItem) {
    if (this.possibleResults == null) {
      this.possibleResults = new ArrayList<>();
    }
    this.possibleResults.add(possibleResultsItem);
    return this;
  }

  /**
   * Возможные результаты (normal, high quality, masterwork)
   * @return possibleResults
   */
  
  @Schema(name = "possible_results", description = "Возможные результаты (normal, high quality, masterwork)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("possible_results")
  public List<Object> getPossibleResults() {
    return possibleResults;
  }

  public void setPossibleResults(List<Object> possibleResults) {
    this.possibleResults = possibleResults;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RecipeDetails recipeDetails = (RecipeDetails) o;
    return Objects.equals(this.recipeId, recipeDetails.recipeId) &&
        Objects.equals(this.name, recipeDetails.name) &&
        Objects.equals(this.description, recipeDetails.description) &&
        Objects.equals(this.itemType, recipeDetails.itemType) &&
        Objects.equals(this.tier, recipeDetails.tier) &&
        Objects.equals(this.components, recipeDetails.components) &&
        Objects.equals(this.requiredSkill, recipeDetails.requiredSkill) &&
        Objects.equals(this.requiredSkillLevel, recipeDetails.requiredSkillLevel) &&
        Objects.equals(this.craftingTime, recipeDetails.craftingTime) &&
        Objects.equals(this.successRateBase, recipeDetails.successRateBase) &&
        Objects.equals(this.possibleResults, recipeDetails.possibleResults);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recipeId, name, description, itemType, tier, components, requiredSkill, requiredSkillLevel, craftingTime, successRateBase, possibleResults);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RecipeDetails {\n");
    sb.append("    recipeId: ").append(toIndentedString(recipeId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    itemType: ").append(toIndentedString(itemType)).append("\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    components: ").append(toIndentedString(components)).append("\n");
    sb.append("    requiredSkill: ").append(toIndentedString(requiredSkill)).append("\n");
    sb.append("    requiredSkillLevel: ").append(toIndentedString(requiredSkillLevel)).append("\n");
    sb.append("    craftingTime: ").append(toIndentedString(craftingTime)).append("\n");
    sb.append("    successRateBase: ").append(toIndentedString(successRateBase)).append("\n");
    sb.append("    possibleResults: ").append(toIndentedString(possibleResults)).append("\n");
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

