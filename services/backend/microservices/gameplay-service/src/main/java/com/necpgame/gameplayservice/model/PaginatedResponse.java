package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.PaginationMeta;
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
 * Обертка для пагинированного ответа
 */

@Schema(name = "PaginatedResponse", description = "Обертка для пагинированного ответа")

public class PaginatedResponse {

  @Valid
  private List<Object> data = new ArrayList<>();

  private PaginationMeta meta;

  public PaginatedResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PaginatedResponse(List<Object> data, PaginationMeta meta) {
    this.data = data;
    this.meta = meta;
  }

  public PaginatedResponse data(List<Object> data) {
    this.data = data;
    return this;
  }

  public PaginatedResponse addDataItem(Object dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<>();
    }
    this.data.add(dataItem);
    return this;
  }

  /**
   * Массив данных текущей страницы
   * @return data
   */
  @NotNull 
  @Schema(name = "data", description = "Массив данных текущей страницы", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("data")
  public List<Object> getData() {
    return data;
  }

  public void setData(List<Object> data) {
    this.data = data;
  }

  public PaginatedResponse meta(PaginationMeta meta) {
    this.meta = meta;
    return this;
  }

  /**
   * Get meta
   * @return meta
   */
  @NotNull @Valid 
  @Schema(name = "meta", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("meta")
  public PaginationMeta getMeta() {
    return meta;
  }

  public void setMeta(PaginationMeta meta) {
    this.meta = meta;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaginatedResponse paginatedResponse = (PaginatedResponse) o;
    return Objects.equals(this.data, paginatedResponse.data) &&
        Objects.equals(this.meta, paginatedResponse.meta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, meta);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaginatedResponse {\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    meta: ").append(toIndentedString(meta)).append("\n");
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

