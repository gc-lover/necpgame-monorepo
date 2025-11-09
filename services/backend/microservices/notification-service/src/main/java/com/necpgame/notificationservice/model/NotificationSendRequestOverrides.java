package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.notificationservice.model.NotificationSendRequestOverridesMediaInner;
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
 * NotificationSendRequestOverrides
 */

@JsonTypeName("NotificationSendRequest_overrides")

public class NotificationSendRequestOverrides {

  private @Nullable String subject;

  private @Nullable String message;

  @Valid
  private List<@Valid NotificationSendRequestOverridesMediaInner> media = new ArrayList<>();

  public NotificationSendRequestOverrides subject(@Nullable String subject) {
    this.subject = subject;
    return this;
  }

  /**
   * Get subject
   * @return subject
   */
  
  @Schema(name = "subject", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subject")
  public @Nullable String getSubject() {
    return subject;
  }

  public void setSubject(@Nullable String subject) {
    this.subject = subject;
  }

  public NotificationSendRequestOverrides message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  
  @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public NotificationSendRequestOverrides media(List<@Valid NotificationSendRequestOverridesMediaInner> media) {
    this.media = media;
    return this;
  }

  public NotificationSendRequestOverrides addMediaItem(NotificationSendRequestOverridesMediaInner mediaItem) {
    if (this.media == null) {
      this.media = new ArrayList<>();
    }
    this.media.add(mediaItem);
    return this;
  }

  /**
   * Get media
   * @return media
   */
  @Valid 
  @Schema(name = "media", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("media")
  public List<@Valid NotificationSendRequestOverridesMediaInner> getMedia() {
    return media;
  }

  public void setMedia(List<@Valid NotificationSendRequestOverridesMediaInner> media) {
    this.media = media;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationSendRequestOverrides notificationSendRequestOverrides = (NotificationSendRequestOverrides) o;
    return Objects.equals(this.subject, notificationSendRequestOverrides.subject) &&
        Objects.equals(this.message, notificationSendRequestOverrides.message) &&
        Objects.equals(this.media, notificationSendRequestOverrides.media);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subject, message, media);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationSendRequestOverrides {\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    media: ").append(toIndentedString(media)).append("\n");
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

