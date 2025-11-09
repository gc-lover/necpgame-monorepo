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
 * Модификаторы потери
 */

@Schema(name = "HumanityLossCalculation_modifiers", description = "Модификаторы потери")
@JsonTypeName("HumanityLossCalculation_modifiers")

public class HumanityLossCalculationModifiers {

  private @Nullable Float typeModifier;

  private @Nullable Float qualityModifier;

  private @Nullable Float installerModifier;

  private @Nullable Float compatibilityModifier;

  private @Nullable Float intensityModifier;

  public HumanityLossCalculationModifiers typeModifier(@Nullable Float typeModifier) {
    this.typeModifier = typeModifier;
    return this;
  }

  /**
   * Get typeModifier
   * @return typeModifier
   */
  
  @Schema(name = "type_modifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type_modifier")
  public @Nullable Float getTypeModifier() {
    return typeModifier;
  }

  public void setTypeModifier(@Nullable Float typeModifier) {
    this.typeModifier = typeModifier;
  }

  public HumanityLossCalculationModifiers qualityModifier(@Nullable Float qualityModifier) {
    this.qualityModifier = qualityModifier;
    return this;
  }

  /**
   * Get qualityModifier
   * @return qualityModifier
   */
  
  @Schema(name = "quality_modifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quality_modifier")
  public @Nullable Float getQualityModifier() {
    return qualityModifier;
  }

  public void setQualityModifier(@Nullable Float qualityModifier) {
    this.qualityModifier = qualityModifier;
  }

  public HumanityLossCalculationModifiers installerModifier(@Nullable Float installerModifier) {
    this.installerModifier = installerModifier;
    return this;
  }

  /**
   * Get installerModifier
   * @return installerModifier
   */
  
  @Schema(name = "installer_modifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("installer_modifier")
  public @Nullable Float getInstallerModifier() {
    return installerModifier;
  }

  public void setInstallerModifier(@Nullable Float installerModifier) {
    this.installerModifier = installerModifier;
  }

  public HumanityLossCalculationModifiers compatibilityModifier(@Nullable Float compatibilityModifier) {
    this.compatibilityModifier = compatibilityModifier;
    return this;
  }

  /**
   * Get compatibilityModifier
   * @return compatibilityModifier
   */
  
  @Schema(name = "compatibility_modifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("compatibility_modifier")
  public @Nullable Float getCompatibilityModifier() {
    return compatibilityModifier;
  }

  public void setCompatibilityModifier(@Nullable Float compatibilityModifier) {
    this.compatibilityModifier = compatibilityModifier;
  }

  public HumanityLossCalculationModifiers intensityModifier(@Nullable Float intensityModifier) {
    this.intensityModifier = intensityModifier;
    return this;
  }

  /**
   * Get intensityModifier
   * @return intensityModifier
   */
  
  @Schema(name = "intensity_modifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("intensity_modifier")
  public @Nullable Float getIntensityModifier() {
    return intensityModifier;
  }

  public void setIntensityModifier(@Nullable Float intensityModifier) {
    this.intensityModifier = intensityModifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HumanityLossCalculationModifiers humanityLossCalculationModifiers = (HumanityLossCalculationModifiers) o;
    return Objects.equals(this.typeModifier, humanityLossCalculationModifiers.typeModifier) &&
        Objects.equals(this.qualityModifier, humanityLossCalculationModifiers.qualityModifier) &&
        Objects.equals(this.installerModifier, humanityLossCalculationModifiers.installerModifier) &&
        Objects.equals(this.compatibilityModifier, humanityLossCalculationModifiers.compatibilityModifier) &&
        Objects.equals(this.intensityModifier, humanityLossCalculationModifiers.intensityModifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(typeModifier, qualityModifier, installerModifier, compatibilityModifier, intensityModifier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HumanityLossCalculationModifiers {\n");
    sb.append("    typeModifier: ").append(toIndentedString(typeModifier)).append("\n");
    sb.append("    qualityModifier: ").append(toIndentedString(qualityModifier)).append("\n");
    sb.append("    installerModifier: ").append(toIndentedString(installerModifier)).append("\n");
    sb.append("    compatibilityModifier: ").append(toIndentedString(compatibilityModifier)).append("\n");
    sb.append("    intensityModifier: ").append(toIndentedString(intensityModifier)).append("\n");
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

