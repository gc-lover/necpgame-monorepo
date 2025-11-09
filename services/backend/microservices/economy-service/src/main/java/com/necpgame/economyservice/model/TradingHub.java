package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TradingHub
 */


public class TradingHub {

  private @Nullable String hubId;

  private @Nullable String name;

  private @Nullable Integer tier;

  private @Nullable String region;

  private @Nullable Object marketPrices;

  public TradingHub hubId(@Nullable String hubId) {
    this.hubId = hubId;
    return this;
  }

  /**
   * Get hubId
   * @return hubId
   */
  
  @Schema(name = "hub_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hub_id")
  public @Nullable String getHubId() {
    return hubId;
  }

  public void setHubId(@Nullable String hubId) {
    this.hubId = hubId;
  }

  public TradingHub name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public TradingHub tier(@Nullable Integer tier) {
    this.tier = tier;
    return this;
  }

  /**
   * Get tier
   * @return tier
   */
  
  @Schema(name = "tier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tier")
  public @Nullable Integer getTier() {
    return tier;
  }

  public void setTier(@Nullable Integer tier) {
    this.tier = tier;
  }

  public TradingHub region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  public TradingHub marketPrices(@Nullable Object marketPrices) {
    this.marketPrices = marketPrices;
    return this;
  }

  /**
   * Get marketPrices
   * @return marketPrices
   */
  
  @Schema(name = "market_prices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("market_prices")
  public @Nullable Object getMarketPrices() {
    return marketPrices;
  }

  public void setMarketPrices(@Nullable Object marketPrices) {
    this.marketPrices = marketPrices;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TradingHub tradingHub = (TradingHub) o;
    return Objects.equals(this.hubId, tradingHub.hubId) &&
        Objects.equals(this.name, tradingHub.name) &&
        Objects.equals(this.tier, tradingHub.tier) &&
        Objects.equals(this.region, tradingHub.region) &&
        Objects.equals(this.marketPrices, tradingHub.marketPrices);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hubId, name, tier, region, marketPrices);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TradingHub {\n");
    sb.append("    hubId: ").append(toIndentedString(hubId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    marketPrices: ").append(toIndentedString(marketPrices)).append("\n");
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

