package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.CraftingRecipe;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetKnownRecipes200Response
 */

@JsonTypeName("getKnownRecipes_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetKnownRecipes200Response {

  @Valid
  private List<@Valid CraftingRecipe> knownRecipes = new ArrayList<>();

  private @Nullable Integer totalRecipes;

  @Valid
  private Map<String, Integer> recipesByCategory = new HashMap<>();

  public GetKnownRecipes200Response knownRecipes(List<@Valid CraftingRecipe> knownRecipes) {
    this.knownRecipes = knownRecipes;
    return this;
  }

  public GetKnownRecipes200Response addKnownRecipesItem(CraftingRecipe knownRecipesItem) {
    if (this.knownRecipes == null) {
      this.knownRecipes = new ArrayList<>();
    }
    this.knownRecipes.add(knownRecipesItem);
    return this;
  }

  /**
   * Get knownRecipes
   * @return knownRecipes
   */
  @Valid 
  @Schema(name = "known_recipes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("known_recipes")
  public List<@Valid CraftingRecipe> getKnownRecipes() {
    return knownRecipes;
  }

  public void setKnownRecipes(List<@Valid CraftingRecipe> knownRecipes) {
    this.knownRecipes = knownRecipes;
  }

  public GetKnownRecipes200Response totalRecipes(@Nullable Integer totalRecipes) {
    this.totalRecipes = totalRecipes;
    return this;
  }

  /**
   * Get totalRecipes
   * @return totalRecipes
   */
  
  @Schema(name = "total_recipes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_recipes")
  public @Nullable Integer getTotalRecipes() {
    return totalRecipes;
  }

  public void setTotalRecipes(@Nullable Integer totalRecipes) {
    this.totalRecipes = totalRecipes;
  }

  public GetKnownRecipes200Response recipesByCategory(Map<String, Integer> recipesByCategory) {
    this.recipesByCategory = recipesByCategory;
    return this;
  }

  public GetKnownRecipes200Response putRecipesByCategoryItem(String key, Integer recipesByCategoryItem) {
    if (this.recipesByCategory == null) {
      this.recipesByCategory = new HashMap<>();
    }
    this.recipesByCategory.put(key, recipesByCategoryItem);
    return this;
  }

  /**
   * Get recipesByCategory
   * @return recipesByCategory
   */
  
  @Schema(name = "recipes_by_category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recipes_by_category")
  public Map<String, Integer> getRecipesByCategory() {
    return recipesByCategory;
  }

  public void setRecipesByCategory(Map<String, Integer> recipesByCategory) {
    this.recipesByCategory = recipesByCategory;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetKnownRecipes200Response getKnownRecipes200Response = (GetKnownRecipes200Response) o;
    return Objects.equals(this.knownRecipes, getKnownRecipes200Response.knownRecipes) &&
        Objects.equals(this.totalRecipes, getKnownRecipes200Response.totalRecipes) &&
        Objects.equals(this.recipesByCategory, getKnownRecipes200Response.recipesByCategory);
  }

  @Override
  public int hashCode() {
    return Objects.hash(knownRecipes, totalRecipes, recipesByCategory);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetKnownRecipes200Response {\n");
    sb.append("    knownRecipes: ").append(toIndentedString(knownRecipes)).append("\n");
    sb.append("    totalRecipes: ").append(toIndentedString(totalRecipes)).append("\n");
    sb.append("    recipesByCategory: ").append(toIndentedString(recipesByCategory)).append("\n");
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

