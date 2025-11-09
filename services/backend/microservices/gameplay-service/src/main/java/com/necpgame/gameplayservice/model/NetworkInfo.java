package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.NetworkInfoConnectionsInner;
import com.necpgame.gameplayservice.model.NetworkNode;
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
 * NetworkInfo
 */


public class NetworkInfo {

  private @Nullable String networkId;

  private @Nullable String name;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    LOCAL("local"),
    
    CORPORATE("corporate"),
    
    CITY("city"),
    
    PERSONAL("personal"),
    
    ISOLATED("isolated");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  /**
   * Gets or Sets complexity
   */
  public enum ComplexityEnum {
    SIMPLE("simple"),
    
    HIERARCHICAL("hierarchical"),
    
    GRAPH("graph"),
    
    ISOLATED("isolated");

    private final String value;

    ComplexityEnum(String value) {
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
    public static ComplexityEnum fromValue(String value) {
      for (ComplexityEnum b : ComplexityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ComplexityEnum complexity;

  private @Nullable Integer securityLevel;

  private @Nullable Boolean isIsolated;

  @Valid
  private List<@Valid NetworkNode> nodes = new ArrayList<>();

  @Valid
  private List<@Valid NetworkInfoConnectionsInner> connections = new ArrayList<>();

  public NetworkInfo networkId(@Nullable String networkId) {
    this.networkId = networkId;
    return this;
  }

  /**
   * Get networkId
   * @return networkId
   */
  
  @Schema(name = "network_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("network_id")
  public @Nullable String getNetworkId() {
    return networkId;
  }

  public void setNetworkId(@Nullable String networkId) {
    this.networkId = networkId;
  }

  public NetworkInfo name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public NetworkInfo type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public NetworkInfo complexity(@Nullable ComplexityEnum complexity) {
    this.complexity = complexity;
    return this;
  }

  /**
   * Get complexity
   * @return complexity
   */
  
  @Schema(name = "complexity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("complexity")
  public @Nullable ComplexityEnum getComplexity() {
    return complexity;
  }

  public void setComplexity(@Nullable ComplexityEnum complexity) {
    this.complexity = complexity;
  }

  public NetworkInfo securityLevel(@Nullable Integer securityLevel) {
    this.securityLevel = securityLevel;
    return this;
  }

  /**
   * Get securityLevel
   * minimum: 1
   * maximum: 10
   * @return securityLevel
   */
  @Min(value = 1) @Max(value = 10) 
  @Schema(name = "security_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("security_level")
  public @Nullable Integer getSecurityLevel() {
    return securityLevel;
  }

  public void setSecurityLevel(@Nullable Integer securityLevel) {
    this.securityLevel = securityLevel;
  }

  public NetworkInfo isIsolated(@Nullable Boolean isIsolated) {
    this.isIsolated = isIsolated;
    return this;
  }

  /**
   * Get isIsolated
   * @return isIsolated
   */
  
  @Schema(name = "is_isolated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_isolated")
  public @Nullable Boolean getIsIsolated() {
    return isIsolated;
  }

  public void setIsIsolated(@Nullable Boolean isIsolated) {
    this.isIsolated = isIsolated;
  }

  public NetworkInfo nodes(List<@Valid NetworkNode> nodes) {
    this.nodes = nodes;
    return this;
  }

  public NetworkInfo addNodesItem(NetworkNode nodesItem) {
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
  public List<@Valid NetworkNode> getNodes() {
    return nodes;
  }

  public void setNodes(List<@Valid NetworkNode> nodes) {
    this.nodes = nodes;
  }

  public NetworkInfo connections(List<@Valid NetworkInfoConnectionsInner> connections) {
    this.connections = connections;
    return this;
  }

  public NetworkInfo addConnectionsItem(NetworkInfoConnectionsInner connectionsItem) {
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
  public List<@Valid NetworkInfoConnectionsInner> getConnections() {
    return connections;
  }

  public void setConnections(List<@Valid NetworkInfoConnectionsInner> connections) {
    this.connections = connections;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NetworkInfo networkInfo = (NetworkInfo) o;
    return Objects.equals(this.networkId, networkInfo.networkId) &&
        Objects.equals(this.name, networkInfo.name) &&
        Objects.equals(this.type, networkInfo.type) &&
        Objects.equals(this.complexity, networkInfo.complexity) &&
        Objects.equals(this.securityLevel, networkInfo.securityLevel) &&
        Objects.equals(this.isIsolated, networkInfo.isIsolated) &&
        Objects.equals(this.nodes, networkInfo.nodes) &&
        Objects.equals(this.connections, networkInfo.connections);
  }

  @Override
  public int hashCode() {
    return Objects.hash(networkId, name, type, complexity, securityLevel, isIsolated, nodes, connections);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NetworkInfo {\n");
    sb.append("    networkId: ").append(toIndentedString(networkId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    complexity: ").append(toIndentedString(complexity)).append("\n");
    sb.append("    securityLevel: ").append(toIndentedString(securityLevel)).append("\n");
    sb.append("    isIsolated: ").append(toIndentedString(isIsolated)).append("\n");
    sb.append("    nodes: ").append(toIndentedString(nodes)).append("\n");
    sb.append("    connections: ").append(toIndentedString(connections)).append("\n");
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

