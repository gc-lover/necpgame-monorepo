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
 * DatabaseStatusConnections
 */

@JsonTypeName("DatabaseStatus_connections")

public class DatabaseStatusConnections {

  private @Nullable Integer active;

  private @Nullable Integer idle;

  private @Nullable Integer max;

  public DatabaseStatusConnections active(@Nullable Integer active) {
    this.active = active;
    return this;
  }

  /**
   * Get active
   * @return active
   */
  
  @Schema(name = "active", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active")
  public @Nullable Integer getActive() {
    return active;
  }

  public void setActive(@Nullable Integer active) {
    this.active = active;
  }

  public DatabaseStatusConnections idle(@Nullable Integer idle) {
    this.idle = idle;
    return this;
  }

  /**
   * Get idle
   * @return idle
   */
  
  @Schema(name = "idle", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("idle")
  public @Nullable Integer getIdle() {
    return idle;
  }

  public void setIdle(@Nullable Integer idle) {
    this.idle = idle;
  }

  public DatabaseStatusConnections max(@Nullable Integer max) {
    this.max = max;
    return this;
  }

  /**
   * Get max
   * @return max
   */
  
  @Schema(name = "max", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max")
  public @Nullable Integer getMax() {
    return max;
  }

  public void setMax(@Nullable Integer max) {
    this.max = max;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DatabaseStatusConnections databaseStatusConnections = (DatabaseStatusConnections) o;
    return Objects.equals(this.active, databaseStatusConnections.active) &&
        Objects.equals(this.idle, databaseStatusConnections.idle) &&
        Objects.equals(this.max, databaseStatusConnections.max);
  }

  @Override
  public int hashCode() {
    return Objects.hash(active, idle, max);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DatabaseStatusConnections {\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    idle: ").append(toIndentedString(idle)).append("\n");
    sb.append("    max: ").append(toIndentedString(max)).append("\n");
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

