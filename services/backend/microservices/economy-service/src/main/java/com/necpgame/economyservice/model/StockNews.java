package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * StockNews
 */


public class StockNews {

  private @Nullable String newsId;

  private @Nullable String headline;

  private @Nullable String summary;

  @Valid
  private List<String> affectedTickers = new ArrayList<>();

  /**
   * Gets or Sets impact
   */
  public enum ImpactEnum {
    VERY_NEGATIVE("very_negative"),
    
    NEGATIVE("negative"),
    
    NEUTRAL("neutral"),
    
    POSITIVE("positive"),
    
    VERY_POSITIVE("very_positive");

    private final String value;

    ImpactEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ImpactEnum fromValue(String value) {
      for (ImpactEnum b : ImpactEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ImpactEnum impact;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime publishedAt;

  private @Nullable String source;

  public StockNews newsId(@Nullable String newsId) {
    this.newsId = newsId;
    return this;
  }

  /**
   * Get newsId
   * @return newsId
   */
  
  @Schema(name = "news_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("news_id")
  public @Nullable String getNewsId() {
    return newsId;
  }

  public void setNewsId(@Nullable String newsId) {
    this.newsId = newsId;
  }

  public StockNews headline(@Nullable String headline) {
    this.headline = headline;
    return this;
  }

  /**
   * Get headline
   * @return headline
   */
  
  @Schema(name = "headline", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("headline")
  public @Nullable String getHeadline() {
    return headline;
  }

  public void setHeadline(@Nullable String headline) {
    this.headline = headline;
  }

  public StockNews summary(@Nullable String summary) {
    this.summary = summary;
    return this;
  }

  /**
   * Get summary
   * @return summary
   */
  
  @Schema(name = "summary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("summary")
  public @Nullable String getSummary() {
    return summary;
  }

  public void setSummary(@Nullable String summary) {
    this.summary = summary;
  }

  public StockNews affectedTickers(List<String> affectedTickers) {
    this.affectedTickers = affectedTickers;
    return this;
  }

  public StockNews addAffectedTickersItem(String affectedTickersItem) {
    if (this.affectedTickers == null) {
      this.affectedTickers = new ArrayList<>();
    }
    this.affectedTickers.add(affectedTickersItem);
    return this;
  }

  /**
   * Get affectedTickers
   * @return affectedTickers
   */
  
  @Schema(name = "affected_tickers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affected_tickers")
  public List<String> getAffectedTickers() {
    return affectedTickers;
  }

  public void setAffectedTickers(List<String> affectedTickers) {
    this.affectedTickers = affectedTickers;
  }

  public StockNews impact(@Nullable ImpactEnum impact) {
    this.impact = impact;
    return this;
  }

  /**
   * Get impact
   * @return impact
   */
  
  @Schema(name = "impact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("impact")
  public @Nullable ImpactEnum getImpact() {
    return impact;
  }

  public void setImpact(@Nullable ImpactEnum impact) {
    this.impact = impact;
  }

  public StockNews publishedAt(@Nullable OffsetDateTime publishedAt) {
    this.publishedAt = publishedAt;
    return this;
  }

  /**
   * Get publishedAt
   * @return publishedAt
   */
  @Valid 
  @Schema(name = "published_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("published_at")
  public @Nullable OffsetDateTime getPublishedAt() {
    return publishedAt;
  }

  public void setPublishedAt(@Nullable OffsetDateTime publishedAt) {
    this.publishedAt = publishedAt;
  }

  public StockNews source(@Nullable String source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  
  @Schema(name = "source", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("source")
  public @Nullable String getSource() {
    return source;
  }

  public void setSource(@Nullable String source) {
    this.source = source;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StockNews stockNews = (StockNews) o;
    return Objects.equals(this.newsId, stockNews.newsId) &&
        Objects.equals(this.headline, stockNews.headline) &&
        Objects.equals(this.summary, stockNews.summary) &&
        Objects.equals(this.affectedTickers, stockNews.affectedTickers) &&
        Objects.equals(this.impact, stockNews.impact) &&
        Objects.equals(this.publishedAt, stockNews.publishedAt) &&
        Objects.equals(this.source, stockNews.source);
  }

  @Override
  public int hashCode() {
    return Objects.hash(newsId, headline, summary, affectedTickers, impact, publishedAt, source);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StockNews {\n");
    sb.append("    newsId: ").append(toIndentedString(newsId)).append("\n");
    sb.append("    headline: ").append(toIndentedString(headline)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    affectedTickers: ").append(toIndentedString(affectedTickers)).append("\n");
    sb.append("    impact: ").append(toIndentedString(impact)).append("\n");
    sb.append("    publishedAt: ").append(toIndentedString(publishedAt)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
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

