package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.UUID;
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
 * AdminPlayerView
 */


public class AdminPlayerView {

  private @Nullable UUID playerId;

  private @Nullable String username;

  private @Nullable String email;

  private @Nullable String status;

  private @Nullable Integer level;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastLogin;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  private @Nullable Integer violationsCount;

  public AdminPlayerView playerId(@Nullable UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @Valid 
  @Schema(name = "player_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_id")
  public @Nullable UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable UUID playerId) {
    this.playerId = playerId;
  }

  public AdminPlayerView username(@Nullable String username) {
    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
   */
  
  @Schema(name = "username", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("username")
  public @Nullable String getUsername() {
    return username;
  }

  public void setUsername(@Nullable String username) {
    this.username = username;
  }

  public AdminPlayerView email(@Nullable String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   */
  
  @Schema(name = "email", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public @Nullable String getEmail() {
    return email;
  }

  public void setEmail(@Nullable String email) {
    this.email = email;
  }

  public AdminPlayerView status(@Nullable String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable String getStatus() {
    return status;
  }

  public void setStatus(@Nullable String status) {
    this.status = status;
  }

  public AdminPlayerView level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  
  @Schema(name = "level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public AdminPlayerView lastLogin(@Nullable OffsetDateTime lastLogin) {
    this.lastLogin = lastLogin;
    return this;
  }

  /**
   * Get lastLogin
   * @return lastLogin
   */
  @Valid 
  @Schema(name = "last_login", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_login")
  public @Nullable OffsetDateTime getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(@Nullable OffsetDateTime lastLogin) {
    this.lastLogin = lastLogin;
  }

  public AdminPlayerView createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "created_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_at")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public AdminPlayerView violationsCount(@Nullable Integer violationsCount) {
    this.violationsCount = violationsCount;
    return this;
  }

  /**
   * Get violationsCount
   * @return violationsCount
   */
  
  @Schema(name = "violations_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("violations_count")
  public @Nullable Integer getViolationsCount() {
    return violationsCount;
  }

  public void setViolationsCount(@Nullable Integer violationsCount) {
    this.violationsCount = violationsCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AdminPlayerView adminPlayerView = (AdminPlayerView) o;
    return Objects.equals(this.playerId, adminPlayerView.playerId) &&
        Objects.equals(this.username, adminPlayerView.username) &&
        Objects.equals(this.email, adminPlayerView.email) &&
        Objects.equals(this.status, adminPlayerView.status) &&
        Objects.equals(this.level, adminPlayerView.level) &&
        Objects.equals(this.lastLogin, adminPlayerView.lastLogin) &&
        Objects.equals(this.createdAt, adminPlayerView.createdAt) &&
        Objects.equals(this.violationsCount, adminPlayerView.violationsCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, username, email, status, level, lastLogin, createdAt, violationsCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdminPlayerView {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    lastLogin: ").append(toIndentedString(lastLogin)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    violationsCount: ").append(toIndentedString(violationsCount)).append("\n");
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

