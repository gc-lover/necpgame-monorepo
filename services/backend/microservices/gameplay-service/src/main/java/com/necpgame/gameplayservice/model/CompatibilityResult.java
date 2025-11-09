package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.CompatibilityResultConflictsInner;
import com.necpgame.gameplayservice.model.CompatibilityResultSetBonusesInner;
import com.necpgame.gameplayservice.model.CompatibilityResultSynergiesInner;
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
 * CompatibilityResult
 */


public class CompatibilityResult {

  private @Nullable Boolean compatible;

  @Valid
  private List<@Valid CompatibilityResultConflictsInner> conflicts = new ArrayList<>();

  @Valid
  private List<@Valid CompatibilityResultSetBonusesInner> setBonuses = new ArrayList<>();

  @Valid
  private List<@Valid CompatibilityResultSynergiesInner> synergies = new ArrayList<>();

  public CompatibilityResult compatible(@Nullable Boolean compatible) {
    this.compatible = compatible;
    return this;
  }

  /**
   * Get compatible
   * @return compatible
   */
  
  @Schema(name = "compatible", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("compatible")
  public @Nullable Boolean getCompatible() {
    return compatible;
  }

  public void setCompatible(@Nullable Boolean compatible) {
    this.compatible = compatible;
  }

  public CompatibilityResult conflicts(List<@Valid CompatibilityResultConflictsInner> conflicts) {
    this.conflicts = conflicts;
    return this;
  }

  public CompatibilityResult addConflictsItem(CompatibilityResultConflictsInner conflictsItem) {
    if (this.conflicts == null) {
      this.conflicts = new ArrayList<>();
    }
    this.conflicts.add(conflictsItem);
    return this;
  }

  /**
   * Get conflicts
   * @return conflicts
   */
  @Valid 
  @Schema(name = "conflicts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conflicts")
  public List<@Valid CompatibilityResultConflictsInner> getConflicts() {
    return conflicts;
  }

  public void setConflicts(List<@Valid CompatibilityResultConflictsInner> conflicts) {
    this.conflicts = conflicts;
  }

  public CompatibilityResult setBonuses(List<@Valid CompatibilityResultSetBonusesInner> setBonuses) {
    this.setBonuses = setBonuses;
    return this;
  }

  public CompatibilityResult addSetBonusesItem(CompatibilityResultSetBonusesInner setBonusesItem) {
    if (this.setBonuses == null) {
      this.setBonuses = new ArrayList<>();
    }
    this.setBonuses.add(setBonusesItem);
    return this;
  }

  /**
   * Get setBonuses
   * @return setBonuses
   */
  @Valid 
  @Schema(name = "set_bonuses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("set_bonuses")
  public List<@Valid CompatibilityResultSetBonusesInner> getSetBonuses() {
    return setBonuses;
  }

  public void setSetBonuses(List<@Valid CompatibilityResultSetBonusesInner> setBonuses) {
    this.setBonuses = setBonuses;
  }

  public CompatibilityResult synergies(List<@Valid CompatibilityResultSynergiesInner> synergies) {
    this.synergies = synergies;
    return this;
  }

  public CompatibilityResult addSynergiesItem(CompatibilityResultSynergiesInner synergiesItem) {
    if (this.synergies == null) {
      this.synergies = new ArrayList<>();
    }
    this.synergies.add(synergiesItem);
    return this;
  }

  /**
   * Get synergies
   * @return synergies
   */
  @Valid 
  @Schema(name = "synergies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("synergies")
  public List<@Valid CompatibilityResultSynergiesInner> getSynergies() {
    return synergies;
  }

  public void setSynergies(List<@Valid CompatibilityResultSynergiesInner> synergies) {
    this.synergies = synergies;
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
    return Objects.equals(this.compatible, compatibilityResult.compatible) &&
        Objects.equals(this.conflicts, compatibilityResult.conflicts) &&
        Objects.equals(this.setBonuses, compatibilityResult.setBonuses) &&
        Objects.equals(this.synergies, compatibilityResult.synergies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(compatible, conflicts, setBonuses, synergies);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompatibilityResult {\n");
    sb.append("    compatible: ").append(toIndentedString(compatible)).append("\n");
    sb.append("    conflicts: ").append(toIndentedString(conflicts)).append("\n");
    sb.append("    setBonuses: ").append(toIndentedString(setBonuses)).append("\n");
    sb.append("    synergies: ").append(toIndentedString(synergies)).append("\n");
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

