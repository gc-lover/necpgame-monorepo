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
 * PriceCalculationResultPriceBreakdown
 */

@JsonTypeName("PriceCalculationResult_price_breakdown")

public class PriceCalculationResultPriceBreakdown {

  private @Nullable Integer base;

  private @Nullable Integer qualityBonus;

  private @Nullable Integer rarityBonus;

  private @Nullable Integer durabilityPenalty;

  private @Nullable Integer regionalAdjustment;

  private @Nullable Integer factionDiscount;

  private @Nullable Integer eventImpact;

  private @Nullable Integer quantityDiscount;

  public PriceCalculationResultPriceBreakdown base(@Nullable Integer base) {
    this.base = base;
    return this;
  }

  /**
   * Get base
   * @return base
   */
  
  @Schema(name = "base", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base")
  public @Nullable Integer getBase() {
    return base;
  }

  public void setBase(@Nullable Integer base) {
    this.base = base;
  }

  public PriceCalculationResultPriceBreakdown qualityBonus(@Nullable Integer qualityBonus) {
    this.qualityBonus = qualityBonus;
    return this;
  }

  /**
   * Get qualityBonus
   * @return qualityBonus
   */
  
  @Schema(name = "quality_bonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quality_bonus")
  public @Nullable Integer getQualityBonus() {
    return qualityBonus;
  }

  public void setQualityBonus(@Nullable Integer qualityBonus) {
    this.qualityBonus = qualityBonus;
  }

  public PriceCalculationResultPriceBreakdown rarityBonus(@Nullable Integer rarityBonus) {
    this.rarityBonus = rarityBonus;
    return this;
  }

  /**
   * Get rarityBonus
   * @return rarityBonus
   */
  
  @Schema(name = "rarity_bonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarity_bonus")
  public @Nullable Integer getRarityBonus() {
    return rarityBonus;
  }

  public void setRarityBonus(@Nullable Integer rarityBonus) {
    this.rarityBonus = rarityBonus;
  }

  public PriceCalculationResultPriceBreakdown durabilityPenalty(@Nullable Integer durabilityPenalty) {
    this.durabilityPenalty = durabilityPenalty;
    return this;
  }

  /**
   * Get durabilityPenalty
   * @return durabilityPenalty
   */
  
  @Schema(name = "durability_penalty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durability_penalty")
  public @Nullable Integer getDurabilityPenalty() {
    return durabilityPenalty;
  }

  public void setDurabilityPenalty(@Nullable Integer durabilityPenalty) {
    this.durabilityPenalty = durabilityPenalty;
  }

  public PriceCalculationResultPriceBreakdown regionalAdjustment(@Nullable Integer regionalAdjustment) {
    this.regionalAdjustment = regionalAdjustment;
    return this;
  }

  /**
   * Get regionalAdjustment
   * @return regionalAdjustment
   */
  
  @Schema(name = "regional_adjustment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("regional_adjustment")
  public @Nullable Integer getRegionalAdjustment() {
    return regionalAdjustment;
  }

  public void setRegionalAdjustment(@Nullable Integer regionalAdjustment) {
    this.regionalAdjustment = regionalAdjustment;
  }

  public PriceCalculationResultPriceBreakdown factionDiscount(@Nullable Integer factionDiscount) {
    this.factionDiscount = factionDiscount;
    return this;
  }

  /**
   * Get factionDiscount
   * @return factionDiscount
   */
  
  @Schema(name = "faction_discount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_discount")
  public @Nullable Integer getFactionDiscount() {
    return factionDiscount;
  }

  public void setFactionDiscount(@Nullable Integer factionDiscount) {
    this.factionDiscount = factionDiscount;
  }

  public PriceCalculationResultPriceBreakdown eventImpact(@Nullable Integer eventImpact) {
    this.eventImpact = eventImpact;
    return this;
  }

  /**
   * Get eventImpact
   * @return eventImpact
   */
  
  @Schema(name = "event_impact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_impact")
  public @Nullable Integer getEventImpact() {
    return eventImpact;
  }

  public void setEventImpact(@Nullable Integer eventImpact) {
    this.eventImpact = eventImpact;
  }

  public PriceCalculationResultPriceBreakdown quantityDiscount(@Nullable Integer quantityDiscount) {
    this.quantityDiscount = quantityDiscount;
    return this;
  }

  /**
   * Get quantityDiscount
   * @return quantityDiscount
   */
  
  @Schema(name = "quantity_discount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity_discount")
  public @Nullable Integer getQuantityDiscount() {
    return quantityDiscount;
  }

  public void setQuantityDiscount(@Nullable Integer quantityDiscount) {
    this.quantityDiscount = quantityDiscount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PriceCalculationResultPriceBreakdown priceCalculationResultPriceBreakdown = (PriceCalculationResultPriceBreakdown) o;
    return Objects.equals(this.base, priceCalculationResultPriceBreakdown.base) &&
        Objects.equals(this.qualityBonus, priceCalculationResultPriceBreakdown.qualityBonus) &&
        Objects.equals(this.rarityBonus, priceCalculationResultPriceBreakdown.rarityBonus) &&
        Objects.equals(this.durabilityPenalty, priceCalculationResultPriceBreakdown.durabilityPenalty) &&
        Objects.equals(this.regionalAdjustment, priceCalculationResultPriceBreakdown.regionalAdjustment) &&
        Objects.equals(this.factionDiscount, priceCalculationResultPriceBreakdown.factionDiscount) &&
        Objects.equals(this.eventImpact, priceCalculationResultPriceBreakdown.eventImpact) &&
        Objects.equals(this.quantityDiscount, priceCalculationResultPriceBreakdown.quantityDiscount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(base, qualityBonus, rarityBonus, durabilityPenalty, regionalAdjustment, factionDiscount, eventImpact, quantityDiscount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PriceCalculationResultPriceBreakdown {\n");
    sb.append("    base: ").append(toIndentedString(base)).append("\n");
    sb.append("    qualityBonus: ").append(toIndentedString(qualityBonus)).append("\n");
    sb.append("    rarityBonus: ").append(toIndentedString(rarityBonus)).append("\n");
    sb.append("    durabilityPenalty: ").append(toIndentedString(durabilityPenalty)).append("\n");
    sb.append("    regionalAdjustment: ").append(toIndentedString(regionalAdjustment)).append("\n");
    sb.append("    factionDiscount: ").append(toIndentedString(factionDiscount)).append("\n");
    sb.append("    eventImpact: ").append(toIndentedString(eventImpact)).append("\n");
    sb.append("    quantityDiscount: ").append(toIndentedString(quantityDiscount)).append("\n");
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

