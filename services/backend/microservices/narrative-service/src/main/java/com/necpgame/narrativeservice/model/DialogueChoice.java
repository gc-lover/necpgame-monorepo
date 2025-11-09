package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.Consequence;
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
 * DialogueChoice
 */


public class DialogueChoice {

  private String choiceId;

  private String text;

  private @Nullable String requiredSkill;

  private @Nullable Integer difficultyClass;

  private @Nullable String nextNodeId;

  @Valid
  private List<@Valid Consequence> consequences = new ArrayList<>();

  public DialogueChoice() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DialogueChoice(String choiceId, String text) {
    this.choiceId = choiceId;
    this.text = text;
  }

  public DialogueChoice choiceId(String choiceId) {
    this.choiceId = choiceId;
    return this;
  }

  /**
   * Get choiceId
   * @return choiceId
   */
  @NotNull 
  @Schema(name = "choice_id", example = "choice_persuade", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("choice_id")
  public String getChoiceId() {
    return choiceId;
  }

  public void setChoiceId(String choiceId) {
    this.choiceId = choiceId;
  }

  public DialogueChoice text(String text) {
    this.text = text;
    return this;
  }

  /**
   * Get text
   * @return text
   */
  @NotNull 
  @Schema(name = "text", example = "[Persuasion] Trust me, I know what I'm doing.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("text")
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public DialogueChoice requiredSkill(@Nullable String requiredSkill) {
    this.requiredSkill = requiredSkill;
    return this;
  }

  /**
   * Навык для skill check
   * @return requiredSkill
   */
  
  @Schema(name = "required_skill", example = "persuasion", description = "Навык для skill check", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_skill")
  public @Nullable String getRequiredSkill() {
    return requiredSkill;
  }

  public void setRequiredSkill(@Nullable String requiredSkill) {
    this.requiredSkill = requiredSkill;
  }

  public DialogueChoice difficultyClass(@Nullable Integer difficultyClass) {
    this.difficultyClass = difficultyClass;
    return this;
  }

  /**
   * DC для D&D check
   * minimum: 1
   * maximum: 30
   * @return difficultyClass
   */
  @Min(value = 1) @Max(value = 30) 
  @Schema(name = "difficulty_class", example = "15", description = "DC для D&D check", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("difficulty_class")
  public @Nullable Integer getDifficultyClass() {
    return difficultyClass;
  }

  public void setDifficultyClass(@Nullable Integer difficultyClass) {
    this.difficultyClass = difficultyClass;
  }

  public DialogueChoice nextNodeId(@Nullable String nextNodeId) {
    this.nextNodeId = nextNodeId;
    return this;
  }

  /**
   * ID следующего node
   * @return nextNodeId
   */
  
  @Schema(name = "next_node_id", example = "node_002", description = "ID следующего node", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("next_node_id")
  public @Nullable String getNextNodeId() {
    return nextNodeId;
  }

  public void setNextNodeId(@Nullable String nextNodeId) {
    this.nextNodeId = nextNodeId;
  }

  public DialogueChoice consequences(List<@Valid Consequence> consequences) {
    this.consequences = consequences;
    return this;
  }

  public DialogueChoice addConsequencesItem(Consequence consequencesItem) {
    if (this.consequences == null) {
      this.consequences = new ArrayList<>();
    }
    this.consequences.add(consequencesItem);
    return this;
  }

  /**
   * Get consequences
   * @return consequences
   */
  @Valid 
  @Schema(name = "consequences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequences")
  public List<@Valid Consequence> getConsequences() {
    return consequences;
  }

  public void setConsequences(List<@Valid Consequence> consequences) {
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
    DialogueChoice dialogueChoice = (DialogueChoice) o;
    return Objects.equals(this.choiceId, dialogueChoice.choiceId) &&
        Objects.equals(this.text, dialogueChoice.text) &&
        Objects.equals(this.requiredSkill, dialogueChoice.requiredSkill) &&
        Objects.equals(this.difficultyClass, dialogueChoice.difficultyClass) &&
        Objects.equals(this.nextNodeId, dialogueChoice.nextNodeId) &&
        Objects.equals(this.consequences, dialogueChoice.consequences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(choiceId, text, requiredSkill, difficultyClass, nextNodeId, consequences);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DialogueChoice {\n");
    sb.append("    choiceId: ").append(toIndentedString(choiceId)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    requiredSkill: ").append(toIndentedString(requiredSkill)).append("\n");
    sb.append("    difficultyClass: ").append(toIndentedString(difficultyClass)).append("\n");
    sb.append("    nextNodeId: ").append(toIndentedString(nextNodeId)).append("\n");
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

