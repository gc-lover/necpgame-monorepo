package com.necpgame.mailservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CODPaymentRequest
 */


public class CODPaymentRequest {

  /**
   * Gets or Sets paymentMethod
   */
  public enum PaymentMethodEnum {
    WALLET("WALLET"),
    
    CASH("CASH"),
    
    TOKEN("TOKEN");

    private final String value;

    PaymentMethodEnum(String value) {
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
    public static PaymentMethodEnum fromValue(String value) {
      for (PaymentMethodEnum b : PaymentMethodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PaymentMethodEnum paymentMethod;

  private @Nullable String confirmationCode;

  public CODPaymentRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CODPaymentRequest(PaymentMethodEnum paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public CODPaymentRequest paymentMethod(PaymentMethodEnum paymentMethod) {
    this.paymentMethod = paymentMethod;
    return this;
  }

  /**
   * Get paymentMethod
   * @return paymentMethod
   */
  @NotNull 
  @Schema(name = "paymentMethod", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("paymentMethod")
  public PaymentMethodEnum getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(PaymentMethodEnum paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public CODPaymentRequest confirmationCode(@Nullable String confirmationCode) {
    this.confirmationCode = confirmationCode;
    return this;
  }

  /**
   * Get confirmationCode
   * @return confirmationCode
   */
  
  @Schema(name = "confirmationCode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("confirmationCode")
  public @Nullable String getConfirmationCode() {
    return confirmationCode;
  }

  public void setConfirmationCode(@Nullable String confirmationCode) {
    this.confirmationCode = confirmationCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CODPaymentRequest coDPaymentRequest = (CODPaymentRequest) o;
    return Objects.equals(this.paymentMethod, coDPaymentRequest.paymentMethod) &&
        Objects.equals(this.confirmationCode, coDPaymentRequest.confirmationCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(paymentMethod, confirmationCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CODPaymentRequest {\n");
    sb.append("    paymentMethod: ").append(toIndentedString(paymentMethod)).append("\n");
    sb.append("    confirmationCode: ").append(toIndentedString(confirmationCode)).append("\n");
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

