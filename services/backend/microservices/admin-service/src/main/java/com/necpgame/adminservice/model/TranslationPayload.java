package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * TranslationPayload
 */


public class TranslationPayload {

  private String locale;

  private String title;

  private @Nullable String summary;

  private String body;

  @Valid
  private List<@Valid MediaAsset> mediaAssets = new ArrayList<>();

  public TranslationPayload() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TranslationPayload(String locale, String title, String body) {
    this.locale = locale;
    this.title = title;
    this.body = body;
  }

  public TranslationPayload locale(String locale) {
    this.locale = locale;
    return this;
  }

  /**
   * Get locale
   * @return locale
   */
  @NotNull @Pattern(regexp = "^[a-z]{2}-[A-Z]{2}$") 
  @Schema(name = "locale", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("locale")
  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public TranslationPayload title(String title) {
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

  public TranslationPayload summary(@Nullable String summary) {
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

  public TranslationPayload body(String body) {
    this.body = body;
    return this;
  }

  /**
   * Get body
   * @return body
   */
  @NotNull 
  @Schema(name = "body", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("body")
  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public TranslationPayload mediaAssets(List<@Valid MediaAsset> mediaAssets) {
    this.mediaAssets = mediaAssets;
    return this;
  }

  public TranslationPayload addMediaAssetsItem(MediaAsset mediaAssetsItem) {
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
    TranslationPayload translationPayload = (TranslationPayload) o;
    return Objects.equals(this.locale, translationPayload.locale) &&
        Objects.equals(this.title, translationPayload.title) &&
        Objects.equals(this.summary, translationPayload.summary) &&
        Objects.equals(this.body, translationPayload.body) &&
        Objects.equals(this.mediaAssets, translationPayload.mediaAssets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locale, title, summary, body, mediaAssets);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TranslationPayload {\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
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

