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
 * StockPortfolioHoldingsInner
 */

@JsonTypeName("StockPortfolio_holdings_inner")

public class StockPortfolioHoldingsInner {

  private @Nullable String ticker;

  private @Nullable Integer shares;

  private @Nullable BigDecimal avgBuyPrice;

  private @Nullable BigDecimal currentPrice;

  private @Nullable BigDecimal profitLoss;

  public StockPortfolioHoldingsInner ticker(@Nullable String ticker) {
    this.ticker = ticker;
    return this;
  }

  /**
   * Get ticker
   * @return ticker
   */
  
  @Schema(name = "ticker", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ticker")
  public @Nullable String getTicker() {
    return ticker;
  }

  public void setTicker(@Nullable String ticker) {
    this.ticker = ticker;
  }

  public StockPortfolioHoldingsInner shares(@Nullable Integer shares) {
    this.shares = shares;
    return this;
  }

  /**
   * Get shares
   * @return shares
   */
  
  @Schema(name = "shares", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("shares")
  public @Nullable Integer getShares() {
    return shares;
  }

  public void setShares(@Nullable Integer shares) {
    this.shares = shares;
  }

  public StockPortfolioHoldingsInner avgBuyPrice(@Nullable BigDecimal avgBuyPrice) {
    this.avgBuyPrice = avgBuyPrice;
    return this;
  }

  /**
   * Get avgBuyPrice
   * @return avgBuyPrice
   */
  @Valid 
  @Schema(name = "avg_buy_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("avg_buy_price")
  public @Nullable BigDecimal getAvgBuyPrice() {
    return avgBuyPrice;
  }

  public void setAvgBuyPrice(@Nullable BigDecimal avgBuyPrice) {
    this.avgBuyPrice = avgBuyPrice;
  }

  public StockPortfolioHoldingsInner currentPrice(@Nullable BigDecimal currentPrice) {
    this.currentPrice = currentPrice;
    return this;
  }

  /**
   * Get currentPrice
   * @return currentPrice
   */
  @Valid 
  @Schema(name = "current_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_price")
  public @Nullable BigDecimal getCurrentPrice() {
    return currentPrice;
  }

  public void setCurrentPrice(@Nullable BigDecimal currentPrice) {
    this.currentPrice = currentPrice;
  }

  public StockPortfolioHoldingsInner profitLoss(@Nullable BigDecimal profitLoss) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StockPortfolioHoldingsInner stockPortfolioHoldingsInner = (StockPortfolioHoldingsInner) o;
    return Objects.equals(this.ticker, stockPortfolioHoldingsInner.ticker) &&
        Objects.equals(this.shares, stockPortfolioHoldingsInner.shares) &&
        Objects.equals(this.avgBuyPrice, stockPortfolioHoldingsInner.avgBuyPrice) &&
        Objects.equals(this.currentPrice, stockPortfolioHoldingsInner.currentPrice) &&
        Objects.equals(this.profitLoss, stockPortfolioHoldingsInner.profitLoss);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticker, shares, avgBuyPrice, currentPrice, profitLoss);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StockPortfolioHoldingsInner {\n");
    sb.append("    ticker: ").append(toIndentedString(ticker)).append("\n");
    sb.append("    shares: ").append(toIndentedString(shares)).append("\n");
    sb.append("    avgBuyPrice: ").append(toIndentedString(avgBuyPrice)).append("\n");
    sb.append("    currentPrice: ").append(toIndentedString(currentPrice)).append("\n");
    sb.append("    profitLoss: ").append(toIndentedString(profitLoss)).append("\n");
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

