package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CraftingRecipe
 */


public class CraftingRecipe {

  private @Nullable String recipeId;

  private @Nullable String name;

  private @Nullable String description;

  /**
   * Gets or Sets category
   */
  public enum CategoryEnum {
    WEAPONS("WEAPONS"),
    
    ARMOR("ARMOR"),
    
    IMPLANTS("IMPLANTS"),
    
    MODS("MODS"),
    
    CONSUMABLES("CONSUMABLES");

    private final String value;

    CategoryEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static CategoryEnum fromValue(String value) {
      for (CategoryEnum b : CategoryEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable CategoryEnum category;

  /**
   * Gets or Sets tier
   */
  public enum TierEnum {
    T1("T1"),
    
    T2("T2"),
    
    T3("T3"),
    
    T4("T4"),
    
    T5("T5");

    private final String value;

    TierEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TierEnum fromValue(String value) {
      for (TierEnum b : TierEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TierEnum tier;

  private @Nullable String requiredSkill;

  private @Nullable Integer requiredSkillLevel;

  private @Nullable Integer baseCraftingTimeSeconds;

  private @Nullable Float baseSuccessRate;

  private @Nullable Integer componentsCount;

  public CraftingRecipe recipeId(@Nullable String recipeId) {
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

  public CraftingRecipe name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", example = "Legendary Mantis Blades", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public CraftingRecipe description(@Nullable String description) {
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

  public CraftingRecipe category(@Nullable CategoryEnum category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable CategoryEnum getCategory() {
    return category;
  }

  public void setCategory(@Nullable CategoryEnum category) {
    this.category = category;
  }

  public CraftingRecipe tier(@Nullable TierEnum tier) {
    this.tier = tier;
    return this;
  }

  /**
   * Get tier
   * @return tier
   */
  
  @Schema(name = "tier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tier")
  public @Nullable TierEnum getTier() {
    return tier;
  }

  public void setTier(@Nullable TierEnum tier) {
    this.tier = tier;
  }

  public CraftingRecipe requiredSkill(@Nullable String requiredSkill) {
    this.requiredSkill = requiredSkill;
    return this;
  }

  /**
   * Get requiredSkill
   * @return requiredSkill
   */
  
  @Schema(name = "required_skill", example = "CRAFTING", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_skill")
  public @Nullable String getRequiredSkill() {
    return requiredSkill;
  }

  public void setRequiredSkill(@Nullable String requiredSkill) {
    this.requiredSkill = requiredSkill;
  }

  public CraftingRecipe requiredSkillLevel(@Nullable Integer requiredSkillLevel) {
    this.requiredSkillLevel = requiredSkillLevel;
    return this;
  }

  /**
   * Get requiredSkillLevel
   * @return requiredSkillLevel
   */
  
  @Schema(name = "required_skill_level", example = "15", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_skill_level")
  public @Nullable Integer getRequiredSkillLevel() {
    return requiredSkillLevel;
  }

  public void setRequiredSkillLevel(@Nullable Integer requiredSkillLevel) {
    this.requiredSkillLevel = requiredSkillLevel;
  }

  public CraftingRecipe baseCraftingTimeSeconds(@Nullable Integer baseCraftingTimeSeconds) {
    this.baseCraftingTimeSeconds = baseCraftingTimeSeconds;
    return this;
  }

  /**
   * Get baseCraftingTimeSeconds
   * @return baseCraftingTimeSeconds
   */
  
  @Schema(name = "base_crafting_time_seconds", example = "300", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base_crafting_time_seconds")
  public @Nullable Integer getBaseCraftingTimeSeconds() {
    return baseCraftingTimeSeconds;
  }

  public void setBaseCraftingTimeSeconds(@Nullable Integer baseCraftingTimeSeconds) {
    this.baseCraftingTimeSeconds = baseCraftingTimeSeconds;
  }

  public CraftingRecipe baseSuccessRate(@Nullable Float baseSuccessRate) {
    this.baseSuccessRate = baseSuccessRate;
    return this;
  }

  /**
   * Get baseSuccessRate
   * @return baseSuccessRate
   */
  
  @Schema(name = "base_success_rate", example = "0.75", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base_success_rate")
  public @Nullable Float getBaseSuccessRate() {
    return baseSuccessRate;
  }

  public void setBaseSuccessRate(@Nullable Float baseSuccessRate) {
    this.baseSuccessRate = baseSuccessRate;
  }

  public CraftingRecipe componentsCount(@Nullable Integer componentsCount) {
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
    CraftingRecipe craftingRecipe = (CraftingRecipe) o;
    return Objects.equals(this.recipeId, craftingRecipe.recipeId) &&
        Objects.equals(this.name, craftingRecipe.name) &&
        Objects.equals(this.description, craftingRecipe.description) &&
        Objects.equals(this.category, craftingRecipe.category) &&
        Objects.equals(this.tier, craftingRecipe.tier) &&
        Objects.equals(this.requiredSkill, craftingRecipe.requiredSkill) &&
        Objects.equals(this.requiredSkillLevel, craftingRecipe.requiredSkillLevel) &&
        Objects.equals(this.baseCraftingTimeSeconds, craftingRecipe.baseCraftingTimeSeconds) &&
        Objects.equals(this.baseSuccessRate, craftingRecipe.baseSuccessRate) &&
        Objects.equals(this.componentsCount, craftingRecipe.componentsCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recipeId, name, description, category, tier, requiredSkill, requiredSkillLevel, baseCraftingTimeSeconds, baseSuccessRate, componentsCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CraftingRecipe {\n");
    sb.append("    recipeId: ").append(toIndentedString(recipeId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    requiredSkill: ").append(toIndentedString(requiredSkill)).append("\n");
    sb.append("    requiredSkillLevel: ").append(toIndentedString(requiredSkillLevel)).append("\n");
    sb.append("    baseCraftingTimeSeconds: ").append(toIndentedString(baseCraftingTimeSeconds)).append("\n");
    sb.append("    baseSuccessRate: ").append(toIndentedString(baseSuccessRate)).append("\n");
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

