package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.DialogueTreeNodesInner;
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
 * DialogueTree
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class DialogueTree {

  private @Nullable String questId;

  private @Nullable Integer totalNodes;

  private @Nullable String rootNodeId;

  @Valid
  private List<@Valid DialogueTreeNodesInner> nodes = new ArrayList<>();

  public DialogueTree questId(@Nullable String questId) {
    this.questId = questId;
    return this;
  }

  /**
   * Get questId
   * @return questId
   */
  
  @Schema(name = "quest_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_id")
  public @Nullable String getQuestId() {
    return questId;
  }

  public void setQuestId(@Nullable String questId) {
    this.questId = questId;
  }

  public DialogueTree totalNodes(@Nullable Integer totalNodes) {
    this.totalNodes = totalNodes;
    return this;
  }

  /**
   * Get totalNodes
   * @return totalNodes
   */
  
  @Schema(name = "total_nodes", example = "25", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_nodes")
  public @Nullable Integer getTotalNodes() {
    return totalNodes;
  }

  public void setTotalNodes(@Nullable Integer totalNodes) {
    this.totalNodes = totalNodes;
  }

  public DialogueTree rootNodeId(@Nullable String rootNodeId) {
    this.rootNodeId = rootNodeId;
    return this;
  }

  /**
   * Get rootNodeId
   * @return rootNodeId
   */
  
  @Schema(name = "root_node_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("root_node_id")
  public @Nullable String getRootNodeId() {
    return rootNodeId;
  }

  public void setRootNodeId(@Nullable String rootNodeId) {
    this.rootNodeId = rootNodeId;
  }

  public DialogueTree nodes(List<@Valid DialogueTreeNodesInner> nodes) {
    this.nodes = nodes;
    return this;
  }

  public DialogueTree addNodesItem(DialogueTreeNodesInner nodesItem) {
    if (this.nodes == null) {
      this.nodes = new ArrayList<>();
    }
    this.nodes.add(nodesItem);
    return this;
  }

  /**
   * Get nodes
   * @return nodes
   */
  @Valid 
  @Schema(name = "nodes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nodes")
  public List<@Valid DialogueTreeNodesInner> getNodes() {
    return nodes;
  }

  public void setNodes(List<@Valid DialogueTreeNodesInner> nodes) {
    this.nodes = nodes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DialogueTree dialogueTree = (DialogueTree) o;
    return Objects.equals(this.questId, dialogueTree.questId) &&
        Objects.equals(this.totalNodes, dialogueTree.totalNodes) &&
        Objects.equals(this.rootNodeId, dialogueTree.rootNodeId) &&
        Objects.equals(this.nodes, dialogueTree.nodes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, totalNodes, rootNodeId, nodes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DialogueTree {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    totalNodes: ").append(toIndentedString(totalNodes)).append("\n");
    sb.append("    rootNodeId: ").append(toIndentedString(rootNodeId)).append("\n");
    sb.append("    nodes: ").append(toIndentedString(nodes)).append("\n");
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

