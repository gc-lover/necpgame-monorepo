package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * MarketDataPriceTrends
 */

@JsonTypeName("MarketData_price_trends")

public class MarketDataPriceTrends {

  @Valid
  private List<String> trendingUp = new ArrayList<>();

  @Valid
  private List<String> trendingDown = new ArrayList<>();

  public MarketDataPriceTrends trendingUp(List<String> trendingUp) {
    this.trendingUp = trendingUp;
    return this;
  }

  public MarketDataPriceTrends addTrendingUpItem(String trendingUpItem) {
    if (this.trendingUp == null) {
      this.trendingUp = new ArrayList<>();
    }
    this.trendingUp.add(trendingUpItem);
    return this;
  }

  /**
   * Get trendingUp
   * @return trendingUp
   */
  
  @Schema(name = "trending_up", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trending_up")
  public List<String> getTrendingUp() {
    return trendingUp;
  }

  public void setTrendingUp(List<String> trendingUp) {
    this.trendingUp = trendingUp;
  }

  public MarketDataPriceTrends trendingDown(List<String> trendingDown) {
    this.trendingDown = trendingDown;
    return this;
  }

  public MarketDataPriceTrends addTrendingDownItem(String trendingDownItem) {
    if (this.trendingDown == null) {
      this.trendingDown = new ArrayList<>();
    }
    this.trendingDown.add(trendingDownItem);
    return this;
  }

  /**
   * Get trendingDown
   * @return trendingDown
   */
  
  @Schema(name = "trending_down", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trending_down")
  public List<String> getTrendingDown() {
    return trendingDown;
  }

  public void setTrendingDown(List<String> trendingDown) {
    this.trendingDown = trendingDown;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MarketDataPriceTrends marketDataPriceTrends = (MarketDataPriceTrends) o;
    return Objects.equals(this.trendingUp, marketDataPriceTrends.trendingUp) &&
        Objects.equals(this.trendingDown, marketDataPriceTrends.trendingDown);
  }

  @Override
  public int hashCode() {
    return Objects.hash(trendingUp, trendingDown);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MarketDataPriceTrends {\n");
    sb.append("    trendingUp: ").append(toIndentedString(trendingUp)).append("\n");
    sb.append("    trendingDown: ").append(toIndentedString(trendingDown)).append("\n");
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

