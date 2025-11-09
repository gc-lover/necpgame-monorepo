package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.WorldState;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GlobalState
 */


public class GlobalState {

  private @Nullable Integer version;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  private @Nullable WorldState world;

  private @Nullable Integer onlinePlayers;

  private @Nullable Integer activeSessions;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime serverTime;

  public GlobalState version(@Nullable Integer version) {
    this.version = version;
    return this;
  }

  /**
   * Версия состояния для conflict resolution
   * @return version
   */
  
  @Schema(name = "version", description = "Версия состояния для conflict resolution", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("version")
  public @Nullable Integer getVersion() {
    return version;
  }

  public void setVersion(@Nullable Integer version) {
    this.version = version;
  }

  public GlobalState timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public GlobalState world(@Nullable WorldState world) {
    this.world = world;
    return this;
  }

  /**
   * Get world
   * @return world
   */
  @Valid 
  @Schema(name = "world", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("world")
  public @Nullable WorldState getWorld() {
    return world;
  }

  public void setWorld(@Nullable WorldState world) {
    this.world = world;
  }

  public GlobalState onlinePlayers(@Nullable Integer onlinePlayers) {
    this.onlinePlayers = onlinePlayers;
    return this;
  }

  /**
   * Get onlinePlayers
   * @return onlinePlayers
   */
  
  @Schema(name = "online_players", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("online_players")
  public @Nullable Integer getOnlinePlayers() {
    return onlinePlayers;
  }

  public void setOnlinePlayers(@Nullable Integer onlinePlayers) {
    this.onlinePlayers = onlinePlayers;
  }

  public GlobalState activeSessions(@Nullable Integer activeSessions) {
    this.activeSessions = activeSessions;
    return this;
  }

  /**
   * Get activeSessions
   * @return activeSessions
   */
  
  @Schema(name = "active_sessions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_sessions")
  public @Nullable Integer getActiveSessions() {
    return activeSessions;
  }

  public void setActiveSessions(@Nullable Integer activeSessions) {
    this.activeSessions = activeSessions;
  }

  public GlobalState serverTime(@Nullable OffsetDateTime serverTime) {
    this.serverTime = serverTime;
    return this;
  }

  /**
   * Get serverTime
   * @return serverTime
   */
  @Valid 
  @Schema(name = "server_time", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("server_time")
  public @Nullable OffsetDateTime getServerTime() {
    return serverTime;
  }

  public void setServerTime(@Nullable OffsetDateTime serverTime) {
    this.serverTime = serverTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GlobalState globalState = (GlobalState) o;
    return Objects.equals(this.version, globalState.version) &&
        Objects.equals(this.timestamp, globalState.timestamp) &&
        Objects.equals(this.world, globalState.world) &&
        Objects.equals(this.onlinePlayers, globalState.onlinePlayers) &&
        Objects.equals(this.activeSessions, globalState.activeSessions) &&
        Objects.equals(this.serverTime, globalState.serverTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(version, timestamp, world, onlinePlayers, activeSessions, serverTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GlobalState {\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    world: ").append(toIndentedString(world)).append("\n");
    sb.append("    onlinePlayers: ").append(toIndentedString(onlinePlayers)).append("\n");
    sb.append("    activeSessions: ").append(toIndentedString(activeSessions)).append("\n");
    sb.append("    serverTime: ").append(toIndentedString(serverTime)).append("\n");
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

