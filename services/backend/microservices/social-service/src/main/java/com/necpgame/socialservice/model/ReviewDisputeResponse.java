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
 * ReviewDisputeResponse
 */


public class ReviewDisputeResponse {

  private UUID reviewId;

  private UUID disputeId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    FILED("filed"),
    
    INVESTIGATING("investigating"),
    
    RESOLVED("resolved"),
    
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

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  public ReviewDisputeResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReviewDisputeResponse(UUID reviewId, UUID disputeId, StatusEnum status) {
    this.reviewId = reviewId;
    this.disputeId = disputeId;
    this.status = status;
  }

  public ReviewDisputeResponse reviewId(UUID reviewId) {
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

  public ReviewDisputeResponse disputeId(UUID disputeId) {
    this.disputeId = disputeId;
    return this;
  }

  /**
   * Get disputeId
   * @return disputeId
   */
  @NotNull @Valid 
  @Schema(name = "disputeId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("disputeId")
  public UUID getDisputeId() {
    return disputeId;
  }

  public void setDisputeId(UUID disputeId) {
    this.disputeId = disputeId;
  }

  public ReviewDisputeResponse status(StatusEnum status) {
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

  public ReviewDisputeResponse createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
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
    ReviewDisputeResponse reviewDisputeResponse = (ReviewDisputeResponse) o;
    return Objects.equals(this.reviewId, reviewDisputeResponse.reviewId) &&
        Objects.equals(this.disputeId, reviewDisputeResponse.disputeId) &&
        Objects.equals(this.status, reviewDisputeResponse.status) &&
        Objects.equals(this.createdAt, reviewDisputeResponse.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reviewId, disputeId, status, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReviewDisputeResponse {\n");
    sb.append("    reviewId: ").append(toIndentedString(reviewId)).append("\n");
    sb.append("    disputeId: ").append(toIndentedString(disputeId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

