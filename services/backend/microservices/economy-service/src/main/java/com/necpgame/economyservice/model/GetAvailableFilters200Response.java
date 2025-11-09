package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.GetAvailableFilters200ResponseLevelRange;
import com.necpgame.economyservice.model.GetAvailableFilters200ResponsePriceRange;
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
 * GetAvailableFilters200Response
 */

@JsonTypeName("getAvailableFilters_200_response")

public class GetAvailableFilters200Response {

  @Valid
  private List<String> categories = new ArrayList<>();

  @Valid
  private List<String> brands = new ArrayList<>();

  @Valid
  private List<String> rarities = new ArrayList<>();

  private @Nullable GetAvailableFilters200ResponseLevelRange levelRange;

  private @Nullable GetAvailableFilters200ResponsePriceRange priceRange;

  public GetAvailableFilters200Response categories(List<String> categories) {
    this.categories = categories;
    return this;
  }

  public GetAvailableFilters200Response addCategoriesItem(String categoriesItem) {
    if (this.categories == null) {
      this.categories = new ArrayList<>();
    }
    this.categories.add(categoriesItem);
    return this;
  }

  /**
   * Get categories
   * @return categories
   */
  
  @Schema(name = "categories", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("categories")
  public List<String> getCategories() {
    return categories;
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
  }

  public GetAvailableFilters200Response brands(List<String> brands) {
    this.brands = brands;
    return this;
  }

  public GetAvailableFilters200Response addBrandsItem(String brandsItem) {
    if (this.brands == null) {
      this.brands = new ArrayList<>();
    }
    this.brands.add(brandsItem);
    return this;
  }

  /**
   * Get brands
   * @return brands
   */
  
  @Schema(name = "brands", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("brands")
  public List<String> getBrands() {
    return brands;
  }

  public void setBrands(List<String> brands) {
    this.brands = brands;
  }

  public GetAvailableFilters200Response rarities(List<String> rarities) {
    this.rarities = rarities;
    return this;
  }

  public GetAvailableFilters200Response addRaritiesItem(String raritiesItem) {
    if (this.rarities == null) {
      this.rarities = new ArrayList<>();
    }
    this.rarities.add(raritiesItem);
    return this;
  }

  /**
   * Get rarities
   * @return rarities
   */
  
  @Schema(name = "rarities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarities")
  public List<String> getRarities() {
    return rarities;
  }

  public void setRarities(List<String> rarities) {
    this.rarities = rarities;
  }

  public GetAvailableFilters200Response levelRange(@Nullable GetAvailableFilters200ResponseLevelRange levelRange) {
    this.levelRange = levelRange;
    return this;
  }

  /**
   * Get levelRange
   * @return levelRange
   */
  @Valid 
  @Schema(name = "level_range", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level_range")
  public @Nullable GetAvailableFilters200ResponseLevelRange getLevelRange() {
    return levelRange;
  }

  public void setLevelRange(@Nullable GetAvailableFilters200ResponseLevelRange levelRange) {
    this.levelRange = levelRange;
  }

  public GetAvailableFilters200Response priceRange(@Nullable GetAvailableFilters200ResponsePriceRange priceRange) {
    this.priceRange = priceRange;
    return this;
  }

  /**
   * Get priceRange
   * @return priceRange
   */
  @Valid 
  @Schema(name = "price_range", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price_range")
  public @Nullable GetAvailableFilters200ResponsePriceRange getPriceRange() {
    return priceRange;
  }

  public void setPriceRange(@Nullable GetAvailableFilters200ResponsePriceRange priceRange) {
    this.priceRange = priceRange;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAvailableFilters200Response getAvailableFilters200Response = (GetAvailableFilters200Response) o;
    return Objects.equals(this.categories, getAvailableFilters200Response.categories) &&
        Objects.equals(this.brands, getAvailableFilters200Response.brands) &&
        Objects.equals(this.rarities, getAvailableFilters200Response.rarities) &&
        Objects.equals(this.levelRange, getAvailableFilters200Response.levelRange) &&
        Objects.equals(this.priceRange, getAvailableFilters200Response.priceRange);
  }

  @Override
  public int hashCode() {
    return Objects.hash(categories, brands, rarities, levelRange, priceRange);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAvailableFilters200Response {\n");
    sb.append("    categories: ").append(toIndentedString(categories)).append("\n");
    sb.append("    brands: ").append(toIndentedString(brands)).append("\n");
    sb.append("    rarities: ").append(toIndentedString(rarities)).append("\n");
    sb.append("    levelRange: ").append(toIndentedString(levelRange)).append("\n");
    sb.append("    priceRange: ").append(toIndentedString(priceRange)).append("\n");
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

