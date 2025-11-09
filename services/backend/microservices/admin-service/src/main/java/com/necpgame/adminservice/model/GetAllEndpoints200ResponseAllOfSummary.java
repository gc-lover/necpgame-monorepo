package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.HashMap;
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
 * GetAllEndpoints200ResponseAllOfSummary
 */

@JsonTypeName("getAllEndpoints_200_response_allOf_summary")

public class GetAllEndpoints200ResponseAllOfSummary {

  private @Nullable Integer totalEndpoints;

  @Valid
  private Map<String, Integer> byCategory = new HashMap<>();

  @Valid
  private Map<String, Integer> byMethod = new HashMap<>();

  public GetAllEndpoints200ResponseAllOfSummary totalEndpoints(@Nullable Integer totalEndpoints) {
    this.totalEndpoints = totalEndpoints;
    return this;
  }

  /**
   * Get totalEndpoints
   * @return totalEndpoints
   */
  
  @Schema(name = "total_endpoints", example = "310", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_endpoints")
  public @Nullable Integer getTotalEndpoints() {
    return totalEndpoints;
  }

  public void setTotalEndpoints(@Nullable Integer totalEndpoints) {
    this.totalEndpoints = totalEndpoints;
  }

  public GetAllEndpoints200ResponseAllOfSummary byCategory(Map<String, Integer> byCategory) {
    this.byCategory = byCategory;
    return this;
  }

  public GetAllEndpoints200ResponseAllOfSummary putByCategoryItem(String key, Integer byCategoryItem) {
    if (this.byCategory == null) {
      this.byCategory = new HashMap<>();
    }
    this.byCategory.put(key, byCategoryItem);
    return this;
  }

  /**
   * Get byCategory
   * @return byCategory
   */
  
  @Schema(name = "by_category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("by_category")
  public Map<String, Integer> getByCategory() {
    return byCategory;
  }

  public void setByCategory(Map<String, Integer> byCategory) {
    this.byCategory = byCategory;
  }

  public GetAllEndpoints200ResponseAllOfSummary byMethod(Map<String, Integer> byMethod) {
    this.byMethod = byMethod;
    return this;
  }

  public GetAllEndpoints200ResponseAllOfSummary putByMethodItem(String key, Integer byMethodItem) {
    if (this.byMethod == null) {
      this.byMethod = new HashMap<>();
    }
    this.byMethod.put(key, byMethodItem);
    return this;
  }

  /**
   * Get byMethod
   * @return byMethod
   */
  
  @Schema(name = "by_method", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("by_method")
  public Map<String, Integer> getByMethod() {
    return byMethod;
  }

  public void setByMethod(Map<String, Integer> byMethod) {
    this.byMethod = byMethod;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAllEndpoints200ResponseAllOfSummary getAllEndpoints200ResponseAllOfSummary = (GetAllEndpoints200ResponseAllOfSummary) o;
    return Objects.equals(this.totalEndpoints, getAllEndpoints200ResponseAllOfSummary.totalEndpoints) &&
        Objects.equals(this.byCategory, getAllEndpoints200ResponseAllOfSummary.byCategory) &&
        Objects.equals(this.byMethod, getAllEndpoints200ResponseAllOfSummary.byMethod);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalEndpoints, byCategory, byMethod);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAllEndpoints200ResponseAllOfSummary {\n");
    sb.append("    totalEndpoints: ").append(toIndentedString(totalEndpoints)).append("\n");
    sb.append("    byCategory: ").append(toIndentedString(byCategory)).append("\n");
    sb.append("    byMethod: ").append(toIndentedString(byMethod)).append("\n");
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

