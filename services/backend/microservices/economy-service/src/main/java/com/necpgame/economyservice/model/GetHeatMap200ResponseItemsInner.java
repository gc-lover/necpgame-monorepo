package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetHeatMap200ResponseItemsInner
 */

@JsonTypeName("getHeatMap_200_response_items_inner")

public class GetHeatMap200ResponseItemsInner {

  private @Nullable String itemId;

  private @Nullable String itemName;

  private @Nullable BigDecimal priceChangePercent;

  private @Nullable BigDecimal volumeChangePercent;

  public GetHeatMap200ResponseItemsInner itemId(@Nullable String itemId) {
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

  public GetHeatMap200ResponseItemsInner itemName(@Nullable String itemName) {
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

  public GetHeatMap200ResponseItemsInner priceChangePercent(@Nullable BigDecimal priceChangePercent) {
    this.priceChangePercent = priceChangePercent;
    return this;
  }

  /**
   * Get priceChangePercent
   * @return priceChangePercent
   */
  @Valid 
  @Schema(name = "price_change_percent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price_change_percent")
  public @Nullable BigDecimal getPriceChangePercent() {
    return priceChangePercent;
  }

  public void setPriceChangePercent(@Nullable BigDecimal priceChangePercent) {
    this.priceChangePercent = priceChangePercent;
  }

  public GetHeatMap200ResponseItemsInner volumeChangePercent(@Nullable BigDecimal volumeChangePercent) {
    this.volumeChangePercent = volumeChangePercent;
    return this;
  }

  /**
   * Get volumeChangePercent
   * @return volumeChangePercent
   */
  @Valid 
  @Schema(name = "volume_change_percent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("volume_change_percent")
  public @Nullable BigDecimal getVolumeChangePercent() {
    return volumeChangePercent;
  }

  public void setVolumeChangePercent(@Nullable BigDecimal volumeChangePercent) {
    this.volumeChangePercent = volumeChangePercent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetHeatMap200ResponseItemsInner getHeatMap200ResponseItemsInner = (GetHeatMap200ResponseItemsInner) o;
    return Objects.equals(this.itemId, getHeatMap200ResponseItemsInner.itemId) &&
        Objects.equals(this.itemName, getHeatMap200ResponseItemsInner.itemName) &&
        Objects.equals(this.priceChangePercent, getHeatMap200ResponseItemsInner.priceChangePercent) &&
        Objects.equals(this.volumeChangePercent, getHeatMap200ResponseItemsInner.volumeChangePercent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, itemName, priceChangePercent, volumeChangePercent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetHeatMap200ResponseItemsInner {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    itemName: ").append(toIndentedString(itemName)).append("\n");
    sb.append("    priceChangePercent: ").append(toIndentedString(priceChangePercent)).append("\n");
    sb.append("    volumeChangePercent: ").append(toIndentedString(volumeChangePercent)).append("\n");
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

