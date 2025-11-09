package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.LeaderboardEntry;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * PlayerRankResponse
 */


public class PlayerRankResponse {

  private @Nullable UUID playerId;

  private @Nullable String category;

  private @Nullable Integer rank;

  private @Nullable BigDecimal score;

  private @Nullable Integer totalPlayers;

  private @Nullable Float percentile;

  @Valid
  private List<@Valid LeaderboardEntry> nearbyEntries = new ArrayList<>();

  public PlayerRankResponse playerId(@Nullable UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @Valid 
  @Schema(name = "player_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_id")
  public @Nullable UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable UUID playerId) {
    this.playerId = playerId;
  }

  public PlayerRankResponse category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  public PlayerRankResponse rank(@Nullable Integer rank) {
    this.rank = rank;
    return this;
  }

  /**
   * Get rank
   * @return rank
   */
  
  @Schema(name = "rank", example = "523", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rank")
  public @Nullable Integer getRank() {
    return rank;
  }

  public void setRank(@Nullable Integer rank) {
    this.rank = rank;
  }

  public PlayerRankResponse score(@Nullable BigDecimal score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * @return score
   */
  @Valid 
  @Schema(name = "score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("score")
  public @Nullable BigDecimal getScore() {
    return score;
  }

  public void setScore(@Nullable BigDecimal score) {
    this.score = score;
  }

  public PlayerRankResponse totalPlayers(@Nullable Integer totalPlayers) {
    this.totalPlayers = totalPlayers;
    return this;
  }

  /**
   * Всего игроков в рейтинге
   * @return totalPlayers
   */
  
  @Schema(name = "total_players", description = "Всего игроков в рейтинге", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_players")
  public @Nullable Integer getTotalPlayers() {
    return totalPlayers;
  }

  public void setTotalPlayers(@Nullable Integer totalPlayers) {
    this.totalPlayers = totalPlayers;
  }

  public PlayerRankResponse percentile(@Nullable Float percentile) {
    this.percentile = percentile;
    return this;
  }

  /**
   * Процентиль (топ X%)
   * @return percentile
   */
  
  @Schema(name = "percentile", example = "95.5", description = "Процентиль (топ X%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("percentile")
  public @Nullable Float getPercentile() {
    return percentile;
  }

  public void setPercentile(@Nullable Float percentile) {
    this.percentile = percentile;
  }

  public PlayerRankResponse nearbyEntries(List<@Valid LeaderboardEntry> nearbyEntries) {
    this.nearbyEntries = nearbyEntries;
    return this;
  }

  public PlayerRankResponse addNearbyEntriesItem(LeaderboardEntry nearbyEntriesItem) {
    if (this.nearbyEntries == null) {
      this.nearbyEntries = new ArrayList<>();
    }
    this.nearbyEntries.add(nearbyEntriesItem);
    return this;
  }

  /**
   * Игроки рядом в рейтинге
   * @return nearbyEntries
   */
  @Valid 
  @Schema(name = "nearby_entries", description = "Игроки рядом в рейтинге", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nearby_entries")
  public List<@Valid LeaderboardEntry> getNearbyEntries() {
    return nearbyEntries;
  }

  public void setNearbyEntries(List<@Valid LeaderboardEntry> nearbyEntries) {
    this.nearbyEntries = nearbyEntries;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerRankResponse playerRankResponse = (PlayerRankResponse) o;
    return Objects.equals(this.playerId, playerRankResponse.playerId) &&
        Objects.equals(this.category, playerRankResponse.category) &&
        Objects.equals(this.rank, playerRankResponse.rank) &&
        Objects.equals(this.score, playerRankResponse.score) &&
        Objects.equals(this.totalPlayers, playerRankResponse.totalPlayers) &&
        Objects.equals(this.percentile, playerRankResponse.percentile) &&
        Objects.equals(this.nearbyEntries, playerRankResponse.nearbyEntries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, category, rank, score, totalPlayers, percentile, nearbyEntries);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerRankResponse {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    rank: ").append(toIndentedString(rank)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    totalPlayers: ").append(toIndentedString(totalPlayers)).append("\n");
    sb.append("    percentile: ").append(toIndentedString(percentile)).append("\n");
    sb.append("    nearbyEntries: ").append(toIndentedString(nearbyEntries)).append("\n");
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

