package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.ConversationSnapshotRecommendations;
import com.necpgame.narrativeservice.model.DialogueState;
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
 * ConversationSnapshot
 */


public class ConversationSnapshot {

  private String questId;

  private String version;

  @Valid
  private List<String> entryNodes = new ArrayList<>();

  @Valid
  private List<@Valid DialogueState> states = new ArrayList<>();

  @Valid
  private List<String> availableNodes = new ArrayList<>();

  @Valid
  private List<String> lockedNodes = new ArrayList<>();

  private @Nullable ConversationSnapshotRecommendations recommendations;

  public ConversationSnapshot() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ConversationSnapshot(String questId, String version, List<String> entryNodes, List<@Valid DialogueState> states) {
    this.questId = questId;
    this.version = version;
    this.entryNodes = entryNodes;
    this.states = states;
  }

  public ConversationSnapshot questId(String questId) {
    this.questId = questId;
    return this;
  }

  /**
   * Get questId
   * @return questId
   */
  @NotNull 
  @Schema(name = "questId", example = "quest-main-001-first-steps", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("questId")
  public String getQuestId() {
    return questId;
  }

  public void setQuestId(String questId) {
    this.questId = questId;
  }

  public ConversationSnapshot version(String version) {
    this.version = version;
    return this;
  }

  /**
   * Get version
   * @return version
   */
  @NotNull 
  @Schema(name = "version", example = "1.0.0", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("version")
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public ConversationSnapshot entryNodes(List<String> entryNodes) {
    this.entryNodes = entryNodes;
    return this;
  }

  public ConversationSnapshot addEntryNodesItem(String entryNodesItem) {
    if (this.entryNodes == null) {
      this.entryNodes = new ArrayList<>();
    }
    this.entryNodes.add(entryNodesItem);
    return this;
  }

  /**
   * Get entryNodes
   * @return entryNodes
   */
  @NotNull 
  @Schema(name = "entryNodes", example = "[\"arrival\"]", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("entryNodes")
  public List<String> getEntryNodes() {
    return entryNodes;
  }

  public void setEntryNodes(List<String> entryNodes) {
    this.entryNodes = entryNodes;
  }

  public ConversationSnapshot states(List<@Valid DialogueState> states) {
    this.states = states;
    return this;
  }

  public ConversationSnapshot addStatesItem(DialogueState statesItem) {
    if (this.states == null) {
      this.states = new ArrayList<>();
    }
    this.states.add(statesItem);
    return this;
  }

  /**
   * Get states
   * @return states
   */
  @NotNull @Valid 
  @Schema(name = "states", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("states")
  public List<@Valid DialogueState> getStates() {
    return states;
  }

  public void setStates(List<@Valid DialogueState> states) {
    this.states = states;
  }

  public ConversationSnapshot availableNodes(List<String> availableNodes) {
    this.availableNodes = availableNodes;
    return this;
  }

  public ConversationSnapshot addAvailableNodesItem(String availableNodesItem) {
    if (this.availableNodes == null) {
      this.availableNodes = new ArrayList<>();
    }
    this.availableNodes.add(availableNodesItem);
    return this;
  }

  /**
   * Узлы, доступные при заданных флагах игрока.
   * @return availableNodes
   */
  
  @Schema(name = "availableNodes", description = "Узлы, доступные при заданных флагах игрока.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("availableNodes")
  public List<String> getAvailableNodes() {
    return availableNodes;
  }

  public void setAvailableNodes(List<String> availableNodes) {
    this.availableNodes = availableNodes;
  }

  public ConversationSnapshot lockedNodes(List<String> lockedNodes) {
    this.lockedNodes = lockedNodes;
    return this;
  }

  public ConversationSnapshot addLockedNodesItem(String lockedNodesItem) {
    if (this.lockedNodes == null) {
      this.lockedNodes = new ArrayList<>();
    }
    this.lockedNodes.add(lockedNodesItem);
    return this;
  }

  /**
   * Узлы, недоступные из-за отсутствующих условий.
   * @return lockedNodes
   */
  
  @Schema(name = "lockedNodes", description = "Узлы, недоступные из-за отсутствующих условий.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lockedNodes")
  public List<String> getLockedNodes() {
    return lockedNodes;
  }

  public void setLockedNodes(List<String> lockedNodes) {
    this.lockedNodes = lockedNodes;
  }

  public ConversationSnapshot recommendations(@Nullable ConversationSnapshotRecommendations recommendations) {
    this.recommendations = recommendations;
    return this;
  }

  /**
   * Get recommendations
   * @return recommendations
   */
  @Valid 
  @Schema(name = "recommendations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendations")
  public @Nullable ConversationSnapshotRecommendations getRecommendations() {
    return recommendations;
  }

  public void setRecommendations(@Nullable ConversationSnapshotRecommendations recommendations) {
    this.recommendations = recommendations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConversationSnapshot conversationSnapshot = (ConversationSnapshot) o;
    return Objects.equals(this.questId, conversationSnapshot.questId) &&
        Objects.equals(this.version, conversationSnapshot.version) &&
        Objects.equals(this.entryNodes, conversationSnapshot.entryNodes) &&
        Objects.equals(this.states, conversationSnapshot.states) &&
        Objects.equals(this.availableNodes, conversationSnapshot.availableNodes) &&
        Objects.equals(this.lockedNodes, conversationSnapshot.lockedNodes) &&
        Objects.equals(this.recommendations, conversationSnapshot.recommendations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, version, entryNodes, states, availableNodes, lockedNodes, recommendations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConversationSnapshot {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    entryNodes: ").append(toIndentedString(entryNodes)).append("\n");
    sb.append("    states: ").append(toIndentedString(states)).append("\n");
    sb.append("    availableNodes: ").append(toIndentedString(availableNodes)).append("\n");
    sb.append("    lockedNodes: ").append(toIndentedString(lockedNodes)).append("\n");
    sb.append("    recommendations: ").append(toIndentedString(recommendations)).append("\n");
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

