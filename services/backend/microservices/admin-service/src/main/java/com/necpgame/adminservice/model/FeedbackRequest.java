package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * FeedbackRequest
 */


public class FeedbackRequest {

  private Integer rating;

  private @Nullable String comment;

  public FeedbackRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FeedbackRequest(Integer rating) {
    this.rating = rating;
  }

  public FeedbackRequest rating(Integer rating) {
    this.rating = rating;
    return this;
  }

  /**
   * Get rating
   * minimum: 1
   * maximum: 5
   * @return rating
   */
  @NotNull @Min(value = 1) @Max(value = 5) 
  @Schema(name = "rating", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rating")
  public Integer getRating() {
    return rating;
  }

  public void setRating(Integer rating) {
    this.rating = rating;
  }

  public FeedbackRequest comment(@Nullable String comment) {
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
    FeedbackRequest feedbackRequest = (FeedbackRequest) o;
    return Objects.equals(this.rating, feedbackRequest.rating) &&
        Objects.equals(this.comment, feedbackRequest.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rating, comment);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FeedbackRequest {\n");
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

