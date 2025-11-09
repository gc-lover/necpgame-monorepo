package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.PlayerMention;
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
 * FormatPreviewResponse
 */


public class FormatPreviewResponse {

  private String html;

  @Valid
  private List<@Valid PlayerMention> mentions = new ArrayList<>();

  @Valid
  private List<String> emotes = new ArrayList<>();

  private @Nullable Boolean sanitized;

  public FormatPreviewResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FormatPreviewResponse(String html) {
    this.html = html;
  }

  public FormatPreviewResponse html(String html) {
    this.html = html;
    return this;
  }

  /**
   * Get html
   * @return html
   */
  @NotNull 
  @Schema(name = "html", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("html")
  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
  }

  public FormatPreviewResponse mentions(List<@Valid PlayerMention> mentions) {
    this.mentions = mentions;
    return this;
  }

  public FormatPreviewResponse addMentionsItem(PlayerMention mentionsItem) {
    if (this.mentions == null) {
      this.mentions = new ArrayList<>();
    }
    this.mentions.add(mentionsItem);
    return this;
  }

  /**
   * Get mentions
   * @return mentions
   */
  @Valid 
  @Schema(name = "mentions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mentions")
  public List<@Valid PlayerMention> getMentions() {
    return mentions;
  }

  public void setMentions(List<@Valid PlayerMention> mentions) {
    this.mentions = mentions;
  }

  public FormatPreviewResponse emotes(List<String> emotes) {
    this.emotes = emotes;
    return this;
  }

  public FormatPreviewResponse addEmotesItem(String emotesItem) {
    if (this.emotes == null) {
      this.emotes = new ArrayList<>();
    }
    this.emotes.add(emotesItem);
    return this;
  }

  /**
   * Get emotes
   * @return emotes
   */
  
  @Schema(name = "emotes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("emotes")
  public List<String> getEmotes() {
    return emotes;
  }

  public void setEmotes(List<String> emotes) {
    this.emotes = emotes;
  }

  public FormatPreviewResponse sanitized(@Nullable Boolean sanitized) {
    this.sanitized = sanitized;
    return this;
  }

  /**
   * Get sanitized
   * @return sanitized
   */
  
  @Schema(name = "sanitized", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sanitized")
  public @Nullable Boolean getSanitized() {
    return sanitized;
  }

  public void setSanitized(@Nullable Boolean sanitized) {
    this.sanitized = sanitized;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FormatPreviewResponse formatPreviewResponse = (FormatPreviewResponse) o;
    return Objects.equals(this.html, formatPreviewResponse.html) &&
        Objects.equals(this.mentions, formatPreviewResponse.mentions) &&
        Objects.equals(this.emotes, formatPreviewResponse.emotes) &&
        Objects.equals(this.sanitized, formatPreviewResponse.sanitized);
  }

  @Override
  public int hashCode() {
    return Objects.hash(html, mentions, emotes, sanitized);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FormatPreviewResponse {\n");
    sb.append("    html: ").append(toIndentedString(html)).append("\n");
    sb.append("    mentions: ").append(toIndentedString(mentions)).append("\n");
    sb.append("    emotes: ").append(toIndentedString(emotes)).append("\n");
    sb.append("    sanitized: ").append(toIndentedString(sanitized)).append("\n");
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

