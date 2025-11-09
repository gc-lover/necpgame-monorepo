package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.LeaderboardEntry;
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
 * LeaderboardPage
 */


public class LeaderboardPage {

  @Valid
  private List<@Valid LeaderboardEntry> items = new ArrayList<>();

  private @Nullable Integer page;

  private @Nullable Integer pageSize;

  private Integer total;

  private @Nullable String etag;

  public LeaderboardPage() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LeaderboardPage(List<@Valid LeaderboardEntry> items, Integer total) {
    this.items = items;
    this.total = total;
  }

  public LeaderboardPage items(List<@Valid LeaderboardEntry> items) {
    this.items = items;
    return this;
  }

  public LeaderboardPage addItemsItem(LeaderboardEntry itemsItem) {
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
  public List<@Valid LeaderboardEntry> getItems() {
    return items;
  }

  public void setItems(List<@Valid LeaderboardEntry> items) {
    this.items = items;
  }

  public LeaderboardPage page(@Nullable Integer page) {
    this.page = page;
    return this;
  }

  /**
   * Get page
   * @return page
   */
  
  @Schema(name = "page", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("page")
  public @Nullable Integer getPage() {
    return page;
  }

  public void setPage(@Nullable Integer page) {
    this.page = page;
  }

  public LeaderboardPage pageSize(@Nullable Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  /**
   * Get pageSize
   * @return pageSize
   */
  
  @Schema(name = "pageSize", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pageSize")
  public @Nullable Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(@Nullable Integer pageSize) {
    this.pageSize = pageSize;
  }

  public LeaderboardPage total(Integer total) {
    this.total = total;
    return this;
  }

  /**
   * Get total
   * @return total
   */
  @NotNull 
  @Schema(name = "total", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("total")
  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public LeaderboardPage etag(@Nullable String etag) {
    this.etag = etag;
    return this;
  }

  /**
   * Get etag
   * @return etag
   */
  
  @Schema(name = "etag", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("etag")
  public @Nullable String getEtag() {
    return etag;
  }

  public void setEtag(@Nullable String etag) {
    this.etag = etag;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LeaderboardPage leaderboardPage = (LeaderboardPage) o;
    return Objects.equals(this.items, leaderboardPage.items) &&
        Objects.equals(this.page, leaderboardPage.page) &&
        Objects.equals(this.pageSize, leaderboardPage.pageSize) &&
        Objects.equals(this.total, leaderboardPage.total) &&
        Objects.equals(this.etag, leaderboardPage.etag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, page, pageSize, total, etag);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeaderboardPage {\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    etag: ").append(toIndentedString(etag)).append("\n");
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

