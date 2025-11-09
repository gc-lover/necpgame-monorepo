package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.ScoringResultComponentScores;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ScoringResult
 */


public class ScoringResult {

  private @Nullable BigDecimal totalScore;

  private @Nullable ScoringResultComponentScores componentScores;

  @Valid
  private Map<String, BigDecimal> weightedScores = new HashMap<>();

  private @Nullable String explanation;

  public ScoringResult totalScore(@Nullable BigDecimal totalScore) {
    this.totalScore = totalScore;
    return this;
  }

  /**
   * Итоговый score (0-1)
   * minimum: 0
   * maximum: 1
   * @return totalScore
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "total_score", example = "0.85", description = "Итоговый score (0-1)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_score")
  public @Nullable BigDecimal getTotalScore() {
    return totalScore;
  }

  public void setTotalScore(@Nullable BigDecimal totalScore) {
    this.totalScore = totalScore;
  }

  public ScoringResult componentScores(@Nullable ScoringResultComponentScores componentScores) {
    this.componentScores = componentScores;
    return this;
  }

  /**
   * Get componentScores
   * @return componentScores
   */
  @Valid 
  @Schema(name = "component_scores", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("component_scores")
  public @Nullable ScoringResultComponentScores getComponentScores() {
    return componentScores;
  }

  public void setComponentScores(@Nullable ScoringResultComponentScores componentScores) {
    this.componentScores = componentScores;
  }

  public ScoringResult weightedScores(Map<String, BigDecimal> weightedScores) {
    this.weightedScores = weightedScores;
    return this;
  }

  public ScoringResult putWeightedScoresItem(String key, BigDecimal weightedScoresItem) {
    if (this.weightedScores == null) {
      this.weightedScores = new HashMap<>();
    }
    this.weightedScores.put(key, weightedScoresItem);
    return this;
  }

  /**
   * Взвешенные scores
   * @return weightedScores
   */
  @Valid 
  @Schema(name = "weighted_scores", description = "Взвешенные scores", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weighted_scores")
  public Map<String, BigDecimal> getWeightedScores() {
    return weightedScores;
  }

  public void setWeightedScores(Map<String, BigDecimal> weightedScores) {
    this.weightedScores = weightedScores;
  }

  public ScoringResult explanation(@Nullable String explanation) {
    this.explanation = explanation;
    return this;
  }

  /**
   * Объяснение почему этот score
   * @return explanation
   */
  
  @Schema(name = "explanation", example = "High compatibility due to matching interests and current relationship stage", description = "Объяснение почему этот score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("explanation")
  public @Nullable String getExplanation() {
    return explanation;
  }

  public void setExplanation(@Nullable String explanation) {
    this.explanation = explanation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScoringResult scoringResult = (ScoringResult) o;
    return Objects.equals(this.totalScore, scoringResult.totalScore) &&
        Objects.equals(this.componentScores, scoringResult.componentScores) &&
        Objects.equals(this.weightedScores, scoringResult.weightedScores) &&
        Objects.equals(this.explanation, scoringResult.explanation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalScore, componentScores, weightedScores, explanation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScoringResult {\n");
    sb.append("    totalScore: ").append(toIndentedString(totalScore)).append("\n");
    sb.append("    componentScores: ").append(toIndentedString(componentScores)).append("\n");
    sb.append("    weightedScores: ").append(toIndentedString(weightedScores)).append("\n");
    sb.append("    explanation: ").append(toIndentedString(explanation)).append("\n");
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

