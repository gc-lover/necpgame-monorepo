package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.narrativeservice.model.GetQuestConnections200ResponseGraph;
import com.necpgame.narrativeservice.model.QuestConnection;
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
 * GetQuestConnections200Response
 */

@JsonTypeName("getQuestConnections_200_response")

public class GetQuestConnections200Response {

  private @Nullable String questId;

  @Valid
  private List<@Valid QuestConnection> connections = new ArrayList<>();

  private @Nullable GetQuestConnections200ResponseGraph graph;

  public GetQuestConnections200Response questId(@Nullable String questId) {
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

  public GetQuestConnections200Response connections(List<@Valid QuestConnection> connections) {
    this.connections = connections;
    return this;
  }

  public GetQuestConnections200Response addConnectionsItem(QuestConnection connectionsItem) {
    if (this.connections == null) {
      this.connections = new ArrayList<>();
    }
    this.connections.add(connectionsItem);
    return this;
  }

  /**
   * Get connections
   * @return connections
   */
  @Valid 
  @Schema(name = "connections", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("connections")
  public List<@Valid QuestConnection> getConnections() {
    return connections;
  }

  public void setConnections(List<@Valid QuestConnection> connections) {
    this.connections = connections;
  }

  public GetQuestConnections200Response graph(@Nullable GetQuestConnections200ResponseGraph graph) {
    this.graph = graph;
    return this;
  }

  /**
   * Get graph
   * @return graph
   */
  @Valid 
  @Schema(name = "graph", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("graph")
  public @Nullable GetQuestConnections200ResponseGraph getGraph() {
    return graph;
  }

  public void setGraph(@Nullable GetQuestConnections200ResponseGraph graph) {
    this.graph = graph;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetQuestConnections200Response getQuestConnections200Response = (GetQuestConnections200Response) o;
    return Objects.equals(this.questId, getQuestConnections200Response.questId) &&
        Objects.equals(this.connections, getQuestConnections200Response.connections) &&
        Objects.equals(this.graph, getQuestConnections200Response.graph);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, connections, graph);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetQuestConnections200Response {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    connections: ").append(toIndentedString(connections)).append("\n");
    sb.append("    graph: ").append(toIndentedString(graph)).append("\n");
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

