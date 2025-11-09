package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.StockPortfolioHoldingsInner;
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
 * StockPortfolio
 */


public class StockPortfolio {

  private @Nullable String characterId;

  private @Nullable BigDecimal totalValue;

  private @Nullable BigDecimal totalInvested;

  private @Nullable BigDecimal profitLoss;

  private @Nullable BigDecimal roiPercent;

  @Valid
  private List<@Valid StockPortfolioHoldingsInner> holdings = new ArrayList<>();

  public StockPortfolio characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public StockPortfolio totalValue(@Nullable BigDecimal totalValue) {
    this.totalValue = totalValue;
    return this;
  }

  /**
   * Get totalValue
   * @return totalValue
   */
  @Valid 
  @Schema(name = "total_value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_value")
  public @Nullable BigDecimal getTotalValue() {
    return totalValue;
  }

  public void setTotalValue(@Nullable BigDecimal totalValue) {
    this.totalValue = totalValue;
  }

  public StockPortfolio totalInvested(@Nullable BigDecimal totalInvested) {
    this.totalInvested = totalInvested;
    return this;
  }

  /**
   * Get totalInvested
   * @return totalInvested
   */
  @Valid 
  @Schema(name = "total_invested", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_invested")
  public @Nullable BigDecimal getTotalInvested() {
    return totalInvested;
  }

  public void setTotalInvested(@Nullable BigDecimal totalInvested) {
    this.totalInvested = totalInvested;
  }

  public StockPortfolio profitLoss(@Nullable BigDecimal profitLoss) {
    this.profitLoss = profitLoss;
    return this;
  }

  /**
   * Get profitLoss
   * @return profitLoss
   */
  @Valid 
  @Schema(name = "profit_loss", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("profit_loss")
  public @Nullable BigDecimal getProfitLoss() {
    return profitLoss;
  }

  public void setProfitLoss(@Nullable BigDecimal profitLoss) {
    this.profitLoss = profitLoss;
  }

  public StockPortfolio roiPercent(@Nullable BigDecimal roiPercent) {
    this.roiPercent = roiPercent;
    return this;
  }

  /**
   * Get roiPercent
   * @return roiPercent
   */
  @Valid 
  @Schema(name = "roi_percent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roi_percent")
  public @Nullable BigDecimal getRoiPercent() {
    return roiPercent;
  }

  public void setRoiPercent(@Nullable BigDecimal roiPercent) {
    this.roiPercent = roiPercent;
  }

  public StockPortfolio holdings(List<@Valid StockPortfolioHoldingsInner> holdings) {
    this.holdings = holdings;
    return this;
  }

  public StockPortfolio addHoldingsItem(StockPortfolioHoldingsInner holdingsItem) {
    if (this.holdings == null) {
      this.holdings = new ArrayList<>();
    }
    this.holdings.add(holdingsItem);
    return this;
  }

  /**
   * Get holdings
   * @return holdings
   */
  @Valid 
  @Schema(name = "holdings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("holdings")
  public List<@Valid StockPortfolioHoldingsInner> getHoldings() {
    return holdings;
  }

  public void setHoldings(List<@Valid StockPortfolioHoldingsInner> holdings) {
    this.holdings = holdings;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StockPortfolio stockPortfolio = (StockPortfolio) o;
    return Objects.equals(this.characterId, stockPortfolio.characterId) &&
        Objects.equals(this.totalValue, stockPortfolio.totalValue) &&
        Objects.equals(this.totalInvested, stockPortfolio.totalInvested) &&
        Objects.equals(this.profitLoss, stockPortfolio.profitLoss) &&
        Objects.equals(this.roiPercent, stockPortfolio.roiPercent) &&
        Objects.equals(this.holdings, stockPortfolio.holdings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, totalValue, totalInvested, profitLoss, roiPercent, holdings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StockPortfolio {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    totalValue: ").append(toIndentedString(totalValue)).append("\n");
    sb.append("    totalInvested: ").append(toIndentedString(totalInvested)).append("\n");
    sb.append("    profitLoss: ").append(toIndentedString(profitLoss)).append("\n");
    sb.append("    roiPercent: ").append(toIndentedString(roiPercent)).append("\n");
    sb.append("    holdings: ").append(toIndentedString(holdings)).append("\n");
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

