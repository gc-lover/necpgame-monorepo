package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * FilterCriteria
 */


public class FilterCriteria {

  private @Nullable String filterId;

  private @Nullable String filterName;

  /**
   * Gets or Sets filterType
   */
  public enum FilterTypeEnum {
    RELATIONSHIP_STAGE("relationship_stage"),
    
    LOCATION("location"),
    
    TIME_OF_DAY("time_of_day"),
    
    PREREQUISITES("prerequisites"),
    
    COOLDOWN("cooldown"),
    
    MOOD("mood"),
    
    QUEST_STATUS("quest_status");

    private final String value;

    FilterTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static FilterTypeEnum fromValue(String value) {
      for (FilterTypeEnum b : FilterTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable FilterTypeEnum filterType;

  private @Nullable Boolean enabled;

  @Valid
  private Map<String, Object> parameters = new HashMap<>();

  public FilterCriteria filterId(@Nullable String filterId) {
    this.filterId = filterId;
    return this;
  }

  /**
   * Get filterId
   * @return filterId
   */
  
  @Schema(name = "filter_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("filter_id")
  public @Nullable String getFilterId() {
    return filterId;
  }

  public void setFilterId(@Nullable String filterId) {
    this.filterId = filterId;
  }

  public FilterCriteria filterName(@Nullable String filterName) {
    this.filterName = filterName;
    return this;
  }

  /**
   * Get filterName
   * @return filterName
   */
  
  @Schema(name = "filter_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("filter_name")
  public @Nullable String getFilterName() {
    return filterName;
  }

  public void setFilterName(@Nullable String filterName) {
    this.filterName = filterName;
  }

  public FilterCriteria filterType(@Nullable FilterTypeEnum filterType) {
    this.filterType = filterType;
    return this;
  }

  /**
   * Get filterType
   * @return filterType
   */
  
  @Schema(name = "filter_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("filter_type")
  public @Nullable FilterTypeEnum getFilterType() {
    return filterType;
  }

  public void setFilterType(@Nullable FilterTypeEnum filterType) {
    this.filterType = filterType;
  }

  public FilterCriteria enabled(@Nullable Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Get enabled
   * @return enabled
   */
  
  @Schema(name = "enabled", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enabled")
  public @Nullable Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(@Nullable Boolean enabled) {
    this.enabled = enabled;
  }

  public FilterCriteria parameters(Map<String, Object> parameters) {
    this.parameters = parameters;
    return this;
  }

  public FilterCriteria putParametersItem(String key, Object parametersItem) {
    if (this.parameters == null) {
      this.parameters = new HashMap<>();
    }
    this.parameters.put(key, parametersItem);
    return this;
  }

  /**
   * Get parameters
   * @return parameters
   */
  
  @Schema(name = "parameters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("parameters")
  public Map<String, Object> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, Object> parameters) {
    this.parameters = parameters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FilterCriteria filterCriteria = (FilterCriteria) o;
    return Objects.equals(this.filterId, filterCriteria.filterId) &&
        Objects.equals(this.filterName, filterCriteria.filterName) &&
        Objects.equals(this.filterType, filterCriteria.filterType) &&
        Objects.equals(this.enabled, filterCriteria.enabled) &&
        Objects.equals(this.parameters, filterCriteria.parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(filterId, filterName, filterType, enabled, parameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FilterCriteria {\n");
    sb.append("    filterId: ").append(toIndentedString(filterId)).append("\n");
    sb.append("    filterName: ").append(toIndentedString(filterName)).append("\n");
    sb.append("    filterType: ").append(toIndentedString(filterType)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    parameters: ").append(toIndentedString(parameters)).append("\n");
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

