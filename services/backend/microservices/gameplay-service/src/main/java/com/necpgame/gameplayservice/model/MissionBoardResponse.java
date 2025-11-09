package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.MissionTicket;
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
 * MissionBoardResponse
 */


public class MissionBoardResponse {

  private @Nullable String playerId;

  @Valid
  private List<@Valid MissionTicket> missions = new ArrayList<>();

  public MissionBoardResponse playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public MissionBoardResponse missions(List<@Valid MissionTicket> missions) {
    this.missions = missions;
    return this;
  }

  public MissionBoardResponse addMissionsItem(MissionTicket missionsItem) {
    if (this.missions == null) {
      this.missions = new ArrayList<>();
    }
    this.missions.add(missionsItem);
    return this;
  }

  /**
   * Get missions
   * @return missions
   */
  @Valid 
  @Schema(name = "missions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("missions")
  public List<@Valid MissionTicket> getMissions() {
    return missions;
  }

  public void setMissions(List<@Valid MissionTicket> missions) {
    this.missions = missions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MissionBoardResponse missionBoardResponse = (MissionBoardResponse) o;
    return Objects.equals(this.playerId, missionBoardResponse.playerId) &&
        Objects.equals(this.missions, missionBoardResponse.missions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, missions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MissionBoardResponse {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    missions: ").append(toIndentedString(missions)).append("\n");
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

