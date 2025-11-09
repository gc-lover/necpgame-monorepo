package com.necpgame.backjava.model;

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
 * TradingGuildDetailedAllOfBonuses
 */

@JsonTypeName("TradingGuildDetailed_allOf_bonuses")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class TradingGuildDetailedAllOfBonuses {

  private @Nullable Float tradingFeeReduction;

  private @Nullable BigDecimal profitMarginIncrease;

  private @Nullable Integer exclusiveRoutesCount;

  public TradingGuildDetailedAllOfBonuses tradingFeeReduction(@Nullable Float tradingFeeReduction) {
    this.tradingFeeReduction = tradingFeeReduction;
    return this;
  }

  /**
   * Get tradingFeeReduction
   * @return tradingFeeReduction
   */
  
  @Schema(name = "trading_fee_reduction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trading_fee_reduction")
  public @Nullable Float getTradingFeeReduction() {
    return tradingFeeReduction;
  }

  public void setTradingFeeReduction(@Nullable Float tradingFeeReduction) {
    this.tradingFeeReduction = tradingFeeReduction;
  }

  public TradingGuildDetailedAllOfBonuses profitMarginIncrease(@Nullable BigDecimal profitMarginIncrease) {
    this.profitMarginIncrease = profitMarginIncrease;
    return this;
  }

  /**
   * Get profitMarginIncrease
   * @return profitMarginIncrease
   */
  @Valid 
  @Schema(name = "profit_margin_increase", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("profit_margin_increase")
  public @Nullable BigDecimal getProfitMarginIncrease() {
    return profitMarginIncrease;
  }

  public void setProfitMarginIncrease(@Nullable BigDecimal profitMarginIncrease) {
    this.profitMarginIncrease = profitMarginIncrease;
  }

  public TradingGuildDetailedAllOfBonuses exclusiveRoutesCount(@Nullable Integer exclusiveRoutesCount) {
    this.exclusiveRoutesCount = exclusiveRoutesCount;
    return this;
  }

  /**
   * Get exclusiveRoutesCount
   * @return exclusiveRoutesCount
   */
  
  @Schema(name = "exclusive_routes_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("exclusive_routes_count")
  public @Nullable Integer getExclusiveRoutesCount() {
    return exclusiveRoutesCount;
  }

  public void setExclusiveRoutesCount(@Nullable Integer exclusiveRoutesCount) {
    this.exclusiveRoutesCount = exclusiveRoutesCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TradingGuildDetailedAllOfBonuses tradingGuildDetailedAllOfBonuses = (TradingGuildDetailedAllOfBonuses) o;
    return Objects.equals(this.tradingFeeReduction, tradingGuildDetailedAllOfBonuses.tradingFeeReduction) &&
        Objects.equals(this.profitMarginIncrease, tradingGuildDetailedAllOfBonuses.profitMarginIncrease) &&
        Objects.equals(this.exclusiveRoutesCount, tradingGuildDetailedAllOfBonuses.exclusiveRoutesCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tradingFeeReduction, profitMarginIncrease, exclusiveRoutesCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TradingGuildDetailedAllOfBonuses {\n");
    sb.append("    tradingFeeReduction: ").append(toIndentedString(tradingFeeReduction)).append("\n");
    sb.append("    profitMarginIncrease: ").append(toIndentedString(profitMarginIncrease)).append("\n");
    sb.append("    exclusiveRoutesCount: ").append(toIndentedString(exclusiveRoutesCount)).append("\n");
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

