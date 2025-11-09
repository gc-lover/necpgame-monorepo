package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.ChannelMember;
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
 * ChannelMembersPage
 */


public class ChannelMembersPage {

  @Valid
  private List<@Valid ChannelMember> items = new ArrayList<>();

  private @Nullable String nextCursor;

  private @Nullable Integer total;

  public ChannelMembersPage() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChannelMembersPage(List<@Valid ChannelMember> items) {
    this.items = items;
  }

  public ChannelMembersPage items(List<@Valid ChannelMember> items) {
    this.items = items;
    return this;
  }

  public ChannelMembersPage addItemsItem(ChannelMember itemsItem) {
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
  public List<@Valid ChannelMember> getItems() {
    return items;
  }

  public void setItems(List<@Valid ChannelMember> items) {
    this.items = items;
  }

  public ChannelMembersPage nextCursor(@Nullable String nextCursor) {
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

  public ChannelMembersPage total(@Nullable Integer total) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChannelMembersPage channelMembersPage = (ChannelMembersPage) o;
    return Objects.equals(this.items, channelMembersPage.items) &&
        Objects.equals(this.nextCursor, channelMembersPage.nextCursor) &&
        Objects.equals(this.total, channelMembersPage.total);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, nextCursor, total);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChannelMembersPage {\n");
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

