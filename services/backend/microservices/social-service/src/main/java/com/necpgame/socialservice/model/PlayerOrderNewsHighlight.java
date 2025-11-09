package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.PlayerOrderNewsAttachment;
import com.necpgame.socialservice.model.PlayerOrderNewsHighlightCta;
import com.necpgame.socialservice.model.PlayerOrderNewsTag;
import java.math.BigDecimal;
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
 * PlayerOrderNewsHighlight
 */


public class PlayerOrderNewsHighlight {

  private UUID highlightId;

  private String title;

  private @Nullable String description;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    SPOTLIGHT("spotlight"),
    
    CRISIS("crisis"),
    
    TOURNAMENT("tournament"),
    
    MILESTONE("milestone"),
    
    PROMOTIONAL("promotional");

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

  private @Nullable PlayerOrderNewsAttachment media;

  private @Nullable PlayerOrderNewsHighlightCta cta;

  private @Nullable BigDecimal score;

  @Valid
  private List<@Valid PlayerOrderNewsTag> tags = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime publishedAt;

  public PlayerOrderNewsHighlight() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderNewsHighlight(UUID highlightId, String title, TypeEnum type, OffsetDateTime publishedAt) {
    this.highlightId = highlightId;
    this.title = title;
    this.type = type;
    this.publishedAt = publishedAt;
  }

  public PlayerOrderNewsHighlight highlightId(UUID highlightId) {
    this.highlightId = highlightId;
    return this;
  }

  /**
   * Get highlightId
   * @return highlightId
   */
  @NotNull @Valid 
  @Schema(name = "highlightId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("highlightId")
  public UUID getHighlightId() {
    return highlightId;
  }

  public void setHighlightId(UUID highlightId) {
    this.highlightId = highlightId;
  }

  public PlayerOrderNewsHighlight title(String title) {
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

  public PlayerOrderNewsHighlight description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public PlayerOrderNewsHighlight type(TypeEnum type) {
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

  public PlayerOrderNewsHighlight media(@Nullable PlayerOrderNewsAttachment media) {
    this.media = media;
    return this;
  }

  /**
   * Get media
   * @return media
   */
  @Valid 
  @Schema(name = "media", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("media")
  public @Nullable PlayerOrderNewsAttachment getMedia() {
    return media;
  }

  public void setMedia(@Nullable PlayerOrderNewsAttachment media) {
    this.media = media;
  }

  public PlayerOrderNewsHighlight cta(@Nullable PlayerOrderNewsHighlightCta cta) {
    this.cta = cta;
    return this;
  }

  /**
   * Get cta
   * @return cta
   */
  @Valid 
  @Schema(name = "cta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cta")
  public @Nullable PlayerOrderNewsHighlightCta getCta() {
    return cta;
  }

  public void setCta(@Nullable PlayerOrderNewsHighlightCta cta) {
    this.cta = cta;
  }

  public PlayerOrderNewsHighlight score(@Nullable BigDecimal score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * @return score
   */
  @Valid 
  @Schema(name = "score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("score")
  public @Nullable BigDecimal getScore() {
    return score;
  }

  public void setScore(@Nullable BigDecimal score) {
    this.score = score;
  }

  public PlayerOrderNewsHighlight tags(List<@Valid PlayerOrderNewsTag> tags) {
    this.tags = tags;
    return this;
  }

  public PlayerOrderNewsHighlight addTagsItem(PlayerOrderNewsTag tagsItem) {
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
  @Valid 
  @Schema(name = "tags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tags")
  public List<@Valid PlayerOrderNewsTag> getTags() {
    return tags;
  }

  public void setTags(List<@Valid PlayerOrderNewsTag> tags) {
    this.tags = tags;
  }

  public PlayerOrderNewsHighlight publishedAt(OffsetDateTime publishedAt) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderNewsHighlight playerOrderNewsHighlight = (PlayerOrderNewsHighlight) o;
    return Objects.equals(this.highlightId, playerOrderNewsHighlight.highlightId) &&
        Objects.equals(this.title, playerOrderNewsHighlight.title) &&
        Objects.equals(this.description, playerOrderNewsHighlight.description) &&
        Objects.equals(this.type, playerOrderNewsHighlight.type) &&
        Objects.equals(this.media, playerOrderNewsHighlight.media) &&
        Objects.equals(this.cta, playerOrderNewsHighlight.cta) &&
        Objects.equals(this.score, playerOrderNewsHighlight.score) &&
        Objects.equals(this.tags, playerOrderNewsHighlight.tags) &&
        Objects.equals(this.publishedAt, playerOrderNewsHighlight.publishedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(highlightId, title, description, type, media, cta, score, tags, publishedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderNewsHighlight {\n");
    sb.append("    highlightId: ").append(toIndentedString(highlightId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    media: ").append(toIndentedString(media)).append("\n");
    sb.append("    cta: ").append(toIndentedString(cta)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    publishedAt: ").append(toIndentedString(publishedAt)).append("\n");
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

