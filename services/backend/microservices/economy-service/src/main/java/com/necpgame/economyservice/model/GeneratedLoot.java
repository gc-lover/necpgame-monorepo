package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.GeneratedLootItemsInner;
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
 * GeneratedLoot
 */


public class GeneratedLoot {

  @Valid
  private List<@Valid GeneratedLootItemsInner> items = new ArrayList<>();

  private @Nullable BigDecimal totalValue;

  private @Nullable Object rarityBreakdown;

  public GeneratedLoot items(List<@Valid GeneratedLootItemsInner> items) {
    this.items = items;
    return this;
  }

  public GeneratedLoot addItemsItem(GeneratedLootItemsInner itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
   */
  @Valid 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items")
  public List<@Valid GeneratedLootItemsInner> getItems() {
    return items;
  }

  public void setItems(List<@Valid GeneratedLootItemsInner> items) {
    this.items = items;
  }

  public GeneratedLoot totalValue(@Nullable BigDecimal totalValue) {
    this.totalValue = totalValue;
    return this;
  }

  /**
   * Get totalValue
   * @return totalValue
   */
  @Valid 
  @Schema(name = "total_value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_value")
  public @Nullable BigDecimal getTotalValue() {
    return totalValue;
  }

  public void setTotalValue(@Nullable BigDecimal totalValue) {
    this.totalValue = totalValue;
  }

  public GeneratedLoot rarityBreakdown(@Nullable Object rarityBreakdown) {
    this.rarityBreakdown = rarityBreakdown;
    return this;
  }

  /**
   * Количество предметов по редкости
   * @return rarityBreakdown
   */
  
  @Schema(name = "rarity_breakdown", description = "Количество предметов по редкости", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarity_breakdown")
  public @Nullable Object getRarityBreakdown() {
    return rarityBreakdown;
  }

  public void setRarityBreakdown(@Nullable Object rarityBreakdown) {
    this.rarityBreakdown = rarityBreakdown;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeneratedLoot generatedLoot = (GeneratedLoot) o;
    return Objects.equals(this.items, generatedLoot.items) &&
        Objects.equals(this.totalValue, generatedLoot.totalValue) &&
        Objects.equals(this.rarityBreakdown, generatedLoot.rarityBreakdown);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, totalValue, rarityBreakdown);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeneratedLoot {\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    totalValue: ").append(toIndentedString(totalValue)).append("\n");
    sb.append("    rarityBreakdown: ").append(toIndentedString(rarityBreakdown)).append("\n");
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

