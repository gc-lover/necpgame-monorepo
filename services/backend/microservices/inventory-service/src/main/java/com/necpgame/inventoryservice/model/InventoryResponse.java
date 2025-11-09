package com.necpgame.inventoryservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.inventoryservice.model.InventoryItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * InventoryResponse
 */


public class InventoryResponse {

  @Valid
  private List<@Valid InventoryItem> items = new ArrayList<>();

  private Float currentWeight;

  private Float maxWeight;

  @Valid
  private Map<String, Integer> categories = new HashMap<>();

  public InventoryResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InventoryResponse(List<@Valid InventoryItem> items, Float currentWeight, Float maxWeight) {
    this.items = items;
    this.currentWeight = currentWeight;
    this.maxWeight = maxWeight;
  }

  public InventoryResponse items(List<@Valid InventoryItem> items) {
    this.items = items;
    return this;
  }

  public InventoryResponse addItemsItem(InventoryItem itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Предметы в инвентаре
   * @return items
   */
  @NotNull @Valid 
  @Schema(name = "items", description = "Предметы в инвентаре", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("items")
  public List<@Valid InventoryItem> getItems() {
    return items;
  }

  public void setItems(List<@Valid InventoryItem> items) {
    this.items = items;
  }

  public InventoryResponse currentWeight(Float currentWeight) {
    this.currentWeight = currentWeight;
    return this;
  }

  /**
   * Текущий вес инвентаря (кг)
   * @return currentWeight
   */
  @NotNull 
  @Schema(name = "currentWeight", example = "45.5", description = "Текущий вес инвентаря (кг)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("currentWeight")
  public Float getCurrentWeight() {
    return currentWeight;
  }

  public void setCurrentWeight(Float currentWeight) {
    this.currentWeight = currentWeight;
  }

  public InventoryResponse maxWeight(Float maxWeight) {
    this.maxWeight = maxWeight;
    return this;
  }

  /**
   * Максимальный вес инвентаря (кг)
   * @return maxWeight
   */
  @NotNull 
  @Schema(name = "maxWeight", example = "100.0", description = "Максимальный вес инвентаря (кг)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maxWeight")
  public Float getMaxWeight() {
    return maxWeight;
  }

  public void setMaxWeight(Float maxWeight) {
    this.maxWeight = maxWeight;
  }

  public InventoryResponse categories(Map<String, Integer> categories) {
    this.categories = categories;
    return this;
  }

  public InventoryResponse putCategoriesItem(String key, Integer categoriesItem) {
    if (this.categories == null) {
      this.categories = new HashMap<>();
    }
    this.categories.put(key, categoriesItem);
    return this;
  }

  /**
   * Количество предметов по категориям
   * @return categories
   */
  
  @Schema(name = "categories", example = "{\"weapons\":3,\"armor\":5,\"consumables\":10}", description = "Количество предметов по категориям", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("categories")
  public Map<String, Integer> getCategories() {
    return categories;
  }

  public void setCategories(Map<String, Integer> categories) {
    this.categories = categories;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InventoryResponse inventoryResponse = (InventoryResponse) o;
    return Objects.equals(this.items, inventoryResponse.items) &&
        Objects.equals(this.currentWeight, inventoryResponse.currentWeight) &&
        Objects.equals(this.maxWeight, inventoryResponse.maxWeight) &&
        Objects.equals(this.categories, inventoryResponse.categories);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, currentWeight, maxWeight, categories);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InventoryResponse {\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    currentWeight: ").append(toIndentedString(currentWeight)).append("\n");
    sb.append("    maxWeight: ").append(toIndentedString(maxWeight)).append("\n");
    sb.append("    categories: ").append(toIndentedString(categories)).append("\n");
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

