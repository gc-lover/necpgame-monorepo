package com.necpgame.backjava.model;

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
 * FurniturePurchaseRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class FurniturePurchaseRequest {

  private String itemId;

  private @Nullable Integer quantity;

  private @Nullable String paymentMethod;

  public FurniturePurchaseRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FurniturePurchaseRequest(String itemId) {
    this.itemId = itemId;
  }

  public FurniturePurchaseRequest itemId(String itemId) {
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

  public FurniturePurchaseRequest quantity(@Nullable Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * minimum: 1
   * @return quantity
   */
  @Min(value = 1) 
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public @Nullable Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(@Nullable Integer quantity) {
    this.quantity = quantity;
  }

  public FurniturePurchaseRequest paymentMethod(@Nullable String paymentMethod) {
    this.paymentMethod = paymentMethod;
    return this;
  }

  /**
   * Get paymentMethod
   * @return paymentMethod
   */
  
  @Schema(name = "paymentMethod", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("paymentMethod")
  public @Nullable String getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(@Nullable String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FurniturePurchaseRequest furniturePurchaseRequest = (FurniturePurchaseRequest) o;
    return Objects.equals(this.itemId, furniturePurchaseRequest.itemId) &&
        Objects.equals(this.quantity, furniturePurchaseRequest.quantity) &&
        Objects.equals(this.paymentMethod, furniturePurchaseRequest.paymentMethod);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, quantity, paymentMethod);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FurniturePurchaseRequest {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    paymentMethod: ").append(toIndentedString(paymentMethod)).append("\n");
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

