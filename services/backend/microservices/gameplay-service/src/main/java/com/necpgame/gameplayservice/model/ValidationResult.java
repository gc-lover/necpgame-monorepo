package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.Error1;
import com.necpgame.gameplayservice.model.Reason;
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
 * Результат валидации установки импланта. Источник: .BRAIN/02-gameplay/combat/combat-implants-limits.md -&gt; Ограничения имплантов 
 */

@Schema(name = "ValidationResult", description = "Результат валидации установки импланта. Источник: .BRAIN/02-gameplay/combat/combat-implants-limits.md -> Ограничения имплантов ")

public class ValidationResult {

  private Boolean canInstall;

  @Valid
  private List<@Valid Reason> reasons = new ArrayList<>();

  @Valid
  private List<@Valid Warning> warnings = new ArrayList<>();

  @Valid
  private List<@Valid Error1> errors = new ArrayList<>();

  private Boolean slotAvailable;

  private Boolean compatibilityOk;

  private Boolean limitOk;

  private Boolean energyOk;

  public ValidationResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ValidationResult(Boolean canInstall, Boolean slotAvailable, Boolean compatibilityOk, Boolean limitOk, Boolean energyOk) {
    this.canInstall = canInstall;
    this.slotAvailable = slotAvailable;
    this.compatibilityOk = compatibilityOk;
    this.limitOk = limitOk;
    this.energyOk = energyOk;
  }

  public ValidationResult canInstall(Boolean canInstall) {
    this.canInstall = canInstall;
    return this;
  }

  /**
   * Можно ли установить имплант
   * @return canInstall
   */
  @NotNull 
  @Schema(name = "can_install", description = "Можно ли установить имплант", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("can_install")
  public Boolean getCanInstall() {
    return canInstall;
  }

  public void setCanInstall(Boolean canInstall) {
    this.canInstall = canInstall;
  }

  public ValidationResult reasons(List<@Valid Reason> reasons) {
    this.reasons = reasons;
    return this;
  }

  public ValidationResult addReasonsItem(Reason reasonsItem) {
    if (this.reasons == null) {
      this.reasons = new ArrayList<>();
    }
    this.reasons.add(reasonsItem);
    return this;
  }

  /**
   * Причины отказа (если can_install=false)
   * @return reasons
   */
  @Valid 
  @Schema(name = "reasons", description = "Причины отказа (если can_install=false)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reasons")
  public List<@Valid Reason> getReasons() {
    return reasons;
  }

  public void setReasons(List<@Valid Reason> reasons) {
    this.reasons = reasons;
  }

  public ValidationResult warnings(List<@Valid Warning> warnings) {
    this.warnings = warnings;
    return this;
  }

  public ValidationResult addWarningsItem(Warning warningsItem) {
    if (this.warnings == null) {
      this.warnings = new ArrayList<>();
    }
    this.warnings.add(warningsItem);
    return this;
  }

  /**
   * Предупреждения
   * @return warnings
   */
  @Valid 
  @Schema(name = "warnings", description = "Предупреждения", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warnings")
  public List<@Valid Warning> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<@Valid Warning> warnings) {
    this.warnings = warnings;
  }

  public ValidationResult errors(List<@Valid Error1> errors) {
    this.errors = errors;
    return this;
  }

  public ValidationResult addErrorsItem(Error1 errorsItem) {
    if (this.errors == null) {
      this.errors = new ArrayList<>();
    }
    this.errors.add(errorsItem);
    return this;
  }

  /**
   * Ошибки валидации
   * @return errors
   */
  @Valid 
  @Schema(name = "errors", description = "Ошибки валидации", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("errors")
  public List<@Valid Error1> getErrors() {
    return errors;
  }

  public void setErrors(List<@Valid Error1> errors) {
    this.errors = errors;
  }

  public ValidationResult slotAvailable(Boolean slotAvailable) {
    this.slotAvailable = slotAvailable;
    return this;
  }

  /**
   * Доступен ли слот для установки
   * @return slotAvailable
   */
  @NotNull 
  @Schema(name = "slot_available", description = "Доступен ли слот для установки", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slot_available")
  public Boolean getSlotAvailable() {
    return slotAvailable;
  }

  public void setSlotAvailable(Boolean slotAvailable) {
    this.slotAvailable = slotAvailable;
  }

  public ValidationResult compatibilityOk(Boolean compatibilityOk) {
    this.compatibilityOk = compatibilityOk;
    return this;
  }

  /**
   * Совместим ли имплант с установленными
   * @return compatibilityOk
   */
  @NotNull 
  @Schema(name = "compatibility_ok", description = "Совместим ли имплант с установленными", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("compatibility_ok")
  public Boolean getCompatibilityOk() {
    return compatibilityOk;
  }

  public void setCompatibilityOk(Boolean compatibilityOk) {
    this.compatibilityOk = compatibilityOk;
  }

  public ValidationResult limitOk(Boolean limitOk) {
    this.limitOk = limitOk;
    return this;
  }

  /**
   * Не превышен ли лимит имплантов
   * @return limitOk
   */
  @NotNull 
  @Schema(name = "limit_ok", description = "Не превышен ли лимит имплантов", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("limit_ok")
  public Boolean getLimitOk() {
    return limitOk;
  }

  public void setLimitOk(Boolean limitOk) {
    this.limitOk = limitOk;
  }

  public ValidationResult energyOk(Boolean energyOk) {
    this.energyOk = energyOk;
    return this;
  }

  /**
   * Достаточно ли энергии для установки
   * @return energyOk
   */
  @NotNull 
  @Schema(name = "energy_ok", description = "Достаточно ли энергии для установки", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("energy_ok")
  public Boolean getEnergyOk() {
    return energyOk;
  }

  public void setEnergyOk(Boolean energyOk) {
    this.energyOk = energyOk;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidationResult validationResult = (ValidationResult) o;
    return Objects.equals(this.canInstall, validationResult.canInstall) &&
        Objects.equals(this.reasons, validationResult.reasons) &&
        Objects.equals(this.warnings, validationResult.warnings) &&
        Objects.equals(this.errors, validationResult.errors) &&
        Objects.equals(this.slotAvailable, validationResult.slotAvailable) &&
        Objects.equals(this.compatibilityOk, validationResult.compatibilityOk) &&
        Objects.equals(this.limitOk, validationResult.limitOk) &&
        Objects.equals(this.energyOk, validationResult.energyOk);
  }

  @Override
  public int hashCode() {
    return Objects.hash(canInstall, reasons, warnings, errors, slotAvailable, compatibilityOk, limitOk, energyOk);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidationResult {\n");
    sb.append("    canInstall: ").append(toIndentedString(canInstall)).append("\n");
    sb.append("    reasons: ").append(toIndentedString(reasons)).append("\n");
    sb.append("    warnings: ").append(toIndentedString(warnings)).append("\n");
    sb.append("    errors: ").append(toIndentedString(errors)).append("\n");
    sb.append("    slotAvailable: ").append(toIndentedString(slotAvailable)).append("\n");
    sb.append("    compatibilityOk: ").append(toIndentedString(compatibilityOk)).append("\n");
    sb.append("    limitOk: ").append(toIndentedString(limitOk)).append("\n");
    sb.append("    energyOk: ").append(toIndentedString(energyOk)).append("\n");
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

