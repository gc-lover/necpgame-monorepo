package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.Currency;
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
 * GetCurrencies200Response
 */

@JsonTypeName("getCurrencies_200_response")

public class GetCurrencies200Response {

  @Valid
  private List<@Valid Currency> currencies = new ArrayList<>();

  public GetCurrencies200Response currencies(List<@Valid Currency> currencies) {
    this.currencies = currencies;
    return this;
  }

  public GetCurrencies200Response addCurrenciesItem(Currency currenciesItem) {
    if (this.currencies == null) {
      this.currencies = new ArrayList<>();
    }
    this.currencies.add(currenciesItem);
    return this;
  }

  /**
   * Get currencies
   * @return currencies
   */
  @Valid 
  @Schema(name = "currencies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currencies")
  public List<@Valid Currency> getCurrencies() {
    return currencies;
  }

  public void setCurrencies(List<@Valid Currency> currencies) {
    this.currencies = currencies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCurrencies200Response getCurrencies200Response = (GetCurrencies200Response) o;
    return Objects.equals(this.currencies, getCurrencies200Response.currencies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currencies);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCurrencies200Response {\n");
    sb.append("    currencies: ").append(toIndentedString(currencies)).append("\n");
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

