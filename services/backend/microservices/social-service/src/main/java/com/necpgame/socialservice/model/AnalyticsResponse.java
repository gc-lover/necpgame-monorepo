package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.AnalyticsResponseConversionRates;
import com.necpgame.socialservice.model.AnalyticsResponseTopChannelsInner;
import com.necpgame.socialservice.model.AnalyticsResponseTotals;
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
 * AnalyticsResponse
 */


public class AnalyticsResponse {

  private @Nullable String range;

  private @Nullable AnalyticsResponseTotals totals;

  private @Nullable AnalyticsResponseConversionRates conversionRates;

  private @Nullable BigDecimal revenuePerReferral;

  @Valid
  private List<@Valid AnalyticsResponseTopChannelsInner> topChannels = new ArrayList<>();

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

  public AnalyticsResponse totals(@Nullable AnalyticsResponseTotals totals) {
    this.totals = totals;
    return this;
  }

  /**
   * Get totals
   * @return totals
   */
  @Valid 
  @Schema(name = "totals", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totals")
  public @Nullable AnalyticsResponseTotals getTotals() {
    return totals;
  }

  public void setTotals(@Nullable AnalyticsResponseTotals totals) {
    this.totals = totals;
  }

  public AnalyticsResponse conversionRates(@Nullable AnalyticsResponseConversionRates conversionRates) {
    this.conversionRates = conversionRates;
    return this;
  }

  /**
   * Get conversionRates
   * @return conversionRates
   */
  @Valid 
  @Schema(name = "conversionRates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conversionRates")
  public @Nullable AnalyticsResponseConversionRates getConversionRates() {
    return conversionRates;
  }

  public void setConversionRates(@Nullable AnalyticsResponseConversionRates conversionRates) {
    this.conversionRates = conversionRates;
  }

  public AnalyticsResponse revenuePerReferral(@Nullable BigDecimal revenuePerReferral) {
    this.revenuePerReferral = revenuePerReferral;
    return this;
  }

  /**
   * Get revenuePerReferral
   * @return revenuePerReferral
   */
  @Valid 
  @Schema(name = "revenuePerReferral", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("revenuePerReferral")
  public @Nullable BigDecimal getRevenuePerReferral() {
    return revenuePerReferral;
  }

  public void setRevenuePerReferral(@Nullable BigDecimal revenuePerReferral) {
    this.revenuePerReferral = revenuePerReferral;
  }

  public AnalyticsResponse topChannels(List<@Valid AnalyticsResponseTopChannelsInner> topChannels) {
    this.topChannels = topChannels;
    return this;
  }

  public AnalyticsResponse addTopChannelsItem(AnalyticsResponseTopChannelsInner topChannelsItem) {
    if (this.topChannels == null) {
      this.topChannels = new ArrayList<>();
    }
    this.topChannels.add(topChannelsItem);
    return this;
  }

  /**
   * Get topChannels
   * @return topChannels
   */
  @Valid 
  @Schema(name = "topChannels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("topChannels")
  public List<@Valid AnalyticsResponseTopChannelsInner> getTopChannels() {
    return topChannels;
  }

  public void setTopChannels(List<@Valid AnalyticsResponseTopChannelsInner> topChannels) {
    this.topChannels = topChannels;
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
    return Objects.equals(this.range, analyticsResponse.range) &&
        Objects.equals(this.totals, analyticsResponse.totals) &&
        Objects.equals(this.conversionRates, analyticsResponse.conversionRates) &&
        Objects.equals(this.revenuePerReferral, analyticsResponse.revenuePerReferral) &&
        Objects.equals(this.topChannels, analyticsResponse.topChannels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(range, totals, conversionRates, revenuePerReferral, topChannels);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsResponse {\n");
    sb.append("    range: ").append(toIndentedString(range)).append("\n");
    sb.append("    totals: ").append(toIndentedString(totals)).append("\n");
    sb.append("    conversionRates: ").append(toIndentedString(conversionRates)).append("\n");
    sb.append("    revenuePerReferral: ").append(toIndentedString(revenuePerReferral)).append("\n");
    sb.append("    topChannels: ").append(toIndentedString(topChannels)).append("\n");
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

