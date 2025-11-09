package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DatabaseStatusReplication
 */

@JsonTypeName("DatabaseStatus_replication")

public class DatabaseStatusReplication {

  private @Nullable Integer lagSeconds;

  private @Nullable Integer replicasCount;

  public DatabaseStatusReplication lagSeconds(@Nullable Integer lagSeconds) {
    this.lagSeconds = lagSeconds;
    return this;
  }

  /**
   * Get lagSeconds
   * @return lagSeconds
   */
  
  @Schema(name = "lag_seconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lag_seconds")
  public @Nullable Integer getLagSeconds() {
    return lagSeconds;
  }

  public void setLagSeconds(@Nullable Integer lagSeconds) {
    this.lagSeconds = lagSeconds;
  }

  public DatabaseStatusReplication replicasCount(@Nullable Integer replicasCount) {
    this.replicasCount = replicasCount;
    return this;
  }

  /**
   * Get replicasCount
   * @return replicasCount
   */
  
  @Schema(name = "replicas_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("replicas_count")
  public @Nullable Integer getReplicasCount() {
    return replicasCount;
  }

  public void setReplicasCount(@Nullable Integer replicasCount) {
    this.replicasCount = replicasCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DatabaseStatusReplication databaseStatusReplication = (DatabaseStatusReplication) o;
    return Objects.equals(this.lagSeconds, databaseStatusReplication.lagSeconds) &&
        Objects.equals(this.replicasCount, databaseStatusReplication.replicasCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lagSeconds, replicasCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DatabaseStatusReplication {\n");
    sb.append("    lagSeconds: ").append(toIndentedString(lagSeconds)).append("\n");
    sb.append("    replicasCount: ").append(toIndentedString(replicasCount)).append("\n");
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

