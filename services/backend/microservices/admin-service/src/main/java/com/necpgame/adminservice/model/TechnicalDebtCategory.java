package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.TechnicalDebtCategoryPriorityDistribution;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TechnicalDebtCategory
 */


public class TechnicalDebtCategory {

  /**
   * Gets or Sets category
   */
  public enum CategoryEnum {
    SECURITY("security"),
    
    PERFORMANCE("performance"),
    
    SCALABILITY("scalability"),
    
    MAINTAINABILITY("maintainability"),
    
    TESTING("testing");

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

  private @Nullable Integer debtHours;

  private @Nullable Integer itemsCount;

  private @Nullable TechnicalDebtCategoryPriorityDistribution priorityDistribution;

  public TechnicalDebtCategory category(@Nullable CategoryEnum category) {
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

  public TechnicalDebtCategory debtHours(@Nullable Integer debtHours) {
    this.debtHours = debtHours;
    return this;
  }

  /**
   * Время на устранение (часы)
   * @return debtHours
   */
  
  @Schema(name = "debt_hours", description = "Время на устранение (часы)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("debt_hours")
  public @Nullable Integer getDebtHours() {
    return debtHours;
  }

  public void setDebtHours(@Nullable Integer debtHours) {
    this.debtHours = debtHours;
  }

  public TechnicalDebtCategory itemsCount(@Nullable Integer itemsCount) {
    this.itemsCount = itemsCount;
    return this;
  }

  /**
   * Количество items
   * @return itemsCount
   */
  
  @Schema(name = "items_count", description = "Количество items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items_count")
  public @Nullable Integer getItemsCount() {
    return itemsCount;
  }

  public void setItemsCount(@Nullable Integer itemsCount) {
    this.itemsCount = itemsCount;
  }

  public TechnicalDebtCategory priorityDistribution(@Nullable TechnicalDebtCategoryPriorityDistribution priorityDistribution) {
    this.priorityDistribution = priorityDistribution;
    return this;
  }

  /**
   * Get priorityDistribution
   * @return priorityDistribution
   */
  @Valid 
  @Schema(name = "priority_distribution", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority_distribution")
  public @Nullable TechnicalDebtCategoryPriorityDistribution getPriorityDistribution() {
    return priorityDistribution;
  }

  public void setPriorityDistribution(@Nullable TechnicalDebtCategoryPriorityDistribution priorityDistribution) {
    this.priorityDistribution = priorityDistribution;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TechnicalDebtCategory technicalDebtCategory = (TechnicalDebtCategory) o;
    return Objects.equals(this.category, technicalDebtCategory.category) &&
        Objects.equals(this.debtHours, technicalDebtCategory.debtHours) &&
        Objects.equals(this.itemsCount, technicalDebtCategory.itemsCount) &&
        Objects.equals(this.priorityDistribution, technicalDebtCategory.priorityDistribution);
  }

  @Override
  public int hashCode() {
    return Objects.hash(category, debtHours, itemsCount, priorityDistribution);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TechnicalDebtCategory {\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    debtHours: ").append(toIndentedString(debtHours)).append("\n");
    sb.append("    itemsCount: ").append(toIndentedString(itemsCount)).append("\n");
    sb.append("    priorityDistribution: ").append(toIndentedString(priorityDistribution)).append("\n");
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

