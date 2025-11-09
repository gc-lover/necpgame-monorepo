package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.URI;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CharacterSlotPaymentRequiredResponse
 */


public class CharacterSlotPaymentRequiredResponse {

  private URI paymentUrl;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime expiresAt;

  private @Nullable String message;

  public CharacterSlotPaymentRequiredResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterSlotPaymentRequiredResponse(URI paymentUrl, OffsetDateTime expiresAt) {
    this.paymentUrl = paymentUrl;
    this.expiresAt = expiresAt;
  }

  public CharacterSlotPaymentRequiredResponse paymentUrl(URI paymentUrl) {
    this.paymentUrl = paymentUrl;
    return this;
  }

  /**
   * Get paymentUrl
   * @return paymentUrl
   */
  @NotNull @Valid 
  @Schema(name = "paymentUrl", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("paymentUrl")
  public URI getPaymentUrl() {
    return paymentUrl;
  }

  public void setPaymentUrl(URI paymentUrl) {
    this.paymentUrl = paymentUrl;
  }

  public CharacterSlotPaymentRequiredResponse expiresAt(OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @NotNull @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("expiresAt")
  public OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public CharacterSlotPaymentRequiredResponse message(@Nullable String message) {
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
    CharacterSlotPaymentRequiredResponse characterSlotPaymentRequiredResponse = (CharacterSlotPaymentRequiredResponse) o;
    return Objects.equals(this.paymentUrl, characterSlotPaymentRequiredResponse.paymentUrl) &&
        Objects.equals(this.expiresAt, characterSlotPaymentRequiredResponse.expiresAt) &&
        Objects.equals(this.message, characterSlotPaymentRequiredResponse.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(paymentUrl, expiresAt, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterSlotPaymentRequiredResponse {\n");
    sb.append("    paymentUrl: ").append(toIndentedString(paymentUrl)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

