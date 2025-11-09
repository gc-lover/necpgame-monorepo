package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.InsurancePlan;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
 * InsuranceQuoteResponse
 */


public class InsuranceQuoteResponse {

  private UUID quoteId;

  private InsurancePlan plan;

  private Float premium;

  private @Nullable Float escrowRequired;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime validUntil;

  @Valid
  private List<String> discounts = new ArrayList<>();

  public InsuranceQuoteResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InsuranceQuoteResponse(UUID quoteId, InsurancePlan plan, Float premium) {
    this.quoteId = quoteId;
    this.plan = plan;
    this.premium = premium;
  }

  public InsuranceQuoteResponse quoteId(UUID quoteId) {
    this.quoteId = quoteId;
    return this;
  }

  /**
   * Get quoteId
   * @return quoteId
   */
  @NotNull @Valid 
  @Schema(name = "quoteId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quoteId")
  public UUID getQuoteId() {
    return quoteId;
  }

  public void setQuoteId(UUID quoteId) {
    this.quoteId = quoteId;
  }

  public InsuranceQuoteResponse plan(InsurancePlan plan) {
    this.plan = plan;
    return this;
  }

  /**
   * Get plan
   * @return plan
   */
  @NotNull @Valid 
  @Schema(name = "plan", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("plan")
  public InsurancePlan getPlan() {
    return plan;
  }

  public void setPlan(InsurancePlan plan) {
    this.plan = plan;
  }

  public InsuranceQuoteResponse premium(Float premium) {
    this.premium = premium;
    return this;
  }

  /**
   * Get premium
   * @return premium
   */
  @NotNull 
  @Schema(name = "premium", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("premium")
  public Float getPremium() {
    return premium;
  }

  public void setPremium(Float premium) {
    this.premium = premium;
  }

  public InsuranceQuoteResponse escrowRequired(@Nullable Float escrowRequired) {
    this.escrowRequired = escrowRequired;
    return this;
  }

  /**
   * Get escrowRequired
   * @return escrowRequired
   */
  
  @Schema(name = "escrowRequired", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("escrowRequired")
  public @Nullable Float getEscrowRequired() {
    return escrowRequired;
  }

  public void setEscrowRequired(@Nullable Float escrowRequired) {
    this.escrowRequired = escrowRequired;
  }

  public InsuranceQuoteResponse validUntil(@Nullable OffsetDateTime validUntil) {
    this.validUntil = validUntil;
    return this;
  }

  /**
   * Get validUntil
   * @return validUntil
   */
  @Valid 
  @Schema(name = "validUntil", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("validUntil")
  public @Nullable OffsetDateTime getValidUntil() {
    return validUntil;
  }

  public void setValidUntil(@Nullable OffsetDateTime validUntil) {
    this.validUntil = validUntil;
  }

  public InsuranceQuoteResponse discounts(List<String> discounts) {
    this.discounts = discounts;
    return this;
  }

  public InsuranceQuoteResponse addDiscountsItem(String discountsItem) {
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
  
  @Schema(name = "discounts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("discounts")
  public List<String> getDiscounts() {
    return discounts;
  }

  public void setDiscounts(List<String> discounts) {
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
    InsuranceQuoteResponse insuranceQuoteResponse = (InsuranceQuoteResponse) o;
    return Objects.equals(this.quoteId, insuranceQuoteResponse.quoteId) &&
        Objects.equals(this.plan, insuranceQuoteResponse.plan) &&
        Objects.equals(this.premium, insuranceQuoteResponse.premium) &&
        Objects.equals(this.escrowRequired, insuranceQuoteResponse.escrowRequired) &&
        Objects.equals(this.validUntil, insuranceQuoteResponse.validUntil) &&
        Objects.equals(this.discounts, insuranceQuoteResponse.discounts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(quoteId, plan, premium, escrowRequired, validUntil, discounts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InsuranceQuoteResponse {\n");
    sb.append("    quoteId: ").append(toIndentedString(quoteId)).append("\n");
    sb.append("    plan: ").append(toIndentedString(plan)).append("\n");
    sb.append("    premium: ").append(toIndentedString(premium)).append("\n");
    sb.append("    escrowRequired: ").append(toIndentedString(escrowRequired)).append("\n");
    sb.append("    validUntil: ").append(toIndentedString(validUntil)).append("\n");
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

