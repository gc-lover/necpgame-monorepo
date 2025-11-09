package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.IndividualLimit;
import com.necpgame.gameplayservice.model.Warning;
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
 * Расчет энергетического потребления имплантов. Источник: .BRAIN/02-gameplay/combat/combat-implants-limits.md -&gt; Энергетический лимит 
 */

@Schema(name = "EnergyCalculation", description = "Расчет энергетического потребления имплантов. Источник: .BRAIN/02-gameplay/combat/combat-implants-limits.md -> Энергетический лимит ")

public class EnergyCalculation {

  private Float totalDrain;

  @Valid
  private List<@Valid IndividualLimit> individualLimits = new ArrayList<>();

  private Boolean canInstall;

  @Valid
  private List<@Valid Warning> warnings = new ArrayList<>();

  public EnergyCalculation() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EnergyCalculation(Float totalDrain, Boolean canInstall) {
    this.totalDrain = totalDrain;
    this.canInstall = canInstall;
  }

  public EnergyCalculation totalDrain(Float totalDrain) {
    this.totalDrain = totalDrain;
    return this;
  }

  /**
   * Общее потребление энергии
   * minimum: 0
   * @return totalDrain
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "total_drain", description = "Общее потребление энергии", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("total_drain")
  public Float getTotalDrain() {
    return totalDrain;
  }

  public void setTotalDrain(Float totalDrain) {
    this.totalDrain = totalDrain;
  }

  public EnergyCalculation individualLimits(List<@Valid IndividualLimit> individualLimits) {
    this.individualLimits = individualLimits;
    return this;
  }

  public EnergyCalculation addIndividualLimitsItem(IndividualLimit individualLimitsItem) {
    if (this.individualLimits == null) {
      this.individualLimits = new ArrayList<>();
    }
    this.individualLimits.add(individualLimitsItem);
    return this;
  }

  /**
   * Индивидуальные ограничения имплантов
   * @return individualLimits
   */
  @Valid 
  @Schema(name = "individual_limits", description = "Индивидуальные ограничения имплантов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("individual_limits")
  public List<@Valid IndividualLimit> getIndividualLimits() {
    return individualLimits;
  }

  public void setIndividualLimits(List<@Valid IndividualLimit> individualLimits) {
    this.individualLimits = individualLimits;
  }

  public EnergyCalculation canInstall(Boolean canInstall) {
    this.canInstall = canInstall;
    return this;
  }

  /**
   * Можно ли установить импланты с учетом энергетического лимита
   * @return canInstall
   */
  @NotNull 
  @Schema(name = "can_install", description = "Можно ли установить импланты с учетом энергетического лимита", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("can_install")
  public Boolean getCanInstall() {
    return canInstall;
  }

  public void setCanInstall(Boolean canInstall) {
    this.canInstall = canInstall;
  }

  public EnergyCalculation warnings(List<@Valid Warning> warnings) {
    this.warnings = warnings;
    return this;
  }

  public EnergyCalculation addWarningsItem(Warning warningsItem) {
    if (this.warnings == null) {
      this.warnings = new ArrayList<>();
    }
    this.warnings.add(warningsItem);
    return this;
  }

  /**
   * Предупреждения о энергетическом потреблении
   * @return warnings
   */
  @Valid 
  @Schema(name = "warnings", description = "Предупреждения о энергетическом потреблении", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warnings")
  public List<@Valid Warning> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<@Valid Warning> warnings) {
    this.warnings = warnings;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnergyCalculation energyCalculation = (EnergyCalculation) o;
    return Objects.equals(this.totalDrain, energyCalculation.totalDrain) &&
        Objects.equals(this.individualLimits, energyCalculation.individualLimits) &&
        Objects.equals(this.canInstall, energyCalculation.canInstall) &&
        Objects.equals(this.warnings, energyCalculation.warnings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalDrain, individualLimits, canInstall, warnings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnergyCalculation {\n");
    sb.append("    totalDrain: ").append(toIndentedString(totalDrain)).append("\n");
    sb.append("    individualLimits: ").append(toIndentedString(individualLimits)).append("\n");
    sb.append("    canInstall: ").append(toIndentedString(canInstall)).append("\n");
    sb.append("    warnings: ").append(toIndentedString(warnings)).append("\n");
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

