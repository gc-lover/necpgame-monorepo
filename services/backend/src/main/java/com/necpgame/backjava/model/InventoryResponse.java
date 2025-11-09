package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.necpgame.backjava.model.InventoryItem;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:50:45.778329200+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
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
   * РџСЂРµРґРјРµС‚С‹ РІ РёРЅРІРµРЅС‚Р°СЂРµ
   * @return items
   */
  @NotNull @Valid 
  @Schema(name = "items", description = "РџСЂРµРґРјРµС‚С‹ РІ РёРЅРІРµРЅС‚Р°СЂРµ", requiredMode = Schema.RequiredMode.REQUIRED)
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
   * РўРµРєСѓС‰РёР№ РІРµСЃ РёРЅРІРµРЅС‚Р°СЂСЏ (РєРі)
   * @return currentWeight
   */
  @NotNull 
  @Schema(name = "currentWeight", example = "45.5", description = "РўРµРєСѓС‰РёР№ РІРµСЃ РёРЅРІРµРЅС‚Р°СЂСЏ (РєРі)", requiredMode = Schema.RequiredMode.REQUIRED)
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
   * РњР°РєСЃРёРјР°Р»СЊРЅС‹Р№ РІРµСЃ РёРЅРІРµРЅС‚Р°СЂСЏ (РєРі)
   * @return maxWeight
   */
  @NotNull 
  @Schema(name = "maxWeight", example = "100.0", description = "РњР°РєСЃРёРјР°Р»СЊРЅС‹Р№ РІРµСЃ РёРЅРІРµРЅС‚Р°СЂСЏ (РєРі)", requiredMode = Schema.RequiredMode.REQUIRED)
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
   * РљРѕР»РёС‡РµСЃС‚РІРѕ РїСЂРµРґРјРµС‚РѕРІ РїРѕ РєР°С‚РµРіРѕСЂРёСЏРј
   * @return categories
   */
  
  @Schema(name = "categories", example = "{\"weapons\":3,\"armor\":5,\"consumables\":10}", description = "РљРѕР»РёС‡РµСЃС‚РІРѕ РїСЂРµРґРјРµС‚РѕРІ РїРѕ РєР°С‚РµРіРѕСЂРёСЏРј", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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


