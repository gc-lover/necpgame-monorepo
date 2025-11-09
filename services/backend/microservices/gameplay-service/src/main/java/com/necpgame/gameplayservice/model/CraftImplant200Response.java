package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * CraftImplant200Response
 */

@JsonTypeName("craftImplant_200_response")

public class CraftImplant200Response {

  private @Nullable Boolean success;

  private @Nullable String implantId;

  private @Nullable String recipeId;

  @Valid
  private List<Object> materialsUsed = new ArrayList<>();

  private @Nullable BigDecimal experienceGained;

  public CraftImplant200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public CraftImplant200Response implantId(@Nullable String implantId) {
    this.implantId = implantId;
    return this;
  }

  /**
   * Get implantId
   * @return implantId
   */
  
  @Schema(name = "implant_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implant_id")
  public @Nullable String getImplantId() {
    return implantId;
  }

  public void setImplantId(@Nullable String implantId) {
    this.implantId = implantId;
  }

  public CraftImplant200Response recipeId(@Nullable String recipeId) {
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

  public CraftImplant200Response materialsUsed(List<Object> materialsUsed) {
    this.materialsUsed = materialsUsed;
    return this;
  }

  public CraftImplant200Response addMaterialsUsedItem(Object materialsUsedItem) {
    if (this.materialsUsed == null) {
      this.materialsUsed = new ArrayList<>();
    }
    this.materialsUsed.add(materialsUsedItem);
    return this;
  }

  /**
   * Get materialsUsed
   * @return materialsUsed
   */
  
  @Schema(name = "materials_used", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("materials_used")
  public List<Object> getMaterialsUsed() {
    return materialsUsed;
  }

  public void setMaterialsUsed(List<Object> materialsUsed) {
    this.materialsUsed = materialsUsed;
  }

  public CraftImplant200Response experienceGained(@Nullable BigDecimal experienceGained) {
    this.experienceGained = experienceGained;
    return this;
  }

  /**
   * Get experienceGained
   * @return experienceGained
   */
  @Valid 
  @Schema(name = "experience_gained", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience_gained")
  public @Nullable BigDecimal getExperienceGained() {
    return experienceGained;
  }

  public void setExperienceGained(@Nullable BigDecimal experienceGained) {
    this.experienceGained = experienceGained;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CraftImplant200Response craftImplant200Response = (CraftImplant200Response) o;
    return Objects.equals(this.success, craftImplant200Response.success) &&
        Objects.equals(this.implantId, craftImplant200Response.implantId) &&
        Objects.equals(this.recipeId, craftImplant200Response.recipeId) &&
        Objects.equals(this.materialsUsed, craftImplant200Response.materialsUsed) &&
        Objects.equals(this.experienceGained, craftImplant200Response.experienceGained);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, implantId, recipeId, materialsUsed, experienceGained);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CraftImplant200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    recipeId: ").append(toIndentedString(recipeId)).append("\n");
    sb.append("    materialsUsed: ").append(toIndentedString(materialsUsed)).append("\n");
    sb.append("    experienceGained: ").append(toIndentedString(experienceGained)).append("\n");
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

