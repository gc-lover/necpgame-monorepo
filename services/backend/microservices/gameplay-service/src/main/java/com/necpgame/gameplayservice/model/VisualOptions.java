package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * VisualOptions
 */


public class VisualOptions {

  private @Nullable String implantId;

  private @Nullable String brand;

  @Valid
  private List<String> availableColors = new ArrayList<>();

  @Valid
  private List<String> availableStyles = new ArrayList<>();

  private @Nullable String brandStyle;

  private @Nullable Boolean lightEffectsAvailable;

  public VisualOptions implantId(@Nullable String implantId) {
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

  public VisualOptions brand(@Nullable String brand) {
    this.brand = brand;
    return this;
  }

  /**
   * Get brand
   * @return brand
   */
  
  @Schema(name = "brand", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("brand")
  public @Nullable String getBrand() {
    return brand;
  }

  public void setBrand(@Nullable String brand) {
    this.brand = brand;
  }

  public VisualOptions availableColors(List<String> availableColors) {
    this.availableColors = availableColors;
    return this;
  }

  public VisualOptions addAvailableColorsItem(String availableColorsItem) {
    if (this.availableColors == null) {
      this.availableColors = new ArrayList<>();
    }
    this.availableColors.add(availableColorsItem);
    return this;
  }

  /**
   * Get availableColors
   * @return availableColors
   */
  
  @Schema(name = "available_colors", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available_colors")
  public List<String> getAvailableColors() {
    return availableColors;
  }

  public void setAvailableColors(List<String> availableColors) {
    this.availableColors = availableColors;
  }

  public VisualOptions availableStyles(List<String> availableStyles) {
    this.availableStyles = availableStyles;
    return this;
  }

  public VisualOptions addAvailableStylesItem(String availableStylesItem) {
    if (this.availableStyles == null) {
      this.availableStyles = new ArrayList<>();
    }
    this.availableStyles.add(availableStylesItem);
    return this;
  }

  /**
   * Get availableStyles
   * @return availableStyles
   */
  
  @Schema(name = "available_styles", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available_styles")
  public List<String> getAvailableStyles() {
    return availableStyles;
  }

  public void setAvailableStyles(List<String> availableStyles) {
    this.availableStyles = availableStyles;
  }

  public VisualOptions brandStyle(@Nullable String brandStyle) {
    this.brandStyle = brandStyle;
    return this;
  }

  /**
   * Рекомендуемый стиль бренда
   * @return brandStyle
   */
  
  @Schema(name = "brand_style", description = "Рекомендуемый стиль бренда", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("brand_style")
  public @Nullable String getBrandStyle() {
    return brandStyle;
  }

  public void setBrandStyle(@Nullable String brandStyle) {
    this.brandStyle = brandStyle;
  }

  public VisualOptions lightEffectsAvailable(@Nullable Boolean lightEffectsAvailable) {
    this.lightEffectsAvailable = lightEffectsAvailable;
    return this;
  }

  /**
   * Get lightEffectsAvailable
   * @return lightEffectsAvailable
   */
  
  @Schema(name = "light_effects_available", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("light_effects_available")
  public @Nullable Boolean getLightEffectsAvailable() {
    return lightEffectsAvailable;
  }

  public void setLightEffectsAvailable(@Nullable Boolean lightEffectsAvailable) {
    this.lightEffectsAvailable = lightEffectsAvailable;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VisualOptions visualOptions = (VisualOptions) o;
    return Objects.equals(this.implantId, visualOptions.implantId) &&
        Objects.equals(this.brand, visualOptions.brand) &&
        Objects.equals(this.availableColors, visualOptions.availableColors) &&
        Objects.equals(this.availableStyles, visualOptions.availableStyles) &&
        Objects.equals(this.brandStyle, visualOptions.brandStyle) &&
        Objects.equals(this.lightEffectsAvailable, visualOptions.lightEffectsAvailable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implantId, brand, availableColors, availableStyles, brandStyle, lightEffectsAvailable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VisualOptions {\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    brand: ").append(toIndentedString(brand)).append("\n");
    sb.append("    availableColors: ").append(toIndentedString(availableColors)).append("\n");
    sb.append("    availableStyles: ").append(toIndentedString(availableStyles)).append("\n");
    sb.append("    brandStyle: ").append(toIndentedString(brandStyle)).append("\n");
    sb.append("    lightEffectsAvailable: ").append(toIndentedString(lightEffectsAvailable)).append("\n");
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

