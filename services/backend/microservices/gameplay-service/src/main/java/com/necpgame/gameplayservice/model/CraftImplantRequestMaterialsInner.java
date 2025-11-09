package com.necpgame.gameplayservice.model;

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
 * CraftImplantRequestMaterialsInner
 */

@JsonTypeName("craftImplant_request_materials_inner")

public class CraftImplantRequestMaterialsInner {

  private @Nullable String materialId;

  private @Nullable Integer quantity;

  public CraftImplantRequestMaterialsInner materialId(@Nullable String materialId) {
    this.materialId = materialId;
    return this;
  }

  /**
   * Get materialId
   * @return materialId
   */
  
  @Schema(name = "material_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("material_id")
  public @Nullable String getMaterialId() {
    return materialId;
  }

  public void setMaterialId(@Nullable String materialId) {
    this.materialId = materialId;
  }

  public CraftImplantRequestMaterialsInner quantity(@Nullable Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public @Nullable Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(@Nullable Integer quantity) {
    this.quantity = quantity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CraftImplantRequestMaterialsInner craftImplantRequestMaterialsInner = (CraftImplantRequestMaterialsInner) o;
    return Objects.equals(this.materialId, craftImplantRequestMaterialsInner.materialId) &&
        Objects.equals(this.quantity, craftImplantRequestMaterialsInner.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(materialId, quantity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CraftImplantRequestMaterialsInner {\n");
    sb.append("    materialId: ").append(toIndentedString(materialId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
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

