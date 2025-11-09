package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.DialogueOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
 * DialogueNode
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class DialogueNode {

  private @Nullable String nodeId;

  private @Nullable String speaker;

  private @Nullable String text;

  @Valid
  private List<@Valid DialogueOption> options = new ArrayList<>();

  @Valid
  private Map<String, Object> conditions = new HashMap<>();

  public DialogueNode nodeId(@Nullable String nodeId) {
    this.nodeId = nodeId;
    return this;
  }

  /**
   * Get nodeId
   * @return nodeId
   */
  
  @Schema(name = "node_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("node_id")
  public @Nullable String getNodeId() {
    return nodeId;
  }

  public void setNodeId(@Nullable String nodeId) {
    this.nodeId = nodeId;
  }

  public DialogueNode speaker(@Nullable String speaker) {
    this.speaker = speaker;
    return this;
  }

  /**
   * Get speaker
   * @return speaker
   */
  
  @Schema(name = "speaker", example = "Jackie Welles", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("speaker")
  public @Nullable String getSpeaker() {
    return speaker;
  }

  public void setSpeaker(@Nullable String speaker) {
    this.speaker = speaker;
  }

  public DialogueNode text(@Nullable String text) {
    this.text = text;
    return this;
  }

  /**
   * Get text
   * @return text
   */
  
  @Schema(name = "text", example = "Yo, V! Ready for your first gig?", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("text")
  public @Nullable String getText() {
    return text;
  }

  public void setText(@Nullable String text) {
    this.text = text;
  }

  public DialogueNode options(List<@Valid DialogueOption> options) {
    this.options = options;
    return this;
  }

  public DialogueNode addOptionsItem(DialogueOption optionsItem) {
    if (this.options == null) {
      this.options = new ArrayList<>();
    }
    this.options.add(optionsItem);
    return this;
  }

  /**
   * Get options
   * @return options
   */
  @Valid 
  @Schema(name = "options", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("options")
  public List<@Valid DialogueOption> getOptions() {
    return options;
  }

  public void setOptions(List<@Valid DialogueOption> options) {
    this.options = options;
  }

  public DialogueNode conditions(Map<String, Object> conditions) {
    this.conditions = conditions;
    return this;
  }

  public DialogueNode putConditionsItem(String key, Object conditionsItem) {
    if (this.conditions == null) {
      this.conditions = new HashMap<>();
    }
    this.conditions.put(key, conditionsItem);
    return this;
  }

  /**
   * Условия показа (reputation, flags, items)
   * @return conditions
   */
  
  @Schema(name = "conditions", description = "Условия показа (reputation, flags, items)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conditions")
  public Map<String, Object> getConditions() {
    return conditions;
  }

  public void setConditions(Map<String, Object> conditions) {
    this.conditions = conditions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DialogueNode dialogueNode = (DialogueNode) o;
    return Objects.equals(this.nodeId, dialogueNode.nodeId) &&
        Objects.equals(this.speaker, dialogueNode.speaker) &&
        Objects.equals(this.text, dialogueNode.text) &&
        Objects.equals(this.options, dialogueNode.options) &&
        Objects.equals(this.conditions, dialogueNode.conditions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeId, speaker, text, options, conditions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DialogueNode {\n");
    sb.append("    nodeId: ").append(toIndentedString(nodeId)).append("\n");
    sb.append("    speaker: ").append(toIndentedString(speaker)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    options: ").append(toIndentedString(options)).append("\n");
    sb.append("    conditions: ").append(toIndentedString(conditions)).append("\n");
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

