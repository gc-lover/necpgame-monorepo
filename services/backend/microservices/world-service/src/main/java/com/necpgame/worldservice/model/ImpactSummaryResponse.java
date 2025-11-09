package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.PaginationMeta;
import com.necpgame.worldservice.model.PlayerOrderImpact;
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
 * ImpactSummaryResponse
 */


public class ImpactSummaryResponse {

  @Valid
  private List<@Valid PlayerOrderImpact> data = new ArrayList<>();

  private PaginationMeta pagination;

  public ImpactSummaryResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ImpactSummaryResponse(List<@Valid PlayerOrderImpact> data, PaginationMeta pagination) {
    this.data = data;
    this.pagination = pagination;
  }

  public ImpactSummaryResponse data(List<@Valid PlayerOrderImpact> data) {
    this.data = data;
    return this;
  }

  public ImpactSummaryResponse addDataItem(PlayerOrderImpact dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<>();
    }
    this.data.add(dataItem);
    return this;
  }

  /**
   * Get data
   * @return data
   */
  @NotNull @Valid 
  @Schema(name = "data", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("data")
  public List<@Valid PlayerOrderImpact> getData() {
    return data;
  }

  public void setData(List<@Valid PlayerOrderImpact> data) {
    this.data = data;
  }

  public ImpactSummaryResponse pagination(PaginationMeta pagination) {
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
    ImpactSummaryResponse impactSummaryResponse = (ImpactSummaryResponse) o;
    return Objects.equals(this.data, impactSummaryResponse.data) &&
        Objects.equals(this.pagination, impactSummaryResponse.pagination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, pagination);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImpactSummaryResponse {\n");
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

