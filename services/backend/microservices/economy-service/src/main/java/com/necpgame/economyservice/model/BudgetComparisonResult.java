package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.BudgetComparisonResultPercentileRange;
import com.necpgame.economyservice.model.BudgetWarning;
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
 * BudgetComparisonResult
 */


public class BudgetComparisonResult {

  private Float proposedBudget;

  private Float recommendedBudget;

  private Float medianBudget;

  private Float deviationPercent;

  private @Nullable BudgetComparisonResultPercentileRange percentileRange;

  @Valid
  private List<@Valid BudgetWarning> warnings = new ArrayList<>();

  public BudgetComparisonResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BudgetComparisonResult(Float proposedBudget, Float recommendedBudget, Float medianBudget, Float deviationPercent, List<@Valid BudgetWarning> warnings) {
    this.proposedBudget = proposedBudget;
    this.recommendedBudget = recommendedBudget;
    this.medianBudget = medianBudget;
    this.deviationPercent = deviationPercent;
    this.warnings = warnings;
  }

  public BudgetComparisonResult proposedBudget(Float proposedBudget) {
    this.proposedBudget = proposedBudget;
    return this;
  }

  /**
   * Get proposedBudget
   * @return proposedBudget
   */
  @NotNull 
  @Schema(name = "proposedBudget", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("proposedBudget")
  public Float getProposedBudget() {
    return proposedBudget;
  }

  public void setProposedBudget(Float proposedBudget) {
    this.proposedBudget = proposedBudget;
  }

  public BudgetComparisonResult recommendedBudget(Float recommendedBudget) {
    this.recommendedBudget = recommendedBudget;
    return this;
  }

  /**
   * Get recommendedBudget
   * @return recommendedBudget
   */
  @NotNull 
  @Schema(name = "recommendedBudget", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("recommendedBudget")
  public Float getRecommendedBudget() {
    return recommendedBudget;
  }

  public void setRecommendedBudget(Float recommendedBudget) {
    this.recommendedBudget = recommendedBudget;
  }

  public BudgetComparisonResult medianBudget(Float medianBudget) {
    this.medianBudget = medianBudget;
    return this;
  }

  /**
   * Get medianBudget
   * @return medianBudget
   */
  @NotNull 
  @Schema(name = "medianBudget", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("medianBudget")
  public Float getMedianBudget() {
    return medianBudget;
  }

  public void setMedianBudget(Float medianBudget) {
    this.medianBudget = medianBudget;
  }

  public BudgetComparisonResult deviationPercent(Float deviationPercent) {
    this.deviationPercent = deviationPercent;
    return this;
  }

  /**
   * Get deviationPercent
   * @return deviationPercent
   */
  @NotNull 
  @Schema(name = "deviationPercent", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("deviationPercent")
  public Float getDeviationPercent() {
    return deviationPercent;
  }

  public void setDeviationPercent(Float deviationPercent) {
    this.deviationPercent = deviationPercent;
  }

  public BudgetComparisonResult percentileRange(@Nullable BudgetComparisonResultPercentileRange percentileRange) {
    this.percentileRange = percentileRange;
    return this;
  }

  /**
   * Get percentileRange
   * @return percentileRange
   */
  @Valid 
  @Schema(name = "percentileRange", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("percentileRange")
  public @Nullable BudgetComparisonResultPercentileRange getPercentileRange() {
    return percentileRange;
  }

  public void setPercentileRange(@Nullable BudgetComparisonResultPercentileRange percentileRange) {
    this.percentileRange = percentileRange;
  }

  public BudgetComparisonResult warnings(List<@Valid BudgetWarning> warnings) {
    this.warnings = warnings;
    return this;
  }

  public BudgetComparisonResult addWarningsItem(BudgetWarning warningsItem) {
    if (this.warnings == null) {
      this.warnings = new ArrayList<>();
    }
    this.warnings.add(warningsItem);
    return this;
  }

  /**
   * Get warnings
   * @return warnings
   */
  @NotNull @Valid 
  @Schema(name = "warnings", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("warnings")
  public List<@Valid BudgetWarning> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<@Valid BudgetWarning> warnings) {
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
    BudgetComparisonResult budgetComparisonResult = (BudgetComparisonResult) o;
    return Objects.equals(this.proposedBudget, budgetComparisonResult.proposedBudget) &&
        Objects.equals(this.recommendedBudget, budgetComparisonResult.recommendedBudget) &&
        Objects.equals(this.medianBudget, budgetComparisonResult.medianBudget) &&
        Objects.equals(this.deviationPercent, budgetComparisonResult.deviationPercent) &&
        Objects.equals(this.percentileRange, budgetComparisonResult.percentileRange) &&
        Objects.equals(this.warnings, budgetComparisonResult.warnings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(proposedBudget, recommendedBudget, medianBudget, deviationPercent, percentileRange, warnings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BudgetComparisonResult {\n");
    sb.append("    proposedBudget: ").append(toIndentedString(proposedBudget)).append("\n");
    sb.append("    recommendedBudget: ").append(toIndentedString(recommendedBudget)).append("\n");
    sb.append("    medianBudget: ").append(toIndentedString(medianBudget)).append("\n");
    sb.append("    deviationPercent: ").append(toIndentedString(deviationPercent)).append("\n");
    sb.append("    percentileRange: ").append(toIndentedString(percentileRange)).append("\n");
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

