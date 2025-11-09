package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.CraftImplantRequestMaterialsInner;
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
 * CraftImplantRequest
 */

@JsonTypeName("craftImplant_request")

public class CraftImplantRequest {

  private String characterId;

  private String implantId;

  private String recipeId;

  @Valid
  private List<@Valid CraftImplantRequestMaterialsInner> materials = new ArrayList<>();

  public CraftImplantRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CraftImplantRequest(String characterId, String implantId, String recipeId) {
    this.characterId = characterId;
    this.implantId = implantId;
    this.recipeId = recipeId;
  }

  public CraftImplantRequest characterId(String characterId) {
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

  public CraftImplantRequest implantId(String implantId) {
    this.implantId = implantId;
    return this;
  }

  /**
   * Get implantId
   * @return implantId
   */
  @NotNull 
  @Schema(name = "implant_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("implant_id")
  public String getImplantId() {
    return implantId;
  }

  public void setImplantId(String implantId) {
    this.implantId = implantId;
  }

  public CraftImplantRequest recipeId(String recipeId) {
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

  public CraftImplantRequest materials(List<@Valid CraftImplantRequestMaterialsInner> materials) {
    this.materials = materials;
    return this;
  }

  public CraftImplantRequest addMaterialsItem(CraftImplantRequestMaterialsInner materialsItem) {
    if (this.materials == null) {
      this.materials = new ArrayList<>();
    }
    this.materials.add(materialsItem);
    return this;
  }

  /**
   * Get materials
   * @return materials
   */
  @Valid 
  @Schema(name = "materials", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("materials")
  public List<@Valid CraftImplantRequestMaterialsInner> getMaterials() {
    return materials;
  }

  public void setMaterials(List<@Valid CraftImplantRequestMaterialsInner> materials) {
    this.materials = materials;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CraftImplantRequest craftImplantRequest = (CraftImplantRequest) o;
    return Objects.equals(this.characterId, craftImplantRequest.characterId) &&
        Objects.equals(this.implantId, craftImplantRequest.implantId) &&
        Objects.equals(this.recipeId, craftImplantRequest.recipeId) &&
        Objects.equals(this.materials, craftImplantRequest.materials);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, implantId, recipeId, materials);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CraftImplantRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    recipeId: ").append(toIndentedString(recipeId)).append("\n");
    sb.append("    materials: ").append(toIndentedString(materials)).append("\n");
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

