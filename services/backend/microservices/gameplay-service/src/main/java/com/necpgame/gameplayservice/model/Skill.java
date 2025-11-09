package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Skill
 */


public class Skill {

  private @Nullable String skillId;

  private @Nullable String name;

  private @Nullable String category;

  private @Nullable Integer rank;

  private @Nullable BigDecimal progress;

  private @Nullable BigDecimal progressToNext;

  private @Nullable BigDecimal classModifier;

  public Skill skillId(@Nullable String skillId) {
    this.skillId = skillId;
    return this;
  }

  /**
   * Get skillId
   * @return skillId
   */
  
  @Schema(name = "skill_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_id")
  public @Nullable String getSkillId() {
    return skillId;
  }

  public void setSkillId(@Nullable String skillId) {
    this.skillId = skillId;
  }

  public Skill name(@Nullable String name) {
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

  public Skill category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  public Skill rank(@Nullable Integer rank) {
    this.rank = rank;
    return this;
  }

  /**
   * Текущий ранг навыка (0-10)
   * @return rank
   */
  
  @Schema(name = "rank", description = "Текущий ранг навыка (0-10)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rank")
  public @Nullable Integer getRank() {
    return rank;
  }

  public void setRank(@Nullable Integer rank) {
    this.rank = rank;
  }

  public Skill progress(@Nullable BigDecimal progress) {
    this.progress = progress;
    return this;
  }

  /**
   * Прогресс к следующему рангу
   * @return progress
   */
  @Valid 
  @Schema(name = "progress", description = "Прогресс к следующему рангу", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progress")
  public @Nullable BigDecimal getProgress() {
    return progress;
  }

  public void setProgress(@Nullable BigDecimal progress) {
    this.progress = progress;
  }

  public Skill progressToNext(@Nullable BigDecimal progressToNext) {
    this.progressToNext = progressToNext;
    return this;
  }

  /**
   * Get progressToNext
   * @return progressToNext
   */
  @Valid 
  @Schema(name = "progress_to_next", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progress_to_next")
  public @Nullable BigDecimal getProgressToNext() {
    return progressToNext;
  }

  public void setProgressToNext(@Nullable BigDecimal progressToNext) {
    this.progressToNext = progressToNext;
  }

  public Skill classModifier(@Nullable BigDecimal classModifier) {
    this.classModifier = classModifier;
    return this;
  }

  /**
   * Модификатор от класса персонажа
   * @return classModifier
   */
  @Valid 
  @Schema(name = "class_modifier", description = "Модификатор от класса персонажа", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class_modifier")
  public @Nullable BigDecimal getClassModifier() {
    return classModifier;
  }

  public void setClassModifier(@Nullable BigDecimal classModifier) {
    this.classModifier = classModifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Skill skill = (Skill) o;
    return Objects.equals(this.skillId, skill.skillId) &&
        Objects.equals(this.name, skill.name) &&
        Objects.equals(this.category, skill.category) &&
        Objects.equals(this.rank, skill.rank) &&
        Objects.equals(this.progress, skill.progress) &&
        Objects.equals(this.progressToNext, skill.progressToNext) &&
        Objects.equals(this.classModifier, skill.classModifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(skillId, name, category, rank, progress, progressToNext, classModifier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Skill {\n");
    sb.append("    skillId: ").append(toIndentedString(skillId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    rank: ").append(toIndentedString(rank)).append("\n");
    sb.append("    progress: ").append(toIndentedString(progress)).append("\n");
    sb.append("    progressToNext: ").append(toIndentedString(progressToNext)).append("\n");
    sb.append("    classModifier: ").append(toIndentedString(classModifier)).append("\n");
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

