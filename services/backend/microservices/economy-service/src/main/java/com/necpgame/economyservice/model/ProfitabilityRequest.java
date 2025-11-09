package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ProfitabilityRequest
 */


public class ProfitabilityRequest {

  private String chainId;

  private Integer quantity = 1;

  private JsonNullable<Object> inputPrices = JsonNullable.<Object>undefined();

  private JsonNullable<Integer> sellingPrice = JsonNullable.<Integer>undefined();

  public ProfitabilityRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ProfitabilityRequest(String chainId) {
    this.chainId = chainId;
  }

  public ProfitabilityRequest chainId(String chainId) {
    this.chainId = chainId;
    return this;
  }

  /**
   * Get chainId
   * @return chainId
   */
  @NotNull 
  @Schema(name = "chain_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("chain_id")
  public String getChainId() {
    return chainId;
  }

  public void setChainId(String chainId) {
    this.chainId = chainId;
  }

  public ProfitabilityRequest quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public ProfitabilityRequest inputPrices(Object inputPrices) {
    this.inputPrices = JsonNullable.of(inputPrices);
    return this;
  }

  /**
   * Текущие цены входов (если не указаны - берутся market)
   * @return inputPrices
   */
  
  @Schema(name = "input_prices", description = "Текущие цены входов (если не указаны - берутся market)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("input_prices")
  public JsonNullable<Object> getInputPrices() {
    return inputPrices;
  }

  public void setInputPrices(JsonNullable<Object> inputPrices) {
    this.inputPrices = inputPrices;
  }

  public ProfitabilityRequest sellingPrice(Integer sellingPrice) {
    this.sellingPrice = JsonNullable.of(sellingPrice);
    return this;
  }

  /**
   * Get sellingPrice
   * @return sellingPrice
   */
  
  @Schema(name = "selling_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("selling_price")
  public JsonNullable<Integer> getSellingPrice() {
    return sellingPrice;
  }

  public void setSellingPrice(JsonNullable<Integer> sellingPrice) {
    this.sellingPrice = sellingPrice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProfitabilityRequest profitabilityRequest = (ProfitabilityRequest) o;
    return Objects.equals(this.chainId, profitabilityRequest.chainId) &&
        Objects.equals(this.quantity, profitabilityRequest.quantity) &&
        equalsNullable(this.inputPrices, profitabilityRequest.inputPrices) &&
        equalsNullable(this.sellingPrice, profitabilityRequest.sellingPrice);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(chainId, quantity, hashCodeNullable(inputPrices), hashCodeNullable(sellingPrice));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProfitabilityRequest {\n");
    sb.append("    chainId: ").append(toIndentedString(chainId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    inputPrices: ").append(toIndentedString(inputPrices)).append("\n");
    sb.append("    sellingPrice: ").append(toIndentedString(sellingPrice)).append("\n");
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

