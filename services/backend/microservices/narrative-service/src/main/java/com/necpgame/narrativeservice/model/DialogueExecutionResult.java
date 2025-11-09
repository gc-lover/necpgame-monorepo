package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.Consequence;
import com.necpgame.narrativeservice.model.DialogueExecutionResultSkillCheckResult;
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
 * DialogueExecutionResult
 */


public class DialogueExecutionResult {

  private @Nullable Boolean success;

  private @Nullable DialogueExecutionResultSkillCheckResult skillCheckResult;

  private @Nullable String nextNodeId;

  @Valid
  private List<@Valid Consequence> consequences = new ArrayList<>();

  private @Nullable String dialogueText;

  public DialogueExecutionResult success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Успешно ли выполнен choice
   * @return success
   */
  
  @Schema(name = "success", description = "Успешно ли выполнен choice", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public DialogueExecutionResult skillCheckResult(@Nullable DialogueExecutionResultSkillCheckResult skillCheckResult) {
    this.skillCheckResult = skillCheckResult;
    return this;
  }

  /**
   * Get skillCheckResult
   * @return skillCheckResult
   */
  @Valid 
  @Schema(name = "skill_check_result", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_check_result")
  public @Nullable DialogueExecutionResultSkillCheckResult getSkillCheckResult() {
    return skillCheckResult;
  }

  public void setSkillCheckResult(@Nullable DialogueExecutionResultSkillCheckResult skillCheckResult) {
    this.skillCheckResult = skillCheckResult;
  }

  public DialogueExecutionResult nextNodeId(@Nullable String nextNodeId) {
    this.nextNodeId = nextNodeId;
    return this;
  }

  /**
   * ID следующего node
   * @return nextNodeId
   */
  
  @Schema(name = "next_node_id", example = "node_003", description = "ID следующего node", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("next_node_id")
  public @Nullable String getNextNodeId() {
    return nextNodeId;
  }

  public void setNextNodeId(@Nullable String nextNodeId) {
    this.nextNodeId = nextNodeId;
  }

  public DialogueExecutionResult consequences(List<@Valid Consequence> consequences) {
    this.consequences = consequences;
    return this;
  }

  public DialogueExecutionResult addConsequencesItem(Consequence consequencesItem) {
    if (this.consequences == null) {
      this.consequences = new ArrayList<>();
    }
    this.consequences.add(consequencesItem);
    return this;
  }

  /**
   * Примененные consequences
   * @return consequences
   */
  @Valid 
  @Schema(name = "consequences", description = "Примененные consequences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequences")
  public List<@Valid Consequence> getConsequences() {
    return consequences;
  }

  public void setConsequences(List<@Valid Consequence> consequences) {
    this.consequences = consequences;
  }

  public DialogueExecutionResult dialogueText(@Nullable String dialogueText) {
    this.dialogueText = dialogueText;
    return this;
  }

  /**
   * Текст ответа NPC
   * @return dialogueText
   */
  
  @Schema(name = "dialogue_text", example = "Alright, I believe you. Let's do this.", description = "Текст ответа NPC", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dialogue_text")
  public @Nullable String getDialogueText() {
    return dialogueText;
  }

  public void setDialogueText(@Nullable String dialogueText) {
    this.dialogueText = dialogueText;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DialogueExecutionResult dialogueExecutionResult = (DialogueExecutionResult) o;
    return Objects.equals(this.success, dialogueExecutionResult.success) &&
        Objects.equals(this.skillCheckResult, dialogueExecutionResult.skillCheckResult) &&
        Objects.equals(this.nextNodeId, dialogueExecutionResult.nextNodeId) &&
        Objects.equals(this.consequences, dialogueExecutionResult.consequences) &&
        Objects.equals(this.dialogueText, dialogueExecutionResult.dialogueText);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, skillCheckResult, nextNodeId, consequences, dialogueText);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DialogueExecutionResult {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    skillCheckResult: ").append(toIndentedString(skillCheckResult)).append("\n");
    sb.append("    nextNodeId: ").append(toIndentedString(nextNodeId)).append("\n");
    sb.append("    consequences: ").append(toIndentedString(consequences)).append("\n");
    sb.append("    dialogueText: ").append(toIndentedString(dialogueText)).append("\n");
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

