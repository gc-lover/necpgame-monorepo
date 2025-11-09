package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.GetExchangeRates200ResponseRatesInner;
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
 * GetExchangeRates200Response
 */

@JsonTypeName("getExchangeRates_200_response")

public class GetExchangeRates200Response {

  private @Nullable String baseCurrency;

  @Valid
  private List<@Valid GetExchangeRates200ResponseRatesInner> rates = new ArrayList<>();

  public GetExchangeRates200Response baseCurrency(@Nullable String baseCurrency) {
    this.baseCurrency = baseCurrency;
    return this;
  }

  /**
   * Get baseCurrency
   * @return baseCurrency
   */
  
  @Schema(name = "base_currency", example = "eurodollar", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base_currency")
  public @Nullable String getBaseCurrency() {
    return baseCurrency;
  }

  public void setBaseCurrency(@Nullable String baseCurrency) {
    this.baseCurrency = baseCurrency;
  }

  public GetExchangeRates200Response rates(List<@Valid GetExchangeRates200ResponseRatesInner> rates) {
    this.rates = rates;
    return this;
  }

  public GetExchangeRates200Response addRatesItem(GetExchangeRates200ResponseRatesInner ratesItem) {
    if (this.rates == null) {
      this.rates = new ArrayList<>();
    }
    this.rates.add(ratesItem);
    return this;
  }

  /**
   * Get rates
   * @return rates
   */
  @Valid 
  @Schema(name = "rates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rates")
  public List<@Valid GetExchangeRates200ResponseRatesInner> getRates() {
    return rates;
  }

  public void setRates(List<@Valid GetExchangeRates200ResponseRatesInner> rates) {
    this.rates = rates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetExchangeRates200Response getExchangeRates200Response = (GetExchangeRates200Response) o;
    return Objects.equals(this.baseCurrency, getExchangeRates200Response.baseCurrency) &&
        Objects.equals(this.rates, getExchangeRates200Response.rates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseCurrency, rates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetExchangeRates200Response {\n");
    sb.append("    baseCurrency: ").append(toIndentedString(baseCurrency)).append("\n");
    sb.append("    rates: ").append(toIndentedString(rates)).append("\n");
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

