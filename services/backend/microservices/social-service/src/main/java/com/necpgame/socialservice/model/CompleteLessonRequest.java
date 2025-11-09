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
 * CompleteLessonRequest
 */

@JsonTypeName("completeLesson_request")

public class CompleteLessonRequest {

  private @Nullable Integer performanceScore;

  public CompleteLessonRequest performanceScore(@Nullable Integer performanceScore) {
    this.performanceScore = performanceScore;
    return this;
  }

  /**
   * Get performanceScore
   * minimum: 0
   * maximum: 100
   * @return performanceScore
   */
  @Min(value = 0) @Max(value = 100) 
  @Schema(name = "performance_score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("performance_score")
  public @Nullable Integer getPerformanceScore() {
    return performanceScore;
  }

  public void setPerformanceScore(@Nullable Integer performanceScore) {
    this.performanceScore = performanceScore;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompleteLessonRequest completeLessonRequest = (CompleteLessonRequest) o;
    return Objects.equals(this.performanceScore, completeLessonRequest.performanceScore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(performanceScore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompleteLessonRequest {\n");
    sb.append("    performanceScore: ").append(toIndentedString(performanceScore)).append("\n");
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

