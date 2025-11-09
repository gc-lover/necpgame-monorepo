package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.narrativeservice.model.DialogueNode;
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
 * GetMainQuestNodes200Response
 */

@JsonTypeName("getMainQuestNodes_200_response")

public class GetMainQuestNodes200Response {

  private @Nullable String questId;

  @Valid
  private List<@Valid DialogueNode> nodes = new ArrayList<>();

  private @Nullable String currentNode;

  public GetMainQuestNodes200Response questId(@Nullable String questId) {
    this.questId = questId;
    return this;
  }

  /**
   * Get questId
   * @return questId
   */
  
  @Schema(name = "quest_id", example = "001-first-steps", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_id")
  public @Nullable String getQuestId() {
    return questId;
  }

  public void setQuestId(@Nullable String questId) {
    this.questId = questId;
  }

  public GetMainQuestNodes200Response nodes(List<@Valid DialogueNode> nodes) {
    this.nodes = nodes;
    return this;
  }

  public GetMainQuestNodes200Response addNodesItem(DialogueNode nodesItem) {
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
  public List<@Valid DialogueNode> getNodes() {
    return nodes;
  }

  public void setNodes(List<@Valid DialogueNode> nodes) {
    this.nodes = nodes;
  }

  public GetMainQuestNodes200Response currentNode(@Nullable String currentNode) {
    this.currentNode = currentNode;
    return this;
  }

  /**
   * ID текущего active node
   * @return currentNode
   */
  
  @Schema(name = "current_node", example = "node_001", description = "ID текущего active node", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_node")
  public @Nullable String getCurrentNode() {
    return currentNode;
  }

  public void setCurrentNode(@Nullable String currentNode) {
    this.currentNode = currentNode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetMainQuestNodes200Response getMainQuestNodes200Response = (GetMainQuestNodes200Response) o;
    return Objects.equals(this.questId, getMainQuestNodes200Response.questId) &&
        Objects.equals(this.nodes, getMainQuestNodes200Response.nodes) &&
        Objects.equals(this.currentNode, getMainQuestNodes200Response.currentNode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, nodes, currentNode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetMainQuestNodes200Response {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    nodes: ").append(toIndentedString(nodes)).append("\n");
    sb.append("    currentNode: ").append(toIndentedString(currentNode)).append("\n");
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

