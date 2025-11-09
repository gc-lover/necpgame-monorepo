package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.ActiveQuestProgressChoicesMadeInner;
import com.necpgame.backjava.model.ActiveQuestProgressObjectivesInner;
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
 * ActiveQuestProgress
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ActiveQuestProgress {

  private @Nullable String questId;

  private @Nullable String currentBranch;

  @Valid
  private List<@Valid ActiveQuestProgressChoicesMadeInner> choicesMade = new ArrayList<>();

  @Valid
  private List<@Valid ActiveQuestProgressObjectivesInner> objectives = new ArrayList<>();

  public ActiveQuestProgress questId(@Nullable String questId) {
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

  public ActiveQuestProgress currentBranch(@Nullable String currentBranch) {
    this.currentBranch = currentBranch;
    return this;
  }

  /**
   * Get currentBranch
   * @return currentBranch
   */
  
  @Schema(name = "current_branch", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_branch")
  public @Nullable String getCurrentBranch() {
    return currentBranch;
  }

  public void setCurrentBranch(@Nullable String currentBranch) {
    this.currentBranch = currentBranch;
  }

  public ActiveQuestProgress choicesMade(List<@Valid ActiveQuestProgressChoicesMadeInner> choicesMade) {
    this.choicesMade = choicesMade;
    return this;
  }

  public ActiveQuestProgress addChoicesMadeItem(ActiveQuestProgressChoicesMadeInner choicesMadeItem) {
    if (this.choicesMade == null) {
      this.choicesMade = new ArrayList<>();
    }
    this.choicesMade.add(choicesMadeItem);
    return this;
  }

  /**
   * Get choicesMade
   * @return choicesMade
   */
  @Valid 
  @Schema(name = "choices_made", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("choices_made")
  public List<@Valid ActiveQuestProgressChoicesMadeInner> getChoicesMade() {
    return choicesMade;
  }

  public void setChoicesMade(List<@Valid ActiveQuestProgressChoicesMadeInner> choicesMade) {
    this.choicesMade = choicesMade;
  }

  public ActiveQuestProgress objectives(List<@Valid ActiveQuestProgressObjectivesInner> objectives) {
    this.objectives = objectives;
    return this;
  }

  public ActiveQuestProgress addObjectivesItem(ActiveQuestProgressObjectivesInner objectivesItem) {
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
  public List<@Valid ActiveQuestProgressObjectivesInner> getObjectives() {
    return objectives;
  }

  public void setObjectives(List<@Valid ActiveQuestProgressObjectivesInner> objectives) {
    this.objectives = objectives;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActiveQuestProgress activeQuestProgress = (ActiveQuestProgress) o;
    return Objects.equals(this.questId, activeQuestProgress.questId) &&
        Objects.equals(this.currentBranch, activeQuestProgress.currentBranch) &&
        Objects.equals(this.choicesMade, activeQuestProgress.choicesMade) &&
        Objects.equals(this.objectives, activeQuestProgress.objectives);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, currentBranch, choicesMade, objectives);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActiveQuestProgress {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    currentBranch: ").append(toIndentedString(currentBranch)).append("\n");
    sb.append("    choicesMade: ").append(toIndentedString(choicesMade)).append("\n");
    sb.append("    objectives: ").append(toIndentedString(objectives)).append("\n");
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

