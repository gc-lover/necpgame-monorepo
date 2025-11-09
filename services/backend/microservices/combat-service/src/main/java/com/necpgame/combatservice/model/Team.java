package com.necpgame.combatservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.combatservice.model.Participant;
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
 * Team
 */


public class Team {

  private @Nullable String teamId;

  private @Nullable String name;

  private @Nullable String side;

  @Valid
  private List<@Valid Participant> participants = new ArrayList<>();

  public Team teamId(@Nullable String teamId) {
    this.teamId = teamId;
    return this;
  }

  /**
   * Get teamId
   * @return teamId
   */
  
  @Schema(name = "teamId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("teamId")
  public @Nullable String getTeamId() {
    return teamId;
  }

  public void setTeamId(@Nullable String teamId) {
    this.teamId = teamId;
  }

  public Team name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public Team side(@Nullable String side) {
    this.side = side;
    return this;
  }

  /**
   * Get side
   * @return side
   */
  
  @Schema(name = "side", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("side")
  public @Nullable String getSide() {
    return side;
  }

  public void setSide(@Nullable String side) {
    this.side = side;
  }

  public Team participants(List<@Valid Participant> participants) {
    this.participants = participants;
    return this;
  }

  public Team addParticipantsItem(Participant participantsItem) {
    if (this.participants == null) {
      this.participants = new ArrayList<>();
    }
    this.participants.add(participantsItem);
    return this;
  }

  /**
   * Get participants
   * @return participants
   */
  @Valid 
  @Schema(name = "participants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participants")
  public List<@Valid Participant> getParticipants() {
    return participants;
  }

  public void setParticipants(List<@Valid Participant> participants) {
    this.participants = participants;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Team team = (Team) o;
    return Objects.equals(this.teamId, team.teamId) &&
        Objects.equals(this.name, team.name) &&
        Objects.equals(this.side, team.side) &&
        Objects.equals(this.participants, team.participants);
  }

  @Override
  public int hashCode() {
    return Objects.hash(teamId, name, side, participants);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Team {\n");
    sb.append("    teamId: ").append(toIndentedString(teamId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    side: ").append(toIndentedString(side)).append("\n");
    sb.append("    participants: ").append(toIndentedString(participants)).append("\n");
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

