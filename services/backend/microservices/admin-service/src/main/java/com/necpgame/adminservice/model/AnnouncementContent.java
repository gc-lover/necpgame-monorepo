package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.MediaAsset;
import java.net.URI;
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
 * AnnouncementContent
 */


public class AnnouncementContent {

  private @Nullable String body;

  private @Nullable String summary;

  @Valid
  private List<@Valid MediaAsset> mediaAssets = new ArrayList<>();

  @Valid
  private List<URI> attachments = new ArrayList<>();

  public AnnouncementContent body(@Nullable String body) {
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

  public AnnouncementContent summary(@Nullable String summary) {
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

  public AnnouncementContent mediaAssets(List<@Valid MediaAsset> mediaAssets) {
    this.mediaAssets = mediaAssets;
    return this;
  }

  public AnnouncementContent addMediaAssetsItem(MediaAsset mediaAssetsItem) {
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

  public AnnouncementContent attachments(List<URI> attachments) {
    this.attachments = attachments;
    return this;
  }

  public AnnouncementContent addAttachmentsItem(URI attachmentsItem) {
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
  public List<URI> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<URI> attachments) {
    this.attachments = attachments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnnouncementContent announcementContent = (AnnouncementContent) o;
    return Objects.equals(this.body, announcementContent.body) &&
        Objects.equals(this.summary, announcementContent.summary) &&
        Objects.equals(this.mediaAssets, announcementContent.mediaAssets) &&
        Objects.equals(this.attachments, announcementContent.attachments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(body, summary, mediaAssets, attachments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnnouncementContent {\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    mediaAssets: ").append(toIndentedString(mediaAssets)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
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

