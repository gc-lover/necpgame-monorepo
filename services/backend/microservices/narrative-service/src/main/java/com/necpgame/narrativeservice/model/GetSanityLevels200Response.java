package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.narrativeservice.model.GetSanityLevels200ResponsePlayersInner;
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
 * GetSanityLevels200Response
 */

@JsonTypeName("getSanityLevels_200_response")

public class GetSanityLevels200Response {

  private @Nullable String raidId;

  @Valid
  private List<@Valid GetSanityLevels200ResponsePlayersInner> players = new ArrayList<>();

  public GetSanityLevels200Response raidId(@Nullable String raidId) {
    this.raidId = raidId;
    return this;
  }

  /**
   * Get raidId
   * @return raidId
   */
  
  @Schema(name = "raid_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("raid_id")
  public @Nullable String getRaidId() {
    return raidId;
  }

  public void setRaidId(@Nullable String raidId) {
    this.raidId = raidId;
  }

  public GetSanityLevels200Response players(List<@Valid GetSanityLevels200ResponsePlayersInner> players) {
    this.players = players;
    return this;
  }

  public GetSanityLevels200Response addPlayersItem(GetSanityLevels200ResponsePlayersInner playersItem) {
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
  @Valid 
  @Schema(name = "players", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("players")
  public List<@Valid GetSanityLevels200ResponsePlayersInner> getPlayers() {
    return players;
  }

  public void setPlayers(List<@Valid GetSanityLevels200ResponsePlayersInner> players) {
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
    GetSanityLevels200Response getSanityLevels200Response = (GetSanityLevels200Response) o;
    return Objects.equals(this.raidId, getSanityLevels200Response.raidId) &&
        Objects.equals(this.players, getSanityLevels200Response.players);
  }

  @Override
  public int hashCode() {
    return Objects.hash(raidId, players);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetSanityLevels200Response {\n");
    sb.append("    raidId: ").append(toIndentedString(raidId)).append("\n");
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

