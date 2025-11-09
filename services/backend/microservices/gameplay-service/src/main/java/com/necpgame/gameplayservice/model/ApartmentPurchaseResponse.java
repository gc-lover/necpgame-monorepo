package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.Apartment;
import com.necpgame.gameplayservice.model.ApartmentPurchaseResponsePayment;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ApartmentPurchaseResponse
 */


public class ApartmentPurchaseResponse {

  private @Nullable Apartment apartment;

  private @Nullable ApartmentPurchaseResponsePayment payment;

  private @Nullable Integer prestigeGain;

  public ApartmentPurchaseResponse apartment(@Nullable Apartment apartment) {
    this.apartment = apartment;
    return this;
  }

  /**
   * Get apartment
   * @return apartment
   */
  @Valid 
  @Schema(name = "apartment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("apartment")
  public @Nullable Apartment getApartment() {
    return apartment;
  }

  public void setApartment(@Nullable Apartment apartment) {
    this.apartment = apartment;
  }

  public ApartmentPurchaseResponse payment(@Nullable ApartmentPurchaseResponsePayment payment) {
    this.payment = payment;
    return this;
  }

  /**
   * Get payment
   * @return payment
   */
  @Valid 
  @Schema(name = "payment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("payment")
  public @Nullable ApartmentPurchaseResponsePayment getPayment() {
    return payment;
  }

  public void setPayment(@Nullable ApartmentPurchaseResponsePayment payment) {
    this.payment = payment;
  }

  public ApartmentPurchaseResponse prestigeGain(@Nullable Integer prestigeGain) {
    this.prestigeGain = prestigeGain;
    return this;
  }

  /**
   * Get prestigeGain
   * @return prestigeGain
   */
  
  @Schema(name = "prestigeGain", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("prestigeGain")
  public @Nullable Integer getPrestigeGain() {
    return prestigeGain;
  }

  public void setPrestigeGain(@Nullable Integer prestigeGain) {
    this.prestigeGain = prestigeGain;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApartmentPurchaseResponse apartmentPurchaseResponse = (ApartmentPurchaseResponse) o;
    return Objects.equals(this.apartment, apartmentPurchaseResponse.apartment) &&
        Objects.equals(this.payment, apartmentPurchaseResponse.payment) &&
        Objects.equals(this.prestigeGain, apartmentPurchaseResponse.prestigeGain);
  }

  @Override
  public int hashCode() {
    return Objects.hash(apartment, payment, prestigeGain);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApartmentPurchaseResponse {\n");
    sb.append("    apartment: ").append(toIndentedString(apartment)).append("\n");
    sb.append("    payment: ").append(toIndentedString(payment)).append("\n");
    sb.append("    prestigeGain: ").append(toIndentedString(prestigeGain)).append("\n");
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

