package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.ItemPriceEventMultipliersInner;
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
 * ItemPrice
 */


public class ItemPrice {

  private @Nullable UUID itemId;

  private @Nullable String itemName;

  private @Nullable Integer basePrice;

  private @Nullable Integer currentPrice;

  private @Nullable Integer vendorSellPrice;

  private @Nullable Integer vendorBuyPrice;

  private @Nullable Float qualityMultiplier;

  private @Nullable Float rarityMultiplier;

  private @Nullable Float regionalMultiplier;

  private @Nullable Float factionMultiplier;

  @Valid
  private List<@Valid ItemPriceEventMultipliersInner> eventMultipliers = new ArrayList<>();

  private @Nullable Float finalMultiplier;

  public ItemPrice itemId(@Nullable UUID itemId) {
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

  public ItemPrice itemName(@Nullable String itemName) {
    this.itemName = itemName;
    return this;
  }

  /**
   * Get itemName
   * @return itemName
   */
  
  @Schema(name = "item_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_name")
  public @Nullable String getItemName() {
    return itemName;
  }

  public void setItemName(@Nullable String itemName) {
    this.itemName = itemName;
  }

  public ItemPrice basePrice(@Nullable Integer basePrice) {
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

  public ItemPrice currentPrice(@Nullable Integer currentPrice) {
    this.currentPrice = currentPrice;
    return this;
  }

  /**
   * Get currentPrice
   * @return currentPrice
   */
  
  @Schema(name = "current_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_price")
  public @Nullable Integer getCurrentPrice() {
    return currentPrice;
  }

  public void setCurrentPrice(@Nullable Integer currentPrice) {
    this.currentPrice = currentPrice;
  }

  public ItemPrice vendorSellPrice(@Nullable Integer vendorSellPrice) {
    this.vendorSellPrice = vendorSellPrice;
    return this;
  }

  /**
   * Цена продажи vendor
   * @return vendorSellPrice
   */
  
  @Schema(name = "vendor_sell_price", description = "Цена продажи vendor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("vendor_sell_price")
  public @Nullable Integer getVendorSellPrice() {
    return vendorSellPrice;
  }

  public void setVendorSellPrice(@Nullable Integer vendorSellPrice) {
    this.vendorSellPrice = vendorSellPrice;
  }

  public ItemPrice vendorBuyPrice(@Nullable Integer vendorBuyPrice) {
    this.vendorBuyPrice = vendorBuyPrice;
    return this;
  }

  /**
   * Цена покупки у vendor
   * @return vendorBuyPrice
   */
  
  @Schema(name = "vendor_buy_price", description = "Цена покупки у vendor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("vendor_buy_price")
  public @Nullable Integer getVendorBuyPrice() {
    return vendorBuyPrice;
  }

  public void setVendorBuyPrice(@Nullable Integer vendorBuyPrice) {
    this.vendorBuyPrice = vendorBuyPrice;
  }

  public ItemPrice qualityMultiplier(@Nullable Float qualityMultiplier) {
    this.qualityMultiplier = qualityMultiplier;
    return this;
  }

  /**
   * Get qualityMultiplier
   * @return qualityMultiplier
   */
  
  @Schema(name = "quality_multiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quality_multiplier")
  public @Nullable Float getQualityMultiplier() {
    return qualityMultiplier;
  }

  public void setQualityMultiplier(@Nullable Float qualityMultiplier) {
    this.qualityMultiplier = qualityMultiplier;
  }

  public ItemPrice rarityMultiplier(@Nullable Float rarityMultiplier) {
    this.rarityMultiplier = rarityMultiplier;
    return this;
  }

  /**
   * Get rarityMultiplier
   * @return rarityMultiplier
   */
  
  @Schema(name = "rarity_multiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarity_multiplier")
  public @Nullable Float getRarityMultiplier() {
    return rarityMultiplier;
  }

  public void setRarityMultiplier(@Nullable Float rarityMultiplier) {
    this.rarityMultiplier = rarityMultiplier;
  }

  public ItemPrice regionalMultiplier(@Nullable Float regionalMultiplier) {
    this.regionalMultiplier = regionalMultiplier;
    return this;
  }

  /**
   * Get regionalMultiplier
   * @return regionalMultiplier
   */
  
  @Schema(name = "regional_multiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("regional_multiplier")
  public @Nullable Float getRegionalMultiplier() {
    return regionalMultiplier;
  }

  public void setRegionalMultiplier(@Nullable Float regionalMultiplier) {
    this.regionalMultiplier = regionalMultiplier;
  }

  public ItemPrice factionMultiplier(@Nullable Float factionMultiplier) {
    this.factionMultiplier = factionMultiplier;
    return this;
  }

  /**
   * Get factionMultiplier
   * @return factionMultiplier
   */
  
  @Schema(name = "faction_multiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_multiplier")
  public @Nullable Float getFactionMultiplier() {
    return factionMultiplier;
  }

  public void setFactionMultiplier(@Nullable Float factionMultiplier) {
    this.factionMultiplier = factionMultiplier;
  }

  public ItemPrice eventMultipliers(List<@Valid ItemPriceEventMultipliersInner> eventMultipliers) {
    this.eventMultipliers = eventMultipliers;
    return this;
  }

  public ItemPrice addEventMultipliersItem(ItemPriceEventMultipliersInner eventMultipliersItem) {
    if (this.eventMultipliers == null) {
      this.eventMultipliers = new ArrayList<>();
    }
    this.eventMultipliers.add(eventMultipliersItem);
    return this;
  }

  /**
   * Get eventMultipliers
   * @return eventMultipliers
   */
  @Valid 
  @Schema(name = "event_multipliers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_multipliers")
  public List<@Valid ItemPriceEventMultipliersInner> getEventMultipliers() {
    return eventMultipliers;
  }

  public void setEventMultipliers(List<@Valid ItemPriceEventMultipliersInner> eventMultipliers) {
    this.eventMultipliers = eventMultipliers;
  }

  public ItemPrice finalMultiplier(@Nullable Float finalMultiplier) {
    this.finalMultiplier = finalMultiplier;
    return this;
  }

  /**
   * Get finalMultiplier
   * @return finalMultiplier
   */
  
  @Schema(name = "final_multiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("final_multiplier")
  public @Nullable Float getFinalMultiplier() {
    return finalMultiplier;
  }

  public void setFinalMultiplier(@Nullable Float finalMultiplier) {
    this.finalMultiplier = finalMultiplier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemPrice itemPrice = (ItemPrice) o;
    return Objects.equals(this.itemId, itemPrice.itemId) &&
        Objects.equals(this.itemName, itemPrice.itemName) &&
        Objects.equals(this.basePrice, itemPrice.basePrice) &&
        Objects.equals(this.currentPrice, itemPrice.currentPrice) &&
        Objects.equals(this.vendorSellPrice, itemPrice.vendorSellPrice) &&
        Objects.equals(this.vendorBuyPrice, itemPrice.vendorBuyPrice) &&
        Objects.equals(this.qualityMultiplier, itemPrice.qualityMultiplier) &&
        Objects.equals(this.rarityMultiplier, itemPrice.rarityMultiplier) &&
        Objects.equals(this.regionalMultiplier, itemPrice.regionalMultiplier) &&
        Objects.equals(this.factionMultiplier, itemPrice.factionMultiplier) &&
        Objects.equals(this.eventMultipliers, itemPrice.eventMultipliers) &&
        Objects.equals(this.finalMultiplier, itemPrice.finalMultiplier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, itemName, basePrice, currentPrice, vendorSellPrice, vendorBuyPrice, qualityMultiplier, rarityMultiplier, regionalMultiplier, factionMultiplier, eventMultipliers, finalMultiplier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemPrice {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    itemName: ").append(toIndentedString(itemName)).append("\n");
    sb.append("    basePrice: ").append(toIndentedString(basePrice)).append("\n");
    sb.append("    currentPrice: ").append(toIndentedString(currentPrice)).append("\n");
    sb.append("    vendorSellPrice: ").append(toIndentedString(vendorSellPrice)).append("\n");
    sb.append("    vendorBuyPrice: ").append(toIndentedString(vendorBuyPrice)).append("\n");
    sb.append("    qualityMultiplier: ").append(toIndentedString(qualityMultiplier)).append("\n");
    sb.append("    rarityMultiplier: ").append(toIndentedString(rarityMultiplier)).append("\n");
    sb.append("    regionalMultiplier: ").append(toIndentedString(regionalMultiplier)).append("\n");
    sb.append("    factionMultiplier: ").append(toIndentedString(factionMultiplier)).append("\n");
    sb.append("    eventMultipliers: ").append(toIndentedString(eventMultipliers)).append("\n");
    sb.append("    finalMultiplier: ").append(toIndentedString(finalMultiplier)).append("\n");
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

