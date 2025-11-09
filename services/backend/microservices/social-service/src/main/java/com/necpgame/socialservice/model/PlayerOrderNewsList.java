package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.PaginationMeta;
import com.necpgame.socialservice.model.PlayerOrderNewsItem;
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
 * PlayerOrderNewsList
 */


public class PlayerOrderNewsList {

  @Valid
  private List<@Valid PlayerOrderNewsItem> data = new ArrayList<>();

  private PaginationMeta pagination;

  public PlayerOrderNewsList() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderNewsList(List<@Valid PlayerOrderNewsItem> data, PaginationMeta pagination) {
    this.data = data;
    this.pagination = pagination;
  }

  public PlayerOrderNewsList data(List<@Valid PlayerOrderNewsItem> data) {
    this.data = data;
    return this;
  }

  public PlayerOrderNewsList addDataItem(PlayerOrderNewsItem dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<>();
    }
    this.data.add(dataItem);
    return this;
  }

  /**
   * Get data
   * @return data
   */
  @NotNull @Valid 
  @Schema(name = "data", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("data")
  public List<@Valid PlayerOrderNewsItem> getData() {
    return data;
  }

  public void setData(List<@Valid PlayerOrderNewsItem> data) {
    this.data = data;
  }

  public PlayerOrderNewsList pagination(PaginationMeta pagination) {
    this.pagination = pagination;
    return this;
  }

  /**
   * Get pagination
   * @return pagination
   */
  @NotNull @Valid 
  @Schema(name = "pagination", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("pagination")
  public PaginationMeta getPagination() {
    return pagination;
  }

  public void setPagination(PaginationMeta pagination) {
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
    PlayerOrderNewsList playerOrderNewsList = (PlayerOrderNewsList) o;
    return Objects.equals(this.data, playerOrderNewsList.data) &&
        Objects.equals(this.pagination, playerOrderNewsList.pagination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, pagination);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderNewsList {\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
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

