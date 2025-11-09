package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.PriceCalculationResultModifiersAppliedInner;
import com.necpgame.economyservice.model.PriceCalculationResultPriceBreakdown;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * PriceCalculationResult
 */


public class PriceCalculationResult {

  private @Nullable UUID itemId;

  private @Nullable Integer basePrice;

  private @Nullable Integer calculatedPrice;

  private @Nullable PriceCalculationResultPriceBreakdown priceBreakdown;

  private @Nullable Integer totalForQuantity;

  @Valid
  private List<@Valid PriceCalculationResultModifiersAppliedInner> modifiersApplied = new ArrayList<>();

  public PriceCalculationResult itemId(@Nullable UUID itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @Valid 
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_id")
  public @Nullable UUID getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable UUID itemId) {
    this.itemId = itemId;
  }

  public PriceCalculationResult basePrice(@Nullable Integer basePrice) {
    this.basePrice = basePrice;
    return this;
  }

  /**
   * Get basePrice
   * @return basePrice
   */
  
  @Schema(name = "base_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base_price")
  public @Nullable Integer getBasePrice() {
    return basePrice;
  }

  public void setBasePrice(@Nullable Integer basePrice) {
    this.basePrice = basePrice;
  }

  public PriceCalculationResult calculatedPrice(@Nullable Integer calculatedPrice) {
    this.calculatedPrice = calculatedPrice;
    return this;
  }

  /**
   * Get calculatedPrice
   * @return calculatedPrice
   */
  
  @Schema(name = "calculated_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("calculated_price")
  public @Nullable Integer getCalculatedPrice() {
    return calculatedPrice;
  }

  public void setCalculatedPrice(@Nullable Integer calculatedPrice) {
    this.calculatedPrice = calculatedPrice;
  }

  public PriceCalculationResult priceBreakdown(@Nullable PriceCalculationResultPriceBreakdown priceBreakdown) {
    this.priceBreakdown = priceBreakdown;
    return this;
  }

  /**
   * Get priceBreakdown
   * @return priceBreakdown
   */
  @Valid 
  @Schema(name = "price_breakdown", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price_breakdown")
  public @Nullable PriceCalculationResultPriceBreakdown getPriceBreakdown() {
    return priceBreakdown;
  }

  public void setPriceBreakdown(@Nullable PriceCalculationResultPriceBreakdown priceBreakdown) {
    this.priceBreakdown = priceBreakdown;
  }

  public PriceCalculationResult totalForQuantity(@Nullable Integer totalForQuantity) {
    this.totalForQuantity = totalForQuantity;
    return this;
  }

  /**
   * Get totalForQuantity
   * @return totalForQuantity
   */
  
  @Schema(name = "total_for_quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_for_quantity")
  public @Nullable Integer getTotalForQuantity() {
    return totalForQuantity;
  }

  public void setTotalForQuantity(@Nullable Integer totalForQuantity) {
    this.totalForQuantity = totalForQuantity;
  }

  public PriceCalculationResult modifiersApplied(List<@Valid PriceCalculationResultModifiersAppliedInner> modifiersApplied) {
    this.modifiersApplied = modifiersApplied;
    return this;
  }

  public PriceCalculationResult addModifiersAppliedItem(PriceCalculationResultModifiersAppliedInner modifiersAppliedItem) {
    if (this.modifiersApplied == null) {
      this.modifiersApplied = new ArrayList<>();
    }
    this.modifiersApplied.add(modifiersAppliedItem);
    return this;
  }

  /**
   * Get modifiersApplied
   * @return modifiersApplied
   */
  @Valid 
  @Schema(name = "modifiers_applied", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifiers_applied")
  public List<@Valid PriceCalculationResultModifiersAppliedInner> getModifiersApplied() {
    return modifiersApplied;
  }

  public void setModifiersApplied(List<@Valid PriceCalculationResultModifiersAppliedInner> modifiersApplied) {
    this.modifiersApplied = modifiersApplied;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PriceCalculationResult priceCalculationResult = (PriceCalculationResult) o;
    return Objects.equals(this.itemId, priceCalculationResult.itemId) &&
        Objects.equals(this.basePrice, priceCalculationResult.basePrice) &&
        Objects.equals(this.calculatedPrice, priceCalculationResult.calculatedPrice) &&
        Objects.equals(this.priceBreakdown, priceCalculationResult.priceBreakdown) &&
        Objects.equals(this.totalForQuantity, priceCalculationResult.totalForQuantity) &&
        Objects.equals(this.modifiersApplied, priceCalculationResult.modifiersApplied);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, basePrice, calculatedPrice, priceBreakdown, totalForQuantity, modifiersApplied);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PriceCalculationResult {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    basePrice: ").append(toIndentedString(basePrice)).append("\n");
    sb.append("    calculatedPrice: ").append(toIndentedString(calculatedPrice)).append("\n");
    sb.append("    priceBreakdown: ").append(toIndentedString(priceBreakdown)).append("\n");
    sb.append("    totalForQuantity: ").append(toIndentedString(totalForQuantity)).append("\n");
    sb.append("    modifiersApplied: ").append(toIndentedString(modifiersApplied)).append("\n");
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

