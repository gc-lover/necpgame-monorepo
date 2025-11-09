package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.OptimizationResultExpectedImprovement;
import com.necpgame.economyservice.model.OptimizationResultOptimizedScheduleInner;
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
 * OptimizationResult
 */


public class OptimizationResult {

  private @Nullable String chainId;

  private @Nullable String goal;

  @Valid
  private List<String> recommendations = new ArrayList<>();

  @Valid
  private List<@Valid OptimizationResultOptimizedScheduleInner> optimizedSchedule = new ArrayList<>();

  private @Nullable OptimizationResultExpectedImprovement expectedImprovement;

  public OptimizationResult chainId(@Nullable String chainId) {
    this.chainId = chainId;
    return this;
  }

  /**
   * Get chainId
   * @return chainId
   */
  
  @Schema(name = "chain_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("chain_id")
  public @Nullable String getChainId() {
    return chainId;
  }

  public void setChainId(@Nullable String chainId) {
    this.chainId = chainId;
  }

  public OptimizationResult goal(@Nullable String goal) {
    this.goal = goal;
    return this;
  }

  /**
   * Get goal
   * @return goal
   */
  
  @Schema(name = "goal", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("goal")
  public @Nullable String getGoal() {
    return goal;
  }

  public void setGoal(@Nullable String goal) {
    this.goal = goal;
  }

  public OptimizationResult recommendations(List<String> recommendations) {
    this.recommendations = recommendations;
    return this;
  }

  public OptimizationResult addRecommendationsItem(String recommendationsItem) {
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

  public OptimizationResult optimizedSchedule(List<@Valid OptimizationResultOptimizedScheduleInner> optimizedSchedule) {
    this.optimizedSchedule = optimizedSchedule;
    return this;
  }

  public OptimizationResult addOptimizedScheduleItem(OptimizationResultOptimizedScheduleInner optimizedScheduleItem) {
    if (this.optimizedSchedule == null) {
      this.optimizedSchedule = new ArrayList<>();
    }
    this.optimizedSchedule.add(optimizedScheduleItem);
    return this;
  }

  /**
   * Get optimizedSchedule
   * @return optimizedSchedule
   */
  @Valid 
  @Schema(name = "optimized_schedule", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("optimized_schedule")
  public List<@Valid OptimizationResultOptimizedScheduleInner> getOptimizedSchedule() {
    return optimizedSchedule;
  }

  public void setOptimizedSchedule(List<@Valid OptimizationResultOptimizedScheduleInner> optimizedSchedule) {
    this.optimizedSchedule = optimizedSchedule;
  }

  public OptimizationResult expectedImprovement(@Nullable OptimizationResultExpectedImprovement expectedImprovement) {
    this.expectedImprovement = expectedImprovement;
    return this;
  }

  /**
   * Get expectedImprovement
   * @return expectedImprovement
   */
  @Valid 
  @Schema(name = "expected_improvement", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expected_improvement")
  public @Nullable OptimizationResultExpectedImprovement getExpectedImprovement() {
    return expectedImprovement;
  }

  public void setExpectedImprovement(@Nullable OptimizationResultExpectedImprovement expectedImprovement) {
    this.expectedImprovement = expectedImprovement;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OptimizationResult optimizationResult = (OptimizationResult) o;
    return Objects.equals(this.chainId, optimizationResult.chainId) &&
        Objects.equals(this.goal, optimizationResult.goal) &&
        Objects.equals(this.recommendations, optimizationResult.recommendations) &&
        Objects.equals(this.optimizedSchedule, optimizationResult.optimizedSchedule) &&
        Objects.equals(this.expectedImprovement, optimizationResult.expectedImprovement);
  }

  @Override
  public int hashCode() {
    return Objects.hash(chainId, goal, recommendations, optimizedSchedule, expectedImprovement);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OptimizationResult {\n");
    sb.append("    chainId: ").append(toIndentedString(chainId)).append("\n");
    sb.append("    goal: ").append(toIndentedString(goal)).append("\n");
    sb.append("    recommendations: ").append(toIndentedString(recommendations)).append("\n");
    sb.append("    optimizedSchedule: ").append(toIndentedString(optimizedSchedule)).append("\n");
    sb.append("    expectedImprovement: ").append(toIndentedString(expectedImprovement)).append("\n");
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

