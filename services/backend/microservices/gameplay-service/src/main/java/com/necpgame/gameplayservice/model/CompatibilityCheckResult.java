package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.CompatibilityCheckResultConflictsInner;
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
 * CompatibilityCheckResult
 */


public class CompatibilityCheckResult {

  private @Nullable Boolean compatible;

  @Valid
  private List<@Valid CompatibilityCheckResultConflictsInner> conflicts = new ArrayList<>();

  private @Nullable Boolean canInstall;

  @Valid
  private List<String> warnings = new ArrayList<>();

  public CompatibilityCheckResult compatible(@Nullable Boolean compatible) {
    this.compatible = compatible;
    return this;
  }

  /**
   * Совместимы ли импланты
   * @return compatible
   */
  
  @Schema(name = "compatible", description = "Совместимы ли импланты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("compatible")
  public @Nullable Boolean getCompatible() {
    return compatible;
  }

  public void setCompatible(@Nullable Boolean compatible) {
    this.compatible = compatible;
  }

  public CompatibilityCheckResult conflicts(List<@Valid CompatibilityCheckResultConflictsInner> conflicts) {
    this.conflicts = conflicts;
    return this;
  }

  public CompatibilityCheckResult addConflictsItem(CompatibilityCheckResultConflictsInner conflictsItem) {
    if (this.conflicts == null) {
      this.conflicts = new ArrayList<>();
    }
    this.conflicts.add(conflictsItem);
    return this;
  }

  /**
   * Список конфликтов
   * @return conflicts
   */
  @Valid 
  @Schema(name = "conflicts", description = "Список конфликтов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conflicts")
  public List<@Valid CompatibilityCheckResultConflictsInner> getConflicts() {
    return conflicts;
  }

  public void setConflicts(List<@Valid CompatibilityCheckResultConflictsInner> conflicts) {
    this.conflicts = conflicts;
  }

  public CompatibilityCheckResult canInstall(@Nullable Boolean canInstall) {
    this.canInstall = canInstall;
    return this;
  }

  /**
   * Можно ли установить все импланты
   * @return canInstall
   */
  
  @Schema(name = "can_install", description = "Можно ли установить все импланты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("can_install")
  public @Nullable Boolean getCanInstall() {
    return canInstall;
  }

  public void setCanInstall(@Nullable Boolean canInstall) {
    this.canInstall = canInstall;
  }

  public CompatibilityCheckResult warnings(List<String> warnings) {
    this.warnings = warnings;
    return this;
  }

  public CompatibilityCheckResult addWarningsItem(String warningsItem) {
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
  
  @Schema(name = "warnings", description = "Предупреждения", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warnings")
  public List<String> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<String> warnings) {
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
    CompatibilityCheckResult compatibilityCheckResult = (CompatibilityCheckResult) o;
    return Objects.equals(this.compatible, compatibilityCheckResult.compatible) &&
        Objects.equals(this.conflicts, compatibilityCheckResult.conflicts) &&
        Objects.equals(this.canInstall, compatibilityCheckResult.canInstall) &&
        Objects.equals(this.warnings, compatibilityCheckResult.warnings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(compatible, conflicts, canInstall, warnings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompatibilityCheckResult {\n");
    sb.append("    compatible: ").append(toIndentedString(compatible)).append("\n");
    sb.append("    conflicts: ").append(toIndentedString(conflicts)).append("\n");
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

