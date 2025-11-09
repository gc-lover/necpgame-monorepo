package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.GuildSummary;
import com.necpgame.backjava.model.PaginationMeta;
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
 * GuildSearchResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GuildSearchResponse {

  @Valid
  private List<@Valid GuildSummary> results = new ArrayList<>();

  private @Nullable PaginationMeta pagination;

  public GuildSearchResponse results(List<@Valid GuildSummary> results) {
    this.results = results;
    return this;
  }

  public GuildSearchResponse addResultsItem(GuildSummary resultsItem) {
    if (this.results == null) {
      this.results = new ArrayList<>();
    }
    this.results.add(resultsItem);
    return this;
  }

  /**
   * Get results
   * @return results
   */
  @Valid 
  @Schema(name = "results", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("results")
  public List<@Valid GuildSummary> getResults() {
    return results;
  }

  public void setResults(List<@Valid GuildSummary> results) {
    this.results = results;
  }

  public GuildSearchResponse pagination(@Nullable PaginationMeta pagination) {
    this.pagination = pagination;
    return this;
  }

  /**
   * Get pagination
   * @return pagination
   */
  @Valid 
  @Schema(name = "pagination", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pagination")
  public @Nullable PaginationMeta getPagination() {
    return pagination;
  }

  public void setPagination(@Nullable PaginationMeta pagination) {
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
    GuildSearchResponse guildSearchResponse = (GuildSearchResponse) o;
    return Objects.equals(this.results, guildSearchResponse.results) &&
        Objects.equals(this.pagination, guildSearchResponse.pagination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(results, pagination);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildSearchResponse {\n");
    sb.append("    results: ").append(toIndentedString(results)).append("\n");
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

