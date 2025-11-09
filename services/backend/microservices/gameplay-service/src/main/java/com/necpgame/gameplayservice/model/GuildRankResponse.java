package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.GuildLeaderboardEntry;
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
 * GuildRankResponse
 */


public class GuildRankResponse {

  private @Nullable UUID guildId;

  private @Nullable String category;

  private @Nullable Integer rank;

  private @Nullable BigDecimal score;

  private @Nullable Integer totalGuilds;

  @Valid
  private List<@Valid GuildLeaderboardEntry> nearbyGuilds = new ArrayList<>();

  public GuildRankResponse guildId(@Nullable UUID guildId) {
    this.guildId = guildId;
    return this;
  }

  /**
   * Get guildId
   * @return guildId
   */
  @Valid 
  @Schema(name = "guild_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guild_id")
  public @Nullable UUID getGuildId() {
    return guildId;
  }

  public void setGuildId(@Nullable UUID guildId) {
    this.guildId = guildId;
  }

  public GuildRankResponse category(@Nullable String category) {
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

  public GuildRankResponse rank(@Nullable Integer rank) {
    this.rank = rank;
    return this;
  }

  /**
   * Get rank
   * @return rank
   */
  
  @Schema(name = "rank", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rank")
  public @Nullable Integer getRank() {
    return rank;
  }

  public void setRank(@Nullable Integer rank) {
    this.rank = rank;
  }

  public GuildRankResponse score(@Nullable BigDecimal score) {
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

  public GuildRankResponse totalGuilds(@Nullable Integer totalGuilds) {
    this.totalGuilds = totalGuilds;
    return this;
  }

  /**
   * Get totalGuilds
   * @return totalGuilds
   */
  
  @Schema(name = "total_guilds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_guilds")
  public @Nullable Integer getTotalGuilds() {
    return totalGuilds;
  }

  public void setTotalGuilds(@Nullable Integer totalGuilds) {
    this.totalGuilds = totalGuilds;
  }

  public GuildRankResponse nearbyGuilds(List<@Valid GuildLeaderboardEntry> nearbyGuilds) {
    this.nearbyGuilds = nearbyGuilds;
    return this;
  }

  public GuildRankResponse addNearbyGuildsItem(GuildLeaderboardEntry nearbyGuildsItem) {
    if (this.nearbyGuilds == null) {
      this.nearbyGuilds = new ArrayList<>();
    }
    this.nearbyGuilds.add(nearbyGuildsItem);
    return this;
  }

  /**
   * Get nearbyGuilds
   * @return nearbyGuilds
   */
  @Valid 
  @Schema(name = "nearby_guilds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nearby_guilds")
  public List<@Valid GuildLeaderboardEntry> getNearbyGuilds() {
    return nearbyGuilds;
  }

  public void setNearbyGuilds(List<@Valid GuildLeaderboardEntry> nearbyGuilds) {
    this.nearbyGuilds = nearbyGuilds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildRankResponse guildRankResponse = (GuildRankResponse) o;
    return Objects.equals(this.guildId, guildRankResponse.guildId) &&
        Objects.equals(this.category, guildRankResponse.category) &&
        Objects.equals(this.rank, guildRankResponse.rank) &&
        Objects.equals(this.score, guildRankResponse.score) &&
        Objects.equals(this.totalGuilds, guildRankResponse.totalGuilds) &&
        Objects.equals(this.nearbyGuilds, guildRankResponse.nearbyGuilds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(guildId, category, rank, score, totalGuilds, nearbyGuilds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildRankResponse {\n");
    sb.append("    guildId: ").append(toIndentedString(guildId)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    rank: ").append(toIndentedString(rank)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    totalGuilds: ").append(toIndentedString(totalGuilds)).append("\n");
    sb.append("    nearbyGuilds: ").append(toIndentedString(nearbyGuilds)).append("\n");
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

