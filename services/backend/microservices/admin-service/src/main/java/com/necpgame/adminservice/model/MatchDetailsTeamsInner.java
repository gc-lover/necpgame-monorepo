package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * MatchDetailsTeamsInner
 */

@JsonTypeName("MatchDetails_teams_inner")

public class MatchDetailsTeamsInner {

  private @Nullable String teamId;

  @Valid
  private List<Object> players = new ArrayList<>();

  public MatchDetailsTeamsInner teamId(@Nullable String teamId) {
    this.teamId = teamId;
    return this;
  }

  /**
   * Get teamId
   * @return teamId
   */
  
  @Schema(name = "team_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("team_id")
  public @Nullable String getTeamId() {
    return teamId;
  }

  public void setTeamId(@Nullable String teamId) {
    this.teamId = teamId;
  }

  public MatchDetailsTeamsInner players(List<Object> players) {
    this.players = players;
    return this;
  }

  public MatchDetailsTeamsInner addPlayersItem(Object playersItem) {
    if (this.players == null) {
      this.players = new ArrayList<>();
    }
    this.players.add(playersItem);
    return this;
  }

  /**
   * Get players
   * @return players
   */
  
  @Schema(name = "players", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("players")
  public List<Object> getPlayers() {
    return players;
  }

  public void setPlayers(List<Object> players) {
    this.players = players;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchDetailsTeamsInner matchDetailsTeamsInner = (MatchDetailsTeamsInner) o;
    return Objects.equals(this.teamId, matchDetailsTeamsInner.teamId) &&
        Objects.equals(this.players, matchDetailsTeamsInner.players);
  }

  @Override
  public int hashCode() {
    return Objects.hash(teamId, players);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchDetailsTeamsInner {\n");
    sb.append("    teamId: ").append(toIndentedString(teamId)).append("\n");
    sb.append("    players: ").append(toIndentedString(players)).append("\n");
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

