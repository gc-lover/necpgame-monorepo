package com.necpgame.partyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.partyservice.model.PaginationMeta;
import com.necpgame.partyservice.model.PartySummary;
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
 * PartySearchResponse
 */


public class PartySearchResponse {

  @Valid
  private List<@Valid PartySummary> results = new ArrayList<>();

  private @Nullable PaginationMeta pagination;

  public PartySearchResponse results(List<@Valid PartySummary> results) {
    this.results = results;
    return this;
  }

  public PartySearchResponse addResultsItem(PartySummary resultsItem) {
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
  public List<@Valid PartySummary> getResults() {
    return results;
  }

  public void setResults(List<@Valid PartySummary> results) {
    this.results = results;
  }

  public PartySearchResponse pagination(@Nullable PaginationMeta pagination) {
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
    PartySearchResponse partySearchResponse = (PartySearchResponse) o;
    return Objects.equals(this.results, partySearchResponse.results) &&
        Objects.equals(this.pagination, partySearchResponse.pagination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(results, pagination);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartySearchResponse {\n");
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

