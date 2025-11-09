package com.necpgame.gameplayservice.model;

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
 * BundlePurchaseResponseCompensation
 */

@JsonTypeName("BundlePurchaseResponse_compensation")

public class BundlePurchaseResponseCompensation {

  private @Nullable Integer currencyAmount;

  private @Nullable Integer charmsGranted;

  public BundlePurchaseResponseCompensation currencyAmount(@Nullable Integer currencyAmount) {
    this.currencyAmount = currencyAmount;
    return this;
  }

  /**
   * Get currencyAmount
   * @return currencyAmount
   */
  
  @Schema(name = "currencyAmount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currencyAmount")
  public @Nullable Integer getCurrencyAmount() {
    return currencyAmount;
  }

  public void setCurrencyAmount(@Nullable Integer currencyAmount) {
    this.currencyAmount = currencyAmount;
  }

  public BundlePurchaseResponseCompensation charmsGranted(@Nullable Integer charmsGranted) {
    this.charmsGranted = charmsGranted;
    return this;
  }

  /**
   * Get charmsGranted
   * @return charmsGranted
   */
  
  @Schema(name = "charmsGranted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("charmsGranted")
  public @Nullable Integer getCharmsGranted() {
    return charmsGranted;
  }

  public void setCharmsGranted(@Nullable Integer charmsGranted) {
    this.charmsGranted = charmsGranted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BundlePurchaseResponseCompensation bundlePurchaseResponseCompensation = (BundlePurchaseResponseCompensation) o;
    return Objects.equals(this.currencyAmount, bundlePurchaseResponseCompensation.currencyAmount) &&
        Objects.equals(this.charmsGranted, bundlePurchaseResponseCompensation.charmsGranted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currencyAmount, charmsGranted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BundlePurchaseResponseCompensation {\n");
    sb.append("    currencyAmount: ").append(toIndentedString(currencyAmount)).append("\n");
    sb.append("    charmsGranted: ").append(toIndentedString(charmsGranted)).append("\n");
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

