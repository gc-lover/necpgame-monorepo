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
 * MarketSentiment
 */


public class MarketSentiment {

  private @Nullable String market;

  private @Nullable BigDecimal bullBearRatio;

  /**
   * Gets or Sets volumeTrend
   */
  public enum VolumeTrendEnum {
    INCREASING("increasing"),
    
    DECREASING("decreasing"),
    
    STABLE("stable");

    private final String value;

    VolumeTrendEnum(String value) {
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
    public static VolumeTrendEnum fromValue(String value) {
      for (VolumeTrendEnum b : VolumeTrendEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable VolumeTrendEnum volumeTrend;

  /**
   * Gets or Sets momentum
   */
  public enum MomentumEnum {
    STRONG_BULLISH("strong_bullish"),
    
    BULLISH("bullish"),
    
    NEUTRAL("neutral"),
    
    BEARISH("bearish"),
    
    STRONG_BEARISH("strong_bearish");

    private final String value;

    MomentumEnum(String value) {
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
    public static MomentumEnum fromValue(String value) {
      for (MomentumEnum b : MomentumEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable MomentumEnum momentum;

  public MarketSentiment market(@Nullable String market) {
    this.market = market;
    return this;
  }

  /**
   * Get market
   * @return market
   */
  
  @Schema(name = "market", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("market")
  public @Nullable String getMarket() {
    return market;
  }

  public void setMarket(@Nullable String market) {
    this.market = market;
  }

  public MarketSentiment bullBearRatio(@Nullable BigDecimal bullBearRatio) {
    this.bullBearRatio = bullBearRatio;
    return this;
  }

  /**
   * Get bullBearRatio
   * @return bullBearRatio
   */
  @Valid 
  @Schema(name = "bull_bear_ratio", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bull_bear_ratio")
  public @Nullable BigDecimal getBullBearRatio() {
    return bullBearRatio;
  }

  public void setBullBearRatio(@Nullable BigDecimal bullBearRatio) {
    this.bullBearRatio = bullBearRatio;
  }

  public MarketSentiment volumeTrend(@Nullable VolumeTrendEnum volumeTrend) {
    this.volumeTrend = volumeTrend;
    return this;
  }

  /**
   * Get volumeTrend
   * @return volumeTrend
   */
  
  @Schema(name = "volume_trend", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("volume_trend")
  public @Nullable VolumeTrendEnum getVolumeTrend() {
    return volumeTrend;
  }

  public void setVolumeTrend(@Nullable VolumeTrendEnum volumeTrend) {
    this.volumeTrend = volumeTrend;
  }

  public MarketSentiment momentum(@Nullable MomentumEnum momentum) {
    this.momentum = momentum;
    return this;
  }

  /**
   * Get momentum
   * @return momentum
   */
  
  @Schema(name = "momentum", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("momentum")
  public @Nullable MomentumEnum getMomentum() {
    return momentum;
  }

  public void setMomentum(@Nullable MomentumEnum momentum) {
    this.momentum = momentum;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MarketSentiment marketSentiment = (MarketSentiment) o;
    return Objects.equals(this.market, marketSentiment.market) &&
        Objects.equals(this.bullBearRatio, marketSentiment.bullBearRatio) &&
        Objects.equals(this.volumeTrend, marketSentiment.volumeTrend) &&
        Objects.equals(this.momentum, marketSentiment.momentum);
  }

  @Override
  public int hashCode() {
    return Objects.hash(market, bullBearRatio, volumeTrend, momentum);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MarketSentiment {\n");
    sb.append("    market: ").append(toIndentedString(market)).append("\n");
    sb.append("    bullBearRatio: ").append(toIndentedString(bullBearRatio)).append("\n");
    sb.append("    volumeTrend: ").append(toIndentedString(volumeTrend)).append("\n");
    sb.append("    momentum: ").append(toIndentedString(momentum)).append("\n");
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

