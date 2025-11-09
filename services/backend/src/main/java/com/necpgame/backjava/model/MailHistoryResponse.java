package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.MailHistoryEntry;
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
 * MailHistoryResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class MailHistoryResponse {

  @Valid
  private List<@Valid MailHistoryEntry> entries = new ArrayList<>();

  private @Nullable PaginationMeta pagination;

  public MailHistoryResponse entries(List<@Valid MailHistoryEntry> entries) {
    this.entries = entries;
    return this;
  }

  public MailHistoryResponse addEntriesItem(MailHistoryEntry entriesItem) {
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
  public List<@Valid MailHistoryEntry> getEntries() {
    return entries;
  }

  public void setEntries(List<@Valid MailHistoryEntry> entries) {
    this.entries = entries;
  }

  public MailHistoryResponse pagination(@Nullable PaginationMeta pagination) {
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
    MailHistoryResponse mailHistoryResponse = (MailHistoryResponse) o;
    return Objects.equals(this.entries, mailHistoryResponse.entries) &&
        Objects.equals(this.pagination, mailHistoryResponse.pagination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entries, pagination);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MailHistoryResponse {\n");
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

