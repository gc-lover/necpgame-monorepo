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
 * CacheMetrics
 */


public class CacheMetrics {

  private @Nullable String timeRange;

  private @Nullable Integer totalRequests;

  private @Nullable Integer cacheHits;

  private @Nullable Integer cacheMisses;

  private @Nullable BigDecimal hitRate;

  private @Nullable Integer averageLatencyMs;

  @Valid
  private Map<String, Object> byLayer = new HashMap<>();

  public CacheMetrics timeRange(@Nullable String timeRange) {
    this.timeRange = timeRange;
    return this;
  }

  /**
   * Get timeRange
   * @return timeRange
   */
  
  @Schema(name = "time_range", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_range")
  public @Nullable String getTimeRange() {
    return timeRange;
  }

  public void setTimeRange(@Nullable String timeRange) {
    this.timeRange = timeRange;
  }

  public CacheMetrics totalRequests(@Nullable Integer totalRequests) {
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

  public CacheMetrics cacheHits(@Nullable Integer cacheHits) {
    this.cacheHits = cacheHits;
    return this;
  }

  /**
   * Get cacheHits
   * @return cacheHits
   */
  
  @Schema(name = "cache_hits", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cache_hits")
  public @Nullable Integer getCacheHits() {
    return cacheHits;
  }

  public void setCacheHits(@Nullable Integer cacheHits) {
    this.cacheHits = cacheHits;
  }

  public CacheMetrics cacheMisses(@Nullable Integer cacheMisses) {
    this.cacheMisses = cacheMisses;
    return this;
  }

  /**
   * Get cacheMisses
   * @return cacheMisses
   */
  
  @Schema(name = "cache_misses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cache_misses")
  public @Nullable Integer getCacheMisses() {
    return cacheMisses;
  }

  public void setCacheMisses(@Nullable Integer cacheMisses) {
    this.cacheMisses = cacheMisses;
  }

  public CacheMetrics hitRate(@Nullable BigDecimal hitRate) {
    this.hitRate = hitRate;
    return this;
  }

  /**
   * Get hitRate
   * @return hitRate
   */
  @Valid 
  @Schema(name = "hit_rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hit_rate")
  public @Nullable BigDecimal getHitRate() {
    return hitRate;
  }

  public void setHitRate(@Nullable BigDecimal hitRate) {
    this.hitRate = hitRate;
  }

  public CacheMetrics averageLatencyMs(@Nullable Integer averageLatencyMs) {
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

  public CacheMetrics byLayer(Map<String, Object> byLayer) {
    this.byLayer = byLayer;
    return this;
  }

  public CacheMetrics putByLayerItem(String key, Object byLayerItem) {
    if (this.byLayer == null) {
      this.byLayer = new HashMap<>();
    }
    this.byLayer.put(key, byLayerItem);
    return this;
  }

  /**
   * Get byLayer
   * @return byLayer
   */
  
  @Schema(name = "by_layer", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("by_layer")
  public Map<String, Object> getByLayer() {
    return byLayer;
  }

  public void setByLayer(Map<String, Object> byLayer) {
    this.byLayer = byLayer;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CacheMetrics cacheMetrics = (CacheMetrics) o;
    return Objects.equals(this.timeRange, cacheMetrics.timeRange) &&
        Objects.equals(this.totalRequests, cacheMetrics.totalRequests) &&
        Objects.equals(this.cacheHits, cacheMetrics.cacheHits) &&
        Objects.equals(this.cacheMisses, cacheMetrics.cacheMisses) &&
        Objects.equals(this.hitRate, cacheMetrics.hitRate) &&
        Objects.equals(this.averageLatencyMs, cacheMetrics.averageLatencyMs) &&
        Objects.equals(this.byLayer, cacheMetrics.byLayer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeRange, totalRequests, cacheHits, cacheMisses, hitRate, averageLatencyMs, byLayer);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CacheMetrics {\n");
    sb.append("    timeRange: ").append(toIndentedString(timeRange)).append("\n");
    sb.append("    totalRequests: ").append(toIndentedString(totalRequests)).append("\n");
    sb.append("    cacheHits: ").append(toIndentedString(cacheHits)).append("\n");
    sb.append("    cacheMisses: ").append(toIndentedString(cacheMisses)).append("\n");
    sb.append("    hitRate: ").append(toIndentedString(hitRate)).append("\n");
    sb.append("    averageLatencyMs: ").append(toIndentedString(averageLatencyMs)).append("\n");
    sb.append("    byLayer: ").append(toIndentedString(byLayer)).append("\n");
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

