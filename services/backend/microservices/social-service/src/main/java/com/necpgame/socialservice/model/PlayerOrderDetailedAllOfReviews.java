package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlayerOrderDetailedAllOfReviews
 */

@JsonTypeName("PlayerOrderDetailed_allOf_reviews")

public class PlayerOrderDetailedAllOfReviews {

  private @Nullable String reviewerId;

  private @Nullable Integer rating;

  private @Nullable String comment;

  public PlayerOrderDetailedAllOfReviews reviewerId(@Nullable String reviewerId) {
    this.reviewerId = reviewerId;
    return this;
  }

  /**
   * Get reviewerId
   * @return reviewerId
   */
  
  @Schema(name = "reviewer_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reviewer_id")
  public @Nullable String getReviewerId() {
    return reviewerId;
  }

  public void setReviewerId(@Nullable String reviewerId) {
    this.reviewerId = reviewerId;
  }

  public PlayerOrderDetailedAllOfReviews rating(@Nullable Integer rating) {
    this.rating = rating;
    return this;
  }

  /**
   * Get rating
   * @return rating
   */
  
  @Schema(name = "rating", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rating")
  public @Nullable Integer getRating() {
    return rating;
  }

  public void setRating(@Nullable Integer rating) {
    this.rating = rating;
  }

  public PlayerOrderDetailedAllOfReviews comment(@Nullable String comment) {
    this.comment = comment;
    return this;
  }

  /**
   * Get comment
   * @return comment
   */
  
  @Schema(name = "comment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("comment")
  public @Nullable String getComment() {
    return comment;
  }

  public void setComment(@Nullable String comment) {
    this.comment = comment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderDetailedAllOfReviews playerOrderDetailedAllOfReviews = (PlayerOrderDetailedAllOfReviews) o;
    return Objects.equals(this.reviewerId, playerOrderDetailedAllOfReviews.reviewerId) &&
        Objects.equals(this.rating, playerOrderDetailedAllOfReviews.rating) &&
        Objects.equals(this.comment, playerOrderDetailedAllOfReviews.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reviewerId, rating, comment);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderDetailedAllOfReviews {\n");
    sb.append("    reviewerId: ").append(toIndentedString(reviewerId)).append("\n");
    sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
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

