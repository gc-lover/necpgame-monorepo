package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Базовые метаданные пагинации.
 */

@Schema(name = "Page", description = "Базовые метаданные пагинации.")

public class Page {

  private Integer page;

  private Integer pageSize;

  private Integer total;

  private Integer totalPages;

  private @Nullable Boolean hasNext;

  private @Nullable Boolean hasPrev;

  public Page() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Page(Integer page, Integer pageSize, Integer total, Integer totalPages) {
    this.page = page;
    this.pageSize = pageSize;
    this.total = total;
    this.totalPages = totalPages;
  }

  public Page page(Integer page) {
    this.page = page;
    return this;
  }

  /**
   * Текущий номер страницы
   * minimum: 1
   * @return page
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "page", example = "1", description = "Текущий номер страницы", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("page")
  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public Page pageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  /**
   * Количество элементов на странице
   * minimum: 1
   * @return pageSize
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "page_size", example = "20", description = "Количество элементов на странице", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("page_size")
  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Page total(Integer total) {
    this.total = total;
    return this;
  }

  /**
   * Общее количество элементов
   * minimum: 0
   * @return total
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "total", example = "156", description = "Общее количество элементов", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("total")
  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public Page totalPages(Integer totalPages) {
    this.totalPages = totalPages;
    return this;
  }

  /**
   * Общее количество страниц
   * minimum: 0
   * @return totalPages
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "total_pages", example = "8", description = "Общее количество страниц", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("total_pages")
  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public Page hasNext(@Nullable Boolean hasNext) {
    this.hasNext = hasNext;
    return this;
  }

  /**
   * Есть ли следующая страница
   * @return hasNext
   */
  
  @Schema(name = "has_next", example = "true", description = "Есть ли следующая страница", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("has_next")
  public @Nullable Boolean getHasNext() {
    return hasNext;
  }

  public void setHasNext(@Nullable Boolean hasNext) {
    this.hasNext = hasNext;
  }

  public Page hasPrev(@Nullable Boolean hasPrev) {
    this.hasPrev = hasPrev;
    return this;
  }

  /**
   * Есть ли предыдущая страница
   * @return hasPrev
   */
  
  @Schema(name = "has_prev", example = "false", description = "Есть ли предыдущая страница", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("has_prev")
  public @Nullable Boolean getHasPrev() {
    return hasPrev;
  }

  public void setHasPrev(@Nullable Boolean hasPrev) {
    this.hasPrev = hasPrev;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Page page = (Page) o;
    return Objects.equals(this.page, page.page) &&
        Objects.equals(this.pageSize, page.pageSize) &&
        Objects.equals(this.total, page.total) &&
        Objects.equals(this.totalPages, page.totalPages) &&
        Objects.equals(this.hasNext, page.hasNext) &&
        Objects.equals(this.hasPrev, page.hasPrev);
  }

  @Override
  public int hashCode() {
    return Objects.hash(page, pageSize, total, totalPages, hasNext, hasPrev);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Page {\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    totalPages: ").append(toIndentedString(totalPages)).append("\n");
    sb.append("    hasNext: ").append(toIndentedString(hasNext)).append("\n");
    sb.append("    hasPrev: ").append(toIndentedString(hasPrev)).append("\n");
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

