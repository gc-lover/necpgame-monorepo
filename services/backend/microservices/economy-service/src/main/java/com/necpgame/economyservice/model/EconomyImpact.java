package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
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
 * EconomyImpact
 */


public class EconomyImpact {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  private @Nullable Integer activeEventsCount;

  /**
   * Gets or Sets overallMarketHealth
   */
  public enum OverallMarketHealthEnum {
    STRONG("STRONG"),
    
    STABLE("STABLE"),
    
    WEAK("WEAK"),
    
    CRISIS("CRISIS");

    private final String value;

    OverallMarketHealthEnum(String value) {
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
    public static OverallMarketHealthEnum fromValue(String value) {
      for (OverallMarketHealthEnum b : OverallMarketHealthEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable OverallMarketHealthEnum overallMarketHealth;

  private @Nullable Float priceIndexChange;

  @Valid
  private Map<String, BigDecimal> sectorImpacts = new HashMap<>();

  public EconomyImpact timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public EconomyImpact activeEventsCount(@Nullable Integer activeEventsCount) {
    this.activeEventsCount = activeEventsCount;
    return this;
  }

  /**
   * Get activeEventsCount
   * @return activeEventsCount
   */
  
  @Schema(name = "active_events_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_events_count")
  public @Nullable Integer getActiveEventsCount() {
    return activeEventsCount;
  }

  public void setActiveEventsCount(@Nullable Integer activeEventsCount) {
    this.activeEventsCount = activeEventsCount;
  }

  public EconomyImpact overallMarketHealth(@Nullable OverallMarketHealthEnum overallMarketHealth) {
    this.overallMarketHealth = overallMarketHealth;
    return this;
  }

  /**
   * Get overallMarketHealth
   * @return overallMarketHealth
   */
  
  @Schema(name = "overall_market_health", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("overall_market_health")
  public @Nullable OverallMarketHealthEnum getOverallMarketHealth() {
    return overallMarketHealth;
  }

  public void setOverallMarketHealth(@Nullable OverallMarketHealthEnum overallMarketHealth) {
    this.overallMarketHealth = overallMarketHealth;
  }

  public EconomyImpact priceIndexChange(@Nullable Float priceIndexChange) {
    this.priceIndexChange = priceIndexChange;
    return this;
  }

  /**
   * Общее изменение цен (%)
   * @return priceIndexChange
   */
  
  @Schema(name = "price_index_change", description = "Общее изменение цен (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price_index_change")
  public @Nullable Float getPriceIndexChange() {
    return priceIndexChange;
  }

  public void setPriceIndexChange(@Nullable Float priceIndexChange) {
    this.priceIndexChange = priceIndexChange;
  }

  public EconomyImpact sectorImpacts(Map<String, BigDecimal> sectorImpacts) {
    this.sectorImpacts = sectorImpacts;
    return this;
  }

  public EconomyImpact putSectorImpactsItem(String key, BigDecimal sectorImpactsItem) {
    if (this.sectorImpacts == null) {
      this.sectorImpacts = new HashMap<>();
    }
    this.sectorImpacts.put(key, sectorImpactsItem);
    return this;
  }

  /**
   * Get sectorImpacts
   * @return sectorImpacts
   */
  @Valid 
  @Schema(name = "sector_impacts", example = "{\"weapons\":1.25,\"cyberware\":0.85,\"food\":1.1}", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sector_impacts")
  public Map<String, BigDecimal> getSectorImpacts() {
    return sectorImpacts;
  }

  public void setSectorImpacts(Map<String, BigDecimal> sectorImpacts) {
    this.sectorImpacts = sectorImpacts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EconomyImpact economyImpact = (EconomyImpact) o;
    return Objects.equals(this.timestamp, economyImpact.timestamp) &&
        Objects.equals(this.activeEventsCount, economyImpact.activeEventsCount) &&
        Objects.equals(this.overallMarketHealth, economyImpact.overallMarketHealth) &&
        Objects.equals(this.priceIndexChange, economyImpact.priceIndexChange) &&
        Objects.equals(this.sectorImpacts, economyImpact.sectorImpacts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, activeEventsCount, overallMarketHealth, priceIndexChange, sectorImpacts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EconomyImpact {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    activeEventsCount: ").append(toIndentedString(activeEventsCount)).append("\n");
    sb.append("    overallMarketHealth: ").append(toIndentedString(overallMarketHealth)).append("\n");
    sb.append("    priceIndexChange: ").append(toIndentedString(priceIndexChange)).append("\n");
    sb.append("    sectorImpacts: ").append(toIndentedString(sectorImpacts)).append("\n");
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

