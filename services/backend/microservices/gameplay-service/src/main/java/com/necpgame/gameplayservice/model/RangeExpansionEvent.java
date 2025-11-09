package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * RangeExpansionEvent
 */


public class RangeExpansionEvent {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime expandedAt;

  private Integer ratingDelta;

  private @Nullable String reason;

  public RangeExpansionEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RangeExpansionEvent(OffsetDateTime expandedAt, Integer ratingDelta) {
    this.expandedAt = expandedAt;
    this.ratingDelta = ratingDelta;
  }

  public RangeExpansionEvent expandedAt(OffsetDateTime expandedAt) {
    this.expandedAt = expandedAt;
    return this;
  }

  /**
   * Get expandedAt
   * @return expandedAt
   */
  @NotNull @Valid 
  @Schema(name = "expandedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("expandedAt")
  public OffsetDateTime getExpandedAt() {
    return expandedAt;
  }

  public void setExpandedAt(OffsetDateTime expandedAt) {
    this.expandedAt = expandedAt;
  }

  public RangeExpansionEvent ratingDelta(Integer ratingDelta) {
    this.ratingDelta = ratingDelta;
    return this;
  }

  /**
   * Get ratingDelta
   * @return ratingDelta
   */
  @NotNull 
  @Schema(name = "ratingDelta", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ratingDelta")
  public Integer getRatingDelta() {
    return ratingDelta;
  }

  public void setRatingDelta(Integer ratingDelta) {
    this.ratingDelta = ratingDelta;
  }

  public RangeExpansionEvent reason(@Nullable String reason) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RangeExpansionEvent rangeExpansionEvent = (RangeExpansionEvent) o;
    return Objects.equals(this.expandedAt, rangeExpansionEvent.expandedAt) &&
        Objects.equals(this.ratingDelta, rangeExpansionEvent.ratingDelta) &&
        Objects.equals(this.reason, rangeExpansionEvent.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(expandedAt, ratingDelta, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RangeExpansionEvent {\n");
    sb.append("    expandedAt: ").append(toIndentedString(expandedAt)).append("\n");
    sb.append("    ratingDelta: ").append(toIndentedString(ratingDelta)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

