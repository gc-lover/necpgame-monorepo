package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CalculateTradeProfit200Response
 */

@JsonTypeName("calculateTradeProfit_200_response")

public class CalculateTradeProfit200Response {

  private @Nullable BigDecimal baseProfit;

  private @Nullable BigDecimal skillBonus;

  private @Nullable BigDecimal reputationBonus;

  private @Nullable BigDecimal riskFactor;

  private @Nullable BigDecimal estimatedProfit;

  public CalculateTradeProfit200Response baseProfit(@Nullable BigDecimal baseProfit) {
    this.baseProfit = baseProfit;
    return this;
  }

  /**
   * Get baseProfit
   * @return baseProfit
   */
  @Valid 
  @Schema(name = "base_profit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base_profit")
  public @Nullable BigDecimal getBaseProfit() {
    return baseProfit;
  }

  public void setBaseProfit(@Nullable BigDecimal baseProfit) {
    this.baseProfit = baseProfit;
  }

  public CalculateTradeProfit200Response skillBonus(@Nullable BigDecimal skillBonus) {
    this.skillBonus = skillBonus;
    return this;
  }

  /**
   * Get skillBonus
   * @return skillBonus
   */
  @Valid 
  @Schema(name = "skill_bonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_bonus")
  public @Nullable BigDecimal getSkillBonus() {
    return skillBonus;
  }

  public void setSkillBonus(@Nullable BigDecimal skillBonus) {
    this.skillBonus = skillBonus;
  }

  public CalculateTradeProfit200Response reputationBonus(@Nullable BigDecimal reputationBonus) {
    this.reputationBonus = reputationBonus;
    return this;
  }

  /**
   * Get reputationBonus
   * @return reputationBonus
   */
  @Valid 
  @Schema(name = "reputation_bonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_bonus")
  public @Nullable BigDecimal getReputationBonus() {
    return reputationBonus;
  }

  public void setReputationBonus(@Nullable BigDecimal reputationBonus) {
    this.reputationBonus = reputationBonus;
  }

  public CalculateTradeProfit200Response riskFactor(@Nullable BigDecimal riskFactor) {
    this.riskFactor = riskFactor;
    return this;
  }

  /**
   * Get riskFactor
   * @return riskFactor
   */
  @Valid 
  @Schema(name = "risk_factor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("risk_factor")
  public @Nullable BigDecimal getRiskFactor() {
    return riskFactor;
  }

  public void setRiskFactor(@Nullable BigDecimal riskFactor) {
    this.riskFactor = riskFactor;
  }

  public CalculateTradeProfit200Response estimatedProfit(@Nullable BigDecimal estimatedProfit) {
    this.estimatedProfit = estimatedProfit;
    return this;
  }

  /**
   * Get estimatedProfit
   * @return estimatedProfit
   */
  @Valid 
  @Schema(name = "estimated_profit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_profit")
  public @Nullable BigDecimal getEstimatedProfit() {
    return estimatedProfit;
  }

  public void setEstimatedProfit(@Nullable BigDecimal estimatedProfit) {
    this.estimatedProfit = estimatedProfit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculateTradeProfit200Response calculateTradeProfit200Response = (CalculateTradeProfit200Response) o;
    return Objects.equals(this.baseProfit, calculateTradeProfit200Response.baseProfit) &&
        Objects.equals(this.skillBonus, calculateTradeProfit200Response.skillBonus) &&
        Objects.equals(this.reputationBonus, calculateTradeProfit200Response.reputationBonus) &&
        Objects.equals(this.riskFactor, calculateTradeProfit200Response.riskFactor) &&
        Objects.equals(this.estimatedProfit, calculateTradeProfit200Response.estimatedProfit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseProfit, skillBonus, reputationBonus, riskFactor, estimatedProfit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculateTradeProfit200Response {\n");
    sb.append("    baseProfit: ").append(toIndentedString(baseProfit)).append("\n");
    sb.append("    skillBonus: ").append(toIndentedString(skillBonus)).append("\n");
    sb.append("    reputationBonus: ").append(toIndentedString(reputationBonus)).append("\n");
    sb.append("    riskFactor: ").append(toIndentedString(riskFactor)).append("\n");
    sb.append("    estimatedProfit: ").append(toIndentedString(estimatedProfit)).append("\n");
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

