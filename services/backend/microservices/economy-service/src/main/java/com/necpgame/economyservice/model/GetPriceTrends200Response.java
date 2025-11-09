package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * GetPriceTrends200Response
 */

@JsonTypeName("getPriceTrends_200_response")

public class GetPriceTrends200Response {

  private @Nullable String itemId;

  /**
   * Gets or Sets trend
   */
  public enum TrendEnum {
    INCREASING("increasing"),
    
    DECREASING("decreasing"),
    
    STABLE("stable"),
    
    VOLATILE("volatile");

    private final String value;

    TrendEnum(String value) {
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
    public static TrendEnum fromValue(String value) {
      for (TrendEnum b : TrendEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TrendEnum trend;

  private @Nullable BigDecimal priceChange7d;

  private @Nullable BigDecimal priceChange30d;

  private @Nullable BigDecimal volatility;

  public GetPriceTrends200Response itemId(@Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_id")
  public @Nullable String getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable String itemId) {
    this.itemId = itemId;
  }

  public GetPriceTrends200Response trend(@Nullable TrendEnum trend) {
    this.trend = trend;
    return this;
  }

  /**
   * Get trend
   * @return trend
   */
  
  @Schema(name = "trend", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trend")
  public @Nullable TrendEnum getTrend() {
    return trend;
  }

  public void setTrend(@Nullable TrendEnum trend) {
    this.trend = trend;
  }

  public GetPriceTrends200Response priceChange7d(@Nullable BigDecimal priceChange7d) {
    this.priceChange7d = priceChange7d;
    return this;
  }

  /**
   * Изменение за 7 дней (%)
   * @return priceChange7d
   */
  @Valid 
  @Schema(name = "price_change_7d", description = "Изменение за 7 дней (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price_change_7d")
  public @Nullable BigDecimal getPriceChange7d() {
    return priceChange7d;
  }

  public void setPriceChange7d(@Nullable BigDecimal priceChange7d) {
    this.priceChange7d = priceChange7d;
  }

  public GetPriceTrends200Response priceChange30d(@Nullable BigDecimal priceChange30d) {
    this.priceChange30d = priceChange30d;
    return this;
  }

  /**
   * Изменение за 30 дней (%)
   * @return priceChange30d
   */
  @Valid 
  @Schema(name = "price_change_30d", description = "Изменение за 30 дней (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price_change_30d")
  public @Nullable BigDecimal getPriceChange30d() {
    return priceChange30d;
  }

  public void setPriceChange30d(@Nullable BigDecimal priceChange30d) {
    this.priceChange30d = priceChange30d;
  }

  public GetPriceTrends200Response volatility(@Nullable BigDecimal volatility) {
    this.volatility = volatility;
    return this;
  }

  /**
   * Волатильность (%)
   * @return volatility
   */
  @Valid 
  @Schema(name = "volatility", description = "Волатильность (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("volatility")
  public @Nullable BigDecimal getVolatility() {
    return volatility;
  }

  public void setVolatility(@Nullable BigDecimal volatility) {
    this.volatility = volatility;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetPriceTrends200Response getPriceTrends200Response = (GetPriceTrends200Response) o;
    return Objects.equals(this.itemId, getPriceTrends200Response.itemId) &&
        Objects.equals(this.trend, getPriceTrends200Response.trend) &&
        Objects.equals(this.priceChange7d, getPriceTrends200Response.priceChange7d) &&
        Objects.equals(this.priceChange30d, getPriceTrends200Response.priceChange30d) &&
        Objects.equals(this.volatility, getPriceTrends200Response.volatility);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, trend, priceChange7d, priceChange30d, volatility);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetPriceTrends200Response {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    trend: ").append(toIndentedString(trend)).append("\n");
    sb.append("    priceChange7d: ").append(toIndentedString(priceChange7d)).append("\n");
    sb.append("    priceChange30d: ").append(toIndentedString(priceChange30d)).append("\n");
    sb.append("    volatility: ").append(toIndentedString(volatility)).append("\n");
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

