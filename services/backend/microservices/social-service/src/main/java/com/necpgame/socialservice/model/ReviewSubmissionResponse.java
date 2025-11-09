package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ReviewModeration;
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
 * ReviewSubmissionResponse
 */


public class ReviewSubmissionResponse {

  private UUID reviewId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACCEPTED("accepted"),
    
    PENDING_MODERATION("pending_moderation"),
    
    REJECTED("rejected");

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

  private @Nullable ReviewModeration moderation;

  public ReviewSubmissionResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReviewSubmissionResponse(UUID reviewId, StatusEnum status) {
    this.reviewId = reviewId;
    this.status = status;
  }

  public ReviewSubmissionResponse reviewId(UUID reviewId) {
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

  public ReviewSubmissionResponse status(StatusEnum status) {
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

  public ReviewSubmissionResponse moderation(@Nullable ReviewModeration moderation) {
    this.moderation = moderation;
    return this;
  }

  /**
   * Get moderation
   * @return moderation
   */
  @Valid 
  @Schema(name = "moderation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("moderation")
  public @Nullable ReviewModeration getModeration() {
    return moderation;
  }

  public void setModeration(@Nullable ReviewModeration moderation) {
    this.moderation = moderation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReviewSubmissionResponse reviewSubmissionResponse = (ReviewSubmissionResponse) o;
    return Objects.equals(this.reviewId, reviewSubmissionResponse.reviewId) &&
        Objects.equals(this.status, reviewSubmissionResponse.status) &&
        Objects.equals(this.moderation, reviewSubmissionResponse.moderation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reviewId, status, moderation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReviewSubmissionResponse {\n");
    sb.append("    reviewId: ").append(toIndentedString(reviewId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    moderation: ").append(toIndentedString(moderation)).append("\n");
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

