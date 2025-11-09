package com.necpgame.combatservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.combatservice.model.Team;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * CombatSession
 */


public class CombatSession {

  private @Nullable String sessionId;

  /**
   * Gets or Sets mode
   */
  public enum ModeEnum {
    PVE("PVE"),
    
    PVP("PVP"),
    
    RAID("RAID"),
    
    DUEL("DUEL"),
    
    SIMULATION("SIMULATION");

    private final String value;

    ModeEnum(String value) {
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
    public static ModeEnum fromValue(String value) {
      for (ModeEnum b : ModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ModeEnum mode;

  private @Nullable String map;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("PENDING"),
    
    ACTIVE("ACTIVE"),
    
    COMPLETED("COMPLETED"),
    
    ABORTED("ABORTED");

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

  @Valid
  private Map<String, Object> rules = new HashMap<>();

  @Valid
  private Map<String, Object> settings = new HashMap<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startTime;

  private @Nullable String createdBy;

  @Valid
  private List<@Valid Team> teams = new ArrayList<>();

  public CombatSession sessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  
  @Schema(name = "sessionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sessionId")
  public @Nullable String getSessionId() {
    return sessionId;
  }

  public void setSessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
  }

  public CombatSession mode(@Nullable ModeEnum mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Get mode
   * @return mode
   */
  
  @Schema(name = "mode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mode")
  public @Nullable ModeEnum getMode() {
    return mode;
  }

  public void setMode(@Nullable ModeEnum mode) {
    this.mode = mode;
  }

  public CombatSession map(@Nullable String map) {
    this.map = map;
    return this;
  }

  /**
   * Get map
   * @return map
   */
  
  @Schema(name = "map", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("map")
  public @Nullable String getMap() {
    return map;
  }

  public void setMap(@Nullable String map) {
    this.map = map;
  }

  public CombatSession status(@Nullable StatusEnum status) {
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

  public CombatSession rules(Map<String, Object> rules) {
    this.rules = rules;
    return this;
  }

  public CombatSession putRulesItem(String key, Object rulesItem) {
    if (this.rules == null) {
      this.rules = new HashMap<>();
    }
    this.rules.put(key, rulesItem);
    return this;
  }

  /**
   * Get rules
   * @return rules
   */
  
  @Schema(name = "rules", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rules")
  public Map<String, Object> getRules() {
    return rules;
  }

  public void setRules(Map<String, Object> rules) {
    this.rules = rules;
  }

  public CombatSession settings(Map<String, Object> settings) {
    this.settings = settings;
    return this;
  }

  public CombatSession putSettingsItem(String key, Object settingsItem) {
    if (this.settings == null) {
      this.settings = new HashMap<>();
    }
    this.settings.put(key, settingsItem);
    return this;
  }

  /**
   * Get settings
   * @return settings
   */
  
  @Schema(name = "settings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("settings")
  public Map<String, Object> getSettings() {
    return settings;
  }

  public void setSettings(Map<String, Object> settings) {
    this.settings = settings;
  }

  public CombatSession startTime(@Nullable OffsetDateTime startTime) {
    this.startTime = startTime;
    return this;
  }

  /**
   * Get startTime
   * @return startTime
   */
  @Valid 
  @Schema(name = "startTime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startTime")
  public @Nullable OffsetDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(@Nullable OffsetDateTime startTime) {
    this.startTime = startTime;
  }

  public CombatSession createdBy(@Nullable String createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  /**
   * Get createdBy
   * @return createdBy
   */
  
  @Schema(name = "createdBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdBy")
  public @Nullable String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(@Nullable String createdBy) {
    this.createdBy = createdBy;
  }

  public CombatSession teams(List<@Valid Team> teams) {
    this.teams = teams;
    return this;
  }

  public CombatSession addTeamsItem(Team teamsItem) {
    if (this.teams == null) {
      this.teams = new ArrayList<>();
    }
    this.teams.add(teamsItem);
    return this;
  }

  /**
   * Get teams
   * @return teams
   */
  @Valid 
  @Schema(name = "teams", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("teams")
  public List<@Valid Team> getTeams() {
    return teams;
  }

  public void setTeams(List<@Valid Team> teams) {
    this.teams = teams;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CombatSession combatSession = (CombatSession) o;
    return Objects.equals(this.sessionId, combatSession.sessionId) &&
        Objects.equals(this.mode, combatSession.mode) &&
        Objects.equals(this.map, combatSession.map) &&
        Objects.equals(this.status, combatSession.status) &&
        Objects.equals(this.rules, combatSession.rules) &&
        Objects.equals(this.settings, combatSession.settings) &&
        Objects.equals(this.startTime, combatSession.startTime) &&
        Objects.equals(this.createdBy, combatSession.createdBy) &&
        Objects.equals(this.teams, combatSession.teams);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, mode, map, status, rules, settings, startTime, createdBy, teams);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CombatSession {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    map: ").append(toIndentedString(map)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    rules: ").append(toIndentedString(rules)).append("\n");
    sb.append("    settings: ").append(toIndentedString(settings)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    teams: ").append(toIndentedString(teams)).append("\n");
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

