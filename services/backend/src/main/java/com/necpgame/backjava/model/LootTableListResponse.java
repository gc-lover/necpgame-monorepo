package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.LootTableSummary;
import com.necpgame.backjava.model.Page;
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
 * LootTableListResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LootTableListResponse {

  @Valid
  private List<@Valid LootTableSummary> tables = new ArrayList<>();

  private @Nullable Page page;

  public LootTableListResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LootTableListResponse(List<@Valid LootTableSummary> tables) {
    this.tables = tables;
  }

  public LootTableListResponse tables(List<@Valid LootTableSummary> tables) {
    this.tables = tables;
    return this;
  }

  public LootTableListResponse addTablesItem(LootTableSummary tablesItem) {
    if (this.tables == null) {
      this.tables = new ArrayList<>();
    }
    this.tables.add(tablesItem);
    return this;
  }

  /**
   * Get tables
   * @return tables
   */
  @NotNull @Valid 
  @Schema(name = "tables", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tables")
  public List<@Valid LootTableSummary> getTables() {
    return tables;
  }

  public void setTables(List<@Valid LootTableSummary> tables) {
    this.tables = tables;
  }

  public LootTableListResponse page(@Nullable Page page) {
    this.page = page;
    return this;
  }

  /**
   * Get page
   * @return page
   */
  @Valid 
  @Schema(name = "page", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("page")
  public @Nullable Page getPage() {
    return page;
  }

  public void setPage(@Nullable Page page) {
    this.page = page;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootTableListResponse lootTableListResponse = (LootTableListResponse) o;
    return Objects.equals(this.tables, lootTableListResponse.tables) &&
        Objects.equals(this.page, lootTableListResponse.page);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tables, page);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootTableListResponse {\n");
    sb.append("    tables: ").append(toIndentedString(tables)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
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

