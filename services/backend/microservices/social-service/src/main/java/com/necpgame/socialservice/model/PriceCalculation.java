package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.PriceCalculationModifiersInner;
import java.math.BigDecimal;
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
 * PriceCalculation
 */


public class PriceCalculation {

  private @Nullable String itemId;

  private @Nullable BigDecimal basePrice;

  @Valid
  private List<@Valid PriceCalculationModifiersInner> modifiers = new ArrayList<>();

  private @Nullable BigDecimal finalPrice;

  public PriceCalculation itemId(@Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_id")
  public @Nullable String getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable String itemId) {
    this.itemId = itemId;
  }

  public PriceCalculation basePrice(@Nullable BigDecimal basePrice) {
    this.basePrice = basePrice;
    return this;
  }

  /**
   * Get basePrice
   * @return basePrice
   */
  @Valid 
  @Schema(name = "base_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base_price")
  public @Nullable BigDecimal getBasePrice() {
    return basePrice;
  }

  public void setBasePrice(@Nullable BigDecimal basePrice) {
    this.basePrice = basePrice;
  }

  public PriceCalculation modifiers(List<@Valid PriceCalculationModifiersInner> modifiers) {
    this.modifiers = modifiers;
    return this;
  }

  public PriceCalculation addModifiersItem(PriceCalculationModifiersInner modifiersItem) {
    if (this.modifiers == null) {
      this.modifiers = new ArrayList<>();
    }
    this.modifiers.add(modifiersItem);
    return this;
  }

  /**
   * Get modifiers
   * @return modifiers
   */
  @Valid 
  @Schema(name = "modifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifiers")
  public List<@Valid PriceCalculationModifiersInner> getModifiers() {
    return modifiers;
  }

  public void setModifiers(List<@Valid PriceCalculationModifiersInner> modifiers) {
    this.modifiers = modifiers;
  }

  public PriceCalculation finalPrice(@Nullable BigDecimal finalPrice) {
    this.finalPrice = finalPrice;
    return this;
  }

  /**
   * Get finalPrice
   * @return finalPrice
   */
  @Valid 
  @Schema(name = "final_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("final_price")
  public @Nullable BigDecimal getFinalPrice() {
    return finalPrice;
  }

  public void setFinalPrice(@Nullable BigDecimal finalPrice) {
    this.finalPrice = finalPrice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PriceCalculation priceCalculation = (PriceCalculation) o;
    return Objects.equals(this.itemId, priceCalculation.itemId) &&
        Objects.equals(this.basePrice, priceCalculation.basePrice) &&
        Objects.equals(this.modifiers, priceCalculation.modifiers) &&
        Objects.equals(this.finalPrice, priceCalculation.finalPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, basePrice, modifiers, finalPrice);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PriceCalculation {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    basePrice: ").append(toIndentedString(basePrice)).append("\n");
    sb.append("    modifiers: ").append(toIndentedString(modifiers)).append("\n");
    sb.append("    finalPrice: ").append(toIndentedString(finalPrice)).append("\n");
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

