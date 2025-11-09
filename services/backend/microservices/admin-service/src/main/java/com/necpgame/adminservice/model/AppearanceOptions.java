package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.AppearanceOptionsCustomizationSlidersInner;
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
 * AppearanceOptions
 */


public class AppearanceOptions {

  @Valid
  private List<Object> bodyTypes = new ArrayList<>();

  @Valid
  private List<Object> facePresets = new ArrayList<>();

  @Valid
  private List<Object> hairstyles = new ArrayList<>();

  @Valid
  private List<@Valid AppearanceOptionsCustomizationSlidersInner> customizationSliders = new ArrayList<>();

  public AppearanceOptions bodyTypes(List<Object> bodyTypes) {
    this.bodyTypes = bodyTypes;
    return this;
  }

  public AppearanceOptions addBodyTypesItem(Object bodyTypesItem) {
    if (this.bodyTypes == null) {
      this.bodyTypes = new ArrayList<>();
    }
    this.bodyTypes.add(bodyTypesItem);
    return this;
  }

  /**
   * Get bodyTypes
   * @return bodyTypes
   */
  
  @Schema(name = "body_types", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("body_types")
  public List<Object> getBodyTypes() {
    return bodyTypes;
  }

  public void setBodyTypes(List<Object> bodyTypes) {
    this.bodyTypes = bodyTypes;
  }

  public AppearanceOptions facePresets(List<Object> facePresets) {
    this.facePresets = facePresets;
    return this;
  }

  public AppearanceOptions addFacePresetsItem(Object facePresetsItem) {
    if (this.facePresets == null) {
      this.facePresets = new ArrayList<>();
    }
    this.facePresets.add(facePresetsItem);
    return this;
  }

  /**
   * Get facePresets
   * @return facePresets
   */
  
  @Schema(name = "face_presets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("face_presets")
  public List<Object> getFacePresets() {
    return facePresets;
  }

  public void setFacePresets(List<Object> facePresets) {
    this.facePresets = facePresets;
  }

  public AppearanceOptions hairstyles(List<Object> hairstyles) {
    this.hairstyles = hairstyles;
    return this;
  }

  public AppearanceOptions addHairstylesItem(Object hairstylesItem) {
    if (this.hairstyles == null) {
      this.hairstyles = new ArrayList<>();
    }
    this.hairstyles.add(hairstylesItem);
    return this;
  }

  /**
   * Get hairstyles
   * @return hairstyles
   */
  
  @Schema(name = "hairstyles", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hairstyles")
  public List<Object> getHairstyles() {
    return hairstyles;
  }

  public void setHairstyles(List<Object> hairstyles) {
    this.hairstyles = hairstyles;
  }

  public AppearanceOptions customizationSliders(List<@Valid AppearanceOptionsCustomizationSlidersInner> customizationSliders) {
    this.customizationSliders = customizationSliders;
    return this;
  }

  public AppearanceOptions addCustomizationSlidersItem(AppearanceOptionsCustomizationSlidersInner customizationSlidersItem) {
    if (this.customizationSliders == null) {
      this.customizationSliders = new ArrayList<>();
    }
    this.customizationSliders.add(customizationSlidersItem);
    return this;
  }

  /**
   * Get customizationSliders
   * @return customizationSliders
   */
  @Valid 
  @Schema(name = "customization_sliders", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("customization_sliders")
  public List<@Valid AppearanceOptionsCustomizationSlidersInner> getCustomizationSliders() {
    return customizationSliders;
  }

  public void setCustomizationSliders(List<@Valid AppearanceOptionsCustomizationSlidersInner> customizationSliders) {
    this.customizationSliders = customizationSliders;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AppearanceOptions appearanceOptions = (AppearanceOptions) o;
    return Objects.equals(this.bodyTypes, appearanceOptions.bodyTypes) &&
        Objects.equals(this.facePresets, appearanceOptions.facePresets) &&
        Objects.equals(this.hairstyles, appearanceOptions.hairstyles) &&
        Objects.equals(this.customizationSliders, appearanceOptions.customizationSliders);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bodyTypes, facePresets, hairstyles, customizationSliders);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AppearanceOptions {\n");
    sb.append("    bodyTypes: ").append(toIndentedString(bodyTypes)).append("\n");
    sb.append("    facePresets: ").append(toIndentedString(facePresets)).append("\n");
    sb.append("    hairstyles: ").append(toIndentedString(hairstyles)).append("\n");
    sb.append("    customizationSliders: ").append(toIndentedString(customizationSliders)).append("\n");
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

