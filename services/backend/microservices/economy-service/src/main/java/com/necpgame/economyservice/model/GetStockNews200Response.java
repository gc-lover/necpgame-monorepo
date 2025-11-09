package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.StockNews;
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
 * GetStockNews200Response
 */

@JsonTypeName("getStockNews_200_response")

public class GetStockNews200Response {

  @Valid
  private List<@Valid StockNews> news = new ArrayList<>();

  public GetStockNews200Response news(List<@Valid StockNews> news) {
    this.news = news;
    return this;
  }

  public GetStockNews200Response addNewsItem(StockNews newsItem) {
    if (this.news == null) {
      this.news = new ArrayList<>();
    }
    this.news.add(newsItem);
    return this;
  }

  /**
   * Get news
   * @return news
   */
  @Valid 
  @Schema(name = "news", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("news")
  public List<@Valid StockNews> getNews() {
    return news;
  }

  public void setNews(List<@Valid StockNews> news) {
    this.news = news;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetStockNews200Response getStockNews200Response = (GetStockNews200Response) o;
    return Objects.equals(this.news, getStockNews200Response.news);
  }

  @Override
  public int hashCode() {
    return Objects.hash(news);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetStockNews200Response {\n");
    sb.append("    news: ").append(toIndentedString(news)).append("\n");
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

