package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.QuestBranchRequirementsSkillChecksInner;
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
 * QuestBranchRequirements
 */

@JsonTypeName("QuestBranch_requirements")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class QuestBranchRequirements {

  @Valid
  private List<String> choices = new ArrayList<>();

  @Valid
  private List<@Valid QuestBranchRequirementsSkillChecksInner> skillChecks = new ArrayList<>();

  public QuestBranchRequirements choices(List<String> choices) {
    this.choices = choices;
    return this;
  }

  public QuestBranchRequirements addChoicesItem(String choicesItem) {
    if (this.choices == null) {
      this.choices = new ArrayList<>();
    }
    this.choices.add(choicesItem);
    return this;
  }

  /**
   * Выборы, которые ведут к этой ветке
   * @return choices
   */
  
  @Schema(name = "choices", description = "Выборы, которые ведут к этой ветке", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("choices")
  public List<String> getChoices() {
    return choices;
  }

  public void setChoices(List<String> choices) {
    this.choices = choices;
  }

  public QuestBranchRequirements skillChecks(List<@Valid QuestBranchRequirementsSkillChecksInner> skillChecks) {
    this.skillChecks = skillChecks;
    return this;
  }

  public QuestBranchRequirements addSkillChecksItem(QuestBranchRequirementsSkillChecksInner skillChecksItem) {
    if (this.skillChecks == null) {
      this.skillChecks = new ArrayList<>();
    }
    this.skillChecks.add(skillChecksItem);
    return this;
  }

  /**
   * Get skillChecks
   * @return skillChecks
   */
  @Valid 
  @Schema(name = "skill_checks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_checks")
  public List<@Valid QuestBranchRequirementsSkillChecksInner> getSkillChecks() {
    return skillChecks;
  }

  public void setSkillChecks(List<@Valid QuestBranchRequirementsSkillChecksInner> skillChecks) {
    this.skillChecks = skillChecks;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestBranchRequirements questBranchRequirements = (QuestBranchRequirements) o;
    return Objects.equals(this.choices, questBranchRequirements.choices) &&
        Objects.equals(this.skillChecks, questBranchRequirements.skillChecks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(choices, skillChecks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestBranchRequirements {\n");
    sb.append("    choices: ").append(toIndentedString(choices)).append("\n");
    sb.append("    skillChecks: ").append(toIndentedString(skillChecks)).append("\n");
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

