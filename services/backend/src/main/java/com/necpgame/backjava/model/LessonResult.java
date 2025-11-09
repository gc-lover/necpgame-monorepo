package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.Lesson;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LessonResult
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LessonResult {

  private @Nullable String lessonId;

  private @Nullable Integer performanceScore;

  private @Nullable Boolean passed;

  private @Nullable Object rewardsEarned;

  private @Nullable Lesson nextLesson;

  private @Nullable Integer bondChange;

  public LessonResult lessonId(@Nullable String lessonId) {
    this.lessonId = lessonId;
    return this;
  }

  /**
   * Get lessonId
   * @return lessonId
   */
  
  @Schema(name = "lesson_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lesson_id")
  public @Nullable String getLessonId() {
    return lessonId;
  }

  public void setLessonId(@Nullable String lessonId) {
    this.lessonId = lessonId;
  }

  public LessonResult performanceScore(@Nullable Integer performanceScore) {
    this.performanceScore = performanceScore;
    return this;
  }

  /**
   * Get performanceScore
   * @return performanceScore
   */
  
  @Schema(name = "performance_score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("performance_score")
  public @Nullable Integer getPerformanceScore() {
    return performanceScore;
  }

  public void setPerformanceScore(@Nullable Integer performanceScore) {
    this.performanceScore = performanceScore;
  }

  public LessonResult passed(@Nullable Boolean passed) {
    this.passed = passed;
    return this;
  }

  /**
   * Get passed
   * @return passed
   */
  
  @Schema(name = "passed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("passed")
  public @Nullable Boolean getPassed() {
    return passed;
  }

  public void setPassed(@Nullable Boolean passed) {
    this.passed = passed;
  }

  public LessonResult rewardsEarned(@Nullable Object rewardsEarned) {
    this.rewardsEarned = rewardsEarned;
    return this;
  }

  /**
   * Get rewardsEarned
   * @return rewardsEarned
   */
  
  @Schema(name = "rewards_earned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards_earned")
  public @Nullable Object getRewardsEarned() {
    return rewardsEarned;
  }

  public void setRewardsEarned(@Nullable Object rewardsEarned) {
    this.rewardsEarned = rewardsEarned;
  }

  public LessonResult nextLesson(@Nullable Lesson nextLesson) {
    this.nextLesson = nextLesson;
    return this;
  }

  /**
   * Get nextLesson
   * @return nextLesson
   */
  @Valid 
  @Schema(name = "next_lesson", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("next_lesson")
  public @Nullable Lesson getNextLesson() {
    return nextLesson;
  }

  public void setNextLesson(@Nullable Lesson nextLesson) {
    this.nextLesson = nextLesson;
  }

  public LessonResult bondChange(@Nullable Integer bondChange) {
    this.bondChange = bondChange;
    return this;
  }

  /**
   * Get bondChange
   * @return bondChange
   */
  
  @Schema(name = "bond_change", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bond_change")
  public @Nullable Integer getBondChange() {
    return bondChange;
  }

  public void setBondChange(@Nullable Integer bondChange) {
    this.bondChange = bondChange;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LessonResult lessonResult = (LessonResult) o;
    return Objects.equals(this.lessonId, lessonResult.lessonId) &&
        Objects.equals(this.performanceScore, lessonResult.performanceScore) &&
        Objects.equals(this.passed, lessonResult.passed) &&
        Objects.equals(this.rewardsEarned, lessonResult.rewardsEarned) &&
        Objects.equals(this.nextLesson, lessonResult.nextLesson) &&
        Objects.equals(this.bondChange, lessonResult.bondChange);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lessonId, performanceScore, passed, rewardsEarned, nextLesson, bondChange);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LessonResult {\n");
    sb.append("    lessonId: ").append(toIndentedString(lessonId)).append("\n");
    sb.append("    performanceScore: ").append(toIndentedString(performanceScore)).append("\n");
    sb.append("    passed: ").append(toIndentedString(passed)).append("\n");
    sb.append("    rewardsEarned: ").append(toIndentedString(rewardsEarned)).append("\n");
    sb.append("    nextLesson: ").append(toIndentedString(nextLesson)).append("\n");
    sb.append("    bondChange: ").append(toIndentedString(bondChange)).append("\n");
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

