package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.ComponentRequirement;
import com.necpgame.economyservice.model.CraftingRecipeDetailedAllOfResultItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CraftingRecipeDetailed
 */


public class CraftingRecipeDetailed {

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

  @Valid
  private List<@Valid ComponentRequirement> components = new ArrayList<>();

  private @Nullable CraftingRecipeDetailedAllOfResultItem resultItem;

  private JsonNullable<String> stationRequirement = JsonNullable.<String>undefined();

  private @Nullable String unlockSource;

  public CraftingRecipeDetailed recipeId(@Nullable String recipeId) {
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

  public CraftingRecipeDetailed name(@Nullable String name) {
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

  public CraftingRecipeDetailed description(@Nullable String description) {
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

  public CraftingRecipeDetailed category(@Nullable CategoryEnum category) {
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

  public CraftingRecipeDetailed tier(@Nullable TierEnum tier) {
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

  public CraftingRecipeDetailed requiredSkill(@Nullable String requiredSkill) {
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

  public CraftingRecipeDetailed requiredSkillLevel(@Nullable Integer requiredSkillLevel) {
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

  public CraftingRecipeDetailed baseCraftingTimeSeconds(@Nullable Integer baseCraftingTimeSeconds) {
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

  public CraftingRecipeDetailed baseSuccessRate(@Nullable Float baseSuccessRate) {
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

  public CraftingRecipeDetailed componentsCount(@Nullable Integer componentsCount) {
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

  public CraftingRecipeDetailed components(List<@Valid ComponentRequirement> components) {
    this.components = components;
    return this;
  }

  public CraftingRecipeDetailed addComponentsItem(ComponentRequirement componentsItem) {
    if (this.components == null) {
      this.components = new ArrayList<>();
    }
    this.components.add(componentsItem);
    return this;
  }

  /**
   * Get components
   * @return components
   */
  @Valid 
  @Schema(name = "components", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("components")
  public List<@Valid ComponentRequirement> getComponents() {
    return components;
  }

  public void setComponents(List<@Valid ComponentRequirement> components) {
    this.components = components;
  }

  public CraftingRecipeDetailed resultItem(@Nullable CraftingRecipeDetailedAllOfResultItem resultItem) {
    this.resultItem = resultItem;
    return this;
  }

  /**
   * Get resultItem
   * @return resultItem
   */
  @Valid 
  @Schema(name = "result_item", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("result_item")
  public @Nullable CraftingRecipeDetailedAllOfResultItem getResultItem() {
    return resultItem;
  }

  public void setResultItem(@Nullable CraftingRecipeDetailedAllOfResultItem resultItem) {
    this.resultItem = resultItem;
  }

  public CraftingRecipeDetailed stationRequirement(String stationRequirement) {
    this.stationRequirement = JsonNullable.of(stationRequirement);
    return this;
  }

  /**
   * Get stationRequirement
   * @return stationRequirement
   */
  
  @Schema(name = "station_requirement", example = "WEAPONS_BENCH", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("station_requirement")
  public JsonNullable<String> getStationRequirement() {
    return stationRequirement;
  }

  public void setStationRequirement(JsonNullable<String> stationRequirement) {
    this.stationRequirement = stationRequirement;
  }

  public CraftingRecipeDetailed unlockSource(@Nullable String unlockSource) {
    this.unlockSource = unlockSource;
    return this;
  }

  /**
   * Откуда получен рецепт
   * @return unlockSource
   */
  
  @Schema(name = "unlock_source", example = "Blueprint: Legendary Mantis Blades", description = "Откуда получен рецепт", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlock_source")
  public @Nullable String getUnlockSource() {
    return unlockSource;
  }

  public void setUnlockSource(@Nullable String unlockSource) {
    this.unlockSource = unlockSource;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CraftingRecipeDetailed craftingRecipeDetailed = (CraftingRecipeDetailed) o;
    return Objects.equals(this.recipeId, craftingRecipeDetailed.recipeId) &&
        Objects.equals(this.name, craftingRecipeDetailed.name) &&
        Objects.equals(this.description, craftingRecipeDetailed.description) &&
        Objects.equals(this.category, craftingRecipeDetailed.category) &&
        Objects.equals(this.tier, craftingRecipeDetailed.tier) &&
        Objects.equals(this.requiredSkill, craftingRecipeDetailed.requiredSkill) &&
        Objects.equals(this.requiredSkillLevel, craftingRecipeDetailed.requiredSkillLevel) &&
        Objects.equals(this.baseCraftingTimeSeconds, craftingRecipeDetailed.baseCraftingTimeSeconds) &&
        Objects.equals(this.baseSuccessRate, craftingRecipeDetailed.baseSuccessRate) &&
        Objects.equals(this.componentsCount, craftingRecipeDetailed.componentsCount) &&
        Objects.equals(this.components, craftingRecipeDetailed.components) &&
        Objects.equals(this.resultItem, craftingRecipeDetailed.resultItem) &&
        equalsNullable(this.stationRequirement, craftingRecipeDetailed.stationRequirement) &&
        Objects.equals(this.unlockSource, craftingRecipeDetailed.unlockSource);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(recipeId, name, description, category, tier, requiredSkill, requiredSkillLevel, baseCraftingTimeSeconds, baseSuccessRate, componentsCount, components, resultItem, hashCodeNullable(stationRequirement), unlockSource);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CraftingRecipeDetailed {\n");
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
    sb.append("    components: ").append(toIndentedString(components)).append("\n");
    sb.append("    resultItem: ").append(toIndentedString(resultItem)).append("\n");
    sb.append("    stationRequirement: ").append(toIndentedString(stationRequirement)).append("\n");
    sb.append("    unlockSource: ").append(toIndentedString(unlockSource)).append("\n");
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

