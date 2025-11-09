package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.NewsLink;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
 * PlayerOrderNewsItem
 */


public class PlayerOrderNewsItem {

  private UUID newsId;

  private @Nullable UUID orderId;

  private @Nullable UUID effectId;

  private String headline;

  private String summary;

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    INFO("info"),
    
    WARNING("warning"),
    
    ALERT("alert");

    private final String value;

    SeverityEnum(String value) {
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
    public static SeverityEnum fromValue(String value) {
      for (SeverityEnum b : SeverityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SeverityEnum severity;

  @Valid
  private List<String> tags = new ArrayList<>();

  private @Nullable String locale;

  /**
   * Gets or Sets source
   */
  public enum SourceEnum {
    WORLD_SERVICE("world-service"),
    
    ECONOMY_SERVICE("economy-service"),
    
    SOCIAL_SERVICE("social-service"),
    
    FACTIONS_SERVICE("factions-service"),
    
    MANUAL("manual");

    private final String value;

    SourceEnum(String value) {
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
    public static SourceEnum fromValue(String value) {
      for (SourceEnum b : SourceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SourceEnum source;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime publishedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  @Valid
  private List<@Valid NewsLink> links = new ArrayList<>();

  public PlayerOrderNewsItem() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderNewsItem(UUID newsId, String headline, String summary, SeverityEnum severity, List<String> tags, OffsetDateTime publishedAt) {
    this.newsId = newsId;
    this.headline = headline;
    this.summary = summary;
    this.severity = severity;
    this.tags = tags;
    this.publishedAt = publishedAt;
  }

  public PlayerOrderNewsItem newsId(UUID newsId) {
    this.newsId = newsId;
    return this;
  }

  /**
   * Get newsId
   * @return newsId
   */
  @NotNull @Valid 
  @Schema(name = "newsId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("newsId")
  public UUID getNewsId() {
    return newsId;
  }

  public void setNewsId(UUID newsId) {
    this.newsId = newsId;
  }

  public PlayerOrderNewsItem orderId(@Nullable UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Get orderId
   * @return orderId
   */
  @Valid 
  @Schema(name = "orderId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("orderId")
  public @Nullable UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(@Nullable UUID orderId) {
    this.orderId = orderId;
  }

  public PlayerOrderNewsItem effectId(@Nullable UUID effectId) {
    this.effectId = effectId;
    return this;
  }

  /**
   * Get effectId
   * @return effectId
   */
  @Valid 
  @Schema(name = "effectId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effectId")
  public @Nullable UUID getEffectId() {
    return effectId;
  }

  public void setEffectId(@Nullable UUID effectId) {
    this.effectId = effectId;
  }

  public PlayerOrderNewsItem headline(String headline) {
    this.headline = headline;
    return this;
  }

  /**
   * Get headline
   * @return headline
   */
  @NotNull 
  @Schema(name = "headline", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("headline")
  public String getHeadline() {
    return headline;
  }

  public void setHeadline(String headline) {
    this.headline = headline;
  }

  public PlayerOrderNewsItem summary(String summary) {
    this.summary = summary;
    return this;
  }

  /**
   * Get summary
   * @return summary
   */
  @NotNull 
  @Schema(name = "summary", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("summary")
  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public PlayerOrderNewsItem severity(SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  @NotNull 
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("severity")
  public SeverityEnum getSeverity() {
    return severity;
  }

  public void setSeverity(SeverityEnum severity) {
    this.severity = severity;
  }

  public PlayerOrderNewsItem tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public PlayerOrderNewsItem addTagsItem(String tagsItem) {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tagsItem);
    return this;
  }

  /**
   * Get tags
   * @return tags
   */
  @NotNull 
  @Schema(name = "tags", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tags")
  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public PlayerOrderNewsItem locale(@Nullable String locale) {
    this.locale = locale;
    return this;
  }

  /**
   * Get locale
   * @return locale
   */
  @Pattern(regexp = "^[a-z]{2}(-[A-Z]{2})?$") 
  @Schema(name = "locale", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locale")
  public @Nullable String getLocale() {
    return locale;
  }

  public void setLocale(@Nullable String locale) {
    this.locale = locale;
  }

  public PlayerOrderNewsItem source(@Nullable SourceEnum source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  
  @Schema(name = "source", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("source")
  public @Nullable SourceEnum getSource() {
    return source;
  }

  public void setSource(@Nullable SourceEnum source) {
    this.source = source;
  }

  public PlayerOrderNewsItem publishedAt(OffsetDateTime publishedAt) {
    this.publishedAt = publishedAt;
    return this;
  }

  /**
   * Get publishedAt
   * @return publishedAt
   */
  @NotNull @Valid 
  @Schema(name = "publishedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("publishedAt")
  public OffsetDateTime getPublishedAt() {
    return publishedAt;
  }

  public void setPublishedAt(OffsetDateTime publishedAt) {
    this.publishedAt = publishedAt;
  }

  public PlayerOrderNewsItem expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public PlayerOrderNewsItem links(List<@Valid NewsLink> links) {
    this.links = links;
    return this;
  }

  public PlayerOrderNewsItem addLinksItem(NewsLink linksItem) {
    if (this.links == null) {
      this.links = new ArrayList<>();
    }
    this.links.add(linksItem);
    return this;
  }

  /**
   * Get links
   * @return links
   */
  @Valid 
  @Schema(name = "links", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("links")
  public List<@Valid NewsLink> getLinks() {
    return links;
  }

  public void setLinks(List<@Valid NewsLink> links) {
    this.links = links;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderNewsItem playerOrderNewsItem = (PlayerOrderNewsItem) o;
    return Objects.equals(this.newsId, playerOrderNewsItem.newsId) &&
        Objects.equals(this.orderId, playerOrderNewsItem.orderId) &&
        Objects.equals(this.effectId, playerOrderNewsItem.effectId) &&
        Objects.equals(this.headline, playerOrderNewsItem.headline) &&
        Objects.equals(this.summary, playerOrderNewsItem.summary) &&
        Objects.equals(this.severity, playerOrderNewsItem.severity) &&
        Objects.equals(this.tags, playerOrderNewsItem.tags) &&
        Objects.equals(this.locale, playerOrderNewsItem.locale) &&
        Objects.equals(this.source, playerOrderNewsItem.source) &&
        Objects.equals(this.publishedAt, playerOrderNewsItem.publishedAt) &&
        Objects.equals(this.expiresAt, playerOrderNewsItem.expiresAt) &&
        Objects.equals(this.links, playerOrderNewsItem.links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(newsId, orderId, effectId, headline, summary, severity, tags, locale, source, publishedAt, expiresAt, links);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderNewsItem {\n");
    sb.append("    newsId: ").append(toIndentedString(newsId)).append("\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    effectId: ").append(toIndentedString(effectId)).append("\n");
    sb.append("    headline: ").append(toIndentedString(headline)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    publishedAt: ").append(toIndentedString(publishedAt)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    links: ").append(toIndentedString(links)).append("\n");
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

