package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.PremiumConfigRequestDiscountsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PremiumConfigRequest
 */


public class PremiumConfigRequest {

  private Integer premiumPrice;

  private String premiumCurrency;

  @Valid
  private List<@Valid PremiumConfigRequestDiscountsInner> discounts = new ArrayList<>();

  public PremiumConfigRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PremiumConfigRequest(Integer premiumPrice, String premiumCurrency) {
    this.premiumPrice = premiumPrice;
    this.premiumCurrency = premiumCurrency;
  }

  public PremiumConfigRequest premiumPrice(Integer premiumPrice) {
    this.premiumPrice = premiumPrice;
    return this;
  }

  /**
   * Get premiumPrice
   * minimum: 0
   * @return premiumPrice
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "premiumPrice", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("premiumPrice")
  public Integer getPremiumPrice() {
    return premiumPrice;
  }

  public void setPremiumPrice(Integer premiumPrice) {
    this.premiumPrice = premiumPrice;
  }

  public PremiumConfigRequest premiumCurrency(String premiumCurrency) {
    this.premiumCurrency = premiumCurrency;
    return this;
  }

  /**
   * Get premiumCurrency
   * @return premiumCurrency
   */
  @NotNull 
  @Schema(name = "premiumCurrency", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("premiumCurrency")
  public String getPremiumCurrency() {
    return premiumCurrency;
  }

  public void setPremiumCurrency(String premiumCurrency) {
    this.premiumCurrency = premiumCurrency;
  }

  public PremiumConfigRequest discounts(List<@Valid PremiumConfigRequestDiscountsInner> discounts) {
    this.discounts = discounts;
    return this;
  }

  public PremiumConfigRequest addDiscountsItem(PremiumConfigRequestDiscountsInner discountsItem) {
    if (this.discounts == null) {
      this.discounts = new ArrayList<>();
    }
    this.discounts.add(discountsItem);
    return this;
  }

  /**
   * Get discounts
   * @return discounts
   */
  @Valid 
  @Schema(name = "discounts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("discounts")
  public List<@Valid PremiumConfigRequestDiscountsInner> getDiscounts() {
    return discounts;
  }

  public void setDiscounts(List<@Valid PremiumConfigRequestDiscountsInner> discounts) {
    this.discounts = discounts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PremiumConfigRequest premiumConfigRequest = (PremiumConfigRequest) o;
    return Objects.equals(this.premiumPrice, premiumConfigRequest.premiumPrice) &&
        Objects.equals(this.premiumCurrency, premiumConfigRequest.premiumCurrency) &&
        Objects.equals(this.discounts, premiumConfigRequest.discounts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(premiumPrice, premiumCurrency, discounts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PremiumConfigRequest {\n");
    sb.append("    premiumPrice: ").append(toIndentedString(premiumPrice)).append("\n");
    sb.append("    premiumCurrency: ").append(toIndentedString(premiumCurrency)).append("\n");
    sb.append("    discounts: ").append(toIndentedString(discounts)).append("\n");
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

