package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.TechnicalDebtSummaryByCategory;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TechnicalDebtSummary
 */


public class TechnicalDebtSummary {

  private @Nullable Integer totalDebtHours;

  private @Nullable Integer criticalDebtItems;

  private @Nullable TechnicalDebtSummaryByCategory byCategory;

  public TechnicalDebtSummary totalDebtHours(@Nullable Integer totalDebtHours) {
    this.totalDebtHours = totalDebtHours;
    return this;
  }

  /**
   * Общее время на устранение (часы)
   * @return totalDebtHours
   */
  
  @Schema(name = "total_debt_hours", example = "120", description = "Общее время на устранение (часы)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_debt_hours")
  public @Nullable Integer getTotalDebtHours() {
    return totalDebtHours;
  }

  public void setTotalDebtHours(@Nullable Integer totalDebtHours) {
    this.totalDebtHours = totalDebtHours;
  }

  public TechnicalDebtSummary criticalDebtItems(@Nullable Integer criticalDebtItems) {
    this.criticalDebtItems = criticalDebtItems;
    return this;
  }

  /**
   * Get criticalDebtItems
   * @return criticalDebtItems
   */
  
  @Schema(name = "critical_debt_items", example = "2", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("critical_debt_items")
  public @Nullable Integer getCriticalDebtItems() {
    return criticalDebtItems;
  }

  public void setCriticalDebtItems(@Nullable Integer criticalDebtItems) {
    this.criticalDebtItems = criticalDebtItems;
  }

  public TechnicalDebtSummary byCategory(@Nullable TechnicalDebtSummaryByCategory byCategory) {
    this.byCategory = byCategory;
    return this;
  }

  /**
   * Get byCategory
   * @return byCategory
   */
  @Valid 
  @Schema(name = "by_category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("by_category")
  public @Nullable TechnicalDebtSummaryByCategory getByCategory() {
    return byCategory;
  }

  public void setByCategory(@Nullable TechnicalDebtSummaryByCategory byCategory) {
    this.byCategory = byCategory;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TechnicalDebtSummary technicalDebtSummary = (TechnicalDebtSummary) o;
    return Objects.equals(this.totalDebtHours, technicalDebtSummary.totalDebtHours) &&
        Objects.equals(this.criticalDebtItems, technicalDebtSummary.criticalDebtItems) &&
        Objects.equals(this.byCategory, technicalDebtSummary.byCategory);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalDebtHours, criticalDebtItems, byCategory);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TechnicalDebtSummary {\n");
    sb.append("    totalDebtHours: ").append(toIndentedString(totalDebtHours)).append("\n");
    sb.append("    criticalDebtItems: ").append(toIndentedString(criticalDebtItems)).append("\n");
    sb.append("    byCategory: ").append(toIndentedString(byCategory)).append("\n");
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

