package com.necpgame.economyservice.model;

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
 * ProductionChain
 */


public class ProductionChain {

  private @Nullable String chainId;

  private @Nullable String name;

  /**
   * Gets or Sets category
   */
  public enum CategoryEnum {
    WEAPONS("WEAPONS"),
    
    ARMOR("ARMOR"),
    
    IMPLANTS("IMPLANTS"),
    
    CONSUMABLES("CONSUMABLES");

    private final String value;

    CategoryEnum(String value) {
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
    public static CategoryEnum fromValue(String value) {
      for (CategoryEnum b : CategoryEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable CategoryEnum category;

  private @Nullable Integer stagesCount;

  private @Nullable BigDecimal totalTimeHours;

  private @Nullable Float estimatedProfitMargin;

  public ProductionChain chainId(@Nullable String chainId) {
    this.chainId = chainId;
    return this;
  }

  /**
   * Get chainId
   * @return chainId
   */
  
  @Schema(name = "chain_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("chain_id")
  public @Nullable String getChainId() {
    return chainId;
  }

  public void setChainId(@Nullable String chainId) {
    this.chainId = chainId;
  }

  public ProductionChain name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", example = "Legendary Weapons Chain", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public ProductionChain category(@Nullable CategoryEnum category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable CategoryEnum getCategory() {
    return category;
  }

  public void setCategory(@Nullable CategoryEnum category) {
    this.category = category;
  }

  public ProductionChain stagesCount(@Nullable Integer stagesCount) {
    this.stagesCount = stagesCount;
    return this;
  }

  /**
   * Get stagesCount
   * @return stagesCount
   */
  
  @Schema(name = "stages_count", example = "5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stages_count")
  public @Nullable Integer getStagesCount() {
    return stagesCount;
  }

  public void setStagesCount(@Nullable Integer stagesCount) {
    this.stagesCount = stagesCount;
  }

  public ProductionChain totalTimeHours(@Nullable BigDecimal totalTimeHours) {
    this.totalTimeHours = totalTimeHours;
    return this;
  }

  /**
   * Get totalTimeHours
   * @return totalTimeHours
   */
  @Valid 
  @Schema(name = "total_time_hours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_time_hours")
  public @Nullable BigDecimal getTotalTimeHours() {
    return totalTimeHours;
  }

  public void setTotalTimeHours(@Nullable BigDecimal totalTimeHours) {
    this.totalTimeHours = totalTimeHours;
  }

  public ProductionChain estimatedProfitMargin(@Nullable Float estimatedProfitMargin) {
    this.estimatedProfitMargin = estimatedProfitMargin;
    return this;
  }

  /**
   * Get estimatedProfitMargin
   * @return estimatedProfitMargin
   */
  
  @Schema(name = "estimated_profit_margin", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_profit_margin")
  public @Nullable Float getEstimatedProfitMargin() {
    return estimatedProfitMargin;
  }

  public void setEstimatedProfitMargin(@Nullable Float estimatedProfitMargin) {
    this.estimatedProfitMargin = estimatedProfitMargin;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductionChain productionChain = (ProductionChain) o;
    return Objects.equals(this.chainId, productionChain.chainId) &&
        Objects.equals(this.name, productionChain.name) &&
        Objects.equals(this.category, productionChain.category) &&
        Objects.equals(this.stagesCount, productionChain.stagesCount) &&
        Objects.equals(this.totalTimeHours, productionChain.totalTimeHours) &&
        Objects.equals(this.estimatedProfitMargin, productionChain.estimatedProfitMargin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(chainId, name, category, stagesCount, totalTimeHours, estimatedProfitMargin);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductionChain {\n");
    sb.append("    chainId: ").append(toIndentedString(chainId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    stagesCount: ").append(toIndentedString(stagesCount)).append("\n");
    sb.append("    totalTimeHours: ").append(toIndentedString(totalTimeHours)).append("\n");
    sb.append("    estimatedProfitMargin: ").append(toIndentedString(estimatedProfitMargin)).append("\n");
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

