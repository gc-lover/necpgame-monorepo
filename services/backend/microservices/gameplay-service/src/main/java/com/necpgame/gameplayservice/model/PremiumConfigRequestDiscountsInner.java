package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
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
 * PremiumConfigRequestDiscountsInner
 */

@JsonTypeName("PremiumConfigRequest_discounts_inner")

public class PremiumConfigRequestDiscountsInner {

  private @Nullable String tier;

  private @Nullable BigDecimal percent;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime validFrom;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime validUntil;

  public PremiumConfigRequestDiscountsInner tier(@Nullable String tier) {
    this.tier = tier;
    return this;
  }

  /**
   * Get tier
   * @return tier
   */
  
  @Schema(name = "tier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tier")
  public @Nullable String getTier() {
    return tier;
  }

  public void setTier(@Nullable String tier) {
    this.tier = tier;
  }

  public PremiumConfigRequestDiscountsInner percent(@Nullable BigDecimal percent) {
    this.percent = percent;
    return this;
  }

  /**
   * Get percent
   * @return percent
   */
  @Valid 
  @Schema(name = "percent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("percent")
  public @Nullable BigDecimal getPercent() {
    return percent;
  }

  public void setPercent(@Nullable BigDecimal percent) {
    this.percent = percent;
  }

  public PremiumConfigRequestDiscountsInner validFrom(@Nullable OffsetDateTime validFrom) {
    this.validFrom = validFrom;
    return this;
  }

  /**
   * Get validFrom
   * @return validFrom
   */
  @Valid 
  @Schema(name = "validFrom", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("validFrom")
  public @Nullable OffsetDateTime getValidFrom() {
    return validFrom;
  }

  public void setValidFrom(@Nullable OffsetDateTime validFrom) {
    this.validFrom = validFrom;
  }

  public PremiumConfigRequestDiscountsInner validUntil(@Nullable OffsetDateTime validUntil) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PremiumConfigRequestDiscountsInner premiumConfigRequestDiscountsInner = (PremiumConfigRequestDiscountsInner) o;
    return Objects.equals(this.tier, premiumConfigRequestDiscountsInner.tier) &&
        Objects.equals(this.percent, premiumConfigRequestDiscountsInner.percent) &&
        Objects.equals(this.validFrom, premiumConfigRequestDiscountsInner.validFrom) &&
        Objects.equals(this.validUntil, premiumConfigRequestDiscountsInner.validUntil);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tier, percent, validFrom, validUntil);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PremiumConfigRequestDiscountsInner {\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    percent: ").append(toIndentedString(percent)).append("\n");
    sb.append("    validFrom: ").append(toIndentedString(validFrom)).append("\n");
    sb.append("    validUntil: ").append(toIndentedString(validUntil)).append("\n");
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

