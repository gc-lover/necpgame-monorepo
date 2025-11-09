package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * RotationItem
 */


public class RotationItem {

  private @Nullable String itemId;

  private @Nullable Integer displayOrder;

  private @Nullable BigDecimal discountPercent;

  private @Nullable Integer limitedQuantity;

  private @Nullable Integer remainingQuantity;

  public RotationItem itemId(@Nullable String itemId) {
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

  public RotationItem displayOrder(@Nullable Integer displayOrder) {
    this.displayOrder = displayOrder;
    return this;
  }

  /**
   * Get displayOrder
   * @return displayOrder
   */
  
  @Schema(name = "displayOrder", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("displayOrder")
  public @Nullable Integer getDisplayOrder() {
    return displayOrder;
  }

  public void setDisplayOrder(@Nullable Integer displayOrder) {
    this.displayOrder = displayOrder;
  }

  public RotationItem discountPercent(@Nullable BigDecimal discountPercent) {
    this.discountPercent = discountPercent;
    return this;
  }

  /**
   * Get discountPercent
   * @return discountPercent
   */
  @Valid 
  @Schema(name = "discountPercent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("discountPercent")
  public @Nullable BigDecimal getDiscountPercent() {
    return discountPercent;
  }

  public void setDiscountPercent(@Nullable BigDecimal discountPercent) {
    this.discountPercent = discountPercent;
  }

  public RotationItem limitedQuantity(@Nullable Integer limitedQuantity) {
    this.limitedQuantity = limitedQuantity;
    return this;
  }

  /**
   * Get limitedQuantity
   * @return limitedQuantity
   */
  
  @Schema(name = "limitedQuantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("limitedQuantity")
  public @Nullable Integer getLimitedQuantity() {
    return limitedQuantity;
  }

  public void setLimitedQuantity(@Nullable Integer limitedQuantity) {
    this.limitedQuantity = limitedQuantity;
  }

  public RotationItem remainingQuantity(@Nullable Integer remainingQuantity) {
    this.remainingQuantity = remainingQuantity;
    return this;
  }

  /**
   * Get remainingQuantity
   * @return remainingQuantity
   */
  
  @Schema(name = "remainingQuantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("remainingQuantity")
  public @Nullable Integer getRemainingQuantity() {
    return remainingQuantity;
  }

  public void setRemainingQuantity(@Nullable Integer remainingQuantity) {
    this.remainingQuantity = remainingQuantity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RotationItem rotationItem = (RotationItem) o;
    return Objects.equals(this.itemId, rotationItem.itemId) &&
        Objects.equals(this.displayOrder, rotationItem.displayOrder) &&
        Objects.equals(this.discountPercent, rotationItem.discountPercent) &&
        Objects.equals(this.limitedQuantity, rotationItem.limitedQuantity) &&
        Objects.equals(this.remainingQuantity, rotationItem.remainingQuantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, displayOrder, discountPercent, limitedQuantity, remainingQuantity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RotationItem {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    displayOrder: ").append(toIndentedString(displayOrder)).append("\n");
    sb.append("    discountPercent: ").append(toIndentedString(discountPercent)).append("\n");
    sb.append("    limitedQuantity: ").append(toIndentedString(limitedQuantity)).append("\n");
    sb.append("    remainingQuantity: ").append(toIndentedString(remainingQuantity)).append("\n");
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

