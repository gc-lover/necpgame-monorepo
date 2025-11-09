package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.DialogueNode;
import com.necpgame.narrativeservice.model.LockedOption;
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
 * DialogueNodeResponse
 */


public class DialogueNodeResponse {

  private String questId;

  private DialogueNode node;

  @Valid
  private List<String> availableOptions = new ArrayList<>();

  @Valid
  private List<@Valid LockedOption> lockedOptions = new ArrayList<>();

  public DialogueNodeResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DialogueNodeResponse(String questId, DialogueNode node) {
    this.questId = questId;
    this.node = node;
  }

  public DialogueNodeResponse questId(String questId) {
    this.questId = questId;
    return this;
  }

  /**
   * Get questId
   * @return questId
   */
  @NotNull 
  @Schema(name = "questId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("questId")
  public String getQuestId() {
    return questId;
  }

  public void setQuestId(String questId) {
    this.questId = questId;
  }

  public DialogueNodeResponse node(DialogueNode node) {
    this.node = node;
    return this;
  }

  /**
   * Get node
   * @return node
   */
  @NotNull @Valid 
  @Schema(name = "node", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("node")
  public DialogueNode getNode() {
    return node;
  }

  public void setNode(DialogueNode node) {
    this.node = node;
  }

  public DialogueNodeResponse availableOptions(List<String> availableOptions) {
    this.availableOptions = availableOptions;
    return this;
  }

  public DialogueNodeResponse addAvailableOptionsItem(String availableOptionsItem) {
    if (this.availableOptions == null) {
      this.availableOptions = new ArrayList<>();
    }
    this.availableOptions.add(availableOptionsItem);
    return this;
  }

  /**
   * Get availableOptions
   * @return availableOptions
   */
  
  @Schema(name = "availableOptions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("availableOptions")
  public List<String> getAvailableOptions() {
    return availableOptions;
  }

  public void setAvailableOptions(List<String> availableOptions) {
    this.availableOptions = availableOptions;
  }

  public DialogueNodeResponse lockedOptions(List<@Valid LockedOption> lockedOptions) {
    this.lockedOptions = lockedOptions;
    return this;
  }

  public DialogueNodeResponse addLockedOptionsItem(LockedOption lockedOptionsItem) {
    if (this.lockedOptions == null) {
      this.lockedOptions = new ArrayList<>();
    }
    this.lockedOptions.add(lockedOptionsItem);
    return this;
  }

  /**
   * Get lockedOptions
   * @return lockedOptions
   */
  @Valid 
  @Schema(name = "lockedOptions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lockedOptions")
  public List<@Valid LockedOption> getLockedOptions() {
    return lockedOptions;
  }

  public void setLockedOptions(List<@Valid LockedOption> lockedOptions) {
    this.lockedOptions = lockedOptions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DialogueNodeResponse dialogueNodeResponse = (DialogueNodeResponse) o;
    return Objects.equals(this.questId, dialogueNodeResponse.questId) &&
        Objects.equals(this.node, dialogueNodeResponse.node) &&
        Objects.equals(this.availableOptions, dialogueNodeResponse.availableOptions) &&
        Objects.equals(this.lockedOptions, dialogueNodeResponse.lockedOptions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, node, availableOptions, lockedOptions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DialogueNodeResponse {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    node: ").append(toIndentedString(node)).append("\n");
    sb.append("    availableOptions: ").append(toIndentedString(availableOptions)).append("\n");
    sb.append("    lockedOptions: ").append(toIndentedString(lockedOptions)).append("\n");
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

