package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LearnRecipeRequest
 */

@JsonTypeName("learnRecipe_request")

public class LearnRecipeRequest {

  private @Nullable String recipeId;

  private @Nullable UUID blueprintItemId;

  public LearnRecipeRequest recipeId(@Nullable String recipeId) {
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

  public LearnRecipeRequest blueprintItemId(@Nullable UUID blueprintItemId) {
    this.blueprintItemId = blueprintItemId;
    return this;
  }

  /**
   * ID чертежа в инвентаре
   * @return blueprintItemId
   */
  @Valid 
  @Schema(name = "blueprint_item_id", description = "ID чертежа в инвентаре", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("blueprint_item_id")
  public @Nullable UUID getBlueprintItemId() {
    return blueprintItemId;
  }

  public void setBlueprintItemId(@Nullable UUID blueprintItemId) {
    this.blueprintItemId = blueprintItemId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LearnRecipeRequest learnRecipeRequest = (LearnRecipeRequest) o;
    return Objects.equals(this.recipeId, learnRecipeRequest.recipeId) &&
        Objects.equals(this.blueprintItemId, learnRecipeRequest.blueprintItemId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recipeId, blueprintItemId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LearnRecipeRequest {\n");
    sb.append("    recipeId: ").append(toIndentedString(recipeId)).append("\n");
    sb.append("    blueprintItemId: ").append(toIndentedString(blueprintItemId)).append("\n");
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

