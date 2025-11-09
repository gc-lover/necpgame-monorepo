package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * NetworkNode
 */


public class NetworkNode {

  private @Nullable String nodeId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    CAMERA("camera"),
    
    DOOR("door"),
    
    ROBOT("robot"),
    
    SERVER("server"),
    
    TERMINAL("terminal"),
    
    TURRET("turret");

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
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("active"),
    
    DISABLED("disabled"),
    
    CONTROLLED("controlled"),
    
    HACKED("hacked");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  private @Nullable Integer securityLevel;

  public NetworkNode nodeId(@Nullable String nodeId) {
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

  public NetworkNode type(@Nullable TypeEnum type) {
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

  public NetworkNode status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public NetworkNode securityLevel(@Nullable Integer securityLevel) {
    this.securityLevel = securityLevel;
    return this;
  }

  /**
   * Get securityLevel
   * @return securityLevel
   */
  
  @Schema(name = "security_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("security_level")
  public @Nullable Integer getSecurityLevel() {
    return securityLevel;
  }

  public void setSecurityLevel(@Nullable Integer securityLevel) {
    this.securityLevel = securityLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NetworkNode networkNode = (NetworkNode) o;
    return Objects.equals(this.nodeId, networkNode.nodeId) &&
        Objects.equals(this.type, networkNode.type) &&
        Objects.equals(this.status, networkNode.status) &&
        Objects.equals(this.securityLevel, networkNode.securityLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeId, type, status, securityLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NetworkNode {\n");
    sb.append("    nodeId: ").append(toIndentedString(nodeId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    securityLevel: ").append(toIndentedString(securityLevel)).append("\n");
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

