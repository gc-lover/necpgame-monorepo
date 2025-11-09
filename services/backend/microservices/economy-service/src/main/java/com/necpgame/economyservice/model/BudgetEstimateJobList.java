package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.BudgetEstimateJob;
import com.necpgame.economyservice.model.PaginationMeta;
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
 * BudgetEstimateJobList
 */


public class BudgetEstimateJobList {

  @Valid
  private List<@Valid BudgetEstimateJob> data = new ArrayList<>();

  private PaginationMeta pagination;

  public BudgetEstimateJobList() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BudgetEstimateJobList(List<@Valid BudgetEstimateJob> data, PaginationMeta pagination) {
    this.data = data;
    this.pagination = pagination;
  }

  public BudgetEstimateJobList data(List<@Valid BudgetEstimateJob> data) {
    this.data = data;
    return this;
  }

  public BudgetEstimateJobList addDataItem(BudgetEstimateJob dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<>();
    }
    this.data.add(dataItem);
    return this;
  }

  /**
   * Список задач расчёта бюджета.
   * @return data
   */
  @NotNull @Valid 
  @Schema(name = "data", description = "Список задач расчёта бюджета.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("data")
  public List<@Valid BudgetEstimateJob> getData() {
    return data;
  }

  public void setData(List<@Valid BudgetEstimateJob> data) {
    this.data = data;
  }

  public BudgetEstimateJobList pagination(PaginationMeta pagination) {
    this.pagination = pagination;
    return this;
  }

  /**
   * Get pagination
   * @return pagination
   */
  @NotNull @Valid 
  @Schema(name = "pagination", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("pagination")
  public PaginationMeta getPagination() {
    return pagination;
  }

  public void setPagination(PaginationMeta pagination) {
    this.pagination = pagination;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BudgetEstimateJobList budgetEstimateJobList = (BudgetEstimateJobList) o;
    return Objects.equals(this.data, budgetEstimateJobList.data) &&
        Objects.equals(this.pagination, budgetEstimateJobList.pagination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, pagination);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BudgetEstimateJobList {\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    pagination: ").append(toIndentedString(pagination)).append("\n");
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

