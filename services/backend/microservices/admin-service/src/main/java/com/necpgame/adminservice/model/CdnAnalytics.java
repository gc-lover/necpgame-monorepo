package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CdnAnalytics
 */


public class CdnAnalytics {

  private @Nullable BigDecimal totalBandwidthGb;

  private @Nullable Integer totalRequests;

  private @Nullable BigDecimal cacheHitRate;

  private @Nullable Integer averageLatencyMs;

  public CdnAnalytics totalBandwidthGb(@Nullable BigDecimal totalBandwidthGb) {
    this.totalBandwidthGb = totalBandwidthGb;
    return this;
  }

  /**
   * Get totalBandwidthGb
   * @return totalBandwidthGb
   */
  @Valid 
  @Schema(name = "total_bandwidth_gb", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_bandwidth_gb")
  public @Nullable BigDecimal getTotalBandwidthGb() {
    return totalBandwidthGb;
  }

  public void setTotalBandwidthGb(@Nullable BigDecimal totalBandwidthGb) {
    this.totalBandwidthGb = totalBandwidthGb;
  }

  public CdnAnalytics totalRequests(@Nullable Integer totalRequests) {
    this.totalRequests = totalRequests;
    return this;
  }

  /**
   * Get totalRequests
   * @return totalRequests
   */
  
  @Schema(name = "total_requests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_requests")
  public @Nullable Integer getTotalRequests() {
    return totalRequests;
  }

  public void setTotalRequests(@Nullable Integer totalRequests) {
    this.totalRequests = totalRequests;
  }

  public CdnAnalytics cacheHitRate(@Nullable BigDecimal cacheHitRate) {
    this.cacheHitRate = cacheHitRate;
    return this;
  }

  /**
   * Get cacheHitRate
   * @return cacheHitRate
   */
  @Valid 
  @Schema(name = "cache_hit_rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cache_hit_rate")
  public @Nullable BigDecimal getCacheHitRate() {
    return cacheHitRate;
  }

  public void setCacheHitRate(@Nullable BigDecimal cacheHitRate) {
    this.cacheHitRate = cacheHitRate;
  }

  public CdnAnalytics averageLatencyMs(@Nullable Integer averageLatencyMs) {
    this.averageLatencyMs = averageLatencyMs;
    return this;
  }

  /**
   * Get averageLatencyMs
   * @return averageLatencyMs
   */
  
  @Schema(name = "average_latency_ms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("average_latency_ms")
  public @Nullable Integer getAverageLatencyMs() {
    return averageLatencyMs;
  }

  public void setAverageLatencyMs(@Nullable Integer averageLatencyMs) {
    this.averageLatencyMs = averageLatencyMs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CdnAnalytics cdnAnalytics = (CdnAnalytics) o;
    return Objects.equals(this.totalBandwidthGb, cdnAnalytics.totalBandwidthGb) &&
        Objects.equals(this.totalRequests, cdnAnalytics.totalRequests) &&
        Objects.equals(this.cacheHitRate, cdnAnalytics.cacheHitRate) &&
        Objects.equals(this.averageLatencyMs, cdnAnalytics.averageLatencyMs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalBandwidthGb, totalRequests, cacheHitRate, averageLatencyMs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CdnAnalytics {\n");
    sb.append("    totalBandwidthGb: ").append(toIndentedString(totalBandwidthGb)).append("\n");
    sb.append("    totalRequests: ").append(toIndentedString(totalRequests)).append("\n");
    sb.append("    cacheHitRate: ").append(toIndentedString(cacheHitRate)).append("\n");
    sb.append("    averageLatencyMs: ").append(toIndentedString(averageLatencyMs)).append("\n");
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

