package com.necpgame.gameplayservice.model;

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
 * AnalyticsItemMetric
 */


public class AnalyticsItemMetric {

  private @Nullable String itemId;

  private @Nullable String name;

  private @Nullable Integer purchases;

  private @Nullable BigDecimal equipRate;

  private @Nullable BigDecimal averageRevenue;

  public AnalyticsItemMetric itemId(@Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  
  @Schema(name = "itemId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("itemId")
  public @Nullable String getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable String itemId) {
    this.itemId = itemId;
  }

  public AnalyticsItemMetric name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public AnalyticsItemMetric purchases(@Nullable Integer purchases) {
    this.purchases = purchases;
    return this;
  }

  /**
   * Get purchases
   * @return purchases
   */
  
  @Schema(name = "purchases", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("purchases")
  public @Nullable Integer getPurchases() {
    return purchases;
  }

  public void setPurchases(@Nullable Integer purchases) {
    this.purchases = purchases;
  }

  public AnalyticsItemMetric equipRate(@Nullable BigDecimal equipRate) {
    this.equipRate = equipRate;
    return this;
  }

  /**
   * Get equipRate
   * @return equipRate
   */
  @Valid 
  @Schema(name = "equipRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("equipRate")
  public @Nullable BigDecimal getEquipRate() {
    return equipRate;
  }

  public void setEquipRate(@Nullable BigDecimal equipRate) {
    this.equipRate = equipRate;
  }

  public AnalyticsItemMetric averageRevenue(@Nullable BigDecimal averageRevenue) {
    this.averageRevenue = averageRevenue;
    return this;
  }

  /**
   * Get averageRevenue
   * @return averageRevenue
   */
  @Valid 
  @Schema(name = "averageRevenue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("averageRevenue")
  public @Nullable BigDecimal getAverageRevenue() {
    return averageRevenue;
  }

  public void setAverageRevenue(@Nullable BigDecimal averageRevenue) {
    this.averageRevenue = averageRevenue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsItemMetric analyticsItemMetric = (AnalyticsItemMetric) o;
    return Objects.equals(this.itemId, analyticsItemMetric.itemId) &&
        Objects.equals(this.name, analyticsItemMetric.name) &&
        Objects.equals(this.purchases, analyticsItemMetric.purchases) &&
        Objects.equals(this.equipRate, analyticsItemMetric.equipRate) &&
        Objects.equals(this.averageRevenue, analyticsItemMetric.averageRevenue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, name, purchases, equipRate, averageRevenue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsItemMetric {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    purchases: ").append(toIndentedString(purchases)).append("\n");
    sb.append("    equipRate: ").append(toIndentedString(equipRate)).append("\n");
    sb.append("    averageRevenue: ").append(toIndentedString(averageRevenue)).append("\n");
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

