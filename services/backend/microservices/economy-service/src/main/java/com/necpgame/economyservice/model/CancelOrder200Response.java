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
 * CancelOrder200Response
 */

@JsonTypeName("cancelOrder_200_response")

public class CancelOrder200Response {

  private @Nullable Boolean success;

  private @Nullable String orderId;

  private @Nullable BigDecimal refundAmount;

  public CancelOrder200Response success(@Nullable Boolean success) {
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

  public CancelOrder200Response orderId(@Nullable String orderId) {
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

  public CancelOrder200Response refundAmount(@Nullable BigDecimal refundAmount) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CancelOrder200Response cancelOrder200Response = (CancelOrder200Response) o;
    return Objects.equals(this.success, cancelOrder200Response.success) &&
        Objects.equals(this.orderId, cancelOrder200Response.orderId) &&
        Objects.equals(this.refundAmount, cancelOrder200Response.refundAmount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, orderId, refundAmount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CancelOrder200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    refundAmount: ").append(toIndentedString(refundAmount)).append("\n");
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

