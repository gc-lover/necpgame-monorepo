package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * CharacterSlotStateResponsePendingPaymentsInner
 */

@JsonTypeName("CharacterSlotStateResponse_pendingPayments_inner")

public class CharacterSlotStateResponsePendingPaymentsInner {

  private @Nullable String referenceId;

  private @Nullable Integer amount;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("pending"),
    
    FAILED("failed"),
    
    COMPLETED("completed");

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

  public CharacterSlotStateResponsePendingPaymentsInner referenceId(@Nullable String referenceId) {
    this.referenceId = referenceId;
    return this;
  }

  /**
   * Get referenceId
   * @return referenceId
   */
  
  @Schema(name = "referenceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("referenceId")
  public @Nullable String getReferenceId() {
    return referenceId;
  }

  public void setReferenceId(@Nullable String referenceId) {
    this.referenceId = referenceId;
  }

  public CharacterSlotStateResponsePendingPaymentsInner amount(@Nullable Integer amount) {
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

  public CharacterSlotStateResponsePendingPaymentsInner status(@Nullable StatusEnum status) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterSlotStateResponsePendingPaymentsInner characterSlotStateResponsePendingPaymentsInner = (CharacterSlotStateResponsePendingPaymentsInner) o;
    return Objects.equals(this.referenceId, characterSlotStateResponsePendingPaymentsInner.referenceId) &&
        Objects.equals(this.amount, characterSlotStateResponsePendingPaymentsInner.amount) &&
        Objects.equals(this.status, characterSlotStateResponsePendingPaymentsInner.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(referenceId, amount, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterSlotStateResponsePendingPaymentsInner {\n");
    sb.append("    referenceId: ").append(toIndentedString(referenceId)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

