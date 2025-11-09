package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * PlayerOrderReviewUpdateRequest
 */


public class PlayerOrderReviewUpdateRequest {

  private @Nullable String text;

  @Valid
  private List<@Size(max = 32)String> tags = new ArrayList<>();

  /**
   * Gets or Sets visibility
   */
  public enum VisibilityEnum {
    PUBLIC("public"),
    
    PRIVATE("private"),
    
    TRUSTED_ONLY("trusted_only");

    private final String value;

    VisibilityEnum(String value) {
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
    public static VisibilityEnum fromValue(String value) {
      for (VisibilityEnum b : VisibilityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable VisibilityEnum visibility;

  private @Nullable String locale;

  @Valid
  private List<URI> attachments = new ArrayList<>();

  public PlayerOrderReviewUpdateRequest text(@Nullable String text) {
    this.text = text;
    return this;
  }

  /**
   * Get text
   * @return text
   */
  @Size(min = 16, max = 2000) 
  @Schema(name = "text", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("text")
  public @Nullable String getText() {
    return text;
  }

  public void setText(@Nullable String text) {
    this.text = text;
  }

  public PlayerOrderReviewUpdateRequest tags(List<@Size(max = 32)String> tags) {
    this.tags = tags;
    return this;
  }

  public PlayerOrderReviewUpdateRequest addTagsItem(String tagsItem) {
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
  public List<@Size(max = 32)String> getTags() {
    return tags;
  }

  public void setTags(List<@Size(max = 32)String> tags) {
    this.tags = tags;
  }

  public PlayerOrderReviewUpdateRequest visibility(@Nullable VisibilityEnum visibility) {
    this.visibility = visibility;
    return this;
  }

  /**
   * Get visibility
   * @return visibility
   */
  
  @Schema(name = "visibility", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("visibility")
  public @Nullable VisibilityEnum getVisibility() {
    return visibility;
  }

  public void setVisibility(@Nullable VisibilityEnum visibility) {
    this.visibility = visibility;
  }

  public PlayerOrderReviewUpdateRequest locale(@Nullable String locale) {
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

  public PlayerOrderReviewUpdateRequest attachments(List<URI> attachments) {
    this.attachments = attachments;
    return this;
  }

  public PlayerOrderReviewUpdateRequest addAttachmentsItem(URI attachmentsItem) {
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
    PlayerOrderReviewUpdateRequest playerOrderReviewUpdateRequest = (PlayerOrderReviewUpdateRequest) o;
    return Objects.equals(this.text, playerOrderReviewUpdateRequest.text) &&
        Objects.equals(this.tags, playerOrderReviewUpdateRequest.tags) &&
        Objects.equals(this.visibility, playerOrderReviewUpdateRequest.visibility) &&
        Objects.equals(this.locale, playerOrderReviewUpdateRequest.locale) &&
        Objects.equals(this.attachments, playerOrderReviewUpdateRequest.attachments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(text, tags, visibility, locale, attachments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderReviewUpdateRequest {\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    visibility: ").append(toIndentedString(visibility)).append("\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
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

