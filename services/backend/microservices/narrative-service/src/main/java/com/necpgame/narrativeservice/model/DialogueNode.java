package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.narrativeservice.model.DialogueNodeCinematic;
import com.necpgame.narrativeservice.model.DialogueOption;
import com.necpgame.narrativeservice.model.TutorialHint;
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
 * DialogueNode
 */


public class DialogueNode {

  private String id;

  private String speaker;

  /**
   * Gets or Sets nodeType
   */
  public enum NodeTypeEnum {
    DIALOGUE("dialogue"),
    
    TUTORIAL("tutorial"),
    
    BRANCH("branch"),
    
    REWARD("reward");

    private final String value;

    NodeTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static NodeTypeEnum fromValue(String value) {
      for (NodeTypeEnum b : NodeTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private NodeTypeEnum nodeType;

  private String textKey;

  private @Nullable DialogueNodeCinematic cinematic;

  @Valid
  private List<@Valid DialogueOption> options = new ArrayList<>();

  private @Nullable String defaultNextNode;

  @Valid
  private List<@Valid TutorialHint> tutorials = new ArrayList<>();

  public DialogueNode() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DialogueNode(String id, String speaker, NodeTypeEnum nodeType, String textKey) {
    this.id = id;
    this.speaker = speaker;
    this.nodeType = nodeType;
    this.textKey = textKey;
  }

  public DialogueNode id(String id) {
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

  public DialogueNode speaker(String speaker) {
    this.speaker = speaker;
    return this;
  }

  /**
   * Get speaker
   * @return speaker
   */
  @NotNull 
  @Schema(name = "speaker", example = "marco_fix_sanchez", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("speaker")
  public String getSpeaker() {
    return speaker;
  }

  public void setSpeaker(String speaker) {
    this.speaker = speaker;
  }

  public DialogueNode nodeType(NodeTypeEnum nodeType) {
    this.nodeType = nodeType;
    return this;
  }

  /**
   * Get nodeType
   * @return nodeType
   */
  @NotNull 
  @Schema(name = "nodeType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("nodeType")
  public NodeTypeEnum getNodeType() {
    return nodeType;
  }

  public void setNodeType(NodeTypeEnum nodeType) {
    this.nodeType = nodeType;
  }

  public DialogueNode textKey(String textKey) {
    this.textKey = textKey;
    return this;
  }

  /**
   * Get textKey
   * @return textKey
   */
  @NotNull 
  @Schema(name = "textKey", example = "dialogue.quest001.arrival.marco", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("textKey")
  public String getTextKey() {
    return textKey;
  }

  public void setTextKey(String textKey) {
    this.textKey = textKey;
  }

  public DialogueNode cinematic(@Nullable DialogueNodeCinematic cinematic) {
    this.cinematic = cinematic;
    return this;
  }

  /**
   * Get cinematic
   * @return cinematic
   */
  @Valid 
  @Schema(name = "cinematic", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cinematic")
  public @Nullable DialogueNodeCinematic getCinematic() {
    return cinematic;
  }

  public void setCinematic(@Nullable DialogueNodeCinematic cinematic) {
    this.cinematic = cinematic;
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

  public DialogueNode defaultNextNode(@Nullable String defaultNextNode) {
    this.defaultNextNode = defaultNextNode;
    return this;
  }

  /**
   * Get defaultNextNode
   * @return defaultNextNode
   */
  
  @Schema(name = "defaultNextNode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("defaultNextNode")
  public @Nullable String getDefaultNextNode() {
    return defaultNextNode;
  }

  public void setDefaultNextNode(@Nullable String defaultNextNode) {
    this.defaultNextNode = defaultNextNode;
  }

  public DialogueNode tutorials(List<@Valid TutorialHint> tutorials) {
    this.tutorials = tutorials;
    return this;
  }

  public DialogueNode addTutorialsItem(TutorialHint tutorialsItem) {
    if (this.tutorials == null) {
      this.tutorials = new ArrayList<>();
    }
    this.tutorials.add(tutorialsItem);
    return this;
  }

  /**
   * Get tutorials
   * @return tutorials
   */
  @Valid 
  @Schema(name = "tutorials", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tutorials")
  public List<@Valid TutorialHint> getTutorials() {
    return tutorials;
  }

  public void setTutorials(List<@Valid TutorialHint> tutorials) {
    this.tutorials = tutorials;
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
    return Objects.equals(this.id, dialogueNode.id) &&
        Objects.equals(this.speaker, dialogueNode.speaker) &&
        Objects.equals(this.nodeType, dialogueNode.nodeType) &&
        Objects.equals(this.textKey, dialogueNode.textKey) &&
        Objects.equals(this.cinematic, dialogueNode.cinematic) &&
        Objects.equals(this.options, dialogueNode.options) &&
        Objects.equals(this.defaultNextNode, dialogueNode.defaultNextNode) &&
        Objects.equals(this.tutorials, dialogueNode.tutorials);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, speaker, nodeType, textKey, cinematic, options, defaultNextNode, tutorials);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DialogueNode {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    speaker: ").append(toIndentedString(speaker)).append("\n");
    sb.append("    nodeType: ").append(toIndentedString(nodeType)).append("\n");
    sb.append("    textKey: ").append(toIndentedString(textKey)).append("\n");
    sb.append("    cinematic: ").append(toIndentedString(cinematic)).append("\n");
    sb.append("    options: ").append(toIndentedString(options)).append("\n");
    sb.append("    defaultNextNode: ").append(toIndentedString(defaultNextNode)).append("\n");
    sb.append("    tutorials: ").append(toIndentedString(tutorials)).append("\n");
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

