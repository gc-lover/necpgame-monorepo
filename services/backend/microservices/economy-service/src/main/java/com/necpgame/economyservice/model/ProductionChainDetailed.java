package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.ProductionChainDetailedAllOfFinalProduct;
import com.necpgame.economyservice.model.ProductionStage;
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
 * ProductionChainDetailed
 */


public class ProductionChainDetailed {

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

  @Valid
  private List<@Valid ProductionStage> stages = new ArrayList<>();

  private @Nullable ProductionChainDetailedAllOfFinalProduct finalProduct;

  public ProductionChainDetailed chainId(@Nullable String chainId) {
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

  public ProductionChainDetailed name(@Nullable String name) {
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

  public ProductionChainDetailed category(@Nullable CategoryEnum category) {
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

  public ProductionChainDetailed stagesCount(@Nullable Integer stagesCount) {
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

  public ProductionChainDetailed totalTimeHours(@Nullable BigDecimal totalTimeHours) {
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

  public ProductionChainDetailed estimatedProfitMargin(@Nullable Float estimatedProfitMargin) {
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

  public ProductionChainDetailed stages(List<@Valid ProductionStage> stages) {
    this.stages = stages;
    return this;
  }

  public ProductionChainDetailed addStagesItem(ProductionStage stagesItem) {
    if (this.stages == null) {
      this.stages = new ArrayList<>();
    }
    this.stages.add(stagesItem);
    return this;
  }

  /**
   * Get stages
   * @return stages
   */
  @Valid 
  @Schema(name = "stages", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stages")
  public List<@Valid ProductionStage> getStages() {
    return stages;
  }

  public void setStages(List<@Valid ProductionStage> stages) {
    this.stages = stages;
  }

  public ProductionChainDetailed finalProduct(@Nullable ProductionChainDetailedAllOfFinalProduct finalProduct) {
    this.finalProduct = finalProduct;
    return this;
  }

  /**
   * Get finalProduct
   * @return finalProduct
   */
  @Valid 
  @Schema(name = "final_product", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("final_product")
  public @Nullable ProductionChainDetailedAllOfFinalProduct getFinalProduct() {
    return finalProduct;
  }

  public void setFinalProduct(@Nullable ProductionChainDetailedAllOfFinalProduct finalProduct) {
    this.finalProduct = finalProduct;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductionChainDetailed productionChainDetailed = (ProductionChainDetailed) o;
    return Objects.equals(this.chainId, productionChainDetailed.chainId) &&
        Objects.equals(this.name, productionChainDetailed.name) &&
        Objects.equals(this.category, productionChainDetailed.category) &&
        Objects.equals(this.stagesCount, productionChainDetailed.stagesCount) &&
        Objects.equals(this.totalTimeHours, productionChainDetailed.totalTimeHours) &&
        Objects.equals(this.estimatedProfitMargin, productionChainDetailed.estimatedProfitMargin) &&
        Objects.equals(this.stages, productionChainDetailed.stages) &&
        Objects.equals(this.finalProduct, productionChainDetailed.finalProduct);
  }

  @Override
  public int hashCode() {
    return Objects.hash(chainId, name, category, stagesCount, totalTimeHours, estimatedProfitMargin, stages, finalProduct);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductionChainDetailed {\n");
    sb.append("    chainId: ").append(toIndentedString(chainId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    stagesCount: ").append(toIndentedString(stagesCount)).append("\n");
    sb.append("    totalTimeHours: ").append(toIndentedString(totalTimeHours)).append("\n");
    sb.append("    estimatedProfitMargin: ").append(toIndentedString(estimatedProfitMargin)).append("\n");
    sb.append("    stages: ").append(toIndentedString(stages)).append("\n");
    sb.append("    finalProduct: ").append(toIndentedString(finalProduct)).append("\n");
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

