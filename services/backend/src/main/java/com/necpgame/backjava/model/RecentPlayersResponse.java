package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.RecentPlayer;
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
 * RecentPlayersResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class RecentPlayersResponse {

  @Valid
  private List<@Valid RecentPlayer> players = new ArrayList<>();

  public RecentPlayersResponse players(List<@Valid RecentPlayer> players) {
    this.players = players;
    return this;
  }

  public RecentPlayersResponse addPlayersItem(RecentPlayer playersItem) {
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
  public List<@Valid RecentPlayer> getPlayers() {
    return players;
  }

  public void setPlayers(List<@Valid RecentPlayer> players) {
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
    RecentPlayersResponse recentPlayersResponse = (RecentPlayersResponse) o;
    return Objects.equals(this.players, recentPlayersResponse.players);
  }

  @Override
  public int hashCode() {
    return Objects.hash(players);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RecentPlayersResponse {\n");
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

