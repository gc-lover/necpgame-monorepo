package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.PaginationMeta;
import com.necpgame.backjava.model.QuestCatalogEntry;
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
 * GetQuestCatalog200Response
 */

@JsonTypeName("getQuestCatalog_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetQuestCatalog200Response {

  @Valid
  private List<@Valid QuestCatalogEntry> data = new ArrayList<>();

  private PaginationMeta meta;

  private @Nullable Object filtersApplied;

  public GetQuestCatalog200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GetQuestCatalog200Response(List<@Valid QuestCatalogEntry> data, PaginationMeta meta) {
    this.data = data;
    this.meta = meta;
  }

  public GetQuestCatalog200Response data(List<@Valid QuestCatalogEntry> data) {
    this.data = data;
    return this;
  }

  public GetQuestCatalog200Response addDataItem(QuestCatalogEntry dataItem) {
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
  public List<@Valid QuestCatalogEntry> getData() {
    return data;
  }

  public void setData(List<@Valid QuestCatalogEntry> data) {
    this.data = data;
  }

  public GetQuestCatalog200Response meta(PaginationMeta meta) {
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

  public GetQuestCatalog200Response filtersApplied(@Nullable Object filtersApplied) {
    this.filtersApplied = filtersApplied;
    return this;
  }

  /**
   * Примененные фильтры
   * @return filtersApplied
   */
  
  @Schema(name = "filters_applied", description = "Примененные фильтры", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("filters_applied")
  public @Nullable Object getFiltersApplied() {
    return filtersApplied;
  }

  public void setFiltersApplied(@Nullable Object filtersApplied) {
    this.filtersApplied = filtersApplied;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetQuestCatalog200Response getQuestCatalog200Response = (GetQuestCatalog200Response) o;
    return Objects.equals(this.data, getQuestCatalog200Response.data) &&
        Objects.equals(this.meta, getQuestCatalog200Response.meta) &&
        Objects.equals(this.filtersApplied, getQuestCatalog200Response.filtersApplied);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, meta, filtersApplied);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetQuestCatalog200Response {\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    meta: ").append(toIndentedString(meta)).append("\n");
    sb.append("    filtersApplied: ").append(toIndentedString(filtersApplied)).append("\n");
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

