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
 * CharacterSlotStateNextTierCost
 */

@JsonTypeName("CharacterSlotState_nextTierCost")

public class CharacterSlotStateNextTierCost {

  private @Nullable String currency;

  private @Nullable Integer amount;

  private @Nullable Boolean requiresApproval;

  public CharacterSlotStateNextTierCost currency(@Nullable String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Get currency
   * @return currency
   */
  
  @Schema(name = "currency", example = "eddies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency")
  public @Nullable String getCurrency() {
    return currency;
  }

  public void setCurrency(@Nullable String currency) {
    this.currency = currency;
  }

  public CharacterSlotStateNextTierCost amount(@Nullable Integer amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * minimum: 0
   * @return amount
   */
  @Min(value = 0) 
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount")
  public @Nullable Integer getAmount() {
    return amount;
  }

  public void setAmount(@Nullable Integer amount) {
    this.amount = amount;
  }

  public CharacterSlotStateNextTierCost requiresApproval(@Nullable Boolean requiresApproval) {
    this.requiresApproval = requiresApproval;
    return this;
  }

  /**
   * Get requiresApproval
   * @return requiresApproval
   */
  
  @Schema(name = "requiresApproval", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requiresApproval")
  public @Nullable Boolean getRequiresApproval() {
    return requiresApproval;
  }

  public void setRequiresApproval(@Nullable Boolean requiresApproval) {
    this.requiresApproval = requiresApproval;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterSlotStateNextTierCost characterSlotStateNextTierCost = (CharacterSlotStateNextTierCost) o;
    return Objects.equals(this.currency, characterSlotStateNextTierCost.currency) &&
        Objects.equals(this.amount, characterSlotStateNextTierCost.amount) &&
        Objects.equals(this.requiresApproval, characterSlotStateNextTierCost.requiresApproval);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currency, amount, requiresApproval);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterSlotStateNextTierCost {\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    requiresApproval: ").append(toIndentedString(requiresApproval)).append("\n");
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

