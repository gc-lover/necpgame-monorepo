package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.AudienceRules;
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
 * AnnouncementUpdateRequest
 */


public class AnnouncementUpdateRequest {

  private @Nullable String title;

  private @Nullable String summary;

  private @Nullable String body;

  @Valid
  private List<String> tags = new ArrayList<>();

  private @Nullable ChannelConfig channels;

  private @Nullable AudienceRules audience;

  @Valid
  private List<@Valid MediaAsset> mediaAssets = new ArrayList<>();

  public AnnouncementUpdateRequest title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public AnnouncementUpdateRequest summary(@Nullable String summary) {
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

  public AnnouncementUpdateRequest body(@Nullable String body) {
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

  public AnnouncementUpdateRequest tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public AnnouncementUpdateRequest addTagsItem(String tagsItem) {
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

  public AnnouncementUpdateRequest channels(@Nullable ChannelConfig channels) {
    this.channels = channels;
    return this;
  }

  /**
   * Get channels
   * @return channels
   */
  @Valid 
  @Schema(name = "channels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channels")
  public @Nullable ChannelConfig getChannels() {
    return channels;
  }

  public void setChannels(@Nullable ChannelConfig channels) {
    this.channels = channels;
  }

  public AnnouncementUpdateRequest audience(@Nullable AudienceRules audience) {
    this.audience = audience;
    return this;
  }

  /**
   * Get audience
   * @return audience
   */
  @Valid 
  @Schema(name = "audience", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("audience")
  public @Nullable AudienceRules getAudience() {
    return audience;
  }

  public void setAudience(@Nullable AudienceRules audience) {
    this.audience = audience;
  }

  public AnnouncementUpdateRequest mediaAssets(List<@Valid MediaAsset> mediaAssets) {
    this.mediaAssets = mediaAssets;
    return this;
  }

  public AnnouncementUpdateRequest addMediaAssetsItem(MediaAsset mediaAssetsItem) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnnouncementUpdateRequest announcementUpdateRequest = (AnnouncementUpdateRequest) o;
    return Objects.equals(this.title, announcementUpdateRequest.title) &&
        Objects.equals(this.summary, announcementUpdateRequest.summary) &&
        Objects.equals(this.body, announcementUpdateRequest.body) &&
        Objects.equals(this.tags, announcementUpdateRequest.tags) &&
        Objects.equals(this.channels, announcementUpdateRequest.channels) &&
        Objects.equals(this.audience, announcementUpdateRequest.audience) &&
        Objects.equals(this.mediaAssets, announcementUpdateRequest.mediaAssets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, summary, body, tags, channels, audience, mediaAssets);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnnouncementUpdateRequest {\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    channels: ").append(toIndentedString(channels)).append("\n");
    sb.append("    audience: ").append(toIndentedString(audience)).append("\n");
    sb.append("    mediaAssets: ").append(toIndentedString(mediaAssets)).append("\n");
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

