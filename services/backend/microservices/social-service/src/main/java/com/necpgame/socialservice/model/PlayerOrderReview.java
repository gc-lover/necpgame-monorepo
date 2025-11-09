package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ReviewFlag;
import com.necpgame.socialservice.model.ReviewModeration;
import com.necpgame.socialservice.model.ReviewRatings;
import java.net.URI;
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
 * PlayerOrderReview
 */


public class PlayerOrderReview {

  private UUID reviewId;

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
  private List<@Valid ReviewFlag> flags = new ArrayList<>();

  private ReviewModeration moderation;

  private @Nullable Float sentimentScore;

  private @Nullable String locale;

  @Valid
  private List<URI> attachments = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("active"),
    
    ARCHIVED("archived"),
    
    REMOVED("removed");

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

  private @Nullable StatusEnum status;

  public PlayerOrderReview() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderReview(UUID reviewId, UUID orderId, UUID reviewerId, UUID targetId, RoleEnum role, ReviewRatings ratings, String text, ReviewModeration moderation, OffsetDateTime createdAt) {
    this.reviewId = reviewId;
    this.orderId = orderId;
    this.reviewerId = reviewerId;
    this.targetId = targetId;
    this.role = role;
    this.ratings = ratings;
    this.text = text;
    this.moderation = moderation;
    this.createdAt = createdAt;
  }

  public PlayerOrderReview reviewId(UUID reviewId) {
    this.reviewId = reviewId;
    return this;
  }

  /**
   * Get reviewId
   * @return reviewId
   */
  @NotNull @Valid 
  @Schema(name = "reviewId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reviewId")
  public UUID getReviewId() {
    return reviewId;
  }

  public void setReviewId(UUID reviewId) {
    this.reviewId = reviewId;
  }

  public PlayerOrderReview orderId(UUID orderId) {
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

  public PlayerOrderReview reviewerId(UUID reviewerId) {
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

  public PlayerOrderReview targetId(UUID targetId) {
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

  public PlayerOrderReview role(RoleEnum role) {
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

  public PlayerOrderReview ratings(ReviewRatings ratings) {
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

  public PlayerOrderReview text(String text) {
    this.text = text;
    return this;
  }

  /**
   * Get text
   * @return text
   */
  @NotNull 
  @Schema(name = "text", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("text")
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public PlayerOrderReview flags(List<@Valid ReviewFlag> flags) {
    this.flags = flags;
    return this;
  }

  public PlayerOrderReview addFlagsItem(ReviewFlag flagsItem) {
    if (this.flags == null) {
      this.flags = new ArrayList<>();
    }
    this.flags.add(flagsItem);
    return this;
  }

  /**
   * Get flags
   * @return flags
   */
  @Valid 
  @Schema(name = "flags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("flags")
  public List<@Valid ReviewFlag> getFlags() {
    return flags;
  }

  public void setFlags(List<@Valid ReviewFlag> flags) {
    this.flags = flags;
  }

  public PlayerOrderReview moderation(ReviewModeration moderation) {
    this.moderation = moderation;
    return this;
  }

  /**
   * Get moderation
   * @return moderation
   */
  @NotNull @Valid 
  @Schema(name = "moderation", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("moderation")
  public ReviewModeration getModeration() {
    return moderation;
  }

  public void setModeration(ReviewModeration moderation) {
    this.moderation = moderation;
  }

  public PlayerOrderReview sentimentScore(@Nullable Float sentimentScore) {
    this.sentimentScore = sentimentScore;
    return this;
  }

  /**
   * Get sentimentScore
   * minimum: -1
   * maximum: 1
   * @return sentimentScore
   */
  @DecimalMin(value = "-1") @DecimalMax(value = "1") 
  @Schema(name = "sentimentScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sentimentScore")
  public @Nullable Float getSentimentScore() {
    return sentimentScore;
  }

  public void setSentimentScore(@Nullable Float sentimentScore) {
    this.sentimentScore = sentimentScore;
  }

  public PlayerOrderReview locale(@Nullable String locale) {
    this.locale = locale;
    return this;
  }

  /**
   * Get locale
   * @return locale
   */
  
  @Schema(name = "locale", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locale")
  public @Nullable String getLocale() {
    return locale;
  }

  public void setLocale(@Nullable String locale) {
    this.locale = locale;
  }

  public PlayerOrderReview attachments(List<URI> attachments) {
    this.attachments = attachments;
    return this;
  }

  public PlayerOrderReview addAttachmentsItem(URI attachmentsItem) {
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

  public PlayerOrderReview createdAt(OffsetDateTime createdAt) {
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

  public PlayerOrderReview updatedAt(@Nullable OffsetDateTime updatedAt) {
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

  public PlayerOrderReview status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderReview playerOrderReview = (PlayerOrderReview) o;
    return Objects.equals(this.reviewId, playerOrderReview.reviewId) &&
        Objects.equals(this.orderId, playerOrderReview.orderId) &&
        Objects.equals(this.reviewerId, playerOrderReview.reviewerId) &&
        Objects.equals(this.targetId, playerOrderReview.targetId) &&
        Objects.equals(this.role, playerOrderReview.role) &&
        Objects.equals(this.ratings, playerOrderReview.ratings) &&
        Objects.equals(this.text, playerOrderReview.text) &&
        Objects.equals(this.flags, playerOrderReview.flags) &&
        Objects.equals(this.moderation, playerOrderReview.moderation) &&
        Objects.equals(this.sentimentScore, playerOrderReview.sentimentScore) &&
        Objects.equals(this.locale, playerOrderReview.locale) &&
        Objects.equals(this.attachments, playerOrderReview.attachments) &&
        Objects.equals(this.createdAt, playerOrderReview.createdAt) &&
        Objects.equals(this.updatedAt, playerOrderReview.updatedAt) &&
        Objects.equals(this.status, playerOrderReview.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reviewId, orderId, reviewerId, targetId, role, ratings, text, flags, moderation, sentimentScore, locale, attachments, createdAt, updatedAt, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderReview {\n");
    sb.append("    reviewId: ").append(toIndentedString(reviewId)).append("\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    reviewerId: ").append(toIndentedString(reviewerId)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    ratings: ").append(toIndentedString(ratings)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    flags: ").append(toIndentedString(flags)).append("\n");
    sb.append("    moderation: ").append(toIndentedString(moderation)).append("\n");
    sb.append("    sentimentScore: ").append(toIndentedString(sentimentScore)).append("\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

