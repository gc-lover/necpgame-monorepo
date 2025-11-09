package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AttachmentCurrency
 */

@JsonTypeName("Attachment_currency")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class AttachmentCurrency {

  private @Nullable String currencyId;

  private @Nullable Integer amount;

  public AttachmentCurrency currencyId(@Nullable String currencyId) {
    this.currencyId = currencyId;
    return this;
  }

  /**
   * Get currencyId
   * @return currencyId
   */
  
  @Schema(name = "currencyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currencyId")
  public @Nullable String getCurrencyId() {
    return currencyId;
  }

  public void setCurrencyId(@Nullable String currencyId) {
    this.currencyId = currencyId;
  }

  public AttachmentCurrency amount(@Nullable Integer amount) {
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
    AttachmentCurrency attachmentCurrency = (AttachmentCurrency) o;
    return Objects.equals(this.currencyId, attachmentCurrency.currencyId) &&
        Objects.equals(this.amount, attachmentCurrency.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currencyId, amount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttachmentCurrency {\n");
    sb.append("    currencyId: ").append(toIndentedString(currencyId)).append("\n");
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

