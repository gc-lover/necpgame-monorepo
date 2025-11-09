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
 * ExchangeCurrencyRequest
 */

@JsonTypeName("exchangeCurrency_request")

public class ExchangeCurrencyRequest {

  private String characterId;

  private String fromCurrency;

  private String toCurrency;

  private BigDecimal amount;

  public ExchangeCurrencyRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ExchangeCurrencyRequest(String characterId, String fromCurrency, String toCurrency, BigDecimal amount) {
    this.characterId = characterId;
    this.fromCurrency = fromCurrency;
    this.toCurrency = toCurrency;
    this.amount = amount;
  }

  public ExchangeCurrencyRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public ExchangeCurrencyRequest fromCurrency(String fromCurrency) {
    this.fromCurrency = fromCurrency;
    return this;
  }

  /**
   * Get fromCurrency
   * @return fromCurrency
   */
  @NotNull 
  @Schema(name = "from_currency", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("from_currency")
  public String getFromCurrency() {
    return fromCurrency;
  }

  public void setFromCurrency(String fromCurrency) {
    this.fromCurrency = fromCurrency;
  }

  public ExchangeCurrencyRequest toCurrency(String toCurrency) {
    this.toCurrency = toCurrency;
    return this;
  }

  /**
   * Get toCurrency
   * @return toCurrency
   */
  @NotNull 
  @Schema(name = "to_currency", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("to_currency")
  public String getToCurrency() {
    return toCurrency;
  }

  public void setToCurrency(String toCurrency) {
    this.toCurrency = toCurrency;
  }

  public ExchangeCurrencyRequest amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * minimum: 0.01
   * @return amount
   */
  @NotNull @Valid @DecimalMin(value = "0.01") 
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("amount")
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExchangeCurrencyRequest exchangeCurrencyRequest = (ExchangeCurrencyRequest) o;
    return Objects.equals(this.characterId, exchangeCurrencyRequest.characterId) &&
        Objects.equals(this.fromCurrency, exchangeCurrencyRequest.fromCurrency) &&
        Objects.equals(this.toCurrency, exchangeCurrencyRequest.toCurrency) &&
        Objects.equals(this.amount, exchangeCurrencyRequest.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, fromCurrency, toCurrency, amount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExchangeCurrencyRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    fromCurrency: ").append(toIndentedString(fromCurrency)).append("\n");
    sb.append("    toCurrency: ").append(toIndentedString(toCurrency)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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

