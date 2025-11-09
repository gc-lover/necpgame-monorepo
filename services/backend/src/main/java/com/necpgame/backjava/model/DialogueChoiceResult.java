package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.DialogueNode;
import com.necpgame.backjava.model.SkillCheckResult;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DialogueChoiceResult
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class DialogueChoiceResult {

  private @Nullable Boolean success;

  private @Nullable DialogueNode nextNode;

  private @Nullable SkillCheckResult skillCheckPerformed;

  @Valid
  private Map<String, Object> effectsApplied = new HashMap<>();

  private @Nullable Boolean questUpdated;

  public DialogueChoiceResult success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public DialogueChoiceResult nextNode(@Nullable DialogueNode nextNode) {
    this.nextNode = nextNode;
    return this;
  }

  /**
   * Get nextNode
   * @return nextNode
   */
  @Valid 
  @Schema(name = "next_node", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("next_node")
  public @Nullable DialogueNode getNextNode() {
    return nextNode;
  }

  public void setNextNode(@Nullable DialogueNode nextNode) {
    this.nextNode = nextNode;
  }

  public DialogueChoiceResult skillCheckPerformed(@Nullable SkillCheckResult skillCheckPerformed) {
    this.skillCheckPerformed = skillCheckPerformed;
    return this;
  }

  /**
   * Get skillCheckPerformed
   * @return skillCheckPerformed
   */
  @Valid 
  @Schema(name = "skill_check_performed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_check_performed")
  public @Nullable SkillCheckResult getSkillCheckPerformed() {
    return skillCheckPerformed;
  }

  public void setSkillCheckPerformed(@Nullable SkillCheckResult skillCheckPerformed) {
    this.skillCheckPerformed = skillCheckPerformed;
  }

  public DialogueChoiceResult effectsApplied(Map<String, Object> effectsApplied) {
    this.effectsApplied = effectsApplied;
    return this;
  }

  public DialogueChoiceResult putEffectsAppliedItem(String key, Object effectsAppliedItem) {
    if (this.effectsApplied == null) {
      this.effectsApplied = new HashMap<>();
    }
    this.effectsApplied.put(key, effectsAppliedItem);
    return this;
  }

  /**
   * Примененные эффекты
   * @return effectsApplied
   */
  
  @Schema(name = "effects_applied", description = "Примененные эффекты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effects_applied")
  public Map<String, Object> getEffectsApplied() {
    return effectsApplied;
  }

  public void setEffectsApplied(Map<String, Object> effectsApplied) {
    this.effectsApplied = effectsApplied;
  }

  public DialogueChoiceResult questUpdated(@Nullable Boolean questUpdated) {
    this.questUpdated = questUpdated;
    return this;
  }

  /**
   * Get questUpdated
   * @return questUpdated
   */
  
  @Schema(name = "quest_updated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_updated")
  public @Nullable Boolean getQuestUpdated() {
    return questUpdated;
  }

  public void setQuestUpdated(@Nullable Boolean questUpdated) {
    this.questUpdated = questUpdated;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DialogueChoiceResult dialogueChoiceResult = (DialogueChoiceResult) o;
    return Objects.equals(this.success, dialogueChoiceResult.success) &&
        Objects.equals(this.nextNode, dialogueChoiceResult.nextNode) &&
        Objects.equals(this.skillCheckPerformed, dialogueChoiceResult.skillCheckPerformed) &&
        Objects.equals(this.effectsApplied, dialogueChoiceResult.effectsApplied) &&
        Objects.equals(this.questUpdated, dialogueChoiceResult.questUpdated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, nextNode, skillCheckPerformed, effectsApplied, questUpdated);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DialogueChoiceResult {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    nextNode: ").append(toIndentedString(nextNode)).append("\n");
    sb.append("    skillCheckPerformed: ").append(toIndentedString(skillCheckPerformed)).append("\n");
    sb.append("    effectsApplied: ").append(toIndentedString(effectsApplied)).append("\n");
    sb.append("    questUpdated: ").append(toIndentedString(questUpdated)).append("\n");
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

