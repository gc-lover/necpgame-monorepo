package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.HumanityLossCalculationModifiers;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Расчет потери человечности при установке импланта
 */

@Schema(name = "HumanityLossCalculation", description = "Расчет потери человечности при установке импланта")

public class HumanityLossCalculation {

  private Float baseLoss;

  private @Nullable HumanityLossCalculationModifiers modifiers;

  private Float totalLoss;

  private Float newHumanityLevel;

  public HumanityLossCalculation() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public HumanityLossCalculation(Float baseLoss, Float totalLoss, Float newHumanityLevel) {
    this.baseLoss = baseLoss;
    this.totalLoss = totalLoss;
    this.newHumanityLevel = newHumanityLevel;
  }

  public HumanityLossCalculation baseLoss(Float baseLoss) {
    this.baseLoss = baseLoss;
    return this;
  }

  /**
   * Базовая потеря человечности
   * minimum: 0
   * @return baseLoss
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "base_loss", description = "Базовая потеря человечности", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("base_loss")
  public Float getBaseLoss() {
    return baseLoss;
  }

  public void setBaseLoss(Float baseLoss) {
    this.baseLoss = baseLoss;
  }

  public HumanityLossCalculation modifiers(@Nullable HumanityLossCalculationModifiers modifiers) {
    this.modifiers = modifiers;
    return this;
  }

  /**
   * Get modifiers
   * @return modifiers
   */
  @Valid 
  @Schema(name = "modifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifiers")
  public @Nullable HumanityLossCalculationModifiers getModifiers() {
    return modifiers;
  }

  public void setModifiers(@Nullable HumanityLossCalculationModifiers modifiers) {
    this.modifiers = modifiers;
  }

  public HumanityLossCalculation totalLoss(Float totalLoss) {
    this.totalLoss = totalLoss;
    return this;
  }

  /**
   * Итоговая потеря человечности
   * minimum: 0
   * @return totalLoss
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "total_loss", description = "Итоговая потеря человечности", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("total_loss")
  public Float getTotalLoss() {
    return totalLoss;
  }

  public void setTotalLoss(Float totalLoss) {
    this.totalLoss = totalLoss;
  }

  public HumanityLossCalculation newHumanityLevel(Float newHumanityLevel) {
    this.newHumanityLevel = newHumanityLevel;
    return this;
  }

  /**
   * Новый уровень человечности после потери
   * minimum: 0
   * maximum: 100
   * @return newHumanityLevel
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "new_humanity_level", description = "Новый уровень человечности после потери", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("new_humanity_level")
  public Float getNewHumanityLevel() {
    return newHumanityLevel;
  }

  public void setNewHumanityLevel(Float newHumanityLevel) {
    this.newHumanityLevel = newHumanityLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HumanityLossCalculation humanityLossCalculation = (HumanityLossCalculation) o;
    return Objects.equals(this.baseLoss, humanityLossCalculation.baseLoss) &&
        Objects.equals(this.modifiers, humanityLossCalculation.modifiers) &&
        Objects.equals(this.totalLoss, humanityLossCalculation.totalLoss) &&
        Objects.equals(this.newHumanityLevel, humanityLossCalculation.newHumanityLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseLoss, modifiers, totalLoss, newHumanityLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HumanityLossCalculation {\n");
    sb.append("    baseLoss: ").append(toIndentedString(baseLoss)).append("\n");
    sb.append("    modifiers: ").append(toIndentedString(modifiers)).append("\n");
    sb.append("    totalLoss: ").append(toIndentedString(totalLoss)).append("\n");
    sb.append("    newHumanityLevel: ").append(toIndentedString(newHumanityLevel)).append("\n");
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

