package com.necpgame.inventoryservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * FilterPreset
 */


public class FilterPreset {

  private @Nullable String presetId;

  private @Nullable String name;

  @Valid
  private List<String> filters = new ArrayList<>();

  public FilterPreset presetId(@Nullable String presetId) {
    this.presetId = presetId;
    return this;
  }

  /**
   * Get presetId
   * @return presetId
   */
  
  @Schema(name = "presetId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("presetId")
  public @Nullable String getPresetId() {
    return presetId;
  }

  public void setPresetId(@Nullable String presetId) {
    this.presetId = presetId;
  }

  public FilterPreset name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public FilterPreset filters(List<String> filters) {
    this.filters = filters;
    return this;
  }

  public FilterPreset addFiltersItem(String filtersItem) {
    if (this.filters == null) {
      this.filters = new ArrayList<>();
    }
    this.filters.add(filtersItem);
    return this;
  }

  /**
   * Get filters
   * @return filters
   */
  
  @Schema(name = "filters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("filters")
  public List<String> getFilters() {
    return filters;
  }

  public void setFilters(List<String> filters) {
    this.filters = filters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FilterPreset filterPreset = (FilterPreset) o;
    return Objects.equals(this.presetId, filterPreset.presetId) &&
        Objects.equals(this.name, filterPreset.name) &&
        Objects.equals(this.filters, filterPreset.filters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(presetId, name, filters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FilterPreset {\n");
    sb.append("    presetId: ").append(toIndentedString(presetId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    filters: ").append(toIndentedString(filters)).append("\n");
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

