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
 * SellItem200Response
 */

@JsonTypeName("sellItem_200_response")

public class SellItem200Response {

  private @Nullable Boolean success;

  private @Nullable Integer totalEarned;

  private @Nullable Integer remainingCurrency;

  public SellItem200Response success(@Nullable Boolean success) {
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

  public SellItem200Response totalEarned(@Nullable Integer totalEarned) {
    this.totalEarned = totalEarned;
    return this;
  }

  /**
   * Get totalEarned
   * @return totalEarned
   */
  
  @Schema(name = "totalEarned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalEarned")
  public @Nullable Integer getTotalEarned() {
    return totalEarned;
  }

  public void setTotalEarned(@Nullable Integer totalEarned) {
    this.totalEarned = totalEarned;
  }

  public SellItem200Response remainingCurrency(@Nullable Integer remainingCurrency) {
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
    SellItem200Response sellItem200Response = (SellItem200Response) o;
    return Objects.equals(this.success, sellItem200Response.success) &&
        Objects.equals(this.totalEarned, sellItem200Response.totalEarned) &&
        Objects.equals(this.remainingCurrency, sellItem200Response.remainingCurrency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, totalEarned, remainingCurrency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SellItem200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    totalEarned: ").append(toIndentedString(totalEarned)).append("\n");
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

