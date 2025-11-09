package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.ChannelConfig;
import com.necpgame.adminservice.model.MediaAsset;
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
 * AnnouncementDraftRequest
 */


public class AnnouncementDraftRequest {

  private String title;

  private @Nullable String summary;

  private String body;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    NEWS("NEWS"),
    
    PATCH_NOTES("PATCH_NOTES"),
    
    MAINTENANCE("MAINTENANCE"),
    
    EVENT("EVENT"),
    
    EMERGENCY("EMERGENCY");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  @Valid
  private List<String> tags = new ArrayList<>();

  @Valid
  private List<@Valid MediaAsset> mediaAssets = new ArrayList<>();

  private @Nullable ChannelConfig defaultChannels;

  private @Nullable String authorId;

  public AnnouncementDraftRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AnnouncementDraftRequest(String title, String body, TypeEnum type) {
    this.title = title;
    this.body = body;
    this.type = type;
  }

  public AnnouncementDraftRequest title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  @NotNull @Size(max = 140) 
  @Schema(name = "title", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public AnnouncementDraftRequest summary(@Nullable String summary) {
    this.summary = summary;
    return this;
  }

  /**
   * Get summary
   * @return summary
   */
  @Size(max = 280) 
  @Schema(name = "summary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("summary")
  public @Nullable String getSummary() {
    return summary;
  }

  public void setSummary(@Nullable String summary) {
    this.summary = summary;
  }

  public AnnouncementDraftRequest body(String body) {
    this.body = body;
    return this;
  }

  /**
   * Markdown/HTML контент
   * @return body
   */
  @NotNull 
  @Schema(name = "body", description = "Markdown/HTML контент", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("body")
  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public AnnouncementDraftRequest type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public AnnouncementDraftRequest tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public AnnouncementDraftRequest addTagsItem(String tagsItem) {
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
  
  @Schema(name = "tags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tags")
  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public AnnouncementDraftRequest mediaAssets(List<@Valid MediaAsset> mediaAssets) {
    this.mediaAssets = mediaAssets;
    return this;
  }

  public AnnouncementDraftRequest addMediaAssetsItem(MediaAsset mediaAssetsItem) {
    if (this.mediaAssets == null) {
      this.mediaAssets = new ArrayList<>();
    }
    this.mediaAssets.add(mediaAssetsItem);
    return this;
  }

  /**
   * Get mediaAssets
   * @return mediaAssets
   */
  @Valid 
  @Schema(name = "mediaAssets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mediaAssets")
  public List<@Valid MediaAsset> getMediaAssets() {
    return mediaAssets;
  }

  public void setMediaAssets(List<@Valid MediaAsset> mediaAssets) {
    this.mediaAssets = mediaAssets;
  }

  public AnnouncementDraftRequest defaultChannels(@Nullable ChannelConfig defaultChannels) {
    this.defaultChannels = defaultChannels;
    return this;
  }

  /**
   * Get defaultChannels
   * @return defaultChannels
   */
  @Valid 
  @Schema(name = "defaultChannels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("defaultChannels")
  public @Nullable ChannelConfig getDefaultChannels() {
    return defaultChannels;
  }

  public void setDefaultChannels(@Nullable ChannelConfig defaultChannels) {
    this.defaultChannels = defaultChannels;
  }

  public AnnouncementDraftRequest authorId(@Nullable String authorId) {
    this.authorId = authorId;
    return this;
  }

  /**
   * Get authorId
   * @return authorId
   */
  
  @Schema(name = "authorId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("authorId")
  public @Nullable String getAuthorId() {
    return authorId;
  }

  public void setAuthorId(@Nullable String authorId) {
    this.authorId = authorId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnnouncementDraftRequest announcementDraftRequest = (AnnouncementDraftRequest) o;
    return Objects.equals(this.title, announcementDraftRequest.title) &&
        Objects.equals(this.summary, announcementDraftRequest.summary) &&
        Objects.equals(this.body, announcementDraftRequest.body) &&
        Objects.equals(this.type, announcementDraftRequest.type) &&
        Objects.equals(this.tags, announcementDraftRequest.tags) &&
        Objects.equals(this.mediaAssets, announcementDraftRequest.mediaAssets) &&
        Objects.equals(this.defaultChannels, announcementDraftRequest.defaultChannels) &&
        Objects.equals(this.authorId, announcementDraftRequest.authorId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, summary, body, type, tags, mediaAssets, defaultChannels, authorId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnnouncementDraftRequest {\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    mediaAssets: ").append(toIndentedString(mediaAssets)).append("\n");
    sb.append("    defaultChannels: ").append(toIndentedString(defaultChannels)).append("\n");
    sb.append("    authorId: ").append(toIndentedString(authorId)).append("\n");
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

