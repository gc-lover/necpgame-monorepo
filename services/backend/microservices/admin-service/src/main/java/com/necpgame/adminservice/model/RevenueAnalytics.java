package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RevenueAnalytics
 */


public class RevenueAnalytics {

  private @Nullable BigDecimal totalRevenue;

  private @Nullable BigDecimal arpu;

  private @Nullable BigDecimal arppu;

  private @Nullable BigDecimal conversionRate;

  @Valid
  private Map<String, BigDecimal> revenueBySource = new HashMap<>();

  public RevenueAnalytics totalRevenue(@Nullable BigDecimal totalRevenue) {
    this.totalRevenue = totalRevenue;
    return this;
  }

  /**
   * Get totalRevenue
   * @return totalRevenue
   */
  @Valid 
  @Schema(name = "total_revenue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_revenue")
  public @Nullable BigDecimal getTotalRevenue() {
    return totalRevenue;
  }

  public void setTotalRevenue(@Nullable BigDecimal totalRevenue) {
    this.totalRevenue = totalRevenue;
  }

  public RevenueAnalytics arpu(@Nullable BigDecimal arpu) {
    this.arpu = arpu;
    return this;
  }

  /**
   * Average Revenue Per User
   * @return arpu
   */
  @Valid 
  @Schema(name = "arpu", description = "Average Revenue Per User", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("arpu")
  public @Nullable BigDecimal getArpu() {
    return arpu;
  }

  public void setArpu(@Nullable BigDecimal arpu) {
    this.arpu = arpu;
  }

  public RevenueAnalytics arppu(@Nullable BigDecimal arppu) {
    this.arppu = arppu;
    return this;
  }

  /**
   * Average Revenue Per Paying User
   * @return arppu
   */
  @Valid 
  @Schema(name = "arppu", description = "Average Revenue Per Paying User", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("arppu")
  public @Nullable BigDecimal getArppu() {
    return arppu;
  }

  public void setArppu(@Nullable BigDecimal arppu) {
    this.arppu = arppu;
  }

  public RevenueAnalytics conversionRate(@Nullable BigDecimal conversionRate) {
    this.conversionRate = conversionRate;
    return this;
  }

  /**
   * Get conversionRate
   * @return conversionRate
   */
  @Valid 
  @Schema(name = "conversion_rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conversion_rate")
  public @Nullable BigDecimal getConversionRate() {
    return conversionRate;
  }

  public void setConversionRate(@Nullable BigDecimal conversionRate) {
    this.conversionRate = conversionRate;
  }

  public RevenueAnalytics revenueBySource(Map<String, BigDecimal> revenueBySource) {
    this.revenueBySource = revenueBySource;
    return this;
  }

  public RevenueAnalytics putRevenueBySourceItem(String key, BigDecimal revenueBySourceItem) {
    if (this.revenueBySource == null) {
      this.revenueBySource = new HashMap<>();
    }
    this.revenueBySource.put(key, revenueBySourceItem);
    return this;
  }

  /**
   * Get revenueBySource
   * @return revenueBySource
   */
  @Valid 
  @Schema(name = "revenue_by_source", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("revenue_by_source")
  public Map<String, BigDecimal> getRevenueBySource() {
    return revenueBySource;
  }

  public void setRevenueBySource(Map<String, BigDecimal> revenueBySource) {
    this.revenueBySource = revenueBySource;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RevenueAnalytics revenueAnalytics = (RevenueAnalytics) o;
    return Objects.equals(this.totalRevenue, revenueAnalytics.totalRevenue) &&
        Objects.equals(this.arpu, revenueAnalytics.arpu) &&
        Objects.equals(this.arppu, revenueAnalytics.arppu) &&
        Objects.equals(this.conversionRate, revenueAnalytics.conversionRate) &&
        Objects.equals(this.revenueBySource, revenueAnalytics.revenueBySource);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalRevenue, arpu, arppu, conversionRate, revenueBySource);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RevenueAnalytics {\n");
    sb.append("    totalRevenue: ").append(toIndentedString(totalRevenue)).append("\n");
    sb.append("    arpu: ").append(toIndentedString(arpu)).append("\n");
    sb.append("    arppu: ").append(toIndentedString(arppu)).append("\n");
    sb.append("    conversionRate: ").append(toIndentedString(conversionRate)).append("\n");
    sb.append("    revenueBySource: ").append(toIndentedString(revenueBySource)).append("\n");
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

