package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
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
 * ReviewCreatedEvent
 */


public class ReviewCreatedEvent {

  private UUID eventId;

  private UUID reviewId;

  private UUID orderId;

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

  private @Nullable Float sentimentScore;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  public ReviewCreatedEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReviewCreatedEvent(UUID eventId, UUID reviewId, UUID orderId, UUID targetId, RoleEnum role, OffsetDateTime createdAt) {
    this.eventId = eventId;
    this.reviewId = reviewId;
    this.orderId = orderId;
    this.targetId = targetId;
    this.role = role;
    this.createdAt = createdAt;
  }

  public ReviewCreatedEvent eventId(UUID eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  @NotNull @Valid 
  @Schema(name = "eventId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eventId")
  public UUID getEventId() {
    return eventId;
  }

  public void setEventId(UUID eventId) {
    this.eventId = eventId;
  }

  public ReviewCreatedEvent reviewId(UUID reviewId) {
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

  public ReviewCreatedEvent orderId(UUID orderId) {
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

  public ReviewCreatedEvent targetId(UUID targetId) {
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

  public ReviewCreatedEvent role(RoleEnum role) {
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

  public ReviewCreatedEvent sentimentScore(@Nullable Float sentimentScore) {
    this.sentimentScore = sentimentScore;
    return this;
  }

  /**
   * Get sentimentScore
   * @return sentimentScore
   */
  
  @Schema(name = "sentimentScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sentimentScore")
  public @Nullable Float getSentimentScore() {
    return sentimentScore;
  }

  public void setSentimentScore(@Nullable Float sentimentScore) {
    this.sentimentScore = sentimentScore;
  }

  public ReviewCreatedEvent createdAt(OffsetDateTime createdAt) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReviewCreatedEvent reviewCreatedEvent = (ReviewCreatedEvent) o;
    return Objects.equals(this.eventId, reviewCreatedEvent.eventId) &&
        Objects.equals(this.reviewId, reviewCreatedEvent.reviewId) &&
        Objects.equals(this.orderId, reviewCreatedEvent.orderId) &&
        Objects.equals(this.targetId, reviewCreatedEvent.targetId) &&
        Objects.equals(this.role, reviewCreatedEvent.role) &&
        Objects.equals(this.sentimentScore, reviewCreatedEvent.sentimentScore) &&
        Objects.equals(this.createdAt, reviewCreatedEvent.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, reviewId, orderId, targetId, role, sentimentScore, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReviewCreatedEvent {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    reviewId: ").append(toIndentedString(reviewId)).append("\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    sentimentScore: ").append(toIndentedString(sentimentScore)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

