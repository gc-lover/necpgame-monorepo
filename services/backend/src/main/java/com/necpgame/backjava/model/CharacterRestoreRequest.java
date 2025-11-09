package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CharacterRestoreRequest
 */


public class CharacterRestoreRequest {

  private @Nullable String reason;

  private @Nullable String paymentReference;

  private Boolean skipCost = false;

  public CharacterRestoreRequest reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", example = "undo_delete", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  public CharacterRestoreRequest paymentReference(@Nullable String paymentReference) {
    this.paymentReference = paymentReference;
    return this;
  }

  /**
   * Get paymentReference
   * @return paymentReference
   */
  
  @Schema(name = "paymentReference", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("paymentReference")
  public @Nullable String getPaymentReference() {
    return paymentReference;
  }

  public void setPaymentReference(@Nullable String paymentReference) {
    this.paymentReference = paymentReference;
  }

  public CharacterRestoreRequest skipCost(Boolean skipCost) {
    this.skipCost = skipCost;
    return this;
  }

  /**
   * Get skipCost
   * @return skipCost
   */
  
  @Schema(name = "skipCost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skipCost")
  public Boolean getSkipCost() {
    return skipCost;
  }

  public void setSkipCost(Boolean skipCost) {
    this.skipCost = skipCost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterRestoreRequest characterRestoreRequest = (CharacterRestoreRequest) o;
    return Objects.equals(this.reason, characterRestoreRequest.reason) &&
        Objects.equals(this.paymentReference, characterRestoreRequest.paymentReference) &&
        Objects.equals(this.skipCost, characterRestoreRequest.skipCost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason, paymentReference, skipCost);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterRestoreRequest {\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    paymentReference: ").append(toIndentedString(paymentReference)).append("\n");
    sb.append("    skipCost: ").append(toIndentedString(skipCost)).append("\n");
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

