package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Факторы, снижающие потерю человечности
 */

@Schema(name = "CalculateLossRequest_mitigating_factors", description = "Факторы, снижающие потерю человечности")
@JsonTypeName("CalculateLossRequest_mitigating_factors")

public class CalculateLossRequestMitigatingFactors {

  private @Nullable Integer cyberwareTolerance;

  /**
   * Доступный уровень медицинской поддержки
   */
  public enum MedicalSupportLevelEnum {
    NONE("none"),
    
    CLINIC("clinic"),
    
    HOSPITAL("hospital"),
    
    BLACK_CLINIC("black_clinic");

    private final String value;

    MedicalSupportLevelEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static MedicalSupportLevelEnum fromValue(String value) {
      for (MedicalSupportLevelEnum b : MedicalSupportLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable MedicalSupportLevelEnum medicalSupportLevel;

  private @Nullable Boolean preInstallationTherapy;

  public CalculateLossRequestMitigatingFactors cyberwareTolerance(@Nullable Integer cyberwareTolerance) {
    this.cyberwareTolerance = cyberwareTolerance;
    return this;
  }

  /**
   * Уровень прокачки устойчивости к кибернетике
   * minimum: 0
   * maximum: 10
   * @return cyberwareTolerance
   */
  @Min(value = 0) @Max(value = 10) 
  @Schema(name = "cyberware_tolerance", example = "3", description = "Уровень прокачки устойчивости к кибернетике", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cyberware_tolerance")
  public @Nullable Integer getCyberwareTolerance() {
    return cyberwareTolerance;
  }

  public void setCyberwareTolerance(@Nullable Integer cyberwareTolerance) {
    this.cyberwareTolerance = cyberwareTolerance;
  }

  public CalculateLossRequestMitigatingFactors medicalSupportLevel(@Nullable MedicalSupportLevelEnum medicalSupportLevel) {
    this.medicalSupportLevel = medicalSupportLevel;
    return this;
  }

  /**
   * Доступный уровень медицинской поддержки
   * @return medicalSupportLevel
   */
  
  @Schema(name = "medical_support_level", example = "clinic", description = "Доступный уровень медицинской поддержки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("medical_support_level")
  public @Nullable MedicalSupportLevelEnum getMedicalSupportLevel() {
    return medicalSupportLevel;
  }

  public void setMedicalSupportLevel(@Nullable MedicalSupportLevelEnum medicalSupportLevel) {
    this.medicalSupportLevel = medicalSupportLevel;
  }

  public CalculateLossRequestMitigatingFactors preInstallationTherapy(@Nullable Boolean preInstallationTherapy) {
    this.preInstallationTherapy = preInstallationTherapy;
    return this;
  }

  /**
   * Проходил ли персонаж терапию перед установкой импланта
   * @return preInstallationTherapy
   */
  
  @Schema(name = "pre_installation_therapy", example = "true", description = "Проходил ли персонаж терапию перед установкой импланта", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pre_installation_therapy")
  public @Nullable Boolean getPreInstallationTherapy() {
    return preInstallationTherapy;
  }

  public void setPreInstallationTherapy(@Nullable Boolean preInstallationTherapy) {
    this.preInstallationTherapy = preInstallationTherapy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculateLossRequestMitigatingFactors calculateLossRequestMitigatingFactors = (CalculateLossRequestMitigatingFactors) o;
    return Objects.equals(this.cyberwareTolerance, calculateLossRequestMitigatingFactors.cyberwareTolerance) &&
        Objects.equals(this.medicalSupportLevel, calculateLossRequestMitigatingFactors.medicalSupportLevel) &&
        Objects.equals(this.preInstallationTherapy, calculateLossRequestMitigatingFactors.preInstallationTherapy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cyberwareTolerance, medicalSupportLevel, preInstallationTherapy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculateLossRequestMitigatingFactors {\n");
    sb.append("    cyberwareTolerance: ").append(toIndentedString(cyberwareTolerance)).append("\n");
    sb.append("    medicalSupportLevel: ").append(toIndentedString(medicalSupportLevel)).append("\n");
    sb.append("    preInstallationTherapy: ").append(toIndentedString(preInstallationTherapy)).append("\n");
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

