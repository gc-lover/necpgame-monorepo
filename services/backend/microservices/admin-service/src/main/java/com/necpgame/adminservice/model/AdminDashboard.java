package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.AdminDashboardServerHealth;
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
 * AdminDashboard
 */


public class AdminDashboard {

  private @Nullable Integer onlinePlayers;

  private @Nullable Integer totalPlayers;

  private @Nullable Integer activeSessions;

  private @Nullable Integer pendingReports;

  private @Nullable AdminDashboardServerHealth serverHealth;

  @Valid
  private List<Object> recentActivity = new ArrayList<>();

  public AdminDashboard onlinePlayers(@Nullable Integer onlinePlayers) {
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

  public AdminDashboard totalPlayers(@Nullable Integer totalPlayers) {
    this.totalPlayers = totalPlayers;
    return this;
  }

  /**
   * Get totalPlayers
   * @return totalPlayers
   */
  
  @Schema(name = "total_players", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_players")
  public @Nullable Integer getTotalPlayers() {
    return totalPlayers;
  }

  public void setTotalPlayers(@Nullable Integer totalPlayers) {
    this.totalPlayers = totalPlayers;
  }

  public AdminDashboard activeSessions(@Nullable Integer activeSessions) {
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

  public AdminDashboard pendingReports(@Nullable Integer pendingReports) {
    this.pendingReports = pendingReports;
    return this;
  }

  /**
   * Get pendingReports
   * @return pendingReports
   */
  
  @Schema(name = "pending_reports", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pending_reports")
  public @Nullable Integer getPendingReports() {
    return pendingReports;
  }

  public void setPendingReports(@Nullable Integer pendingReports) {
    this.pendingReports = pendingReports;
  }

  public AdminDashboard serverHealth(@Nullable AdminDashboardServerHealth serverHealth) {
    this.serverHealth = serverHealth;
    return this;
  }

  /**
   * Get serverHealth
   * @return serverHealth
   */
  @Valid 
  @Schema(name = "server_health", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("server_health")
  public @Nullable AdminDashboardServerHealth getServerHealth() {
    return serverHealth;
  }

  public void setServerHealth(@Nullable AdminDashboardServerHealth serverHealth) {
    this.serverHealth = serverHealth;
  }

  public AdminDashboard recentActivity(List<Object> recentActivity) {
    this.recentActivity = recentActivity;
    return this;
  }

  public AdminDashboard addRecentActivityItem(Object recentActivityItem) {
    if (this.recentActivity == null) {
      this.recentActivity = new ArrayList<>();
    }
    this.recentActivity.add(recentActivityItem);
    return this;
  }

  /**
   * Get recentActivity
   * @return recentActivity
   */
  
  @Schema(name = "recent_activity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recent_activity")
  public List<Object> getRecentActivity() {
    return recentActivity;
  }

  public void setRecentActivity(List<Object> recentActivity) {
    this.recentActivity = recentActivity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AdminDashboard adminDashboard = (AdminDashboard) o;
    return Objects.equals(this.onlinePlayers, adminDashboard.onlinePlayers) &&
        Objects.equals(this.totalPlayers, adminDashboard.totalPlayers) &&
        Objects.equals(this.activeSessions, adminDashboard.activeSessions) &&
        Objects.equals(this.pendingReports, adminDashboard.pendingReports) &&
        Objects.equals(this.serverHealth, adminDashboard.serverHealth) &&
        Objects.equals(this.recentActivity, adminDashboard.recentActivity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(onlinePlayers, totalPlayers, activeSessions, pendingReports, serverHealth, recentActivity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdminDashboard {\n");
    sb.append("    onlinePlayers: ").append(toIndentedString(onlinePlayers)).append("\n");
    sb.append("    totalPlayers: ").append(toIndentedString(totalPlayers)).append("\n");
    sb.append("    activeSessions: ").append(toIndentedString(activeSessions)).append("\n");
    sb.append("    pendingReports: ").append(toIndentedString(pendingReports)).append("\n");
    sb.append("    serverHealth: ").append(toIndentedString(serverHealth)).append("\n");
    sb.append("    recentActivity: ").append(toIndentedString(recentActivity)).append("\n");
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

