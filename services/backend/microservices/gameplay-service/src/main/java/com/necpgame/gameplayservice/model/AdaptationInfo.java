package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Информация об адаптации к симптомам
 */

@Schema(name = "AdaptationInfo", description = "Информация об адаптации к симптомам")

public class AdaptationInfo {

  @Valid
  private List<UUID> adaptedSymptoms = new ArrayList<>();

  private Float adaptationLevel;

  private Float effectsReduction;

  public AdaptationInfo() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AdaptationInfo(List<UUID> adaptedSymptoms, Float adaptationLevel, Float effectsReduction) {
    this.adaptedSymptoms = adaptedSymptoms;
    this.adaptationLevel = adaptationLevel;
    this.effectsReduction = effectsReduction;
  }

  public AdaptationInfo adaptedSymptoms(List<UUID> adaptedSymptoms) {
    this.adaptedSymptoms = adaptedSymptoms;
    return this;
  }

  public AdaptationInfo addAdaptedSymptomsItem(UUID adaptedSymptomsItem) {
    if (this.adaptedSymptoms == null) {
      this.adaptedSymptoms = new ArrayList<>();
    }
    this.adaptedSymptoms.add(adaptedSymptomsItem);
    return this;
  }

  /**
   * Идентификаторы адаптированных симптомов
   * @return adaptedSymptoms
   */
  @NotNull @Valid 
  @Schema(name = "adapted_symptoms", description = "Идентификаторы адаптированных симптомов", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("adapted_symptoms")
  public List<UUID> getAdaptedSymptoms() {
    return adaptedSymptoms;
  }

  public void setAdaptedSymptoms(List<UUID> adaptedSymptoms) {
    this.adaptedSymptoms = adaptedSymptoms;
  }

  public AdaptationInfo adaptationLevel(Float adaptationLevel) {
    this.adaptationLevel = adaptationLevel;
    return this;
  }

  /**
   * Уровень адаптации
   * minimum: 0
   * maximum: 100
   * @return adaptationLevel
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "adaptation_level", description = "Уровень адаптации", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("adaptation_level")
  public Float getAdaptationLevel() {
    return adaptationLevel;
  }

  public void setAdaptationLevel(Float adaptationLevel) {
    this.adaptationLevel = adaptationLevel;
  }

  public AdaptationInfo effectsReduction(Float effectsReduction) {
    this.effectsReduction = effectsReduction;
    return this;
  }

  /**
   * Снижение влияния симптомов
   * minimum: 0
   * maximum: 100
   * @return effectsReduction
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "effects_reduction", description = "Снижение влияния симптомов", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("effects_reduction")
  public Float getEffectsReduction() {
    return effectsReduction;
  }

  public void setEffectsReduction(Float effectsReduction) {
    this.effectsReduction = effectsReduction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AdaptationInfo adaptationInfo = (AdaptationInfo) o;
    return Objects.equals(this.adaptedSymptoms, adaptationInfo.adaptedSymptoms) &&
        Objects.equals(this.adaptationLevel, adaptationInfo.adaptationLevel) &&
        Objects.equals(this.effectsReduction, adaptationInfo.effectsReduction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(adaptedSymptoms, adaptationLevel, effectsReduction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdaptationInfo {\n");
    sb.append("    adaptedSymptoms: ").append(toIndentedString(adaptedSymptoms)).append("\n");
    sb.append("    adaptationLevel: ").append(toIndentedString(adaptationLevel)).append("\n");
    sb.append("    effectsReduction: ").append(toIndentedString(effectsReduction)).append("\n");
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

