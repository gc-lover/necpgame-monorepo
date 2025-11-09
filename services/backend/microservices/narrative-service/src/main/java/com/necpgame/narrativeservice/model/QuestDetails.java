package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * QuestDetails
 */


public class QuestDetails {

  private @Nullable String questId;

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable String type;

  @Valid
  private List<Object> branches = new ArrayList<>();

  @Valid
  private List<Object> objectives = new ArrayList<>();

  private @Nullable Object rewards;

  private @Nullable Object consequences;

  public QuestDetails questId(@Nullable String questId) {
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

  public QuestDetails name(@Nullable String name) {
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

  public QuestDetails description(@Nullable String description) {
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

  public QuestDetails type(@Nullable String type) {
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

  public QuestDetails branches(List<Object> branches) {
    this.branches = branches;
    return this;
  }

  public QuestDetails addBranchesItem(Object branchesItem) {
    if (this.branches == null) {
      this.branches = new ArrayList<>();
    }
    this.branches.add(branchesItem);
    return this;
  }

  /**
   * Ветвления квеста
   * @return branches
   */
  
  @Schema(name = "branches", description = "Ветвления квеста", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("branches")
  public List<Object> getBranches() {
    return branches;
  }

  public void setBranches(List<Object> branches) {
    this.branches = branches;
  }

  public QuestDetails objectives(List<Object> objectives) {
    this.objectives = objectives;
    return this;
  }

  public QuestDetails addObjectivesItem(Object objectivesItem) {
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
  
  @Schema(name = "objectives", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("objectives")
  public List<Object> getObjectives() {
    return objectives;
  }

  public void setObjectives(List<Object> objectives) {
    this.objectives = objectives;
  }

  public QuestDetails rewards(@Nullable Object rewards) {
    this.rewards = rewards;
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public @Nullable Object getRewards() {
    return rewards;
  }

  public void setRewards(@Nullable Object rewards) {
    this.rewards = rewards;
  }

  public QuestDetails consequences(@Nullable Object consequences) {
    this.consequences = consequences;
    return this;
  }

  /**
   * Возможные последствия выборов
   * @return consequences
   */
  
  @Schema(name = "consequences", description = "Возможные последствия выборов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequences")
  public @Nullable Object getConsequences() {
    return consequences;
  }

  public void setConsequences(@Nullable Object consequences) {
    this.consequences = consequences;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestDetails questDetails = (QuestDetails) o;
    return Objects.equals(this.questId, questDetails.questId) &&
        Objects.equals(this.name, questDetails.name) &&
        Objects.equals(this.description, questDetails.description) &&
        Objects.equals(this.type, questDetails.type) &&
        Objects.equals(this.branches, questDetails.branches) &&
        Objects.equals(this.objectives, questDetails.objectives) &&
        Objects.equals(this.rewards, questDetails.rewards) &&
        Objects.equals(this.consequences, questDetails.consequences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, name, description, type, branches, objectives, rewards, consequences);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestDetails {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    branches: ").append(toIndentedString(branches)).append("\n");
    sb.append("    objectives: ").append(toIndentedString(objectives)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    consequences: ").append(toIndentedString(consequences)).append("\n");
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

