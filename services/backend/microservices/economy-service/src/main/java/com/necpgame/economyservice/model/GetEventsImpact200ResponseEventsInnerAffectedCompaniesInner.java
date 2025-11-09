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
 * GetEventsImpact200ResponseEventsInnerAffectedCompaniesInner
 */

@JsonTypeName("getEventsImpact_200_response_events_inner_affected_companies_inner")

public class GetEventsImpact200ResponseEventsInnerAffectedCompaniesInner {

  private @Nullable String ticker;

  /**
   * Gets or Sets impact
   */
  public enum ImpactEnum {
    VERY_NEGATIVE("very_negative"),
    
    NEGATIVE("negative"),
    
    NEUTRAL("neutral"),
    
    POSITIVE("positive"),
    
    VERY_POSITIVE("very_positive");

    private final String value;

    ImpactEnum(String value) {
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
    public static ImpactEnum fromValue(String value) {
      for (ImpactEnum b : ImpactEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ImpactEnum impact;

  private @Nullable BigDecimal priceChangeExpected;

  public GetEventsImpact200ResponseEventsInnerAffectedCompaniesInner ticker(@Nullable String ticker) {
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

  public GetEventsImpact200ResponseEventsInnerAffectedCompaniesInner impact(@Nullable ImpactEnum impact) {
    this.impact = impact;
    return this;
  }

  /**
   * Get impact
   * @return impact
   */
  
  @Schema(name = "impact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("impact")
  public @Nullable ImpactEnum getImpact() {
    return impact;
  }

  public void setImpact(@Nullable ImpactEnum impact) {
    this.impact = impact;
  }

  public GetEventsImpact200ResponseEventsInnerAffectedCompaniesInner priceChangeExpected(@Nullable BigDecimal priceChangeExpected) {
    this.priceChangeExpected = priceChangeExpected;
    return this;
  }

  /**
   * Get priceChangeExpected
   * @return priceChangeExpected
   */
  @Valid 
  @Schema(name = "price_change_expected", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price_change_expected")
  public @Nullable BigDecimal getPriceChangeExpected() {
    return priceChangeExpected;
  }

  public void setPriceChangeExpected(@Nullable BigDecimal priceChangeExpected) {
    this.priceChangeExpected = priceChangeExpected;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetEventsImpact200ResponseEventsInnerAffectedCompaniesInner getEventsImpact200ResponseEventsInnerAffectedCompaniesInner = (GetEventsImpact200ResponseEventsInnerAffectedCompaniesInner) o;
    return Objects.equals(this.ticker, getEventsImpact200ResponseEventsInnerAffectedCompaniesInner.ticker) &&
        Objects.equals(this.impact, getEventsImpact200ResponseEventsInnerAffectedCompaniesInner.impact) &&
        Objects.equals(this.priceChangeExpected, getEventsImpact200ResponseEventsInnerAffectedCompaniesInner.priceChangeExpected);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticker, impact, priceChangeExpected);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetEventsImpact200ResponseEventsInnerAffectedCompaniesInner {\n");
    sb.append("    ticker: ").append(toIndentedString(ticker)).append("\n");
    sb.append("    impact: ").append(toIndentedString(impact)).append("\n");
    sb.append("    priceChangeExpected: ").append(toIndentedString(priceChangeExpected)).append("\n");
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

