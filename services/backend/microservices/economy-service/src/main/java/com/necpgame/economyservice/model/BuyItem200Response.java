package com.necpgame.economyservice.model;

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
 * BuyItem200Response
 */

@JsonTypeName("buyItem_200_response")

public class BuyItem200Response {

  private @Nullable Boolean success;

  private @Nullable Integer totalCost;

  private @Nullable Integer remainingCurrency;

  public BuyItem200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public BuyItem200Response totalCost(@Nullable Integer totalCost) {
    this.totalCost = totalCost;
    return this;
  }

  /**
   * Get totalCost
   * @return totalCost
   */
  
  @Schema(name = "totalCost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalCost")
  public @Nullable Integer getTotalCost() {
    return totalCost;
  }

  public void setTotalCost(@Nullable Integer totalCost) {
    this.totalCost = totalCost;
  }

  public BuyItem200Response remainingCurrency(@Nullable Integer remainingCurrency) {
    this.remainingCurrency = remainingCurrency;
    return this;
  }

  /**
   * Get remainingCurrency
   * @return remainingCurrency
   */
  
  @Schema(name = "remainingCurrency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("remainingCurrency")
  public @Nullable Integer getRemainingCurrency() {
    return remainingCurrency;
  }

  public void setRemainingCurrency(@Nullable Integer remainingCurrency) {
    this.remainingCurrency = remainingCurrency;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BuyItem200Response buyItem200Response = (BuyItem200Response) o;
    return Objects.equals(this.success, buyItem200Response.success) &&
        Objects.equals(this.totalCost, buyItem200Response.totalCost) &&
        Objects.equals(this.remainingCurrency, buyItem200Response.remainingCurrency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, totalCost, remainingCurrency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BuyItem200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    totalCost: ").append(toIndentedString(totalCost)).append("\n");
    sb.append("    remainingCurrency: ").append(toIndentedString(remainingCurrency)).append("\n");
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

