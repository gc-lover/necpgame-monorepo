package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Recipe
 */


public class Recipe {

  private @Nullable String recipeId;

  private @Nullable String name;

  private @Nullable String itemType;

  private @Nullable String tier;

  private @Nullable Integer requiredSkillLevel;

  private @Nullable Integer componentsCount;

  public Recipe recipeId(@Nullable String recipeId) {
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

  public Recipe name(@Nullable String name) {
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

  public Recipe itemType(@Nullable String itemType) {
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

  public Recipe tier(@Nullable String tier) {
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

  public Recipe requiredSkillLevel(@Nullable Integer requiredSkillLevel) {
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

  public Recipe componentsCount(@Nullable Integer componentsCount) {
    this.componentsCount = componentsCount;
    return this;
  }

  /**
   * Get componentsCount
   * @return componentsCount
   */
  
  @Schema(name = "components_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("components_count")
  public @Nullable Integer getComponentsCount() {
    return componentsCount;
  }

  public void setComponentsCount(@Nullable Integer componentsCount) {
    this.componentsCount = componentsCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Recipe recipe = (Recipe) o;
    return Objects.equals(this.recipeId, recipe.recipeId) &&
        Objects.equals(this.name, recipe.name) &&
        Objects.equals(this.itemType, recipe.itemType) &&
        Objects.equals(this.tier, recipe.tier) &&
        Objects.equals(this.requiredSkillLevel, recipe.requiredSkillLevel) &&
        Objects.equals(this.componentsCount, recipe.componentsCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recipeId, name, itemType, tier, requiredSkillLevel, componentsCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Recipe {\n");
    sb.append("    recipeId: ").append(toIndentedString(recipeId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    itemType: ").append(toIndentedString(itemType)).append("\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    requiredSkillLevel: ").append(toIndentedString(requiredSkillLevel)).append("\n");
    sb.append("    componentsCount: ").append(toIndentedString(componentsCount)).append("\n");
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

