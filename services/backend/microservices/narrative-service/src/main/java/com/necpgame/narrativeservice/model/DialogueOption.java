package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.DialogueSkillCheck;
import com.necpgame.narrativeservice.model.OptionOutcome;
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
 * DialogueOption
 */


public class DialogueOption {

  private String id;

  private String textKey;

  @Valid
  private List<@Valid DialogueSkillCheck> skillChecks = new ArrayList<>();

  private @Nullable OptionOutcome success;

  private @Nullable OptionOutcome failure;

  private @Nullable OptionOutcome criticalFailure;

  private @Nullable String nextNode;

  private @Nullable String tooltipKey;

  private @Nullable Integer cooldownSeconds;

  private @Nullable String telemetryTag;

  public DialogueOption() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DialogueOption(String id, String textKey) {
    this.id = id;
    this.textKey = textKey;
  }

  public DialogueOption id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public DialogueOption textKey(String textKey) {
    this.textKey = textKey;
    return this;
  }

  /**
   * Get textKey
   * @return textKey
   */
  @NotNull 
  @Schema(name = "textKey", example = "dialogue.quest001.option.arrival.ready", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("textKey")
  public String getTextKey() {
    return textKey;
  }

  public void setTextKey(String textKey) {
    this.textKey = textKey;
  }

  public DialogueOption skillChecks(List<@Valid DialogueSkillCheck> skillChecks) {
    this.skillChecks = skillChecks;
    return this;
  }

  public DialogueOption addSkillChecksItem(DialogueSkillCheck skillChecksItem) {
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
  @Schema(name = "skillChecks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skillChecks")
  public List<@Valid DialogueSkillCheck> getSkillChecks() {
    return skillChecks;
  }

  public void setSkillChecks(List<@Valid DialogueSkillCheck> skillChecks) {
    this.skillChecks = skillChecks;
  }

  public DialogueOption success(@Nullable OptionOutcome success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  @Valid 
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable OptionOutcome getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable OptionOutcome success) {
    this.success = success;
  }

  public DialogueOption failure(@Nullable OptionOutcome failure) {
    this.failure = failure;
    return this;
  }

  /**
   * Get failure
   * @return failure
   */
  @Valid 
  @Schema(name = "failure", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("failure")
  public @Nullable OptionOutcome getFailure() {
    return failure;
  }

  public void setFailure(@Nullable OptionOutcome failure) {
    this.failure = failure;
  }

  public DialogueOption criticalFailure(@Nullable OptionOutcome criticalFailure) {
    this.criticalFailure = criticalFailure;
    return this;
  }

  /**
   * Get criticalFailure
   * @return criticalFailure
   */
  @Valid 
  @Schema(name = "criticalFailure", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("criticalFailure")
  public @Nullable OptionOutcome getCriticalFailure() {
    return criticalFailure;
  }

  public void setCriticalFailure(@Nullable OptionOutcome criticalFailure) {
    this.criticalFailure = criticalFailure;
  }

  public DialogueOption nextNode(@Nullable String nextNode) {
    this.nextNode = nextNode;
    return this;
  }

  /**
   * Get nextNode
   * @return nextNode
   */
  
  @Schema(name = "nextNode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextNode")
  public @Nullable String getNextNode() {
    return nextNode;
  }

  public void setNextNode(@Nullable String nextNode) {
    this.nextNode = nextNode;
  }

  public DialogueOption tooltipKey(@Nullable String tooltipKey) {
    this.tooltipKey = tooltipKey;
    return this;
  }

  /**
   * Get tooltipKey
   * @return tooltipKey
   */
  
  @Schema(name = "tooltipKey", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tooltipKey")
  public @Nullable String getTooltipKey() {
    return tooltipKey;
  }

  public void setTooltipKey(@Nullable String tooltipKey) {
    this.tooltipKey = tooltipKey;
  }

  public DialogueOption cooldownSeconds(@Nullable Integer cooldownSeconds) {
    this.cooldownSeconds = cooldownSeconds;
    return this;
  }

  /**
   * Get cooldownSeconds
   * @return cooldownSeconds
   */
  
  @Schema(name = "cooldownSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldownSeconds")
  public @Nullable Integer getCooldownSeconds() {
    return cooldownSeconds;
  }

  public void setCooldownSeconds(@Nullable Integer cooldownSeconds) {
    this.cooldownSeconds = cooldownSeconds;
  }

  public DialogueOption telemetryTag(@Nullable String telemetryTag) {
    this.telemetryTag = telemetryTag;
    return this;
  }

  /**
   * Get telemetryTag
   * @return telemetryTag
   */
  
  @Schema(name = "telemetryTag", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("telemetryTag")
  public @Nullable String getTelemetryTag() {
    return telemetryTag;
  }

  public void setTelemetryTag(@Nullable String telemetryTag) {
    this.telemetryTag = telemetryTag;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DialogueOption dialogueOption = (DialogueOption) o;
    return Objects.equals(this.id, dialogueOption.id) &&
        Objects.equals(this.textKey, dialogueOption.textKey) &&
        Objects.equals(this.skillChecks, dialogueOption.skillChecks) &&
        Objects.equals(this.success, dialogueOption.success) &&
        Objects.equals(this.failure, dialogueOption.failure) &&
        Objects.equals(this.criticalFailure, dialogueOption.criticalFailure) &&
        Objects.equals(this.nextNode, dialogueOption.nextNode) &&
        Objects.equals(this.tooltipKey, dialogueOption.tooltipKey) &&
        Objects.equals(this.cooldownSeconds, dialogueOption.cooldownSeconds) &&
        Objects.equals(this.telemetryTag, dialogueOption.telemetryTag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, textKey, skillChecks, success, failure, criticalFailure, nextNode, tooltipKey, cooldownSeconds, telemetryTag);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DialogueOption {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    textKey: ").append(toIndentedString(textKey)).append("\n");
    sb.append("    skillChecks: ").append(toIndentedString(skillChecks)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    failure: ").append(toIndentedString(failure)).append("\n");
    sb.append("    criticalFailure: ").append(toIndentedString(criticalFailure)).append("\n");
    sb.append("    nextNode: ").append(toIndentedString(nextNode)).append("\n");
    sb.append("    tooltipKey: ").append(toIndentedString(tooltipKey)).append("\n");
    sb.append("    cooldownSeconds: ").append(toIndentedString(cooldownSeconds)).append("\n");
    sb.append("    telemetryTag: ").append(toIndentedString(telemetryTag)).append("\n");
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

