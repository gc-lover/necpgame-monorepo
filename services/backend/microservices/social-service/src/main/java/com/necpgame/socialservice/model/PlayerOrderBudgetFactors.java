package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.PlayerOrderComplexityFactor;
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
 * PlayerOrderBudgetFactors
 */


public class PlayerOrderBudgetFactors {

  @Valid
  private List<@Valid PlayerOrderComplexityFactor> complexityFactors = new ArrayList<>();

  private BigDecimal riskModifier;

  private BigDecimal marketIndex;

  private BigDecimal timeModifier;

  @Valid
  private List<BigDecimal> bonuses = new ArrayList<>();

  @Valid
  private List<BigDecimal> penalties = new ArrayList<>();

  public PlayerOrderBudgetFactors() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderBudgetFactors(List<@Valid PlayerOrderComplexityFactor> complexityFactors, BigDecimal riskModifier, BigDecimal marketIndex, BigDecimal timeModifier) {
    this.complexityFactors = complexityFactors;
    this.riskModifier = riskModifier;
    this.marketIndex = marketIndex;
    this.timeModifier = timeModifier;
  }

  public PlayerOrderBudgetFactors complexityFactors(List<@Valid PlayerOrderComplexityFactor> complexityFactors) {
    this.complexityFactors = complexityFactors;
    return this;
  }

  public PlayerOrderBudgetFactors addComplexityFactorsItem(PlayerOrderComplexityFactor complexityFactorsItem) {
    if (this.complexityFactors == null) {
      this.complexityFactors = new ArrayList<>();
    }
    this.complexityFactors.add(complexityFactorsItem);
    return this;
  }

  /**
   * Набор факторов, участвующих в расчёте сложности.
   * @return complexityFactors
   */
  @NotNull @Valid 
  @Schema(name = "complexityFactors", description = "Набор факторов, участвующих в расчёте сложности.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("complexityFactors")
  public List<@Valid PlayerOrderComplexityFactor> getComplexityFactors() {
    return complexityFactors;
  }

  public void setComplexityFactors(List<@Valid PlayerOrderComplexityFactor> complexityFactors) {
    this.complexityFactors = complexityFactors;
  }

  public PlayerOrderBudgetFactors riskModifier(BigDecimal riskModifier) {
    this.riskModifier = riskModifier;
    return this;
  }

  /**
   * Коэффициент риска, рассчитанный из `PlayerOrderRiskProfile`.
   * minimum: 0.5
   * maximum: 2.0
   * @return riskModifier
   */
  @NotNull @Valid @DecimalMin(value = "0.5") @DecimalMax(value = "2.0") 
  @Schema(name = "riskModifier", description = "Коэффициент риска, рассчитанный из `PlayerOrderRiskProfile`.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("riskModifier")
  public BigDecimal getRiskModifier() {
    return riskModifier;
  }

  public void setRiskModifier(BigDecimal riskModifier) {
    this.riskModifier = riskModifier;
  }

  public PlayerOrderBudgetFactors marketIndex(BigDecimal marketIndex) {
    this.marketIndex = marketIndex;
    return this;
  }

  /**
   * Рыночный индекс, предоставляемый economy-service.
   * minimum: 0
   * @return marketIndex
   */
  @NotNull @Valid @DecimalMin(value = "0") 
  @Schema(name = "marketIndex", description = "Рыночный индекс, предоставляемый economy-service.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("marketIndex")
  public BigDecimal getMarketIndex() {
    return marketIndex;
  }

  public void setMarketIndex(BigDecimal marketIndex) {
    this.marketIndex = marketIndex;
  }

  public PlayerOrderBudgetFactors timeModifier(BigDecimal timeModifier) {
    this.timeModifier = timeModifier;
    return this;
  }

  /**
   * Коэффициент срочности и количества чекпоинтов.
   * minimum: 0.5
   * maximum: 2.0
   * @return timeModifier
   */
  @NotNull @Valid @DecimalMin(value = "0.5") @DecimalMax(value = "2.0") 
  @Schema(name = "timeModifier", description = "Коэффициент срочности и количества чекпоинтов.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timeModifier")
  public BigDecimal getTimeModifier() {
    return timeModifier;
  }

  public void setTimeModifier(BigDecimal timeModifier) {
    this.timeModifier = timeModifier;
  }

  public PlayerOrderBudgetFactors bonuses(List<BigDecimal> bonuses) {
    this.bonuses = bonuses;
    return this;
  }

  public PlayerOrderBudgetFactors addBonusesItem(BigDecimal bonusesItem) {
    if (this.bonuses == null) {
      this.bonuses = new ArrayList<>();
    }
    this.bonuses.add(bonusesItem);
    return this;
  }

  /**
   * Дополнительные бонусы, предлагаемые заказчиком.
   * @return bonuses
   */
  @Valid 
  @Schema(name = "bonuses", description = "Дополнительные бонусы, предлагаемые заказчиком.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonuses")
  public List<BigDecimal> getBonuses() {
    return bonuses;
  }

  public void setBonuses(List<BigDecimal> bonuses) {
    this.bonuses = bonuses;
  }

  public PlayerOrderBudgetFactors penalties(List<BigDecimal> penalties) {
    this.penalties = penalties;
    return this;
  }

  public PlayerOrderBudgetFactors addPenaltiesItem(BigDecimal penaltiesItem) {
    if (this.penalties == null) {
      this.penalties = new ArrayList<>();
    }
    this.penalties.add(penaltiesItem);
    return this;
  }

  /**
   * Штрафы за невыполнение чекпоинтов.
   * @return penalties
   */
  @Valid 
  @Schema(name = "penalties", description = "Штрафы за невыполнение чекпоинтов.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("penalties")
  public List<BigDecimal> getPenalties() {
    return penalties;
  }

  public void setPenalties(List<BigDecimal> penalties) {
    this.penalties = penalties;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderBudgetFactors playerOrderBudgetFactors = (PlayerOrderBudgetFactors) o;
    return Objects.equals(this.complexityFactors, playerOrderBudgetFactors.complexityFactors) &&
        Objects.equals(this.riskModifier, playerOrderBudgetFactors.riskModifier) &&
        Objects.equals(this.marketIndex, playerOrderBudgetFactors.marketIndex) &&
        Objects.equals(this.timeModifier, playerOrderBudgetFactors.timeModifier) &&
        Objects.equals(this.bonuses, playerOrderBudgetFactors.bonuses) &&
        Objects.equals(this.penalties, playerOrderBudgetFactors.penalties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(complexityFactors, riskModifier, marketIndex, timeModifier, bonuses, penalties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderBudgetFactors {\n");
    sb.append("    complexityFactors: ").append(toIndentedString(complexityFactors)).append("\n");
    sb.append("    riskModifier: ").append(toIndentedString(riskModifier)).append("\n");
    sb.append("    marketIndex: ").append(toIndentedString(marketIndex)).append("\n");
    sb.append("    timeModifier: ").append(toIndentedString(timeModifier)).append("\n");
    sb.append("    bonuses: ").append(toIndentedString(bonuses)).append("\n");
    sb.append("    penalties: ").append(toIndentedString(penalties)).append("\n");
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

