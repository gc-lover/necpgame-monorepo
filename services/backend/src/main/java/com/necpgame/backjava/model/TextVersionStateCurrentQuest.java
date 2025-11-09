package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * TextVersionStateCurrentQuest
 */

@JsonTypeName("TextVersionState_current_quest")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class TextVersionStateCurrentQuest {

  private @Nullable String questName;

  @Valid
  private List<String> objectives = new ArrayList<>();

  public TextVersionStateCurrentQuest questName(@Nullable String questName) {
    this.questName = questName;
    return this;
  }

  /**
   * Get questName
   * @return questName
   */
  
  @Schema(name = "quest_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_name")
  public @Nullable String getQuestName() {
    return questName;
  }

  public void setQuestName(@Nullable String questName) {
    this.questName = questName;
  }

  public TextVersionStateCurrentQuest objectives(List<String> objectives) {
    this.objectives = objectives;
    return this;
  }

  public TextVersionStateCurrentQuest addObjectivesItem(String objectivesItem) {
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
  public List<String> getObjectives() {
    return objectives;
  }

  public void setObjectives(List<String> objectives) {
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
    TextVersionStateCurrentQuest textVersionStateCurrentQuest = (TextVersionStateCurrentQuest) o;
    return Objects.equals(this.questName, textVersionStateCurrentQuest.questName) &&
        Objects.equals(this.objectives, textVersionStateCurrentQuest.objectives);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questName, objectives);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TextVersionStateCurrentQuest {\n");
    sb.append("    questName: ").append(toIndentedString(questName)).append("\n");
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

