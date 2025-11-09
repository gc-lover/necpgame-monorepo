package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.AudienceRules;
import com.necpgame.adminservice.model.ChannelConfig;
import java.time.OffsetDateTime;
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
 * Announcement
 */


public class Announcement {

  private String announcementId;

  private String title;

  private String type;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    DRAFT("draft"),
    
    SCHEDULED("scheduled"),
    
    ACTIVE("active"),
    
    ARCHIVED("archived"),
    
    CANCELLED("cancelled");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endAt;

  private @Nullable ChannelConfig channels;

  private @Nullable AudienceRules audience;

  private @Nullable String authorId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public Announcement() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Announcement(String announcementId, String title, String type, StatusEnum status, OffsetDateTime createdAt) {
    this.announcementId = announcementId;
    this.title = title;
    this.type = type;
    this.status = status;
    this.createdAt = createdAt;
  }

  public Announcement announcementId(String announcementId) {
    this.announcementId = announcementId;
    return this;
  }

  /**
   * Get announcementId
   * @return announcementId
   */
  @NotNull 
  @Schema(name = "announcementId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("announcementId")
  public String getAnnouncementId() {
    return announcementId;
  }

  public void setAnnouncementId(String announcementId) {
    this.announcementId = announcementId;
  }

  public Announcement title(String title) {
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

  public Announcement type(String type) {
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
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Announcement status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public Announcement startAt(@Nullable OffsetDateTime startAt) {
    this.startAt = startAt;
    return this;
  }

  /**
   * Get startAt
   * @return startAt
   */
  @Valid 
  @Schema(name = "startAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startAt")
  public @Nullable OffsetDateTime getStartAt() {
    return startAt;
  }

  public void setStartAt(@Nullable OffsetDateTime startAt) {
    this.startAt = startAt;
  }

  public Announcement endAt(@Nullable OffsetDateTime endAt) {
    this.endAt = endAt;
    return this;
  }

  /**
   * Get endAt
   * @return endAt
   */
  @Valid 
  @Schema(name = "endAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endAt")
  public @Nullable OffsetDateTime getEndAt() {
    return endAt;
  }

  public void setEndAt(@Nullable OffsetDateTime endAt) {
    this.endAt = endAt;
  }

  public Announcement channels(@Nullable ChannelConfig channels) {
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

  public Announcement audience(@Nullable AudienceRules audience) {
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

  public Announcement authorId(@Nullable String authorId) {
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

  public Announcement createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @NotNull @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Announcement updatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public @Nullable OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Announcement announcement = (Announcement) o;
    return Objects.equals(this.announcementId, announcement.announcementId) &&
        Objects.equals(this.title, announcement.title) &&
        Objects.equals(this.type, announcement.type) &&
        Objects.equals(this.status, announcement.status) &&
        Objects.equals(this.startAt, announcement.startAt) &&
        Objects.equals(this.endAt, announcement.endAt) &&
        Objects.equals(this.channels, announcement.channels) &&
        Objects.equals(this.audience, announcement.audience) &&
        Objects.equals(this.authorId, announcement.authorId) &&
        Objects.equals(this.createdAt, announcement.createdAt) &&
        Objects.equals(this.updatedAt, announcement.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(announcementId, title, type, status, startAt, endAt, channels, audience, authorId, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Announcement {\n");
    sb.append("    announcementId: ").append(toIndentedString(announcementId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    startAt: ").append(toIndentedString(startAt)).append("\n");
    sb.append("    endAt: ").append(toIndentedString(endAt)).append("\n");
    sb.append("    channels: ").append(toIndentedString(channels)).append("\n");
    sb.append("    audience: ").append(toIndentedString(audience)).append("\n");
    sb.append("    authorId: ").append(toIndentedString(authorId)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

