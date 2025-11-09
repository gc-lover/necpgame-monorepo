package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.QualityFactor;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * MatchQualityReport
 */


public class MatchQualityReport {

  private Float score;

  private Float ratingBalance;

  private Float roleFulfillment;

  private Float waitTimePenalty;

  private Float latencyPenalty;

  @Valid
  private List<@Valid QualityFactor> factors = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime computedAt;

  public MatchQualityReport() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MatchQualityReport(Float score, Float ratingBalance, Float roleFulfillment, Float waitTimePenalty, Float latencyPenalty) {
    this.score = score;
    this.ratingBalance = ratingBalance;
    this.roleFulfillment = roleFulfillment;
    this.waitTimePenalty = waitTimePenalty;
    this.latencyPenalty = latencyPenalty;
  }

  public MatchQualityReport score(Float score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * minimum: 0
   * maximum: 100
   * @return score
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "score", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("score")
  public Float getScore() {
    return score;
  }

  public void setScore(Float score) {
    this.score = score;
  }

  public MatchQualityReport ratingBalance(Float ratingBalance) {
    this.ratingBalance = ratingBalance;
    return this;
  }

  /**
   * Get ratingBalance
   * @return ratingBalance
   */
  @NotNull 
  @Schema(name = "ratingBalance", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ratingBalance")
  public Float getRatingBalance() {
    return ratingBalance;
  }

  public void setRatingBalance(Float ratingBalance) {
    this.ratingBalance = ratingBalance;
  }

  public MatchQualityReport roleFulfillment(Float roleFulfillment) {
    this.roleFulfillment = roleFulfillment;
    return this;
  }

  /**
   * Get roleFulfillment
   * @return roleFulfillment
   */
  @NotNull 
  @Schema(name = "roleFulfillment", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("roleFulfillment")
  public Float getRoleFulfillment() {
    return roleFulfillment;
  }

  public void setRoleFulfillment(Float roleFulfillment) {
    this.roleFulfillment = roleFulfillment;
  }

  public MatchQualityReport waitTimePenalty(Float waitTimePenalty) {
    this.waitTimePenalty = waitTimePenalty;
    return this;
  }

  /**
   * Get waitTimePenalty
   * @return waitTimePenalty
   */
  @NotNull 
  @Schema(name = "waitTimePenalty", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("waitTimePenalty")
  public Float getWaitTimePenalty() {
    return waitTimePenalty;
  }

  public void setWaitTimePenalty(Float waitTimePenalty) {
    this.waitTimePenalty = waitTimePenalty;
  }

  public MatchQualityReport latencyPenalty(Float latencyPenalty) {
    this.latencyPenalty = latencyPenalty;
    return this;
  }

  /**
   * Get latencyPenalty
   * @return latencyPenalty
   */
  @NotNull 
  @Schema(name = "latencyPenalty", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("latencyPenalty")
  public Float getLatencyPenalty() {
    return latencyPenalty;
  }

  public void setLatencyPenalty(Float latencyPenalty) {
    this.latencyPenalty = latencyPenalty;
  }

  public MatchQualityReport factors(List<@Valid QualityFactor> factors) {
    this.factors = factors;
    return this;
  }

  public MatchQualityReport addFactorsItem(QualityFactor factorsItem) {
    if (this.factors == null) {
      this.factors = new ArrayList<>();
    }
    this.factors.add(factorsItem);
    return this;
  }

  /**
   * Get factors
   * @return factors
   */
  @Valid 
  @Schema(name = "factors", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factors")
  public List<@Valid QualityFactor> getFactors() {
    return factors;
  }

  public void setFactors(List<@Valid QualityFactor> factors) {
    this.factors = factors;
  }

  public MatchQualityReport computedAt(@Nullable OffsetDateTime computedAt) {
    this.computedAt = computedAt;
    return this;
  }

  /**
   * Get computedAt
   * @return computedAt
   */
  @Valid 
  @Schema(name = "computedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("computedAt")
  public @Nullable OffsetDateTime getComputedAt() {
    return computedAt;
  }

  public void setComputedAt(@Nullable OffsetDateTime computedAt) {
    this.computedAt = computedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchQualityReport matchQualityReport = (MatchQualityReport) o;
    return Objects.equals(this.score, matchQualityReport.score) &&
        Objects.equals(this.ratingBalance, matchQualityReport.ratingBalance) &&
        Objects.equals(this.roleFulfillment, matchQualityReport.roleFulfillment) &&
        Objects.equals(this.waitTimePenalty, matchQualityReport.waitTimePenalty) &&
        Objects.equals(this.latencyPenalty, matchQualityReport.latencyPenalty) &&
        Objects.equals(this.factors, matchQualityReport.factors) &&
        Objects.equals(this.computedAt, matchQualityReport.computedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(score, ratingBalance, roleFulfillment, waitTimePenalty, latencyPenalty, factors, computedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchQualityReport {\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    ratingBalance: ").append(toIndentedString(ratingBalance)).append("\n");
    sb.append("    roleFulfillment: ").append(toIndentedString(roleFulfillment)).append("\n");
    sb.append("    waitTimePenalty: ").append(toIndentedString(waitTimePenalty)).append("\n");
    sb.append("    latencyPenalty: ").append(toIndentedString(latencyPenalty)).append("\n");
    sb.append("    factors: ").append(toIndentedString(factors)).append("\n");
    sb.append("    computedAt: ").append(toIndentedString(computedAt)).append("\n");
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

