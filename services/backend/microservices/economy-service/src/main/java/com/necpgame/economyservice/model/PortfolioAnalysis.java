package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.PortfolioAnalysisPerformanceMetrics;
import com.necpgame.economyservice.model.PortfolioAnalysisRiskBreakdown;
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
 * PortfolioAnalysis
 */


public class PortfolioAnalysis {

  private @Nullable UUID characterId;

  private @Nullable Float riskScore;

  private @Nullable Float diversificationScore;

  @Valid
  private List<String> recommendations = new ArrayList<>();

  private @Nullable PortfolioAnalysisRiskBreakdown riskBreakdown;

  private @Nullable PortfolioAnalysisPerformanceMetrics performanceMetrics;

  public PortfolioAnalysis characterId(@Nullable UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable UUID characterId) {
    this.characterId = characterId;
  }

  public PortfolioAnalysis riskScore(@Nullable Float riskScore) {
    this.riskScore = riskScore;
    return this;
  }

  /**
   * Общий риск портфеля (0-10)
   * @return riskScore
   */
  
  @Schema(name = "risk_score", description = "Общий риск портфеля (0-10)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("risk_score")
  public @Nullable Float getRiskScore() {
    return riskScore;
  }

  public void setRiskScore(@Nullable Float riskScore) {
    this.riskScore = riskScore;
  }

  public PortfolioAnalysis diversificationScore(@Nullable Float diversificationScore) {
    this.diversificationScore = diversificationScore;
    return this;
  }

  /**
   * Диверсификация (0-100)
   * @return diversificationScore
   */
  
  @Schema(name = "diversification_score", description = "Диверсификация (0-100)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("diversification_score")
  public @Nullable Float getDiversificationScore() {
    return diversificationScore;
  }

  public void setDiversificationScore(@Nullable Float diversificationScore) {
    this.diversificationScore = diversificationScore;
  }

  public PortfolioAnalysis recommendations(List<String> recommendations) {
    this.recommendations = recommendations;
    return this;
  }

  public PortfolioAnalysis addRecommendationsItem(String recommendationsItem) {
    if (this.recommendations == null) {
      this.recommendations = new ArrayList<>();
    }
    this.recommendations.add(recommendationsItem);
    return this;
  }

  /**
   * Get recommendations
   * @return recommendations
   */
  
  @Schema(name = "recommendations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendations")
  public List<String> getRecommendations() {
    return recommendations;
  }

  public void setRecommendations(List<String> recommendations) {
    this.recommendations = recommendations;
  }

  public PortfolioAnalysis riskBreakdown(@Nullable PortfolioAnalysisRiskBreakdown riskBreakdown) {
    this.riskBreakdown = riskBreakdown;
    return this;
  }

  /**
   * Get riskBreakdown
   * @return riskBreakdown
   */
  @Valid 
  @Schema(name = "risk_breakdown", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("risk_breakdown")
  public @Nullable PortfolioAnalysisRiskBreakdown getRiskBreakdown() {
    return riskBreakdown;
  }

  public void setRiskBreakdown(@Nullable PortfolioAnalysisRiskBreakdown riskBreakdown) {
    this.riskBreakdown = riskBreakdown;
  }

  public PortfolioAnalysis performanceMetrics(@Nullable PortfolioAnalysisPerformanceMetrics performanceMetrics) {
    this.performanceMetrics = performanceMetrics;
    return this;
  }

  /**
   * Get performanceMetrics
   * @return performanceMetrics
   */
  @Valid 
  @Schema(name = "performance_metrics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("performance_metrics")
  public @Nullable PortfolioAnalysisPerformanceMetrics getPerformanceMetrics() {
    return performanceMetrics;
  }

  public void setPerformanceMetrics(@Nullable PortfolioAnalysisPerformanceMetrics performanceMetrics) {
    this.performanceMetrics = performanceMetrics;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PortfolioAnalysis portfolioAnalysis = (PortfolioAnalysis) o;
    return Objects.equals(this.characterId, portfolioAnalysis.characterId) &&
        Objects.equals(this.riskScore, portfolioAnalysis.riskScore) &&
        Objects.equals(this.diversificationScore, portfolioAnalysis.diversificationScore) &&
        Objects.equals(this.recommendations, portfolioAnalysis.recommendations) &&
        Objects.equals(this.riskBreakdown, portfolioAnalysis.riskBreakdown) &&
        Objects.equals(this.performanceMetrics, portfolioAnalysis.performanceMetrics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, riskScore, diversificationScore, recommendations, riskBreakdown, performanceMetrics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PortfolioAnalysis {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    riskScore: ").append(toIndentedString(riskScore)).append("\n");
    sb.append("    diversificationScore: ").append(toIndentedString(diversificationScore)).append("\n");
    sb.append("    recommendations: ").append(toIndentedString(recommendations)).append("\n");
    sb.append("    riskBreakdown: ").append(toIndentedString(riskBreakdown)).append("\n");
    sb.append("    performanceMetrics: ").append(toIndentedString(performanceMetrics)).append("\n");
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

