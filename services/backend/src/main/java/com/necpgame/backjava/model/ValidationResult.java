package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.Error;
import com.necpgame.backjava.model.Reason;
import com.necpgame.backjava.model.Warning;
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
 * Р РµР·СѓР»СЊС‚Р°С‚ РІР°Р»РёРґР°С†РёРё СѓСЃС‚Р°РЅРѕРІРєРё РёРјРїР»Р°РЅС‚Р°. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-implants-limits.md -&gt; РћРіСЂР°РЅРёС‡РµРЅРёСЏ РёРјРїР»Р°РЅС‚РѕРІ 
 */

@Schema(name = "ValidationResult", description = "Р РµР·СѓР»СЊС‚Р°С‚ РІР°Р»РёРґР°С†РёРё СѓСЃС‚Р°РЅРѕРІРєРё РёРјРїР»Р°РЅС‚Р°. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-implants-limits.md -> РћРіСЂР°РЅРёС‡РµРЅРёСЏ РёРјРїР»Р°РЅС‚РѕРІ ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:51:47.912860600+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class ValidationResult {

  private Boolean canInstall;

  @Valid
  private List<@Valid Reason> reasons = new ArrayList<>();

  @Valid
  private List<@Valid Warning> warnings = new ArrayList<>();

  @Valid
  private List<@Valid Error> errors = new ArrayList<>();

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
   * РњРѕР¶РЅРѕ Р»Рё СѓСЃС‚Р°РЅРѕРІРёС‚СЊ РёРјРїР»Р°РЅС‚
   * @return canInstall
   */
  @NotNull 
  @Schema(name = "can_install", description = "РњРѕР¶РЅРѕ Р»Рё СѓСЃС‚Р°РЅРѕРІРёС‚СЊ РёРјРїР»Р°РЅС‚", requiredMode = Schema.RequiredMode.REQUIRED)
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
   * РџСЂРёС‡РёРЅС‹ РѕС‚РєР°Р·Р° (РµСЃР»Рё can_install=false)
   * @return reasons
   */
  @Valid 
  @Schema(name = "reasons", description = "РџСЂРёС‡РёРЅС‹ РѕС‚РєР°Р·Р° (РµСЃР»Рё can_install=false)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
   * РџСЂРµРґСѓРїСЂРµР¶РґРµРЅРёСЏ
   * @return warnings
   */
  @Valid 
  @Schema(name = "warnings", description = "РџСЂРµРґСѓРїСЂРµР¶РґРµРЅРёСЏ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warnings")
  public List<@Valid Warning> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<@Valid Warning> warnings) {
    this.warnings = warnings;
  }

  public ValidationResult errors(List<@Valid Error> errors) {
    this.errors = errors;
    return this;
  }

  public ValidationResult addErrorsItem(Error errorsItem) {
    if (this.errors == null) {
      this.errors = new ArrayList<>();
    }
    this.errors.add(errorsItem);
    return this;
  }

  /**
   * РћС€РёР±РєРё РІР°Р»РёРґР°С†РёРё
   * @return errors
   */
  @Valid 
  @Schema(name = "errors", description = "РћС€РёР±РєРё РІР°Р»РёРґР°С†РёРё", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("errors")
  public List<@Valid Error> getErrors() {
    return errors;
  }

  public void setErrors(List<@Valid Error> errors) {
    this.errors = errors;
  }

  public ValidationResult slotAvailable(Boolean slotAvailable) {
    this.slotAvailable = slotAvailable;
    return this;
  }

  /**
   * Р”РѕСЃС‚СѓРїРµРЅ Р»Рё СЃР»РѕС‚ РґР»СЏ СѓСЃС‚Р°РЅРѕРІРєРё
   * @return slotAvailable
   */
  @NotNull 
  @Schema(name = "slot_available", description = "Р”РѕСЃС‚СѓРїРµРЅ Р»Рё СЃР»РѕС‚ РґР»СЏ СѓСЃС‚Р°РЅРѕРІРєРё", requiredMode = Schema.RequiredMode.REQUIRED)
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
   * РЎРѕРІРјРµСЃС‚РёРј Р»Рё РёРјРїР»Р°РЅС‚ СЃ СѓСЃС‚Р°РЅРѕРІР»РµРЅРЅС‹РјРё
   * @return compatibilityOk
   */
  @NotNull 
  @Schema(name = "compatibility_ok", description = "РЎРѕРІРјРµСЃС‚РёРј Р»Рё РёРјРїР»Р°РЅС‚ СЃ СѓСЃС‚Р°РЅРѕРІР»РµРЅРЅС‹РјРё", requiredMode = Schema.RequiredMode.REQUIRED)
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
   * РќРµ РїСЂРµРІС‹С€РµРЅ Р»Рё Р»РёРјРёС‚ РёРјРїР»Р°РЅС‚РѕРІ
   * @return limitOk
   */
  @NotNull 
  @Schema(name = "limit_ok", description = "РќРµ РїСЂРµРІС‹С€РµРЅ Р»Рё Р»РёРјРёС‚ РёРјРїР»Р°РЅС‚РѕРІ", requiredMode = Schema.RequiredMode.REQUIRED)
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
   * Р”РѕСЃС‚Р°С‚РѕС‡РЅРѕ Р»Рё СЌРЅРµСЂРіРёРё РґР»СЏ СѓСЃС‚Р°РЅРѕРІРєРё
   * @return energyOk
   */
  @NotNull 
  @Schema(name = "energy_ok", description = "Р”РѕСЃС‚Р°С‚РѕС‡РЅРѕ Р»Рё СЌРЅРµСЂРіРёРё РґР»СЏ СѓСЃС‚Р°РЅРѕРІРєРё", requiredMode = Schema.RequiredMode.REQUIRED)
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

