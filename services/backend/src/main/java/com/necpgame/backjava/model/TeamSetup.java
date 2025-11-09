package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.ParticipantSetup;
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
 * TeamSetup
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class TeamSetup {

  private @Nullable String teamId;

  private @Nullable String name;

  @Valid
  private List<@Valid ParticipantSetup> participants = new ArrayList<>();

  public TeamSetup teamId(@Nullable String teamId) {
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

  public TeamSetup name(@Nullable String name) {
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

  public TeamSetup participants(List<@Valid ParticipantSetup> participants) {
    this.participants = participants;
    return this;
  }

  public TeamSetup addParticipantsItem(ParticipantSetup participantsItem) {
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
  public List<@Valid ParticipantSetup> getParticipants() {
    return participants;
  }

  public void setParticipants(List<@Valid ParticipantSetup> participants) {
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
    TeamSetup teamSetup = (TeamSetup) o;
    return Objects.equals(this.teamId, teamSetup.teamId) &&
        Objects.equals(this.name, teamSetup.name) &&
        Objects.equals(this.participants, teamSetup.participants);
  }

  @Override
  public int hashCode() {
    return Objects.hash(teamId, name, participants);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TeamSetup {\n");
    sb.append("    teamId: ").append(toIndentedString(teamId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

