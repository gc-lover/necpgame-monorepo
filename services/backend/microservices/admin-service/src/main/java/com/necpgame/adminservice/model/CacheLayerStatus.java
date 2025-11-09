package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * CacheLayerStatus
 */


public class CacheLayerStatus {

  private @Nullable String layerName;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    OPERATIONAL("operational"),
    
    DEGRADED("degraded"),
    
    DOWN("down");

    private final String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  private @Nullable Integer memoryUsed;

  private @Nullable Integer memoryTotal;

  private @Nullable Integer keysCount;

  private @Nullable BigDecimal hitRate;

  public CacheLayerStatus layerName(@Nullable String layerName) {
    this.layerName = layerName;
    return this;
  }

  /**
   * Get layerName
   * @return layerName
   */
  
  @Schema(name = "layer_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("layer_name")
  public @Nullable String getLayerName() {
    return layerName;
  }

  public void setLayerName(@Nullable String layerName) {
    this.layerName = layerName;
  }

  public CacheLayerStatus status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public CacheLayerStatus memoryUsed(@Nullable Integer memoryUsed) {
    this.memoryUsed = memoryUsed;
    return this;
  }

  /**
   * Memory in MB
   * @return memoryUsed
   */
  
  @Schema(name = "memory_used", description = "Memory in MB", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("memory_used")
  public @Nullable Integer getMemoryUsed() {
    return memoryUsed;
  }

  public void setMemoryUsed(@Nullable Integer memoryUsed) {
    this.memoryUsed = memoryUsed;
  }

  public CacheLayerStatus memoryTotal(@Nullable Integer memoryTotal) {
    this.memoryTotal = memoryTotal;
    return this;
  }

  /**
   * Get memoryTotal
   * @return memoryTotal
   */
  
  @Schema(name = "memory_total", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("memory_total")
  public @Nullable Integer getMemoryTotal() {
    return memoryTotal;
  }

  public void setMemoryTotal(@Nullable Integer memoryTotal) {
    this.memoryTotal = memoryTotal;
  }

  public CacheLayerStatus keysCount(@Nullable Integer keysCount) {
    this.keysCount = keysCount;
    return this;
  }

  /**
   * Get keysCount
   * @return keysCount
   */
  
  @Schema(name = "keys_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("keys_count")
  public @Nullable Integer getKeysCount() {
    return keysCount;
  }

  public void setKeysCount(@Nullable Integer keysCount) {
    this.keysCount = keysCount;
  }

  public CacheLayerStatus hitRate(@Nullable BigDecimal hitRate) {
    this.hitRate = hitRate;
    return this;
  }

  /**
   * Cache hit rate (0-1)
   * @return hitRate
   */
  @Valid 
  @Schema(name = "hit_rate", description = "Cache hit rate (0-1)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hit_rate")
  public @Nullable BigDecimal getHitRate() {
    return hitRate;
  }

  public void setHitRate(@Nullable BigDecimal hitRate) {
    this.hitRate = hitRate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CacheLayerStatus cacheLayerStatus = (CacheLayerStatus) o;
    return Objects.equals(this.layerName, cacheLayerStatus.layerName) &&
        Objects.equals(this.status, cacheLayerStatus.status) &&
        Objects.equals(this.memoryUsed, cacheLayerStatus.memoryUsed) &&
        Objects.equals(this.memoryTotal, cacheLayerStatus.memoryTotal) &&
        Objects.equals(this.keysCount, cacheLayerStatus.keysCount) &&
        Objects.equals(this.hitRate, cacheLayerStatus.hitRate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(layerName, status, memoryUsed, memoryTotal, keysCount, hitRate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CacheLayerStatus {\n");
    sb.append("    layerName: ").append(toIndentedString(layerName)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    memoryUsed: ").append(toIndentedString(memoryUsed)).append("\n");
    sb.append("    memoryTotal: ").append(toIndentedString(memoryTotal)).append("\n");
    sb.append("    keysCount: ").append(toIndentedString(keysCount)).append("\n");
    sb.append("    hitRate: ").append(toIndentedString(hitRate)).append("\n");
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

