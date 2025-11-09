package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.socialservice.model.PaginationMeta;
import com.necpgame.socialservice.model.PlayerOrderSummary;
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
 * ListPlayerOrders200Response
 */

@JsonTypeName("listPlayerOrders_200_response")

public class ListPlayerOrders200Response {

  @Valid
  private List<@Valid PlayerOrderSummary> data = new ArrayList<>();

  private PaginationMeta pagination;

  public ListPlayerOrders200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ListPlayerOrders200Response(List<@Valid PlayerOrderSummary> data, PaginationMeta pagination) {
    this.data = data;
    this.pagination = pagination;
  }

  public ListPlayerOrders200Response data(List<@Valid PlayerOrderSummary> data) {
    this.data = data;
    return this;
  }

  public ListPlayerOrders200Response addDataItem(PlayerOrderSummary dataItem) {
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
  public List<@Valid PlayerOrderSummary> getData() {
    return data;
  }

  public void setData(List<@Valid PlayerOrderSummary> data) {
    this.data = data;
  }

  public ListPlayerOrders200Response pagination(PaginationMeta pagination) {
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
    ListPlayerOrders200Response listPlayerOrders200Response = (ListPlayerOrders200Response) o;
    return Objects.equals(this.data, listPlayerOrders200Response.data) &&
        Objects.equals(this.pagination, listPlayerOrders200Response.pagination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, pagination);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ListPlayerOrders200Response {\n");
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

