package com.necpgame.combatservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.combatservice.model.TeamSetup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CombatSessionCreateRequest
 */


public class CombatSessionCreateRequest {

  private String mode;

  private String map;

  @Valid
  private Map<String, Object> rules = new HashMap<>();

  @Valid
  private Map<String, Object> settings = new HashMap<>();

  @Valid
  private List<@Valid TeamSetup> teams = new ArrayList<>();

  private @Nullable Boolean allowLateJoin;

  private @Nullable Integer maxDurationSeconds;

  private @Nullable String telemetryId;

  public CombatSessionCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CombatSessionCreateRequest(String mode, String map, List<@Valid TeamSetup> teams) {
    this.mode = mode;
    this.map = map;
    this.teams = teams;
  }

  public CombatSessionCreateRequest mode(String mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Get mode
   * @return mode
   */
  @NotNull 
  @Schema(name = "mode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mode")
  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public CombatSessionCreateRequest map(String map) {
    this.map = map;
    return this;
  }

  /**
   * Get map
   * @return map
   */
  @NotNull 
  @Schema(name = "map", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("map")
  public String getMap() {
    return map;
  }

  public void setMap(String map) {
    this.map = map;
  }

  public CombatSessionCreateRequest rules(Map<String, Object> rules) {
    this.rules = rules;
    return this;
  }

  public CombatSessionCreateRequest putRulesItem(String key, Object rulesItem) {
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

  public CombatSessionCreateRequest settings(Map<String, Object> settings) {
    this.settings = settings;
    return this;
  }

  public CombatSessionCreateRequest putSettingsItem(String key, Object settingsItem) {
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

  public CombatSessionCreateRequest teams(List<@Valid TeamSetup> teams) {
    this.teams = teams;
    return this;
  }

  public CombatSessionCreateRequest addTeamsItem(TeamSetup teamsItem) {
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
  @NotNull @Valid 
  @Schema(name = "teams", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("teams")
  public List<@Valid TeamSetup> getTeams() {
    return teams;
  }

  public void setTeams(List<@Valid TeamSetup> teams) {
    this.teams = teams;
  }

  public CombatSessionCreateRequest allowLateJoin(@Nullable Boolean allowLateJoin) {
    this.allowLateJoin = allowLateJoin;
    return this;
  }

  /**
   * Get allowLateJoin
   * @return allowLateJoin
   */
  
  @Schema(name = "allowLateJoin", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowLateJoin")
  public @Nullable Boolean getAllowLateJoin() {
    return allowLateJoin;
  }

  public void setAllowLateJoin(@Nullable Boolean allowLateJoin) {
    this.allowLateJoin = allowLateJoin;
  }

  public CombatSessionCreateRequest maxDurationSeconds(@Nullable Integer maxDurationSeconds) {
    this.maxDurationSeconds = maxDurationSeconds;
    return this;
  }

  /**
   * Get maxDurationSeconds
   * @return maxDurationSeconds
   */
  
  @Schema(name = "maxDurationSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxDurationSeconds")
  public @Nullable Integer getMaxDurationSeconds() {
    return maxDurationSeconds;
  }

  public void setMaxDurationSeconds(@Nullable Integer maxDurationSeconds) {
    this.maxDurationSeconds = maxDurationSeconds;
  }

  public CombatSessionCreateRequest telemetryId(@Nullable String telemetryId) {
    this.telemetryId = telemetryId;
    return this;
  }

  /**
   * Get telemetryId
   * @return telemetryId
   */
  
  @Schema(name = "telemetryId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("telemetryId")
  public @Nullable String getTelemetryId() {
    return telemetryId;
  }

  public void setTelemetryId(@Nullable String telemetryId) {
    this.telemetryId = telemetryId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CombatSessionCreateRequest combatSessionCreateRequest = (CombatSessionCreateRequest) o;
    return Objects.equals(this.mode, combatSessionCreateRequest.mode) &&
        Objects.equals(this.map, combatSessionCreateRequest.map) &&
        Objects.equals(this.rules, combatSessionCreateRequest.rules) &&
        Objects.equals(this.settings, combatSessionCreateRequest.settings) &&
        Objects.equals(this.teams, combatSessionCreateRequest.teams) &&
        Objects.equals(this.allowLateJoin, combatSessionCreateRequest.allowLateJoin) &&
        Objects.equals(this.maxDurationSeconds, combatSessionCreateRequest.maxDurationSeconds) &&
        Objects.equals(this.telemetryId, combatSessionCreateRequest.telemetryId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mode, map, rules, settings, teams, allowLateJoin, maxDurationSeconds, telemetryId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CombatSessionCreateRequest {\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    map: ").append(toIndentedString(map)).append("\n");
    sb.append("    rules: ").append(toIndentedString(rules)).append("\n");
    sb.append("    settings: ").append(toIndentedString(settings)).append("\n");
    sb.append("    teams: ").append(toIndentedString(teams)).append("\n");
    sb.append("    allowLateJoin: ").append(toIndentedString(allowLateJoin)).append("\n");
    sb.append("    maxDurationSeconds: ").append(toIndentedString(maxDurationSeconds)).append("\n");
    sb.append("    telemetryId: ").append(toIndentedString(telemetryId)).append("\n");
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

