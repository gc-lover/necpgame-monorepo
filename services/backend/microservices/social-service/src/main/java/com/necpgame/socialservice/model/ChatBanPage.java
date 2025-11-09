package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.ChatBan;
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
 * ChatBanPage
 */


public class ChatBanPage {

  @Valid
  private List<@Valid ChatBan> items = new ArrayList<>();

  private @Nullable Integer total;

  private @Nullable Integer page;

  public ChatBanPage() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChatBanPage(List<@Valid ChatBan> items) {
    this.items = items;
  }

  public ChatBanPage items(List<@Valid ChatBan> items) {
    this.items = items;
    return this;
  }

  public ChatBanPage addItemsItem(ChatBan itemsItem) {
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
  public List<@Valid ChatBan> getItems() {
    return items;
  }

  public void setItems(List<@Valid ChatBan> items) {
    this.items = items;
  }

  public ChatBanPage total(@Nullable Integer total) {
    this.total = total;
    return this;
  }

  /**
   * Get total
   * @return total
   */
  
  @Schema(name = "total", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total")
  public @Nullable Integer getTotal() {
    return total;
  }

  public void setTotal(@Nullable Integer total) {
    this.total = total;
  }

  public ChatBanPage page(@Nullable Integer page) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatBanPage chatBanPage = (ChatBanPage) o;
    return Objects.equals(this.items, chatBanPage.items) &&
        Objects.equals(this.total, chatBanPage.total) &&
        Objects.equals(this.page, chatBanPage.page);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, total, page);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChatBanPage {\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
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

