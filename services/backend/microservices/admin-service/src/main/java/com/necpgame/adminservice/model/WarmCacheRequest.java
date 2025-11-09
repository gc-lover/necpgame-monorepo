package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * WarmCacheRequest
 */

@JsonTypeName("warmCache_request")

public class WarmCacheRequest {

  /**
   * Gets or Sets dataTypes
   */
  public enum DataTypesEnum {
    PLAYER_PROFILES("player_profiles"),
    
    QUESTS("quests"),
    
    ITEMS("items"),
    
    NPCS("npcs");

    private final String value;

    DataTypesEnum(String value) {
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
    public static DataTypesEnum fromValue(String value) {
      for (DataTypesEnum b : DataTypesEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<DataTypesEnum> dataTypes = new ArrayList<>();

  public WarmCacheRequest dataTypes(List<DataTypesEnum> dataTypes) {
    this.dataTypes = dataTypes;
    return this;
  }

  public WarmCacheRequest addDataTypesItem(DataTypesEnum dataTypesItem) {
    if (this.dataTypes == null) {
      this.dataTypes = new ArrayList<>();
    }
    this.dataTypes.add(dataTypesItem);
    return this;
  }

  /**
   * Get dataTypes
   * @return dataTypes
   */
  
  @Schema(name = "data_types", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("data_types")
  public List<DataTypesEnum> getDataTypes() {
    return dataTypes;
  }

  public void setDataTypes(List<DataTypesEnum> dataTypes) {
    this.dataTypes = dataTypes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WarmCacheRequest warmCacheRequest = (WarmCacheRequest) o;
    return Objects.equals(this.dataTypes, warmCacheRequest.dataTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataTypes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarmCacheRequest {\n");
    sb.append("    dataTypes: ").append(toIndentedString(dataTypes)).append("\n");
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

