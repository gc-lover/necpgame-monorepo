package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * OrderResult
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class OrderResult {

  private @Nullable String orderId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    FILLED("filled"),
    
    PARTIALLY_FILLED("partially_filled"),
    
    PENDING("pending");

    private final String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  private @Nullable Integer filledQuantity;

  private @Nullable Integer remainingQuantity;

  private @Nullable BigDecimal averagePrice;

  private @Nullable BigDecimal totalCost;

  private @Nullable BigDecimal commission;

  public OrderResult orderId(@Nullable String orderId) {
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

  public OrderResult status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public OrderResult filledQuantity(@Nullable Integer filledQuantity) {
    this.filledQuantity = filledQuantity;
    return this;
  }

  /**
   * Get filledQuantity
   * @return filledQuantity
   */
  
  @Schema(name = "filled_quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("filled_quantity")
  public @Nullable Integer getFilledQuantity() {
    return filledQuantity;
  }

  public void setFilledQuantity(@Nullable Integer filledQuantity) {
    this.filledQuantity = filledQuantity;
  }

  public OrderResult remainingQuantity(@Nullable Integer remainingQuantity) {
    this.remainingQuantity = remainingQuantity;
    return this;
  }

  /**
   * Get remainingQuantity
   * @return remainingQuantity
   */
  
  @Schema(name = "remaining_quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("remaining_quantity")
  public @Nullable Integer getRemainingQuantity() {
    return remainingQuantity;
  }

  public void setRemainingQuantity(@Nullable Integer remainingQuantity) {
    this.remainingQuantity = remainingQuantity;
  }

  public OrderResult averagePrice(@Nullable BigDecimal averagePrice) {
    this.averagePrice = averagePrice;
    return this;
  }

  /**
   * Get averagePrice
   * @return averagePrice
   */
  @Valid 
  @Schema(name = "average_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("average_price")
  public @Nullable BigDecimal getAveragePrice() {
    return averagePrice;
  }

  public void setAveragePrice(@Nullable BigDecimal averagePrice) {
    this.averagePrice = averagePrice;
  }

  public OrderResult totalCost(@Nullable BigDecimal totalCost) {
    this.totalCost = totalCost;
    return this;
  }

  /**
   * Get totalCost
   * @return totalCost
   */
  @Valid 
  @Schema(name = "total_cost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_cost")
  public @Nullable BigDecimal getTotalCost() {
    return totalCost;
  }

  public void setTotalCost(@Nullable BigDecimal totalCost) {
    this.totalCost = totalCost;
  }

  public OrderResult commission(@Nullable BigDecimal commission) {
    this.commission = commission;
    return this;
  }

  /**
   * Get commission
   * @return commission
   */
  @Valid 
  @Schema(name = "commission", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("commission")
  public @Nullable BigDecimal getCommission() {
    return commission;
  }

  public void setCommission(@Nullable BigDecimal commission) {
    this.commission = commission;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderResult orderResult = (OrderResult) o;
    return Objects.equals(this.orderId, orderResult.orderId) &&
        Objects.equals(this.status, orderResult.status) &&
        Objects.equals(this.filledQuantity, orderResult.filledQuantity) &&
        Objects.equals(this.remainingQuantity, orderResult.remainingQuantity) &&
        Objects.equals(this.averagePrice, orderResult.averagePrice) &&
        Objects.equals(this.totalCost, orderResult.totalCost) &&
        Objects.equals(this.commission, orderResult.commission);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, status, filledQuantity, remainingQuantity, averagePrice, totalCost, commission);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderResult {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    filledQuantity: ").append(toIndentedString(filledQuantity)).append("\n");
    sb.append("    remainingQuantity: ").append(toIndentedString(remainingQuantity)).append("\n");
    sb.append("    averagePrice: ").append(toIndentedString(averagePrice)).append("\n");
    sb.append("    totalCost: ").append(toIndentedString(totalCost)).append("\n");
    sb.append("    commission: ").append(toIndentedString(commission)).append("\n");
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

