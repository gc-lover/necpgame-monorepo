package com.necpgame.socialservice.model;

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
 * PlayerOrderCancellationResult
 */


public class PlayerOrderCancellationResult {

  private String orderId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    CANCELLATION_PENDING("cancellation_pending"),
    
    CANCELLED("cancelled");

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

  private StatusEnum status;

  private @Nullable BigDecimal refundAmount;

  private @Nullable String message;

  public PlayerOrderCancellationResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderCancellationResult(String orderId, StatusEnum status) {
    this.orderId = orderId;
    this.status = status;
  }

  public PlayerOrderCancellationResult orderId(String orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Get orderId
   * @return orderId
   */
  @NotNull 
  @Schema(name = "orderId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("orderId")
  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public PlayerOrderCancellationResult status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public PlayerOrderCancellationResult refundAmount(@Nullable BigDecimal refundAmount) {
    this.refundAmount = refundAmount;
    return this;
  }

  /**
   * Сумма, возвращённая заказчику.
   * @return refundAmount
   */
  @Valid 
  @Schema(name = "refundAmount", description = "Сумма, возвращённая заказчику.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("refundAmount")
  public @Nullable BigDecimal getRefundAmount() {
    return refundAmount;
  }

  public void setRefundAmount(@Nullable BigDecimal refundAmount) {
    this.refundAmount = refundAmount;
  }

  public PlayerOrderCancellationResult message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  
  @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderCancellationResult playerOrderCancellationResult = (PlayerOrderCancellationResult) o;
    return Objects.equals(this.orderId, playerOrderCancellationResult.orderId) &&
        Objects.equals(this.status, playerOrderCancellationResult.status) &&
        Objects.equals(this.refundAmount, playerOrderCancellationResult.refundAmount) &&
        Objects.equals(this.message, playerOrderCancellationResult.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, status, refundAmount, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderCancellationResult {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    refundAmount: ").append(toIndentedString(refundAmount)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

