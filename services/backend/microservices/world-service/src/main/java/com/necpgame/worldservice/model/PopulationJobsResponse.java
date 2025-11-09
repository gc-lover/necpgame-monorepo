package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.PaginationMeta;
import com.necpgame.worldservice.model.PopulationRecalcJob;
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
 * PopulationJobsResponse
 */


public class PopulationJobsResponse {

  @Valid
  private List<@Valid PopulationRecalcJob> data = new ArrayList<>();

  private @Nullable PaginationMeta pagination;

  public PopulationJobsResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PopulationJobsResponse(List<@Valid PopulationRecalcJob> data) {
    this.data = data;
  }

  public PopulationJobsResponse data(List<@Valid PopulationRecalcJob> data) {
    this.data = data;
    return this;
  }

  public PopulationJobsResponse addDataItem(PopulationRecalcJob dataItem) {
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
  public List<@Valid PopulationRecalcJob> getData() {
    return data;
  }

  public void setData(List<@Valid PopulationRecalcJob> data) {
    this.data = data;
  }

  public PopulationJobsResponse pagination(@Nullable PaginationMeta pagination) {
    this.pagination = pagination;
    return this;
  }

  /**
   * Get pagination
   * @return pagination
   */
  @Valid 
  @Schema(name = "pagination", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pagination")
  public @Nullable PaginationMeta getPagination() {
    return pagination;
  }

  public void setPagination(@Nullable PaginationMeta pagination) {
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
    PopulationJobsResponse populationJobsResponse = (PopulationJobsResponse) o;
    return Objects.equals(this.data, populationJobsResponse.data) &&
        Objects.equals(this.pagination, populationJobsResponse.pagination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, pagination);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PopulationJobsResponse {\n");
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

