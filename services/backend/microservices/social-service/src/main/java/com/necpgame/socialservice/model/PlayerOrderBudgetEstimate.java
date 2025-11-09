package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
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
 * PlayerOrderBudgetEstimate
 */


public class PlayerOrderBudgetEstimate {

  private BigDecimal complexityScore;

  private BigDecimal riskModifier;

  private BigDecimal marketIndex;

  private BigDecimal timeModifier;

  private BigDecimal baseReward;

  private BigDecimal escrow;

  private BigDecimal commission;

  private @Nullable BigDecimal insurancePremium;

  private BigDecimal medianDeviation;

  @Valid
  private List<String> recommendations = new ArrayList<>();

  @Valid
  private List<String> warnings = new ArrayList<>();

  public PlayerOrderBudgetEstimate() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderBudgetEstimate(BigDecimal complexityScore, BigDecimal riskModifier, BigDecimal marketIndex, BigDecimal timeModifier, BigDecimal baseReward, BigDecimal escrow, BigDecimal commission, BigDecimal medianDeviation) {
    this.complexityScore = complexityScore;
    this.riskModifier = riskModifier;
    this.marketIndex = marketIndex;
    this.timeModifier = timeModifier;
    this.baseReward = baseReward;
    this.escrow = escrow;
    this.commission = commission;
    this.medianDeviation = medianDeviation;
  }

  public PlayerOrderBudgetEstimate complexityScore(BigDecimal complexityScore) {
    this.complexityScore = complexityScore;
    return this;
  }

  /**
   * Итоговая сложность заказа.
   * @return complexityScore
   */
  @NotNull @Valid 
  @Schema(name = "complexityScore", description = "Итоговая сложность заказа.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("complexityScore")
  public BigDecimal getComplexityScore() {
    return complexityScore;
  }

  public void setComplexityScore(BigDecimal complexityScore) {
    this.complexityScore = complexityScore;
  }

  public PlayerOrderBudgetEstimate riskModifier(BigDecimal riskModifier) {
    this.riskModifier = riskModifier;
    return this;
  }

  /**
   * Применённый коэффициент риска.
   * @return riskModifier
   */
  @NotNull @Valid 
  @Schema(name = "riskModifier", description = "Применённый коэффициент риска.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("riskModifier")
  public BigDecimal getRiskModifier() {
    return riskModifier;
  }

  public void setRiskModifier(BigDecimal riskModifier) {
    this.riskModifier = riskModifier;
  }

  public PlayerOrderBudgetEstimate marketIndex(BigDecimal marketIndex) {
    this.marketIndex = marketIndex;
    return this;
  }

  /**
   * Рыночный индекс на момент расчёта.
   * @return marketIndex
   */
  @NotNull @Valid 
  @Schema(name = "marketIndex", description = "Рыночный индекс на момент расчёта.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("marketIndex")
  public BigDecimal getMarketIndex() {
    return marketIndex;
  }

  public void setMarketIndex(BigDecimal marketIndex) {
    this.marketIndex = marketIndex;
  }

  public PlayerOrderBudgetEstimate timeModifier(BigDecimal timeModifier) {
    this.timeModifier = timeModifier;
    return this;
  }

  /**
   * Учёт дедлайнов и чекпоинтов.
   * @return timeModifier
   */
  @NotNull @Valid 
  @Schema(name = "timeModifier", description = "Учёт дедлайнов и чекпоинтов.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timeModifier")
  public BigDecimal getTimeModifier() {
    return timeModifier;
  }

  public void setTimeModifier(BigDecimal timeModifier) {
    this.timeModifier = timeModifier;
  }

  public PlayerOrderBudgetEstimate baseReward(BigDecimal baseReward) {
    this.baseReward = baseReward;
    return this;
  }

  /**
   * Расчётная базовая сумма награды.
   * @return baseReward
   */
  @NotNull @Valid 
  @Schema(name = "baseReward", description = "Расчётная базовая сумма награды.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("baseReward")
  public BigDecimal getBaseReward() {
    return baseReward;
  }

  public void setBaseReward(BigDecimal baseReward) {
    this.baseReward = baseReward;
  }

  public PlayerOrderBudgetEstimate escrow(BigDecimal escrow) {
    this.escrow = escrow;
    return this;
  }

  /**
   * Сумма, блокируемая в escrow.
   * @return escrow
   */
  @NotNull @Valid 
  @Schema(name = "escrow", description = "Сумма, блокируемая в escrow.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("escrow")
  public BigDecimal getEscrow() {
    return escrow;
  }

  public void setEscrow(BigDecimal escrow) {
    this.escrow = escrow;
  }

  public PlayerOrderBudgetEstimate commission(BigDecimal commission) {
    this.commission = commission;
    return this;
  }

  /**
   * Комиссия платформы.
   * @return commission
   */
  @NotNull @Valid 
  @Schema(name = "commission", description = "Комиссия платформы.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("commission")
  public BigDecimal getCommission() {
    return commission;
  }

  public void setCommission(BigDecimal commission) {
    this.commission = commission;
  }

  public PlayerOrderBudgetEstimate insurancePremium(@Nullable BigDecimal insurancePremium) {
    this.insurancePremium = insurancePremium;
    return this;
  }

  /**
   * Стоимость страховки (при выборе расширенных гарантий).
   * @return insurancePremium
   */
  @Valid 
  @Schema(name = "insurancePremium", description = "Стоимость страховки (при выборе расширенных гарантий).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("insurancePremium")
  public @Nullable BigDecimal getInsurancePremium() {
    return insurancePremium;
  }

  public void setInsurancePremium(@Nullable BigDecimal insurancePremium) {
    this.insurancePremium = insurancePremium;
  }

  public PlayerOrderBudgetEstimate medianDeviation(BigDecimal medianDeviation) {
    this.medianDeviation = medianDeviation;
    return this;
  }

  /**
   * Отклонение от медианного бюджета по типу заказа (в процентах).
   * @return medianDeviation
   */
  @NotNull @Valid 
  @Schema(name = "medianDeviation", description = "Отклонение от медианного бюджета по типу заказа (в процентах).", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("medianDeviation")
  public BigDecimal getMedianDeviation() {
    return medianDeviation;
  }

  public void setMedianDeviation(BigDecimal medianDeviation) {
    this.medianDeviation = medianDeviation;
  }

  public PlayerOrderBudgetEstimate recommendations(List<String> recommendations) {
    this.recommendations = recommendations;
    return this;
  }

  public PlayerOrderBudgetEstimate addRecommendationsItem(String recommendationsItem) {
    if (this.recommendations == null) {
      this.recommendations = new ArrayList<>();
    }
    this.recommendations.add(recommendationsItem);
    return this;
  }

  /**
   * Рекомендации по бюджету (повышение/снижение, страховка).
   * @return recommendations
   */
  
  @Schema(name = "recommendations", description = "Рекомендации по бюджету (повышение/снижение, страховка).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendations")
  public List<String> getRecommendations() {
    return recommendations;
  }

  public void setRecommendations(List<String> recommendations) {
    this.recommendations = recommendations;
  }

  public PlayerOrderBudgetEstimate warnings(List<String> warnings) {
    this.warnings = warnings;
    return this;
  }

  public PlayerOrderBudgetEstimate addWarningsItem(String warningsItem) {
    if (this.warnings == null) {
      this.warnings = new ArrayList<>();
    }
    this.warnings.add(warningsItem);
    return this;
  }

  /**
   * Предупреждения (например, превышение медианы).
   * @return warnings
   */
  
  @Schema(name = "warnings", description = "Предупреждения (например, превышение медианы).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    PlayerOrderBudgetEstimate playerOrderBudgetEstimate = (PlayerOrderBudgetEstimate) o;
    return Objects.equals(this.complexityScore, playerOrderBudgetEstimate.complexityScore) &&
        Objects.equals(this.riskModifier, playerOrderBudgetEstimate.riskModifier) &&
        Objects.equals(this.marketIndex, playerOrderBudgetEstimate.marketIndex) &&
        Objects.equals(this.timeModifier, playerOrderBudgetEstimate.timeModifier) &&
        Objects.equals(this.baseReward, playerOrderBudgetEstimate.baseReward) &&
        Objects.equals(this.escrow, playerOrderBudgetEstimate.escrow) &&
        Objects.equals(this.commission, playerOrderBudgetEstimate.commission) &&
        Objects.equals(this.insurancePremium, playerOrderBudgetEstimate.insurancePremium) &&
        Objects.equals(this.medianDeviation, playerOrderBudgetEstimate.medianDeviation) &&
        Objects.equals(this.recommendations, playerOrderBudgetEstimate.recommendations) &&
        Objects.equals(this.warnings, playerOrderBudgetEstimate.warnings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(complexityScore, riskModifier, marketIndex, timeModifier, baseReward, escrow, commission, insurancePremium, medianDeviation, recommendations, warnings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderBudgetEstimate {\n");
    sb.append("    complexityScore: ").append(toIndentedString(complexityScore)).append("\n");
    sb.append("    riskModifier: ").append(toIndentedString(riskModifier)).append("\n");
    sb.append("    marketIndex: ").append(toIndentedString(marketIndex)).append("\n");
    sb.append("    timeModifier: ").append(toIndentedString(timeModifier)).append("\n");
    sb.append("    baseReward: ").append(toIndentedString(baseReward)).append("\n");
    sb.append("    escrow: ").append(toIndentedString(escrow)).append("\n");
    sb.append("    commission: ").append(toIndentedString(commission)).append("\n");
    sb.append("    insurancePremium: ").append(toIndentedString(insurancePremium)).append("\n");
    sb.append("    medianDeviation: ").append(toIndentedString(medianDeviation)).append("\n");
    sb.append("    recommendations: ").append(toIndentedString(recommendations)).append("\n");
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

