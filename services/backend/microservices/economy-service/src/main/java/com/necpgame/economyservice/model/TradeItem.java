package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TradeItem
 */


public class TradeItem {

  private String itemId;

  private String name;

  private Integer buyPrice;

  private Integer sellPrice;

  private Integer quantity;

  private @Nullable String category;

  public TradeItem() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TradeItem(String itemId, String name, Integer buyPrice, Integer sellPrice, Integer quantity) {
    this.itemId = itemId;
    this.name = name;
    this.buyPrice = buyPrice;
    this.sellPrice = sellPrice;
    this.quantity = quantity;
  }

  public TradeItem itemId(String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @NotNull 
  @Schema(name = "itemId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("itemId")
  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public TradeItem name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TradeItem buyPrice(Integer buyPrice) {
    this.buyPrice = buyPrice;
    return this;
  }

  /**
   * Get buyPrice
   * @return buyPrice
   */
  @NotNull 
  @Schema(name = "buyPrice", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("buyPrice")
  public Integer getBuyPrice() {
    return buyPrice;
  }

  public void setBuyPrice(Integer buyPrice) {
    this.buyPrice = buyPrice;
  }

  public TradeItem sellPrice(Integer sellPrice) {
    this.sellPrice = sellPrice;
    return this;
  }

  /**
   * Get sellPrice
   * @return sellPrice
   */
  @NotNull 
  @Schema(name = "sellPrice", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("sellPrice")
  public Integer getSellPrice() {
    return sellPrice;
  }

  public void setSellPrice(Integer sellPrice) {
    this.sellPrice = sellPrice;
  }

  public TradeItem quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  @NotNull 
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public TradeItem category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TradeItem tradeItem = (TradeItem) o;
    return Objects.equals(this.itemId, tradeItem.itemId) &&
        Objects.equals(this.name, tradeItem.name) &&
        Objects.equals(this.buyPrice, tradeItem.buyPrice) &&
        Objects.equals(this.sellPrice, tradeItem.sellPrice) &&
        Objects.equals(this.quantity, tradeItem.quantity) &&
        Objects.equals(this.category, tradeItem.category);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, name, buyPrice, sellPrice, quantity, category);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TradeItem {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    buyPrice: ").append(toIndentedString(buyPrice)).append("\n");
    sb.append("    sellPrice: ").append(toIndentedString(sellPrice)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
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

