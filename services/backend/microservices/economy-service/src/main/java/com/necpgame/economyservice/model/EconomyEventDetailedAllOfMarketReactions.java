package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EconomyEventDetailedAllOfMarketReactions
 */

@JsonTypeName("EconomyEventDetailed_allOf_market_reactions")

public class EconomyEventDetailedAllOfMarketReactions {

  @Valid
  private Map<String, BigDecimal> stockChanges = new HashMap<>();

  @Valid
  private Map<String, BigDecimal> commodityChanges = new HashMap<>();

  @Valid
  private Map<String, BigDecimal> currencyImpact = new HashMap<>();

  public EconomyEventDetailedAllOfMarketReactions stockChanges(Map<String, BigDecimal> stockChanges) {
    this.stockChanges = stockChanges;
    return this;
  }

  public EconomyEventDetailedAllOfMarketReactions putStockChangesItem(String key, BigDecimal stockChangesItem) {
    if (this.stockChanges == null) {
      this.stockChanges = new HashMap<>();
    }
    this.stockChanges.put(key, stockChangesItem);
    return this;
  }

  /**
   * Get stockChanges
   * @return stockChanges
   */
  @Valid 
  @Schema(name = "stock_changes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stock_changes")
  public Map<String, BigDecimal> getStockChanges() {
    return stockChanges;
  }

  public void setStockChanges(Map<String, BigDecimal> stockChanges) {
    this.stockChanges = stockChanges;
  }

  public EconomyEventDetailedAllOfMarketReactions commodityChanges(Map<String, BigDecimal> commodityChanges) {
    this.commodityChanges = commodityChanges;
    return this;
  }

  public EconomyEventDetailedAllOfMarketReactions putCommodityChangesItem(String key, BigDecimal commodityChangesItem) {
    if (this.commodityChanges == null) {
      this.commodityChanges = new HashMap<>();
    }
    this.commodityChanges.put(key, commodityChangesItem);
    return this;
  }

  /**
   * Get commodityChanges
   * @return commodityChanges
   */
  @Valid 
  @Schema(name = "commodity_changes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("commodity_changes")
  public Map<String, BigDecimal> getCommodityChanges() {
    return commodityChanges;
  }

  public void setCommodityChanges(Map<String, BigDecimal> commodityChanges) {
    this.commodityChanges = commodityChanges;
  }

  public EconomyEventDetailedAllOfMarketReactions currencyImpact(Map<String, BigDecimal> currencyImpact) {
    this.currencyImpact = currencyImpact;
    return this;
  }

  public EconomyEventDetailedAllOfMarketReactions putCurrencyImpactItem(String key, BigDecimal currencyImpactItem) {
    if (this.currencyImpact == null) {
      this.currencyImpact = new HashMap<>();
    }
    this.currencyImpact.put(key, currencyImpactItem);
    return this;
  }

  /**
   * Get currencyImpact
   * @return currencyImpact
   */
  @Valid 
  @Schema(name = "currency_impact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency_impact")
  public Map<String, BigDecimal> getCurrencyImpact() {
    return currencyImpact;
  }

  public void setCurrencyImpact(Map<String, BigDecimal> currencyImpact) {
    this.currencyImpact = currencyImpact;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EconomyEventDetailedAllOfMarketReactions economyEventDetailedAllOfMarketReactions = (EconomyEventDetailedAllOfMarketReactions) o;
    return Objects.equals(this.stockChanges, economyEventDetailedAllOfMarketReactions.stockChanges) &&
        Objects.equals(this.commodityChanges, economyEventDetailedAllOfMarketReactions.commodityChanges) &&
        Objects.equals(this.currencyImpact, economyEventDetailedAllOfMarketReactions.currencyImpact);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stockChanges, commodityChanges, currencyImpact);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EconomyEventDetailedAllOfMarketReactions {\n");
    sb.append("    stockChanges: ").append(toIndentedString(stockChanges)).append("\n");
    sb.append("    commodityChanges: ").append(toIndentedString(commodityChanges)).append("\n");
    sb.append("    currencyImpact: ").append(toIndentedString(currencyImpact)).append("\n");
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

