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
 * GetTradeHistory200ResponseStatistics
 */

@JsonTypeName("getTradeHistory_200_response_statistics")

public class GetTradeHistory200ResponseStatistics {

  private @Nullable Integer totalTrades;

  private @Nullable Integer profitableTrades;

  private @Nullable BigDecimal winRate;

  private @Nullable BigDecimal averageProfit;

  public GetTradeHistory200ResponseStatistics totalTrades(@Nullable Integer totalTrades) {
    this.totalTrades = totalTrades;
    return this;
  }

  /**
   * Get totalTrades
   * @return totalTrades
   */
  
  @Schema(name = "total_trades", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_trades")
  public @Nullable Integer getTotalTrades() {
    return totalTrades;
  }

  public void setTotalTrades(@Nullable Integer totalTrades) {
    this.totalTrades = totalTrades;
  }

  public GetTradeHistory200ResponseStatistics profitableTrades(@Nullable Integer profitableTrades) {
    this.profitableTrades = profitableTrades;
    return this;
  }

  /**
   * Get profitableTrades
   * @return profitableTrades
   */
  
  @Schema(name = "profitable_trades", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("profitable_trades")
  public @Nullable Integer getProfitableTrades() {
    return profitableTrades;
  }

  public void setProfitableTrades(@Nullable Integer profitableTrades) {
    this.profitableTrades = profitableTrades;
  }

  public GetTradeHistory200ResponseStatistics winRate(@Nullable BigDecimal winRate) {
    this.winRate = winRate;
    return this;
  }

  /**
   * Get winRate
   * @return winRate
   */
  @Valid 
  @Schema(name = "win_rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("win_rate")
  public @Nullable BigDecimal getWinRate() {
    return winRate;
  }

  public void setWinRate(@Nullable BigDecimal winRate) {
    this.winRate = winRate;
  }

  public GetTradeHistory200ResponseStatistics averageProfit(@Nullable BigDecimal averageProfit) {
    this.averageProfit = averageProfit;
    return this;
  }

  /**
   * Get averageProfit
   * @return averageProfit
   */
  @Valid 
  @Schema(name = "average_profit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("average_profit")
  public @Nullable BigDecimal getAverageProfit() {
    return averageProfit;
  }

  public void setAverageProfit(@Nullable BigDecimal averageProfit) {
    this.averageProfit = averageProfit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetTradeHistory200ResponseStatistics getTradeHistory200ResponseStatistics = (GetTradeHistory200ResponseStatistics) o;
    return Objects.equals(this.totalTrades, getTradeHistory200ResponseStatistics.totalTrades) &&
        Objects.equals(this.profitableTrades, getTradeHistory200ResponseStatistics.profitableTrades) &&
        Objects.equals(this.winRate, getTradeHistory200ResponseStatistics.winRate) &&
        Objects.equals(this.averageProfit, getTradeHistory200ResponseStatistics.averageProfit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalTrades, profitableTrades, winRate, averageProfit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetTradeHistory200ResponseStatistics {\n");
    sb.append("    totalTrades: ").append(toIndentedString(totalTrades)).append("\n");
    sb.append("    profitableTrades: ").append(toIndentedString(profitableTrades)).append("\n");
    sb.append("    winRate: ").append(toIndentedString(winRate)).append("\n");
    sb.append("    averageProfit: ").append(toIndentedString(averageProfit)).append("\n");
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

