package com.necpgame.inventoryservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.inventoryservice.model.FilterPreset;
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
 * InventoryFiltersResponse
 */


public class InventoryFiltersResponse {

  @Valid
  private List<String> activeFilters = new ArrayList<>();

  @Valid
  private List<@Valid FilterPreset> presets = new ArrayList<>();

  public InventoryFiltersResponse activeFilters(List<String> activeFilters) {
    this.activeFilters = activeFilters;
    return this;
  }

  public InventoryFiltersResponse addActiveFiltersItem(String activeFiltersItem) {
    if (this.activeFilters == null) {
      this.activeFilters = new ArrayList<>();
    }
    this.activeFilters.add(activeFiltersItem);
    return this;
  }

  /**
   * Get activeFilters
   * @return activeFilters
   */
  
  @Schema(name = "activeFilters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activeFilters")
  public List<String> getActiveFilters() {
    return activeFilters;
  }

  public void setActiveFilters(List<String> activeFilters) {
    this.activeFilters = activeFilters;
  }

  public InventoryFiltersResponse presets(List<@Valid FilterPreset> presets) {
    this.presets = presets;
    return this;
  }

  public InventoryFiltersResponse addPresetsItem(FilterPreset presetsItem) {
    if (this.presets == null) {
      this.presets = new ArrayList<>();
    }
    this.presets.add(presetsItem);
    return this;
  }

  /**
   * Get presets
   * @return presets
   */
  @Valid 
  @Schema(name = "presets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("presets")
  public List<@Valid FilterPreset> getPresets() {
    return presets;
  }

  public void setPresets(List<@Valid FilterPreset> presets) {
    this.presets = presets;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InventoryFiltersResponse inventoryFiltersResponse = (InventoryFiltersResponse) o;
    return Objects.equals(this.activeFilters, inventoryFiltersResponse.activeFilters) &&
        Objects.equals(this.presets, inventoryFiltersResponse.presets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activeFilters, presets);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InventoryFiltersResponse {\n");
    sb.append("    activeFilters: ").append(toIndentedString(activeFilters)).append("\n");
    sb.append("    presets: ").append(toIndentedString(presets)).append("\n");
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

