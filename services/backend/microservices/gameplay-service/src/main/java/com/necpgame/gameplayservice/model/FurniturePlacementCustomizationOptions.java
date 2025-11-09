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
 * FurniturePlacementCustomizationOptions
 */

@JsonTypeName("FurniturePlacement_customizationOptions")

public class FurniturePlacementCustomizationOptions {

  private @Nullable String color;

  private @Nullable String variant;

  public FurniturePlacementCustomizationOptions color(@Nullable String color) {
    this.color = color;
    return this;
  }

  /**
   * Get color
   * @return color
   */
  
  @Schema(name = "color", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("color")
  public @Nullable String getColor() {
    return color;
  }

  public void setColor(@Nullable String color) {
    this.color = color;
  }

  public FurniturePlacementCustomizationOptions variant(@Nullable String variant) {
    this.variant = variant;
    return this;
  }

  /**
   * Get variant
   * @return variant
   */
  
  @Schema(name = "variant", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("variant")
  public @Nullable String getVariant() {
    return variant;
  }

  public void setVariant(@Nullable String variant) {
    this.variant = variant;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FurniturePlacementCustomizationOptions furniturePlacementCustomizationOptions = (FurniturePlacementCustomizationOptions) o;
    return Objects.equals(this.color, furniturePlacementCustomizationOptions.color) &&
        Objects.equals(this.variant, furniturePlacementCustomizationOptions.variant);
  }

  @Override
  public int hashCode() {
    return Objects.hash(color, variant);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FurniturePlacementCustomizationOptions {\n");
    sb.append("    color: ").append(toIndentedString(color)).append("\n");
    sb.append("    variant: ").append(toIndentedString(variant)).append("\n");
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

