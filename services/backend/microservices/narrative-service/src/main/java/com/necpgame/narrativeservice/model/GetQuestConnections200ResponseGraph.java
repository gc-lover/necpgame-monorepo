package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * Graph data для визуализации
 */

@Schema(name = "getQuestConnections_200_response_graph", description = "Graph data для визуализации")
@JsonTypeName("getQuestConnections_200_response_graph")

public class GetQuestConnections200ResponseGraph {

  @Valid
  private List<Object> nodes = new ArrayList<>();

  @Valid
  private List<Object> edges = new ArrayList<>();

  public GetQuestConnections200ResponseGraph nodes(List<Object> nodes) {
    this.nodes = nodes;
    return this;
  }

  public GetQuestConnections200ResponseGraph addNodesItem(Object nodesItem) {
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
  
  @Schema(name = "nodes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nodes")
  public List<Object> getNodes() {
    return nodes;
  }

  public void setNodes(List<Object> nodes) {
    this.nodes = nodes;
  }

  public GetQuestConnections200ResponseGraph edges(List<Object> edges) {
    this.edges = edges;
    return this;
  }

  public GetQuestConnections200ResponseGraph addEdgesItem(Object edgesItem) {
    if (this.edges == null) {
      this.edges = new ArrayList<>();
    }
    this.edges.add(edgesItem);
    return this;
  }

  /**
   * Get edges
   * @return edges
   */
  
  @Schema(name = "edges", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("edges")
  public List<Object> getEdges() {
    return edges;
  }

  public void setEdges(List<Object> edges) {
    this.edges = edges;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetQuestConnections200ResponseGraph getQuestConnections200ResponseGraph = (GetQuestConnections200ResponseGraph) o;
    return Objects.equals(this.nodes, getQuestConnections200ResponseGraph.nodes) &&
        Objects.equals(this.edges, getQuestConnections200ResponseGraph.edges);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodes, edges);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetQuestConnections200ResponseGraph {\n");
    sb.append("    nodes: ").append(toIndentedString(nodes)).append("\n");
    sb.append("    edges: ").append(toIndentedString(edges)).append("\n");
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

