package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetItemPrice200Response
 */

@JsonTypeName("getItemPrice_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T21:22:08.934689200+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class GetItemPrice200Response {

  private @Nullable String itemId;

  private @Nullable Integer buyPrice;

  private @Nullable Integer sellPrice;

  public GetItemPrice200Response itemId(@Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  
  @Schema(name = "itemId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("itemId")
  public @Nullable String getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable String itemId) {
    this.itemId = itemId;
  }

  public GetItemPrice200Response buyPrice(@Nullable Integer buyPrice) {
    this.buyPrice = buyPrice;
    return this;
  }

  /**
   * Get buyPrice
   * @return buyPrice
   */
  
  @Schema(name = "buyPrice", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("buyPrice")
  public @Nullable Integer getBuyPrice() {
    return buyPrice;
  }

  public void setBuyPrice(@Nullable Integer buyPrice) {
    this.buyPrice = buyPrice;
  }

  public GetItemPrice200Response sellPrice(@Nullable Integer sellPrice) {
    this.sellPrice = sellPrice;
    return this;
  }

  /**
   * Get sellPrice
   * @return sellPrice
   */
  
  @Schema(name = "sellPrice", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sellPrice")
  public @Nullable Integer getSellPrice() {
    return sellPrice;
  }

  public void setSellPrice(@Nullable Integer sellPrice) {
    this.sellPrice = sellPrice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetItemPrice200Response getItemPrice200Response = (GetItemPrice200Response) o;
    return Objects.equals(this.itemId, getItemPrice200Response.itemId) &&
        Objects.equals(this.buyPrice, getItemPrice200Response.buyPrice) &&
        Objects.equals(this.sellPrice, getItemPrice200Response.sellPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, buyPrice, sellPrice);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetItemPrice200Response {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    buyPrice: ").append(toIndentedString(buyPrice)).append("\n");
    sb.append("    sellPrice: ").append(toIndentedString(sellPrice)).append("\n");
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

