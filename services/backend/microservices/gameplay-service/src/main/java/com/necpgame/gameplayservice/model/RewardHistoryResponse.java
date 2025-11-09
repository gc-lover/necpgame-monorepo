package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.PaginationMeta;
import com.necpgame.gameplayservice.model.RewardHistoryEntry;
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
 * RewardHistoryResponse
 */


public class RewardHistoryResponse {

  @Valid
  private List<@Valid RewardHistoryEntry> entries = new ArrayList<>();

  private @Nullable PaginationMeta pagination;

  public RewardHistoryResponse entries(List<@Valid RewardHistoryEntry> entries) {
    this.entries = entries;
    return this;
  }

  public RewardHistoryResponse addEntriesItem(RewardHistoryEntry entriesItem) {
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
  public List<@Valid RewardHistoryEntry> getEntries() {
    return entries;
  }

  public void setEntries(List<@Valid RewardHistoryEntry> entries) {
    this.entries = entries;
  }

  public RewardHistoryResponse pagination(@Nullable PaginationMeta pagination) {
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
    RewardHistoryResponse rewardHistoryResponse = (RewardHistoryResponse) o;
    return Objects.equals(this.entries, rewardHistoryResponse.entries) &&
        Objects.equals(this.pagination, rewardHistoryResponse.pagination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entries, pagination);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RewardHistoryResponse {\n");
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

