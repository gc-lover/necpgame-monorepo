package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.LeaderboardEntry;
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
 * CompanionLeaderboardResponse
 */


public class CompanionLeaderboardResponse {

  private @Nullable String metric;

  private @Nullable String range;

  @Valid
  private List<@Valid LeaderboardEntry> entries = new ArrayList<>();

  private @Nullable PaginationMeta pagination;

  public CompanionLeaderboardResponse metric(@Nullable String metric) {
    this.metric = metric;
    return this;
  }

  /**
   * Get metric
   * @return metric
   */
  
  @Schema(name = "metric", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metric")
  public @Nullable String getMetric() {
    return metric;
  }

  public void setMetric(@Nullable String metric) {
    this.metric = metric;
  }

  public CompanionLeaderboardResponse range(@Nullable String range) {
    this.range = range;
    return this;
  }

  /**
   * Get range
   * @return range
   */
  
  @Schema(name = "range", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("range")
  public @Nullable String getRange() {
    return range;
  }

  public void setRange(@Nullable String range) {
    this.range = range;
  }

  public CompanionLeaderboardResponse entries(List<@Valid LeaderboardEntry> entries) {
    this.entries = entries;
    return this;
  }

  public CompanionLeaderboardResponse addEntriesItem(LeaderboardEntry entriesItem) {
    if (this.entries == null) {
      this.entries = new ArrayList<>();
    }
    this.entries.add(entriesItem);
    return this;
  }

  /**
   * Get entries
   * @return entries
   */
  @Valid 
  @Schema(name = "entries", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entries")
  public List<@Valid LeaderboardEntry> getEntries() {
    return entries;
  }

  public void setEntries(List<@Valid LeaderboardEntry> entries) {
    this.entries = entries;
  }

  public CompanionLeaderboardResponse pagination(@Nullable PaginationMeta pagination) {
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
    CompanionLeaderboardResponse companionLeaderboardResponse = (CompanionLeaderboardResponse) o;
    return Objects.equals(this.metric, companionLeaderboardResponse.metric) &&
        Objects.equals(this.range, companionLeaderboardResponse.range) &&
        Objects.equals(this.entries, companionLeaderboardResponse.entries) &&
        Objects.equals(this.pagination, companionLeaderboardResponse.pagination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(metric, range, entries, pagination);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompanionLeaderboardResponse {\n");
    sb.append("    metric: ").append(toIndentedString(metric)).append("\n");
    sb.append("    range: ").append(toIndentedString(range)).append("\n");
    sb.append("    entries: ").append(toIndentedString(entries)).append("\n");
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

