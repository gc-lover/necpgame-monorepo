package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.PlayerOrderNewsAttachment;
import com.necpgame.socialservice.model.PlayerOrderNewsItemMetrics;
import com.necpgame.socialservice.model.PlayerOrderNewsTag;
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

  private String title;

  private String summary;

  private @Nullable String body;

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    INFO("info"),
    
    CAUTION("caution"),
    
    WARNING("warning"),
    
    CRITICAL("critical");

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
  private List<UUID> orderIds = new ArrayList<>();

  @Valid
  private List<UUID> cityIds = new ArrayList<>();

  @Valid
  private List<UUID> factionIds = new ArrayList<>();

  @Valid
  private List<@Valid PlayerOrderNewsTag> tags = new ArrayList<>();

  @Valid
  private List<@Valid PlayerOrderNewsAttachment> attachments = new ArrayList<>();

  private @Nullable PlayerOrderNewsItemMetrics metrics;

  /**
   * Gets or Sets source
   */
  public enum SourceEnum {
    SYSTEM("system"),
    
    MODERATOR("moderator"),
    
    ECONOMY_SERVICE("economy-service"),
    
    WORLD_SERVICE("world-service"),
    
    PLAYER("player");

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

  private @Nullable String language;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime publishedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  @Valid
  private List<UUID> relatedEffects = new ArrayList<>();

  @Valid
  private List<String> relatedIndexes = new ArrayList<>();

  private @Nullable Integer priority;

  public PlayerOrderNewsItem() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderNewsItem(UUID newsId, String title, String summary, SeverityEnum severity, List<@Valid PlayerOrderNewsTag> tags, OffsetDateTime publishedAt) {
    this.newsId = newsId;
    this.title = title;
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

  public PlayerOrderNewsItem title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  @NotNull 
  @Schema(name = "title", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
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

  public PlayerOrderNewsItem body(@Nullable String body) {
    this.body = body;
    return this;
  }

  /**
   * Get body
   * @return body
   */
  
  @Schema(name = "body", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("body")
  public @Nullable String getBody() {
    return body;
  }

  public void setBody(@Nullable String body) {
    this.body = body;
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

  public PlayerOrderNewsItem orderIds(List<UUID> orderIds) {
    this.orderIds = orderIds;
    return this;
  }

  public PlayerOrderNewsItem addOrderIdsItem(UUID orderIdsItem) {
    if (this.orderIds == null) {
      this.orderIds = new ArrayList<>();
    }
    this.orderIds.add(orderIdsItem);
    return this;
  }

  /**
   * Get orderIds
   * @return orderIds
   */
  @Valid 
  @Schema(name = "orderIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("orderIds")
  public List<UUID> getOrderIds() {
    return orderIds;
  }

  public void setOrderIds(List<UUID> orderIds) {
    this.orderIds = orderIds;
  }

  public PlayerOrderNewsItem cityIds(List<UUID> cityIds) {
    this.cityIds = cityIds;
    return this;
  }

  public PlayerOrderNewsItem addCityIdsItem(UUID cityIdsItem) {
    if (this.cityIds == null) {
      this.cityIds = new ArrayList<>();
    }
    this.cityIds.add(cityIdsItem);
    return this;
  }

  /**
   * Get cityIds
   * @return cityIds
   */
  @Valid 
  @Schema(name = "cityIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cityIds")
  public List<UUID> getCityIds() {
    return cityIds;
  }

  public void setCityIds(List<UUID> cityIds) {
    this.cityIds = cityIds;
  }

  public PlayerOrderNewsItem factionIds(List<UUID> factionIds) {
    this.factionIds = factionIds;
    return this;
  }

  public PlayerOrderNewsItem addFactionIdsItem(UUID factionIdsItem) {
    if (this.factionIds == null) {
      this.factionIds = new ArrayList<>();
    }
    this.factionIds.add(factionIdsItem);
    return this;
  }

  /**
   * Get factionIds
   * @return factionIds
   */
  @Valid 
  @Schema(name = "factionIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factionIds")
  public List<UUID> getFactionIds() {
    return factionIds;
  }

  public void setFactionIds(List<UUID> factionIds) {
    this.factionIds = factionIds;
  }

  public PlayerOrderNewsItem tags(List<@Valid PlayerOrderNewsTag> tags) {
    this.tags = tags;
    return this;
  }

  public PlayerOrderNewsItem addTagsItem(PlayerOrderNewsTag tagsItem) {
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
  @NotNull @Valid 
  @Schema(name = "tags", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tags")
  public List<@Valid PlayerOrderNewsTag> getTags() {
    return tags;
  }

  public void setTags(List<@Valid PlayerOrderNewsTag> tags) {
    this.tags = tags;
  }

  public PlayerOrderNewsItem attachments(List<@Valid PlayerOrderNewsAttachment> attachments) {
    this.attachments = attachments;
    return this;
  }

  public PlayerOrderNewsItem addAttachmentsItem(PlayerOrderNewsAttachment attachmentsItem) {
    if (this.attachments == null) {
      this.attachments = new ArrayList<>();
    }
    this.attachments.add(attachmentsItem);
    return this;
  }

  /**
   * Get attachments
   * @return attachments
   */
  @Valid 
  @Schema(name = "attachments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attachments")
  public List<@Valid PlayerOrderNewsAttachment> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<@Valid PlayerOrderNewsAttachment> attachments) {
    this.attachments = attachments;
  }

  public PlayerOrderNewsItem metrics(@Nullable PlayerOrderNewsItemMetrics metrics) {
    this.metrics = metrics;
    return this;
  }

  /**
   * Get metrics
   * @return metrics
   */
  @Valid 
  @Schema(name = "metrics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metrics")
  public @Nullable PlayerOrderNewsItemMetrics getMetrics() {
    return metrics;
  }

  public void setMetrics(@Nullable PlayerOrderNewsItemMetrics metrics) {
    this.metrics = metrics;
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

  public PlayerOrderNewsItem language(@Nullable String language) {
    this.language = language;
    return this;
  }

  /**
   * Get language
   * @return language
   */
  
  @Schema(name = "language", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("language")
  public @Nullable String getLanguage() {
    return language;
  }

  public void setLanguage(@Nullable String language) {
    this.language = language;
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

  public PlayerOrderNewsItem relatedEffects(List<UUID> relatedEffects) {
    this.relatedEffects = relatedEffects;
    return this;
  }

  public PlayerOrderNewsItem addRelatedEffectsItem(UUID relatedEffectsItem) {
    if (this.relatedEffects == null) {
      this.relatedEffects = new ArrayList<>();
    }
    this.relatedEffects.add(relatedEffectsItem);
    return this;
  }

  /**
   * Get relatedEffects
   * @return relatedEffects
   */
  @Valid 
  @Schema(name = "relatedEffects", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relatedEffects")
  public List<UUID> getRelatedEffects() {
    return relatedEffects;
  }

  public void setRelatedEffects(List<UUID> relatedEffects) {
    this.relatedEffects = relatedEffects;
  }

  public PlayerOrderNewsItem relatedIndexes(List<String> relatedIndexes) {
    this.relatedIndexes = relatedIndexes;
    return this;
  }

  public PlayerOrderNewsItem addRelatedIndexesItem(String relatedIndexesItem) {
    if (this.relatedIndexes == null) {
      this.relatedIndexes = new ArrayList<>();
    }
    this.relatedIndexes.add(relatedIndexesItem);
    return this;
  }

  /**
   * Get relatedIndexes
   * @return relatedIndexes
   */
  
  @Schema(name = "relatedIndexes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relatedIndexes")
  public List<String> getRelatedIndexes() {
    return relatedIndexes;
  }

  public void setRelatedIndexes(List<String> relatedIndexes) {
    this.relatedIndexes = relatedIndexes;
  }

  public PlayerOrderNewsItem priority(@Nullable Integer priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * minimum: 0
   * maximum: 100
   * @return priority
   */
  @Min(value = 0) @Max(value = 100) 
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public @Nullable Integer getPriority() {
    return priority;
  }

  public void setPriority(@Nullable Integer priority) {
    this.priority = priority;
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
        Objects.equals(this.title, playerOrderNewsItem.title) &&
        Objects.equals(this.summary, playerOrderNewsItem.summary) &&
        Objects.equals(this.body, playerOrderNewsItem.body) &&
        Objects.equals(this.severity, playerOrderNewsItem.severity) &&
        Objects.equals(this.orderIds, playerOrderNewsItem.orderIds) &&
        Objects.equals(this.cityIds, playerOrderNewsItem.cityIds) &&
        Objects.equals(this.factionIds, playerOrderNewsItem.factionIds) &&
        Objects.equals(this.tags, playerOrderNewsItem.tags) &&
        Objects.equals(this.attachments, playerOrderNewsItem.attachments) &&
        Objects.equals(this.metrics, playerOrderNewsItem.metrics) &&
        Objects.equals(this.source, playerOrderNewsItem.source) &&
        Objects.equals(this.language, playerOrderNewsItem.language) &&
        Objects.equals(this.publishedAt, playerOrderNewsItem.publishedAt) &&
        Objects.equals(this.expiresAt, playerOrderNewsItem.expiresAt) &&
        Objects.equals(this.relatedEffects, playerOrderNewsItem.relatedEffects) &&
        Objects.equals(this.relatedIndexes, playerOrderNewsItem.relatedIndexes) &&
        Objects.equals(this.priority, playerOrderNewsItem.priority);
  }

  @Override
  public int hashCode() {
    return Objects.hash(newsId, title, summary, body, severity, orderIds, cityIds, factionIds, tags, attachments, metrics, source, language, publishedAt, expiresAt, relatedEffects, relatedIndexes, priority);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderNewsItem {\n");
    sb.append("    newsId: ").append(toIndentedString(newsId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    orderIds: ").append(toIndentedString(orderIds)).append("\n");
    sb.append("    cityIds: ").append(toIndentedString(cityIds)).append("\n");
    sb.append("    factionIds: ").append(toIndentedString(factionIds)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
    sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
    sb.append("    publishedAt: ").append(toIndentedString(publishedAt)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    relatedEffects: ").append(toIndentedString(relatedEffects)).append("\n");
    sb.append("    relatedIndexes: ").append(toIndentedString(relatedIndexes)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
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

