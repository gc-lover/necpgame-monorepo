package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CraftItemRequest
 */

@JsonTypeName("craftItem_request")

public class CraftItemRequest {

  private String characterId;

  private String recipeId;

  private @Nullable String craftingStationId;

  public CraftItemRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CraftItemRequest(String characterId, String recipeId) {
    this.characterId = characterId;
    this.recipeId = recipeId;
  }

  public CraftItemRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public CraftItemRequest recipeId(String recipeId) {
    this.recipeId = recipeId;
    return this;
  }

  /**
   * Get recipeId
   * @return recipeId
   */
  @NotNull 
  @Schema(name = "recipe_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("recipe_id")
  public String getRecipeId() {
    return recipeId;
  }

  public void setRecipeId(String recipeId) {
    this.recipeId = recipeId;
  }

  public CraftItemRequest craftingStationId(@Nullable String craftingStationId) {
    this.craftingStationId = craftingStationId;
    return this;
  }

  /**
   * ID станции крафта (если требуется)
   * @return craftingStationId
   */
  
  @Schema(name = "crafting_station_id", description = "ID станции крафта (если требуется)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("crafting_station_id")
  public @Nullable String getCraftingStationId() {
    return craftingStationId;
  }

  public void setCraftingStationId(@Nullable String craftingStationId) {
    this.craftingStationId = craftingStationId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CraftItemRequest craftItemRequest = (CraftItemRequest) o;
    return Objects.equals(this.characterId, craftItemRequest.characterId) &&
        Objects.equals(this.recipeId, craftItemRequest.recipeId) &&
        Objects.equals(this.craftingStationId, craftItemRequest.craftingStationId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, recipeId, craftingStationId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CraftItemRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    recipeId: ").append(toIndentedString(recipeId)).append("\n");
    sb.append("    craftingStationId: ").append(toIndentedString(craftingStationId)).append("\n");
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

