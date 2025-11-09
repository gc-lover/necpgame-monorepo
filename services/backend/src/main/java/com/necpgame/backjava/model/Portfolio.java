package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.Investment;
import com.necpgame.backjava.model.PortfolioInvestmentsByTypeValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * Portfolio
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-08T01:01:47.984013400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class Portfolio {

  private @Nullable UUID characterId;

  private @Nullable Integer totalInvested;

  private @Nullable Integer totalCurrentValue;

  private @Nullable Integer totalProfitLoss;

  private @Nullable Float overallRoi;

  @Valid
  private List<@Valid Investment> investments = new ArrayList<>();

  @Valid
  private Map<String, PortfolioInvestmentsByTypeValue> investmentsByType = new HashMap<>();

  public Portfolio characterId(@Nullable UUID characterId) {
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

  public Portfolio totalInvested(@Nullable Integer totalInvested) {
    this.totalInvested = totalInvested;
    return this;
  }

  /**
   * Get totalInvested
   * @return totalInvested
   */
  
  @Schema(name = "total_invested", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_invested")
  public @Nullable Integer getTotalInvested() {
    return totalInvested;
  }

  public void setTotalInvested(@Nullable Integer totalInvested) {
    this.totalInvested = totalInvested;
  }

  public Portfolio totalCurrentValue(@Nullable Integer totalCurrentValue) {
    this.totalCurrentValue = totalCurrentValue;
    return this;
  }

  /**
   * Get totalCurrentValue
   * @return totalCurrentValue
   */
  
  @Schema(name = "total_current_value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_current_value")
  public @Nullable Integer getTotalCurrentValue() {
    return totalCurrentValue;
  }

  public void setTotalCurrentValue(@Nullable Integer totalCurrentValue) {
    this.totalCurrentValue = totalCurrentValue;
  }

  public Portfolio totalProfitLoss(@Nullable Integer totalProfitLoss) {
    this.totalProfitLoss = totalProfitLoss;
    return this;
  }

  /**
   * Get totalProfitLoss
   * @return totalProfitLoss
   */
  
  @Schema(name = "total_profit_loss", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_profit_loss")
  public @Nullable Integer getTotalProfitLoss() {
    return totalProfitLoss;
  }

  public void setTotalProfitLoss(@Nullable Integer totalProfitLoss) {
    this.totalProfitLoss = totalProfitLoss;
  }

  public Portfolio overallRoi(@Nullable Float overallRoi) {
    this.overallRoi = overallRoi;
    return this;
  }

  /**
   * Get overallRoi
   * @return overallRoi
   */
  
  @Schema(name = "overall_roi", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("overall_roi")
  public @Nullable Float getOverallRoi() {
    return overallRoi;
  }

  public void setOverallRoi(@Nullable Float overallRoi) {
    this.overallRoi = overallRoi;
  }

  public Portfolio investments(List<@Valid Investment> investments) {
    this.investments = investments;
    return this;
  }

  public Portfolio addInvestmentsItem(Investment investmentsItem) {
    if (this.investments == null) {
      this.investments = new ArrayList<>();
    }
    this.investments.add(investmentsItem);
    return this;
  }

  /**
   * Get investments
   * @return investments
   */
  @Valid 
  @Schema(name = "investments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("investments")
  public List<@Valid Investment> getInvestments() {
    return investments;
  }

  public void setInvestments(List<@Valid Investment> investments) {
    this.investments = investments;
  }

  public Portfolio investmentsByType(Map<String, PortfolioInvestmentsByTypeValue> investmentsByType) {
    this.investmentsByType = investmentsByType;
    return this;
  }

  public Portfolio putInvestmentsByTypeItem(String key, PortfolioInvestmentsByTypeValue investmentsByTypeItem) {
    if (this.investmentsByType == null) {
      this.investmentsByType = new HashMap<>();
    }
    this.investmentsByType.put(key, investmentsByTypeItem);
    return this;
  }

  /**
   * Get investmentsByType
   * @return investmentsByType
   */
  @Valid 
  @Schema(name = "investments_by_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("investments_by_type")
  public Map<String, PortfolioInvestmentsByTypeValue> getInvestmentsByType() {
    return investmentsByType;
  }

  public void setInvestmentsByType(Map<String, PortfolioInvestmentsByTypeValue> investmentsByType) {
    this.investmentsByType = investmentsByType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Portfolio portfolio = (Portfolio) o;
    return Objects.equals(this.characterId, portfolio.characterId) &&
        Objects.equals(this.totalInvested, portfolio.totalInvested) &&
        Objects.equals(this.totalCurrentValue, portfolio.totalCurrentValue) &&
        Objects.equals(this.totalProfitLoss, portfolio.totalProfitLoss) &&
        Objects.equals(this.overallRoi, portfolio.overallRoi) &&
        Objects.equals(this.investments, portfolio.investments) &&
        Objects.equals(this.investmentsByType, portfolio.investmentsByType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, totalInvested, totalCurrentValue, totalProfitLoss, overallRoi, investments, investmentsByType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Portfolio {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    totalInvested: ").append(toIndentedString(totalInvested)).append("\n");
    sb.append("    totalCurrentValue: ").append(toIndentedString(totalCurrentValue)).append("\n");
    sb.append("    totalProfitLoss: ").append(toIndentedString(totalProfitLoss)).append("\n");
    sb.append("    overallRoi: ").append(toIndentedString(overallRoi)).append("\n");
    sb.append("    investments: ").append(toIndentedString(investments)).append("\n");
    sb.append("    investmentsByType: ").append(toIndentedString(investmentsByType)).append("\n");
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

