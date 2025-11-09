package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ReviewModerationAction;
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
 * ReviewModeration
 */


public class ReviewModeration {

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("pending"),
    
    APPROVED("approved"),
    
    REJECTED("rejected"),
    
    NEEDS_REVISION("needs_revision");

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

  private @Nullable UUID reviewerId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime reviewedAt;

  private @Nullable Float toxicityScore;

  private @Nullable String rejectionReason;

  @Valid
  private List<@Valid ReviewModerationAction> actions = new ArrayList<>();

  public ReviewModeration() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReviewModeration(StatusEnum status) {
    this.status = status;
  }

  public ReviewModeration status(StatusEnum status) {
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

  public ReviewModeration reviewerId(@Nullable UUID reviewerId) {
    this.reviewerId = reviewerId;
    return this;
  }

  /**
   * Get reviewerId
   * @return reviewerId
   */
  @Valid 
  @Schema(name = "reviewerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reviewerId")
  public @Nullable UUID getReviewerId() {
    return reviewerId;
  }

  public void setReviewerId(@Nullable UUID reviewerId) {
    this.reviewerId = reviewerId;
  }

  public ReviewModeration reviewedAt(@Nullable OffsetDateTime reviewedAt) {
    this.reviewedAt = reviewedAt;
    return this;
  }

  /**
   * Get reviewedAt
   * @return reviewedAt
   */
  @Valid 
  @Schema(name = "reviewedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reviewedAt")
  public @Nullable OffsetDateTime getReviewedAt() {
    return reviewedAt;
  }

  public void setReviewedAt(@Nullable OffsetDateTime reviewedAt) {
    this.reviewedAt = reviewedAt;
  }

  public ReviewModeration toxicityScore(@Nullable Float toxicityScore) {
    this.toxicityScore = toxicityScore;
    return this;
  }

  /**
   * Get toxicityScore
   * minimum: 0
   * maximum: 1
   * @return toxicityScore
   */
  @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "toxicityScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("toxicityScore")
  public @Nullable Float getToxicityScore() {
    return toxicityScore;
  }

  public void setToxicityScore(@Nullable Float toxicityScore) {
    this.toxicityScore = toxicityScore;
  }

  public ReviewModeration rejectionReason(@Nullable String rejectionReason) {
    this.rejectionReason = rejectionReason;
    return this;
  }

  /**
   * Get rejectionReason
   * @return rejectionReason
   */
  @Size(max = 1024) 
  @Schema(name = "rejectionReason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rejectionReason")
  public @Nullable String getRejectionReason() {
    return rejectionReason;
  }

  public void setRejectionReason(@Nullable String rejectionReason) {
    this.rejectionReason = rejectionReason;
  }

  public ReviewModeration actions(List<@Valid ReviewModerationAction> actions) {
    this.actions = actions;
    return this;
  }

  public ReviewModeration addActionsItem(ReviewModerationAction actionsItem) {
    if (this.actions == null) {
      this.actions = new ArrayList<>();
    }
    this.actions.add(actionsItem);
    return this;
  }

  /**
   * Get actions
   * @return actions
   */
  @Valid 
  @Schema(name = "actions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actions")
  public List<@Valid ReviewModerationAction> getActions() {
    return actions;
  }

  public void setActions(List<@Valid ReviewModerationAction> actions) {
    this.actions = actions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReviewModeration reviewModeration = (ReviewModeration) o;
    return Objects.equals(this.status, reviewModeration.status) &&
        Objects.equals(this.reviewerId, reviewModeration.reviewerId) &&
        Objects.equals(this.reviewedAt, reviewModeration.reviewedAt) &&
        Objects.equals(this.toxicityScore, reviewModeration.toxicityScore) &&
        Objects.equals(this.rejectionReason, reviewModeration.rejectionReason) &&
        Objects.equals(this.actions, reviewModeration.actions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, reviewerId, reviewedAt, toxicityScore, rejectionReason, actions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReviewModeration {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    reviewerId: ").append(toIndentedString(reviewerId)).append("\n");
    sb.append("    reviewedAt: ").append(toIndentedString(reviewedAt)).append("\n");
    sb.append("    toxicityScore: ").append(toIndentedString(toxicityScore)).append("\n");
    sb.append("    rejectionReason: ").append(toIndentedString(rejectionReason)).append("\n");
    sb.append("    actions: ").append(toIndentedString(actions)).append("\n");
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

