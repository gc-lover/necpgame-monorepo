package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.ComponentDebt;
import com.necpgame.adminservice.model.TechnicalDebtCategory;
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
 * GetTechnicalDebt200Response
 */

@JsonTypeName("getTechnicalDebt_200_response")

public class GetTechnicalDebt200Response {

  private @Nullable Integer totalDebtHours;

  @Valid
  private List<@Valid TechnicalDebtCategory> debtByCategory = new ArrayList<>();

  @Valid
  private List<@Valid ComponentDebt> debtByComponent = new ArrayList<>();

  public GetTechnicalDebt200Response totalDebtHours(@Nullable Integer totalDebtHours) {
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

  public GetTechnicalDebt200Response debtByCategory(List<@Valid TechnicalDebtCategory> debtByCategory) {
    this.debtByCategory = debtByCategory;
    return this;
  }

  public GetTechnicalDebt200Response addDebtByCategoryItem(TechnicalDebtCategory debtByCategoryItem) {
    if (this.debtByCategory == null) {
      this.debtByCategory = new ArrayList<>();
    }
    this.debtByCategory.add(debtByCategoryItem);
    return this;
  }

  /**
   * Get debtByCategory
   * @return debtByCategory
   */
  @Valid 
  @Schema(name = "debt_by_category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("debt_by_category")
  public List<@Valid TechnicalDebtCategory> getDebtByCategory() {
    return debtByCategory;
  }

  public void setDebtByCategory(List<@Valid TechnicalDebtCategory> debtByCategory) {
    this.debtByCategory = debtByCategory;
  }

  public GetTechnicalDebt200Response debtByComponent(List<@Valid ComponentDebt> debtByComponent) {
    this.debtByComponent = debtByComponent;
    return this;
  }

  public GetTechnicalDebt200Response addDebtByComponentItem(ComponentDebt debtByComponentItem) {
    if (this.debtByComponent == null) {
      this.debtByComponent = new ArrayList<>();
    }
    this.debtByComponent.add(debtByComponentItem);
    return this;
  }

  /**
   * Get debtByComponent
   * @return debtByComponent
   */
  @Valid 
  @Schema(name = "debt_by_component", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("debt_by_component")
  public List<@Valid ComponentDebt> getDebtByComponent() {
    return debtByComponent;
  }

  public void setDebtByComponent(List<@Valid ComponentDebt> debtByComponent) {
    this.debtByComponent = debtByComponent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetTechnicalDebt200Response getTechnicalDebt200Response = (GetTechnicalDebt200Response) o;
    return Objects.equals(this.totalDebtHours, getTechnicalDebt200Response.totalDebtHours) &&
        Objects.equals(this.debtByCategory, getTechnicalDebt200Response.debtByCategory) &&
        Objects.equals(this.debtByComponent, getTechnicalDebt200Response.debtByComponent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalDebtHours, debtByCategory, debtByComponent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetTechnicalDebt200Response {\n");
    sb.append("    totalDebtHours: ").append(toIndentedString(totalDebtHours)).append("\n");
    sb.append("    debtByCategory: ").append(toIndentedString(debtByCategory)).append("\n");
    sb.append("    debtByComponent: ").append(toIndentedString(debtByComponent)).append("\n");
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

