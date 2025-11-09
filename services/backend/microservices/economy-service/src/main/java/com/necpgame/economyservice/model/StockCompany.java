package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * StockCompany
 */


public class StockCompany {

  private @Nullable String ticker;

  private @Nullable String name;

  /**
   * Gets or Sets sector
   */
  public enum SectorEnum {
    SECURITY_TECH("security_tech"),
    
    MILITARY_TECH("military_tech"),
    
    BIOTECH("biotech"),
    
    MANUFACTURING("manufacturing"),
    
    FINANCE("finance"),
    
    MEDIA("media");

    private final String value;

    SectorEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static SectorEnum fromValue(String value) {
      for (SectorEnum b : SectorEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SectorEnum sector;

  private @Nullable BigDecimal currentPrice;

  private @Nullable BigDecimal priceChange24h;

  private @Nullable BigDecimal marketCap;

  private @Nullable Integer sharesOutstanding;

  private @Nullable BigDecimal dividendYield;

  private @Nullable Integer volume24h;

  public StockCompany ticker(@Nullable String ticker) {
    this.ticker = ticker;
    return this;
  }

  /**
   * Биржевой тикер (ARSK, MILT, etc.)
   * @return ticker
   */
  
  @Schema(name = "ticker", description = "Биржевой тикер (ARSK, MILT, etc.)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ticker")
  public @Nullable String getTicker() {
    return ticker;
  }

  public void setTicker(@Nullable String ticker) {
    this.ticker = ticker;
  }

  public StockCompany name(@Nullable String name) {
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

  public StockCompany sector(@Nullable SectorEnum sector) {
    this.sector = sector;
    return this;
  }

  /**
   * Get sector
   * @return sector
   */
  
  @Schema(name = "sector", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sector")
  public @Nullable SectorEnum getSector() {
    return sector;
  }

  public void setSector(@Nullable SectorEnum sector) {
    this.sector = sector;
  }

  public StockCompany currentPrice(@Nullable BigDecimal currentPrice) {
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

  public StockCompany priceChange24h(@Nullable BigDecimal priceChange24h) {
    this.priceChange24h = priceChange24h;
    return this;
  }

  /**
   * Изменение за 24 часа (%)
   * @return priceChange24h
   */
  @Valid 
  @Schema(name = "price_change_24h", description = "Изменение за 24 часа (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price_change_24h")
  public @Nullable BigDecimal getPriceChange24h() {
    return priceChange24h;
  }

  public void setPriceChange24h(@Nullable BigDecimal priceChange24h) {
    this.priceChange24h = priceChange24h;
  }

  public StockCompany marketCap(@Nullable BigDecimal marketCap) {
    this.marketCap = marketCap;
    return this;
  }

  /**
   * Рыночная капитализация
   * @return marketCap
   */
  @Valid 
  @Schema(name = "market_cap", description = "Рыночная капитализация", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("market_cap")
  public @Nullable BigDecimal getMarketCap() {
    return marketCap;
  }

  public void setMarketCap(@Nullable BigDecimal marketCap) {
    this.marketCap = marketCap;
  }

  public StockCompany sharesOutstanding(@Nullable Integer sharesOutstanding) {
    this.sharesOutstanding = sharesOutstanding;
    return this;
  }

  /**
   * Количество акций в обращении
   * @return sharesOutstanding
   */
  
  @Schema(name = "shares_outstanding", description = "Количество акций в обращении", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("shares_outstanding")
  public @Nullable Integer getSharesOutstanding() {
    return sharesOutstanding;
  }

  public void setSharesOutstanding(@Nullable Integer sharesOutstanding) {
    this.sharesOutstanding = sharesOutstanding;
  }

  public StockCompany dividendYield(@Nullable BigDecimal dividendYield) {
    this.dividendYield = dividendYield;
    return this;
  }

  /**
   * Доходность дивидендов (%)
   * @return dividendYield
   */
  @Valid 
  @Schema(name = "dividend_yield", description = "Доходность дивидендов (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dividend_yield")
  public @Nullable BigDecimal getDividendYield() {
    return dividendYield;
  }

  public void setDividendYield(@Nullable BigDecimal dividendYield) {
    this.dividendYield = dividendYield;
  }

  public StockCompany volume24h(@Nullable Integer volume24h) {
    this.volume24h = volume24h;
    return this;
  }

  /**
   * Объем торгов за 24 часа
   * @return volume24h
   */
  
  @Schema(name = "volume_24h", description = "Объем торгов за 24 часа", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("volume_24h")
  public @Nullable Integer getVolume24h() {
    return volume24h;
  }

  public void setVolume24h(@Nullable Integer volume24h) {
    this.volume24h = volume24h;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StockCompany stockCompany = (StockCompany) o;
    return Objects.equals(this.ticker, stockCompany.ticker) &&
        Objects.equals(this.name, stockCompany.name) &&
        Objects.equals(this.sector, stockCompany.sector) &&
        Objects.equals(this.currentPrice, stockCompany.currentPrice) &&
        Objects.equals(this.priceChange24h, stockCompany.priceChange24h) &&
        Objects.equals(this.marketCap, stockCompany.marketCap) &&
        Objects.equals(this.sharesOutstanding, stockCompany.sharesOutstanding) &&
        Objects.equals(this.dividendYield, stockCompany.dividendYield) &&
        Objects.equals(this.volume24h, stockCompany.volume24h);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticker, name, sector, currentPrice, priceChange24h, marketCap, sharesOutstanding, dividendYield, volume24h);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StockCompany {\n");
    sb.append("    ticker: ").append(toIndentedString(ticker)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    sector: ").append(toIndentedString(sector)).append("\n");
    sb.append("    currentPrice: ").append(toIndentedString(currentPrice)).append("\n");
    sb.append("    priceChange24h: ").append(toIndentedString(priceChange24h)).append("\n");
    sb.append("    marketCap: ").append(toIndentedString(marketCap)).append("\n");
    sb.append("    sharesOutstanding: ").append(toIndentedString(sharesOutstanding)).append("\n");
    sb.append("    dividendYield: ").append(toIndentedString(dividendYield)).append("\n");
    sb.append("    volume24h: ").append(toIndentedString(volume24h)).append("\n");
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

