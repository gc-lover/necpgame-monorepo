package com.necpgame.adminservice.model;

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
 * AnalyticsResponseFeedbackStats
 */

@JsonTypeName("AnalyticsResponse_feedbackStats")

public class AnalyticsResponseFeedbackStats {

  private @Nullable Integer useful;

  private @Nullable Integer neutral;

  private @Nullable Integer notUseful;

  public AnalyticsResponseFeedbackStats useful(@Nullable Integer useful) {
    this.useful = useful;
    return this;
  }

  /**
   * Get useful
   * @return useful
   */
  
  @Schema(name = "useful", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("useful")
  public @Nullable Integer getUseful() {
    return useful;
  }

  public void setUseful(@Nullable Integer useful) {
    this.useful = useful;
  }

  public AnalyticsResponseFeedbackStats neutral(@Nullable Integer neutral) {
    this.neutral = neutral;
    return this;
  }

  /**
   * Get neutral
   * @return neutral
   */
  
  @Schema(name = "neutral", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("neutral")
  public @Nullable Integer getNeutral() {
    return neutral;
  }

  public void setNeutral(@Nullable Integer neutral) {
    this.neutral = neutral;
  }

  public AnalyticsResponseFeedbackStats notUseful(@Nullable Integer notUseful) {
    this.notUseful = notUseful;
    return this;
  }

  /**
   * Get notUseful
   * @return notUseful
   */
  
  @Schema(name = "notUseful", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notUseful")
  public @Nullable Integer getNotUseful() {
    return notUseful;
  }

  public void setNotUseful(@Nullable Integer notUseful) {
    this.notUseful = notUseful;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsResponseFeedbackStats analyticsResponseFeedbackStats = (AnalyticsResponseFeedbackStats) o;
    return Objects.equals(this.useful, analyticsResponseFeedbackStats.useful) &&
        Objects.equals(this.neutral, analyticsResponseFeedbackStats.neutral) &&
        Objects.equals(this.notUseful, analyticsResponseFeedbackStats.notUseful);
  }

  @Override
  public int hashCode() {
    return Objects.hash(useful, neutral, notUseful);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsResponseFeedbackStats {\n");
    sb.append("    useful: ").append(toIndentedString(useful)).append("\n");
    sb.append("    neutral: ").append(toIndentedString(neutral)).append("\n");
    sb.append("    notUseful: ").append(toIndentedString(notUseful)).append("\n");
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

