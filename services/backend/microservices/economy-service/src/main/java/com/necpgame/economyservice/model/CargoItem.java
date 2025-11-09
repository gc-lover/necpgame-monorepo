package com.necpgame.economyservice.model;

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
 * CargoItem
 */


public class CargoItem {

  private @Nullable UUID itemId;

  private @Nullable Integer quantity;

  private @Nullable Float weight;

  private @Nullable Float volume;

  private @Nullable Integer value;

  private @Nullable Boolean fragile;

  public CargoItem itemId(@Nullable UUID itemId) {
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

  public CargoItem quantity(@Nullable Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public @Nullable Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(@Nullable Integer quantity) {
    this.quantity = quantity;
  }

  public CargoItem weight(@Nullable Float weight) {
    this.weight = weight;
    return this;
  }

  /**
   * Вес в кг
   * @return weight
   */
  
  @Schema(name = "weight", description = "Вес в кг", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weight")
  public @Nullable Float getWeight() {
    return weight;
  }

  public void setWeight(@Nullable Float weight) {
    this.weight = weight;
  }

  public CargoItem volume(@Nullable Float volume) {
    this.volume = volume;
    return this;
  }

  /**
   * Объем в м³
   * @return volume
   */
  
  @Schema(name = "volume", description = "Объем в м³", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("volume")
  public @Nullable Float getVolume() {
    return volume;
  }

  public void setVolume(@Nullable Float volume) {
    this.volume = volume;
  }

  public CargoItem value(@Nullable Integer value) {
    this.value = value;
    return this;
  }

  /**
   * Стоимость для страховки
   * @return value
   */
  
  @Schema(name = "value", description = "Стоимость для страховки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("value")
  public @Nullable Integer getValue() {
    return value;
  }

  public void setValue(@Nullable Integer value) {
    this.value = value;
  }

  public CargoItem fragile(@Nullable Boolean fragile) {
    this.fragile = fragile;
    return this;
  }

  /**
   * Get fragile
   * @return fragile
   */
  
  @Schema(name = "fragile", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fragile")
  public @Nullable Boolean getFragile() {
    return fragile;
  }

  public void setFragile(@Nullable Boolean fragile) {
    this.fragile = fragile;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CargoItem cargoItem = (CargoItem) o;
    return Objects.equals(this.itemId, cargoItem.itemId) &&
        Objects.equals(this.quantity, cargoItem.quantity) &&
        Objects.equals(this.weight, cargoItem.weight) &&
        Objects.equals(this.volume, cargoItem.volume) &&
        Objects.equals(this.value, cargoItem.value) &&
        Objects.equals(this.fragile, cargoItem.fragile);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, quantity, weight, volume, value, fragile);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CargoItem {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
    sb.append("    volume: ").append(toIndentedString(volume)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    fragile: ").append(toIndentedString(fragile)).append("\n");
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

