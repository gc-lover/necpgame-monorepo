package com.necpgame.backjava.model;

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
 * GetMVPHealth200ResponseSystems
 */

@JsonTypeName("getMVPHealth_200_response_systems")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetMVPHealth200ResponseSystems {

  private @Nullable String auth;

  private @Nullable String playerManagement;

  private @Nullable String questEngine;

  private @Nullable String combatSession;

  private @Nullable String progression;

  public GetMVPHealth200ResponseSystems auth(@Nullable String auth) {
    this.auth = auth;
    return this;
  }

  /**
   * Get auth
   * @return auth
   */
  
  @Schema(name = "auth", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("auth")
  public @Nullable String getAuth() {
    return auth;
  }

  public void setAuth(@Nullable String auth) {
    this.auth = auth;
  }

  public GetMVPHealth200ResponseSystems playerManagement(@Nullable String playerManagement) {
    this.playerManagement = playerManagement;
    return this;
  }

  /**
   * Get playerManagement
   * @return playerManagement
   */
  
  @Schema(name = "player_management", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_management")
  public @Nullable String getPlayerManagement() {
    return playerManagement;
  }

  public void setPlayerManagement(@Nullable String playerManagement) {
    this.playerManagement = playerManagement;
  }

  public GetMVPHealth200ResponseSystems questEngine(@Nullable String questEngine) {
    this.questEngine = questEngine;
    return this;
  }

  /**
   * Get questEngine
   * @return questEngine
   */
  
  @Schema(name = "quest_engine", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_engine")
  public @Nullable String getQuestEngine() {
    return questEngine;
  }

  public void setQuestEngine(@Nullable String questEngine) {
    this.questEngine = questEngine;
  }

  public GetMVPHealth200ResponseSystems combatSession(@Nullable String combatSession) {
    this.combatSession = combatSession;
    return this;
  }

  /**
   * Get combatSession
   * @return combatSession
   */
  
  @Schema(name = "combat_session", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("combat_session")
  public @Nullable String getCombatSession() {
    return combatSession;
  }

  public void setCombatSession(@Nullable String combatSession) {
    this.combatSession = combatSession;
  }

  public GetMVPHealth200ResponseSystems progression(@Nullable String progression) {
    this.progression = progression;
    return this;
  }

  /**
   * Get progression
   * @return progression
   */
  
  @Schema(name = "progression", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progression")
  public @Nullable String getProgression() {
    return progression;
  }

  public void setProgression(@Nullable String progression) {
    this.progression = progression;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetMVPHealth200ResponseSystems getMVPHealth200ResponseSystems = (GetMVPHealth200ResponseSystems) o;
    return Objects.equals(this.auth, getMVPHealth200ResponseSystems.auth) &&
        Objects.equals(this.playerManagement, getMVPHealth200ResponseSystems.playerManagement) &&
        Objects.equals(this.questEngine, getMVPHealth200ResponseSystems.questEngine) &&
        Objects.equals(this.combatSession, getMVPHealth200ResponseSystems.combatSession) &&
        Objects.equals(this.progression, getMVPHealth200ResponseSystems.progression);
  }

  @Override
  public int hashCode() {
    return Objects.hash(auth, playerManagement, questEngine, combatSession, progression);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetMVPHealth200ResponseSystems {\n");
    sb.append("    auth: ").append(toIndentedString(auth)).append("\n");
    sb.append("    playerManagement: ").append(toIndentedString(playerManagement)).append("\n");
    sb.append("    questEngine: ").append(toIndentedString(questEngine)).append("\n");
    sb.append("    combatSession: ").append(toIndentedString(combatSession)).append("\n");
    sb.append("    progression: ").append(toIndentedString(progression)).append("\n");
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

