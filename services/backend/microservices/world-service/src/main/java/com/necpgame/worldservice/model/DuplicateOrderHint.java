package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * DuplicateOrderHint
 */


public class DuplicateOrderHint {

  private UUID similarOrderId;

  private Float similarityScore;

  private @Nullable String reason;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  public DuplicateOrderHint() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DuplicateOrderHint(UUID similarOrderId, Float similarityScore) {
    this.similarOrderId = similarOrderId;
    this.similarityScore = similarityScore;
  }

  public DuplicateOrderHint similarOrderId(UUID similarOrderId) {
    this.similarOrderId = similarOrderId;
    return this;
  }

  /**
   * Get similarOrderId
   * @return similarOrderId
   */
  @NotNull @Valid 
  @Schema(name = "similarOrderId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("similarOrderId")
  public UUID getSimilarOrderId() {
    return similarOrderId;
  }

  public void setSimilarOrderId(UUID similarOrderId) {
    this.similarOrderId = similarOrderId;
  }

  public DuplicateOrderHint similarityScore(Float similarityScore) {
    this.similarityScore = similarityScore;
    return this;
  }

  /**
   * Get similarityScore
   * minimum: 0
   * maximum: 1
   * @return similarityScore
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "similarityScore", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("similarityScore")
  public Float getSimilarityScore() {
    return similarityScore;
  }

  public void setSimilarityScore(Float similarityScore) {
    this.similarityScore = similarityScore;
  }

  public DuplicateOrderHint reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  public DuplicateOrderHint createdAt(@Nullable OffsetDateTime createdAt) {
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
    DuplicateOrderHint duplicateOrderHint = (DuplicateOrderHint) o;
    return Objects.equals(this.similarOrderId, duplicateOrderHint.similarOrderId) &&
        Objects.equals(this.similarityScore, duplicateOrderHint.similarityScore) &&
        Objects.equals(this.reason, duplicateOrderHint.reason) &&
        Objects.equals(this.createdAt, duplicateOrderHint.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(similarOrderId, similarityScore, reason, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DuplicateOrderHint {\n");
    sb.append("    similarOrderId: ").append(toIndentedString(similarOrderId)).append("\n");
    sb.append("    similarityScore: ").append(toIndentedString(similarityScore)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

