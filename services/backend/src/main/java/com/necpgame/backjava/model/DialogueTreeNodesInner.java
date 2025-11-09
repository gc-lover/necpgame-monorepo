package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.DialogueTreeNodesInnerChoicesInner;
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
 * DialogueTreeNodesInner
 */

@JsonTypeName("DialogueTree_nodes_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class DialogueTreeNodesInner {

  private @Nullable String nodeId;

  private @Nullable String speaker;

  private @Nullable String text;

  @Valid
  private List<@Valid DialogueTreeNodesInnerChoicesInner> choices = new ArrayList<>();

  public DialogueTreeNodesInner nodeId(@Nullable String nodeId) {
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

  public DialogueTreeNodesInner speaker(@Nullable String speaker) {
    this.speaker = speaker;
    return this;
  }

  /**
   * Get speaker
   * @return speaker
   */
  
  @Schema(name = "speaker", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("speaker")
  public @Nullable String getSpeaker() {
    return speaker;
  }

  public void setSpeaker(@Nullable String speaker) {
    this.speaker = speaker;
  }

  public DialogueTreeNodesInner text(@Nullable String text) {
    this.text = text;
    return this;
  }

  /**
   * Get text
   * @return text
   */
  
  @Schema(name = "text", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("text")
  public @Nullable String getText() {
    return text;
  }

  public void setText(@Nullable String text) {
    this.text = text;
  }

  public DialogueTreeNodesInner choices(List<@Valid DialogueTreeNodesInnerChoicesInner> choices) {
    this.choices = choices;
    return this;
  }

  public DialogueTreeNodesInner addChoicesItem(DialogueTreeNodesInnerChoicesInner choicesItem) {
    if (this.choices == null) {
      this.choices = new ArrayList<>();
    }
    this.choices.add(choicesItem);
    return this;
  }

  /**
   * Get choices
   * @return choices
   */
  @Valid 
  @Schema(name = "choices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("choices")
  public List<@Valid DialogueTreeNodesInnerChoicesInner> getChoices() {
    return choices;
  }

  public void setChoices(List<@Valid DialogueTreeNodesInnerChoicesInner> choices) {
    this.choices = choices;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DialogueTreeNodesInner dialogueTreeNodesInner = (DialogueTreeNodesInner) o;
    return Objects.equals(this.nodeId, dialogueTreeNodesInner.nodeId) &&
        Objects.equals(this.speaker, dialogueTreeNodesInner.speaker) &&
        Objects.equals(this.text, dialogueTreeNodesInner.text) &&
        Objects.equals(this.choices, dialogueTreeNodesInner.choices);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeId, speaker, text, choices);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DialogueTreeNodesInner {\n");
    sb.append("    nodeId: ").append(toIndentedString(nodeId)).append("\n");
    sb.append("    speaker: ").append(toIndentedString(speaker)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    choices: ").append(toIndentedString(choices)).append("\n");
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

