package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * ResourceDetails
 */


public class ResourceDetails {

  private @Nullable String resourceId;

  private @Nullable String name;

  /**
   * Gets or Sets category
   */
  public enum CategoryEnum {
    RAW("raw"),
    
    PROCESSED("processed"),
    
    COMPONENTS("components"),
    
    DATA("data"),
    
    SPECIAL("special");

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

  private @Nullable Integer tier;

  /**
   * Gets or Sets rarity
   */
  public enum RarityEnum {
    COMMON("common"),
    
    UNCOMMON("uncommon"),
    
    RARE("rare"),
    
    EPIC("epic"),
    
    LEGENDARY("legendary");

    private final String value;

    RarityEnum(String value) {
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
    public static RarityEnum fromValue(String value) {
      for (RarityEnum b : RarityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RarityEnum rarity;

  private @Nullable String icon;

  private @Nullable Integer stackSize;

  private @Nullable BigDecimal weight;

  private @Nullable String description;

  @Valid
  private List<String> sources = new ArrayList<>();

  @Valid
  private List<String> uses = new ArrayList<>();

  private @Nullable BigDecimal vendorPrice;

  private @Nullable BigDecimal marketAveragePrice;

  @Valid
  private List<String> craftingRecipes = new ArrayList<>();

  public ResourceDetails resourceId(@Nullable String resourceId) {
    this.resourceId = resourceId;
    return this;
  }

  /**
   * Get resourceId
   * @return resourceId
   */
  
  @Schema(name = "resource_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resource_id")
  public @Nullable String getResourceId() {
    return resourceId;
  }

  public void setResourceId(@Nullable String resourceId) {
    this.resourceId = resourceId;
  }

  public ResourceDetails name(@Nullable String name) {
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

  public ResourceDetails category(@Nullable CategoryEnum category) {
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

  public ResourceDetails tier(@Nullable Integer tier) {
    this.tier = tier;
    return this;
  }

  /**
   * Get tier
   * minimum: 1
   * maximum: 5
   * @return tier
   */
  @Min(value = 1) @Max(value = 5) 
  @Schema(name = "tier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tier")
  public @Nullable Integer getTier() {
    return tier;
  }

  public void setTier(@Nullable Integer tier) {
    this.tier = tier;
  }

  public ResourceDetails rarity(@Nullable RarityEnum rarity) {
    this.rarity = rarity;
    return this;
  }

  /**
   * Get rarity
   * @return rarity
   */
  
  @Schema(name = "rarity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarity")
  public @Nullable RarityEnum getRarity() {
    return rarity;
  }

  public void setRarity(@Nullable RarityEnum rarity) {
    this.rarity = rarity;
  }

  public ResourceDetails icon(@Nullable String icon) {
    this.icon = icon;
    return this;
  }

  /**
   * Get icon
   * @return icon
   */
  
  @Schema(name = "icon", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("icon")
  public @Nullable String getIcon() {
    return icon;
  }

  public void setIcon(@Nullable String icon) {
    this.icon = icon;
  }

  public ResourceDetails stackSize(@Nullable Integer stackSize) {
    this.stackSize = stackSize;
    return this;
  }

  /**
   * Get stackSize
   * @return stackSize
   */
  
  @Schema(name = "stack_size", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stack_size")
  public @Nullable Integer getStackSize() {
    return stackSize;
  }

  public void setStackSize(@Nullable Integer stackSize) {
    this.stackSize = stackSize;
  }

  public ResourceDetails weight(@Nullable BigDecimal weight) {
    this.weight = weight;
    return this;
  }

  /**
   * Get weight
   * @return weight
   */
  @Valid 
  @Schema(name = "weight", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weight")
  public @Nullable BigDecimal getWeight() {
    return weight;
  }

  public void setWeight(@Nullable BigDecimal weight) {
    this.weight = weight;
  }

  public ResourceDetails description(@Nullable String description) {
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

  public ResourceDetails sources(List<String> sources) {
    this.sources = sources;
    return this;
  }

  public ResourceDetails addSourcesItem(String sourcesItem) {
    if (this.sources == null) {
      this.sources = new ArrayList<>();
    }
    this.sources.add(sourcesItem);
    return this;
  }

  /**
   * Источники получения
   * @return sources
   */
  
  @Schema(name = "sources", description = "Источники получения", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sources")
  public List<String> getSources() {
    return sources;
  }

  public void setSources(List<String> sources) {
    this.sources = sources;
  }

  public ResourceDetails uses(List<String> uses) {
    this.uses = uses;
    return this;
  }

  public ResourceDetails addUsesItem(String usesItem) {
    if (this.uses == null) {
      this.uses = new ArrayList<>();
    }
    this.uses.add(usesItem);
    return this;
  }

  /**
   * Где используется
   * @return uses
   */
  
  @Schema(name = "uses", description = "Где используется", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uses")
  public List<String> getUses() {
    return uses;
  }

  public void setUses(List<String> uses) {
    this.uses = uses;
  }

  public ResourceDetails vendorPrice(@Nullable BigDecimal vendorPrice) {
    this.vendorPrice = vendorPrice;
    return this;
  }

  /**
   * Get vendorPrice
   * @return vendorPrice
   */
  @Valid 
  @Schema(name = "vendor_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("vendor_price")
  public @Nullable BigDecimal getVendorPrice() {
    return vendorPrice;
  }

  public void setVendorPrice(@Nullable BigDecimal vendorPrice) {
    this.vendorPrice = vendorPrice;
  }

  public ResourceDetails marketAveragePrice(@Nullable BigDecimal marketAveragePrice) {
    this.marketAveragePrice = marketAveragePrice;
    return this;
  }

  /**
   * Get marketAveragePrice
   * @return marketAveragePrice
   */
  @Valid 
  @Schema(name = "market_average_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("market_average_price")
  public @Nullable BigDecimal getMarketAveragePrice() {
    return marketAveragePrice;
  }

  public void setMarketAveragePrice(@Nullable BigDecimal marketAveragePrice) {
    this.marketAveragePrice = marketAveragePrice;
  }

  public ResourceDetails craftingRecipes(List<String> craftingRecipes) {
    this.craftingRecipes = craftingRecipes;
    return this;
  }

  public ResourceDetails addCraftingRecipesItem(String craftingRecipesItem) {
    if (this.craftingRecipes == null) {
      this.craftingRecipes = new ArrayList<>();
    }
    this.craftingRecipes.add(craftingRecipesItem);
    return this;
  }

  /**
   * Рецепты, где используется
   * @return craftingRecipes
   */
  
  @Schema(name = "crafting_recipes", description = "Рецепты, где используется", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("crafting_recipes")
  public List<String> getCraftingRecipes() {
    return craftingRecipes;
  }

  public void setCraftingRecipes(List<String> craftingRecipes) {
    this.craftingRecipes = craftingRecipes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResourceDetails resourceDetails = (ResourceDetails) o;
    return Objects.equals(this.resourceId, resourceDetails.resourceId) &&
        Objects.equals(this.name, resourceDetails.name) &&
        Objects.equals(this.category, resourceDetails.category) &&
        Objects.equals(this.tier, resourceDetails.tier) &&
        Objects.equals(this.rarity, resourceDetails.rarity) &&
        Objects.equals(this.icon, resourceDetails.icon) &&
        Objects.equals(this.stackSize, resourceDetails.stackSize) &&
        Objects.equals(this.weight, resourceDetails.weight) &&
        Objects.equals(this.description, resourceDetails.description) &&
        Objects.equals(this.sources, resourceDetails.sources) &&
        Objects.equals(this.uses, resourceDetails.uses) &&
        Objects.equals(this.vendorPrice, resourceDetails.vendorPrice) &&
        Objects.equals(this.marketAveragePrice, resourceDetails.marketAveragePrice) &&
        Objects.equals(this.craftingRecipes, resourceDetails.craftingRecipes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resourceId, name, category, tier, rarity, icon, stackSize, weight, description, sources, uses, vendorPrice, marketAveragePrice, craftingRecipes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResourceDetails {\n");
    sb.append("    resourceId: ").append(toIndentedString(resourceId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
    sb.append("    icon: ").append(toIndentedString(icon)).append("\n");
    sb.append("    stackSize: ").append(toIndentedString(stackSize)).append("\n");
    sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    sources: ").append(toIndentedString(sources)).append("\n");
    sb.append("    uses: ").append(toIndentedString(uses)).append("\n");
    sb.append("    vendorPrice: ").append(toIndentedString(vendorPrice)).append("\n");
    sb.append("    marketAveragePrice: ").append(toIndentedString(marketAveragePrice)).append("\n");
    sb.append("    craftingRecipes: ").append(toIndentedString(craftingRecipes)).append("\n");
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

