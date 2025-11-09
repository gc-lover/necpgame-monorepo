package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.Recipe;
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
 * GetCraftingRecipes200Response
 */

@JsonTypeName("getCraftingRecipes_200_response")

public class GetCraftingRecipes200Response {

  @Valid
  private List<@Valid Recipe> recipes = new ArrayList<>();

  public GetCraftingRecipes200Response recipes(List<@Valid Recipe> recipes) {
    this.recipes = recipes;
    return this;
  }

  public GetCraftingRecipes200Response addRecipesItem(Recipe recipesItem) {
    if (this.recipes == null) {
      this.recipes = new ArrayList<>();
    }
    this.recipes.add(recipesItem);
    return this;
  }

  /**
   * Get recipes
   * @return recipes
   */
  @Valid 
  @Schema(name = "recipes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recipes")
  public List<@Valid Recipe> getRecipes() {
    return recipes;
  }

  public void setRecipes(List<@Valid Recipe> recipes) {
    this.recipes = recipes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCraftingRecipes200Response getCraftingRecipes200Response = (GetCraftingRecipes200Response) o;
    return Objects.equals(this.recipes, getCraftingRecipes200Response.recipes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recipes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCraftingRecipes200Response {\n");
    sb.append("    recipes: ").append(toIndentedString(recipes)).append("\n");
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

