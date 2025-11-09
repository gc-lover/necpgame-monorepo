package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.LessonObjectivesInner;
import com.necpgame.backjava.model.LessonRewards;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Lesson
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class Lesson {

  private @Nullable String lessonId;

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable String type;

  @Valid
  private List<@Valid LessonObjectivesInner> objectives = new ArrayList<>();

  private @Nullable LessonRewards rewards;

  /**
   * Gets or Sets difficulty
   */
  public enum DifficultyEnum {
    BEGINNER("BEGINNER"),
    
    INTERMEDIATE("INTERMEDIATE"),
    
    ADVANCED("ADVANCED"),
    
    MASTER("MASTER");

    private final String value;

    DifficultyEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static DifficultyEnum fromValue(String value) {
      for (DifficultyEnum b : DifficultyEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable DifficultyEnum difficulty;

  public Lesson lessonId(@Nullable String lessonId) {
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

  public Lesson name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public Lesson description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public Lesson type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public Lesson objectives(List<@Valid LessonObjectivesInner> objectives) {
    this.objectives = objectives;
    return this;
  }

  public Lesson addObjectivesItem(LessonObjectivesInner objectivesItem) {
    if (this.objectives == null) {
      this.objectives = new ArrayList<>();
    }
    this.objectives.add(objectivesItem);
    return this;
  }

  /**
   * Get objectives
   * @return objectives
   */
  @Valid 
  @Schema(name = "objectives", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("objectives")
  public List<@Valid LessonObjectivesInner> getObjectives() {
    return objectives;
  }

  public void setObjectives(List<@Valid LessonObjectivesInner> objectives) {
    this.objectives = objectives;
  }

  public Lesson rewards(@Nullable LessonRewards rewards) {
    this.rewards = rewards;
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  @Valid 
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public @Nullable LessonRewards getRewards() {
    return rewards;
  }

  public void setRewards(@Nullable LessonRewards rewards) {
    this.rewards = rewards;
  }

  public Lesson difficulty(@Nullable DifficultyEnum difficulty) {
    this.difficulty = difficulty;
    return this;
  }

  /**
   * Get difficulty
   * @return difficulty
   */
  
  @Schema(name = "difficulty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("difficulty")
  public @Nullable DifficultyEnum getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(@Nullable DifficultyEnum difficulty) {
    this.difficulty = difficulty;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Lesson lesson = (Lesson) o;
    return Objects.equals(this.lessonId, lesson.lessonId) &&
        Objects.equals(this.name, lesson.name) &&
        Objects.equals(this.description, lesson.description) &&
        Objects.equals(this.type, lesson.type) &&
        Objects.equals(this.objectives, lesson.objectives) &&
        Objects.equals(this.rewards, lesson.rewards) &&
        Objects.equals(this.difficulty, lesson.difficulty);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lessonId, name, description, type, objectives, rewards, difficulty);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Lesson {\n");
    sb.append("    lessonId: ").append(toIndentedString(lessonId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    objectives: ").append(toIndentedString(objectives)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    difficulty: ").append(toIndentedString(difficulty)).append("\n");
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

