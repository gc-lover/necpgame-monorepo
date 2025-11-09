package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.Conflict;
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
 * Р РµР·СѓР»СЊС‚Р°С‚ РїСЂРѕРІРµСЂРєРё СЃРѕРІРјРµСЃС‚РёРјРѕСЃС‚Рё РёРјРїР»Р°РЅС‚Р°. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-implants-limits.md -&gt; РЎРѕРІРјРµСЃС‚РёРјРѕСЃС‚СЊ 
 */

@Schema(name = "CompatibilityResult", description = "Р РµР·СѓР»СЊС‚Р°С‚ РїСЂРѕРІРµСЂРєРё СЃРѕРІРјРµСЃС‚РёРјРѕСЃС‚Рё РёРјРїР»Р°РЅС‚Р°. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-implants-limits.md -> РЎРѕРІРјРµСЃС‚РёРјРѕСЃС‚СЊ ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:51:47.912860600+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class CompatibilityResult {

  private Boolean isCompatible;

  @Valid
  private List<@Valid Conflict> conflicts = new ArrayList<>();

  @Valid
  private List<@Valid Warning> warnings = new ArrayList<>();

  public CompatibilityResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CompatibilityResult(Boolean isCompatible) {
    this.isCompatible = isCompatible;
  }

  public CompatibilityResult isCompatible(Boolean isCompatible) {
    this.isCompatible = isCompatible;
    return this;
  }

  /**
   * РЎРѕРІРјРµСЃС‚РёРј Р»Рё РёРјРїР»Р°РЅС‚ СЃ СѓСЃС‚Р°РЅРѕРІР»РµРЅРЅС‹РјРё
   * @return isCompatible
   */
  @NotNull 
  @Schema(name = "is_compatible", description = "РЎРѕРІРјРµСЃС‚РёРј Р»Рё РёРјРїР»Р°РЅС‚ СЃ СѓСЃС‚Р°РЅРѕРІР»РµРЅРЅС‹РјРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("is_compatible")
  public Boolean getIsCompatible() {
    return isCompatible;
  }

  public void setIsCompatible(Boolean isCompatible) {
    this.isCompatible = isCompatible;
  }

  public CompatibilityResult conflicts(List<@Valid Conflict> conflicts) {
    this.conflicts = conflicts;
    return this;
  }

  public CompatibilityResult addConflictsItem(Conflict conflictsItem) {
    if (this.conflicts == null) {
      this.conflicts = new ArrayList<>();
    }
    this.conflicts.add(conflictsItem);
    return this;
  }

  /**
   * РљРѕРЅС„Р»РёРєС‚С‹ СЃРѕРІРјРµСЃС‚РёРјРѕСЃС‚Рё
   * @return conflicts
   */
  @Valid 
  @Schema(name = "conflicts", description = "РљРѕРЅС„Р»РёРєС‚С‹ СЃРѕРІРјРµСЃС‚РёРјРѕСЃС‚Рё", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conflicts")
  public List<@Valid Conflict> getConflicts() {
    return conflicts;
  }

  public void setConflicts(List<@Valid Conflict> conflicts) {
    this.conflicts = conflicts;
  }

  public CompatibilityResult warnings(List<@Valid Warning> warnings) {
    this.warnings = warnings;
    return this;
  }

  public CompatibilityResult addWarningsItem(Warning warningsItem) {
    if (this.warnings == null) {
      this.warnings = new ArrayList<>();
    }
    this.warnings.add(warningsItem);
    return this;
  }

  /**
   * РџСЂРµРґСѓРїСЂРµР¶РґРµРЅРёСЏ Рѕ СЃРѕРІРјРµСЃС‚РёРјРѕСЃС‚Рё
   * @return warnings
   */
  @Valid 
  @Schema(name = "warnings", description = "РџСЂРµРґСѓРїСЂРµР¶РґРµРЅРёСЏ Рѕ СЃРѕРІРјРµСЃС‚РёРјРѕСЃС‚Рё", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    CompatibilityResult compatibilityResult = (CompatibilityResult) o;
    return Objects.equals(this.isCompatible, compatibilityResult.isCompatible) &&
        Objects.equals(this.conflicts, compatibilityResult.conflicts) &&
        Objects.equals(this.warnings, compatibilityResult.warnings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isCompatible, conflicts, warnings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompatibilityResult {\n");
    sb.append("    isCompatible: ").append(toIndentedString(isCompatible)).append("\n");
    sb.append("    conflicts: ").append(toIndentedString(conflicts)).append("\n");
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

