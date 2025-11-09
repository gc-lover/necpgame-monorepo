package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
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
 * PortfolioAnalytics
 */


public class PortfolioAnalytics {

  private @Nullable String characterId;

  private @Nullable BigDecimal totalValue;

  private @Nullable BigDecimal totalInvested;

  private @Nullable BigDecimal profitLoss;

  private @Nullable BigDecimal roiPercent;

  private @Nullable Object diversification;

  @Valid
  private List<Object> topPerformers = new ArrayList<>();

  public PortfolioAnalytics characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public PortfolioAnalytics totalValue(@Nullable BigDecimal totalValue) {
    this.totalValue = totalValue;
    return this;
  }

  /**
   * Get totalValue
   * @return totalValue
   */
  @Valid 
  @Schema(name = "total_value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_value")
  public @Nullable BigDecimal getTotalValue() {
    return totalValue;
  }

  public void setTotalValue(@Nullable BigDecimal totalValue) {
    this.totalValue = totalValue;
  }

  public PortfolioAnalytics totalInvested(@Nullable BigDecimal totalInvested) {
    this.totalInvested = totalInvested;
    return this;
  }

  /**
   * Get totalInvested
   * @return totalInvested
   */
  @Valid 
  @Schema(name = "total_invested", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_invested")
  public @Nullable BigDecimal getTotalInvested() {
    return totalInvested;
  }

  public void setTotalInvested(@Nullable BigDecimal totalInvested) {
    this.totalInvested = totalInvested;
  }

  public PortfolioAnalytics profitLoss(@Nullable BigDecimal profitLoss) {
    this.profitLoss = profitLoss;
    return this;
  }

  /**
   * Get profitLoss
   * @return profitLoss
   */
  @Valid 
  @Schema(name = "profit_loss", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("profit_loss")
  public @Nullable BigDecimal getProfitLoss() {
    return profitLoss;
  }

  public void setProfitLoss(@Nullable BigDecimal profitLoss) {
    this.profitLoss = profitLoss;
  }

  public PortfolioAnalytics roiPercent(@Nullable BigDecimal roiPercent) {
    this.roiPercent = roiPercent;
    return this;
  }

  /**
   * Get roiPercent
   * @return roiPercent
   */
  @Valid 
  @Schema(name = "roi_percent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roi_percent")
  public @Nullable BigDecimal getRoiPercent() {
    return roiPercent;
  }

  public void setRoiPercent(@Nullable BigDecimal roiPercent) {
    this.roiPercent = roiPercent;
  }

  public PortfolioAnalytics diversification(@Nullable Object diversification) {
    this.diversification = diversification;
    return this;
  }

  /**
   * Get diversification
   * @return diversification
   */
  
  @Schema(name = "diversification", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("diversification")
  public @Nullable Object getDiversification() {
    return diversification;
  }

  public void setDiversification(@Nullable Object diversification) {
    this.diversification = diversification;
  }

  public PortfolioAnalytics topPerformers(List<Object> topPerformers) {
    this.topPerformers = topPerformers;
    return this;
  }

  public PortfolioAnalytics addTopPerformersItem(Object topPerformersItem) {
    if (this.topPerformers == null) {
      this.topPerformers = new ArrayList<>();
    }
    this.topPerformers.add(topPerformersItem);
    return this;
  }

  /**
   * Get topPerformers
   * @return topPerformers
   */
  
  @Schema(name = "top_performers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("top_performers")
  public List<Object> getTopPerformers() {
    return topPerformers;
  }

  public void setTopPerformers(List<Object> topPerformers) {
    this.topPerformers = topPerformers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PortfolioAnalytics portfolioAnalytics = (PortfolioAnalytics) o;
    return Objects.equals(this.characterId, portfolioAnalytics.characterId) &&
        Objects.equals(this.totalValue, portfolioAnalytics.totalValue) &&
        Objects.equals(this.totalInvested, portfolioAnalytics.totalInvested) &&
        Objects.equals(this.profitLoss, portfolioAnalytics.profitLoss) &&
        Objects.equals(this.roiPercent, portfolioAnalytics.roiPercent) &&
        Objects.equals(this.diversification, portfolioAnalytics.diversification) &&
        Objects.equals(this.topPerformers, portfolioAnalytics.topPerformers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, totalValue, totalInvested, profitLoss, roiPercent, diversification, topPerformers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PortfolioAnalytics {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    totalValue: ").append(toIndentedString(totalValue)).append("\n");
    sb.append("    totalInvested: ").append(toIndentedString(totalInvested)).append("\n");
    sb.append("    profitLoss: ").append(toIndentedString(profitLoss)).append("\n");
    sb.append("    roiPercent: ").append(toIndentedString(roiPercent)).append("\n");
    sb.append("    diversification: ").append(toIndentedString(diversification)).append("\n");
    sb.append("    topPerformers: ").append(toIndentedString(topPerformers)).append("\n");
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

