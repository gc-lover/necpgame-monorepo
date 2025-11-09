package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.MatchParticipant;
import com.necpgame.gameplayservice.model.RoleSummary;
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
 * MatchTeam
 */


public class MatchTeam {

  private String teamId;

  private @Nullable Integer averageRating;

  @Valid
  private List<@Valid MatchParticipant> players = new ArrayList<>();

  @Valid
  private List<@Valid RoleSummary> roleSummary = new ArrayList<>();

  private @Nullable Boolean isPartyMixed;

  public MatchTeam() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MatchTeam(String teamId, List<@Valid MatchParticipant> players) {
    this.teamId = teamId;
    this.players = players;
  }

  public MatchTeam teamId(String teamId) {
    this.teamId = teamId;
    return this;
  }

  /**
   * Get teamId
   * @return teamId
   */
  @NotNull 
  @Schema(name = "teamId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("teamId")
  public String getTeamId() {
    return teamId;
  }

  public void setTeamId(String teamId) {
    this.teamId = teamId;
  }

  public MatchTeam averageRating(@Nullable Integer averageRating) {
    this.averageRating = averageRating;
    return this;
  }

  /**
   * Get averageRating
   * @return averageRating
   */
  
  @Schema(name = "averageRating", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("averageRating")
  public @Nullable Integer getAverageRating() {
    return averageRating;
  }

  public void setAverageRating(@Nullable Integer averageRating) {
    this.averageRating = averageRating;
  }

  public MatchTeam players(List<@Valid MatchParticipant> players) {
    this.players = players;
    return this;
  }

  public MatchTeam addPlayersItem(MatchParticipant playersItem) {
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
  @NotNull @Valid 
  @Schema(name = "players", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("players")
  public List<@Valid MatchParticipant> getPlayers() {
    return players;
  }

  public void setPlayers(List<@Valid MatchParticipant> players) {
    this.players = players;
  }

  public MatchTeam roleSummary(List<@Valid RoleSummary> roleSummary) {
    this.roleSummary = roleSummary;
    return this;
  }

  public MatchTeam addRoleSummaryItem(RoleSummary roleSummaryItem) {
    if (this.roleSummary == null) {
      this.roleSummary = new ArrayList<>();
    }
    this.roleSummary.add(roleSummaryItem);
    return this;
  }

  /**
   * Get roleSummary
   * @return roleSummary
   */
  @Valid 
  @Schema(name = "roleSummary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roleSummary")
  public List<@Valid RoleSummary> getRoleSummary() {
    return roleSummary;
  }

  public void setRoleSummary(List<@Valid RoleSummary> roleSummary) {
    this.roleSummary = roleSummary;
  }

  public MatchTeam isPartyMixed(@Nullable Boolean isPartyMixed) {
    this.isPartyMixed = isPartyMixed;
    return this;
  }

  /**
   * Get isPartyMixed
   * @return isPartyMixed
   */
  
  @Schema(name = "isPartyMixed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isPartyMixed")
  public @Nullable Boolean getIsPartyMixed() {
    return isPartyMixed;
  }

  public void setIsPartyMixed(@Nullable Boolean isPartyMixed) {
    this.isPartyMixed = isPartyMixed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchTeam matchTeam = (MatchTeam) o;
    return Objects.equals(this.teamId, matchTeam.teamId) &&
        Objects.equals(this.averageRating, matchTeam.averageRating) &&
        Objects.equals(this.players, matchTeam.players) &&
        Objects.equals(this.roleSummary, matchTeam.roleSummary) &&
        Objects.equals(this.isPartyMixed, matchTeam.isPartyMixed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(teamId, averageRating, players, roleSummary, isPartyMixed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchTeam {\n");
    sb.append("    teamId: ").append(toIndentedString(teamId)).append("\n");
    sb.append("    averageRating: ").append(toIndentedString(averageRating)).append("\n");
    sb.append("    players: ").append(toIndentedString(players)).append("\n");
    sb.append("    roleSummary: ").append(toIndentedString(roleSummary)).append("\n");
    sb.append("    isPartyMixed: ").append(toIndentedString(isPartyMixed)).append("\n");
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

