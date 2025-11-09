package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.PendingMatchSummary;
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
 * PendingMatchPage
 */


public class PendingMatchPage {

  @Valid
  private List<@Valid PendingMatchSummary> items = new ArrayList<>();

  private @Nullable String nextCursor;

  private @Nullable Integer total;

  public PendingMatchPage() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PendingMatchPage(List<@Valid PendingMatchSummary> items) {
    this.items = items;
  }

  public PendingMatchPage items(List<@Valid PendingMatchSummary> items) {
    this.items = items;
    return this;
  }

  public PendingMatchPage addItemsItem(PendingMatchSummary itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
   */
  @NotNull @Valid 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("items")
  public List<@Valid PendingMatchSummary> getItems() {
    return items;
  }

  public void setItems(List<@Valid PendingMatchSummary> items) {
    this.items = items;
  }

  public PendingMatchPage nextCursor(@Nullable String nextCursor) {
    this.nextCursor = nextCursor;
    return this;
  }

  /**
   * Get nextCursor
   * @return nextCursor
   */
  
  @Schema(name = "nextCursor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextCursor")
  public @Nullable String getNextCursor() {
    return nextCursor;
  }

  public void setNextCursor(@Nullable String nextCursor) {
    this.nextCursor = nextCursor;
  }

  public PendingMatchPage total(@Nullable Integer total) {
    this.total = total;
    return this;
  }

  /**
   * Get total
   * minimum: 0
   * @return total
   */
  @Min(value = 0) 
  @Schema(name = "total", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total")
  public @Nullable Integer getTotal() {
    return total;
  }

  public void setTotal(@Nullable Integer total) {
    this.total = total;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PendingMatchPage pendingMatchPage = (PendingMatchPage) o;
    return Objects.equals(this.items, pendingMatchPage.items) &&
        Objects.equals(this.nextCursor, pendingMatchPage.nextCursor) &&
        Objects.equals(this.total, pendingMatchPage.total);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, nextCursor, total);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendingMatchPage {\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    nextCursor: ").append(toIndentedString(nextCursor)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
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

