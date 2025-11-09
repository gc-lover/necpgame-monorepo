package com.necpgame.realtimeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.realtimeservice.model.CellPlayerState;
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
 * ZoneCellSnapshot
 */


public class ZoneCellSnapshot {

  private String cellKey;

  @Valid
  private List<@Valid CellPlayerState> players = new ArrayList<>();

  public ZoneCellSnapshot() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ZoneCellSnapshot(String cellKey, List<@Valid CellPlayerState> players) {
    this.cellKey = cellKey;
    this.players = players;
  }

  public ZoneCellSnapshot cellKey(String cellKey) {
    this.cellKey = cellKey;
    return this;
  }

  /**
   * Get cellKey
   * @return cellKey
   */
  @NotNull 
  @Schema(name = "cellKey", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cellKey")
  public String getCellKey() {
    return cellKey;
  }

  public void setCellKey(String cellKey) {
    this.cellKey = cellKey;
  }

  public ZoneCellSnapshot players(List<@Valid CellPlayerState> players) {
    this.players = players;
    return this;
  }

  public ZoneCellSnapshot addPlayersItem(CellPlayerState playersItem) {
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
  public List<@Valid CellPlayerState> getPlayers() {
    return players;
  }

  public void setPlayers(List<@Valid CellPlayerState> players) {
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
    ZoneCellSnapshot zoneCellSnapshot = (ZoneCellSnapshot) o;
    return Objects.equals(this.cellKey, zoneCellSnapshot.cellKey) &&
        Objects.equals(this.players, zoneCellSnapshot.players);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cellKey, players);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ZoneCellSnapshot {\n");
    sb.append("    cellKey: ").append(toIndentedString(cellKey)).append("\n");
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

