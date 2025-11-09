package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.DatabaseStatusConnections;
import com.necpgame.adminservice.model.DatabaseStatusReplication;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DatabaseStatus
 */


public class DatabaseStatus {

  private @Nullable String dbName;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    UP("up"),
    
    DEGRADED("degraded"),
    
    DOWN("down");

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

  private @Nullable DatabaseStatusConnections connections;

  private @Nullable DatabaseStatusReplication replication;

  public DatabaseStatus dbName(@Nullable String dbName) {
    this.dbName = dbName;
    return this;
  }

  /**
   * Get dbName
   * @return dbName
   */
  
  @Schema(name = "db_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("db_name")
  public @Nullable String getDbName() {
    return dbName;
  }

  public void setDbName(@Nullable String dbName) {
    this.dbName = dbName;
  }

  public DatabaseStatus status(@Nullable StatusEnum status) {
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

  public DatabaseStatus connections(@Nullable DatabaseStatusConnections connections) {
    this.connections = connections;
    return this;
  }

  /**
   * Get connections
   * @return connections
   */
  @Valid 
  @Schema(name = "connections", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("connections")
  public @Nullable DatabaseStatusConnections getConnections() {
    return connections;
  }

  public void setConnections(@Nullable DatabaseStatusConnections connections) {
    this.connections = connections;
  }

  public DatabaseStatus replication(@Nullable DatabaseStatusReplication replication) {
    this.replication = replication;
    return this;
  }

  /**
   * Get replication
   * @return replication
   */
  @Valid 
  @Schema(name = "replication", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("replication")
  public @Nullable DatabaseStatusReplication getReplication() {
    return replication;
  }

  public void setReplication(@Nullable DatabaseStatusReplication replication) {
    this.replication = replication;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DatabaseStatus databaseStatus = (DatabaseStatus) o;
    return Objects.equals(this.dbName, databaseStatus.dbName) &&
        Objects.equals(this.status, databaseStatus.status) &&
        Objects.equals(this.connections, databaseStatus.connections) &&
        Objects.equals(this.replication, databaseStatus.replication);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dbName, status, connections, replication);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DatabaseStatus {\n");
    sb.append("    dbName: ").append(toIndentedString(dbName)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    connections: ").append(toIndentedString(connections)).append("\n");
    sb.append("    replication: ").append(toIndentedString(replication)).append("\n");
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

