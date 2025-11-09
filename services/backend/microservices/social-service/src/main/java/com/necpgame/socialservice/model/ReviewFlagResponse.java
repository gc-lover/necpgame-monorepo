package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.ReviewFlag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * ReviewFlagResponse
 */


public class ReviewFlagResponse {

  private UUID reviewId;

  @Valid
  private List<@Valid ReviewFlag> flags = new ArrayList<>();

  public ReviewFlagResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReviewFlagResponse(UUID reviewId, List<@Valid ReviewFlag> flags) {
    this.reviewId = reviewId;
    this.flags = flags;
  }

  public ReviewFlagResponse reviewId(UUID reviewId) {
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

  public ReviewFlagResponse flags(List<@Valid ReviewFlag> flags) {
    this.flags = flags;
    return this;
  }

  public ReviewFlagResponse addFlagsItem(ReviewFlag flagsItem) {
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
  @NotNull @Valid 
  @Schema(name = "flags", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("flags")
  public List<@Valid ReviewFlag> getFlags() {
    return flags;
  }

  public void setFlags(List<@Valid ReviewFlag> flags) {
    this.flags = flags;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReviewFlagResponse reviewFlagResponse = (ReviewFlagResponse) o;
    return Objects.equals(this.reviewId, reviewFlagResponse.reviewId) &&
        Objects.equals(this.flags, reviewFlagResponse.flags);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reviewId, flags);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReviewFlagResponse {\n");
    sb.append("    reviewId: ").append(toIndentedString(reviewId)).append("\n");
    sb.append("    flags: ").append(toIndentedString(flags)).append("\n");
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

