package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
 * CraftRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CraftRequest {

  private UUID characterId;

  private String recipeId;

  private JsonNullable<String> stationId = JsonNullable.<String>undefined();

  private Integer quantity = 1;

  @Valid
  private List<UUID> useBoosts = new ArrayList<>();

  public CraftRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CraftRequest(UUID characterId, String recipeId) {
    this.characterId = characterId;
    this.recipeId = recipeId;
  }

  public CraftRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public CraftRequest recipeId(String recipeId) {
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

  public CraftRequest stationId(String stationId) {
    this.stationId = JsonNullable.of(stationId);
    return this;
  }

  /**
   * Get stationId
   * @return stationId
   */
  
  @Schema(name = "station_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("station_id")
  public JsonNullable<String> getStationId() {
    return stationId;
  }

  public void setStationId(JsonNullable<String> stationId) {
    this.stationId = stationId;
  }

  public CraftRequest quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Для mass production
   * maximum: 100
   * @return quantity
   */
  @Max(value = 100) 
  @Schema(name = "quantity", description = "Для mass production", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public CraftRequest useBoosts(List<UUID> useBoosts) {
    this.useBoosts = useBoosts;
    return this;
  }

  public CraftRequest addUseBoostsItem(UUID useBoostsItem) {
    if (this.useBoosts == null) {
      this.useBoosts = new ArrayList<>();
    }
    this.useBoosts.add(useBoostsItem);
    return this;
  }

  /**
   * Использовать буст-предметы
   * @return useBoosts
   */
  @Valid 
  @Schema(name = "use_boosts", description = "Использовать буст-предметы", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("use_boosts")
  public List<UUID> getUseBoosts() {
    return useBoosts;
  }

  public void setUseBoosts(List<UUID> useBoosts) {
    this.useBoosts = useBoosts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CraftRequest craftRequest = (CraftRequest) o;
    return Objects.equals(this.characterId, craftRequest.characterId) &&
        Objects.equals(this.recipeId, craftRequest.recipeId) &&
        equalsNullable(this.stationId, craftRequest.stationId) &&
        Objects.equals(this.quantity, craftRequest.quantity) &&
        Objects.equals(this.useBoosts, craftRequest.useBoosts);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, recipeId, hashCodeNullable(stationId), quantity, useBoosts);
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
    sb.append("class CraftRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    recipeId: ").append(toIndentedString(recipeId)).append("\n");
    sb.append("    stationId: ").append(toIndentedString(stationId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    useBoosts: ").append(toIndentedString(useBoosts)).append("\n");
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

