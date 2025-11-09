package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ReviewRatings;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlayerOrderReviewCreateRequest
 */


public class PlayerOrderReviewCreateRequest {

  private UUID orderId;

  private UUID reviewerId;

  private UUID targetId;

  /**
   * Gets or Sets role
   */
  public enum RoleEnum {
    EXECUTOR("executor"),
    
    CLIENT("client");

    private final String value;

    RoleEnum(String value) {
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
    public static RoleEnum fromValue(String value) {
      for (RoleEnum b : RoleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private RoleEnum role;

  private ReviewRatings ratings;

  private String text;

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

  private VisibilityEnum visibility = VisibilityEnum.PUBLIC;

  private @Nullable String locale;

  @Valid
  private List<URI> attachments = new ArrayList<>();

  @Valid
  private Map<String, Object> metadata = new HashMap<>();

  public PlayerOrderReviewCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderReviewCreateRequest(UUID orderId, UUID reviewerId, UUID targetId, RoleEnum role, ReviewRatings ratings, String text) {
    this.orderId = orderId;
    this.reviewerId = reviewerId;
    this.targetId = targetId;
    this.role = role;
    this.ratings = ratings;
    this.text = text;
  }

  public PlayerOrderReviewCreateRequest orderId(UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Get orderId
   * @return orderId
   */
  @NotNull @Valid 
  @Schema(name = "orderId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("orderId")
  public UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public PlayerOrderReviewCreateRequest reviewerId(UUID reviewerId) {
    this.reviewerId = reviewerId;
    return this;
  }

  /**
   * Get reviewerId
   * @return reviewerId
   */
  @NotNull @Valid 
  @Schema(name = "reviewerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reviewerId")
  public UUID getReviewerId() {
    return reviewerId;
  }

  public void setReviewerId(UUID reviewerId) {
    this.reviewerId = reviewerId;
  }

  public PlayerOrderReviewCreateRequest targetId(UUID targetId) {
    this.targetId = targetId;
    return this;
  }

  /**
   * Get targetId
   * @return targetId
   */
  @NotNull @Valid 
  @Schema(name = "targetId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("targetId")
  public UUID getTargetId() {
    return targetId;
  }

  public void setTargetId(UUID targetId) {
    this.targetId = targetId;
  }

  public PlayerOrderReviewCreateRequest role(RoleEnum role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  @NotNull 
  @Schema(name = "role", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("role")
  public RoleEnum getRole() {
    return role;
  }

  public void setRole(RoleEnum role) {
    this.role = role;
  }

  public PlayerOrderReviewCreateRequest ratings(ReviewRatings ratings) {
    this.ratings = ratings;
    return this;
  }

  /**
   * Get ratings
   * @return ratings
   */
  @NotNull @Valid 
  @Schema(name = "ratings", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ratings")
  public ReviewRatings getRatings() {
    return ratings;
  }

  public void setRatings(ReviewRatings ratings) {
    this.ratings = ratings;
  }

  public PlayerOrderReviewCreateRequest text(String text) {
    this.text = text;
    return this;
  }

  /**
   * Get text
   * @return text
   */
  @NotNull @Size(min = 16, max = 2000) 
  @Schema(name = "text", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("text")
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public PlayerOrderReviewCreateRequest tags(List<@Size(max = 32)String> tags) {
    this.tags = tags;
    return this;
  }

  public PlayerOrderReviewCreateRequest addTagsItem(String tagsItem) {
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

  public PlayerOrderReviewCreateRequest visibility(VisibilityEnum visibility) {
    this.visibility = visibility;
    return this;
  }

  /**
   * Get visibility
   * @return visibility
   */
  
  @Schema(name = "visibility", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("visibility")
  public VisibilityEnum getVisibility() {
    return visibility;
  }

  public void setVisibility(VisibilityEnum visibility) {
    this.visibility = visibility;
  }

  public PlayerOrderReviewCreateRequest locale(@Nullable String locale) {
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

  public PlayerOrderReviewCreateRequest attachments(List<URI> attachments) {
    this.attachments = attachments;
    return this;
  }

  public PlayerOrderReviewCreateRequest addAttachmentsItem(URI attachmentsItem) {
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

  public PlayerOrderReviewCreateRequest metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public PlayerOrderReviewCreateRequest putMetadataItem(String key, Object metadataItem) {
    if (this.metadata == null) {
      this.metadata = new HashMap<>();
    }
    this.metadata.put(key, metadataItem);
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   */
  
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderReviewCreateRequest playerOrderReviewCreateRequest = (PlayerOrderReviewCreateRequest) o;
    return Objects.equals(this.orderId, playerOrderReviewCreateRequest.orderId) &&
        Objects.equals(this.reviewerId, playerOrderReviewCreateRequest.reviewerId) &&
        Objects.equals(this.targetId, playerOrderReviewCreateRequest.targetId) &&
        Objects.equals(this.role, playerOrderReviewCreateRequest.role) &&
        Objects.equals(this.ratings, playerOrderReviewCreateRequest.ratings) &&
        Objects.equals(this.text, playerOrderReviewCreateRequest.text) &&
        Objects.equals(this.tags, playerOrderReviewCreateRequest.tags) &&
        Objects.equals(this.visibility, playerOrderReviewCreateRequest.visibility) &&
        Objects.equals(this.locale, playerOrderReviewCreateRequest.locale) &&
        Objects.equals(this.attachments, playerOrderReviewCreateRequest.attachments) &&
        Objects.equals(this.metadata, playerOrderReviewCreateRequest.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, reviewerId, targetId, role, ratings, text, tags, visibility, locale, attachments, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderReviewCreateRequest {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    reviewerId: ").append(toIndentedString(reviewerId)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    ratings: ").append(toIndentedString(ratings)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    visibility: ").append(toIndentedString(visibility)).append("\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

