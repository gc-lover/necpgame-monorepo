package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.EndpointDefinition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetMVPEndpoints200Response
 */

@JsonTypeName("getMVPEndpoints_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetMVPEndpoints200Response {

  @Valid
  private List<@Valid EndpointDefinition> endpoints = new ArrayList<>();

  private @Nullable Integer totalCount;

  @Valid
  private Map<String, Integer> categories = new HashMap<>();

  public GetMVPEndpoints200Response endpoints(List<@Valid EndpointDefinition> endpoints) {
    this.endpoints = endpoints;
    return this;
  }

  public GetMVPEndpoints200Response addEndpointsItem(EndpointDefinition endpointsItem) {
    if (this.endpoints == null) {
      this.endpoints = new ArrayList<>();
    }
    this.endpoints.add(endpointsItem);
    return this;
  }

  /**
   * Get endpoints
   * @return endpoints
   */
  @Valid 
  @Schema(name = "endpoints", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endpoints")
  public List<@Valid EndpointDefinition> getEndpoints() {
    return endpoints;
  }

  public void setEndpoints(List<@Valid EndpointDefinition> endpoints) {
    this.endpoints = endpoints;
  }

  public GetMVPEndpoints200Response totalCount(@Nullable Integer totalCount) {
    this.totalCount = totalCount;
    return this;
  }

  /**
   * Get totalCount
   * @return totalCount
   */
  
  @Schema(name = "total_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_count")
  public @Nullable Integer getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(@Nullable Integer totalCount) {
    this.totalCount = totalCount;
  }

  public GetMVPEndpoints200Response categories(Map<String, Integer> categories) {
    this.categories = categories;
    return this;
  }

  public GetMVPEndpoints200Response putCategoriesItem(String key, Integer categoriesItem) {
    if (this.categories == null) {
      this.categories = new HashMap<>();
    }
    this.categories.put(key, categoriesItem);
    return this;
  }

  /**
   * Get categories
   * @return categories
   */
  
  @Schema(name = "categories", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("categories")
  public Map<String, Integer> getCategories() {
    return categories;
  }

  public void setCategories(Map<String, Integer> categories) {
    this.categories = categories;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetMVPEndpoints200Response getMVPEndpoints200Response = (GetMVPEndpoints200Response) o;
    return Objects.equals(this.endpoints, getMVPEndpoints200Response.endpoints) &&
        Objects.equals(this.totalCount, getMVPEndpoints200Response.totalCount) &&
        Objects.equals(this.categories, getMVPEndpoints200Response.categories);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endpoints, totalCount, categories);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetMVPEndpoints200Response {\n");
    sb.append("    endpoints: ").append(toIndentedString(endpoints)).append("\n");
    sb.append("    totalCount: ").append(toIndentedString(totalCount)).append("\n");
    sb.append("    categories: ").append(toIndentedString(categories)).append("\n");
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

