package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PriceTrend
 */


public class PriceTrend {

  private @Nullable UUID itemId;

  private @Nullable String itemName;

  private @Nullable String category;

  /**
   * Gets or Sets trend
   */
  public enum TrendEnum {
    RISING("RISING"),
    
    FALLING("FALLING"),
    
    STABLE("STABLE");

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

  private @Nullable Float changePercentage;

  private @Nullable BigDecimal change7d;

  private @Nullable BigDecimal change30d;

  private @Nullable String reason;

  public PriceTrend itemId(@Nullable UUID itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @Valid 
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_id")
  public @Nullable UUID getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable UUID itemId) {
    this.itemId = itemId;
  }

  public PriceTrend itemName(@Nullable String itemName) {
    this.itemName = itemName;
    return this;
  }

  /**
   * Get itemName
   * @return itemName
   */
  
  @Schema(name = "item_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_name")
  public @Nullable String getItemName() {
    return itemName;
  }

  public void setItemName(@Nullable String itemName) {
    this.itemName = itemName;
  }

  public PriceTrend category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  public PriceTrend trend(@Nullable TrendEnum trend) {
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

  public PriceTrend changePercentage(@Nullable Float changePercentage) {
    this.changePercentage = changePercentage;
    return this;
  }

  /**
   * Get changePercentage
   * @return changePercentage
   */
  
  @Schema(name = "change_percentage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("change_percentage")
  public @Nullable Float getChangePercentage() {
    return changePercentage;
  }

  public void setChangePercentage(@Nullable Float changePercentage) {
    this.changePercentage = changePercentage;
  }

  public PriceTrend change7d(@Nullable BigDecimal change7d) {
    this.change7d = change7d;
    return this;
  }

  /**
   * Get change7d
   * @return change7d
   */
  @Valid 
  @Schema(name = "change_7d", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("change_7d")
  public @Nullable BigDecimal getChange7d() {
    return change7d;
  }

  public void setChange7d(@Nullable BigDecimal change7d) {
    this.change7d = change7d;
  }

  public PriceTrend change30d(@Nullable BigDecimal change30d) {
    this.change30d = change30d;
    return this;
  }

  /**
   * Get change30d
   * @return change30d
   */
  @Valid 
  @Schema(name = "change_30d", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("change_30d")
  public @Nullable BigDecimal getChange30d() {
    return change30d;
  }

  public void setChange30d(@Nullable BigDecimal change30d) {
    this.change30d = change30d;
  }

  public PriceTrend reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Причина тренда
   * @return reason
   */
  
  @Schema(name = "reason", description = "Причина тренда", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PriceTrend priceTrend = (PriceTrend) o;
    return Objects.equals(this.itemId, priceTrend.itemId) &&
        Objects.equals(this.itemName, priceTrend.itemName) &&
        Objects.equals(this.category, priceTrend.category) &&
        Objects.equals(this.trend, priceTrend.trend) &&
        Objects.equals(this.changePercentage, priceTrend.changePercentage) &&
        Objects.equals(this.change7d, priceTrend.change7d) &&
        Objects.equals(this.change30d, priceTrend.change30d) &&
        Objects.equals(this.reason, priceTrend.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, itemName, category, trend, changePercentage, change7d, change30d, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PriceTrend {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    itemName: ").append(toIndentedString(itemName)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    trend: ").append(toIndentedString(trend)).append("\n");
    sb.append("    changePercentage: ").append(toIndentedString(changePercentage)).append("\n");
    sb.append("    change7d: ").append(toIndentedString(change7d)).append("\n");
    sb.append("    change30d: ").append(toIndentedString(change30d)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

