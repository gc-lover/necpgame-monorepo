package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlacementRequest
 */


public class PlacementRequest {

  private UUID playerId;

  private Integer totalGames;

  private @Nullable Integer wins;

  private @Nullable Integer losses;

  private @Nullable Integer draws;

  public PlacementRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlacementRequest(UUID playerId, Integer totalGames) {
    this.playerId = playerId;
    this.totalGames = totalGames;
  }

  public PlacementRequest playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public PlacementRequest totalGames(Integer totalGames) {
    this.totalGames = totalGames;
    return this;
  }

  /**
   * Get totalGames
   * minimum: 0
   * @return totalGames
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "totalGames", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("totalGames")
  public Integer getTotalGames() {
    return totalGames;
  }

  public void setTotalGames(Integer totalGames) {
    this.totalGames = totalGames;
  }

  public PlacementRequest wins(@Nullable Integer wins) {
    this.wins = wins;
    return this;
  }

  /**
   * Get wins
   * minimum: 0
   * @return wins
   */
  @Min(value = 0) 
  @Schema(name = "wins", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("wins")
  public @Nullable Integer getWins() {
    return wins;
  }

  public void setWins(@Nullable Integer wins) {
    this.wins = wins;
  }

  public PlacementRequest losses(@Nullable Integer losses) {
    this.losses = losses;
    return this;
  }

  /**
   * Get losses
   * minimum: 0
   * @return losses
   */
  @Min(value = 0) 
  @Schema(name = "losses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("losses")
  public @Nullable Integer getLosses() {
    return losses;
  }

  public void setLosses(@Nullable Integer losses) {
    this.losses = losses;
  }

  public PlacementRequest draws(@Nullable Integer draws) {
    this.draws = draws;
    return this;
  }

  /**
   * Get draws
   * minimum: 0
   * @return draws
   */
  @Min(value = 0) 
  @Schema(name = "draws", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("draws")
  public @Nullable Integer getDraws() {
    return draws;
  }

  public void setDraws(@Nullable Integer draws) {
    this.draws = draws;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlacementRequest placementRequest = (PlacementRequest) o;
    return Objects.equals(this.playerId, placementRequest.playerId) &&
        Objects.equals(this.totalGames, placementRequest.totalGames) &&
        Objects.equals(this.wins, placementRequest.wins) &&
        Objects.equals(this.losses, placementRequest.losses) &&
        Objects.equals(this.draws, placementRequest.draws);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, totalGames, wins, losses, draws);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlacementRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    totalGames: ").append(toIndentedString(totalGames)).append("\n");
    sb.append("    wins: ").append(toIndentedString(wins)).append("\n");
    sb.append("    losses: ").append(toIndentedString(losses)).append("\n");
    sb.append("    draws: ").append(toIndentedString(draws)).append("\n");
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

