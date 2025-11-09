package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.MainStoryQuestMajorChoicesInner;
import java.math.BigDecimal;
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
 * MainStoryQuest
 */


public class MainStoryQuest {

  private @Nullable String questId;

  private @Nullable Integer chapter;

  private @Nullable String title;

  private @Nullable String period;

  private @Nullable String description;

  private @Nullable Integer requiredLevel;

  @Valid
  private List<String> prerequisites = new ArrayList<>();

  @Valid
  private List<@Valid MainStoryQuestMajorChoicesInner> majorChoices = new ArrayList<>();

  private @Nullable Integer branches;

  private @Nullable BigDecimal estimatedTimeHours;

  public MainStoryQuest questId(@Nullable String questId) {
    this.questId = questId;
    return this;
  }

  /**
   * Get questId
   * @return questId
   */
  
  @Schema(name = "quest_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_id")
  public @Nullable String getQuestId() {
    return questId;
  }

  public void setQuestId(@Nullable String questId) {
    this.questId = questId;
  }

  public MainStoryQuest chapter(@Nullable Integer chapter) {
    this.chapter = chapter;
    return this;
  }

  /**
   * Get chapter
   * @return chapter
   */
  
  @Schema(name = "chapter", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("chapter")
  public @Nullable Integer getChapter() {
    return chapter;
  }

  public void setChapter(@Nullable Integer chapter) {
    this.chapter = chapter;
  }

  public MainStoryQuest title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public MainStoryQuest period(@Nullable String period) {
    this.period = period;
    return this;
  }

  /**
   * Get period
   * @return period
   */
  
  @Schema(name = "period", example = "2055", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("period")
  public @Nullable String getPeriod() {
    return period;
  }

  public void setPeriod(@Nullable String period) {
    this.period = period;
  }

  public MainStoryQuest description(@Nullable String description) {
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

  public MainStoryQuest requiredLevel(@Nullable Integer requiredLevel) {
    this.requiredLevel = requiredLevel;
    return this;
  }

  /**
   * Get requiredLevel
   * @return requiredLevel
   */
  
  @Schema(name = "required_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_level")
  public @Nullable Integer getRequiredLevel() {
    return requiredLevel;
  }

  public void setRequiredLevel(@Nullable Integer requiredLevel) {
    this.requiredLevel = requiredLevel;
  }

  public MainStoryQuest prerequisites(List<String> prerequisites) {
    this.prerequisites = prerequisites;
    return this;
  }

  public MainStoryQuest addPrerequisitesItem(String prerequisitesItem) {
    if (this.prerequisites == null) {
      this.prerequisites = new ArrayList<>();
    }
    this.prerequisites.add(prerequisitesItem);
    return this;
  }

  /**
   * Предыдущие квесты основного сюжета
   * @return prerequisites
   */
  
  @Schema(name = "prerequisites", description = "Предыдущие квесты основного сюжета", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("prerequisites")
  public List<String> getPrerequisites() {
    return prerequisites;
  }

  public void setPrerequisites(List<String> prerequisites) {
    this.prerequisites = prerequisites;
  }

  public MainStoryQuest majorChoices(List<@Valid MainStoryQuestMajorChoicesInner> majorChoices) {
    this.majorChoices = majorChoices;
    return this;
  }

  public MainStoryQuest addMajorChoicesItem(MainStoryQuestMajorChoicesInner majorChoicesItem) {
    if (this.majorChoices == null) {
      this.majorChoices = new ArrayList<>();
    }
    this.majorChoices.add(majorChoicesItem);
    return this;
  }

  /**
   * Важные выборы в квесте
   * @return majorChoices
   */
  @Valid 
  @Schema(name = "major_choices", description = "Важные выборы в квесте", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("major_choices")
  public List<@Valid MainStoryQuestMajorChoicesInner> getMajorChoices() {
    return majorChoices;
  }

  public void setMajorChoices(List<@Valid MainStoryQuestMajorChoicesInner> majorChoices) {
    this.majorChoices = majorChoices;
  }

  public MainStoryQuest branches(@Nullable Integer branches) {
    this.branches = branches;
    return this;
  }

  /**
   * Количество веток
   * @return branches
   */
  
  @Schema(name = "branches", description = "Количество веток", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("branches")
  public @Nullable Integer getBranches() {
    return branches;
  }

  public void setBranches(@Nullable Integer branches) {
    this.branches = branches;
  }

  public MainStoryQuest estimatedTimeHours(@Nullable BigDecimal estimatedTimeHours) {
    this.estimatedTimeHours = estimatedTimeHours;
    return this;
  }

  /**
   * Get estimatedTimeHours
   * @return estimatedTimeHours
   */
  @Valid 
  @Schema(name = "estimated_time_hours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_time_hours")
  public @Nullable BigDecimal getEstimatedTimeHours() {
    return estimatedTimeHours;
  }

  public void setEstimatedTimeHours(@Nullable BigDecimal estimatedTimeHours) {
    this.estimatedTimeHours = estimatedTimeHours;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MainStoryQuest mainStoryQuest = (MainStoryQuest) o;
    return Objects.equals(this.questId, mainStoryQuest.questId) &&
        Objects.equals(this.chapter, mainStoryQuest.chapter) &&
        Objects.equals(this.title, mainStoryQuest.title) &&
        Objects.equals(this.period, mainStoryQuest.period) &&
        Objects.equals(this.description, mainStoryQuest.description) &&
        Objects.equals(this.requiredLevel, mainStoryQuest.requiredLevel) &&
        Objects.equals(this.prerequisites, mainStoryQuest.prerequisites) &&
        Objects.equals(this.majorChoices, mainStoryQuest.majorChoices) &&
        Objects.equals(this.branches, mainStoryQuest.branches) &&
        Objects.equals(this.estimatedTimeHours, mainStoryQuest.estimatedTimeHours);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, chapter, title, period, description, requiredLevel, prerequisites, majorChoices, branches, estimatedTimeHours);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MainStoryQuest {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    chapter: ").append(toIndentedString(chapter)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    requiredLevel: ").append(toIndentedString(requiredLevel)).append("\n");
    sb.append("    prerequisites: ").append(toIndentedString(prerequisites)).append("\n");
    sb.append("    majorChoices: ").append(toIndentedString(majorChoices)).append("\n");
    sb.append("    branches: ").append(toIndentedString(branches)).append("\n");
    sb.append("    estimatedTimeHours: ").append(toIndentedString(estimatedTimeHours)).append("\n");
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

