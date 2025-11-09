package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.LeaderboardRankingsInner;
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
 * Leaderboard
 */


public class Leaderboard {

  /**
   * Gets or Sets rankingType
   */
  public enum RankingTypeEnum {
    OVERALL("overall"),
    
    PVP("pvp"),
    
    PVE("pve"),
    
    WEALTH("wealth"),
    
    FACTION_REPUTATION("faction_reputation"),
    
    KILLS("kills"),
    
    QUESTS("quests");

    private final String value;

    RankingTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static RankingTypeEnum fromValue(String value) {
      for (RankingTypeEnum b : RankingTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RankingTypeEnum rankingType;

  @Valid
  private List<@Valid LeaderboardRankingsInner> rankings = new ArrayList<>();

  private @Nullable Integer totalPlayers;

  public Leaderboard rankingType(@Nullable RankingTypeEnum rankingType) {
    this.rankingType = rankingType;
    return this;
  }

  /**
   * Get rankingType
   * @return rankingType
   */
  
  @Schema(name = "ranking_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ranking_type")
  public @Nullable RankingTypeEnum getRankingType() {
    return rankingType;
  }

  public void setRankingType(@Nullable RankingTypeEnum rankingType) {
    this.rankingType = rankingType;
  }

  public Leaderboard rankings(List<@Valid LeaderboardRankingsInner> rankings) {
    this.rankings = rankings;
    return this;
  }

  public Leaderboard addRankingsItem(LeaderboardRankingsInner rankingsItem) {
    if (this.rankings == null) {
      this.rankings = new ArrayList<>();
    }
    this.rankings.add(rankingsItem);
    return this;
  }

  /**
   * Get rankings
   * @return rankings
   */
  @Valid 
  @Schema(name = "rankings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rankings")
  public List<@Valid LeaderboardRankingsInner> getRankings() {
    return rankings;
  }

  public void setRankings(List<@Valid LeaderboardRankingsInner> rankings) {
    this.rankings = rankings;
  }

  public Leaderboard totalPlayers(@Nullable Integer totalPlayers) {
    this.totalPlayers = totalPlayers;
    return this;
  }

  /**
   * Get totalPlayers
   * @return totalPlayers
   */
  
  @Schema(name = "total_players", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_players")
  public @Nullable Integer getTotalPlayers() {
    return totalPlayers;
  }

  public void setTotalPlayers(@Nullable Integer totalPlayers) {
    this.totalPlayers = totalPlayers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Leaderboard leaderboard = (Leaderboard) o;
    return Objects.equals(this.rankingType, leaderboard.rankingType) &&
        Objects.equals(this.rankings, leaderboard.rankings) &&
        Objects.equals(this.totalPlayers, leaderboard.totalPlayers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rankingType, rankings, totalPlayers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Leaderboard {\n");
    sb.append("    rankingType: ").append(toIndentedString(rankingType)).append("\n");
    sb.append("    rankings: ").append(toIndentedString(rankings)).append("\n");
    sb.append("    totalPlayers: ").append(toIndentedString(totalPlayers)).append("\n");
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

