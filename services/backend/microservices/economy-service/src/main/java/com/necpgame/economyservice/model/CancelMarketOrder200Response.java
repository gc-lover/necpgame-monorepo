package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * CancelMarketOrder200Response
 */

@JsonTypeName("cancelMarketOrder_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CancelMarketOrder200Response {

  private @Nullable Boolean success;

  private @Nullable String orderId;

  private @Nullable BigDecimal refundAmount;

  @Valid
  private List<Object> refundItems = new ArrayList<>();

  public CancelMarketOrder200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public CancelMarketOrder200Response orderId(@Nullable String orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Get orderId
   * @return orderId
   */
  
  @Schema(name = "order_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("order_id")
  public @Nullable String getOrderId() {
    return orderId;
  }

  public void setOrderId(@Nullable String orderId) {
    this.orderId = orderId;
  }

  public CancelMarketOrder200Response refundAmount(@Nullable BigDecimal refundAmount) {
    this.refundAmount = refundAmount;
    return this;
  }

  /**
   * Get refundAmount
   * @return refundAmount
   */
  @Valid 
  @Schema(name = "refund_amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("refund_amount")
  public @Nullable BigDecimal getRefundAmount() {
    return refundAmount;
  }

  public void setRefundAmount(@Nullable BigDecimal refundAmount) {
    this.refundAmount = refundAmount;
  }

  public CancelMarketOrder200Response refundItems(List<Object> refundItems) {
    this.refundItems = refundItems;
    return this;
  }

  public CancelMarketOrder200Response addRefundItemsItem(Object refundItemsItem) {
    if (this.refundItems == null) {
      this.refundItems = new ArrayList<>();
    }
    this.refundItems.add(refundItemsItem);
    return this;
  }

  /**
   * Get refundItems
   * @return refundItems
   */
  
  @Schema(name = "refund_items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("refund_items")
  public List<Object> getRefundItems() {
    return refundItems;
  }

  public void setRefundItems(List<Object> refundItems) {
    this.refundItems = refundItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CancelMarketOrder200Response cancelMarketOrder200Response = (CancelMarketOrder200Response) o;
    return Objects.equals(this.success, cancelMarketOrder200Response.success) &&
        Objects.equals(this.orderId, cancelMarketOrder200Response.orderId) &&
        Objects.equals(this.refundAmount, cancelMarketOrder200Response.refundAmount) &&
        Objects.equals(this.refundItems, cancelMarketOrder200Response.refundItems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, orderId, refundAmount, refundItems);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CancelMarketOrder200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    refundAmount: ").append(toIndentedString(refundAmount)).append("\n");
    sb.append("    refundItems: ").append(toIndentedString(refundItems)).append("\n");
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

