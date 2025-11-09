package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlayerOrderNewsItemMetrics
 */

@JsonTypeName("PlayerOrderNewsItem_metrics")

public class PlayerOrderNewsItemMetrics {

  private @Nullable BigDecimal engagementScore;

  private @Nullable Integer reads;

  private @Nullable Integer shares;

  public PlayerOrderNewsItemMetrics engagementScore(@Nullable BigDecimal engagementScore) {
    this.engagementScore = engagementScore;
    return this;
  }

  /**
   * Get engagementScore
   * minimum: 0
   * @return engagementScore
   */
  @Valid @DecimalMin(value = "0") 
  @Schema(name = "engagementScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("engagementScore")
  public @Nullable BigDecimal getEngagementScore() {
    return engagementScore;
  }

  public void setEngagementScore(@Nullable BigDecimal engagementScore) {
    this.engagementScore = engagementScore;
  }

  public PlayerOrderNewsItemMetrics reads(@Nullable Integer reads) {
    this.reads = reads;
    return this;
  }

  /**
   * Get reads
   * minimum: 0
   * @return reads
   */
  @Min(value = 0) 
  @Schema(name = "reads", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reads")
  public @Nullable Integer getReads() {
    return reads;
  }

  public void setReads(@Nullable Integer reads) {
    this.reads = reads;
  }

  public PlayerOrderNewsItemMetrics shares(@Nullable Integer shares) {
    this.shares = shares;
    return this;
  }

  /**
   * Get shares
   * minimum: 0
   * @return shares
   */
  @Min(value = 0) 
  @Schema(name = "shares", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("shares")
  public @Nullable Integer getShares() {
    return shares;
  }

  public void setShares(@Nullable Integer shares) {
    this.shares = shares;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderNewsItemMetrics playerOrderNewsItemMetrics = (PlayerOrderNewsItemMetrics) o;
    return Objects.equals(this.engagementScore, playerOrderNewsItemMetrics.engagementScore) &&
        Objects.equals(this.reads, playerOrderNewsItemMetrics.reads) &&
        Objects.equals(this.shares, playerOrderNewsItemMetrics.shares);
  }

  @Override
  public int hashCode() {
    return Objects.hash(engagementScore, reads, shares);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderNewsItemMetrics {\n");
    sb.append("    engagementScore: ").append(toIndentedString(engagementScore)).append("\n");
    sb.append("    reads: ").append(toIndentedString(reads)).append("\n");
    sb.append("    shares: ").append(toIndentedString(shares)).append("\n");
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

