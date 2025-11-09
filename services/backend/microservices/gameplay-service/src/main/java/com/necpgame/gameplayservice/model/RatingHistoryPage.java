package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.RatingHistoryEntry;
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
 * RatingHistoryPage
 */


public class RatingHistoryPage {

  @Valid
  private List<@Valid RatingHistoryEntry> items = new ArrayList<>();

  private @Nullable String nextCursor;

  public RatingHistoryPage() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RatingHistoryPage(List<@Valid RatingHistoryEntry> items) {
    this.items = items;
  }

  public RatingHistoryPage items(List<@Valid RatingHistoryEntry> items) {
    this.items = items;
    return this;
  }

  public RatingHistoryPage addItemsItem(RatingHistoryEntry itemsItem) {
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
  public List<@Valid RatingHistoryEntry> getItems() {
    return items;
  }

  public void setItems(List<@Valid RatingHistoryEntry> items) {
    this.items = items;
  }

  public RatingHistoryPage nextCursor(@Nullable String nextCursor) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatingHistoryPage ratingHistoryPage = (RatingHistoryPage) o;
    return Objects.equals(this.items, ratingHistoryPage.items) &&
        Objects.equals(this.nextCursor, ratingHistoryPage.nextCursor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, nextCursor);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatingHistoryPage {\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    nextCursor: ").append(toIndentedString(nextCursor)).append("\n");
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

