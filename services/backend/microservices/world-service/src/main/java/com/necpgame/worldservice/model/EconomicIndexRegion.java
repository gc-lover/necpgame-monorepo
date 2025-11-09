package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EconomicIndexRegion
 */


public class EconomicIndexRegion {

  private String regionId;

  private Float orderEconomicIndex;

  private Float serviceDemandIndex;

  private @Nullable Float volatility;

  /**
   * Gets or Sets trend
   */
  public enum TrendEnum {
    UP("up"),
    
    DOWN("down"),
    
    STABLE("stable");

    private final String value;

    TrendEnum(String value) {
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
    public static TrendEnum fromValue(String value) {
      for (TrendEnum b : TrendEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TrendEnum trend;

  public EconomicIndexRegion() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EconomicIndexRegion(String regionId, Float orderEconomicIndex, Float serviceDemandIndex) {
    this.regionId = regionId;
    this.orderEconomicIndex = orderEconomicIndex;
    this.serviceDemandIndex = serviceDemandIndex;
  }

  public EconomicIndexRegion regionId(String regionId) {
    this.regionId = regionId;
    return this;
  }

  /**
   * Get regionId
   * @return regionId
   */
  @NotNull 
  @Schema(name = "regionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("regionId")
  public String getRegionId() {
    return regionId;
  }

  public void setRegionId(String regionId) {
    this.regionId = regionId;
  }

  public EconomicIndexRegion orderEconomicIndex(Float orderEconomicIndex) {
    this.orderEconomicIndex = orderEconomicIndex;
    return this;
  }

  /**
   * Get orderEconomicIndex
   * @return orderEconomicIndex
   */
  @NotNull 
  @Schema(name = "orderEconomicIndex", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("orderEconomicIndex")
  public Float getOrderEconomicIndex() {
    return orderEconomicIndex;
  }

  public void setOrderEconomicIndex(Float orderEconomicIndex) {
    this.orderEconomicIndex = orderEconomicIndex;
  }

  public EconomicIndexRegion serviceDemandIndex(Float serviceDemandIndex) {
    this.serviceDemandIndex = serviceDemandIndex;
    return this;
  }

  /**
   * Get serviceDemandIndex
   * @return serviceDemandIndex
   */
  @NotNull 
  @Schema(name = "serviceDemandIndex", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("serviceDemandIndex")
  public Float getServiceDemandIndex() {
    return serviceDemandIndex;
  }

  public void setServiceDemandIndex(Float serviceDemandIndex) {
    this.serviceDemandIndex = serviceDemandIndex;
  }

  public EconomicIndexRegion volatility(@Nullable Float volatility) {
    this.volatility = volatility;
    return this;
  }

  /**
   * Get volatility
   * @return volatility
   */
  
  @Schema(name = "volatility", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("volatility")
  public @Nullable Float getVolatility() {
    return volatility;
  }

  public void setVolatility(@Nullable Float volatility) {
    this.volatility = volatility;
  }

  public EconomicIndexRegion trend(@Nullable TrendEnum trend) {
    this.trend = trend;
    return this;
  }

  /**
   * Get trend
   * @return trend
   */
  
  @Schema(name = "trend", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trend")
  public @Nullable TrendEnum getTrend() {
    return trend;
  }

  public void setTrend(@Nullable TrendEnum trend) {
    this.trend = trend;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EconomicIndexRegion economicIndexRegion = (EconomicIndexRegion) o;
    return Objects.equals(this.regionId, economicIndexRegion.regionId) &&
        Objects.equals(this.orderEconomicIndex, economicIndexRegion.orderEconomicIndex) &&
        Objects.equals(this.serviceDemandIndex, economicIndexRegion.serviceDemandIndex) &&
        Objects.equals(this.volatility, economicIndexRegion.volatility) &&
        Objects.equals(this.trend, economicIndexRegion.trend);
  }

  @Override
  public int hashCode() {
    return Objects.hash(regionId, orderEconomicIndex, serviceDemandIndex, volatility, trend);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EconomicIndexRegion {\n");
    sb.append("    regionId: ").append(toIndentedString(regionId)).append("\n");
    sb.append("    orderEconomicIndex: ").append(toIndentedString(orderEconomicIndex)).append("\n");
    sb.append("    serviceDemandIndex: ").append(toIndentedString(serviceDemandIndex)).append("\n");
    sb.append("    volatility: ").append(toIndentedString(volatility)).append("\n");
    sb.append("    trend: ").append(toIndentedString(trend)).append("\n");
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

