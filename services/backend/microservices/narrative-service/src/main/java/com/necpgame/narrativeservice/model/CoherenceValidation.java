package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.CoherenceValidationConflictsInner;
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
 * CoherenceValidation
 */


public class CoherenceValidation {

  private @Nullable Boolean isCoherent;

  @Valid
  private List<@Valid CoherenceValidationConflictsInner> conflicts = new ArrayList<>();

  @Valid
  private List<String> warnings = new ArrayList<>();

  @Valid
  private List<String> recommendations = new ArrayList<>();

  public CoherenceValidation isCoherent(@Nullable Boolean isCoherent) {
    this.isCoherent = isCoherent;
    return this;
  }

  /**
   * Согласованны ли branches с choices
   * @return isCoherent
   */
  
  @Schema(name = "is_coherent", description = "Согласованны ли branches с choices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_coherent")
  public @Nullable Boolean getIsCoherent() {
    return isCoherent;
  }

  public void setIsCoherent(@Nullable Boolean isCoherent) {
    this.isCoherent = isCoherent;
  }

  public CoherenceValidation conflicts(List<@Valid CoherenceValidationConflictsInner> conflicts) {
    this.conflicts = conflicts;
    return this;
  }

  public CoherenceValidation addConflictsItem(CoherenceValidationConflictsInner conflictsItem) {
    if (this.conflicts == null) {
      this.conflicts = new ArrayList<>();
    }
    this.conflicts.add(conflictsItem);
    return this;
  }

  /**
   * Обнаруженные конфликты
   * @return conflicts
   */
  @Valid 
  @Schema(name = "conflicts", description = "Обнаруженные конфликты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conflicts")
  public List<@Valid CoherenceValidationConflictsInner> getConflicts() {
    return conflicts;
  }

  public void setConflicts(List<@Valid CoherenceValidationConflictsInner> conflicts) {
    this.conflicts = conflicts;
  }

  public CoherenceValidation warnings(List<String> warnings) {
    this.warnings = warnings;
    return this;
  }

  public CoherenceValidation addWarningsItem(String warningsItem) {
    if (this.warnings == null) {
      this.warnings = new ArrayList<>();
    }
    this.warnings.add(warningsItem);
    return this;
  }

  /**
   * Предупреждения о potential issues
   * @return warnings
   */
  
  @Schema(name = "warnings", description = "Предупреждения о potential issues", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warnings")
  public List<String> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<String> warnings) {
    this.warnings = warnings;
  }

  public CoherenceValidation recommendations(List<String> recommendations) {
    this.recommendations = recommendations;
    return this;
  }

  public CoherenceValidation addRecommendationsItem(String recommendationsItem) {
    if (this.recommendations == null) {
      this.recommendations = new ArrayList<>();
    }
    this.recommendations.add(recommendationsItem);
    return this;
  }

  /**
   * Рекомендации по исправлению
   * @return recommendations
   */
  
  @Schema(name = "recommendations", description = "Рекомендации по исправлению", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendations")
  public List<String> getRecommendations() {
    return recommendations;
  }

  public void setRecommendations(List<String> recommendations) {
    this.recommendations = recommendations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CoherenceValidation coherenceValidation = (CoherenceValidation) o;
    return Objects.equals(this.isCoherent, coherenceValidation.isCoherent) &&
        Objects.equals(this.conflicts, coherenceValidation.conflicts) &&
        Objects.equals(this.warnings, coherenceValidation.warnings) &&
        Objects.equals(this.recommendations, coherenceValidation.recommendations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isCoherent, conflicts, warnings, recommendations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CoherenceValidation {\n");
    sb.append("    isCoherent: ").append(toIndentedString(isCoherent)).append("\n");
    sb.append("    conflicts: ").append(toIndentedString(conflicts)).append("\n");
    sb.append("    warnings: ").append(toIndentedString(warnings)).append("\n");
    sb.append("    recommendations: ").append(toIndentedString(recommendations)).append("\n");
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

