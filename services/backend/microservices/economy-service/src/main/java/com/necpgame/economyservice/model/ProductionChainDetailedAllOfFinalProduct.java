package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * ProductionChainDetailedAllOfFinalProduct
 */

@JsonTypeName("ProductionChainDetailed_allOf_final_product")

public class ProductionChainDetailedAllOfFinalProduct {

  private @Nullable UUID itemId;

  private @Nullable String name;

  private @Nullable Integer marketPrice;

  public ProductionChainDetailedAllOfFinalProduct itemId(@Nullable UUID itemId) {
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

  public ProductionChainDetailedAllOfFinalProduct name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public ProductionChainDetailedAllOfFinalProduct marketPrice(@Nullable Integer marketPrice) {
    this.marketPrice = marketPrice;
    return this;
  }

  /**
   * Get marketPrice
   * @return marketPrice
   */
  
  @Schema(name = "market_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("market_price")
  public @Nullable Integer getMarketPrice() {
    return marketPrice;
  }

  public void setMarketPrice(@Nullable Integer marketPrice) {
    this.marketPrice = marketPrice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductionChainDetailedAllOfFinalProduct productionChainDetailedAllOfFinalProduct = (ProductionChainDetailedAllOfFinalProduct) o;
    return Objects.equals(this.itemId, productionChainDetailedAllOfFinalProduct.itemId) &&
        Objects.equals(this.name, productionChainDetailedAllOfFinalProduct.name) &&
        Objects.equals(this.marketPrice, productionChainDetailedAllOfFinalProduct.marketPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, name, marketPrice);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductionChainDetailedAllOfFinalProduct {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    marketPrice: ").append(toIndentedString(marketPrice)).append("\n");
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

