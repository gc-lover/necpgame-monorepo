package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.AnalyticsItemMetric;
import com.necpgame.gameplayservice.model.AnalyticsResponseRarityDistributionInner;
import com.necpgame.gameplayservice.model.AnalyticsResponseRetention;
import com.necpgame.gameplayservice.model.AnalyticsResponseRevenue;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * AnalyticsResponse
 */


public class AnalyticsResponse {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime generatedAt;

  private @Nullable String range;

  private @Nullable String region;

  @Valid
  private List<@Valid AnalyticsItemMetric> topItems = new ArrayList<>();

  private @Nullable AnalyticsResponseRevenue revenue;

  private @Nullable BigDecimal usageRate;

  private @Nullable BigDecimal conversionRate;

  @Valid
  private List<@Valid AnalyticsResponseRarityDistributionInner> rarityDistribution = new ArrayList<>();

  private @Nullable AnalyticsResponseRetention retention;

  public AnalyticsResponse generatedAt(@Nullable OffsetDateTime generatedAt) {
    this.generatedAt = generatedAt;
    return this;
  }

  /**
   * Get generatedAt
   * @return generatedAt
   */
  @Valid 
  @Schema(name = "generatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("generatedAt")
  public @Nullable OffsetDateTime getGeneratedAt() {
    return generatedAt;
  }

  public void setGeneratedAt(@Nullable OffsetDateTime generatedAt) {
    this.generatedAt = generatedAt;
  }

  public AnalyticsResponse range(@Nullable String range) {
    this.range = range;
    return this;
  }

  /**
   * Get range
   * @return range
   */
  
  @Schema(name = "range", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("range")
  public @Nullable String getRange() {
    return range;
  }

  public void setRange(@Nullable String range) {
    this.range = range;
  }

  public AnalyticsResponse region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  public AnalyticsResponse topItems(List<@Valid AnalyticsItemMetric> topItems) {
    this.topItems = topItems;
    return this;
  }

  public AnalyticsResponse addTopItemsItem(AnalyticsItemMetric topItemsItem) {
    if (this.topItems == null) {
      this.topItems = new ArrayList<>();
    }
    this.topItems.add(topItemsItem);
    return this;
  }

  /**
   * Get topItems
   * @return topItems
   */
  @Valid 
  @Schema(name = "topItems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("topItems")
  public List<@Valid AnalyticsItemMetric> getTopItems() {
    return topItems;
  }

  public void setTopItems(List<@Valid AnalyticsItemMetric> topItems) {
    this.topItems = topItems;
  }

  public AnalyticsResponse revenue(@Nullable AnalyticsResponseRevenue revenue) {
    this.revenue = revenue;
    return this;
  }

  /**
   * Get revenue
   * @return revenue
   */
  @Valid 
  @Schema(name = "revenue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("revenue")
  public @Nullable AnalyticsResponseRevenue getRevenue() {
    return revenue;
  }

  public void setRevenue(@Nullable AnalyticsResponseRevenue revenue) {
    this.revenue = revenue;
  }

  public AnalyticsResponse usageRate(@Nullable BigDecimal usageRate) {
    this.usageRate = usageRate;
    return this;
  }

  /**
   * Get usageRate
   * @return usageRate
   */
  @Valid 
  @Schema(name = "usageRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("usageRate")
  public @Nullable BigDecimal getUsageRate() {
    return usageRate;
  }

  public void setUsageRate(@Nullable BigDecimal usageRate) {
    this.usageRate = usageRate;
  }

  public AnalyticsResponse conversionRate(@Nullable BigDecimal conversionRate) {
    this.conversionRate = conversionRate;
    return this;
  }

  /**
   * Get conversionRate
   * @return conversionRate
   */
  @Valid 
  @Schema(name = "conversionRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conversionRate")
  public @Nullable BigDecimal getConversionRate() {
    return conversionRate;
  }

  public void setConversionRate(@Nullable BigDecimal conversionRate) {
    this.conversionRate = conversionRate;
  }

  public AnalyticsResponse rarityDistribution(List<@Valid AnalyticsResponseRarityDistributionInner> rarityDistribution) {
    this.rarityDistribution = rarityDistribution;
    return this;
  }

  public AnalyticsResponse addRarityDistributionItem(AnalyticsResponseRarityDistributionInner rarityDistributionItem) {
    if (this.rarityDistribution == null) {
      this.rarityDistribution = new ArrayList<>();
    }
    this.rarityDistribution.add(rarityDistributionItem);
    return this;
  }

  /**
   * Get rarityDistribution
   * @return rarityDistribution
   */
  @Valid 
  @Schema(name = "rarityDistribution", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarityDistribution")
  public List<@Valid AnalyticsResponseRarityDistributionInner> getRarityDistribution() {
    return rarityDistribution;
  }

  public void setRarityDistribution(List<@Valid AnalyticsResponseRarityDistributionInner> rarityDistribution) {
    this.rarityDistribution = rarityDistribution;
  }

  public AnalyticsResponse retention(@Nullable AnalyticsResponseRetention retention) {
    this.retention = retention;
    return this;
  }

  /**
   * Get retention
   * @return retention
   */
  @Valid 
  @Schema(name = "retention", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("retention")
  public @Nullable AnalyticsResponseRetention getRetention() {
    return retention;
  }

  public void setRetention(@Nullable AnalyticsResponseRetention retention) {
    this.retention = retention;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsResponse analyticsResponse = (AnalyticsResponse) o;
    return Objects.equals(this.generatedAt, analyticsResponse.generatedAt) &&
        Objects.equals(this.range, analyticsResponse.range) &&
        Objects.equals(this.region, analyticsResponse.region) &&
        Objects.equals(this.topItems, analyticsResponse.topItems) &&
        Objects.equals(this.revenue, analyticsResponse.revenue) &&
        Objects.equals(this.usageRate, analyticsResponse.usageRate) &&
        Objects.equals(this.conversionRate, analyticsResponse.conversionRate) &&
        Objects.equals(this.rarityDistribution, analyticsResponse.rarityDistribution) &&
        Objects.equals(this.retention, analyticsResponse.retention);
  }

  @Override
  public int hashCode() {
    return Objects.hash(generatedAt, range, region, topItems, revenue, usageRate, conversionRate, rarityDistribution, retention);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsResponse {\n");
    sb.append("    generatedAt: ").append(toIndentedString(generatedAt)).append("\n");
    sb.append("    range: ").append(toIndentedString(range)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    topItems: ").append(toIndentedString(topItems)).append("\n");
    sb.append("    revenue: ").append(toIndentedString(revenue)).append("\n");
    sb.append("    usageRate: ").append(toIndentedString(usageRate)).append("\n");
    sb.append("    conversionRate: ").append(toIndentedString(conversionRate)).append("\n");
    sb.append("    rarityDistribution: ").append(toIndentedString(rarityDistribution)).append("\n");
    sb.append("    retention: ").append(toIndentedString(retention)).append("\n");
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

