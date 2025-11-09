package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * ApartmentUpkeep
 */

@JsonTypeName("Apartment_upkeep")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ApartmentUpkeep {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime nextPaymentAt;

  private @Nullable Integer amount;

  public ApartmentUpkeep nextPaymentAt(@Nullable OffsetDateTime nextPaymentAt) {
    this.nextPaymentAt = nextPaymentAt;
    return this;
  }

  /**
   * Get nextPaymentAt
   * @return nextPaymentAt
   */
  @Valid 
  @Schema(name = "nextPaymentAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextPaymentAt")
  public @Nullable OffsetDateTime getNextPaymentAt() {
    return nextPaymentAt;
  }

  public void setNextPaymentAt(@Nullable OffsetDateTime nextPaymentAt) {
    this.nextPaymentAt = nextPaymentAt;
  }

  public ApartmentUpkeep amount(@Nullable Integer amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   */
  
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount")
  public @Nullable Integer getAmount() {
    return amount;
  }

  public void setAmount(@Nullable Integer amount) {
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApartmentUpkeep apartmentUpkeep = (ApartmentUpkeep) o;
    return Objects.equals(this.nextPaymentAt, apartmentUpkeep.nextPaymentAt) &&
        Objects.equals(this.amount, apartmentUpkeep.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nextPaymentAt, amount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApartmentUpkeep {\n");
    sb.append("    nextPaymentAt: ").append(toIndentedString(nextPaymentAt)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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

