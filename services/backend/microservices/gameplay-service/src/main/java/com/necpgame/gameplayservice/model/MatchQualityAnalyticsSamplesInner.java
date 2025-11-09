package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
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
 * MatchQualityAnalyticsSamplesInner
 */

@JsonTypeName("MatchQualityAnalytics_samples_inner")

public class MatchQualityAnalyticsSamplesInner {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private Float score;

  private @Nullable Integer waitSeconds;

  private @Nullable Integer ratingSpread;

  public MatchQualityAnalyticsSamplesInner() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MatchQualityAnalyticsSamplesInner(OffsetDateTime timestamp, Float score) {
    this.timestamp = timestamp;
    this.score = score;
  }

  public MatchQualityAnalyticsSamplesInner timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @NotNull @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public MatchQualityAnalyticsSamplesInner score(Float score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * @return score
   */
  @NotNull 
  @Schema(name = "score", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("score")
  public Float getScore() {
    return score;
  }

  public void setScore(Float score) {
    this.score = score;
  }

  public MatchQualityAnalyticsSamplesInner waitSeconds(@Nullable Integer waitSeconds) {
    this.waitSeconds = waitSeconds;
    return this;
  }

  /**
   * Get waitSeconds
   * @return waitSeconds
   */
  
  @Schema(name = "waitSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("waitSeconds")
  public @Nullable Integer getWaitSeconds() {
    return waitSeconds;
  }

  public void setWaitSeconds(@Nullable Integer waitSeconds) {
    this.waitSeconds = waitSeconds;
  }

  public MatchQualityAnalyticsSamplesInner ratingSpread(@Nullable Integer ratingSpread) {
    this.ratingSpread = ratingSpread;
    return this;
  }

  /**
   * Get ratingSpread
   * @return ratingSpread
   */
  
  @Schema(name = "ratingSpread", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ratingSpread")
  public @Nullable Integer getRatingSpread() {
    return ratingSpread;
  }

  public void setRatingSpread(@Nullable Integer ratingSpread) {
    this.ratingSpread = ratingSpread;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchQualityAnalyticsSamplesInner matchQualityAnalyticsSamplesInner = (MatchQualityAnalyticsSamplesInner) o;
    return Objects.equals(this.timestamp, matchQualityAnalyticsSamplesInner.timestamp) &&
        Objects.equals(this.score, matchQualityAnalyticsSamplesInner.score) &&
        Objects.equals(this.waitSeconds, matchQualityAnalyticsSamplesInner.waitSeconds) &&
        Objects.equals(this.ratingSpread, matchQualityAnalyticsSamplesInner.ratingSpread);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, score, waitSeconds, ratingSpread);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchQualityAnalyticsSamplesInner {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    waitSeconds: ").append(toIndentedString(waitSeconds)).append("\n");
    sb.append("    ratingSpread: ").append(toIndentedString(ratingSpread)).append("\n");
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

