package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ConvertRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ConvertRequest {

  private UUID characterId;

  private String fromCurrency;

  private String toCurrency;

  private Float amount;

  public ConvertRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ConvertRequest(UUID characterId, String fromCurrency, String toCurrency, Float amount) {
    this.characterId = characterId;
    this.fromCurrency = fromCurrency;
    this.toCurrency = toCurrency;
    this.amount = amount;
  }

  public ConvertRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public ConvertRequest fromCurrency(String fromCurrency) {
    this.fromCurrency = fromCurrency;
    return this;
  }

  /**
   * Get fromCurrency
   * @return fromCurrency
   */
  @NotNull 
  @Schema(name = "from_currency", example = "NCRD", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("from_currency")
  public String getFromCurrency() {
    return fromCurrency;
  }

  public void setFromCurrency(String fromCurrency) {
    this.fromCurrency = fromCurrency;
  }

  public ConvertRequest toCurrency(String toCurrency) {
    this.toCurrency = toCurrency;
    return this;
  }

  /**
   * Get toCurrency
   * @return toCurrency
   */
  @NotNull 
  @Schema(name = "to_currency", example = "EURO", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("to_currency")
  public String getToCurrency() {
    return toCurrency;
  }

  public void setToCurrency(String toCurrency) {
    this.toCurrency = toCurrency;
  }

  public ConvertRequest amount(Float amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   */
  @NotNull 
  @Schema(name = "amount", example = "1000.0", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("amount")
  public Float getAmount() {
    return amount;
  }

  public void setAmount(Float amount) {
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
    ConvertRequest convertRequest = (ConvertRequest) o;
    return Objects.equals(this.characterId, convertRequest.characterId) &&
        Objects.equals(this.fromCurrency, convertRequest.fromCurrency) &&
        Objects.equals(this.toCurrency, convertRequest.toCurrency) &&
        Objects.equals(this.amount, convertRequest.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, fromCurrency, toCurrency, amount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConvertRequest {\n");
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

